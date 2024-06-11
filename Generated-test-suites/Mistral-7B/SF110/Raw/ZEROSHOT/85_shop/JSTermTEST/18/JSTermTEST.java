import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.LinkedList;

public class JSTermTest {

    @Test
    public void testJSTermEmptyConstructor() {
        JSTerm term = new JSTerm();
        assertTrue(term.isEmpty());
    }

    @Test
    public void testJSTermConstructorWithTokenizer() {
        String input = "(* + 1 2)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("(*", term.elementAt(0));
        assertEquals(3, term.size());
        assertEquals("+", term.elementAt(1).elementAt(0));
        assertEquals("1", term.elementAt(2).elementAt(0));
        assertEquals("2", term.elementAt(3).elementAt(0));
    }

    @Test(expected = JSParserError.class)
    public void testJSTermConstructorWithInvalidToken() {
        String input = "1 %%% 2";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        new JSTerm(tokenizer);
    }

    @Test
    public void testParseList() {
        String input = "((a b c) d e)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm term = new JSTerm(tokenizer);
        assertEquals("list", term.elementAt(0).toString());
        LinkedList<JSTerm> list = new LinkedList<>();
        list.add(new JSTerm("a"));
        list.add(new JSTerm("b"));
        list.add(new JSTerm("c"));
        list.add(new JSTerm("d"));
        list.add(new JSTerm("e"));
        for (int i = 1; i < term.size(); ++i) {
            JSTerm element = term.elementAt(i);
            assertEquals(".", element.elementAt(0));
            assertEquals(new JSTerm("nil"), element.elementAt(1));
            assertEquals(list.poll(), element.elementAt(2));
        }
    }

    @Test
    public void testPrintList() {
        JSTerm term = new JSTerm();
        term.addElement("a");
        term.addElement(".");
        term.addElement(new JSTerm("b"));
        term.addElement(".");
        term.addElement(new JSTerm("c"));
        term.addElement("nil");
        String expectedOutput = " . a . b . c ) ";
        assertEquals(expectedOutput, term.toStr().toString());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term1 = new JSTerm("a");
        JSTerm term2 = new JSTerm("b");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("a", term2));
        JSTerm result = term1.applySubstitutionT(substitution);
        assertEquals("b", result.elementAt(0));
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm("a");
        JSTerm term2 = new JSTerm("a");
        JSSubstitution substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("a");
        term2 = new JSTerm("b");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a)");
        term2 = new JSTerm("(f x)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a)");
        term2 = new JSTerm("(f b)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a)");
        term2 = new JSTerm("(g x)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a)");
        term2 = new JSTerm("(f a b)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(f a c)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(f a b c)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(g a b)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(h a c d)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(f a x)");
        JSPairVarTerm pair = (JSPairVarTerm) term1.matches(term2).elementAt(0);
        assertEquals("a", pair.getFirst());
        assertEquals("x", pair.getSecond());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(f a x y)");
        pair = (JSPairVarTerm) term1.matches(term2).elementAt(0);
        assertEquals("a", pair.getFirst());
        assertEquals("x", pair.getSecond());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(f x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b)");
        term2 = new JSTerm("(g x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(f x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(g x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(h x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(f a x y z)");
        pair = (JSPairVarTerm) term1.matches(term2).elementAt(0);
        assertEquals("a", pair.getFirst());
        assertEquals("x", pair.getSecond());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(f a x y z)");
        substitution = term1.matches(term2);
        assertEquals(3, substitution.size());
        assertEquals("b", substitution.elementAt(0).getSecond().elementAt(0));
        assertEquals("y", substitution.elementAt(1).getSecond().elementAt(0));
        assertEquals("z", substitution.elementAt(2).getSecond().elementAt(0));

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(g x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());

        term1 = new JSTerm("(f a b c)");
        term2 = new JSTerm("(h x y z)");
        substitution = term1.matches(term2);
        assertTrue(substitution.isEmpty());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term = new JSTerm("f a b c");
        JSTerm result = term.standardizerTerm();
        assertEquals("f" + Integer.toString(JSJshopVars.VarCounter++)+" a" + Integer.toString(JSJshopVars.VarCounter++)+" b" + Integer.toString(JSJshopVars.VarCounter++), result.toStr().toString());
    }

    @Test
    public void testCall() {
        JSTerm term = new JSTerm("(+ 1 2)");
        JSTerm result = term.call();
        assertEquals(3, result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("1", result.elementAt(1).elementAt(0));
        assertEquals("2", result.elementAt(2).elementAt(0));

        term = new JSTerm("(* 1 (+ 1 2))");
        result = term.call();
        assertEquals("3", result.elementAt(1).elementAt(0));

        term = new JSTerm("(*)");
        result = term.call();
        assertEquals("nil", result.toStr().toString());

        term = new JSTerm("(not (= 1 1))");
        result = term.call();
        assertEquals("nil", result.toStr().toString());

        term = new JSTerm("(not (= 1 2))");
        result = term.call();
        assertEquals("true", result.toStr().toString());

        term = new JSTerm("(not (< 1 2))");
        result = term.call();
        assertEquals("false", result.toStr().toString());

        term = new JSTerm("(not (< 2 1))");
        result = term.call();
        assertEquals("true", result.toStr().toString());

        term = new JSTerm("(< 1 2)");
        result = term.call();
        assertEquals("false", result.toStr().toString());

        term = new JSTerm("(< 2 1)");
        result = term.call();
        assertEquals("true", result.toStr().toString());

        term = new JSTerm("(+ 1 1)");
        result = term.call();
        assertEquals("2", result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("1", result.elementAt(1).elementAt(0));
        assertEquals("1", result.elementAt(2).elementAt(0));

        term = new JSTerm("(+ 1 (* 2 2))");
        result = term.call();
        assertEquals("5", result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("1", result.elementAt(1).elementAt(0));
        assertEquals("4", result.elementAt(2).elementAt(0));

        term = new JSTerm("(+ (* 2 2) (* 3 3))");
        result = term.call();
        assertEquals("10", result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("4", result.elementAt(1).elementAt(0));
        assertEquals("9", result.elementAt(2).elementAt(0));

        term = new JSTerm("(+ (* 2 2) (* 3 3) (* 4 4))");
        result = term.call();
        assertEquals("18", result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("4", result.elementAt(1).elementAt(0));
        assertEquals("9", result.elementAt(2).elementAt(0));
        assertEquals("13", result.elementAt(3).elementAt(0));

        term = new JSTerm("(+ (* 2 2) (* 3 3) (* 4 4) (* 5 5))");
        result = term.call();
        assertEquals("30", result.elementAt(0).equals("+") ? result.size() : result.elementAt(0).size());
        assertEquals("4", result.elementAt(1).elementAt(0));
        assertEquals("9", result.elementAt(2).elementAt(0));
        assertEquals("13", result.elementAt(3).elementAt(0));
        assertEquals("25", result.elementAt(4).elementAt(0));
    }

    @Test
    public void testIsGround() {
        JSTerm term = new JSTerm("a");
        assertTrue(term.isGround());

        term = new JSTerm("(+ a a)");
        assertTrue(term.isGround());

        term = new JSTerm("(+ a b)");
        assertFalse(term.isGround());

        term = new JSTerm("(f a b)");
        assertFalse(term.isGround());

        term = new JSTerm("(f a b c)");
        assertFalse(term.isGround());
    }
}