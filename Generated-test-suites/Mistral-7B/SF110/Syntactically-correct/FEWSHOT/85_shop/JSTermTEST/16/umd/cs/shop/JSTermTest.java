package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTerm_Const() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeConstant();

        JSTerm expected = new JSTerm();
        expected.addElement("x");
        expected.makeConstant();

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_Var() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        JSTerm expected = new JSTerm();
        expected.addElement("x");
        expected.makeVariable();

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_Function() {
        JSTerm term = new JSTerm();
        term.addElement("f");
        term.addElement(new JSTerm());
        term.makeFunction();

        JSTerm expected = new JSTerm();
        expected.addElement("f");
        expected.makeFunction();
        expected.addElement(new JSTerm());

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_Eval() {
        JSTerm term = new JSTerm();
        term.addElement("f");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.makeEval(true);

        JSTerm expected = new JSTerm();
        expected.addElement("call");
        expected.addElement("f");
        expected.addElement(new JSTerm());
        expected.addElement(new JSTerm());

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_List() {
        JSTerm term = new JSTerm();
        term.addElement(".");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.addElement("nil");
        term.makeFunction();

        JSTerm expected = new JSTerm();
        expected.addElement("list");
        expected.addElement(new JSTerm());
        expected.addElement(new JSTerm());
        expected.addElement(new JSTerm());
        expected.addElement("nil");

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_ParseList() {
        JSTerm term = new JSTerm();
        term.addElement(".");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.addElement(".");
        term.addElement(new JSTerm());
        term.addElement("nil");
        term.makeFunction();

        JSTerm expected = new JSTerm();
        expected.addElement(".");
        expected.addElement("list");
        expected.addElement(new JSTerm());
        expected.addElement(new JSTerm());
        expected.addElement("nil");

        assertEquals(expected, term.parseList(new StreamTokenizer()));
    }

    @Test
    public void testJSTerm_Print() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeConstant();

        JSTerm expected = new JSTerm();
        expected.addElement("x");

        term.print();
        assertEquals("x ", System.out.println());
        System.out.flush();

        assertEquals(expected, term);
    }

    @Test
    public void testJSTerm_Clone() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        JSTerm clonedTerm = term.cloneT();

        assertEquals(term, clonedTerm);
    }

    @Test
    public void testJSTerm_ApplySubstitution() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", "y"));

        JSTerm result = term1.applySubstitutionT(alpha);

        JSTerm expected = new JSTerm();
        expected.addElement("y");
        expected.makeVariable();

        assertEquals(expected, result);
    }

    @Test
    public void testJSTerm_Matches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("x");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("y");
        term4.makeVariable();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", "y"));

        JSTerm result1 = term1.matches(term2);
        assertEquals(result1, term3);

        JSTerm result2 = term1.matches(term3, alpha);
        assertEquals(term4, result2);
    }

    @Test
    public void testJSTerm_Equals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("x");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("x");
        term4.makeConstant();

        JSTerm term5 = new JSTerm();
        term5.addElement("f");
        term5.addElement(new JSTerm());
        term5.addElement(new JSTerm());
        term5.makeFunction();

        assertEquals(term1, term1);
        assertEquals(term1, term3);
        assertNotEquals(term1, term2);
        assertNotEquals(term1, term4);
        assertNotEquals(term1, term5);
    }

    @Test
    public void testJSTerm_IsVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        assertTrue(term.isVariable());
    }

    @Test
    public void testJSTerm_IsConstant() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeConstant();

        assertTrue(term.isConstant());
    }

    @Test
    public void testJSTerm_IsFunction() {
        JSTerm term = new JSTerm();
        term.addElement("f");
        term.makeFunction();

        assertTrue(term.isFunction());
    }

    @Test
    public void testJSTerm_IsEval() {
        JSTerm term = new JSTerm();
        term.addElement("f");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.makeEval(true);

        assertTrue(term.isEval());
    }
}