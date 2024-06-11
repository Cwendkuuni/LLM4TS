package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testParseListEmpty() {
        JSTerm term = new JSTerm();
        JSTerm list = term.parseList(new StreamTokenizer(new StringReader("")));
        assertNull(list);
    }

    @Test
    public void testParseListNull() {
        JSTerm term = new JSTerm();
        JSTerm list = term.parseList(new StreamTokenizer(new StringReader(null)));
        assertNull(list);
    }

    @Test
    public void testParseListSingleElement() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term)"));
        JSTerm list = term.parseList(tokenizer);
        assertEquals(1, list.size());
        assertEquals("term", list.elementAt(0).toString());
    }

    @Test
    public void testParseListMultipleElements() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term1 term2 term3)"));
        JSTerm list = term.parseList(tokenizer);
        assertEquals(3, list.size());
        assertEquals("term1", list.elementAt(0).toString());
        assertEquals("term2", list.elementAt(1).toString());
        assertEquals("term3", list.elementAt(2).toString());
    }

    @Test(expected = JSParserError.class)
    public void testParseListInvalidElements() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term term %%% term)"));
        term.parseList(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testParseListMissingRightParenthesis() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term1 term2 term3) term4"));
        term.parseList(tokenizer);
    }

    @Test
    public void testCloneT() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.addElement("term2");
        JSTerm term2 = term1.cloneT();
        assertEquals(2, term2.size());
        assertEquals("term1", term2.elementAt(0).toString());
        assertEquals("term2", term2.elementAt(1).toString());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElements(new JSPair("x", "y"));
        JSTerm termSubstituted = term.applySubstitutionT(substitution);
        assertEquals(1, termSubstituted.size());
        assertEquals("y", termSubstituted.elementAt(0).toString());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("term1");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("term2");
        term3.makeConstant();

        JSSubstitution substitution = new JSSubstitution();
        JSTerm term4 = new JSTerm();
        term4.addElement("x");
        term4.makeVariable();
        substitution.addElements(new JSPair("x", term1));

        JSTerm term5 = new JSTerm();
        term5.addElement("x");
        term5.makeVariable();

        JSTerm term6 = new JSTerm();
        term6.addElement("term1");
        term6.makeFunction();

        JSTerm term7 = new JSTerm();
        term7.addElement("term2");
        term7.makeFunction();

        assertEquals(term1, term1.matches(term1));
        assertEquals(term2, term2.matches(term2));
        assertFalse(term1.matches(term2));
        assertFalse(term2.matches(term1));
        assertFalse(term1.matches(term3));
        assertFalse(term3.matches(term1));
        assertEquals(substitution, term4.matches(term5));
        assertFalse(term5.matches(term4));
        assertFalse(term1.matches(term6));
        assertFalse(term6.matches(term1));
        assertFalse(term2.matches(term7));
        assertFalse(term7.matches(term2));
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("term1");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("term2");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("term1");
        term4.makeFunction();

        assertTrue(term1.equals(term1));
        assertTrue(term1.equals(term2));
        assertFalse(term1.equals(term3));
        assertFalse(term1.equals(term4));
        assertFalse(term2.equals(term3));
        assertFalse(term2.equals(term4));
        assertFalse(term3.equals(term4));
    }

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeConstant();
        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeFunction();
        assertTrue(term.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeEval(true);
        assertTrue(term.isEval());
    }

    @Test
    public void testMakeFunction() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeFunction();
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
    }

    @Test
    public void testMakeVariable() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
    }

    @Test
    public void testMakeConstant() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeConstant();
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isFunction());
    }

    @Test
    public void testMakeEval() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeEval(true);
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isEval());
    }

    @Test
    public void testToString() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        assertEquals("term", term.toStr().toString());

        JSTerm termList = new JSTerm();
        termList.addElement("term1");
        termList.addElement("term2");
        assertEquals("(term1 term2)", termList.toStr().toString());

        JSTerm termFunction = new JSTerm();
        termFunction.makeFunction();
        termFunction.addElement("term");
        assertEquals("(term)", termFunction.toStr().toString());

        JSTerm termEval = new JSTerm();
        termEval.makeEval(true);
        termEval.addElement("term");
        assertEquals("term", termEval.toStr().toString());
    }

    @Test
    public void testIsGround() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.makeConstant();
        assertTrue(term1.isGround());

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.makeVariable();
        assertFalse(term2.isGround());

        JSTerm termList = new JSTerm();
        termList.addElement("term1");
        termList.addElement("term2");
        assertFalse(termList.isGround());

        JSTerm termFunction = new JSTerm();
        termFunction.makeFunction();
        termFunction.addElement("term");
        assertFalse(termFunction.isGround());

        JSTerm termEval = new JSTerm();
        termEval.makeEval(true);
        termEval.addElement("term");
        assertTrue(termEval.isGround());

        JSTerm termGround = new JSTerm();
        termGround.addElement("term1");
        termGround.addElement("term1");
        termGround.addElement("term1");
        termGround.makeConstant();
        termGround.elementAt(0).makeConstant();
        termGround.elementAt(1).makeConstant();
        termGround.elementAt(2).makeConstant();
        assertTrue(termGround.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("term");
        term.makeConstant();
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("term", standardizedTerm.toString());

        JSTerm termFunction = new JSTerm();
        termFunction.makeFunction();
        termFunction.addElement("term");
        JSTerm standardizedTermFunction = termFunction.standardizerTerm();
        assertEquals("(function term)", standardizedTermFunction.toString());

        JSTerm termVariable = new JSTerm();
        termVariable.addElement("x");
        termVariable.makeVariable();
        JSTerm standardizedTermVariable = termVariable.standardizerTerm();
        assertEquals("(variable x)", standardizedTermVariable.toString());

        JSTerm termList = new JSTerm();
        termList.addElement("term1");
        termList.addElement("term2");
        JSTerm standardizedTermList = termList.standardizerTerm();
        assertEquals("(list (term1) (term2))", standardizedTermList.toString());

        JSTerm termCall = new JSTerm();
        termCall.addElement("call");
        termCall.addElement("term");
        termCall.addElement(new JSTerm());
        JSTerm standardizedTermCall = termCall.standardizerTerm();
        assertEquals("(call function term nil)", standardizedTermCall.toString());
    }

    @Test
    public void testCall() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.makeConstant();
        JSTerm term2 = new JSTerm();
        term2.addElement("term2");
        term2.makeConstant();
        JSTerm term3 = new JSTerm();
        term3.addElement("+");
        term3.makeOperator(1);
        JSTerm term4 = new JSTerm();
        term4.addElement("term1");
        term4.makeTerm();
        term4.addElement("term2");
        term4.makeTerm();
        JSTerm expectedResult = new JSTerm();
        expectedResult.addElement("term1");
        expectedResult.addElement("term2");
        JSTerm result = term3.call();
        assertEquals(expectedResult, result);

        JSTerm term5 = new JSTerm();
        term5.addElement("not");
        term5.makeOperator(0);
        term5.addElement("term1");
        term5.makeTerm();
        JSTerm expectedResult2 = new JSTerm();
        expectedResult2.addElement("nil");
        result = term5.call();
        assertEquals(expectedResult2, result);

        JSTerm term6 = new JSTerm();
        term6.addElement("term1");
        term6.makeFunction();
        term6.addElement("term2");
        term6.addElement("term3");
        JSTerm expectedResult3 = new JSTerm();
        expectedResult3.addElement("term1");
        expectedResult3.addElement("term2");
        expectedResult3.addElement("term3");
        result = term6.call();
        assertEquals(expectedResult3, result);

        JSTerm term7 = new JSTerm();
        term7.addElement("term1");
        term7.makeEval(true);
        result = term7.call();
        assertEquals(term1, result);

        JSTerm term8 = new JSTerm();
        term8.addElement("term1");
        term8.makeVariable();
        JSTerm var = term8.elementAt(0);
        term8.addElement("term2");
        term8.addElement("term3");
        JSTerm expectedResult4 = new JSTerm();
        expectedResult4.addElement("term2");
        expectedResult4.addElement("term3");
        result = term8.call();
        assertEquals(expectedResult4, result);

        JSTerm term9 = new JSTerm();
        term9.addElement("term1");
        term9.makeFunction();
        term9.addElement("term2");
        term9.addElement(new JSTerm());
        term9.addElement(new JSTerm());
        term9.addElement(new JSTerm());
        JSTerm expectedResult5 = new JSTerm();
        expectedResult5.addElement("term1");
        expectedResult5.addElement("term2");
        expectedResult5.addElement(new JSTerm());
        expectedResult5.addElement(new JSTerm());
        expectedResult5.addElement(new JSTerm());
        result = term9.call();
        assertEquals(expectedResult5, result);
    }
}