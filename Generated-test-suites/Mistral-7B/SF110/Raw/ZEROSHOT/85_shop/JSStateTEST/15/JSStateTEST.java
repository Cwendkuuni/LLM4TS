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

public class JSStateTest {

	@Test
	public void testJSStateConstruction() {
		JSState state = new JSState();
		assertNotNull(state);

		JSState stateFromTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
		assertNotNull(stateFromTokenizer);
	}

	@Test
	public void testAddElementsToState() {
		JSState state = new JSState();
		JSListLogicalAtoms s = new JSListLogicalAtoms();
		JSPredicateForm atom1 = new JSPredicateForm("p(a)", "p", new String[]{"a"});
		JSPredicateForm atom2 = new JSPredicateForm("q(b)", "q", new String[]{"b"});
		s.addElement(atom1);
		s.addElement(atom2);

		state.addElementsToState(s);

		assertEquals(2, state.size());
		assertTrue(state.contains(atom1));
		assertTrue(state.contains(atom2));
	}

	@Test
	public void testApplyOPNoMatchingElements() {
		JSState state = new JSState();
		JSOperator op = new JSOperator("op", new String[]{"p", "q"}, new String[]{"r"}, new String[]{"a", "b"});
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSState expectedEmptyState = new JSState();

		JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);

		assertEquals(expectedEmptyState, result);
	}

	@Test
	public void testApplyOPMatchingElements() {
		JSState state = new JSState();
		JSOperator op = new JSOperator("op", new String[]{"p(a)", "q(b)"}, new String[]{"r(c)"}, new String[]{"a", "b"});
		JSListLogicalAtoms addL = new JSListLogicalAtoms();
		JSListLogicalAtoms delL = new JSListLogicalAtoms();
		JSListLogicalAtoms expectedState = new JSListLogicalAtoms();
		expectedState.addElement(new JSPredicateForm("r(c)", "r", new String[]{"c"}));

		JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);

		assertEquals(expectedState, result);
	}

	@Test
	public void testSatisfies() {
		JSState state = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSPredicateForm atom1 = new JSPredicateForm("p(a)", "p", new String[]{"a"});
		JSPredicateForm atom2 = new JSPredicateForm("q(b)", "q", new String[]{"b"});
		conds.addElement(atom1);
		conds.addElement(atom2);

		alpha.assign("a", "1");
		alpha.assign("b", "2");

		JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);

		JSState expectedState = new JSState();
		expectedState.addElement(atom1);
		expectedState.addElement(atom2);

		JSSubstitution result = state.satisfies(conds, alpha, axioms);

		assertEquals(expectedState, result.getState());
		assertEquals(satisfiers, result.getSubstitution());
	}

	@Test
	public void testSatisfiesAllFirst() {
		JSState state = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSPredicateForm atom1 = new JSPredicateForm("p(a)", "p", new String[]{"a"});
		JSPredicateForm atom2 = new JSPredicateForm("q(b)", "q", new String[]{"b"});
		conds.addElement(new JSPredicateForm("first(A)", "first", new String[]{"A"}));
		conds.addElement(atom1);
		conds.addElement(atom2);

		alpha.assign("a", "1");
		alpha.assign("b", "2");

		JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, true);

		JSListSubstitution expectedAnswer = new JSListSubstitution();
		expectedAnswer.addElement(new JSSubstitution());

		JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);

		assertEquals(expectedAnswer, result);
	}

	@Test
	public void testSatisfiesAllNotFirst() {
		JSState state = new JSState();
		JSListLogicalAtoms conds = new JSListLogicalAtoms();
		JSSubstitution alpha = new JSSubstitution();
		JSListAxioms axioms = new JSListAxioms();

		JSPredicateForm atom1 = new JSPredicateForm("p(a)", "p", new String[]{"a"});
		JSPredicateForm atom2 = new JSPredicateForm("q(b)", "q", new String[]{"b"});
		conds.addElement(atom1);
		conds.addElement(atom2);

		alpha.assign("a", "1");
		alpha.assign("b", "2");

		JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);

		JSListSubstitution answer1 = new JSListSubstitution();
		answer1.addElement(new JSSubstitution());
		answer1.addElement(new JSSubstitution());

		JSListSubstitution satisfiers2 = new JSListSubstitution();
		satisfiers2.addElement(new JSSubstitution());
		satisfiers2.addElement(new JSSubstitution());
		satisfiers2.addElement(new JSSubstitution());

		JSListSubstitution expectedAnswer = new JSListSubstitution();
		expectedAnswer.addElement(answer1);
		expectedAnswer.addElement(satisfiers2);

		JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);

		assertEquals(expectedAnswer, result);
	}

	@Test
	public void testSatisfiesTAm() {
		JSState state = new JSState();
		JSPredicateForm t = new JSPredicateForm("p(X)", "p", new String[]{"X"});
		JSSubstitution alpha = new JSSubstitution();

		JSPredicateForm atom1 = new JSPredicateForm("p(a)", "p", new String[]{"a"});
		JSPredicateForm atom2 = new JSPredicateForm("p(b)", "p", new String[]{"b"});
		state.addElement(atom1);
		state.addElement(atom2);

		JSListSubstitution answers = state.satisfiesTAm(t, alpha);

		JSListSubstitution expectedAnswer = new JSListSubstitution();
		expectedAnswer.addElement(new JSSubstitution());
		expectedAnswer.addElement(new JSSubstitution());

		assertEquals(expectedAnswer, answers);
	}
}