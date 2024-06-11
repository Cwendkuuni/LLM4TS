import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSSubstitutionList;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSPredicateFormFactory;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

    @Test
    public void testJSStateConstructor() {
        JSState jsState = new JSState();
        assertNotNull(jsState);

        JSState jsStateWithTokenizer = new JSState(new StreamTokenizer());
        assertNotNull(jsStateWithTokenizer);
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        s.add(p1);
        s.add(p2);

        JSState jsState = new JSState();
        jsState.addElementsToState(s);

        Object[] expected = {p1, p2};
        Object[] actual = jsState.toArray();
        assertNotNull(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testApplyOp_EmptyState() {
        JSState jsState = new JSState();

        JSOperator op = new JSOperator("op", Arrays.asList(JSPredicateFormFactory.parse("p(X)")), Arrays.asList(JSPredicateFormFactory.parse("q(Y)")));
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);

        assertNull(result.getState());
        assertSame(addL, result.getAddedList());
        assertSame(delL, result.getDeletedList());
    }

    @Test
    public void testApplyOp_StateWithElements() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        jsState.add(p1);
        jsState.add(p2);

        JSOperator op = new JSOperator("op", Arrays.asList(JSPredicateFormFactory.parse("p(X)")), Arrays.asList(JSPredicateFormFactory.parse("r(Z)")));
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTate result = jsState.applyOp(op, new JSSubstitution(), addL, delL);

        Object[] expectedState = {p2};
        Object[] actualState = result.getState().toArray();
        assertNotNull(actualState);
        assertEquals(expectedState.length, actualState.length);
        for (int i = 0; i < expectedState.length; ++i) {
            assertEquals(expectedState[i], actualState[i]);
        }

        assertSame(addL, result.getAddedList());
        assertSame(delL, result.getDeletedList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyOp_StateWithElements_ThrowsException() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        jsState.add(p1);
        jsState.add(p2);

        JSOperator op = new JSOperator("op", Arrays.asList(JSPredicateFormFactory.parse("p(X)")), Arrays.asList(JSPredicateFormFactory.parse("p(X)")));
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        jsState.applyOp(op, new JSSubstitution(), addL, delL);
    }

    @Test
    public void testSatisfies() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        jsState.add(p1);
        jsState.add(p2);

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(p1);

        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

        assertTrue(result.size() > 0);
        assertSame(alpha, result);
    }

    @Test
    public void testSatisfies_DoesNotSatisfy() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(JSPredicateFormFactory.parse("q(Y)"));

        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesAll() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        jsState.add(p1);
        jsState.add(p2);

        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.add(JSPredicateFormFactory.parse("p(X)"));
        conds.add(JSPredicateFormFactory.parse("q(Y)"));

        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

        List<JSSubstitution> expected = Arrays.asList(alpha, new JSSubstitution());
        List<JSSubstitution> actual = new ArrayList<>(result);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testSatisfiesTAm_EmptyState() {
        JSState jsState = new JSState();

        JSPredicateForm goal = JSPredicateFormFactory.parse("p(X)");

        JSListSubstitution result = jsState.satisfiesTAm(goal, new JSSubstitution());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSatisfiesTAm_StateWithElements() {
        JSState jsState = new JSState();
        JSPredicateForm p1 = JSPredicateFormFactory.parse("p(X)");
        JSPredicateForm p2 = JSPredicateFormFactory.parse("q(Y)");
        jsState.add(p1);
        jsState.add(p2);

        JSPredicateForm goal = JSPredicateFormFactory.parse("p(X)");

        JSListSubstitution result = jsState.satisfiesTAm(goal, new JSSubstitution());

        List<JSSubstitution> expected = Arrays.asList(new JSSubstitution(), new JSSubstitution());
        List<JSSubstitution> actual = new ArrayList<>(result);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}