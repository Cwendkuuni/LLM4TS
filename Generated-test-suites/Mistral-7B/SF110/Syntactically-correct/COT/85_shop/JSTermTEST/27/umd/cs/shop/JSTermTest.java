import static org.junit.Assert.*;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermDefaultConstructor() {
        JSTerm jsterm = new JSTerm();
        assertNull(jsterm.elementAt(0));
        assertFalse(jsterm.isEval());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testJSTermStreamTokenizerConstructor() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(term)")));
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("term", jsterm.elementAt(0));
        assertEquals(0, jsterm.size());

        try {
            new JSTerm(new StreamTokenizer(new StringReader("not (term)")));
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testAddElement() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        assertEquals("x", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());

        jsterm.addElement(new JSTerm());
        assertNotNull(jsterm.elementAt(1));
        assertEquals(2, jsterm.size());
    }

    @Test
    public void testMakeConstant() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeConstant();
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("x", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testMakeVariable() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeVariable();
        assertTrue(jsterm.isVariable());
        assertFalse(jsterm.isFunction());
        assertFalse(jsterm.isEval());
        assertEquals("x", jsterm.elementAt(0));
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testMakeFunction() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeFunction();
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isEval());
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testMakeEval() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeEval(true);
        assertTrue(jsterm.isEval());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testPrintList() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement(new JSTerm());
        jsterm.addElement(new JSTerm());
        jsterm.addElement(new JSTerm());

        String output = new String();
        jsterm.printList(jsterm);
        output += "(list ";
        jsterm.elementAt(0).print();
        output += " ";
        jsterm.elementAt(1).printList(jsterm.elementAt(1));
        output += ") ";
        jsterm.elementAt(2).printList(jsterm.elementAt(2));
        output += ")";

        JSUtil.print(output);
        assertEquals(output, jsterm.toStr().toString());
    }

    @Test
    public void testParseList() {
        JSTerm jsterm = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term1 term2 term3)"));

        JSTerm list = jsterm.parseList(tokenizer);
        assertEquals("term1", list.elementAt(0).toString());
        assertEquals("term2", list.elementAt(1).toString());
        assertEquals("term3", list.elementAt(2).toString());
        assertEquals(3, list.size());
    }

    @Test
    public void testCloneT() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeVariable();

        JSTerm clonedTerm = jsterm.cloneT();
        assertEquals("x", clonedTerm.elementAt(0));
        assertTrue(clonedTerm.isVariable());
        assertEquals(1, clonedTerm.size());

        jsterm.makeConstant();
        JSTerm clonedConstantTerm = jsterm.cloneT();
        assertEquals("x", clonedConstantTerm.elementAt(0));
        assertFalse(clonedConstantTerm.isVariable());
        assertFalse(clonedConstantTerm.isFunction());
        assertFalse(clonedConstantTerm.isEval());
        assertEquals(1, clonedConstantTerm.size());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm jsterm1 = new JSTerm();
        JSTerm jsterm2 = new JSTerm();
        JSSubstitution substitution = new JSSubstitution();

        jsterm1.addElement("x");
        jsterm1.makeVariable();
        jsterm2.addElement("y");
        jsterm2.makeVariable();
        substitution.addElement(new JSPairTerm("x", "y"));

        JSTerm result = jsterm1.applySubstitutionT(substitution);
        assertEquals("y", result.elementAt(0));
        assertTrue(result.isVariable());
        assertEquals(1, result.size());
    }

    @Test(expected = JSParserError.class)
    public void testMatchesNullTerm() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeVariable();

        JSTerm otherTerm = null;
        JSSubstitution substitution = jsterm.matches(otherTerm);
        fail("Expected JSParserError");
    }

    @Test
    public void testMatchesSameTerms() {
        JSTerm jsterm1 = new JSTerm();
        JSTerm jsterm2 = new JSTerm();

        jsterm1.addElement("x");
        jsterm1.makeVariable();

        jsterm2.addElement("x");
        jsterm2.makeVariable();

        JSSubstitution substitution = jsterm1.matches(jsterm2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesDifferentTerms() {
        JSTerm jsterm1 = new JSTerm();
        JSTerm jsterm2 = new JSTerm();

        jsterm1.addElement("x");
        jsterm1.makeVariable();

        jsterm2.addElement("y");
        jsterm2.makeVariable();

        JSSubstitution substitution = jsterm1.matches(jsterm2);
        assertFalse(substitution.isEmpty());
    }

    @Test
    public void testEqualsSameTerms() {
        JSTerm jsterm1 = new JSTerm();
        JSTerm jsterm2 = new JSTerm();

        jsterm1.addElement("x");
        jsterm1.makeVariable();

        jsterm2.addElement("x");
        jsterm2.makeVariable();

        assertTrue(jsterm1.equals(jsterm2));
    }

    @Test
    public void testEqualsDifferentTerms() {
        JSTerm jsterm1 = new JSTerm();
        JSTerm jsterm2 = new JSTerm();

        jsterm1.addElement("x");
        jsterm1.makeVariable();

        jsterm2.addElement("y");
        jsterm2.makeVariable();

        assertFalse(jsterm1.equals(jsterm2));
    }

    @Test
    public void testIsVariable() {
        JSTerm jsterm = new JSTerm();

        jsterm.addElement("x");
        jsterm.makeVariable();

        assertTrue(jsterm.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm jsterm = new JSTerm();

        jsterm.addElement("x");
        jsterm.makeConstant();

        assertTrue(jsterm.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm jsterm = new JSTerm();

        jsterm.addElement("(term)");
        jsterm.makeFunction();

        assertTrue(jsterm.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm jsterm = new JSTerm();

        jsterm.makeEval(true);

        assertTrue(jsterm.isEval());
    }

    @Test
    public void testIsGround() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("x");
        jsterm.makeVariable();
        assertFalse(jsterm.isGround());

        JSTerm constantTerm = new JSTerm();
        constantTerm.addElement("5");
        constantTerm.makeConstant();
        assertTrue(constantTerm.isGround());

        JSTerm groundList = new JSTerm();
        groundList.makeFunction();
        groundList.addElement("+");
        groundList.addElement(new JSTerm());
        groundList.addElement(new JSTerm());
        groundList.elementAt(0).addElement("3");
        groundList.elementAt(1).addElement("4");
        assertTrue(groundList.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm jsterm = new JSTerm();

        jsterm.addElement("x");
        jsterm.makeVariable();

        JSTerm standardizedTerm = jsterm.standardizerTerm();
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.elementAt(0).toString());
        assertEquals(1, standardizedTerm.size());
    }

    @Test
    public void testCall() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("+");
        jsterm.makeFunction();

        JSTerm arg1 = new JSTerm();
        arg1.addElement("3");
        arg1.makeConstant();

        JSTerm arg2 = new JSTerm();
        arg2.addElement("4");
        arg2.makeConstant();

        JSTerm result = jsterm.call();
        assertEquals("7", result.elementAt(0).toString());
        assertEquals(1, result.size());
    }
}