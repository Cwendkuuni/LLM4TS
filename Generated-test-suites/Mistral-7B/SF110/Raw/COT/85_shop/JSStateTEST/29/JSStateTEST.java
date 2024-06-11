import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*; // for importing all necessary classes

public class JSStateTest {

    @Test
    public void testJSStateConstructor() {
        JSState jsState = new JSState();
        assertNull(jsState.getLabel());
        assertTrue(jsState.isEmpty());
    }

    @Test
    public void testJSStateConstructorWithTokenizer() {
        JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
        assertNull(jsState.getLabel());
        assertTrue(jsState.isEmpty());
    }

    @Test
    public void testAddElementsToState() {
        JSState jsState = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm("p1"));
        s.addElement(new JSPredicateForm("p2"));

        jsState.addElementsToState(s);

        assertEquals(2, jsState.size());
        for (short i = 0; i < jsState.size(); ++i) {
            assertEquals(s.elementAt(i), jsState.elementAt(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementsToStateWithNullList() {
        JSState jsState = new JSState();
        jsState.addElementsToState(null);
    }

    @Test
    public void testApply() {
        JSState jsState = new JSState();
        JSPlan pl = new JSPlan();
        JSState result = jsState.apply(pl);
        assertTrue(result.isEmpty());
        JSUtil.flag20("This statement should not be executed as 'apply' method is not implemented yet.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testApplyOpWithEmptyState() {
        JSState jsState = new JSState();
        JSOperator op = new JSAddOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState result = jsState.applyOp(op, alpha, addL, delL);
    }

    @Test
    public void testApplyOp() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p2"));

        JSOperator op = new JSAddOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        addL.addElement(new JSPredicateForm("p3"));
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState expectedResult = new JSState();
        expectedResult.addElement(new JSPredicateForm("p1"));
        expectedResult.addElement(new JSPredicateForm("p2"));
        expectedResult.addElement(new JSPredicateForm("p3"));

        JSState result = jsState.applyOp(op, alpha, addL, delL);

        assertEquals(expectedResult.size(), result.size());
        for (short i = 0; i < result.size(); ++i) {
            assertEquals(expectedResult.elementAt(i), result.elementAt(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSatisfiesWithEmptyList() {
        JSState jsState = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = jsState.satisfies(conds, alpha, axioms);
    }

    @Test
    public void testSatisfies() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1(x)"));
        jsState.addElement(new JSPredicateForm("p2(x)"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1(a)"));
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("a", new JSTermVariable("x"));
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertEquals(1, result.size());
        assertEquals(alpha, result.elementAt(0));
    }

    @Test
    public void testSatisfiesAll() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1(x, y)"));
        jsState.addElement(new JSPredicateForm("p2(x, y)"));
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1(a, b)"));
        conds.addElement(new JSPredicateForm("p2(a, c)"));
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign("a", new JSTermVariable("x"));
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        assertEquals(0, result.size());

        jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1(x, y)"));
        jsState.addElement(new JSPredicateForm("p2(x, y)"));
        conds = new JSListLogicalAtoms();
        conds.addElement(new JSPredicateForm("p1(a, b)"));
        conds.addElement(new JSPredicateForm("p1(a, c)"));
        alpha = new JSSubstitution();
        alpha.assign("a", new JSTermVariable("x"));
        axioms = new JSListAxioms();
        result = jsState.satisfiesAll(conds, alpha, axioms);
        assertEquals(1, result.size());
        JSSubstitution firstResult = result.elementAt(0);
        firstResult.assign("b", new JSTermConstant("b1"));
        firstResult.assign("c", new JSTermConstant("c1"));

        JSListSubstitution secondResult = new JSListSubstitution();
        secondResult.addElement(firstResult);
        secondResult.addElement(new JSSubstitution());

        JSListSubstitution expectedResult = new JSListSubstitution();
        expectedResult.addElement(firstResult);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSatisfiesTAmWithEmptyState() {
        JSState jsState = new JSState();
        JSPredicateForm t = new JSPredicateForm("p(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1(x)"));
        jsState.addElement(new JSPredicateForm("p2(x)"));
        JSPredicateForm t = new JSPredicateForm("p1(x)");
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

        assertEquals(2, result.size());
        for (int i = 0; i < result.size(); ++i) {
            JSSubstitution subResult = result.elementAt(i);
            assertEquals(1, subResult.size());
            JSSubstitution firstSubResult = subResult.elementAt(0);
            assertEquals(1, firstSubResult.size());
            assertEquals(alpha, firstSubResult);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsWithNullElement() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1"));
        boolean result = jsState.contains(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementAtWithNegativeIndex() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p2"));
        jsState.getElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementAtWithOutOfBoundsIndex() {
        JSState jsState = new JSState();
        jsState.addElement(new JSPredicateForm("p1"));
        jsState.addElement(new JSPredicateForm("p2"));
        jsState.getElementAt(2);
    }
}