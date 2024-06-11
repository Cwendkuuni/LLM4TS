import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

    @Test
    public void testDefaultConstructor() {
        JSTerm term = new JSTerm();
        assertNotNull(term);
    }

    @Test(expected = JSParserError.class)
    public void testInvalidConstructor() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalidInput"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testVariableTermConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("x", term.elementAt(0));
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
    }

    @Test
    public void testConstantTermConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("3"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("3", term.elementAt(0));
        assertTrue(term.isConstant());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
    }

    @Test
    public void testFunctionTermConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("+(x . (3))", term.toStr().toString());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
    }

    @Test
    public void testFunctionTermWithArgumentConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("+(x . (y . nil))", term.toStr().toString());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
    }

    @Test
    public void testFunctionTermWithArgumentsConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y z)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("+(x . (y . (z . nil)))", term.toStr().toString());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
    }

    @Test
    public void testFunctionTermWithListArgumentsConstruction() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ (list x y z) 3)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("+(list(x . (y . (z . nil))) . (3 . nil))", term.toStr().toString());
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
    }

    @Test
    public void testCloneTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("y");
        JSTerm clonedTerm = term.cloneT();
        assertEquals(term.toStr().toString(), clonedTerm.toStr().toString());
    }

    @Test
    public void testApplySubstitutionTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("y");
        term.addElement("z");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("x", "a"));
        JSTerm result = term.applySubstitutionT(substitution);
        assertEquals("(a y z)", result.toStr().toString());
    }

    @Test
    public void testMatchesTerm() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        term1.addElement("z");

        JSTerm term2 = new JSTerm();
        term2.addElement("a");
        term2.addElement("x");
        term2.addElement("b");
        term2.addElement("y");
        term2.addElement("c");
        term2.addElement("z");

        JSSubstitution substitution = term1.matches(term2);
        assertEquals(3, substitution.size());
        assertEquals(new JSPair("x", "a"), substitution.elementAt(0));
        assertEquals(new JSPair("y", "b"), substitution.elementAt(1));
        assertEquals(new JSPair("z", "c"), substitution.elementAt(2));
    }

    @Test
    public void testEqualsTerm() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        term1.addElement("z");

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.addElement("y");
        term2.addElement("z");

        JSTerm term3 = new JSTerm();
        term3.addElement("a");
        term3.addElement("b");
        term3.addElement("c");

        assertTrue(term1.equals(term2));
        assertFalse(term1.equals(term3));
    }

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm();
        term.addElement("3");
        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm();
        term.addElement("+");
        term.addElement("x");
        term.addElement("y");
        assertTrue(term.isFunction());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("y");
        term.addElement("z");
        assertEquals("(x y z)", term.toStr().toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        term1.addElement("3");
        assertTrue(term1.isGround());

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.addElement("y");
        term2.addElement("z");
        assertFalse(term2.isGround());

        JSTerm term3 = new JSTerm();
        term3.addElement("+");
        term3.addElement("x");
        term3.addElement("y");
        assertFalse(term3.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("a");
        term.addElement("b");
        term.addElement("c");
        term.addElement("d");
        JSTerm standardizerTerm = term.standardizerTerm();
        assertEquals("(a1 . (b1 . (c1 . (d1 . nil)))))", standardizerTerm.toStr().toString());
    }

    @Test
    public void testCallTerm() {
        JSTerm term = new JSTerm();
        term.addElement("not");
        term.addElement("x");
        assertEquals("nil", term.call().toStr().toString());

        term.makeEval(true);
        assertEquals("(not x)", term.toStr().toString());
        assertEquals(Boolean.FALSE, term.call().toBoolean());

        term.addElement("x");
        term.addElement("y");
        assertEquals("(+ x y)", term.toStr().toString());
        assertEquals(5, term.call(new Object[]{3, 2}).getIntValue());

        term.addElement("+");
        term.addElement(new JSTerm().makeConstant("3"));
        term.addElement(term.call());
        assertEquals("9", term.call().toStr().toString());
    }
}