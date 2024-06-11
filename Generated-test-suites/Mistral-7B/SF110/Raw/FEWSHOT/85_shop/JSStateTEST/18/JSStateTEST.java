package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState state = new JSState();
        assertEquals(0, state.size());
        assertNull(state.getLabel());
    }

    @Test
    public void testJSStateConstructorWithTokenizer() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        assertEquals(0, state.size());
        assertNull(state.getLabel());
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        List<JSPredicateForm> elements = Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
        state.addElementsToState(new JSListLogicalAtoms(elements));
        assertEquals(3, state.size());
        JSPredicateForm[] stateElements = new JSPredicateForm[3];
        for (int i = 0; i < state.size(); i++) {
            stateElements[i] = state.elementAt(i);
        }
        assertArrayEquals(new JSPredicateForm[]{"p1", "p2", "p3"}, stateElements);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddElementToStateWithDuplicate() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p1"));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState result = state.apply(plan);
        assertEquals(0, result.size());
        assertNull(result.getLabel());
        JSUtil.flag20("The apply method should not be called as it is not implemented.");
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p1"));
        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("p2"));

        JSOperator op = new JSOperator(new JSOperatorSymbol("OP"), Arrays.asList(new JSFormulaSymbol("A"), new JSFormulaSymbol("B")));
        JSListLogicalAtoms addList = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p3"), new JSPredicateForm("p4")));
        JSListLogicalAtoms delList = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));

        JSListLogicalAtoms opAddList = new JSListLogicalAtoms();
        JSListLogicalAtoms opDelList = new JSListLogicalAtoms();

        JSState result = state1.applyOp(op, new JSSubstitution(), addList, delList);

        assertEquals(2, result.size());
        JSPredicateForm[] resultElements = new JSPredicateForm[2];
        for (int i = 0; i < result.size(); i++) {
            resultElements[i] = result.elementAt(i);
        }
        assertArrayEquals(new JSPredicateForm[]{"p3", "p4"}, resultElements);

        JSState expectedResult = new JSState();
        expectedResult.addElement(new JSPredicateForm("p2"));
        expectedResult.addElementsToState(addList);
        JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p3"), new JSPredicateForm("p4")));
        JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
        JSTState expectedJSState = new JSTate(expectedResult, expectedAddList, expectedDelList);
        assertEquals(expectedJSState, result);
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p(a)"), new JSPredicateForm("p(b)")));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution expectedAnswer = new JSSubstitution();
        expectedAnswer.addElement(new JSSubstitutionEntry("x", "a"));

        JSSubstitution answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, answer);

        JSUtil.flagLevel = 1;
        answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, answer);

        JSUtil.flagLevel = 4;
        answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, answer);

        JSUtil.flagLevel = 5;
        answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, answer);

        JSUtil.flagLevel = 10;
        answer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p(a)"), new JSPredicateForm("p(b)"), new JSPredicateForm("p(c)")));
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution expectedAnswer = new JSSubstitution();
        expectedAnswer.addElement(new JSSubstitutionEntry("x", "a"));

        JSListSubstitution answers = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(1, answers.size());
        assertEquals(expectedAnswer, answers.elementAt(0));

        JSUtil.flagLevel = 1;
        answers = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(1, answers.size());
        assertEquals(expectedAnswer, answers.elementAt(0));

        JSUtil.flagLevel = 4;
        answers = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(1, answers.size());
        assertEquals(expectedAnswer, answers.elementAt(0));

        JSUtil.flagLevel = 5;
        answers = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(1, answers.size());
        assertEquals(expectedAnswer, answers.elementAt(0));

        JSUtil.flagLevel = 10;
        answers = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(1, answers.size());
        assertEquals(expectedAnswer, answers.elementAt(0));
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p(x)"));

        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        assertEquals(0, answers.size());

        JSUtil.flagLevel = 1;
        answers = state.satisfiesTAm(t, alpha);
        assertEquals(0, answers.size());

        JSUtil.flagLevel = 4;
        answers = state.satisfiesTAm(t, alpha);
        assertEquals(0, answers.size());

        JSUtil.flagLevel = 5;
        answers = state.satisfiesTAm(t, alpha);
        assertEquals(0, answers.size());

        JSUtil.flagLevel = 10;
        answers = state.satisfiesTAm(t, alpha);
        assertEquals(0, answers.size());

        state.addElement(new JSPredicateForm("p(a)"));
        answers = state.satisfiesTAm(t, alpha);
        assertEquals(1, answers.size());
        JSSubstitution answer = answers.elementAt(0);
        assertEquals(1, answer.size());
        assertEquals(new JSSubstitutionEntry("x", "a"), answer.elementAt(0));
    }
}