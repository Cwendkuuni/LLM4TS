package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstruction() {
        JSState jsState = new JSState();
        assertNotNull(jsState);
    }

    @Test
    public void testJSStateWithTokenizerConstruction() {
        JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
        assertNotNull(jsState);
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));
        jsState.addElementsToState(s);

        List<JSPredicateForm> expectedList = new ArrayList<>();
        expectedList.add(new JSPredicateForm("p1"));
        expectedList.add(new JSPredicateForm("p2"));

        List<JSPredicateForm> actualList = new ArrayList<>();
        for (Object obj : jsState) {
            actualList.add(((JSPredicateForm) obj));
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testApplyOpNullState() {
        JSOperator op = new JSOperator();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState expectedResult = null;
        JSTState actualResult = new JSState().applyOp(op, null, addL, delL);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testApplyOpEmptyState() {
        JSOperator op = new JSOperator();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSState jsState = new JSState();

        JSTState expectedResult = new JSTState(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
        JSTState actualResult = jsState.applyOp(op, null, addL, delL);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testApplyOpWithValidParams() {
        JSOperator op = new JSOperator();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSState state1 = new JSState();
        state1.add(new JSPredicateForm("p1"));
        state1.add(new JSPredicateForm("p2"));

        JSState state2 = new JSState();
        state2.add(new JSPredicateForm("q1"));
        state2.add(new JSPredicateForm("q2"));

        addL.add(new JSPredicateForm("p3"));
        delL.add(new JSPredicateForm("p2"));

        JSState expectedResult = new JSState();
        expectedResult.add(new JSPredicateForm("p1"));
        expectedResult.add(new JSPredicateForm("p3"));

        JSTate actualResult = state1.applyOp(op, null, addL, delL);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesNullConditions() {
        JSListLogicalAtoms conditions = null;
        JSState state = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution expectedResult = new JSSubstitution();
        expectedResult.assignFailure();

        JSSubstitution actualResult = state.satisfies(conditions, alpha, axioms);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesValidConditions() {
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.add(new JSPredicateForm("p1(x)"));
        conditions.add(new JSPredicateForm("p2(x)"));

        JSState state = new JSState();
        state.add(new JSPredicateForm("p1(a)"));
        state.add(new JSPredicateForm("p2(a)"));

        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("a", "b");

        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution expectedResult = new JSSubstitution();
        expectedResult.add(new JSSubstitution());

        JSSubstitution actualResult = state.satisfies(conditions, alpha, axioms);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesAllNullConditions() {
        JSListLogicalAtoms conditions = null;
        JSState state = new JSState();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution expectedResult = new JSListSubstitution();

        JSListSubstitution actualResult = state.satisfiesAll(conditions, alpha, axioms);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesAllValidConditions() {
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.add(new JSPredicateForm("p1(x)"));
        conditions.add(new JSPredicateForm("p2(x)"));

        JSState state = new JSState();
        state.add(new JSPredicateForm("p1(a)"));
        state.add(new JSPredicateForm("p2(b)"));

        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("a", "b");

        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.add(new JSSubstitution());
        expectedResult.add(new JSSubstitution());

        JSListSubstitution actualResult = state.satisfiesAll(conditions, alpha, axioms);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesTAmNullState() {
        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSState state = null;

        JSListSubstitution expectedResult = new JSListSubstitution();

        JSListSubstitution actualResult = state.satisfiesTAm(t, alpha);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesTAmEmptyState() {
        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();

        JSListSubstitution expectedResult = new JSListSubstitution();

        JSListSubstitution actualResult = state.satisfiesTAm(t, alpha);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSatisfiesTAmWithValidParams() {
        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a)"));

        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.add(new JSSubstitution());

        JSListSubstitution actualResult = state.satisfiesTAm(t, alpha);

        assertEquals(expectedResult, actualResult);
    }
}