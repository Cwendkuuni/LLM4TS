package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testJSTerm() {
        // Basic functionality test
        JSTerm term = new JSTerm();
        assertTrue(term.isEmpty());
        term.addElement("1");
        assertFalse(term.isEmpty());
        assertEquals("1", term.elementAt(0));

        // Test constructor with StreamTokenizer
        StringReader input = new StringReader("(+ 1 2)");
        JSTerm termFromInput = new JSTerm(new StreamTokenizer(input));
        assertEquals("+(1 2)", termFromInput.toStr().toString());
        assertEquals(2, termFromInput.size());

        // Test parseList method
        JSTerm listTerm = new JSTerm();
        listTerm.addElement("1");
        listTerm.addElement("2");
        listTerm.addElement("3");
        JSTerm parsedList = listTerm.parseList(new StreamTokenizer(new StringReader("(list 1 2 3)")));
        assertEquals(listTerm, parsedList);

        // Edge cases and exception handling tests

        // Test empty term
        JSTerm emptyTerm = new JSTerm();
        try {
            emptyTerm.parseList(new StreamTokenizer(new StringReader("(list)")));
            fail("Should throw JSParserError");
        } catch (JSParserError e) {
            // Expected
        }

        // Test term with invalid operator
        JSTerm invalidTerm = new JSTerm();
        try {
            invalidTerm.parseList(new StreamTokenizer(new StringReader("(+ 1 ^ 2)")));
            fail("Should throw JSParserError");
        } catch (JSParserError e) {
            // Expected
        }

        // Test term with mismatched parentheses
        try {
            new JSTerm().parseList(new StreamTokenizer(new StringReader("(+ 1)")));
            fail("Should throw JSParserError");
        } catch (JSParserError e) {
            // Expected
        }

        // Test term with variables and constants
        JSTerm variableTerm = new JSTerm();
        variableTerm.addElement("x");
        variableTerm.makeVariable();
        JSTerm constantTerm = new JSTerm();
        constantTerm.addElement("5");
        constantTerm.makeConstant();
        JSTerm variableConstantTerm = new JSTerm();
        variableConstantTerm.addElement("y");
        variableConstantTerm.makeVariable();
        variableConstantTerm.addElement("5");
        variableConstantTerm.makeConstant();

        JSTerm validTerm = new JSTerm();
        validTerm.addElement("(+ x 5)");
        JSTerm validVariableConstantTerm = new JSTerm();
        validVariableConstantTerm.addElement("(+ y 5)");

        assertTrue(variableTerm.equals(validTerm.cloneT()));
        assertTrue(constantTerm.equals(validTerm.cloneT()));
        assertTrue(variableConstantTerm.equals(validTerm.cloneT()));

        JSTerm clonedTerm = variableTerm.cloneT();
        assertNotSame(variableTerm, clonedTerm);
        assertTrue(variableTerm.equals(clonedTerm));

        JSTerm substitutedTerm = variableTerm.applySubstitutionT(new JSSubstitution());
        assertFalse(variableTerm.equals(substitutedTerm));

        JSTerm matchTerm = new JSTerm();
        matchTerm.addElement("x");
        matchTerm.makeVariable();
        JSTerm matchSubstitution = new JSTerm();
        matchSubstitution.addElement("y");
        matchSubstitution.makeVariable();
        JSSubstitution match = termFromInput.matches(matchTerm);
        assertTrue(match.isEmpty());

        match = termFromInput.matches(matchSubstitution);
        JSPairVarTerm pair = (JSPairVarTerm) match.elementAt(0);
        assertEquals("x", pair.getFirstTerm().elementAt(0));
        assertEquals("y", pair.getSecondTerm().elementAt(0));
    }

    @Test
    public void testJSTerm_isGround() {
        JSTerm term = new JSTerm();
        assertTrue(term.isGround());

        term.addElement("x");
        term.makeVariable();
        assertFalse(term.isGround());

        term.addElement("1");
        term.makeConstant();
        term.addElement("x");
        term.makeVariable();
        assertFalse(term.isGround());

        JSTerm groundTerm = new JSTerm();
        groundTerm.addElement("1");
        groundTerm.makeConstant();
        groundTerm.addElement("1");
        groundTerm.makeConstant();
        groundTerm.addElement("1");
        groundTerm.makeConstant();
        assertTrue(groundTerm.isGround());
    }

    @Test
    public void testJSTerm_standardizerTerm() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        term.makeVariable();
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("?X", standardizedTerm.elementAt(0));
        assertTrue(standardizedTerm.isVariable());

        JSTerm termWithFunction = new JSTerm();
        termWithFunction.makeFunction();
        termWithFunction.addElement("x");
        termWithFunction.makeVariable();
        termWithFunction.addElement("y");
        termWithFunction.makeVariable();
        JSTerm standardizedTermWithFunction = termWithFunction.standardizerTerm();
        assertEquals("(fun? (?X) (?Y))", standardizedTermWithFunction.toStr().toString());

        JSTerm termWithList = new JSTerm();
        termWithList.makeFunction();
        termWithList.addElement("list");
        termWithList.addElement("1");
        termWithList.addElement("2");
        termWithList.addElement("3");
        JSTerm standardizedTermWithList = termWithList.standardizerTerm();
        assertEquals("(fun? list (?X) (?Y) (?Z))", standardizedTermWithList.toStr().toString());
    }

    @Test
    public void testJSTerm_call() {
        JSTerm term = new JSTerm();
        term.addElement("(+ 1 2)");
        JSTerm result = term.call();
        assertEquals("3", result.elementAt(0));
        assertEquals(1, result.size());

        term = new JSTerm();
        term.addElement("(+ x 2)");
        term.makeVariable();
        term.addElement("x");
        term.makeVariable();
        term.addElement("3");
        term.makeConstant();
        result = term.call();
        assertEquals("5", result.elementAt(0));
        assertEquals(1, result.size());

        term = new JSTerm();
        term.addElement("(not 1)");
        result = term.call();
        assertEquals("0", result.elementAt(0));
        assertEquals(1, result.size());

        term = new JSTerm();
        term.addElement("(not x)");
        term.makeVariable();
        term.addElement("false");
        term.makeConstant();
        result = term.call();
        assertEquals("true", result.elementAt(0));
        assertEquals(1, result.size());

        term = new JSTerm();
        term.addElement("(+ x (+ x 1))");
        term.makeVariable();
        term.addElement("x");
        term.makeVariable();
        result = term.call();
        assertEquals("2 * x", result.toStr().toString());
    }
}