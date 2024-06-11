import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSSubstitution;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm term = new JSTerm();
        assertNull(term.elementAt(0));
        assertEquals(0, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testConstructorWithStreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals(3, term.size());
        assertEquals("+", term.elementAt(0).toString());
        assertEquals("1", term.elementAt(1).toString());
        assertEquals("2", term.elementAt(2).toString());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());

        term = new JSTerm(tokenizer); // should throw an error since the tokenizer has already been read
        fail("Expected JSParserError to be thrown");
    }

    @Test
    public void testParseListEmpty() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSTerm term = new JSTerm();
        JSTerm list = term.parseList(tokenizer);
        assertEquals("nil", list.elementAt(0).toString());
        assertEquals(0, list.size());
        assertTrue(list.isConstant());
    }

    @Test
    public void testParseListSingleElement() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1)"));
        JSTerm term = new JSTerm();
        JSTerm list = term.parseList(tokenizer);
        assertEquals("1", list.elementAt(0).toString());
        assertEquals(1, list.size());
        assertFalse(list.isConstant());
    }

    @Test
    public void testParseListMultipleElements() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1 2 3)"));
        JSTerm term = new JSTerm();
        JSTerm list = term.parseList(tokenizer);
        assertEquals("nil", list.elementAt(list.size() - 1).toString());
        for (int i = 0; i < list.size() - 1; i++) {
            assertEquals(String.valueOf(i + 1), list.elementAt(i).toString());
        }
        assertEquals(3, list.size());
        assertFalse(list.isConstant());
    }

    @Test
    public void testParseListIncorrectParentheses() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1 2)")); // missing right parenthesis
        JSTerm term = new JSTerm();
        try {
            term.parseList(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.addElement("1");
        assertEquals("1", term.toStr().toString());

        term.makeFunction();
        term.addElement("+");
        term.addElement("1");
        term.addElement("2");
        assertEquals("(+ 1 2)", term.toStr().toString());
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("1");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("1");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("2");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("2");
        term4.makeConstant();

        JSTerm term5 = new JSTerm();
        term5.addElement("+");
        term5.addElement("1");
        term5.addElement("2");
        term5.makeFunction();

        JSTerm term6 = new JSTerm();
        term6.addElement("*");
        term6.addElement("1");
        term6.addElement("2");
        term6.makeFunction();

        JSTerm term7 = new JSTerm();
        term7.addElement(".");
        term7.addElement("list");
        term7.addElement("nil");
        term7.makeFunction();

        assertEquals(term1, term1);
        assertEquals(term1, term2);
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
        assertFalse(term1.equals(term7));
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm();
        term.addElement("1");
        term.makeConstant();

        JSTerm clonedTerm = term.cloneT();

        assertEquals(term, clonedTerm);
        assertEquals(1, clonedTerm.size());
        assertEquals("1", clonedTerm.elementAt(0).toString());
        assertTrue(clonedTerm.isConstant());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairVarTerm("x", "y"));

        JSTerm result = term.applySubstitutionT(alpha);

        assertEquals(1, result.size());
        assertEquals("y", result.elementAt(0).toString());
        assertTrue(result.isVariable());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("1");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("2");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("x");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("x");
        term4.makeVariable();

        JSTerm term5 = new JSTerm();
        term5.addElement("+");
        term5.addElement("1");
        term5.addElement("2");
        term5.makeFunction();

        JSTerm term6 = new JSTerm();
        term6.addElement("*");
        term6.addElement("1");
        term6.addElement("2");
        term6.makeFunction();

        JSTerm term7 = new JSTerm();
        term7.addElement(".");
        term7.addElement("list");
        term7.addElement("nil");
        term7.makeFunction();

        JSTerm term8 = new JSTerm();
        term8.addElement("x");
        term8.makeVariable();
        term8.addElement("y");
        term8.makeVariable();

        JSSubstitution beta = term1.matches(term2);
        assertFalse(beta.fail());
        assertTrue(beta.isEmpty());

        beta = term3.matches(term4);
        assertTrue(beta.isEmpty());

        beta = term3.matches(term1);
        assertFalse(beta.fail());
        assertTrue(beta.size() > 0);
        assertEquals("x", beta.elementAt(0).getKey().toString());
        assertEquals("1", beta.elementAt(0).getValue().toString());

        beta = term5.matches(term6);
        assertFalse(beta.fail());
        assertTrue(beta.isEmpty());

        beta = term7.matches(term5);
        assertFalse(beta.fail());
        assertTrue(beta.isEmpty());

        beta = term8.matches(term1);
        assertFalse(beta.fail());
        assertTrue(beta.isEmpty());

        beta = term8.matches(term3);
        assertFalse(beta.fail());
        assertTrue(beta.size() > 0);
        assertEquals("x", beta.elementAt(0).getKey().toString());
        assertEquals("1", beta.elementAt(0).getValue().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSParserError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1")); // missing right parenthesis
        new JSTerm(tokenizer);
    }
}