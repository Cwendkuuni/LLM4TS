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

public class JSStateTest {

	@Test
	public void testJSStateConstructor() {
		JSState jsState = new JSState();
		assertNull(jsState.getLabel());
		JSState jsStateWithTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
		assertNull(jsStateWithTokenizer.getLabel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testJSStateAddElementsToStateWithNullList() {
		JSState jsState = new JSState();
		jsState.addElementsToState(null);
	}

	@Test
	public void testJSStateAddElementsToState() {
		JSState jsState = new JSState();
		JSListLogicalAtoms s = new JSListLogicalAtoms();
		s.addElement(new JSPredicateForm("p(x)", new String[] {"x"}));
		s.addElement(new JSPredicateForm("q(y)", new String[] {"y"}));
		jsState.addElementsToState(s);
		List<JSPredicateForm> elements = jsState.toList();
		assertEquals(2, elements.size());
		assertTrue(elements.contains(new JSPredicateForm("p(x)", new String[] {"x"})));
		assertTrue(elements.contains(new JSPredicateForm("q(y)", new String[] {"y"})));
	}

	@Test
	public void testJSStateApply() {
		JSState jsState = new JSState();
		JSPlan pl = new JSPlan();
		JSState result = jsState.apply(pl);
		fail("apply method not implemented in JSState");
	}

	@Test
	public void testJSStateApplyOpWithEmptyLists() {
		JSState jsState = new JSState();
		JSOperator op = new JSOperator("op", new String[] {}, new String[] {});
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSState result = jsState.applyOp(op, alpha, addL, delL);
		JSState expectedResult = new JSState();
		assertEquals(expectedResult, result);
	}

	@Test
	public void testJSStateApplyOp() {
		JSState jsState = new JSState();
		JSOperator op = new JSOperator("op", new String[] {}, new String[] {});
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		addL.addElement(new JSPredicateForm("p(x)", new String[] {"x"}));
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		delL.addElement(new JSPredicateForm("q(y)", new String[] {"y"}));
		JSState result = jsState.applyOp(op, alpha, addL, delL);
		JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
		expectedAddL.addElement(new JSPredicateForm("p(x)", new String[] {"x"}));
		JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
		expectedDelL.addElement(new JSPredicateForm("q(y)", new String[] {"y"}));
		JSState expectedResult = new JSState();
		expectedResult.addElementsToState(expectedAddL);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testJSStateSatisfies() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p(x)", new String[] {"x"}));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();
		JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
		fail("satisfies method not implemented in JSState");
	}

	@Test
	public void testJSStateSatisfiesAll() {
		JSState jsState = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		conds.addElement(new JSPredicateForm("p(x)", new String[] {"x"}));
		conds.addElement(new JSPredicateForm("q(y)", new String[] {"y"}));
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();
		JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
		JSSubstitution expectedResult = new JSSubstitution();
		assertEquals(expectedResult, result);
	}

	@Test
	public void testJSStateSatisfiesTAmWithEmptyList() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p(x)", new String[] {"x"});
		JSSubstitution alpha = new JSSubstitution();
		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testJSStateSatisfiesTAm() {
		JSState jsState = new JSState();
		JSPredicateForm t = new JSPredicateForm("p(x)", new String[] {"x"});
		JSPredicateForm el = new JSPredicateForm("p(1)", new String[] {"x"});
		JSSubstitution alpha = new JSSubstitution();
		JSListLogicalAtoms elements = new JSListLogicalAtoms();
		elements.addElement(el);
		jsState.addElementsToState(elements);
		JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
		JSListSubstitution expectedResult = new JSListSubstitution();
		expectedResult.addElement(alpha);
		assertTrue(result.equals(expectedResult));
	}

	@Test
	public void testJSStatePrint() {
		JSUtil.setFlagLevel(3);
		JSState jsState = new JSState();
		JSListLogicalAtoms elements = new JSListLogicalAtoms();
		elements.addElement(new JSPredicateForm("p(1)", new String[] {"x"}));
		jsState.addElementsToState(elements);
		jsState.print();
		assertEquals("The resulting state :\n", JSUtil.getBuffer().toString());
		assertEquals("-----------", JSUtil.getBuffer().toString());
		JSUtil.setFlagLevel(0);
	}
}