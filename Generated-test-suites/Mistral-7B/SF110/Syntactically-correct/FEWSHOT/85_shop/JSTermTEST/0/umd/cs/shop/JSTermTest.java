package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.elementAt(0).toString());
    }

    @Test
    public void testCallFunctionWithOneArgument() {
        JSTerm term = new JSTerm();
        term.makeFunction();
        term.addElement("sin");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm("0.5"));

        JSTerm result = term.call();
        assertEquals("sin 0.5", result.toStr().toString());
    }

    @Test
    public void testCallFunctionWithMoreThanOneArgument() {
        JSTerm term = new JSTerm();
        term.makeFunction();
        term.addElement("+");
        term.addElement(new JSTerm());
        term.addElement("3");
        term.addElement(new JSTerm());
        term.addElement("5");

        JSTerm result = term.call();
        assertEquals("3 + 5", result.toStr().toString());
    }

    @Test
    public void testCallFunctionWithUnaryOperator() {
        JSTerm term = new JSTerm();
        term.makeFunction();
        term.addElement("-");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm("5"));

        JSTerm result = term.call();
        assertEquals("-5", result.toStr().toString());
    }

    @Test
    public void testCallFunctionWithNotOperator() {
        JSTerm term = new JSTerm();
        term.makeFunction();
        term.addElement("not");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm("true"));

        JSTerm result = term.call();
        assertEquals("false", result.toStr().toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("3");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("*");
        term3.addElement(term1);
        term3.addElement(term2);

        JSTerm term4 = new JSTerm();
        term4.addElement("sin");
        term4.addElement(new JSTerm());
        term4.addElement(new JSTerm("0.5"));

        assertTrue(term2.isGround());
        assertFalse(term1.isGround());
        assertFalse(term3.isGround());
        assertTrue(term4.isGround());
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("3");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("3");
        term4.makeConstant();

        JSTerm term5 = new JSTerm();
        term5.addElement("*");
        term5.addElement(term1);
        term5.addElement(term2);

        JSTerm term6 = new JSTerm();
        term6.addElement("*");
        term6.addElement(term1);
        term6.addElement(term3);

        assertTrue(term1.equals(term2));
        assertTrue(term3.equals(term4));
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term5));
    }
}