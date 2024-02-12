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
import de.dlr.sc.virsat.model.dvlm.categories.CategoriesFactory;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import java.lang.Exception;


// *****************************************************************
// * Class Declaration
// *****************************************************************

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * Category to describe the relationship of the subfeatures
 * 
 */	
public abstract class ASubFeatureRelationshipTest {
	
	protected Concept concept;
	
	@Before
	public void setUp() throws Exception {
		String conceptXmiPluginPath = "de.dlr.sc.virsat.model.extension.fosd/concept/concept.xmi";
		concept = de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader.loadConceptFromPlugin(conceptXmiPluginPath);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	// *****************************************************************
	// * Constructor Test Cases
	// *****************************************************************
	
	@Test
	public void testSubFeatureRelationship() {
		SubFeatureRelationship testSubFeatureRelationship = new SubFeatureRelationship();
	
		assertNull("There is no internal DVLM object", testSubFeatureRelationship.getTypeInstance());
	}
	
	@Test
	public void testSubFeatureRelationshipConcept() {
		SubFeatureRelationship testSubFeatureRelationship = new SubFeatureRelationship(concept);
		
		assertNotNull("There is an internal DVLM object", testSubFeatureRelationship.getATypeInstance());
	}
	
	@Test
	public void testSubFeatureRelationshipCategoryAssignment() {
		CategoryAssignment testCa = CategoriesFactory.eINSTANCE.createCategoryAssignment();
		SubFeatureRelationship testSubFeatureRelationship = new SubFeatureRelationship(testCa);
		
		assertEquals("DVLM object has been set as specified", testCa, testSubFeatureRelationship.getTypeInstance());
	}
}
