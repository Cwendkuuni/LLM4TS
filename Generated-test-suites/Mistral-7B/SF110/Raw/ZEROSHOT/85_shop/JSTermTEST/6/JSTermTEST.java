import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class JSTermTest {

    @SuppressWarnings("unchecked")
    private List<Object> createList(Object... elements) {
        List<Object> list = new LinkedList<>();
        for (Object element : elements) {
            list.add(element);
        }
        return list;
    }

    @Test
    public void testEmptyTerm() {
        JSTerm term = new JSTerm();
        assertTrue(term.isConstant());
        assertEquals(new StringBuffer(""), term.toStr());
    }

    @Test
    public void testVariableTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x")));
        assertTrue(term.isVariable());
        assertEquals("x", term.elementAt(0));
        assertEquals(new StringBuffer("x "), term.toStr());
    }

    @Test
    public void testConstantTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("3")));
        assertTrue(term.isConstant());
        assertEquals("3", term.elementAt(0));
        assertEquals(new StringBuffer("3"), term.toStr());
    }

    @Test(expected = JSParserError.class)
    public void testMissingTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1"));
        JSTerm term = new JSTerm(tokenizer);
    }

    @Test
    public void testListTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(* (+ 1 2) 3)"));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals("*", term.elementAt(0));
        List<JSTerm> args = (List<JSTerm>) term.elementAt(1);
        assertEquals(3, args.size());
        JSTerm firstArg = args.get(0);
        assertTrue(firstArg.isFunction());
        assertEquals("+", firstArg.elementAt(0));
        List<JSTerm> secondArg = (List<JSTerm>) firstArg.elementAt(1);
        assertEquals(2, secondArg.size());
        JSTerm secondArgFirst = secondArg.get(0);
        assertTrue(secondArgFirst.isConstant());
        assertEquals("1", secondArgFirst.elementAt(0));
        JSTerm secondArgSecond = secondArg.get(1);
        assertTrue(secondArgSecond.isConstant());
        assertEquals("2", secondArgSecond.elementAt(0));
        JSTerm thirdArg = args.get(2);
        assertTrue(thirdArg.isConstant());
        assertEquals("3", thirdArg.elementAt(0));
    }

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 (list 2 3))"));
        JSTerm term = new JSTerm(tokenizer);
        assertTrue(term.isFunction());
        assertEquals("+", term.elementAt(0));
        List<JSTerm> args = (List<JSTerm>) term.elementAt(1);
        assertEquals(2, args.size());
        JSTerm firstArg = args.get(0);
        assertTrue(firstArg.isFunction());
        assertEquals("list", firstArg.elementAt(0));
        List<JSTerm> secondArg = (List<JSTerm>) firstArg.elementAt(1);
        assertEquals(2, secondArg.size());
        JSTerm secondArgFirst = secondArg.get(0);
        assertTrue(secondArgFirst.isConstant());
        assertEquals("2", secondArgFirst.elementAt(0));
        JSTerm secondArgSecond = secondArg.get(1);
        assertTrue(secondArgSecond.isConstant());
        assertEquals("3", secondArgSecond.elementAt(0));
    }

    @Test
    public void testApplySubstitution() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y"))).applySubstitution(new JSSubstitution());
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", term2));
        JSTerm term3 = term1.applySubstitution(alpha);
        assertEquals("y", term3.elementAt(0));

        term1 = new JSTerm(new StreamTokenizer(new StringReader("f(x)"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("g(y)"))).applySubstitution(new JSSubstitution());
        alpha = new JSSubstitution();
        alpha.addElement(new JSPair("x", term2));
        alpha.addElement(new JSPair("f", term1));
        JSTerm term4 = term1.applySubstitution(alpha);
        List<JSTerm> args = (List<JSTerm>) term4.elementAt(1);
        assertEquals(1, args.size());
        JSTerm arg = args.get(0);
        assertEquals("g(y)", arg.toStr().toString());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
        JSSubstitution beta = term1.matches(term2);
        assertTrue(beta.isEmpty());

        term1 = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("x")));
        JSSubstitution gamma = new JSSubstitution();
        gamma.addElement(new JSPair("x", term1));
        JSTerm term3 = term1.applySubstitution(gamma);
        beta = term3.matches(term2);
        assertTrue(beta.isEmpty());

        term1 = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
        gamma = new JSSubstitution();
        gamma.addElement(new JSPair("x", term2));
        term3 = term2.applySubstitution(gamma);
        beta = term1.matches(term3);
        assertTrue(beta.isEmpty());

        term1 = new JSTerm(new StreamTokenizer(new StringReader("f(x)"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("g(y)")));
        JSSubstitution delta = new JSSubstitution();
        delta.addElement(new JSPair("x", term2));
        delta.addElement(new JSPair("f", term1));
        term3 = term1.applySubstitution(delta);
        beta = term3.matches(term2);
        assertTrue(beta.isEmpty());

        term1 = new JSTerm(new StreamTokenizer(new StringReader("f(x)"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("g(x)")));
        beta = term1.matches(term2);
        assertFalse(beta.isEmpty());
        assertEquals("x", beta.elementAt(0).elementAt(0));
        assertEquals(term2, beta.elementAt(0).elementAt(1));
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x = 3"))).applySubstitution(new JSSubstitution());
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y = 3"))).applySubstitution(new JSSubstitution());
        assertFalse(term1.equals(term2));

        term1 = new JSTerm(new StreamTokenizer(new StringReader("x = x"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("x = x")));
        assertTrue(term1.equals(term2));

        term1 = new JSTerm(new StreamTokenizer(new StringReader("x = (y + z)"))).applySubstitution(new JSSubstitution());
        term2 = new JSTerm(new StreamTokenizer(new StringReader("x = (y + z)")));
        assertTrue(term1.equals(term2));
    }

    @Test
    public void testIsVariable() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("3"))).applySubstitution(new JSSubstitution());
        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("+"))).applySubstitution(new JSSubstitution());
        assertTrue(term.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).applySubstitution(new JSSubstitution());
        assertFalse(term.isEval());

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).makeEval(true).applySubstitution(new JSSubstitution());
        assertTrue(term.isEval());
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        String result = term.print().toString();
        assertEquals("x ", result);

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).applySubstitution(new JSSubstitution());
        result = term.print().toString();
        assertEquals("(+ x 3)", result);
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        JSTerm clonedTerm = term.cloneT();
        assertEquals(term, clonedTerm);

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).applySubstitution(new JSSubstitution());
        clonedTerm = term.cloneT();
        assertEquals(term, clonedTerm);
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))).applySubstitution(new JSSubstitution());
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.elementAt(0).toString());
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.toStr().toString());

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).applySubstitution(new JSSubstitution());
        standardizedTerm = term.standardizerTerm();
        assertEquals("+(x" + String.valueOf(JSJshopVars.VarCounter) + ", 3)", standardizedTerm.toStr().toString());
    }

    @Test
    public void testCall() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)"))).applySubstitution(new JSSubstitution());
        JSTerm result = term.call();
        assertEquals(new JSTerm(new StreamTokenizer(new StringReader("+(x, 3)"))).applySubstitution(new JSSubstitution()), result);

        term = new JSTerm(new StreamTokenizer(new StringReader("(* (+ x 3) 4)"))).applySubstitution(new JSSubstitution());
        result = term.call();
        assertEquals(new JSTerm(new StreamTokenizer(new StringReader("*(+(x, 3), 4)"))).applySubstitution(new JSSubstitution()), result);

        term = new JSTerm(new StreamTokenizer(new StringReader("not (= x 3)"))).applySubstitution(new JSSubstitution());
        result = term.call();
        assertEquals(new JSTerm(new StreamTokenizer(new StringReader("not (= x 3)"))).applySubstitution(new JSSubstitution()).makeEval(true), result);

        term = new JSTerm(new StreamTokenizer(new StringReader("not (= x 3)"))).applySubstitution(new JSSubstitution());
        assertTrue(term.isEval());
        result = term.call();
        assertEquals(new Boolean(false), result.elementAt(0));
    }
}