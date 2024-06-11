import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSTermTest {

    @Test
    public void testEmptyTerm() {
        JSTerm term = new JSTerm();
        assertTrue(term.isConstant());
        assertEquals("()", term.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testExpectedTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+ 1 2)".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip '('
        fail();
    }

    @Test
    public void testVariableTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("x".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isVariable());
        assertEquals("x", term.elementAt(0).toString());
        assertEquals("x", term.toStr().toString());
    }

    @Test
    public void testConstantTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("5".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isConstant());
        assertEquals("5", term.elementAt(0).toString());
        assertEquals("5", term.toStr().toString());
    }

    @Test
    public void testListTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+ 1 2)".getBytes()));
        tokenizer.nextToken(); // skip '('
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip '+'
        JSTerm firstTerm = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip ' '
        JSTerm secondTerm = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip ')'
        assertEquals(2, term.size());
        assertEquals(firstTerm, term.elementAt(0));
        assertEquals(secondTerm, term.elementAt(1));
        assertEquals("(+ 1 2)", term.toStr().toString());
    }

    @Test
    public void testListTermWithEmptyList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("()".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isConstant());
        assertEquals("nil", term.elementAt(0).toString());
        assertEquals("()", term.toStr().toString());
    }

    @Test
    public void testCallTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+ x 1)".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals("+", term.elementAt(0).toString());
        assertEquals(2, term.size());
        assertEquals(new JSTerm("x"), term.elementAt(1));
        assertEquals("1", term.elementAt(2).toStr().toString());
        assertEquals("(+ x 1)", term.toStr().toString());
    }

    @Test
    public void testApplySubstitution() {
        JSTerm x = new JSTerm("x");
        JSTerm y = new JSTerm("y");
        JSTerm term = new JSTerm("(+ x y)");
        JSSubstitution substitution = new JSSubstitution();
        substitution.assign(x, y);
        JSTerm expectedResult = new JSTerm("(+ y y)");
        JSTerm result = term.applySubstitution(substitution);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatches() {
        JSTerm x = new JSTerm("x");
        JSTerm y = new JSTerm("y");
        JSTerm term1 = new JSTerm("(+ x y)");
        JSTerm term2 = new JSTerm("(+ y x)");
        JSTerm result = term1.matches(term2);
        assertTrue(result.fail());

        term1 = new JSTerm("(+ x x)");
        result = term1.matches(term2);
        assertTrue(result.fail());

        term1 = new JSTerm("(+ x y)");
        term2 = new JSTerm("(+ y x)");
        substitution = new JSTerm("x", term2).matches(term1, new JSSubstitution());
        assertEquals(2, substitution.size());
        assertEquals(new JSPairVarTerm("x", y), substitution.elementAt(0));
        assertEquals(new JSPairVarTerm("y", x), substitution.elementAt(1));
    }

    @Test
    public void testEquals() {
        JSTerm x = new JSTerm("x");
        JSTerm y = new JSTerm("y");
        JSTerm term1 = new JSTerm("(+ x y)");
        JSTerm term2 = new JSTerm("(+ y x)");
        JSTerm term3 = new JSTerm("(+ x x)");
        JSTerm term4 = new JSTerm("(+ x y)");
        List<JSTerm> list1 = new ArrayList<>();
        list1.add(term1);
        list1.add(term4);
        List<JSTerm> list2 = new ArrayList<>();
        list2.add(term1);
        list2.add(term3);
        assertEquals(list1, term1.matches(term4).getAssignments());
        assertEquals(list2, term1.matches(term3).getAssignments());
        assertFalse(term1.equals(term2));
        assertTrue(term1.equals(term4));
        assertTrue(term1.equals(term1));
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm("(+ x (if (> x 1) 2 x))");
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals(3, standardizedTerm.size());
        assertEquals("if", standardizedTerm.elementAt(0).toString());
        JSTerm ifTerm1 = standardizedTerm.elementAt(1);
        assertEquals(">", ifTerm1.elementAt(0), standardizedTerm.elementAt(2));
        assertEquals(2, ifTerm1.size());
        JSTerm ifTerm2 = ifTerm1.elementAt(1);
        assertEquals("x", ifTerm2.elementAt(0));
        assertEquals("1", ifTerm2.elementAt(1).toString());
        assertEquals(2, ifTerm2.size());
        JSTerm ifTerm3 = ifTerm2.elementAt(1);
        assertEquals("x", ifTerm3.elementAt(0));
        assertEquals("2", ifTerm3.elementAt(1).toString());
    }

    @Test
    public void testCallTermWithEmptyTermList() {
        JSTerm term = new JSTerm("(+ x)");
        assertEquals(new JSTerm(), term.call());
    }

    @Test
    public void testCallTermWithUnaryOperator() {
        JSTerm term = new JSTerm("(not (> x 1))");
        JSTerm result = term.call();
        assertEquals("false", result.toStr().toString());
    }

    @Test
    public void testCallTermWithBinaryOperator() {
        JSTerm term = new JSTerm("(+ x 1)");
        JSTerm result = term.call();
        assertEquals("2", result.toStr().toString());
    }

    @Test
    public void testCallTermWithListOfTerms() {
        JSTerm term = new JSTerm("(* (+ x 1) (/ 2 3))");
        JSTerm result = term.call();
        assertEquals("(1.5)", result.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testCallTermWithInvalidCallSyntax() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+)".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip '+'
        fail();
    }

    @Test(expected = JSParserError.class)
    public void testCallTermWithInvalidFunctionName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(foo x)".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip 'f'
        tokenizer.nextToken(); // skip 'o'
        tokenizer.nextToken(); // skip 'o'
        fail();
    }

    @Test(expected = JSParserError.class)
    public void testCallTermWithMissingArgument() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+)".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip '+'
        tokenizer.nextToken(); // skip ')'
        fail();
    }

    @Test(expected = JSParserError.class)
    public void testCallTermWithInvalidTokenAfterFunctionName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new ByteArrayInputStream("(+ 1)%".getBytes()));
        JSTerm term = new JSTerm(tokenizer);
        tokenizer.nextToken(); // skip '+'
        fail();
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm("(+ x 1)");
        JSTerm clonedTerm = term.cloneT();
        assertEquals(term, clonedTerm);
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm("(+ x 1)");
        assertFalse(term.isGround());

        term = new JSTerm("5");
        term = new JSTerm("x");
        term = new JSTerm("nil");
        term = new JSTerm("()");
        assertTrue(term.isGround());
    }
}