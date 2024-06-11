package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JSTermTest {

    JSTerm term;
    JSTerm term1;
    JSTerm term2;
    JSTerm term3;
    JSTerm term4;
    JSTerm term5;
    JSTerm term6;
    JSTerm term7;
    JSTerm term8;
    JSTerm term9;
    JSTerm term10;

    @Before
    public void setUp() {
        term = new JSTerm();
        term1 = new JSTerm(new StreamTokenizer(new StringReader("(+ x y)")));
        term2 = new JSTerm(new StreamTokenizer(new StringReader("(- x y)")));
        term3 = new JSTerm(new StreamTokenizer(new StringReader("(* x y)")));
        term4 = new JSTerm(new StreamTokenizer(new StringReader("(/ x y)")));
        term5 = new JSTerm(new StreamTokenizer(new StringReader("(+ x)")));
        term6 = new JSTerm(new StreamTokenizer(new StringReader("(* x 3)")));
        term7 = new JSTerm(new StreamTokenizer(new StringReader("(- x 3)")));
        term8 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3.5)")));
        term9 = new JSTerm(new StreamTokenizer(new StringReader("(+ x 'a')")));
        term10 = new JSTerm(new StreamTokenizer(new StringReader("(+ x nil)")));
    }

    @Test
    public void testEmptyConstructor() {
        assertEquals(new JSTerm(), term);
    }

    @Test
    public void testConstructorWithStreamTokenizer() {
        assertEquals(term1, term);
    }

    @Test(expected = JSParserError.class)
    public void testInvalidTermConstruction() {
        new JSTerm(new StreamTokenizer(new StringReader("InvalidTerm")));
    }

    @Test
    public void testIsVariable() {
        assertTrue(term9.isVariable());
        assertFalse(term1.isVariable());
    }

    @Test
    public void testIsConstant() {
        assertFalse(term1.isConstant());
        assertTrue(term5.isConstant());
    }

    @Test
    public void testIsFunction() {
        assertTrue(term1.isFunction());
        assertFalse(term5.isFunction());
    }

    @Test
    public void testIsEval() {
        assertFalse(term1.isEval());
        assertTrue(term8.isEval());
    }

    @Test
    public void testMakeFunction() {
        term.makeFunction();
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
    }

    @Test
    public void testMakeVariable() {
        term.makeVariable();
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
    }

    @Test
    public void testMakeConstant() {
        term.makeConstant();
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isFunction());
    }

    @Test
    public void testMakeEval() {
        term.makeEval(true);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
    }

    @Test
    public void testSize() {
        assertEquals(1, term.size());
        assertEquals(3, term1.size());
    }

    @Test
    public void testAddElement() {
        term.addElement("x");
        assertEquals("x", term.elementAt(0));
        assertEquals(1, term.size());

        term.addElement(term2);
        assertEquals("x", term.elementAt(0));
        assertEquals("y", term.elementAt(1).elementAt(0));
        assertEquals("-", term.elementAt(1).elementAt(1));
        assertEquals(3, term.size());
    }

    @Test
    public void testCloneT() {
        JSTerm clonedTerm = term1.cloneT();
        assertEquals(term1, clonedTerm);
    }

    @Test
    public void testApplySubstitutionT() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairTerm("x", new JSTerm("y")));

        JSTerm result = term1.applySubstitutionT(alpha);
        assertEquals(new JSTerm("y"), result);
    }

    @Test
    public void testMatches() {
        JSTerm t = new JSTerm(new StreamTokenizer(new StringReader("(+ x y)")));
        JSTerm a = new JSTerm(new StreamTokenizer(new StringReader("(+ x z)")));
        JSTerm b = new JSTerm(new StreamTokenizer(new StringReader("(+ y z)")));

        JSSubstitution substitution = term1.matches(t);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(a);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(b);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(new JSTerm(new StreamTokenizer(new StringReader("(* x x)"))));
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testEquals() {
        assertTrue(term1.equals(term1));
        assertFalse(term1.equals(term2));
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
        assertFalse(term1.equals(term7));
        assertFalse(term1.equals(term8));
        assertFalse(term1.equals(term9));
        assertFalse(term1.equals(term10));

        term1.makeVariable();
        term2.makeVariable();
        term1.setElementAt(0, "x");
        term2.setElementAt(0, "y");
        assertFalse(term1.equals(term2));

        term1.makeConstant();
        term2.makeConstant();
        term1.setElementAt(0, 5);
        term2.setElementAt(0, 5);
        assertTrue(term1.equals(term2));
    }

    @Test
    public void testToString() {
        assertEquals("(+ x y)", term1.toStr().toString());
        assertEquals("x", term5.toStr().toString());
    }

    @Test
    public void testIsGround() {
        assertTrue(term5.isGround());
        assertFalse(term1.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm standardizedTerm1 = term1.standardizerTerm();
        assertEquals("(+ x 'x1) (y 'x2)", standardizedTerm1.toStr().toString());

        JSTerm standardizedTerm2 = term2.standardizerTerm();
        assertEquals("(- x 'x1) (y 'x3)", standardizedTerm2.toStr().toString());
    }

    @Test
    public void testCall() {
        assertEquals(new JSTerm("3.5"), term8.call());
        assertEquals(new JSTerm("a"), term9.call());
        assertEquals(new JSTerm("nil"), term10.call());

        assertEquals(new JSTerm("5"), term5.call());
        assertEquals(new JSTerm("9"), term6.call());
        assertEquals(new JSTerm("-3"), term7.call());

        JSTerm term11 = new JSTerm(new StreamTokenizer(new StringReader("(+ x (* y z) (+ w x))")));
        assertEquals(new JSTerm("(* y z) (+ w x) x"), term11.call());

        JSTerm term12 = new JSTerm(new StreamTokenizer(new StringReader("(+ (* x y) (* z w))")));
        assertEquals(new JSTerm("x y z w"), term12.call());

        JSTerm term13 = new JSTerm(new StreamTokenizer(new StringReader("(not (= x y))")));
        assertEquals(new JSTerm("nil"), term13.call());
        assertEquals(new JSTerm("t"), term13.call(new JSSubstitution(new JSPairTerm("x", new JSTerm("y")), new JSTerm("y"))));
    }
}