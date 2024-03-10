/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fosd.ui.wizards;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.tools.DimacsStringSolver;

import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.provider.PropertydefinitionsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.provider.PropertyinstancesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.categories.provider.DVLMCategoriesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.concepts.provider.ConceptsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.general.provider.GeneralItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.provider.DVLMItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.roles.provider.RolesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.provider.DVLMStructuralItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.units.provider.UnitsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.extension.fosd.model.CrossTreeConstraint;
import de.dlr.sc.virsat.model.extension.fosd.model.FeatureTree;
import de.dlr.sc.virsat.model.extension.fosd.model.OptionalRelationship;
import de.dlr.sc.virsat.model.extension.fosd.model.SubFeatureRelationship;
import de.dlr.sc.virsat.model.extension.fosd.util.ProductStructureHelper;
import de.dlr.sc.virsat.model.extension.ps.model.ConfigurationTree;
import de.dlr.sc.virsat.project.editingDomain.VirSatEditingDomainRegistry;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.project.resources.VirSatProjectResource;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;
import de.dlr.sc.virsat.project.ui.contentProvider.VirSatComposedContentProvider;
import de.dlr.sc.virsat.project.ui.contentProvider.VirSatFilteredWrappedTreeContentProvider;
import de.dlr.sc.virsat.project.ui.labelProvider.VirSatComposedLabelProvider;
import de.dlr.sc.virsat.project.ui.navigator.commonSorter.VirSatNavigatorSeiSorter;
import de.dlr.sc.virsat.project.ui.navigator.contentProvider.VirSatProjectContentProvider;
import de.dlr.sc.virsat.project.ui.navigator.contentProvider.VirSatWorkspaceContentProvider;
import de.dlr.sc.virsat.project.ui.navigator.labelProvider.VirSatProjectLabelProvider;
import de.dlr.sc.virsat.project.ui.navigator.labelProvider.VirSatWorkspaceLabelProvider;
/**
 * Page to create a new configuration tree from a product tree or create a new assembly tree from a configuration tree
 * @author bell_er
 *
 */

public class VariantSelectionPage extends WizardPage {
	
	private ISelection selection;
	private ISelection preSelect;
	private VirSatTransactionalEditingDomain ed;
	private Repository rep;
	private Composite content;
	private StructuralElementInstance sc;
	private StructuralElementInstance sei;
	private boolean generateAsConfigurationTree;
	private Text treeName;
	private Tree tree;
	private TreeEditor editor;
	/**
	 * Create a new Generate page
	 * @param preSelect the initial selection to be used as a model
	 */
	
	protected VariantSelectionPage(ISelection preSelect) {
		
		super("");
		sc = (StructuralElementInstance) ((IStructuredSelection) preSelect).getFirstElement();
		if (sc.getType().getName().equals(FeatureTree.class.getSimpleName())) {
			setTitle("Generate the Configuration Tree");
			generateAsConfigurationTree = true;
		}
		setDescription("Right click on an element to rename or duplicate");
		this.preSelect = preSelect;
		
	}

	@Override
	public void createControl(Composite parent) {
		
		/*
		 * If ConfigTree should be generate, make the variant selection and set the selected
		 * variant as the new preselection.
		 */
		
		if (generateAsConfigurationTree) {
			this.getAllValidVariants(sc);
		}
		
		content = new Composite(parent, SWT.NONE);
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		content.setLayout(glContent);
		content.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));	
		
		createTreeUI();
	    setControl(content);
	    
	    ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	    adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DVLMItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DVLMStructuralItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new GeneralItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ConceptsItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new RolesItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new UnitsItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new DVLMCategoriesItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PropertydefinitionsItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PropertyinstancesItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	    
	    Composite composite = new Composite(content, SWT.NONE);
	    composite.setLayout(new GridLayout(2, false));
	    Button btnRename = new Button(composite, SWT.NONE);
	    btnRename.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		// Identify the selected row
	    		TreeItem[] items = tree.getSelection();

	    		if (items[0] == null) {
	    			return;
	    		}
	    		// The control that will be the editor must be a child of the Tree
	    		Text newEditor = new Text(tree, SWT.NONE);
	    		StructuralElementInstance temp = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
	    		newEditor.setText(temp.getName());
	    		newEditor.addModifyListener(new ModifyListener() {
	    			public void modifyText(ModifyEvent e) {
	    				Text text = (Text) editor.getEditor();
	    				editor.getItem().setText(text.getText());
	    				StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
	    				sc.setName(text.getText());
	    			}
	    		});
	    		newEditor.selectAll();
	    		newEditor.setFocus();
	    		editor.setEditor(newEditor, items[0]);
	    	}
	    });
	    btnRename.setText("Rename");
	    Button btnDuplicate = new Button(composite, SWT.NONE);
	    btnDuplicate.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
	    		ProductStructureHelper.duplicate(sc);
	    	}
	    });
	    btnDuplicate.setText("Duplicate");
	    new Label(content, SWT.NONE);
	    Label lblTreeName = new Label(content, SWT.NONE);
	    lblTreeName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    treeName = new Text(content, SWT.BORDER);
	    treeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	  
	 
	    if (generateAsConfigurationTree) {
	    	lblTreeName.setText("Configuration Tree Name");
	    	treeName.setText(ConfigurationTree.class.getSimpleName());
		} else {
			lblTreeName.setText("Feature Tree Name");
			treeName.setText(FeatureTree.class.getSimpleName());
		}
	   
	}
	
	/**
	 * Create the dialog for renaming and duplicating items
	 */
	private void createTreeUI() {
			
		TreeViewer treeViewer =  new TreeViewer(content, SWT.BORDER);
		treeViewer.setComparator(new VirSatNavigatorSeiSorter());
		VirSatComposedContentProvider cp = new VirSatComposedContentProvider();
		cp.registerSubContentProvider(new VirSatWorkspaceContentProvider());
		cp.registerSubContentProvider(new VirSatProjectContentProvider());
		
		VirSatComposedLabelProvider lp = new VirSatComposedLabelProvider();
		lp.registerSubLabelProvider(new VirSatWorkspaceLabelProvider());
		lp.registerSubLabelProvider(new VirSatProjectLabelProvider());
		
		VirSatFilteredWrappedTreeContentProvider filteredCP = new VirSatFilteredWrappedTreeContentProvider(cp);
		filteredCP.setCheckContainedForFilter(true);
		
		// Filter for elements that will be Generated

		filteredCP.addClassFilter(StructuralElementInstance.class);
		filteredCP.addClassFilter(Repository.class);
		filteredCP.addClassFilter(VirSatProjectResource.class);
		
		treeViewer.setContentProvider(filteredCP);
		treeViewer.setLabelProvider(lp);	
		// create the new Structural element 
		StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) preSelect).getFirstElement();
		this.ed = VirSatEditingDomainRegistry.INSTANCE.getEd(sc); 
		VirSatResourceSet virSatResourceSet = ed.getResourceSet();
		this.rep = virSatResourceSet.getRepository();
		
	
			ConfigurationTree at = (ConfigurationTree) ProductStructureHelper.createTreeModel(sc);
			this.sei = at.getStructuralElementInstance();
			treeViewer.setInput(at.getStructuralElementInstance());
	
		treeViewer.expandAll();
		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		editor = new TreeEditor(tree);
		

		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuManager menuMgr = new MenuManager();
				menuMgr.addMenuListener(new IMenuListener() {
					@Override
					public void menuAboutToShow(IMenuManager manager) {
						manager.removeAll();
						// initialize the action to perform
						Action rename = new Action() {
							public void run() {
								// Identify the selected row
								TreeItem item = (TreeItem) e.item;
								if (item == null) {
									return;
								}
								// The control that will be the editor must be a child of the Tree
								Text newEditor = new Text(tree, SWT.NONE);
								StructuralElementInstance temp = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
								newEditor.setText(temp.getName());
								newEditor.addModifyListener(new ModifyListener() {
									public void modifyText(ModifyEvent e) {
										Text text = (Text) editor.getEditor();
										editor.getItem().setText(text.getText());
										StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
										sc.setName(text.getText());
									}
								});
								newEditor.selectAll();
								newEditor.setFocus();
								editor.setEditor(newEditor, item);
								
							}
						};
						rename.setText("Rename");
						manager.add(rename);
						Action duplicate = new Action() {

							public void run() {
								StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
								ProductStructureHelper.duplicate(sc);
							}
						};
						duplicate.setText("Duplicate");
						manager.add(duplicate);
					}
				});
				Menu menu = menuMgr.createContextMenu(treeViewer.getTree());
				treeViewer.getTree().setMenu(menu);
			}
		});
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Control oldEditor = editor.getEditor();
				if (oldEditor != null) {
					oldEditor.dispose();
				}
				selection = event.getSelection();
			}
		});
	}
	
	/*
	 * Get all valid configurations
	 */
	public void getAllValidVariants(StructuralElementInstance featureTree) {

		
		/*
		 * Translate to cnf
		 
			r is root feature | r
			p is parent of feature c | c -> p
			m is mandatory subfeature of p | p -> m
			p is the parent of [1..n] grouped features g1, ... , gn | p -> (g1 V ... V gn)
			p is parent of [1..1] grouped features g1, ... , gn | p -> 1-of-n(g1, ... , gn)
			Cross-tree constraint c requires p | c -> p
			Cross-tree constraint c excludes p | c -> -p
		*/
		
		/*
		 * De Morgan.
		 * 
		 * A <=> B        : (A => B) AND (B => A)
		 * A => B         : NOT(A) OR B
		 *
		 */
		
		
		/*
		 * Map the features to CNF variables.
		 */
		// Two maps to improve performance
		Map<Integer, StructuralElementInstance> variableToFeature = new HashMap<>();
		Map<StructuralElementInstance, Integer> featureToVariable = new HashMap<>();
		
		// put root feature
		variableToFeature.put(1, featureTree);
		featureToVariable.put(featureTree, 1);
		
		// next variable is number 2
		int variableCounter = 2;
		
		for (StructuralElementInstance feature : featureTree.getDeepChildren()) {
			variableToFeature.put(variableCounter, feature);
			featureToVariable.put(feature, variableCounter);
			variableCounter++;
		}
		
		/*
		 * Use DimacStringSolver to safely build a DIMACS file.
		 */
		DimacsStringSolver dimacsStringSolver = new DimacsStringSolver();
		
		/*
		 * Iterate through the features and view every feature as child node.
		 */
		for (Entry<Integer, StructuralElementInstance> feature : variableToFeature.entrySet()) {
			
			/*
			 * feature.getKey() = variable for the clauses
			 * feature.getValue() = structuralElementInstance for handling
			 */
			
			// root is not a child
			if (feature.getKey() == 1) {
				continue;
			}
			
			
			/*
			 * CategoryAssignments of feature.
			 */
			Optional<CategoryAssignment> optionalRelationship = feature.getValue().getCategoryAssignments().stream()
					.filter(ca -> ca.getType().getName().equals("Optional"))
					.findAny();
			
			Optional<CategoryAssignment> crossTreeConstraint = feature.getValue().getCategoryAssignments().stream()
					.filter(ca -> ca.getType().getName().equals("CrossTreeConstraint"))
					.findAny();
			
			/*
			 * SubFeatureRelationship of parent.
			 */
			StructuralElementInstance parent = feature.getValue().getParent();
			Optional<CategoryAssignment> parentSubFeatureRelationship = parent.getCategoryAssignments().stream()
					.filter(ca -> ca.getType().getName().equals("SubFeatureRelationship"))
					.findAny();
			
			/*
			 * Handle SubFeatureRelationship.
			 */
			if (parentSubFeatureRelationship.isPresent()) {
				
				
				
			/*
			 * Create new clauses when the parent has no subfeatureRelationship which affects this feature.
			 */
			} else {
				
				boolean optionalFlagSet = true;
				
				/*
				 * Handle Optional.
				 */
				if (optionalRelationship.isPresent()) {
				
					OptionalRelationship optionalInstance = new OptionalRelationship(optionalRelationship.get());
					
					// check if value is "true", else the relationship is mandatory
					if (optionalInstance.getIsOptional()) { 
						handleOptional(featureToVariable.get(parent), feature.getKey(), dimacsStringSolver); 
					} 
					else {
						optionalFlagSet = false; 
					}			
					
				/*
				 * If either no optionalRelationship is defined or it is set to false, we assume the relationship is mandatory.
				 */
				} else if (optionalRelationship.isEmpty() || !optionalFlagSet) {
					handleMandatory(featureToVariable.get(parent), feature.getKey(), dimacsStringSolver);
				}
				
			}
			
			/*
			 * Handle CrossTreeConstraint.
			 */
			if (crossTreeConstraint.isPresent()) {
				
				CrossTreeConstraint crossTreeConstraintInstance = new CrossTreeConstraint(crossTreeConstraint.get());
				
				if (crossTreeConstraintInstance.getReferenceUuid() != null) {
					
					// get other feature variable by uuid
					int referencedFeatureVariable = 0;
					for (Entry<Integer, StructuralElementInstance> otherFeature : variableToFeature.entrySet()) {
						if (otherFeature.getValue().getUuid().toString().equals(crossTreeConstraintInstance.getReferenceUuid())) {
							referencedFeatureVariable = otherFeature.getKey();
						}
					}
					
					switch (crossTreeConstraintInstance.getCharacter()) {
						case "Requires" -> handleRequires(referencedFeatureVariable, feature.getKey(), dimacsStringSolver);
						case "Excludes" -> handleExcludes(referencedFeatureVariable, feature.getKey(), dimacsStringSolver);
					}
				}
			}
		}
		
		
		
		/*
		 * Create DIMACS.
		 */
		System.out.println(dimacsStringSolver.getOut());
		
		/*
		 * Create SAT solver
		 */
		
		ISolver solver = SolverFactory.newDefault();	
		
		
		/*
		 * Solve until all solutions are found
		 */
	}
	
	/*
	 * Handle mandatory relationship.
	 */
	public void handleMandatory(int parentVariable, int childVariable, DimacsStringSolver dimacsStringSolver) {
		try {
			dimacsStringSolver.addClause(new VecInt(new int[]{parentVariable, childVariable}));
			dimacsStringSolver.addClause(new VecInt(new int[]{childVariable, parentVariable}));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Handle optional relationship.
	 */
	public void handleOptional(int parentVariable, int childVariable, DimacsStringSolver dimacsStringSolver) {
		try {
			dimacsStringSolver.addClause(new VecInt(new int[]{-childVariable, parentVariable}));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Handle requires cross tree constraint.
	 */
	public void handleRequires(int requiredVariable, int optionalVariable, DimacsStringSolver dimacsStringSolver) {
		handleOptional(requiredVariable, optionalVariable, dimacsStringSolver);
	}
	
	/*
	 * Handle excludes cross tree constraint.
	 */
	public void handleExcludes(int excludedVariable, int thisVariable, DimacsStringSolver dimacsStringSolver) {
		try {
			dimacsStringSolver.addClause(new VecInt(new int[]{-excludedVariable, -thisVariable}));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return canFlipToNextPage
	 */
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	/**
	 * Get the selected object
	 * @return the selected object
	 */
	public Object getSelection() {
		return selection;
	}

	/**
	 * Checks if the page has been sufficiently filled with user data
	 * @return true iff the page is complete
	 */
	public boolean isComplete() {			
		return true;	
	}
	/**
	 * @return rep Repository
	 */
	public Repository getRep() {
		return rep;
	}
	/**
	 * @param rep Repository
	 */
	public void setRep(Repository rep) {
		this.rep = rep;
	}
	/**
	 * @return ed VirSatTransactionalEditingDomain
	 */
	public VirSatTransactionalEditingDomain getEd() {
		return ed;
	}
	/**
	 * @param ed VirSatTransactionalEditingDomain
	 */
	public void setEd(VirSatTransactionalEditingDomain ed) {
		this.ed = ed;
	}
	/**
	 * @return sei StructuralElementInstance
	 */
	public StructuralElementInstance getSei() {
		return sei;
	}
	/**
	 * @param sei StructuralElementInstance
	 */
	public void setSei(StructuralElementInstance sei) {
		this.sei = sei;
	}
	/**
	 * @return sei StructuralElementInstance
	 */
	public String getSeiName() {
		return treeName.getText();
	}
}