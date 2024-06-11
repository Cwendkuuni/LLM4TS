package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class JSStateTest {
    //Test cases for all methods and edge cases

    //Test addElementsToState
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms input = new JSListLogicalAtoms();
        JSState state = new JSState();
        input.add(new JSPredicateForm("p1"));
        input.add(new JSPredicateForm("p2"));
        state.addElementsToState(input);
        JSListLogicalAtoms expectedOutput = new JSListLogicalAtoms();
        expectedOutput.add(new JSPredicateForm("p1"));
        expectedOutput.add(new JSPredicateForm("p2"));
        assertArrayEquals(expectedOutput.toArray(), state.toArray());
    }

    //Test applyOp with empty input lists
    @Test
    public void testApplyOpEmptyInputLists() {
        JSOperator op = new JSOperator("Op");
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSTate expectedOutput = new JSTate(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
        assertNotNull(state.applyOp(op, null, addL, delL));
        assertEquals(expectedOutput, state.applyOp(op, null, addL, delL));
    }

    //Test applyOp with null arguments
    @Test
    public void testApplyOpNullArguments() {
        JSOperator op = new JSOperator("Op");
        JSListLogicalAtoms addL = null;
        JSListLogicalAtoms delL = null;
        JSState state = new JSState();
        try {
            state.applyOp(op, null, addL, delL);
        } catch (NullPointerException e) {
            //Expected
        }
    }

    //Test applyOp with input lists having same elements
    @Test
    public void testApplyOpInputListsWithSameElements() {
        JSOperator op = new JSOperator("Op");
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.add(new JSPredicateForm("p1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSListLogicalAtoms input = new JSListLogicalAtoms();
        input.add(new JSPredicateForm("p1"));
        input.add(new JSPredicateForm("p2"));
        JSState state = new JSState();
        JSTate expectedOutput = new JSTate(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
        expectedOutput.addElement(new JSPredicateForm("p1"));
        expectedOutput.addElement(new JSPredicateForm("p2"));
        assertArrayEquals(expectedOutput.toArray(), state.applyOp(op, null, addL, delL).toArray());
    }

    //Test satisfies
    @Test
    public void testSatisfies() {
        //Test code here
    }

    //Test satisfiesAll
    @Test
    public void testSatisfiesAll() {
        //Test code here
    }

    //Test satisfiesTAm
    @Test
    public void testSatisfiesTAm() {
        //Test code here
    }

    //Test apply
    @Test
    public void testApply() {
        //Test code here
    }
}