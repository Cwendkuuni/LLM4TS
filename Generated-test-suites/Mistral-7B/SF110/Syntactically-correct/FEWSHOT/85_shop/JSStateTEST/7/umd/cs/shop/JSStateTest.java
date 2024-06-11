package umd.cs.shop;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

    @Test
    public void testJSStateEmptyConstructor() {
        JSState jsState = new JSState();
        assertTrue(jsState.isEmpty());
    }

    @Test
    public void testJSStateConstructorWithTokenizer() {
        JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
        assertTrue(jsState.isEmpty());
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.add(new JSPredicateForm("p1"));
        s.add(new JSPredicateForm("p2"));

        jsState.addElementsToState(s);

        List<JSPredicateForm> expected = new ArrayList<>(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        List<JSPredicateForm> actual = jsState.getElements();

        assertEquals(expected, actual);
    }

    @Test
    public void testApplyOp_NullOp() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));

        assertThrows(NullPointerException.class, () -> jsState.applyOp(null, null, null, null));
    }

    @Test
    public void testApplyOp_NullAddL() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));

        JSOperator op = new JSOperator(new JSPredicateForm("op"), new ArrayList<>());
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        assertThrows(NullPointerException.class, () -> jsState.applyOp(op, alpha, null, delL));
    }

    @Test
    public void testApplyOp_NullDelL() {
        JSState jsState = new JSState();
        jsState.add(new JSPredicateForm("p1"));
        jsState.add(new JSPredicateForm("p2"));

        JSOperator op = new JSOperator(new JSPredicateForm("op"), new ArrayList<>());
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();

        assertThrows(NullPointerException.class, () -> jsState.applyOp(op, alpha, addL, null));
    }

    @Test
    public void testApplyOp() {
        JSState state1 = new JSState();
        state1.add(new JSPredicateForm("p1"));
        state1.add(new JSPredicateForm("p2"));

        JSOperator op = new JSOperator(new JSPredicateForm("op"), new ArrayList<>());
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.add(new JSPredicateForm("q1"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        delL.add(new JSPredicateForm("p1"));

        JSState state2 = state1.applyOp(op, alpha, addL, delL);

        List<JSPredicateForm> expectedAdd = new ArrayList<>(Arrays.asList(new JSPredicateForm("p2"), new JSPredicateForm("q1")));
        List<JSPredicateForm> expectedDel = new ArrayList<>();

        List<JSPredicateForm> actualAdd = state2.getElements();
        List<JSPredicateForm> actualDel = new JSListLogicalAtoms(state2.getLabel()).getElements();

        assertEquals(expectedAdd, actualAdd);
        assertEquals(expectedDel, actualDel);
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a, b)"));
        state.add(new JSPredicateForm("q(c, d)"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(x, y)"));
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("x", new JSTerm("a"));
        alpha.assign("y", new JSTerm("b"));

        JSSubstitution expected = new JSSubstitution();
        expected.assign("x", new JSTerm("a"));
        expected.assign("y", new JSTerm("b"));

        JSSubstitution result = state.satisfies(conds, alpha, new JSListAxioms());

        assertEquals(expected, result);
    }

    @Test
    public void testSatisfies_NoSatisfier() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a, b)"));
        state.add(new JSPredicateForm("q(c, d)"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("r(e, f)"));
        JSSubstitution alpha = new JSSubstitution();

        JSSubstitution result = state.satisfies(conds, alpha, new JSListAxioms());

        assertTrue(result.isFailure());
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a, b)"));
        state.add(new JSPredicateForm("q(c, d)"));

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(new JSPredicateForm("p(x, y)"));
        conds.add(new JSPredicateForm("q(u, v)"));
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("x", new JSTerm("a"));
        alpha.assign("y", new JSTerm("b"));

        JSListSubstitution expected = new JSListSubstitution();
        expected.add(new JSSubstitution());
        expected.add(new JSSubstitution());

        JSListSubstitution result = state.satisfiesAll(conds, alpha, new JSListAxioms());

        assertEquals(expected, result);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a, b)"));
        state.add(new JSPredicateForm("q(c, d)"));

        JSPredicateForm t = new JSPredicateForm("p(X, Y)");

        JSListSubstitution expected = new JSListSubstitution();

        if (JSJshopVars.flagLevel > 7) {
            System.out.println("Applying t to p(a, b):");
        }

        JSSubstitution result1 = t.matches(new JSPredicateForm("p(a, b)"), new JSSubstitution());
        expected.add(result1);

        if (JSJshopVars.flagLevel > 7) {
            System.out.println("Applying t to p(a, b) failed:");
        }

        JSSubstitution result2 = t.matches(new JSPredicateForm("q(c, d)"), result1);

        if (JSJshopVars.flagLevel > 7) {
            System.out.println("Applying t to q(c, d):");
        }

        assertEquals(expected, state.satisfiesTAm(t, new JSSubstitution()));
    }

    @Test
    public void testSatisfiesTAm_NoMatch() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p(a, b)"));
        state.add(new JSPredicateForm("q(c, d)"));

        JSPredicateForm t = new JSPredicateForm("r(X, Y)");

        JSListSubstitution expected = new JSListSubstitution();

        assertEquals(expected, state.satisfiesTAm(t, new JSSubstitution()));
    }

    @Test
    public void testGetLabel() {
        JSState state = new JSState();
        state.label = "MyLabel";

        String expected = "MyLabel";
        String actual = state.getLabel();

        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p1"));
        state.add(new JSPredicateForm("p2"));

        int expected = 2;
        int actual = state.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        JSState state = new JSState();

        boolean expected = true;
        boolean actual = state.isEmpty();

        assertEquals(expected, actual);

        state.add(new JSPredicateForm("p1"));

        expected = false;
        actual = state.isEmpty();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetElements() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p1"));
        state.add(new JSPredicateForm("p2"));

        List<JSPredicateForm> expected = new ArrayList<>(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
        List<JSPredicateForm> actual = state.getElements();

        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p1"));
        state.add(new JSPredicateForm("p2"));

        JSPredicateForm p3 = new JSPredicateForm("p3");
        JSPredicateForm p1 = new JSPredicateForm("p1");

        boolean expected1 = false;
        boolean actual1 = state.contains(p3);

        assertFalse(expected1);
        assertFalse(actual1);

        boolean expected2 = true;
        boolean actual2 = state.contains(p1);

        assertTrue(expected2);
        assertTrue(actual2);
    }

    @Test
    public void testInsertElementAt() {
        JSState state = new JSState();
        state.add(new JSPredicateForm("p1"));
        state.add(new JSPredicateForm("p2"));

        JSPredicateForm p3 = new JSPredicateForm("p3");
        int position = 1;

        state.insertElementAt(p3, position);

        List<JSPredicateForm> expected = new ArrayList<>(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p3"), new JSPredicateForm("p2")));
        List<JSPredicateForm> actual = state.getElements();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JSState state1 = new JSState();
        state1.add(new JSPredicateForm("p1"));
        state1.add(new JSPredicateForm("p2"));

        JSState state2 = new JSState();
        state2.add(new JSPredicateForm("p1"));
        state2.add(new JSPredicateForm("p2"));

        JSState state3 = new JSState();
        state3.add(new JSPredicateForm("p3"));
        state3.add(new JSPredicateForm("p2"));

        boolean expected1 = true;
        boolean actual1 = state1.equals(state2);

        assertTrue(expected1);
        assertTrue(actual1);

        boolean expected2 = false;
        boolean actual2 = state1.equals(state3);

        assertFalse(expected2);
        assertFalse(actual2);
    }
}