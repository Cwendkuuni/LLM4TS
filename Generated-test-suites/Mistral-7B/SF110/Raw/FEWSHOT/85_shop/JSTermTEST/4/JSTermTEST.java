package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTerm_Empty() {
        JSTerm jsterm = new JSTerm();
        assertTrue(jsterm.size() == 0);
    }

    @Test
    public void testJSTerm_Variable() {
        JSTerm jsterm = new JSTerm("x");
        assertTrue(jsterm.isVariable());
        assertEquals("x", jsterm.elementAt(0));
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testJSTerm_Constant() {
        JSTerm jsterm = new JSTerm("3");
        assertTrue(jsterm.isConstant());
        assertEquals("3", jsterm.elementAt(0));
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testJSTerm_Function() {
        JSTerm jsterm = new JSTerm("(+ x 3)");
        assertTrue(jsterm.isFunction());
        assertEquals("+(", jsterm.elementAt(0));
        assertEquals(3, jsterm.size());
        assertEquals("x", jsterm.elementAt(1).elementAt(0));
        assertEquals("3", jsterm.elementAt(2).elementAt(0));
    }

    @Test
    public void testJSTerm_List() {
        JSTerm jsterm1 = new JSTerm("(cons 1 '(2 3))");
        assertTrue(jsterm1.isFunction());
        assertEquals("cons", jsterm1.elementAt(0));
        assertEquals(3, jsterm1.size());
        assertEquals("1", jsterm1.elementAt(1).elementAt(0));
        JSTerm list = jsterm1.elementAt(1).elementAt(1);
        assertEquals("2", list.elementAt(0));
        assertEquals("3", list.elementAt(1).elementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testJSTerm_Clone() {
        JSTerm jsterm = new JSTerm("(+ x 3)");
        JSTerm cloned = jsterm.cloneT();
        assertEquals(jsterm, cloned);
    }

    @Test
    public void testJSTerm_ApplySubstitution() {
        JSTerm jsterm1 = new JSTerm("x");
        JSTerm jsterm2 = new JSTerm("y");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm(jsterm1, jsterm2));
        JSTerm result = jsterm1.applySubstitutionT(substitution);
        assertEquals(jsterm2, result);
    }

    @Test
    public void testJSTerm_Matches() {
        JSTerm jsterm1 = new JSTerm("x");
        JSTerm jsterm2 = new JSTerm("y");
        JSTerm jsterm3 = new JSTerm("x");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm(jsterm1, jsterm3));
        JSTerm result = jsterm1.matches(jsterm2).applySubstitutionT(substitution);
        assertEquals(jsterm3, result);
    }

    @Test
    public void testJSTerm_IsGround() {
        JSTerm jsterm1 = new JSTerm("x");
        JSTerm jsterm2 = new JSTerm("3");
        JSTerm jsterm3 = new JSTerm("(+ x 3)");
        assertTrue(jsterm2.isGround());
        assertFalse(jsterm1.isGround());
        assertTrue(jsterm3.isGround());
    }

    @Test
    public void testJSTerm_StandardizerTerm() {
        JSTerm jsterm1 = new JSTerm("x");
        JSTerm jsterm2 = new JSTerm("(+ x 3)");
        JSTerm jsterm3 = new JSTerm("(+ y 3)");
        JSTerm standardized1 = jsterm1.standardizerTerm();
        JSTerm standardized2 = jsterm2.standardizerTerm();
        JSTerm standardized3 = jsterm3.standardizerTerm();
        assertEquals("x", standardized1.elementAt(0));
        assertEquals("+(y.", standardized3.elementAt(0));
        assertEquals(1, standardized1.size());
        assertEquals(3, standardized2.size());
        assertEquals(2, standardized3.size());
    }

    @Test
    public void testJSTerm_Call() {
        JSTerm jsterm1 = new JSTerm("(+ 1 2)");
        JSTerm jsterm2 = jsterm1.call();
        assertEquals("3", jsterm2.elementAt(0));
        assertEquals(0, jsterm2.size());
    }
}