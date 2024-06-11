package umd.cs.shop; // Import the package name if necessary

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.Stream;

public class JSTermTest {

    @Test
    public void testJSTermCreation() {
        JSTerm term = new JSTerm();
        assertFalse(term.isEval());
        assertFalse(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Term"));
        JSTerm termFromTokenizer = new JSTerm(tokenizer);
        assertTrue(termFromTokenizer.isEval());

        term.makeFunction();
        assertTrue(term.isFunction());
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());

        term.makeConstant();
        assertTrue(term.isConstant());
        assertFalse(term.isVariable());

        String variable = "x";
        term = new JSTerm(tokenizer);
        tokenizer.pushBack();
        tokenizer.nextToken();
        assertEquals(variable, term.elementAt(0));
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
    }

    @Test
    public void testParseList() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a b c)"));
        JSTerm parsedList = term.parseList(tokenizer);
        String expectedList = "((a) (b) (c) nil)";
        assertEquals(expectedList, parsedList.toStr().toString());
    }

    @Test
    public void testExceptionHandlingDuringParse() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Term Expected"));
        try {
            term.parseList(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // Expected exception
        }
    }

    @Test
    public void testApplySubstitution() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairTerm("x", "y"));

        JSTerm result = term1.applySubstitutionT(alpha);
        String expectedResult = "y";
        assertEquals(expectedResult, result.elementAt(0).toString());

        term1.makeConstant();
        term1.addElement("5");
        term1.makeConstant();

        result = term1.applySubstitutionT(alpha);
        expectedResult = "5";
        assertEquals(expectedResult, result.elementAt(0).toString());
    }

    @Test
    public void testMatches() {
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
        term4.addElement("5");
        term4.makeConstant();

        JSTerm term5 = new JSTerm();
        term5.addElement("(x)");
        term5.makeFunction();

        JSTerm term6 = new JSTerm();
        term6.addElement("(x y)");
        term6.makeFunction();

        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairTerm("x", "y"));

        JSSubstitution result = term1.matches(term2);
        assertTrue(result.isEmpty());

        result = term1.matches(term3);
        assertTrue(result.size() > 0);

        result = term3.matches(term1);
        assertTrue(result.size() > 0);

        result = term1.matches(term4);
        assertTrue(result.isEmpty());

        result = term1.matches(term5);
        assertTrue(result.isEmpty());

        result = term6.matches(term1);
        assertTrue(result.isEmpty());

        result = term1.matches(term1, alpha);
        assertTrue(result.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesWithNullTerm() {
        JSTerm term = new JSTerm();
        term.matches(null);
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.makeVariable();
        term.addElement("f(x)");
        term.makeFunction();

        JSTerm expectedStandardizerTerm = new JSTerm();
        expectedStandardizerTerm.makeFunction();
        expectedStandardizerTerm.addElement("x1");
        expectedStandardizerTerm.makeVariable();
        expectedStandardizerTerm.addElement("x2");
        expectedStandardizerTerm.makeVariable();
        expectedStandardizerTerm.addElement("f(x1)");
        expectedStandardizerTerm.makeFunction();

        JSTerm result = term.standardizerTerm();
        assertEquals(expectedStandardizerTerm.toStr().toString(), result.toStr().toString());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.makeVariable();
        term.addElement("f(x)");
        term.makeFunction();

        assertEquals("(x) (f (x))", term.toStr().toString());
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
        term3.addElement("y");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("5");
        term4.makeConstant();

        JSTerm term5 = new JSTerm();
        term5.addElement("x");
        term5.makeVariable();
        term5.addElement("5");
        term5.makeConstant();

        JSTerm term6 = new JSTerm();
        term6.addElement("(x)");
        term6.makeFunction();

        JSTerm term7 = new JSTerm();
        term7.addElement("(x y)");
        term7.makeFunction();

        assertTrue(term1.equals(term1));
        assertTrue(term1.equals(term1));
        assertFalse(term1.equals(term2));
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term4));
        assertFalse(term1.equals(term5));
        assertFalse(term1.equals(term6));
        assertFalse(term1.equals(term7));
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();

        term.addElement("5");
        term.makeConstant();

        JSTerm termConstant = new JSTerm();
        termConstant.addElement("5");
        termConstant.makeConstant();

        assertTrue(termConstant.isGround());
        assertFalse(term.isGround());

        term.addElement(termConstant);
        term.makeConstant();

        assertTrue(term.isGround());
    }

    @Test
    public void testStandardizerTermWithGroundTerms() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("5");
        term.makeConstant();

        JSTerm expectedStandardizerTerm = new JSTerm();
        expectedStandardizerTerm.makeFunction();
        expectedStandardizerTerm.addElement("x1");
        expectedStandardizerTerm.makeVariable();
        expectedStandardizerTerm.addElement("5");
        expectedStandardizerTerm.makeConstant();

        JSTerm result = term.standardizerTerm();
        assertEquals(expectedStandardizerTerm.toStr().toString(), result.toStr().toString());
    }
}