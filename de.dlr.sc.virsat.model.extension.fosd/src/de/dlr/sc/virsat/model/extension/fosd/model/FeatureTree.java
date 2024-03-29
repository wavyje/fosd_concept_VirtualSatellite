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
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import javax.xml.bind.annotation.XmlType;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * Product tree to describe specifications of used components
 * 
 */
@XmlType(name = AFeatureTree.FULL_QUALIFIED_STRUCTURAL_ELEMENT_NAME)
public class FeatureTree extends AFeatureTree {
	
	/**
	 * Constructor of Concept Class
	 */
	public FeatureTree() {
		super();
	}

	/**
 	 * Constructor of Concept Class
 	 * @param concept The concept from where to initialize
 	 */
	public FeatureTree(Concept concept) {
		super(concept);
	}	

	/**
 	 * Constructor of Concept Class that can be initialized manually by a given StructuralElementInstance
	 * @param sei The StructuralElementInstance to be used for background initialization of the StructuralElementInstance bean
	 */
	public FeatureTree(StructuralElementInstance sei) {
		super(sei);
	}
}
