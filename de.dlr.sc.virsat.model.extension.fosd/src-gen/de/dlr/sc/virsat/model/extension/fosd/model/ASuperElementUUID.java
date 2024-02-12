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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
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
 * Category to define the ElementDefinition uuid in the ProductTree 
 	this Feature was created from
 * 
 */	
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASuperElementUUID extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fosd.SuperElementUUID";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_UUID = "uuid";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ASuperElementUUID() {
	}
	
	public ASuperElementUUID(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "SuperElementUUID");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "SuperElementUUID");
		setTypeInstance(categoryAssignement);
	}
	
	public ASuperElementUUID(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: uuid
	// *****************************************************************
	private BeanPropertyString uuid = new BeanPropertyString();
	
	private void safeAccessUuid() {
		if (uuid.getTypeInstance() == null) {
			uuid.setTypeInstance((ValuePropertyInstance) helper.getPropertyInstance("uuid"));
		}
	}
	
	public Command setUuid(EditingDomain ed, String value) {
		safeAccessUuid();
		return this.uuid.setValue(ed, value);
	}
	
	public void setUuid(String value) {
		safeAccessUuid();
		this.uuid.setValue(value);
	}
	
	public String getUuid() {
		safeAccessUuid();
		return uuid.getValue();
	}
	
	@XmlElement
	public BeanPropertyString getUuidBean() {
		safeAccessUuid();
		return uuid;
	}
	
	
}
