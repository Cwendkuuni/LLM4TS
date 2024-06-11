package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testConstructors() {
        JSState jsState = new JSState();
        assertNull(jsState.getLabel());
        JSState jsStateWithTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
        assertNull(jsStateWithTokenizer.getLabel());
    }

    @Test
    public void testAddElementsToState() {
        final JSState jsState = new JSState();
        final JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));

        jsState.addElementsToState(s);

        assertTrue(jsState.size() == 2);

        for (int i = 0; i < jsState.size(); ++i) {
            assertEquals(s.elementAt(i), jsState.elementAt(i));
        }
    }

    @Test
    public void testApply() {
        JSState jsState = new JSState();
        JSPlan jsPlan = new JSPlan();
        JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
        final JSState result = jsState.apply(jsPlan);
        assertNull(result);
    }

    @Test
    public void testApplyOp() {
        JSState jsState = new JSState();
        JSOperator jsOperator = new JSOperator("op");
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        jsState.applyOp(jsOperator, new JSSubstitution(), addL, delL);
        JSUtil.flag20("<STATE>.applyOp(<JSOperator>, <JSSubstitution>, <JSListLogicalAtoms>, <JSListLogicalAtoms>) not implemented yet");
        assertNull(jsState);
    }

    @Test
    public void testSatisfies() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertNull(result);
    }

    @Test
    public void testSatisfiesAll() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);
        assertNull(answers);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState jsState = new JSState();
        JSPredicateForm t = new JSPredicateForm("t");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
        assertTrue(answers.isEmpty());
    }

    @Test
    public void testContains() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p1"));

        assertTrue(jsState.contains(s.elementAt(0)));

        JSPredicateForm p2 = new JSPredicateForm("p2");
        assertFalse(jsState.contains(p2));
    }

    @Test
    public void testInsertElementAt() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        jsState.insertElementAt(s.elementAt(0), 0);

        assertEquals(jsState.size(), 1);
        assertEquals(jsState.elementAt(0), s.elementAt(0));
    }

    @Test
    public void testSize() {
        JSState jsState = new JSState();
        assertFalse(jsState.size() > 0);
    }

    @Test
    public void testElementAt() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p1"));

        assertEquals(jsState.elementAt(0), s.elementAt(0));
    }

    @Test
    public void testEquals() {
        JSState jsState1 = new JSState();
        JSState jsState2 = new JSState();

        assertFalse(jsState1.equals(jsState2));

        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        jsState1.addElement(new JSPredicateForm("p1"));
        jsState2.addElement(new JSPredicateForm("p1"));

        assertTrue(jsState1.equals(jsState2));
    }

    @Test
    public void testPrint() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p1"));

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("JSState{size=1} [");
        expectedOutput.add("  JSListLogicalAtoms{size=1} [p1]");
        expectedOutput.add("]");

        JSUtil.printState(jsState);

        for (String line : expectedOutput) {
            assertTrue(System.lineSeparator().equals(line.substring(line.lastIndexOf("\n"))));
            assertTrue(line.containsAllOf(System.getProperty("line.separator").split(System.getProperty("line.separator"))));
            assertTrue(line.containsAllOf(line.split(System.lineSeparator())));
            assertTrue(line.containsAllOf(line.split(System.getProperty("line.separator"))));
            assertEquals(line.substring(0, line.lastIndexOf(System.getProperty("line.separator"))), "JSState{size=1} [");
        }
    }
}