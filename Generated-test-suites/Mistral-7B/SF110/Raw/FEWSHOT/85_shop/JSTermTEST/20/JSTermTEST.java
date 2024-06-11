package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testConstruction() {
        JSTerm term = new JSTerm();
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertEquals(0, term.size());

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(foo)"));
        term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals(1, term.size());
        assertEquals("foo", term.elementAt(0));

        term = new JSTerm(tokenizer);
        assertTrue(term.isEval());
        assertEquals(1, term.size());
        assertEquals("call", term.elementAt(0));

        term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals(2, term.size());
        assertEquals("list", term.elementAt(0));
        JSTerm listTerm = term.elementAt(1);
        assertEquals("foo", listTerm.elementAt(0));
        assertEquals("nil", listTerm.elementAt(1).toString());
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(foo . (bar . (baz . nil)))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm listTerm = term.elementAt(0);
        assertEquals("list", listTerm.elementAt(0));
        assertEquals(3, listTerm.size());
        assertEquals("foo", listTerm.elementAt(1));
        JSTerm subListTerm = listTerm.elementAt(2);
        assertEquals(".", listTerm.elementAt(1));
        assertEquals("bar", subListTerm.elementAt(0));
        subListTerm = subListTerm.elementAt(1);
        assertEquals(".", subListTerm.elementAt(0));
        assertEquals("baz", subListTerm.elementAt(0));
        assertEquals("nil", subListTerm.elementAt(1).toString());
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm();
        term.addElement("foo");
        term.makeConstant();
        System.out.println(term.toStr()); // prints: foo

        term.makeVariable();
        System.out.println(term.toStr()); // prints: .foo

        term = new JSTerm();
        term.addElement("foo");
        term.addElement("bar");
        term.addElement("baz");
        term.makeFunction();
        System.out.println(term.toStr()); // prints: (foo bar baz)

        term = new JSTerm();
        term.addElement("call");
        term.addElement("foo");
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        System.out.println(term.toStr()); // prints: (call foo nil nil)
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("foo");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("bar");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("foo");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("foo");
        term4.makeVariable();

        JSTerm term5 = new JSTerm();
        term5.addElement("foo");
        term5.addElement("bar");
        term5.makeFunction();

        JSTerm term6 = new JSTerm();
        term6.addElement("call");
        term6.addElement("foo");
        term6.addElement(new JSTerm());
        term6.addElement(new JSTerm());

        JSTerm term7 = new JSTerm();
        term7.addElement("call");
        term7.addElement("foo");
        term7.addElement(new JSTerm());
        term7.addElement(new JSTerm());
        JSTerm innerTerm1 = term7.elementAt(2);
        innerTerm1.addElement("bar");
        innerTerm1.makeConstant();

        JSTerm innerTerm2 = term7.elementAt(3);
        innerTerm2.addElement("baz");
        innerTerm2.makeConstant();

        assertTrue(term1.equals(term3));
        assertFalse(term1.equals(term2));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
        assertTrue(term6.equals(term7));
        assertTrue(term2.equals(term2));
        assertTrue(term3.equals(term3));
        assertTrue(term4.equals(term4));
        assertTrue(term5.equals(term5));
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("foo");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("bar");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("foo");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("foo");
        term4.addElement("bar");
        term4.makeFunction();

        JSTerm term5 = new JSTerm();
        term5.addElement("call");
        term5.addElement("foo");
        term5.addElement(new JSTerm());
        term5.addElement(new JSTerm());

        JSTerm term6 = new JSTerm();
        term6.addElement("call");
        term6.addElement("foo");
        term6.addElement(new JSTerm());
        term6.addElement(new JSTerm());
        JSTerm innerTerm1 = term6.elementAt(2);
        innerTerm1.addElement("bar");
        innerTerm1.makeConstant();

        JSTerm innerTerm2 = term6.elementAt(3);
        innerTerm2.addElement("baz");
        innerTerm2.makeConstant();

        JSSubstitution substitution = term1.matches(term4);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(term3);
        JSTerm variableTerm = substitution.getInstance(term3);
        assertEquals("foo", variableTerm.elementAt(0));
        assertEquals(".", variableTerm.elementAt(0));
        assertEquals(".", variableTerm.elementAt(1));

        substitution = term1.matches(term5);
        assertTrue(substitution.isEmpty());

        substitution = term2.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term2.matches(term2);
        assertTrue(substitution.isEmpty());

        substitution = term3.matches(term1);
        assertFalse(substitution.isEmpty());

        substitution = term4.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(term2);
        assertFalse(substitution.isEmpty());

        substitution = term1.matches(term3);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(term4);
        assertFalse(substitution.isEmpty());

        substitution = term1.matches(term5);
        assertTrue(substitution.isEmpty());

        substitution = term1.matches(term6);
        assertTrue(substitution.isEmpty());

        substitution = term4.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term4.matches(term2);
        assertFalse(substitution.isEmpty());

        substitution = term4.matches(term3);
        assertFalse(substitution.isEmpty());

        substitution = term4.matches(term4);
        assertTrue(substitution.isEmpty());

        substitution = term4.matches(term5);
        assertFalse(substitution.isEmpty());

        substitution = term4.matches(term6);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term2);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term3);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term4);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term5);
        assertTrue(substitution.isEmpty());

        substitution = term5.matches(term6);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term1);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term2);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term3);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term4);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term5);
        assertTrue(substitution.isEmpty());

        substitution = term6.matches(term6);
        assertTrue(substitution.isEmpty());
    }
}