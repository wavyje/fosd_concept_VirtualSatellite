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
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import javax.xml.bind.annotation.XmlType;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;

// *****************************************************************
// * Class Declaration
// *****************************************************************

@XmlType(name = ACrossTreeConstraint.FULL_QUALIFIED_CATEGORY_NAME)
/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * Reference to another feature and character of relationship.
 * 
 */
public  class CrossTreeConstraint extends ACrossTreeConstraint {
	
	/**
	 * Constructor of Concept Class
	 */
	public CrossTreeConstraint() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public CrossTreeConstraint(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public CrossTreeConstraint(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
}