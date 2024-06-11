package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitutionAlpha;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSUtil.JSListLogicalAtomsFactory;

public class JSStateTest {

	@Test
	public void testJSStateEmptyConstructor() {
		JSState state = new JSState();
		assertEquals(0, state.size());
	}

	@Test
	public void testJSStateWithTokenizerConstructor() {
		JSState state = new JSState(JSUtil.createTokenizer("( (p1) (q1) )"));
		assertEquals(2, state.size());
		assertEquals("p1", state.getElementAt(0).getLabel());
		assertEquals("q1", state.getElementAt(1).getLabel());
	}

	@Test
	public void testAddElementsToState() {
		JSListLogicalAtoms list = JSListLogicalAtomsFactory.createList(Arrays.asList("p1", "q1", "r1"));
		JSState state = new JSState();
		state.addElementsToState(list);
		assertEquals(3, state.size());
		assertEquals("p1", state.getElementAt(0).getLabel());
		assertEquals("q1", state.getElementAt(1).getLabel());
		assertEquals("r1", state.getElementAt(2).getLabel());
	}

	@Test
	public void testApplyOp_EmptyState() {
		JSOperator op = new JSOperator("op", new JSListLogicalAtoms(Arrays.asList("p1")), new JSListLogicalAtoms(Arrays.asList("q1")));
		JSState state = new JSState();
		JSTState expected = new JSTState(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
		JSTState result = new JSState().applyOp(op, new JSSubstitutionAlpha(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
		assertEquals(expected, result);
	}

	@Test
	public void testApplyOp_StateWithElements() {
		JSOperator op = new JSOperator("op", new JSListLogicalAtoms(Arrays.asList("p1", "q1")), new JSListLogicalAtoms(Arrays.asList("r1", "s1")));
		JSState state = new JSState(JSUtil.createTokenizer("( (p1) (q1) )"));
		JSTState expected = new JSTState(new JSState(Arrays.asList("r1", "s1")), new JSListLogicalAtoms(Arrays.asList("p1", "q1")), new JSListLogicalAtoms());
		JSTState result = state.applyOp(op, new JSSubstitutionAlpha(), new JSListLogicalAtoms(Arrays.asList("p1", "q1")), new JSListLogicalAtoms(Arrays.asList("r1", "s1")));
		assertEquals(expected, result);
	}

	@Test
	public void testSatisfies() {
		JSListLogicalAtoms conds = JSListLogicalAtomsFactory.createList(Arrays.asList("p(X) :- q(X)", "r(X) :- p(X)"));
		JSState state = new JSState(JSUtil.createTokenizer("( (p1) (q1) (q1) )"));
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution expected = new JSSubstitution();
		JSSubstitution result = state.satisfies(conds, alpha, new JSListAxioms());
		assertEquals(expected, result);

		JSState stateWithMatch = new JSState(JSUtil.createTokenizer("( (p1) (q1) (q1) (r1))"));
		JSSubstitution expectedWithMatch = new JSSubstitution() {{
			assign("X", "1");
		}};
		JSSubstitution resultWithMatch = stateWithMatch.satisfies(conds, alpha, new JSListAxioms());
		assertEquals(expectedWithMatch, resultWithMatch);
	}

	@Test
	public void testSatisfiesAll() {
		JSListLogicalAtoms conds = JSListLogicalAtomsFactory.createList(Arrays.asList("first :- p(X)", "p(X) :- q(X)", "q(X) :- r(X)", "r(X) :- s(X)"));
		JSState state = new JSState(JSUtil.createTokenizer("( (p1) (q1) (r1) (s1))"));
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution expected = new JSSubstitution() {{
			assign("X", "1");
		}};
		List<JSSubstitution> results = new JSState().satisfiesAll(conds, alpha, new JSListAxioms());
		assertEquals(expected, results.get(0));
		assertEquals(1, results.size());
	}

	@Test
	public void testSatisfiesTAm() {
		JSState state = new JSState(JSUtil.createTokenizer("( (p1) (q1) )"));
		JSListLogicalAtoms t = new JSListLogicalAtoms(Arrays.asList("p(X) :- q(X)"));
		JSSubstitution alpha = new JSSubstitution();
		List<JSSubstitution> results = state.satisfiesTAm(t, alpha);
		assertTrue(results.isEmpty());

		JSState stateWithMatch = new JSState(JSUtil.createTokenizer("( (p1) (q1) (q1) )"));
		List<JSSubstitution> resultsWithMatch = stateWithMatch.satisfiesTAm(t, alpha);
		assertEquals(1, resultsWithMatch.size());
		assertEquals("X", resultsWithMatch.get(0).getVariable("X").getName());
	}
}