package umd.cs.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;
import umd.cs.shop.JSUtil;

public class JSStateTest {

    @Test
    public void testJSStateEmpty() {
        JSState state = new JSState();
        assertEquals(0, state.size());
    }

    @Test
    public void testJSStateAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm("p1"));
        list.addElement(new JSPredicateForm("p2"));
        state.addElementsToState(list);
        assertEquals(2, state.size());
    }

    @Test
    public void testJSStateApplyOp() {
        JSState state1 = new JSState();
        state1.addElement(new JSPredicateForm("p1"));
        state1.addElement(new JSPredicateForm("p2"));

        JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p3"));

        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("p1"));

        JSState expectedState = new JSState();
        expectedState.addElement(new JSPredicateForm("p2"));
        expectedState.addElement(new JSPredicateForm("p3"));

        JSState resultState = state1.applyOp(op, new JSSubstitution(), addL, delL);
        assertEquals(expectedState, resultState);
    }

    @Test
    public void testJSStateSatisfies() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution expectedAnswer = new JSSubstitution();
        expectedAnswer.addElement(new JSVariableForm("x1"));
        expectedAnswer.addElement(new JSVariableForm("x2"));

        JSSubstitution resultAnswer = state.satisfies(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, resultAnswer);
    }

    @Test
    public void testJSStateSatisfiesAll() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1"));
        conds.addElement(new JSPredicateForm("p2"));

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSVariableForm("x1"));
        alpha.addElement(new JSVariableForm("x2"));

        JSListSubstitution expectedAnswer = new JSListSubstitution();
        expectedAnswer.addElement(new JSSubstitution());
        expectedAnswer.addElement(new JSSubstitution());

        JSListSubstitution resultAnswer = state.satisfiesAll(conds, alpha, new JSListAxioms());
        assertEquals(expectedAnswer, resultAnswer);
    }

    @Test
    public void testJSStateSatisfiesTAm() {
        JSState state = new JSState();
        state.addElement(new JSPredicateForm("p1"));
        state.addElement(new JSPredicateForm("p2"));

        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSVariableForm("x1"));

        JSListSubstitution expectedAnswer = new JSListSubstitution();
        expectedAnswer.addElement(new JSSubstitution());
        expectedAnswer.addElement(new JSSubstitution());

        JSListSubstitution resultAnswer = state.satisfiesTAm(t, alpha);
        assertEquals(expectedAnswer, resultAnswer);
    }
}