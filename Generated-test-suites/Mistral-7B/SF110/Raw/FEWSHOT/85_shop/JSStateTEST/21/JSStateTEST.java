package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState state = new JSState();
        assertTrue(state.size() == 0);
    }

    @Test
    public void testJSStateStreamTokenizerConstructor() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        assertTrue(state.size() == 0);
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));
        state.addElementsToState(list);

        List<JSPredicateForm> expectedList = new ArrayList<>();
        expectedList.add(new JSPredicateForm("p1"));
        expectedList.add(new JSPredicateForm("p2"));

        List<JSPredicateForm> actualList = new ArrayList<>();
        for (Object obj : state) {
            actualList.add((JSPredicateForm) obj);
        }

        Collections.sort(expectedList);
        Collections.sort(actualList);

        assertTrue(expectedList.equals(actualList));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan() {
            @Override
            public JSOperator head() {
                return null;
            }
        };
        JSState expectedState = new JSState();

        JSState resultState = state.apply(plan);
        assertEquals(expectedState, resultState);
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p1"));
        state1.addElement(new JSPredicateForm("p2"));

        JSState state2 = new JSState();
        state2.addElement(new JSPredicateForm("q1"));
        state2.addElement(new JSPredicateForm("q2"));

        JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms(), new JSOperator.BindingFunction() {
            @Override
            public JSListLogicalAtoms apply(JSListLogicalAtoms a, JSListLogicalAtoms b) {
                JSListLogicalAtoms result = new JSListLogicalAtoms();
                result.addAll(a);
                result.addAll(b);
                return result;
            }
        });

        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("p3"));

        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        delList.addElement(new JSPredicateForm("p2"));

        JSState resultState = state1.applyOp(op, new JSSubstitution(), addList, delList);

        JSState expectedState = new JSState();
        expectedState.addElement(new JSPredicateForm("p1"));
        expectedState.addElement(new JSPredicateForm("p3"));
        expectedState.addElement(new JSPredicateForm("p2")); // Shouldn't be in the resulting state
        expectedState.addElement(new JSPredicateForm("q1"));
        expectedState.addElement(new JSPredicateForm("q2"));

        List<JSPredicateForm> expectedList = new ArrayList<>();
        expectedList.add(new JSPredicateForm("p1"));
        expectedList.add(new JSPredicateForm("p3"));

        List<JSPredicateForm> actualList = new ArrayList<>();
        for (Object obj : resultState) {
            actualList.add((JSPredicateForm) obj);
        }

        Collections.sort(expectedList);
        Collections.sort(actualList);

        assertTrue(expectedList.equals(actualList));
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = state.satisfies(conditions, alpha, new JSListAxioms());
        assertTrue(result.size() > 0);

        JSSubstitution emptySubstitution = new JSSubstitution();
        result = state.satisfies(new JSListLogicalAtoms(), emptySubstitution, new JSListAxioms());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = state.satisfiesAll(conditions, alpha, new JSListAxioms());
        assertTrue(result.size() > 0);

        JSListSubstitution emptyListSubstitution = new JSListSubstitution();
        result = state.satisfiesAll(new JSListLogicalAtoms(), alpha, new JSListAxioms());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));

        JSPredicateForm t = new JSPredicateForm("p1(X)");

        JSListSubstitution result = state.satisfiesTAm(t, new JSSubstitution());
        assertTrue(result.isEmpty());

        state.addElement(new JSPredicateForm("p1(a)"));
        result = state.satisfiesTAm(t, new JSSubstitution());
        assertTrue(result.size() > 0);
    }

    private void assertEquals(JSState expected, JSState result) {
        List<JSPredicateForm> expectedList = new ArrayList<>();
        List<JSPredicateForm> resultList = new ArrayList<>();

        for (Object obj : expected) {
            expectedList.add((JSPredicateForm) obj);
        }

        for (Object obj : result) {
            resultList.add((JSPredicateForm) obj);
        }

        Collections.sort(expectedList);
        Collections.sort(resultList);

        assertEquals(expectedList, resultList);
    }
}