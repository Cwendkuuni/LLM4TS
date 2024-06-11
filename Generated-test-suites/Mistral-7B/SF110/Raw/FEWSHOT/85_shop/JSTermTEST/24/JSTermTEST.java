package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSTermVariable;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;

public class JSTermTest {

    @Test
    public void testParseConstantTerm() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("42"));
        term.parse(tokenizer);
        JSTerm expectedTerm = new JSTerm();
        expectedTerm.addElement("42");
        expectedTerm.makeConstant();
        assertEquals(expectedTerm, term);
    }

    @Test
    public void testParseVariableTerm() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x"));
        term.parse(tokenizer);
        JSTerm expectedTerm = new JSTerm();
        expectedTerm.addElement("x");
        expectedTerm.makeVariable();
        assertEquals(expectedTerm, term);
    }

    @Test
    public void testParseFunctionTerm() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        term.parse(tokenizer);
        JSTerm expectedTerm = new JSTerm();
        expectedTerm.makeFunction();
        expectedTerm.addElement("+");
        JSTerm arg1 = new JSTerm();
        arg1.addElement("1");
        arg1.makeConstant();
        JSTerm arg2 = new JSTerm();
        arg2.addElement("2");
        arg2.makeConstant();
        expectedTerm.addElement(arg1);
        expectedTerm.addElement(arg2);
        assertEquals(expectedTerm, term);
    }

    @Test
    public void testParseFunctionCallTerm() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        term.parse(tokenizer);
        JSTerm expectedTerm = term.call();
        assertEquals(expectedTerm, new JSTerm(new JSTerm("+"), new JSTerm("1"), new JSTerm("2")));
    }

    @Test
    public void testParseListTerm() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1 2 3)"));
        term.parse(tokenizer);
        JSTerm expectedTerm = new JSTerm();
        expectedTerm.addElement(".");
        expectedTerm.addElement(new JSTermVariable("list"));
        expectedTerm.addElement(new JSTerm(new JSTerm("."), new JSTermVariable("cons"), new JSTerm(new JSTermVariable("nil"), new JSTermVariable("nil"))));
        expectedTerm.addElement(new JSTerm(new JSTermVariable("cons"), new JSTerm("1"), new JSTerm("2")));
        expectedTerm.addElement(new JSTerm(new JSTermVariable("cons"), new JSTerm("2"), new JSTerm("3")));
        expectedTerm.addElement(new JSTermVariable("nil"));
        assertEquals(expectedTerm, term);
    }

    @Test
    public void testParseTermWithOperator() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2 3)"));
        term.parse(tokenizer);
        JSTerm expectedTerm = new JSTerm();
        expectedTerm.makeFunction();
        expectedTerm.addElement("+");
        JSTerm arg1 = new JSTerm();
        arg1.addElement("1");
        arg1.makeConstant();
        JSTerm arg2 = new JSTerm();
        arg2.addElement("2");
        arg2.makeConstant();
        JSTerm arg3 = new JSTerm();
        arg3.addElement("3");
        arg3.makeConstant();
        expectedTerm.addElement(arg1);
        expectedTerm.addElement(arg2);
        expectedTerm.addElement(arg3);
        assertEquals(expectedTerm, term);
    }

    @Test
    public void testApplySubstitution() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("y");
        term.makeVariable();
        JSTerm substitutionTerm = new JSTerm();
        substitutionTerm.addElement("z");
        substitutionTerm.makeVariable();
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair<String, JSTerm>("x", substitutionTerm));
        JSTerm substitutedTerm = term.applySubstitution(substitution);
        JSTerm expectedSubstitutedTerm = new JSTerm();
        expectedSubstitutedTerm.addElement("z");
        expectedSubstitutedTerm.makeVariable();
        assertEquals(expectedSubstitutedTerm, substitutedTerm);
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
        term4.addElement("z");
        term4.makeVariable();
        JSTerm term5 = new JSTerm();
        term5.addElement("5");
        term5.makeConstant();
        JSTerm term6 = new JSTerm();
        term6.addElement("x");
        term6.makeVariable();
        term6.addElement("3");
        term6.makeConstant();
        term6.addElement("+");
        term6.makeFunction();
        JSTerm term7 = new JSTerm();
        term7.addElement("y");
        term7.makeVariable();
        JSTerm term8 = new JSTerm();
        term8.addElement("2");
        term8.makeConstant();
        JSTerm term9 = new JSTerm();
        term9.addElement("+");
        term9.makeFunction();
        JSTerm term10 = new JSTerm();
        term10.addElement("z");
        term10.makeVariable();
        JSTerm term11 = new JSTerm();
        term11.addElement("1");
        term11.makeConstant();
        JSTerm term12 = new JSTerm();
        term12.addElement("=");
        term12.makeFunction();
        JSTerm term13 = new JSTerm();
        term13.addElement("x");
        term13.makeVariable();
        term13.addElement("3");
        term13.makeConstant();
        JSTerm term14 = new JSTerm();
        term14.addElement("y");
        term14.makeVariable();
        JSTerm term15 = new JSTerm();
        term15.addElement("2");
        term15.makeConstant();
        JSTerm term16 = new JSTerm();
        term16.addElement("=");
        term16.makeFunction();
        JSTerm term17 = new JSTerm();
        term17.addElement("z");
        term17.makeVariable();
        term17.addElement("1");
        term17.makeConstant();
        JSTerm term18 = new JSTerm();
        term18.addElement("nil");
        term18.makeConstant();
        JSTerm term19 = new JSTerm();
        term19.addElement("nil");
        term19.makeConstant();
        JSTerm term20 = new JSTerm();
        term20.addElement(".");
        term20.addElement(new JSTermVariable("cons"));
        term20.addElement(term1);
        term20.addElement(term2);
        term20.addElement(term3);
        term20.addElement(term4);
        term20.addElement(term5);
        term20.addElement(term6);
        term20.addElement(term7);
        term20.addElement(term8);
        term20.addElement(term9);
        term20.addElement(term10);
        term20.addElement(term11);
        term20.addElement(term12);
        term20.addElement(term13);
        term20.addElement(term14);
        term20.addElement(term15);
        term20.addElement(term16);
        term20.addElement(term17);
        term20.addElement(term18);
        JSTerm term21 = new JSTerm();
        term21.addElement(".");
        term21.addElement(new JSTermVariable("cons"));
        term21.addElement(term1);
        term21.addElement(term2);
        term21.addElement(term3);
        term21.addElement(term4);
        term21.addElement(term5);
        term21.addElement(term6);
        term21.addElement(term7);
        term21.addElement(term8);
        term21.addElement(term9);
        term21.addElement(term10);
        term21.addElement(term11);
        term21.addElement(term12);
        term21.addElement(term13);
        term21.addElement(term14);
        term21.addElement(term15);
        term21.addElement(term16);
        term21.addElement(term17);
        term21.addElement(new JSTermVariable("nil"));
        JSTerm term22 = new JSTerm();
        term22.addElement(".");
        term22.addElement(new JSTermVariable("cons"));
        term22.addElement(term1);
        term22.addElement(term2);
        term22.addElement(term3);
        term22.addElement(term4);
        term22.addElement(term5);
        term22.addElement(term6);
        term22.addElement(term7);
        term22.addElement(term8);
        term22.addElement(term9);
        term22.addElement(term10);
        term22.addElement(term11);
        term22.addElement(term12);
        term22.addElement(term13);
        term22.addElement(term14);
        term22.addElement(term15);
        term22.addElement(term16);
        term22.addElement(term17);
        term22.addElement(new JSTermVariable("nil"));
        JSSubstitution substitution = new JSSubstitution();
        JSTerm result = term20.matches(term21, substitution);
        JSTerm expectedResult = new JSTerm();
        expectedResult.addElement("nil");
        expectedResult.makeConstant();
        assertEquals(expectedResult, result);

        substitution = new JSSubstitution();
        substitution.addElement(new JSPair<String, JSTerm>("x", term1));
        substitution.addElement(new JSPair<String, JSTerm>("y", term2));
        substitution.addElement(new JSPair<String, JSTerm>("z", term3));
        result = term20.matches(term21, substitution);
        expectedResult = new JSTerm();
        expectedResult.addElement("x");
        expectedResult.makeVariable();
        expectedResult.addElement("y");
        expectedResult.makeVariable();
        expectedResult.addElement("z");
        expectedResult.makeVariable();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        term.addElement("y");
        term.makeVariable();
        term.addElement("+");
        term.makeFunction();
        term.addElement("x");
        term.makeVariable();
        term.addElement("y");
        term.makeVariable();
        JSTerm standardizedTerm = term.standardizerTerm();
        JSTerm expectedStandardizedTerm = new JSTerm();
        expectedStandardizedTerm.addElement("x");
        expectedStandardizedTerm.makeVariable();
        expectedStandardizedTerm.addElement("x1");
        expectedStandardizedTerm.makeVariable();
        expectedStandardizedTerm.addElement("+");
        expectedStandardizedTerm.makeFunction();
        expectedStandardizedTerm.addElement("x1");
        expectedStandardizedTerm.makeVariable();
        expectedStandardizedTerm.addElement("y");
        expectedStandardizedTerm.makeVariable();
        assertEquals(expectedStandardizedTerm, standardizedTerm);
    }
}