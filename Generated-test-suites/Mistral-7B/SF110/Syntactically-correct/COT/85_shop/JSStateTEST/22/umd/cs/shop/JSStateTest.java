import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*; // For importing all necessary classes

public class JSStateTest {

    @Test
    public void testJSStateConstructor() {
        JSState jsState = new JSState();
        assertNotNull(jsState);
    }

    @Test
    public void testJSStateWithTokenizerConstructor() {
        JSState jsState = new JSState(new StreamTokenizer());
        assertNotNull(jsState);
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        s.addElement(new JSPredicateForm("P(b)", new String[]{"P", "b"}));
        JSState jsState = new JSState();
        jsState.addElementsToState(s);
        JSPredicateForm expectedElement = new JSPredicateForm("P(a)", new String[]{"P", "a"});
        JSPredicateForm actualElement = jsState.elementAt(0);
        assertEquals(expectedElement.getLabel(), actualElement.getLabel());
        assertEquals(expectedElement.getArguments()[0], actualElement.getArguments()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementsToStateWithNullArgument() {
        JSState jsState = new JSState();
        jsState.addElementsToState(null);
    }

    @Test
    public void testApplyOp_HappyPath() {
        JSOperator op = new JSOperator(new String[]{"+:-", ":Predicate", ":Predicate", ":Predicate"}, new String[]{"Add", "A", "B", "C"});
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("Q(b)", new String[]{"Q", "b"}));
        JSState jsState = new JSState();
        JSState expectedResult = new JSState();
        expectedResult.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        JSState result = jsState.applyOp(op, alpha, addL, delL);
        assertEquals(expectedResult.size(), result.size());
        for (short i = 0; i < expectedResult.size(); ++i) {
            JSPredicateForm expectedElement = expectedResult.elementAt(i);
            JSPredicateForm actualElement = result.elementAt(i);
            assertEquals(expectedElement.getLabel(), actualElement.getLabel());
            assertEquals(expectedElement.getArguments()[0], actualElement.getArguments()[0]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyOp_NullArgument() {
        JSOperator op = new JSOperator(new String[]{"+:-", ":Predicate", ":Predicate", ":Predicate"}, new String[]{"Add", "A", "B", "C"});
        JSState jsState = new JSState();
        jsState.applyOp(op, null, new JSListLogicalAtoms(), new JSListLogicalAtoms());
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        jsState.addElement(new JSPredicateForm("P(b)", new String[]{"P", "b"}));
        JSSubstitution expectedAnswer = new JSSubstitution();
        expectedAnswer.addElement(new JSSubstitutionElement(new String[]{"a"}, new String[]{"constant_1"}));
        JSSubstitution answer = jsState.satisfies(conds, new JSSubstitution(), new JSListAxioms());
        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        conds.addElement(new JSPredicateForm("P(b)", new String[]{"P", "b"}));
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        jsState.addElement(new JSPredicateForm("P(b)", new String[]{"P", "b"}));
        JSSubstitution expectedAnswer = new JSSubstitution();
        expectedAnswer.addElement(new JSSubstitutionElement(new String[]{"a"}, new String[]{"constant_1"}));
        expectedAnswer.addElement(new JSSubstitutionElement(new String[]{"b"}, new String[]{"constant_2"}));
        JSSubstitution answer = jsState.satisfiesAll(conds, new JSSubstitution(), new JSListAxioms());
        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm("P(X)", new String[]{"P", "X"});
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("P(a)", new String[]{"P", "a"}));
        JSSubstitution answer = jsState.satisfiesTAm(t, new JSSubstitution());
        assertTrue(answer.size() > 0);
    }

    @Test
    public void testSatisfiesTAm_EmptyState() {
        JSPredicateForm t = new JSPredicateForm("P(X)", new String[]{"P", "X"});
        JSState jsState = new JSState();
        JSSubstitution answer = jsState.satisfiesTAm(t, new JSSubstitution());
        assertTrue(answer.isEmpty());
    }
}