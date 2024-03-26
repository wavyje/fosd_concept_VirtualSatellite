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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.provider.PropertydefinitionsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.provider.PropertyinstancesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.categories.provider.DVLMCategoriesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.provider.ConceptsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.general.provider.GeneralItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.provider.DVLMItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.roles.provider.RolesItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.provider.DVLMStructuralItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.dvlm.units.provider.UnitsItemProviderAdapterFactory;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
import de.dlr.sc.virsat.model.extension.budget.mass.model.MassEquipment;
import de.dlr.sc.virsat.model.extension.fosd.model.CrossTreeConstraint;
import de.dlr.sc.virsat.model.extension.fosd.model.FeatureTree;
import de.dlr.sc.virsat.model.extension.fosd.model.MassBudget;
import de.dlr.sc.virsat.model.extension.fosd.model.OptionalRelationship;
import de.dlr.sc.virsat.model.extension.fosd.model.SubFeatureRelationship;
import de.dlr.sc.virsat.model.extension.fosd.util.ProductStructureHelper;
import de.dlr.sc.virsat.model.extension.ps.model.ConfigurationTree;
import de.dlr.sc.virsat.model.extension.ps.model.ElementConfiguration;
import de.dlr.sc.virsat.model.extension.requirements.model.AttributeValue;
import de.dlr.sc.virsat.model.extension.requirements.model.IVerification;
import de.dlr.sc.virsat.model.extension.requirements.model.Requirement;
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
	Map<Integer, StructuralElementInstance> variableToFeature = new HashMap<>();
	Map<StructuralElementInstance, Integer> featureToVariable = new HashMap<>();
	private List<int[]> validConfigurations;
	private StructuralElementInstance selectedConfiguration;
	private boolean generateAsConfigurationTree;
	private Text treeName;
	private List<Tree> tree = new ArrayList<>();
	private TreeEditor editor;
	/**
	 * Create a new Generate page
	 * @param preSelect the initial selection to be used as a model
	 */
	
	protected VariantSelectionPage(ISelection preSelect) {
		
		super("");
		if (preSelect != null) {
			sc = (StructuralElementInstance) ((IStructuredSelection) preSelect).getFirstElement();
			if (sc.getType().getName().equals(FeatureTree.class.getSimpleName())) {
				setTitle("Generate the Configuration Tree");
				generateAsConfigurationTree = true;
			}
			setDescription("Right click on an element to rename or duplicate");
			this.preSelect = preSelect;
		}
		
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
	    Button btnSelect = new Button(composite, SWT.NONE);
	    btnSelect.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		StructuralElementInstance select = ((StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement()).getParent();
	    		
	    		selectedConfiguration = select;
	    		sei = selectedConfiguration;
	    	}
	    });
	    btnSelect.setText("Select");
	    
	    new Label(content, SWT.NONE);
	    Label lblTreeName = new Label(content, SWT.NONE);
	    lblTreeName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    treeName = new Text(content, SWT.BORDER);
	    treeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	  
	 
	    if (generateAsConfigurationTree) {
	    	lblTreeName.setText("Configuration Tree Name");
	    	treeName.setText(ConfigurationTree.class.getSimpleName());
		}
	   
	}
	
	/**
	 * Create the dialog for renaming and duplicating items
	 */
	private void createTreeUI() {
		
		
		//TreeViewer treeViewer =  new TreeViewer(content, SWT.BORDER);
		
		//treeViewer.setComparator(new VirSatNavigatorSeiSorter());
	
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

		// create the new Structural element 
		StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) preSelect).getFirstElement();
		this.ed = VirSatEditingDomainRegistry.INSTANCE.getEd(sc); 
		VirSatResourceSet virSatResourceSet = ed.getResourceSet();
		this.rep = virSatResourceSet.getRepository();
		
		/*
		 * Build the tree models for the view based on the valid configurations.	
		 */
		List<StructuralElementInstance> treeModels = new ArrayList<>();
		
		for (int[] config : validConfigurations) {
			// use of a mapper, so that we can access the newly added feature and get the related original feature.
			Map<StructuralElementInstance, StructuralElementInstance> featureToElementConfigurationMapper = new HashMap<>();
			
			ActiveConceptHelper acHelper = new ActiveConceptHelper(rep);
			Concept activeConcept = acHelper.getConcept(ProductStructureHelper.getConcept());
			
			// Create a configurationTree instance with repository as parent.
			ConfigurationTree ct = (ConfigurationTree) ProductStructureHelper.createTreeModel(sc);
			// We merely want to add the features of the specific configuration.
			ct.removeAllStructuralElementInstance(ct.getDeepChildren(IBeanStructuralElementInstance.class));
			StructuralElementInstance rootFeature = ct.getStructuralElementInstance();
			featureToElementConfigurationMapper.put(sc, rootFeature);
			
			// iterate through the features of the configuration.
			for (int i = 1; i < config.length; i++) {
				if (config[i] > 0) {
					
					// get actual feature of the FeatureTree
					StructuralElementInstance actualFeature = variableToFeature.get(config[i]);
					// find out parent
					int actualParentVariable = featureToVariable.get(actualFeature.getParent());
					
					
					// first feature has to be added to the root feature.
					if (actualParentVariable == 1) {
						StructuralElementInstance feature = ProductStructureHelper.createStructuralElementInstance(activeConcept, ElementConfiguration.FULL_QUALIFIED_STRUCTURAL_ELEMENT_NAME, rootFeature);
						feature.setName(actualFeature.getName());
						feature.setType(acHelper.getStructuralElement(ElementConfiguration.FULL_QUALIFIED_STRUCTURAL_ELEMENT_NAME));
						rootFeature.getChildren().add(feature);
						
						featureToElementConfigurationMapper.put(actualFeature, feature);
					} else {
						StructuralElementInstance parentFeature = featureToElementConfigurationMapper.get(variableToFeature.get(actualParentVariable));
						
						StructuralElementInstance feature = ProductStructureHelper.createStructuralElementInstance(activeConcept, ElementConfiguration.FULL_QUALIFIED_STRUCTURAL_ELEMENT_NAME, parentFeature);
						feature.setName(actualFeature.getName());
						feature.setType(acHelper.getStructuralElement(ElementConfiguration.FULL_QUALIFIED_STRUCTURAL_ELEMENT_NAME));
						parentFeature.getChildren().add(feature);
						
						featureToElementConfigurationMapper.put(actualFeature, feature);
					}
					
				}
			}
			treeModels.add(rootFeature);
		}
		
		// Create the tree models for the configurations. 
		for (StructuralElementInstance treeModel : treeModels) {
			TreeViewer treeViewer = new TreeViewer(content, SWT.BORDER);
			treeViewer.setComparator(new VirSatNavigatorSeiSorter());
			treeViewer.setContentProvider(filteredCP);
			treeViewer.setLabelProvider(lp);	
			treeViewer.setInput(treeModel);
			treeViewer.expandAll();
			tree.add(treeViewer.getTree());
			editor = new TreeEditor(treeViewer.getTree());
			
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
	}
	
	/*
	 * Takes a FeatureTree instance, translates it to conjunctive normal form and feeds it to a SAT solver
	 * to retrieve valid variants of the feature model.
	 * @param StructuralElementInstance the FeatureTree to be evaluated
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
		 * Map features to variables of the CNF.
		 * Root Feature = 1 and so on.
		 */
		
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
		 * Use ModelIterator to find all valid variants.
		 */
		ISolver solver = new ModelIterator(SolverFactory.newDefault());
		
		// List for preventing duplicated SubFeatureRelationship clauses
		List<Integer> existingSubFeatureRelationships = new ArrayList<Integer>();
		
		/*
		 * Iterate through the features and view every feature as child node 
		 * (thus, see parent node for SubFeatureRelationship).
		 */
		for (Entry<Integer, StructuralElementInstance> feature : variableToFeature.entrySet()) {
			
			// root is not a child
			if (feature.getKey() == 1) {
				continue;
			}
			
			
			/*
			 * CategoryAssignments of feature.
			 */
			Optional<CategoryAssignment> optionalRelationship = feature.getValue().getCategoryAssignments().stream()
					.filter(ca -> ca.getType().getName().equals("OptionalRelationship"))
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
				
				SubFeatureRelationship subFeatureRelationship = new SubFeatureRelationship(parentSubFeatureRelationship.get());
				
				if (subFeatureRelationship.getCharacter() != null && !existingSubFeatureRelationships.contains(feature.getKey())) {
					// For SubFeatureRelationships we need the other affected feature variables.
					int[] subFeatureVariables = parent.getChildren().stream().mapToInt(child -> featureToVariable.get(child)).toArray();
					
					// Add parent variable to express the relation to parent feature.
					int[] subFeaturePlusParent = new int[subFeatureVariables.length+1];
					for (int i = 0; i < subFeatureVariables.length; i++) {
						subFeaturePlusParent[i] = subFeatureVariables[i];
					}
					subFeaturePlusParent[subFeaturePlusParent.length-1] = featureToVariable.get(parent);
					
					switch(subFeatureRelationship.getCharacter()) {
						case "xor" -> handleXor(subFeaturePlusParent, solver);
						case "or" -> handleOr(subFeaturePlusParent, solver);
					}
					
					existingSubFeatureRelationships.addAll(IntStream.of(subFeatureVariables).boxed().collect(Collectors.toList()));
				}
				
			/*
			 * Create new clauses when the parent has no SubFeatureRelationship which affects this feature.
			 */
			} else {
				
				// If value of PropertyInstance is set to true
				boolean optionalFlagSet = true;
				
				/*
				 * Handle Optional.
				 */
				if (optionalRelationship.isPresent()) {
				
					OptionalRelationship optionalInstance = new OptionalRelationship(optionalRelationship.get());
					
					// Check if value is "true", else the relationship is mandatory
					if (optionalInstance.getIsOptional()) { 
						handleOptional(featureToVariable.get(parent), feature.getKey(), solver); 
					} 
					else {
						optionalFlagSet = false; 
					}			
					
				/*
				 * If either no OptionalRelationship is defined or it is set to false, we assume the relationship is mandatory.
				 */
				} else if (optionalRelationship.isEmpty() || !optionalFlagSet) {
					handleMandatory(featureToVariable.get(parent), feature.getKey(), solver);
				}
				
			}
			
			/*
			 * Handle CrossTreeConstraint.
			 */
			if (crossTreeConstraint.isPresent()) {
				
				CrossTreeConstraint crossTreeConstraintInstance = new CrossTreeConstraint(crossTreeConstraint.get());
				
				if (crossTreeConstraintInstance.getReferenceUuid() != null) {
					
					// Get other feature variable by uuid
					int referencedFeatureVariable = 0;
					for (Entry<Integer, StructuralElementInstance> otherFeature : variableToFeature.entrySet()) {
						if (otherFeature.getValue().getUuid().toString().equals(crossTreeConstraintInstance.getReferenceUuid())) {
							referencedFeatureVariable = otherFeature.getKey();
						}
					}
					
					switch (crossTreeConstraintInstance.getCharacterBean().getValue()) {
						// Requires
						case "enumValue1" -> handleRequires(referencedFeatureVariable, feature.getKey(), solver);
						// Excludes
						case "enumValue2" -> handleExcludes(referencedFeatureVariable, feature.getKey(), solver);
					}
				}
			}
		}
		
		
		/*
		 * Solve until all solutions are found
		 */
		 boolean unsat = true;
		 List<int[]> models = new ArrayList<>();
		 try {
			while (solver.isSatisfiable()) {
			 unsat = false;
			 models.add(solver.model());
			 // do something with model
			 }
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (unsat) {
		     // UNSAT case
		 }
		 
		 /*
		  * Remove first entry of list, because it is case of every feature being negated.
		  */
		 models.remove(0);
		 
		 /*
		  * Check if variant exceeds mass budget.
		  */
		 Optional<CategoryAssignment> massBudgetOpt = featureTree.getCategoryAssignments().stream()
					.filter(ca -> ca.getType().getName().equals("MassBudget"))
					.findAny();
		 if (massBudgetOpt.isPresent()) {
			 MassBudget massBudget = new MassBudget(massBudgetOpt.get());
			 List<int[]> modelsToRemove = new ArrayList<>();
			 
			 for (int[] model : models) {
				double cumMass = 0.0;
				
				for (int i = 0; i < model.length; i++) {
					if (model[i] > 0) {
						Optional<CategoryAssignment> mass = variableToFeature.get(model[i]).getCategoryAssignments().stream()
								.filter(ca -> ca.getType().getName().equals("MassEquipment"))
								.findAny();
						
						if (mass.isPresent()) {
							MassEquipment massEquipment = new MassEquipment(mass.get());
							cumMass += massEquipment.getMass();
						}
						
					}
					if (cumMass > massBudget.getMass()) {
						modelsToRemove.add(model);
					}
				}
				
			 }
			 models.removeAll(modelsToRemove);
		 }
		 validConfigurations = models;
	}
	
	/*
	 * Handle mandatory relationship.
	 * (-parent or child) and (-child or parent)
	 */
	public void handleMandatory(int parentVariable, int childVariable, ISolver solver) {
		try {
			solver.addClause(new VecInt(new int[]{-parentVariable, childVariable})).toString();
			solver.addClause(new VecInt(new int[]{-childVariable, parentVariable})).toString();
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Handle optional relationship.
	 * (-child or parent)
	 */
	public void handleOptional(int parentVariable, int childVariable, ISolver solver) {
		try {
			solver.addClause(new VecInt(new int[]{-childVariable, parentVariable}));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Handle requires cross tree constraint.
	 * (-optional or required)
	 */
	public void handleRequires(int requiredVariable, int optionalVariable, ISolver solver) {
		handleOptional(optionalVariable, requiredVariable, solver);
	}
	
	/*
	 * Handle excludes cross tree constraint.
	 * (-y or -x)
	 */
	public void handleExcludes(int excludedVariable, int thisVariable, ISolver solver) {
		try {
			solver.addClause(new VecInt(new int[]{-excludedVariable, -thisVariable}));
		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Handle XOR relationship.
	 * (child_1 or ... child_n or -parent) and
	 * for i..n(-child_i or parent) and
	 * for i<j(-child_i or -child_j)
	 */
	public void handleXor(int[] subFeatures, ISolver solver) {
		//solver.addParity(new VecInt(subFeatures), true).toString();
		int[] children = new int[subFeatures.length-1];
		
		int parent = subFeatures[subFeatures.length-1];
		
		for (int i = 0; i < children.length; i++) {
			children[i] = subFeatures[i];
		}
		
		/*
		 * Clause containing all subFeatures, form:
		 * (child_1 or ... child_n or -parent)
		 */
		try {
			int[] copy = subFeatures.clone();
			copy[copy.length-1] = -copy[copy.length-1];
			IVecInt v = new VecInt(copy);
			solver.addClause(v);
		} catch (ContradictionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * Individual clauses for each child, form:
		 * (-child_i or parent)
		 */
		
		for (int i = 0; i < children.length; i++) {
			try {
				IVecInt v = new VecInt(new int[]{-children[i], parent});
				solver.addClause(v);
			} catch (ContradictionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * Individual clauses to prevent children from being the same, form:
		 * all_i<j(-child_i or -child_j)
		 */
		for (int i = 0; i < children.length; i++) {
			for (int j = i+1; j < children.length; j++) {
				try {
					IVecInt v = new VecInt(new int[]{-children[i], -children[j]});
					solver.addClause(v);
				} catch (ContradictionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Handle OR relationship.
	 * (child_1 or ... child_n or -parent) and
	 * for i..n(-child_i or parent)
	 */
	public void handleOr(int[] subFeatures, ISolver solver) {
		
		int[] children = new int[subFeatures.length-1];
		int parent = subFeatures[subFeatures.length-1];
		
		for (int i = 0; i < children.length; i++) {
			children[i] = subFeatures[i];
		}
		
		/*
		 * Clause containing all subFeatures, form:
		 * (child_1 or ... child_n or -parent)
		 */
		try {
			int[] copy = subFeatures.clone();
			copy[copy.length-1] = -copy[copy.length-1];
			IVecInt v = new VecInt(copy);
			solver.addClause(v);
		} catch (ContradictionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * Individual clauses for each child, form:
		 * (-child_i or parent)
		 */
		for (int i = 0; i < children.length; i++) {
			try {
				IVecInt v = new VecInt(new int[]{-children[i], parent});
				solver.addClause(v);
			} catch (ContradictionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public List<int[]> getValidConfigurations() {
		return this.validConfigurations;
	}
}