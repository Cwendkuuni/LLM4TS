package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testParseTerm() {
        JSTerm term = new JSTerm();
        term.parseTerm(new StringReader("2"));
        assertEquals(new Double("2.0"), term.elementAt(0));

        term = new JSTerm();
        term.parseTerm(new StringReader("x"));
        assertTrue(term.isVariable());
        assertEquals("x", term.elementAt(0));

        term = new JSTerm();
        term.parseTerm(new StringReader("(+ 1 2)"));
        JSTerm term1 = new JSTerm();
        term1.parseTerm(new StringReader("1"));
        term1.makeConstant();
        term1.addElement("+");
        term1.addElement(term1);
        term1.addElement(new JSTerm());
        term1.elementAt(2).parseTerm(new StringReader("2"));
        term1.elementAt(2).makeConstant();
        assertEquals(term1, term.cloneT());

        term = new JSTerm();
        term.parseTerm(new StringReader("(list x y z)"));
        JSTerm listTerm = new JSTerm();
        listTerm.makeFunction();
        listTerm.addElement("list");
        listTerm.addElement(new JSTerm());
        listTerm.elementAt(0).parseTerm(new StringReader("x"));
        JSTerm term2 = new JSTerm();
        term2.parseTerm(new StringReader("y"));
        listTerm.elementAt(1).addElement(term2);
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("z"));
        listTerm.elementAt(1).addElement(term2);
        assertEquals(listTerm, term.cloneT());

        term = new JSTerm();
        term.parseTerm(new StringReader("(not x)"));
        JSTerm notTerm = new JSTerm();
        notTerm.makeFunction();
        notTerm.addElement("not");
        notTerm.addElement(new JSTerm());
        notTerm.elementAt(0).parseTerm(new StringReader("x"));
        assertEquals(notTerm, term.cloneT());

        term = new JSTerm();
        term.parseTerm(new StringReader("((call f x) y)"));
        JSTerm callTerm = new JSTerm();
        callTerm.makeFunction();
        callTerm.addElement("call");
        callTerm.addElement(new JSTerm());
        callTerm.elementAt(0).parseTerm(new StringReader("f"));
        JSTerm term3 = new JSTerm();
        term3.parseTerm(new StringReader("x"));
        callTerm.elementAt(1).addElement(term3);
        term3 = new JSTerm();
        term3.parseTerm(new StringReader("y"));
        callTerm.elementAt(1).addElement(term3);
        assertEquals(callTerm, term.cloneT());
    }

    @Test
    public void testListManipulation() {
        JSTerm listTerm = new JSTerm();
        listTerm.parseTerm(new StringReader("(list 1 2 3)"));
        listTerm.makeConstant();
        assertEquals(3, listTerm.size());
        assertEquals(new Integer(1), listTerm.elementAt(0));
        assertEquals(new Integer(2), listTerm.elementAt(1));
        assertEquals(new Integer(3), listTerm.elementAt(2));

        JSTerm list1 = new JSTerm();
        list1.parseTerm(new StringReader("(list x y z)"));
        list1.makeFunction();
        list1.addElement("list");
        list1.addElement(new JSTerm());
        list1.elementAt(0).parseTerm(new StringReader("x"));
        JSTerm list2 = new JSTerm();
        list2.parseTerm(new StringReader("y"));
        list1.elementAt(1).addElement(list2);
        list2 = new JSTerm();
        list2.parseTerm(new StringReader("z"));
        list1.elementAt(1).addElement(list2);

        JSTerm carTerm = new JSTerm();
        carTerm.parseTerm(new StringReader("(car (list 1 2 3))"));
        carTerm.makeFunction();
        carTerm.addElement("car");
        carTerm.addElement(new JSTerm());
        carTerm.elementAt(0).parseTerm(new StringReader("(list 1 2 3)"));
        assertEquals(new Integer(1), carTerm.elementAt(1).elementAt(0));

        JSTerm cdrTerm = new JSTerm();
        cdrTerm.parseTerm(new StringReader("(cdr (list 1 2 3))"));
        cdrTerm.makeFunction();
        cdrTerm.addElement("cdr");
        cdrTerm.addElement(new JSTerm());
        cdrTerm.elementAt(0).parseTerm(new StringReader("(list 1 2 3)"));
        JSTerm cdrList = new JSTerm();
        cdrList.parseTerm(new StringReader("(list 2 3)"));
        cdrTerm.elementAt(1).addElement(cdrList);
        assertEquals(cdrList, carTerm.elementAt(1).elementAt(1));
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.parseTerm(new StringReader("(x)"));
        JSTerm term2 = new JSTerm();
        term2.parseTerm(new StringReader("(y)"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = term1.matches(term2, alpha);
        assertTrue(beta.isEmpty());

        alpha.addElement(new JSPair("x", "y"));
        beta = term1.matches(term2, alpha);
        assertTrue(beta.isEmpty());

        alpha.clear();
        alpha.addElement(new JSPair("x", "y"));
        beta = term2.matches(term1, alpha);
        assertTrue(beta.isEmpty());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ z w)"));

        alpha = new JSSubstitution();
        beta = term1.matches(term2, alpha);
        assertTrue(beta.isEmpty());

        alpha.addElement(new JSPair("x", "z"));
        beta = term1.matches(term2, alpha);
        alpha.addElement(new JSPair("y", "w"));
        assertEquals(new JSPair("x", "z"), beta.elementAt(0));
        assertEquals(new JSPair("y", "w"), beta.elementAt(1));
    }

    @Test
    public void testMatchesFailure() {
        JSTerm term1 = new JSTerm();
        term1.parseTerm(new StringReader("(x)"));
        JSTerm term2 = new JSTerm();
        term2.parseTerm(new StringReader("(y)"));

        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = term1.matches(term2, alpha);
        assertTrue(beta.isEmpty());

        alpha.clear();
        alpha.addElement(new JSPair("x", "z"));
        beta = term1.matches(term2, alpha);
        assertTrue(beta.fail());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ z w x)"));

        alpha = new JSSubstitution();
        beta = term1.matches(term2, alpha);
        assertTrue(beta.fail());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ z w)"));

        alpha = new JSSubstitution();
        beta = term1.matches(term2, alpha);
        assertTrue(beta.fail());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ z w z)"));

        alpha = new JSSubstitution();
        beta = term1.matches(term2, alpha);
        assertTrue(beta.fail());
    }

    @Test
    public void testIsGround() {
        JSTerm term1 = new JSTerm();
        term1.parseTerm(new StringReader("x"));
        assertFalse(term1.isGround());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("2"));
        term1.makeConstant();
        assertTrue(term1.isGround());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        JSTerm term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ 2 y)"));
        term2.makeConstant();
        term2.elementAt(0).makeConstant();
        assertTrue(term1.isGround());

        term1 = new JSTerm();
        term1.parseTerm(new StringReader("(+ x y)"));
        term2 = new JSTerm();
        term2.parseTerm(new StringReader("(+ x 2)"));
        term2.makeConstant();
        term2.elementAt(1).makeConstant();
        assertTrue(term1.isGround());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.parseTerm(new StringReader("x"));
        assertEquals("x", term.toStr().toString());

        term = new JSTerm();
        term.parseTerm(new StringReader("(+ x y)"));
        assertEquals("(+ x y)", term.toStr().toString());

        term = new JSTerm();
        term.parseTerm(new StringReader("(list x y z)"));
        assertEquals("(list x y z)", term.toStr().toString());

        term = new JSTerm();
        term.parseTerm(new StringReader("(not x)"));
        assertEquals("(not x)", term.toStr().toString());

        term = new JSTerm();
        term.parseTerm(new StringReader("(call f x)"));
        assertEquals("(call f x)", term.toStr().toString());
    }
}