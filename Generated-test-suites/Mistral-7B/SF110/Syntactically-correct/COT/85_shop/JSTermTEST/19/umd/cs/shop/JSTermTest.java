package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm jsterm = new JSTerm();
        assertTrue(jsterm.isEmpty());
    }

    @Test
    public void testVariableTermConstruction() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertEquals("x", jsterm.elementAt(0));
        assertTrue(jsterm.isVariable());
        assertFalse(jsterm.isConstant());
        assertFalse(jsterm.isFunction());
    }

    @Test
    public void testConstantTermConstruction() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("5")));
        assertEquals("5", jsterm.elementAt(0));
        assertTrue(jsterm.isConstant());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isFunction());
    }

    @Test
    public void testFunctionTermConstruction() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 2 3)")));
        assertEquals("+(2, 3)", jsterm.toStr().toString());
        assertTrue(jsterm.isFunction());
        assertFalse(jsterm.isVariable());
        assertFalse(jsterm.isConstant());

        ArrayList<JSTerm> expectedArgs = new ArrayList<>();
        expectedArgs.add(new JSTerm(new StreamTokenizer(new StringReader("2"))));
        expectedArgs.add(new JSTerm(new StreamTokenizer(new StringReader("3"))));

        assertEquals(expectedArgs, jsterm.arguments());
    }

    @Test
    public void testEvalTermConstruction() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(call f x)")));
        assertTrue(jsterm.isEval());
        assertTrue(jsterm.isFunction());
        assertTrue(jsterm.arguments().size() == 2);
        assertEquals("f", jsterm.arguments().get(0).elementAt(0));
        assertEquals("x", jsterm.arguments().get(1).elementAt(0));
    }

    @Test(expected = JSParserError.class)
    public void testParseTermInvalidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("xxx"));
        new JSTerm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testParseTermMissingLeftParenthesis() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("5 +"));
        new JSTerm(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testParseTermMissingTermAfterOperator() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("5 +"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testParseTermList() {
        String input = "((list 1 2 3) (call list nil))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("((1 . 2) . (3 . nil))", expected.toStr().toString());
    }

    @Test
    public void testParseTermCallFunctionWithArguments() {
        String input = "(call + 3 4)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("+(3, 4)", expected.toStr().toString());
    }

    @Test
    public void testParseTermCallFunctionWithOneArgument() {
        String input = "(call sin)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("sin", expected.elementAt(0));
        assertTrue(expected.isFunction());
    }

    @Test
    public void testParseTermCallFunctionWithNoArguments() {
        String input = "(call nil)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("nil", expected.toStr().toString());
    }

    @Test
    public void testParseTermCallFunctionWithVariableArgument() {
        String input = "(call f x)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("call", expected.elementAt(0));
        assertEquals("f", expected.arguments().get(0).elementAt(0));
        assertEquals("x", expected.arguments().get(1).elementAt(0));
    }

    @Test
    public void testParseTermCallFunctionWithVariableAndConstantArguments() {
        String input = "(call f 5 x)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("call", expected.elementAt(0));
        assertEquals("f", expected.arguments().get(0).elementAt(0));
        assertEquals("5", expected.arguments().get(1).elementAt(0));
        assertEquals("x", expected.arguments().get(2).elementAt(0));
    }

    @Test
    public void testParseTermWithVariableAndConstantTerms() {
        String input = "(+(x 5))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("+(x, 5)", expected.toStr().toString());
        assertEquals("x", expected.arguments().get(0).elementAt(0));
        assertEquals("5", expected.arguments().get(1).elementAt(0));
    }

    @Test
    public void testParseTermWithMultipleOperators() {
        String input = "(+ (* 2 3) 4)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("+(*)((2, 3), 4)", expected.toStr().toString());
        assertEquals("*", expected.arguments().get(0).elementAt(0));
        assertEquals("2", expected.arguments().get(0).arguments().get(0).elementAt(0));
        assertEquals("3", expected.arguments().get(0).arguments().get(1).elementAt(0));
        assertEquals("+", expected.arguments().get(1).elementAt(0));
        assertEquals("*", expected.arguments().get(1).arguments().get(0).elementAt(0));
        assertEquals("2", expected.arguments().get(1).arguments().get(0).arguments().get(0).elementAt(0));
        assertEquals("3", expected.arguments().get(1).arguments().get(0).arguments().get(1).elementAt(0));
        assertEquals("4", expected.arguments().get(1).arguments().get(1).elementAt(0));
    }

    @Test
    public void testParseTermWithNestedLists() {
        String input = "(+(list 1 2) (list 3 4))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("+(1 . 2) ((3 . 4) . nil)", expected.toStr().toString());
    }

    @Test
    public void testParseTermWithMultipleNestedLists() {
        String input = "(+(list (+ 1 2) (list 3 4)) (list 5 6))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        assertEquals("+(1 . (2 . (3 . 4) . nil)) ((5 . 6) . nil)", expected.toStr().toString());
    }

    @Test
    public void testStandardizerTerm() {
        String input = "(+(x 5) (+ x 3))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm standardizedTerm = expected.standardizerTerm();
        assertEquals("+(x, (1 . (1 . nil))) (+ x, (2 . nil)))", standardizedTerm.toStr().toString());
    }

    @Test
    public void testCallTerm() {
        String input = "(+(x 5) (+ x 3))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm result = expected.call();
        assertEquals("12", result.toStr().toString());
    }

    @Test
    public void testCallTermWithVariableArgument() {
        String input = "(+(f x 5) (+ x 3))";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm result = expected.call();
        assertEquals("(+ (f x) (5)) (+ x (3))", result.toStr().toString());
    }

    @Test
    public void testCallTermWithEmptyFunctionArgument() {
        String input = "(call nil nil)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm result = expected.call();
        assertEquals("nil", result.toStr().toString());
    }

    @Test
    public void testCallTermWithUnaryOperator() {
        String input = "(call not 5)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm result = expected.call();
        assertEquals("0", result.toStr().toString());
    }

    @Test
    public void testCallTermWithBinaryOperator() {
        String input = "(call + 2 3)";
        JSTerm expected = new JSTerm(new StreamTokenizer(new StringReader(input)));
        JSTerm result = expected.call();
        assertEquals("5", result.toStr().toString());
    }

    @Test
    public void testMatchesTermsWithIdenticalTerms() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x + 3)")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(x + 3)")));
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesTermsWithDifferentTerms() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x + 3)")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(y + 3)")));
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesTermsWithIdenticalStructureButDifferentValues() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(+(x 3) (y 4))")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+(5 3) (y 4))")));
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatchesTermsWithIdenticalStructureButDifferentVariables() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(+(x 3) (y 4))")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+(x1 3) (z 4))")));
        JSSubstitution substitution = term1.matches(term2);
        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSPairVarTerm("x", "x1"));
        assertEquals(expected, substitution);
    }

    @Test
    public void testMatchesTermsWithIdenticalStructureButDifferentFunctions() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(+(sin x 3) (cos x 4))")));
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+(cos x 3) (sin x 4))")));
        JSSubstitution substitution = term1.matches(term2);
        JSSubstitution expected = new JSSubstitution();
        expected.addElement(new JSPairVarTerm("sin", "cos"));
        expected.addElement(new JSPairVarTerm("cos", "sin"));
        assertEquals(expected, substitution);
    }
}