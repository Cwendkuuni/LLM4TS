import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSTerm term = new JSTerm(tokenizer);
        assertNull(term.elementAt(0));
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertNull(term.parseList(tokenizer));
    }

    @Test
    public void testJSTermVariableConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("x", term.elementAt(0));
        assertTrue(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertNull(term.parseList(tokenizer));
    }

    @Test
    public void testJSTermConstantConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("5"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("5", term.elementAt(0));
        assertFalse(term.isVariable());
        assertTrue(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertNull(term.parseList(tokenizer));
    }

    @Test
    public void testJSTermListConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 5)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("+", term.elementAt(0));
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        JSTerm first = term.elementAt(1);
        assertEquals("x", first.elementAt(0));
        assertTrue(first.isVariable());
        assertFalse(first.isConstant());
        assertFalse(first.isFunction());
        assertFalse(first.isEval());
        assertNull(first.parseList(tokenizer));

        JSTerm second = term.elementAt(2);
        assertEquals("5", second.elementAt(0));
        assertFalse(second.isVariable());
        assertTrue(second.isConstant());
        assertFalse(second.isFunction());
        assertFalse(second.isEval());
        assertNull(second.parseList(tokenizer));

        JSTerm list = term.parseList(tokenizer);
        List<JSTerm> expectedList = new ArrayList<>();
        expectedList.add(first);
        expectedList.add(second);
        assertEquals(expectedList.toString(), list.toString());
    }

    @Test
    public void testJSTermCallConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call x)"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("call", term.elementAt(0));
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertFalse(term.isEval());

        JSTerm first = term.elementAt(1);
        assertEquals("x", first.elementAt(0));
        assertTrue(first.isVariable());
        assertFalse(first.isConstant());
        assertFalse(first.isFunction());
        assertFalse(first.isEval());
        assertNull(first.parseList(tokenizer));
    }

    @Test
    public void testJSTermEvalConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(eval (+ x 5))"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("eval", term.elementAt(0));
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertTrue(term.isFunction());
        assertTrue(term.isEval());

        JSTerm first = term.elementAt(1);
        assertEquals("+", first.elementAt(0));
        assertFalse(first.isVariable());
        assertFalse(first.isConstant());
        assertTrue(first.isFunction());
        assertFalse(first.isEval());

        JSTerm firstElement = first.elementAt(1);
        assertEquals("x", firstElement.elementAt(0));
        assertTrue(firstElement.isVariable());
        assertFalse(firstElement.isConstant());
        assertFalse(firstElement.isFunction());
        assertFalse(firstElement.isEval());
        assertNull(firstElement.parseList(tokenizer));

        JSTerm secondElement = first.elementAt(2);
        assertEquals("5", secondElement.elementAt(0));
        assertFalse(secondElement.isVariable());
        assertTrue(secondElement.isConstant());
        assertFalse(secondElement.isFunction());
        assertFalse(secondElement.isEval());
        assertNull(secondElement.parseList(tokenizer));
    }

    @Test
    public void testJSTermToString() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(eval (+ x 5))"));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("(eval (+ x 5))", term.toString());
    }

    @Test
    public void testJSTermIsGround() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("5"));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isGround());

        tokenizer = new StreamTokenizer(new StringReader("x"));
        JSTerm termVariable = new JSTerm(tokenizer);
        assertFalse(termVariable.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(+ x 5)"));
        JSTerm termFunction = new JSTerm(tokenizer);
        assertFalse(termFunction.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(eval (+ x 5))"));
        JSTerm termEval = new JSTerm(tokenizer);
        assertFalse(termEval.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(eval (+ x 5))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("(eval (standardizerTerm x) (standardizerTerm 5))", standardizedTerm.toString());
    }

    @Test
    public void testJSTermCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(eval (+ x 5))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm result = term.call();
        assertEquals("5", result.elementAt(0).toString());
    }
}