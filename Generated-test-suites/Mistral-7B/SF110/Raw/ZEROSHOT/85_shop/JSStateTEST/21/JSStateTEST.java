package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

	@Test
	public void testJSStateConstructor() {
		JSState jsState = new JSState();
		assertNotNull(jsState);

		JSState jsStateWithTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
		assertNotNull(jsStateWithTokenizer);
	}

	@Test
	public void testAddElementsToState() {
		JSListLogicalAtoms s = new JSListLogicalAtoms();
		s.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		s.addElement(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z")));

		JSState jsState = new JSState();
		jsState.addElementsToState(s);

		assertEquals(2, jsState.size());
		assertTrue(jsState.contains(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y"))));
		assertTrue(jsState.contains(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z"))));
		assertTrue(jsState.contains(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y"))));
		assertTrue(jsState.contains(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z"))));
	}

	@Test
	public void testApplyOpWithEmptyState() {
		JSOperator op = new JSOperator("op", Arrays.asList("X"), Arrays.asList("Y"), Arrays.asList("Z"));
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();

		JSState jsState = new JSState();

		JSState expectedResult = jsState.applyOp(op, alpha, addL, delL);
		assertEquals(0, expectedResult.size());
	}

	@Test
	public void testApplyOpWithEmptyLists() {
		JSOperator op = new JSOperator("op", Arrays.asList("X"), Arrays.asList("Y"), Arrays.asList("Z"));
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSState expectedResult = jsState.applyOp(op, alpha, addL, delL);
		assertEquals(0, expectedResult.size());
	}

	@Test
	public void testApplyOpWithMatchingElements() {
		JSOperator op = new JSOperator("op", Arrays.asList("X"), Arrays.asList("Y"), Arrays.asList("Z"));
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		jsState.addElement(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z")));

		JSListLogicalAtoms add = op.addList();
		add.addElement(new JSPredicateForm("r", Arrays.asList("w"), Arrays.asList("x")));

		JSListLogicalAtoms del = op.deleteList();
		del.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSState ns = jsState.applyOp(op, alpha, addL, delL);
		assertEquals(1, ns.size());
		assertTrue(ns.contains(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z"))));

		JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
		nAddL.addElement(new JSPredicateForm("r", Arrays.asList("w"), Arrays.asList("x")));
		JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
		nDelL.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSListLogicalAtoms nAdd = new JSListLogicalAtoms();
		nAdd.addElement(new JSPredicateForm("r", Arrays.asList("w"), Arrays.asList("x")));
		JSListLogicalAtoms nDel = new JSListLogicalAtoms();
		nDel.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		nAddL.addElements(nAdd);
		nDelL.addElements(nDel);

		JSState result = ns.applyOp(op.reverse(), alpha, nAddL, nDelL);
		assertEquals(2, result.size());
		assertTrue(result.contains(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y"))));
		assertTrue(result.contains(new JSPredicateForm("q", Arrays.asList("y"), Arrays.asList("z"))));
	}

	@Test
	public void testSatisfiesWithEmptyListOfConditions() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSSubstitution result = new JSState().satisfies(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesWithEmptyStateAndEmptyListOfConditions() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSState jsState = new JSState();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesWithMatchingConditionsAndEmptyState() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSState jsState = new JSState();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesWithMatchingConditionsAndMatchingState() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesWithNonMatchingConditionsAndMatchingState() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("q", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllWithEmptyListOfConditions() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = new JSState().satisfiesAll(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllWithEmptyStateAndEmptyListOfConditions() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = new JSState().satisfiesAll(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllWithMatchingConditionsAndEmptyState() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = new JSState().satisfiesAll(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllWithNonMatchingConditionsAndEmptyState() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("q", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = new JSState().satisfiesAll(conds, alpha, axioms);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllWithMatchingStateAndMatchingConditions() {
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
		assertEquals(1, result.size());
		assertTrue(result.contains(new JSSubstitution()));
	}

	@Test
	public void testSatisfiesTAmWithMatchingAtomAndSubstitution() {
		JSPredicateForm t = new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y"));
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution expectedResult = new JSSubstitution();
		expectedResult.assign("x", "a");
		expectedResult.assign("y", "b");

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y")));

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
		assertEquals(1, result.size());
		assertTrue(result.contains(expectedResult));
	}

	@Test
	public void testSatisfiesTAmWithNonMatchingAtomAndSubstitution() {
		JSPredicateForm t = new JSPredicateForm("p", Arrays.asList("x"), Arrays.asList("y"));
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution expectedResult = new JSSubstitution();
		expectedResult.assignFailure();

		JSState jsState = new JSState();
		jsState.addElement(new JSPredicateForm("q", Arrays.asList("x"), Arrays.asList("y")));

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
		assertEquals(1, result.size());
		assertTrue(result.contains(expectedResult));
	}
}