import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSTermTest {
    @Test
    public void testJSTermConstructor_Empty() {
        JSTerm jsterm = new JSTerm();
        assertNull(jsterm.elementAt(0));
        assertFalse(jsterm.isVariable);
        assertFalse(jsterm.isConstant);
        assertFalse(jsterm.isFunction);
        assertFalse(jsterm.isEval);
        assertEquals(0, jsterm.size());
    }

    @Test
    public void testJSTermConstructor_Variable() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(var a)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertEquals("a", jsterm.elementAt(0));
        assertTrue(jsterm.isVariable);
        assertFalse(jsterm.isConstant);
        assertFalse(jsterm.isFunction);
        assertFalse(jsterm.isEval);
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testJSTermConstructor_Constant() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(42)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertEquals("42", jsterm.elementAt(0));
        assertFalse(jsterm.isVariable);
        assertTrue(jsterm.isConstant);
        assertFalse(jsterm.isFunction);
        assertFalse(jsterm.isEval);
        assertEquals(1, jsterm.size());
    }

    @Test
    public void testJSTermConstructor_Function() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertEquals("+", jsterm.elementAt(0));
        assertTrue(jsterm.isFunction);
        assertFalse(jsterm.isVariable);
        assertFalse(jsterm.isConstant);
        assertFalse(jsterm.isEval);
        assertEquals(3, jsterm.size());
    }

    @Test
    public void testJSTermConstructor_FunctionWithArgs() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("((+ 3 4) 5)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertEquals("+", jsterm.elementAt(0));
        assertTrue(jsterm.isFunction);
        assertFalse(jsterm.isVariable);
        assertFalse(jsterm.isConstant);
        assertFalse(jsterm.isEval);
        assertEquals(4, jsterm.size());
    }

    @Test
    public void testParseList_EmptyList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSTerm jsterm = new JSTerm(tokenizer);
        JSTerm result = jsterm.parseList(tokenizer);
        assertNull(result.elementAt(0));
        assertTrue(result.isVariable);
        assertFalse(result.isConstant);
        assertFalse(result.isFunction);
        assertFalse(result.isEval);
        assertEquals(0, result.size());
    }

    @Test
    public void testParseList_ListWithOneElement() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        JSTerm result = jsterm.parseList(tokenizer);
        assertEquals("a", result.elementAt(0));
        assertTrue(result.isVariable);
        assertFalse(result.isConstant);
        assertFalse(result.isFunction);
        assertFalse(result.isEval);
        assertEquals(1, result.size());
    }

    @Test
    public void testParseList_ListWithMultipleElements() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a b c)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        JSTerm result = jsterm.parseList(tokenizer);
        assertEquals("a", result.elementAt(0).elementAt(0));
        assertEquals("b", result.elementAt(1).elementAt(0));
        assertEquals("c", result.elementAt(2).elementAt(0));
        assertTrue(result.isVariable);
        assertFalse(result.isConstant);
        assertFalse(result.isFunction);
        assertFalse(result.isEval);
        assertEquals(3, result.size());
    }

    @Test(expected = JSParserError.class)
    public void testParseList_Error() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(42) a"));
        JSTerm jsterm = new JSTerm(tokenizer);
        jsterm.parseList(tokenizer);
    }

    @Test
    public void testCloneT() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a 42)"));
        JSTerm original = new JSTerm(tokenizer);
        JSTerm clone = original.cloneT();
        assertEquals(original.toString(), clone.toString());
    }

    @Test
    public void testMatches_SameTerms() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term1 = new JSTerm(tokenizer);
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term2 = new JSTerm(tokenizer2);
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testMatches_DifferentTermsWithSubstitution() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term1 = new JSTerm(tokenizer);
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(+ x y)"));
        JSTerm term2 = new JSTerm(tokenizer2);
        JSSubstitution substitution = term1.matches(term2);
        assertEquals(1, substitution.size());
        JSPairVarTerm pair = (JSPairVarTerm) substitution.elementAt(0);
        assertEquals("a", pair.getFirstTerm().elementAt(0));
        assertEquals("x", pair.getSecondTerm().elementAt(0));
    }

    @Test
    public void testEquals_SameTerms() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term1 = new JSTerm(tokenizer);
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term2 = new JSTerm(tokenizer2);
        assertTrue(term1.equals(term2));
    }

    @Test
    public void testEquals_DifferentTerms() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term1 = new JSTerm(tokenizer);
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(* a b)"));
        JSTerm term2 = new JSTerm(tokenizer2);
        assertFalse(term1.equals(term2));
    }

    @Test
    public void testToString() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("(+ a b)", term.toString());
    }

    @Test
    public void testIsGround() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term = new JSTerm(tokenizer);
        assertFalse(term.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(+ a a)"));
        term = new JSTerm(tokenizer);
        assertTrue(term.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(+ a 42)"));
        term = new JSTerm(tokenizer);
        assertTrue(term.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm standardized = term.standardizerTerm();
        assertEquals("(+ ?a ?b)", standardized.toString());

        tokenizer = new StreamTokenizer(new StringReader("(+ a a)"));
        term = new JSTerm(tokenizer);
        standardized = term.standardizerTerm();
        assertEquals("(+ ?a ?a)", standardized.toString());

        tokenizer = new StreamTokenizer(new StringReader("(+ 42 a)"));
        term = new JSTerm(tokenizer);
        standardized = term.standardizerTerm();
        assertEquals("(+ 42 ?a)", standardized.toString());
    }

    @Test
    public void testCall_Constant() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(42)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertEquals("42", result.toString());
    }

    @Test
    public void testCall_Variable() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertEquals("?a", result.toString());
    }

    @Test
    public void testCall_FunctionWithOneArgument() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a 42)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertEquals("(+ ?a 42)", result.toString());
    }

    @Test
    public void testCall_FunctionWithTwoArguments() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ a b)"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertEquals("(+ ?a ?b)", result.toString());

        List<JSTerm> args = Arrays.asList(new JSTerm(new StringReader("42")), new JSTerm(new StringReader("3")));
        JSTerm expected = new JSTerm(new StringReader("(+ ?a ?b)"));
        expected.addElement(args.get(0));
        expected.addElement(args.get(1));
        JSTerm actual = term.call(args);
        assertEquals(expected.toString(), actual.toString());
    }
}