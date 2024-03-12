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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyEnum;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
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
 * Category to describe the relationship of the subfeatures.
 * 
 */	
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASubFeatureRelationship extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fosd.SubFeatureRelationship";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_CHARACTER = "character";
	
	// Character enumeration value names
	public static final String CHARACTER_or_NAME = "or";
	public static final String CHARACTER_xor_NAME = "xor";
	// Character enumeration values
	public static final String CHARACTER_or_VALUE = "OR";
	public static final String CHARACTER_xor_VALUE = "XOR";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ASubFeatureRelationship() {
	}
	
	public ASubFeatureRelationship(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "SubFeatureRelationship");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "SubFeatureRelationship");
		setTypeInstance(categoryAssignement);
	}
	
	public ASubFeatureRelationship(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: character
	// *****************************************************************
	private BeanPropertyEnum character = new BeanPropertyEnum();
	
	private void safeAccessCharacter() {
		if (character.getTypeInstance() == null) {
			character.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("character"));
		}
	}
	
	public Command setCharacter(EditingDomain ed, String value) {
		safeAccessCharacter();
		return this.character.setValue(ed, value);
	}
	
	public void setCharacter(String value) {
		safeAccessCharacter();
		this.character.setValue(value);
	}
	
	public String getCharacter() {
		safeAccessCharacter();
		return character.getValue();
	}
	
	public double getCharacterEnum() {
		safeAccessCharacter();
		return character.getEnumValue();
	}
	
	@XmlElement
	public BeanPropertyEnum getCharacterBean() {
		safeAccessCharacter();
		return character;
	}
	
	
}
