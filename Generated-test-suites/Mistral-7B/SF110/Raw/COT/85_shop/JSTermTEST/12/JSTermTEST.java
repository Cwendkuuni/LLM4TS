import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSJshopVars;

public class JSTermTest {

    @Test
    public void testJSTermEmptyConstructor() {
        JSTerm term = new JSTerm();
        assertTrue(term.isEmpty());
    }

    @Test
    public void testJSTermTokenizedConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm expected = new JSTerm();
        expected.addElement("+");
        expected.addElement("3");
        expected.addElement("4");
        assertTrue(term.equals(expected));
    }

    @Test(expected = JSParserError.class)
    public void testJSTermInvalidConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("5 %%%"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm("x");
        assertTrue(term.isVariable());

        term = new JSTerm("(+ x 3)");
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm("3");
        assertTrue(term.isConstant());

        term = new JSTerm("(+ 3 x)");
        assertFalse(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm("+");
        assertTrue(term.isFunction());

        term = new JSTerm("(+ x 3)");
        assertTrue(term.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = new JSTerm("(+ 3 x)");
        assertFalse(term.isEval());

        term = new JSTerm("(+ 3 x) %% call");
        term.parseList(new StreamTokenizer(new StringReader("(+ 3 x) %% call (5)")));
        assertTrue(term.isEval());
    }

    @Test
    public void testAddElement() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("3");
        term.addElement("+");
        term.addElement("y");

        JSTerm expected = new JSTerm();
        expected.addElement("x");
        expected.addElement("3");
        expected.addElement("+");
        expected.addElement("y");

        assertTrue(term.equals(expected));
    }

    @Test
    public void testSize() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("3");
        term.addElement("+");
        term.addElement("y");

        assertEquals(4, term.size());
    }

    @Test
    public void testElementAt() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("3");
        term.addElement("+");
        term.addElement("y");

        assertEquals("x", term.elementAt(0).toString());
        assertEquals("3", term.elementAt(1).toString());
        assertEquals("+", term.elementAt(2).toString());
        assertEquals("y", term.elementAt(3).toString());
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm("(+ x 3)");
        String expected = " ( + x 3 ) ";
        term.print();
        assertEquals(expected, JSUtil.getOutput());
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm("(+ x 3)");
        JSTerm cloned = term.cloneT();
        assertEquals(term.toString(), cloned.toString());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm("(+ x 3)");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairVarTerm("x", new JSTerm("5")));
        JSTerm result = term.applySubstitutionT(alpha);
        JSTerm expected = new JSTerm("(+ 5 3)");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm("(+ x 3)");
        JSTerm term2 = new JSTerm("(+ y 3)");
        JSSubstitution result = term1.matches(term2);
        assertEquals(0, result.size());

        term1 = new JSTerm("(+ x 3)");
        term2 = new JSTerm("(+ x 3)");
        result = term1.matches(term2);
        assertEquals(1, result.size());

        term1 = new JSTerm("(+ x 3)");
        term2 = new JSTerm("(+ y z 3)");
        result = term1.matches(term2);
        assertEquals(0, result.size());

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairVarTerm("x", new JSTerm("5")));
        term1 = new JSTerm("(+ x 3)");
        term2 = new JSTerm("(+ y 3)");
        result = term1.matches(term2, alpha);
        assertEquals(1, result.size());
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm("(+ x 3)");
        JSTerm term2 = new JSTerm("(+ x 3)");
        assertTrue(term1.equals(term2));

        term1 = new JSTerm("(+ x 3)");
        term2 = new JSTerm("(+ y 3)");
        assertFalse(term1.equals(term2));

        term1 = new JSTerm("(+ x 3)");
        term2 = new JSTerm("(+ x 3) %% call");
        assertFalse(term1.equals(term2));
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 (/ 4 2) 5)"));
        JSTerm term = new JSTerm();
        term.parseList(tokenizer);

        JSTerm expected = new JSTerm();
        expected.addElement("+");
        expected.addElement("3");
        expected.addElement("/");
        expected.addElement("4");
        expected.addElement("2");
        expected.addElement("5");

        assertTrue(term.equals(expected));
    }
}