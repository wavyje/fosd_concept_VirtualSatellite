/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fosd.model;

// *****************************************************************
// * Import Statements
// *****************************************************************
import javax.xml.bind.annotation.XmlAccessorType;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyBoolean;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
import javax.xml.bind.annotation.XmlElement;


// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * Use this to mark the feature as optional.
 * 
 */	
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AOptionalRelationship extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fosd.OptionalRelationship";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_ISOPTIONAL = "isOptional";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AOptionalRelationship() {
	}
	
	public AOptionalRelationship(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "OptionalRelationship");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "OptionalRelationship");
		setTypeInstance(categoryAssignement);
	}
	
	public AOptionalRelationship(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: isOptional
	// *****************************************************************
	private BeanPropertyBoolean isOptional = new BeanPropertyBoolean();
	
	private void safeAccessIsOptional() {
		if (isOptional.getTypeInstance() == null) {
			isOptional.setTypeInstance((ValuePropertyInstance) helper.getPropertyInstance("isOptional"));
		}
	}
	
	public Command setIsOptional(EditingDomain ed, boolean value) {
		safeAccessIsOptional();
		return this.isOptional.setValue(ed, value);
	}
	
	public void setIsOptional(boolean value) {
		safeAccessIsOptional();
		this.isOptional.setValue(value);
	}
	
	public boolean getIsOptional() {
		safeAccessIsOptional();
		return isOptional.getValue();
	}
	
	@XmlElement
	public BeanPropertyBoolean getIsOptionalBean() {
		safeAccessIsOptional();
		return isOptional;
	}
	
	
}
