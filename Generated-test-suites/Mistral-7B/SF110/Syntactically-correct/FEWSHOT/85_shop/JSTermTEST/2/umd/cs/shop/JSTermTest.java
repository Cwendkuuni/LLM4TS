package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTerm() {
        // Test default constructor
        JSTerm jsTerm = new JSTerm();
        assertEquals(0, jsTerm.size());
        assertFalse(jsTerm.isVariable());
        assertFalse(jsTerm.isConstant());
        assertFalse(jsTerm.isFunction());
        assertFalse(jsTerm.isEval());

        // Test constructor with StreamTokenizer
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("((a b) c)"));
        JSTerm jsTermFromTokenizer = new JSTerm(tokenizer);
        assertEquals(3, jsTermFromTokenizer.size());
        assertTrue(jsTermFromTokenizer.isFunction());
        assertEquals("((", jsTermFromTokenizer.elementAt(0));
        assertEquals("a", jsTermFromTokenizer.elementAt(1).elementAt(0));
        assertEquals("b", jsTermFromTokenizer.elementAt(1).elementAt(1));
        assertEquals("c", jsTermFromTokenizer.elementAt(2).elementAt(0));

        // Test cloneT method
        JSTerm clonedTerm = jsTermFromTokenizer.cloneT();
        assertEquals(jsTermFromTokenizer, clonedTerm);
        clonedTerm.makeConstant();
        assertFalse(jsTermFromTokenizer.isConstant());
        assertTrue(clonedTerm.isConstant());

        // Test applySubstitutionT method
        JSSubstitution alpha = new JSSubstitution();
        alpha.assign(new JSPair("a", new JSTerm("c")));
        JSTerm substitutedTerm = jsTermFromTokenizer.applySubstitutionT(alpha);
        assertEquals("(c c)", substitutedTerm.toStr());

        // Test matches method
        JSTerm termToMatch = new JSTerm();
        termToMatch.addElement("a");
        termToMatch.makeVariable();
        JSSubstitution matchesResult = jsTermFromTokenizer.matches(termToMatch);
        assertTrue(matchesResult.isEmpty());

        termToMatch.makeConstant();
        termToMatch.addElement("((a b) c)");
        matchesResult = jsTermFromTokenizer.matches(termToMatch);
        assertTrue(matchesResult.isEmpty());

        matchesResult = jsTermFromTokenizer.matches(jsTermFromTokenizer);
        assertTrue(matchesResult.isEmpty());

        termToMatch.makeFunction();
        termToMatch.addElement("(list (a b) c)");
        matchesResult = jsTermFromTokenizer.matches(termToMatch);
        assertTrue(matchesResult.isEmpty());

        termToMatch.makeList();
        termToMatch.addElement("a");
        termToMatch.addElement("b");
        termToMatch.addElement("c");
        matchesResult = jsTermFromTokenizer.matches(termToMatch);
        assertTrue(matchesResult.isEmpty());

        // Test equals method
        JSTerm term1 = new JSTerm();
        term1.addElement("a");
        term1.makeVariable();
        JSTerm term2 = new JSTerm();
        term2.addElement("b");
        term2.makeVariable();
        assertFalse(term1.equals(term2));

        term1.makeConstant();
        term1.addElement("a");
        term2.makeConstant();
        term2.addElement("a");
        assertTrue(term1.equals(term2));

        term1.makeFunction();
        term1.addElement("(list a b)");
        term2.makeFunction();
        term2.addElement("(list a c)");
        assertFalse(term1.equals(term2));

        JSTerm term3 = term1.cloneT();
        assertTrue(term1.equals(term3));

        // Test isVariable, isConstant, isFunction, and isEval methods
        JSTerm constantTerm = new JSTerm();
        constantTerm.makeConstant();
        constantTerm.addElement("42");
        assertTrue(constantTerm.isConstant());
        assertFalse(constantTerm.isVariable());
        assertFalse(constantTerm.isFunction());
        assertFalse(constantTerm.isEval());

        JSTerm variableTerm = new JSTerm();
        variableTerm.makeVariable();
        variableTerm.addElement("x");
        assertTrue(variableTerm.isVariable());
        assertFalse(variableTerm.isConstant());
        assertFalse(variableTerm.isFunction());
        assertFalse(variableTerm.isEval());

        JSTerm functionTerm = new JSTerm();
        functionTerm.makeFunction();
        functionTerm.addElement("(list x y)");
        assertTrue(functionTerm.isFunction());
        assertFalse(functionTerm.isVariable());
        assertFalse(functionTerm.isConstant());
        assertFalse(functionTerm.isEval());

        JSTerm evalTerm = new JSTerm();
        evalTerm.makeEval(true);
        evalTerm.addElement("(list x y)");
        assertTrue(evalTerm.isFunction());
        assertFalse(evalTerm.isVariable());
        assertFalse(evalTerm.isConstant());
        assertTrue(evalTerm.isEval());

        // Test print method
        JSTerm printTerm = new JSTerm();
        printTerm.makeConstant();
        printTerm.addElement("42");
        JSTerm printTerm2 = new JSTerm();
        printTerm2.makeVariable();
        printTerm2.addElement("x");
        JSTerm printTerm3 = new JSTerm();
        printTerm3.makeFunction();
        printTerm3.addElement("(list x y)");
        JSTerm printTerm4 = new JSTerm();
        printTerm4.makeList();
        printTerm4.addElement(printTerm);
        printTerm4.addElement(printTerm2);
        printTerm4.addElement(printTerm3);
        List<String> expectedPrintOrder = Arrays.asList("42", "x", "(list x y)", "((", "42", " . ", "x", " . ", "(list x y)", "))");
        for (JSTerm term : Arrays.asList(printTerm, printTerm2, printTerm3, printTerm4)) {
            term.print();
            System.out.print(" ");
        }
        System.out.println();
        List<String> actualPrintOrder = new ArrayList<>();
        try (StringReader reader = new StringReader(System.in)) {
            int c;
            while ((c = reader.read()) != -1) {
                actualPrintOrder.add(Character.toString((char) c));
            }
        }
        assertEquals(expectedPrintOrder, actualPrintOrder);

        // Test isGround method
        JSTerm groundTerm = new JSTerm();
        groundTerm.makeConstant();
        groundTerm.addElement("42");
        assertTrue(groundTerm.isGround());

        JSTerm nonGroundTerm = new JSTerm();
        nonGroundTerm.makeVariable();
        nonGroundTerm.addElement("x");
        assertFalse(nonGroundTerm.isGround());

        JSTerm listTerm = new JSTerm();
        listTerm.makeList();
        listTerm.addElement(groundTerm);
        listTerm.addElement(nonGroundTerm);
        assertFalse(listTerm.isGround());

        // Test standardizerTerm method
        JSTerm standardizedTerm = jsTermFromTokenizer.standardizerTerm();
        assertEquals("(list ((list a b) c) nil)", standardizedTerm.toStr());

        // Test call method
        JSTerm callResult = jsTermFromTokenizer.call();
        assertEquals("(call ((a b) c))", callResult.toStr());
    }

    @Test
    public void testJSTermWithFailingMatches() {
        JSTerm jsTerm1 = new JSTerm();
        jsTerm1.addElement("(list x y)");
        JSTerm jsTerm2 = new JSTerm();
        jsTerm2.addElement("(list z w)");
        JSSubstitution substitution = jsTerm1.matches(jsTerm2);
        assertTrue(substitution.fail());

        substitution = jsTerm2.matches(jsTerm1);
        assertTrue(substitution.fail());

        substitution = jsTerm1.matches(new JSTerm());
        assertTrue(substitution.isEmpty());

        substitution = jsTerm2.matches(new JSTerm());
        assertTrue(substitution.isEmpty());
    }

    @Test(expected = JSParserError.class)
    public void testJSUtilFailToReadToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("((a b) c)"));
        JSUtil.readToken(tokenizer, "Expecting term");
    }

    @Test(expected = JSParserError.class)
    public void testJSUtilFailToExpectTokenType() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("((a b) c)"));
        tokenizer.nextToken();
        JSUtil.expectTokenType(JSJshopVars.percent, tokenizer, "Expecting percent sign");
    }

    @Test(expected = IOException.class)
    public void testStringReaderIOException() {
        StringReader stringReader = new StringReader("");
        stringReader.read();
    }
}