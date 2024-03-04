package de.dlr.sc.virsat.model.extension.fosd.ui.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import de.dlr.sc.virsat.model.concept.types.structural.ABeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.util.StructuralInstantiator;
import de.dlr.sc.virsat.model.extension.fosd.model.FeatureTree;
import de.dlr.sc.virsat.model.extension.fosd.ui.wizards.ChangeRelationOrDeleteWizard;
import de.dlr.sc.virsat.model.extension.fosd.util.ProductStructureHelper;
import de.dlr.sc.virsat.model.extension.ps.model.ProductTree;
import de.dlr.sc.virsat.model.ui.preferences.PreferencesEditorAutoOpen;
import de.dlr.sc.virsat.project.editingDomain.VirSatEditingDomainRegistry;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;
import de.dlr.sc.virsat.project.structure.command.CreateAddSeiWithFileStructureCommand;

public class UpdateFeatureTreeHandler extends AbstractHandler implements IHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		StructuralElementInstance sc = (StructuralElementInstance) ((IStructuredSelection) selection).getFirstElement();
		VirSatTransactionalEditingDomain ed = VirSatEditingDomainRegistry.INSTANCE.getEd(sc);
		VirSatResourceSet resSet = (VirSatResourceSet) ed.getResourceSet();
		Repository repository = resSet.getRepository();
		ActiveConceptHelper ach = new ActiveConceptHelper(repository);
		
		/*
		 * Contains the product tree if present.
		 */
		StructuralElementInstance productTree = null;
		
		/*
		 * Contains the feature tree if present.
		 */
		StructuralElementInstance featureTree = null;
		
		/*
		 * Contains all elements of the product tree.
		 */
		List<StructuralElementInstance> productTreeSEs = new ArrayList<StructuralElementInstance>();

		/*
		 * Contains the elements removed from the product tree.
		 */
		List<StructuralElementInstance> removedFromProductTree = new ArrayList<StructuralElementInstance>();
		
		for (StructuralElementInstance se : repository.getRootEntities()) {
			if (se.getType().getName().equals(ProductTree.class.getSimpleName())) {
				productTree = se;
			}
			if (se.getType().getName().equals(FeatureTree.class.getSimpleName())) {
				featureTree = se;
			}
		}
			
		productTreeSEs.addAll(productTree.getDeepChildren());
		
		/*
		 *  Check for every feature whether the super ElementDefinition 
		 *  is still in the product tree.
		 *  Removed EDs will later be prompted to decide whether 
		 *  terminally removing them or changing the relationship.
		 */
		for (StructuralElementInstance child : featureTree.getDeepChildren()) {
			if (child.getSuperSeis().size() == 0) {
				removedFromProductTree.add(child);
			}
		}
		
		// compare size of trees
		// if different, ElementDefinitions were added
		if (featureTree.getDeepChildren().size() < (productTreeSEs.size() + removedFromProductTree.size())) {
			List<StructuralElementInstance> featuresToAdd = getAddedElementDefinitions(productTree.getDeepChildren(), featureTree.getDeepChildren());
			
			for (StructuralElementInstance featureToAdd : featuresToAdd) {
				EList<StructuralElementInstance> nodesInFeatureTree = featureTree.getDeepChildren();
				nodesInFeatureTree.add(featureTree);
				StructuralElementInstance parent = findParentInFeatureTree(nodesInFeatureTree, featureToAdd);
				addFeatureToFeatureTree(parent, featureToAdd);
			}
		}
			
		if (!removedFromProductTree.isEmpty()) {
			Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
			ChangeRelationOrDeleteWizard wizard = new ChangeRelationOrDeleteWizard();
			wizard.setSelection(selection);
			WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.open(); 
		}
		/*
		
		*/			
		return null;
		
	}
	
	public void addFeatureToFeatureTree(StructuralElementInstance parent, StructuralElementInstance inheritance) {
		VirSatTransactionalEditingDomain ed = VirSatEditingDomainRegistry.INSTANCE.getEd(parent);
		Concept concept = ActiveConceptHelper.getConcept(parent.getType());
		
		
		// Create Feature and add inheritance
		String structuralElementName = "Feature";
		StructuralElement structuralElement = ActiveConceptHelper.getStructuralElement(concept, structuralElementName);
		StructuralInstantiator structuralInstantiator = new StructuralInstantiator();
		StructuralElementInstance structuralElementInstance = structuralInstantiator.generateInstance(structuralElement, structuralElementName);
		structuralElementInstance.setAssignedDiscipline(parent.getAssignedDiscipline());
		structuralElementInstance.getSuperSeis().add(inheritance);
		structuralElementInstance.setName(inheritance.getName());
		
		// Create command and execute
		Command addStructuralElementInstance = CreateAddSeiWithFileStructureCommand.create(ed, parent, structuralElementInstance);
		ed.getCommandStack().execute(addStructuralElementInstance);
		
		// Try to open the CA in case it is preferred
		PreferencesEditorAutoOpen.openEditorIfPreferredForCollection(addStructuralElementInstance.getResult());
	}
	
	/*
	 * Get the difference between two children lists.
	 */
	private List<StructuralElementInstance> getAddedElementDefinitions(EList<StructuralElementInstance> productTreeDeepChildren, 
			EList<StructuralElementInstance> featureTreeDeepChildren) {
		Set<StructuralElementInstance> ptSet = new HashSet<StructuralElementInstance>(productTreeDeepChildren);
		Set<StructuralElementInstance> ftSet = new HashSet<StructuralElementInstance>();
		
		// we need to compare with the superSEI, otherwise all elements are different	
		for (StructuralElementInstance ftChild : featureTreeDeepChildren) {
			if (ftChild.getSuperSeis().size() != 0) {
				ftSet.add(ftChild.getSuperSeis().get(0));
			}
		}
		
		ptSet.removeAll(ftSet);
		List<StructuralElementInstance> addedElementDefinitions = new ArrayList<StructuralElementInstance>(ptSet);
		return addedElementDefinitions;
	}
	
	private StructuralElementInstance findParentInFeatureTree(List<StructuralElementInstance> featureTreeNodes, 
			StructuralElementInstance elementInProductTree) {
		// search for node in feature tree which superSEI is the parent of the elementInProductTree
		for (StructuralElementInstance node : featureTreeNodes) {
			EList<StructuralElementInstance> superSei = node.getSuperSeis();
			if (superSei.size() != 0) {
				if (superSei.get(0).equals(elementInProductTree.getParent())) {
					return node; 
				}
			}
		}
		return null;
	}
	
}
