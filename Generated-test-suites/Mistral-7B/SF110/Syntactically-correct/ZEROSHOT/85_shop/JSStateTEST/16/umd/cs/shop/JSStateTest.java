package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import umd.cs.shop.JSSubstitutionListLogicalAtoms;
import umd.cs.shop.JSTState;

public class JSStateTest {

	@Test
	public void testJSStateConstructor() {
		JSState jsState = new JSState();
		assertNotNull(jsState);
	}

	@Test
	public void testJSStateConstructorWithTokenizer() {
		JSState jsState = new JSState(new StreamTokenizer());
		assertNotNull(jsState);
	}

	@Test
	public void testAddElementsToState() {
		JSState jsState = new JSState();
		JSListLogicalAtoms s = new JSListLogicalAtoms();
		JSPredicateForm p1 = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSPredicateForm p2 = new JSPredicateForm("p2(y)", new Object[] { "y" });
		JSPredicateForm p3 = new JSPredicateForm("p3(z)", new Object[] { "z" });

		s.add(p1);
		s.add(p2);
		s.add(p3);

		jsState.addElementsToState(s);

		assertEquals(3, jsState.size());
		assertEquals(p1, jsState.elementAt(0));
		assertEquals(p2, jsState.elementAt(1));
		assertEquals(p3, jsState.elementAt(2));
	}

	@Test
	public void testApply() {
		JSState jsState = new JSState();
		JSPlan pl = new JSPlan();
		JSUtil.flagLevel = 0;

		assertNull(jsState.apply(pl));
	}

	@Test
	public void testApplyOpNullInputs() {
		JSOperator op = new JSOperator("op", new Object[] { "add", new Object[] { "JSState", "JSListLogicalAtoms", "JSListLogicalAtoms" } }, null);
		JSStatejsState = new JSState();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSTState result = jsState.applyOp(op, null, addL, delL);

		assertNull(result);
	}

	@Test
	public void testApplyOpEmptyInputs() {
		JSOperator op = new JSOperator("op", new Object[] { "add", new Object[] { "JSState", "JSListLogicalAtoms", "JSListLogicalAtoms" } }, null);
		JSState jsState = new JSState();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSTState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);

		assertNull(result);
	}

	@Test
	public void testApplyOpEmptyOperatorsList() {
		JSOperator op = new JSOperator("op", new Object[] { "add", new Object[] { "JSState", "JSListLogicalAtoms", "JSListLogicalAtoms" } }, null);
		JSState jsState = new JSState();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		List<JSOperator> operatorsList = new ArrayList<>();
		JSTState result = jsState.applyOp(op, new JSSubstitution(), addL, delL, operatorsList);

		assertNull(result);
	}

	@Test
	public void testApplyOp() {
		JSOperator op = new JSOperator("op", new Object[] { "add", new Object[] { "JSState", "JSListLogicalAtoms", "JSListLogicalAtoms" } }, null);
		JSState jsState = new JSState();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
		JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
		expectedAddL.add(new JSPredicateForm("p1(x)", new Object[] { "x" }));
		expectedAddL.add(new JSPredicateForm("p2(y)", new Object[] { "y" }));
		expectedDelL.add(new JSPredicateForm("p3(z)", new Object[] { "z" }));

		addL.add(new JSPredicateForm("p4(w)", new Object[] { "w" }));
		delL.add(new JSPredicateForm("p3(z)", new Object[] { "z" }));

		JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
		JSTState expectedResult = new JSTState(jsState, expectedAddL, expectedDelL);

		assertEquals(expectedResult, result);
	}

	@Test
	public void testSatisfiesNullInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = null;
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

		assertNull(result);
	}

	@Test
	public void testSatisfiesEmptyInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

		assertEquals(new JSSubstitution(), result);
	}

	@Test
	public void testSatisfiesOneInput() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.add(new JSPredicateForm("p1(x)", new Object[] { "x" }));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

		assertEquals(new JSSubstitution(), result);
	}

	@Test
	public void testSatisfiesMultipleInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.add(new JSPredicateForm("p1(x)", new Object[] { "x" }));
		conds.add(new JSPredicateForm("p2(y)", new Object[] { "y" }));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

		assertEquals(new JSSubstitution(), result);
	}

	@Test
	public void testSatisfiesAllNullInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = null;
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllEmptyInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllOneInput() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.add(new JSPredicateForm("p1(x)", new Object[] { "x" }));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesAllMultipleInputs() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.add(new JSPredicateForm("p1(x)", new Object[] { "x" }));
		conds.add(new JSPredicateForm("p2(y)", new Object[] { "y" }));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAmNullInputs() {
		JSState jsState = new JSState();
		JSPredicateForm t = null;
		JSSubstitution alpha = new JSSubstitution();

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAmEmptyInputs() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p(x)", new Object[] { "x" });
		JSSubstitution alpha = new JSSubstitution();

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAmOneInput() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSPredicateForm p = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSSubstitution alpha = new JSSubstitution();

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAmMultipleInputs() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSPredicateForm p1 = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSPredicateForm p2 = new JSPredicateForm("p2(y)", new Object[] { "y" });
		JSSubstitution alpha = new JSSubstitution();

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testSatisfiesTAmMatchingInputs() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSPredicateForm p = new JSPredicateForm("p1(x)", new Object[] { "x" });
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms expectedElements = new JSListLogicalAtoms();
		expectedElements.add(p);

		jsState.add(p);

		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

		assertEquals(new JSSubstitution(expectedElements), result);
	}
}