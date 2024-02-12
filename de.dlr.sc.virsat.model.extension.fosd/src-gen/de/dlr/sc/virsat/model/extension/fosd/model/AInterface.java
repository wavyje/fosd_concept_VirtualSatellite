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
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.json.ABeanObjectAdapter;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 * 
 * 
 */	
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AInterface extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fosd.Interface";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_IFENDFROM = "ifEndFrom";
	public static final String PROPERTY_IFENDTO = "ifEndTo";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AInterface() {
	}
	
	public AInterface(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "Interface");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "Interface");
		setTypeInstance(categoryAssignement);
	}
	
	public AInterface(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: ifEndFrom
	// *****************************************************************
	private BeanPropertyReference<InterfaceEnd> ifEndFrom = new BeanPropertyReference<>();
	
	private void safeAccessIfEndFrom() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("ifEndFrom");
		ifEndFrom.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public InterfaceEnd getIfEndFrom() {
		safeAccessIfEndFrom();
		return ifEndFrom.getValue();
	}
	
	public Command setIfEndFrom(EditingDomain ed, InterfaceEnd value) {
		safeAccessIfEndFrom();
		return ifEndFrom.setValue(ed, value);
	}
	
	public void setIfEndFrom(InterfaceEnd value) {
		safeAccessIfEndFrom();
		ifEndFrom.setValue(value);
	}
	
	public BeanPropertyReference<InterfaceEnd> getIfEndFromBean() {
		safeAccessIfEndFrom();
		return ifEndFrom;
	}
	
	// *****************************************************************
	// * Attribute: ifEndTo
	// *****************************************************************
	private BeanPropertyReference<InterfaceEnd> ifEndTo = new BeanPropertyReference<>();
	
	private void safeAccessIfEndTo() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("ifEndTo");
		ifEndTo.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public InterfaceEnd getIfEndTo() {
		safeAccessIfEndTo();
		return ifEndTo.getValue();
	}
	
	public Command setIfEndTo(EditingDomain ed, InterfaceEnd value) {
		safeAccessIfEndTo();
		return ifEndTo.setValue(ed, value);
	}
	
	public void setIfEndTo(InterfaceEnd value) {
		safeAccessIfEndTo();
		ifEndTo.setValue(value);
	}
	
	public BeanPropertyReference<InterfaceEnd> getIfEndToBean() {
		safeAccessIfEndTo();
		return ifEndTo;
	}
	
	
}
