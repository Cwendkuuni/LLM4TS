import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTermDefaultConstructor() {
        JSTerm term = new JSTerm();
        assertTrue(term.isFunction());
        assertTrue(term.isEmpty());
    }

    @Test
    public void testJSTermTokenizerConstructorWithConstantTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(5)")));
        assertTrue(term.isConstant());
        assertEquals("5", term.elementAt(0));
        assertTrue(term.isEmpty());
    }

    @Test
    public void testJSTermTokenizerConstructorWithVariableTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
        assertTrue(term.isVariable());
        assertEquals("x", term.elementAt(0));
        assertTrue(term.isEmpty());
    }

    @Test
    public void testJSTermTokenizerConstructorWithFunctionTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x y)")));
        assertTrue(term.isFunction());
        assertEquals("+", term.elementAt(0));
        assertFalse(term.isEmpty());
        assertEquals(2, term.size());
        assertEquals("x", term.elementAt(1).elementAt(0));
        assertEquals("y", term.elementAt(1).elementAt(1).elementAt(0));
    }

    @Test
    public void testJSTermTokenizerConstructorWithListTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(list x y z)")));
        assertTrue(term.isFunction());
        assertEquals("list", term.elementAt(0));
        assertFalse(term.isEmpty());
        assertEquals(3, term.size());
        assertEquals("x", term.elementAt(1).elementAt(0));
        assertEquals("y", term.elementAt(1).elementAt(1).elementAt(0));
        assertEquals("z", term.elementAt(1).elementAt(2).elementAt(0));
    }

    @Test
    public void testParseListWithEmptyList() {
        JSTerm term = new JSTerm();
        JSTerm parsedList = term.parseList(new StreamTokenizer(new StringReader("()")));
        assertTrue(parsedList.isConstant());
        assertEquals("nil", parsedList.elementAt(0));
        assertTrue(parsedList.isEmpty());
    }

    @Test
    public void testParseListWithSingleElement() {
        JSTerm term = new JSTerm();
        JSTerm parsedList = term.parseList(new StreamTokenizer(new StringReader("(x)")));
        assertEquals(1, parsedList.size());
        assertEquals("x", parsedList.elementAt(0).elementAt(0));
        assertTrue(parsedList.elementAt(0).isEmpty());
    }

    @Test
    public void testParseListWithMultipleElements() {
        JSTerm term = new JSTerm();
        JSTerm parsedList = term.parseList(new StreamTokenizer(new StringReader("(x y z)")));
        assertEquals(3, parsedList.size());
        assertEquals("x", parsedList.elementAt(0).elementAt(0));
        assertEquals("y", parsedList.elementAt(1).elementAt(0));
        assertEquals("z", parsedList.elementAt(2).elementAt(0));
        assertTrue(parsedList.elementAt(0).isEmpty());
        assertTrue(parsedList.elementAt(1).isEmpty());
        assertTrue(parsedList.elementAt(2).isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testParseListWithInvalidInput() {
        JSTerm term = new JSTerm();
        term.parseList(new StreamTokenizer(new StringReader("InvalidInput")));
    }

    @Test
    public void testPrintList() {
        JSTerm term = new JSTerm();
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        term.addElement(new JSTerm());
        String expectedOutput = "nil) ";
        String actualOutput = term.printList(term).toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.addElement("y");
        JSTerm clonedTerm = term.cloneT();
        assertEquals(term.toStr(), clonedTerm.toStr());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("y")));
        JSTerm result = term.applySubstitutionT(substitution);
        assertEquals("y", result.elementAt(0));
    }

    @Test
    public void testMatchesWithSameTerms() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.addElement("y");
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesWithDifferentTerms() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        JSTerm term2 = new JSTerm();
        term2.addElement("z");
        term2.addElement("w");
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesWithVariableTerms() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.addElement("y");
        JSTerm term2 = new JSTerm();
        term2.addElement("z");
        term2.addElement("x");
        JSSubstitution substitution = term1.matches(term2);
        JSPairTerm pair = (JSPairTerm) substitution.elementAt(0);
        assertEquals("x", pair.getFirst());
        assertEquals("z", pair.getSecond());
    }

    @Test
    public void testMatchesWithFunctionTerms() {
        JSTerm term1 = new JSTerm();
        term1.makeFunction();
        term1.addElement("+");
        term1.addElement("x");
        term1.addElement("y");
        JSTerm term2 = new JSTerm();
        term2.makeFunction();
        term2.addElement("+");
        term2.addElement("z");
        term2.addElement("w");
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }
}