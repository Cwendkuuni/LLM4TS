package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.contains;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateConstructorEmpty() {
        JSState state = new JSState();
        assertTrue(state.isEmpty());
    }

    @Test
    public void testJSStateConstructorTokenizer() {
        JSState state = new JSState(new StreamTokenizer(new StringReader("")));
        assertTrue(state.isEmpty());
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));

        state.addElementsToState(list);

        List<JSPredicateForm> expected = Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
        List<JSPredicateForm> actual = new ArrayList<>(state.size());
        for (JSPredicateForm el : state) {
            actual.add(el);
        }

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testApplyOpEmptyState() {
        JSState state = new JSState();
        JSOperator op = new JSOperator(1, "op");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();

        JSTState result = state.applyOp(op, null, addList, delList);

        assertNull(result.getState());
        assertNull(result.getAddedList());
        assertNull(result.getDeletedList());
    }

    @Test
    public void testApplyOpEmptyAddList() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSOperator op = new JSOperator(1, "op");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();

        JSTate result = state.applyOp(op, null, addList, delList);

        JSState expectedState = new JSState();
        JSListLogicalAtoms expectedAddedList = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedDeletedList = new JSListLogicalAtoms();

        JSTate actualResult = result;
        JSListLogicalAtoms actualAddedList = actualResult.getAddedList();
        JSListLogicalAtoms actualDeletedList = actualResult.getDeletedList();

        assertSame(expectedState, actualResult.getState());
        assertSame(expectedAddedList, actualAddedList);
        assertSame(expectedDeletedList, actualDeletedList);
    }

    @Test
    public void testApplyOpEmptyDelList() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSOperator op = new JSOperator(1, "op");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();

        JSTate result = state.applyOp(op, null, addList, delList);

        JSState expectedState = new JSState();
        JSListLogicalAtoms expectedAddedList = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedDeletedList = new JSListLogicalAtoms();

        JSTate actualResult = result;
        JSListLogicalAtoms actualAddedList = actualResult.getAddedList();
        JSListLogicalAtoms actualDeletedList = actualResult.getDeletedList();

        assertSame(expectedState, actualResult.getState());
        assertSame(expectedAddedList, actualAddedList);
        assertSame(expectedDeletedList, actualDeletedList);
    }

    @Test
    public void testApplyOpSingleElementState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSOperator op = new JSOperator(1, "op");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("p2"));
        JSListLogicalAtoms delList = new JSListLogicalAtoms();

        JSTate result = state.applyOp(op, null, addList, delList);

        JSState expectedState = new JSState();
        expectedState.addElement(new JSPredicateForm("p1"));
        expectedState.addElement(new JSPredicateForm("p2"));

        JSState actualResult = result.getState();
        List<JSPredicateForm> actualElements = new ArrayList<>(actualResult.size());
        for (JSPredicateForm el : actualResult) {
            actualElements.add(el);
        }

        assertThat(actualElements, is(contains(expectedState.toArray(new JSPredicateForm[0]))));
        assertSame(addList, result.getAddedList());
        assertSame(delList, result.getDeletedList());
    }

    @Test
    public void testApplyOpMultipleElementsState() {
        JSState state = new JSState();
        state.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));

        JSOperator op = new JSOperator(1, "op");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        addList.addElement(new JSPredicateForm("p4"));
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        delList.addElement(new JSPredicateForm("p2"));

        JSTate result = state.applyOp(op, null, addList, delList);

        JSState expectedState = new JSState();
        expectedState.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p3"), new JSPredicateForm("p4")));

        JSState actualResult = result.getState();
        List<JSPredicateForm> actualElements = new ArrayList<>(actualResult.size());
        for (JSPredicateForm el : actualResult) {
            actualElements.add(el);
        }

        assertThat(actualElements, is(contains(expectedState.toArray(new JSPredicateForm[0]))));
        assertSame(addList, result.getAddedList());
        assertSame(delList, result.getDeletedList());
    }

    @Test
    public void testSatisfiesEmptyState() {
        JSState state = new JSState();

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSSubstitution result = state.satisfies(conditions, alpha, axioms);

        assertFalse(result.isAssigned());
    }

    @Test
    public void testSatisfiesSingleElementState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSSubstitution result = state.satisfies(conditions, alpha, axioms);

        assertTrue(result.isAssigned());
        assertTrue(result.isFailure());
    }

    @Test
    public void testSatisfiesAllSingleCondition() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSListSubstitution result = state.satisfiesAll(conditions, alpha, axioms);

        List<JSSubstitution> expected = Arrays.asList(alpha);
        List<JSSubstitution> actual = result.toList();

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testSatisfiesAllMultipleConditionsEmptyState() {
        JSState state = new JSState();

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));
        conditions.addElement(new JSPredicateForm("p2"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSListSubstitution result = state.satisfiesAll(conditions, alpha, axioms);

        assertFalse(result.isAssigned());
    }

    @Test
    public void testSatisfiesAllMultipleConditionsSingleSatisfiedState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSListSubstitution result = state.satisfiesAll(conditions, alpha, axioms);

        List<JSSubstitution> expected = Arrays.asList(alpha);
        List<JSSubstitution> actual = result.toList();

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testSatisfiesAllMultipleConditionsMultipleSatisfiedState() {
        JSState state = new JSState();
        state.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));

        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        conditions.addElement(new JSPredicateForm("p1"));
        conditions.addElement(new JSPredicateForm("p3"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution axioms = new JSSubstitution();

        JSListSubstitution result = state.satisfiesAll(conditions, alpha, axioms);

        List<JSSubstitution> expected = Arrays.asList(new JSSubstitution(), alpha);
        List<JSSubstitution> actual = result.toList();

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testSatisfiesTAmSingleUnsatisfiedElementState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSPredicateForm goal = new JSPredicateForm("p2");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(goal, alpha);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAmSingleSatisfiedElementState() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));

        JSPredicateForm goal = new JSPredicateForm("p1");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(goal, alpha);

        List<JSSubstitution> expected = Collections.singletonList(alpha);
        List<JSSubstitution> actual = result.toList();

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testSatisfiesTAmMultipleSatisfiedElementState() {
        JSState state = new JSState();
        state.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));

        JSPredicateForm goal = new JSPredicateForm("p2");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(goal, alpha);

        List<JSSubstitution> expected = Arrays.asList(new JSSubstitution(), alpha);
        List<JSSubstitution> actual = result.toList();

        assertThat(actual, is(contains(expected.toArray())));
    }

    @Test
    public void testSatisfiesTAmMultipleUnsatisfiedElementState() {
        JSState state = new JSState();
        state.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));

        JSPredicateForm goal = new JSPredicateForm("p4");
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(goal, alpha);

        assertTrue(result.isEmpty());
    }
}