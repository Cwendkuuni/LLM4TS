package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitutionAlpha;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

    @Test
    public void testJSState() {
        // Test default constructor
        JSState jsState1 = new JSState();
        assertNull(jsState1.getLabel());
        assertEquals(0, jsState1.size());

        // Test constructor with StreamTokenizer
        JSState jsState2 = new JSState(new JSUtil.MyStreamTokenizer());
        assertNull(jsState2.getLabel());
        assertEquals(0, jsState2.size());

        // Test addElementsToState method
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
        s.addElement(new JSPredicateForm("q(y)", Arrays.asList("y")));

        jsState1 = new JSState();
        jsState1.addElementsToState(s);

        assertEquals(2, jsState1.size());
        JSPredicateForm el1 = jsState1.elementAt(0);
        JSPredicateForm el2 = jsState1.elementAt(1);
        assertEquals("p(x)", el1.getPredicateSymbol().getName());
        assertEquals("q(y)", el2.getPredicateSymbol().getName());
        assertArrayEquals(new String[]{"x"}, el1.getParameters().toArray(new String[0]));
        assertArrayEquals(new String[]{"y"}, el2.getParameters().toArray(new String[0]));

        // Test apply method
        JSPlan pl = new JSPlan();
        JSState result = jsState1.apply(pl);
        assertEquals(jsState1, result);

        // Test applyOp method
        JSOperator op = new JSOperator();
        op.setOperatorSymbol(":and");
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("r(z)", Arrays.asList("z")));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.addElement(new JSPredicateForm("q(y)", Arrays.asList("y")));

        JSState expectedState = new JSState();
        expectedState.addElementsToState(s);
        expectedState.addElement(new JSPredicateForm("r(z)", Arrays.asList("z")));
        JSTState expectedJSTState = new JSTState(expectedState, addL, delL);

        JSState resultState = jsState1.applyOp(op, new JSSubstitutionAlpha(), addL, delL);
        JSTState expectedResultJSTState = new JSTState(expectedState, addL, delL);
        JSTState actualResultJSTState = (JSTState)resultState;

        assertEquals(expectedResultJSTState, actualResultJSTState);

        // Test satisfies method
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution resultSubstitution = jsState1.satisfies(conds, alpha, axioms);
        JSSubstitution expectedSubstitution = new JSSubstitution();
        if (jsState1.size() > 0) {
            expectedSubstitution = alpha.clone();
            expectedSubstitution.addElement(jsState1.elementAt(0).clone());
        }

        assertEquals(expectedSubstitution, resultSubstitution);

        // Test satisfiesAll method
        conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
        conds.addElement(new JSPredicateForm("q(y)", Arrays.asList("y")));
        JSListSubstitution expectedAnswers = new JSListSubstitution();
        JSListSubstitution satisfiers = axioms.TheoremProver(conds, jsState1, alpha, false);

        if (!satisfiers.isEmpty()) {
            JSListSubstitution actualAnswers = jsState1.satisfiesAll(conds, alpha, axioms);
            List<JSSubstitution> actualAnswerList = actualAnswers.getSubstitutions();

            for (JSSubstitution actualAnswer : actualAnswerList) {
                expectedAnswers.addElement(satisfiers.elementAt(0).clone());
                expectedAnswers.addElement(actualAnswer.clone());
            }

            assertEquals(expectedAnswers, actualAnswers);
        }

        // Test satisfiesTAm method
        JSPredicateForm t = new JSPredicateForm("p(x)", Arrays.asList("x"));
        JSSubstitution alpha1 = new JSSubstitution();
        alpha1.addElement(new JSPredicateForm("x = 1", Arrays.asList("x")));

        JSListSubstitution resultListSubstitution = jsState1.satisfiesTAm(t, alpha1);

        if (jsState1.size() > 0) {
            JSSubstitution expectedSubstitution1 = t.matches(jsState1.elementAt(0), alpha1);
            JSSubstitution expectedSubstitution2 = alpha1.clone();

            if (!expectedSubstitution1.fail()) {
                expectedSubstitution2.addElement(expectedSubstitution1);
            }

            assertEquals(expectedSubstitution2, resultListSubstitution);
        }
        else {
            assertTrue(resultListSubstitution.isEmpty());
        }
    }
}