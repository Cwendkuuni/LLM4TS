package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermEmpty() {
        JSTerm term = new JSTerm();
        assertNull(term.elementAt(0));
        assertEquals(0, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermVariable() {
        JSTerm term = new JSTerm("x");
        assertEquals("x", term.elementAt(0));
        assertEquals(1, term.size());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermConstant() {
        JSTerm term = new JSTerm("5");
        assertEquals("5", term.elementAt(0));
        assertEquals(1, term.size());
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermFunction() {
        JSTerm term = new JSTerm("+");
        assertEquals("+", term.elementAt(0));
        assertEquals(1, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        term = new JSTerm("sin");
        assertEquals("sin", term.elementAt(0));
        assertEquals(1, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermEval() {
        JSTerm term = new JSTerm("(+ 1 1)");
        assertEquals("(+ 1 1)", term.toStr().toString());
        assertEquals("+(1, 1)", term.toString());
        assertEquals(3, term.size());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertTrue(term.isEval());

        term = new JSTerm("(sin x)");
        assertEquals("(sin x)", term.toStr().toString());
        assertEquals("(sin x)", term.toString());
        assertEquals(2, term.size());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        term = new JSTerm("(call sin x)");
        assertEquals("(call sin x)", term.toStr().toString());
        assertEquals("(call sin x)", term.toString());
        assertEquals(2, term.size());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertTrue(term.isEval());
    }

    @Test
    public void testJSTermParseList() {
        JSTerm term = new JSTerm();
        term.parseList(new StreamTokenizer(new StringReader("(+ 1 1 2 3)")));
        assertEquals(4, term.size());
        assertEquals("+(1, 1, 2, 3)", term.toString());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        term = new JSTerm();
        term.parseList(new StreamTokenizer(new StringReader("(list x y z)")));
        assertEquals(3, term.size());
        assertEquals("(list x y z)", term.toString());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        term = new JSTerm();
        term.parseList(new StreamTokenizer(new StringReader("(call list x y z)")));
        assertEquals(3, term.size());
        assertEquals("(call list x y z)", term.toString());
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertTrue(term.isEval());
    }

    @Test
    public void testJSTermAdd() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("2");
        JSTerm term3 = new JSTerm("+");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("+", term1, term2))));
        assertEquals("1 2", term4.toString());
        term1 = new JSTerm("5");
        term2 = new JSTerm("3");
        term3 = new JSTerm("+");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("+", term1, term2))));
        assertEquals("5 3", term4.toString());
        assertEquals(6, term4.call().toInt());
    }

    @Test
    public void testJSTermMultiply() {
        JSTerm term1 = new JSTerm("2");
        JSTerm term2 = new JSTerm("3");
        JSTerm term3 = new JSTerm("*");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("*", term1, term2))));
        assertEquals("2 3", term4.toString());
        term1 = new JSTerm("5");
        term2 = new JSTerm("3");
        term3 = new JSTerm("*");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("*", term1, term2))));
        assertEquals("5 3", term4.toString());
        assertEquals(15, term4.call().toInt());
    }

    @Test
    public void testJSTermSubtract() {
        JSTerm term1 = new JSTerm("5");
        JSTerm term2 = new JSTerm("2");
        JSTerm term3 = new JSTerm("-");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("-", term1, term2))));
        assertEquals("5 2", term4.toString());
        assertEquals(3, term4.call().toInt());
    }

    @Test
    public void testJSTermDivide() {
        JSTerm term1 = new JSTerm("10");
        JSTerm term2 = new JSTerm("2");
        JSTerm term3 = new JSTerm("/");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("/", term1, term2))));
        assertEquals("10 2", term4.toString());
        assertEquals(5, term4.call().toInt());
    }

    @Test
    public void testJSTermNegate() {
        JSTerm term1 = new JSTerm("5");
        JSTerm term2 = new JSTerm("not");
        JSTerm term3 = new JSTerm("5");
        JSTerm term4 = term2.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("not", term3))));
        assertEquals("not 5", term4.toString());
        assertEquals(-5, term4.call().toInt());

        term1 = new JSTerm("1");
        term2 = new JSTerm("-");
        term3 = new JSTerm("1");
        term4 = term2.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("-", term3))));
        assertEquals("- 1", term4.toString());
        assertEquals(-1, term4.call().toInt());
    }

    @Test
    public void testJSTermEqual() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("1");
        JSTerm term3 = new JSTerm("=");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("=", term1, term2))));
        assertEquals("1 1 =", term4.toString());
        assertEquals(true, term4.call().toBoolean());

        term1 = new JSTerm("1");
        term2 = new JSTerm("2");
        term3 = new JSTerm("=");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("=", term1, term2))));
        assertEquals("1 2 =", term4.toString());
        assertEquals(false, term4.call().toBoolean());
    }

    @Test
    public void testJSTermLessThan() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("2");
        JSTerm term3 = new JSTerm("<");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("<", term1, term2))));
        assertEquals("1 2 <", term4.toString());
        assertEquals(false, term4.call().toBoolean());

        term1 = new JSTerm("1");
        term2 = new JSTerm("1");
        term3 = new JSTerm("<");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("<", term1, term2))));
        assertEquals("1 1 <", term4.toString());
        assertEquals(false, term4.call().toBoolean());

        term1 = new JSTerm("1");
        term2 = new JSTerm("1");
        term3 = new JSTerm("<=");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("<=", term1, term2))));
        assertEquals("1 1 <=", term4.toString());
        assertEquals(true, term4.call().toBoolean());

        term1 = new JSTerm("2");
        term2 = new JSTerm("1");
        term3 = new JSTerm("<=");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution("<=", term1, term2))));
        assertEquals("2 1 <=", term4.toString());
        assertEquals(false, term4.call().toBoolean());
    }

    @Test
    public void testJSTermGreaterThan() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("2");
        JSTerm term3 = new JSTerm(">");
        JSTerm term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution(">", term1, term2))));
        assertEquals("1 2 >", term4.toString());
        assertEquals(false, term4.call().toBoolean());

        term1 = new JSTerm("2");
        term2 = new JSTerm("1");
        term3 = new JSTerm(">");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution(">", term1, term2))));
        assertEquals("2 1 >", term4.toString());
        assertEquals(true, term4.call().toBoolean());

        term1 = new JSTerm("1");
        term2 = new JSTerm("1");
        term3 = new JSTerm(">=");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution(">=", term1, term2))));
        assertEquals("1 1 >=", term4.toString());
        assertEquals(true, term4.call().toBoolean());

        term1 = new JSTerm("1");
        term2 = new JSTerm("2");
        term3 = new JSTerm(">=");
        term4 = term3.applySubstitutionT(new SubstitutionMap(Arrays.asList(new Substitution(">=", term1, term2))));
        assertEquals("1 2 >=", term4.toString());
        assertEquals(false, term4.call().toBoolean());
    }
}