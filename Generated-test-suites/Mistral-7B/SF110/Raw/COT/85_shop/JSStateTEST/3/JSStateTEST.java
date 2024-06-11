import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.*; // for importing all necessary classes

public class JSStateTest {

    @Test
    public void testJSStateConstructor() {
        JSState state = new JSState();
        assertNull(state.getLabel());
        JSState stateWithLabel = new JSState("testLabel");
        assertEquals("testLabel", stateWithLabel.getLabel());
    }

    @Test
    public void testJSStateAddElementsToState() {
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        JSState state = new JSState();
        list.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        list.addElement(new JSPredicateForm("q(y)", new JSTermList(new JSTerm("y"))));
        state.addElementsToState(list);

        JSListLogicalAtoms expectedList = new JSListLogicalAtoms();
        expectedList.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        expectedList.addElement(new JSPredicateForm("q(y)", new JSTermList(new JSTerm("y"))));

        JSListLogicalAtoms actualList = state.getList();
        assertEquals(expectedList, actualList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateAddElementsToStateWithNullList() {
        JSState state = new JSState();
        state.addElementsToState(null);
    }

    @Test
    public void testJSStateApplyOp_GivenEmptyStateAndEmptyLists() {
        JSState emptyState = new JSState();
        JSListLogicalAtoms addEmptyList = new JSListLogicalAtoms();
        JSListLogicalAtoms delEmptyList = new JSListLogicalAtoms();
        JSTate expectedResult = new JSTate(emptyState, addEmptyList, delEmptyList);

        JSState result = new JSState().applyOp(new JSOperator(1), emptyState, addEmptyList, delEmptyList);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJSStateApplyOp_GivenNonEmptyStateAndEmptyLists() {
        JSState state = new JSState();
        JSListLogicalAtoms addEmptyList = new JSListLogicalAtoms();
        JSListLogicalAtoms delEmptyList = new JSListLogicalAtoms();
        state.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));

        JSState result = new JSState().applyOp(new JSOperator(1), state, addEmptyList, delEmptyList);
        JSListLogicalAtoms expectedResultList = new JSListLogicalAtoms();
        expectedResultList.add(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        JSListLogicalAtoms actualResultList = result.getList();
        assertEquals(expectedResultList, actualResultList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateApplyOp_GivenNullOperator() {
        JSState state = new JSState();
        JSState result = state.applyOp(null, state, new JSListLogicalAtoms(), new JSListLogicalAtoms());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateApplyOp_GivenNullState() {
        JSOperator op = new JSOperator(1);
        JSState result = new JSState().applyOp(op, null, new JSListLogicalAtoms(), new JSListLogicalAtoms());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateApplyOp_GivenNullAddList() {
        JSOperator op = new JSOperator(1);
        JSState state = new JSState();
        JSState result = state.applyOp(op, state, null, new JSListLogicalAtoms());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSStateApplyOp_GivenNullDelList() {
        JSOperator op = new JSOperator(1);
        JSState state = new JSState();
        JSState result = state.applyOp(op, state, new JSListLogicalAtoms(), null);
    }

    @Test
    public void testJSStateSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSPredicateForm expectedAnswer = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution satisfiers = new JSListSubstitution();
        satisfiers.addElement(new JSSubstitution(new JSTerm("x"), new JSTerm("a")));

        axioms.addAxiom(expectedAnswer.applySubstitution(satisfiers));

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        JSSubstitution expectedResult = (JSSubstitution)alpha.clone();
        expectedResult.addElements(satisfiers.elementAt(0));

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJSStateSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSPredicateForm expectedAnswer1 = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSPredicateForm expectedAnswer2 = new JSPredicateForm("q(y)", new JSTermList(new JSTerm("y")));
        JSSubstitution satisfiers1 = new JSListSubstitution();
        satisfiers1.addElement(new JSSubstitution(new JSTerm("x"), new JSTerm("a")));
        JSSubstitution satisfiers2 = new JSListSubstitution();
        satisfiers2.addElement(new JSSubstitution(new JSTerm("y"), new JSTerm("b")));

        axioms.addAxiom(expectedAnswer1.applySubstitution(satisfiers1));
        axioms.addAxiom(expectedAnswer2.applySubstitution(satisfiers2));

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);

        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.addElement(new JSSubstitution(null, null));
        expectedResult.addElement(new JSSubstitution(new JSTerm("x"), new JSTerm("a")));
        expectedResult.addElement(new JSSubstitution(new JSTerm("y"), new JSTerm("b")));

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJSStateSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        JSPredicateForm t = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.addElement(alpha);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJSStateSatisfiesTAm_GivenNullState() {
        JSPredicateForm t = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = new JSState().satisfiesTAm(t, alpha);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSStateSatisfiesTAm_GivenEmptyState() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSStateSatisfiesTAm_GivenStateWithMatchedAtom() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        JSPredicateForm t = new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);

        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.addElement(alpha);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testJSStateSatisfiesTAm_GivenStateWithUnmatchedAtom() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)", new JSTermList(new JSTerm("x"))));
        JSPredicateForm t = new JSPredicateForm("q(x)", new JSTermList(new JSTerm("x")));
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);

        assertTrue(result.isEmpty());
    }
}