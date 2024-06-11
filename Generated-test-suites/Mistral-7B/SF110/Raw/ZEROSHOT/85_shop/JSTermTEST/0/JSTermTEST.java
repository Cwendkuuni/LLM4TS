import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTermEmptyConstructor() {
        JSTerm term = new JSTerm();
        assertNull(term.elementAt(0));
        assertFalse(term.isVariable);
        assertFalse(term.isConstant);
        assertFalse(term.isFunction);
        assertFalse(term.isEval);
        assertEquals(0, term.size());
    }

    @Test
    public void testJSTermStreamTokenizerConstructor() {
        String input = "(% call list ( 1 2 3 ))";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JSTerm term = new JSTerm(inputStream);

        assertEquals("call", term.elementAt(0));
        assertEquals(3, term.size());
        assertTrue(term.isFunction);
        assertFalse(term.isVariable);
        assertFalse(term.isConstant);
        assertFalse(term.isEval);

        JSTerm firstChild = term.elementAt(1);
        assertEquals("list", firstChild.elementAt(0));
        assertEquals(3, firstChild.size());

        JSTerm secondChild = firstChild.elementAt(1);
        assertEquals(1, secondChild.size());
        assertEquals("1", secondChild.elementAt(0));
        assertTrue(secondChild.isConstant);

        JSTerm thirdChild = firstChild.elementAt(2);
        assertEquals(3, thirdChild.size());

        JSTerm fourthChild = thirdChild.elementAt(1);
        assertEquals("2", fourthChild.elementAt(0));
        assertTrue(fourthChild.isConstant);

        JSTerm fifthChild = thirdChild.elementAt(2);
        assertEquals("3", fifthChild.elementAt(0));
        assertTrue(fifthChild.isConstant);
    }

    @Test(expected = JSParserError.class)
    public void testJSTermStreamTokenizerConstructorError() {
        String input = "(% expect term)";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        new JSTerm(inputStream);
    }

    @Test
    public void testParseList() {
        String input = "(% list call ( 1 2 3 ))";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JSTerm term = new JSTerm(inputStream);

        JSTerm list = term.elementAt(1);
        assertEquals("list", list.elementAt(0));
        assertEquals(3, list.size());

        JSTerm firstChild = list.elementAt(1);
        assertEquals("call", firstChild.elementAt(0));
        assertEquals(1, firstChild.size());

        JSTerm callChild = firstChild.elementAt(0);
        assertEquals("call", callChild.elementAt(0));
        assertEquals(0, callChild.size());

        JSTerm secondChild = list.elementAt(2);
        assertEquals("3", secondChild.elementAt(1).elementAt(0));
        assertEquals(1, secondChild.size());
        assertTrue(secondChild.elementAt(1).isConstant);

        JSTerm thirdChild = list.elementAt(2);
        assertEquals("2", thirdChild.elementAt(1).elementAt(0));
        assertEquals(1, thirdChild.size());
        assertTrue(thirdChild.elementAt(1).isConstant);

        JSTerm firstChildOfCall = callChild.elementAt(0);
        assertEquals("call", firstChildOfCall.elementAt(0));
        assertEquals(1, firstChildOfCall.size());
    }

    @Test(expected = JSParserError.class)
    public void testParseListError() {
        String input = "(% list expect term )";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        new JSTerm(inputStream);
    }

    @Test
    public void testPrintList() {
        String input = "(% call list ( 1 2 3 ))";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JSTerm term = new JSTerm(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        term.print(outputStream);

        String expectedOutput = "call list 1 2 3 ) ";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCloneT() {
        String input = "(% call list ( 1 2 3 ))";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JSTerm term = new JSTerm(inputStream);

        JSTerm clonedTerm = term.cloneT();

        assertEquals(term.toString(), clonedTerm.toString());
    }

    @Test
    public void testApplySubstitutionT() {
        String input = "(% x ( y z ))";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        JSTerm term = new JSTerm(inputStream);

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm("x", new JSTerm("3")));
        JSTerm result = term.applySubstitutionT(substitution);

        assertEquals("(3 y z)", result.toString());
    }

    @Test
    public void testMatches() {
        String term1Input = "(% a ( b c ))";
        String term2Input = "(% x ( y z ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);
        JSTerm term2 = new JSTerm(term2Stream);

        JSSubstitution substitution = new JSSubstitution();
        JSTerm result = term1.matches(term2, substitution);

        assertNull(result);
    }

    @Test
    public void testMatchesVariable() {
        String term1Input = "(% x ( y z ))";
        String term2Input = "(% a ( b c ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);
        JSTerm term2 = new JSTerm(term2Stream);

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("x", "a"));
        JSTerm result = term1.matches(term2, substitution);

        assertEquals("(a b c)", result.toString());
    }

    @Test
    public void testMatchesFunction() {
        String term1Input = "(% add ( x y ))";
        String term2Input = "(% add ( a b ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);
        JSTerm term2 = new JSTerm(term2Stream);

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("add", "add"));
        substitution.addElement(new JSPairVarTerm("x", "a"));
        substitution.addElement(new JSPairVarTerm("y", "b"));
        JSTerm result = term1.matches(term2, substitution);

        assertEquals("(add a b)", result.toString());
    }

    @Test
    public void testMatchesRecursive() {
        String term1Input = "(% add ( x ( add x 1 )))";
        String term2Input = "(% add ( a ( add a 1 )))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);
        JSTerm term2 = new JSTerm(term2Stream);

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("add", "add"));
        substitution.addElement(new JSPairVarTerm("x", "a"));
        JSTerm result = term1.matches(term2, substitution);

        assertEquals("(add a (add a 1))", result.toString());
    }

    @Test
    public void testMatchesFail() {
        String term1Input = "(% add ( x y ))";
        String term2Input = "(% subtract ( x y ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);
        JSTerm term2 = new JSTerm(term2Stream);

        JSSubstitution substitution = new JSSubstitution();
        JSTerm result = term1.matches(term2, substitution);

        assertNull(result);
    }

    @Test
    public void testEqualsSameTerm() {
        String term1Input = "(% a ( b c ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);

        assertEquals(term1, term1);
    }

    @Test
    public void testEqualsDifferentTerms() {
        String term1Input = "(% a ( b c ))";
        ByteArrayInputStream term1Stream = new ByteArrayInputStream(term1Input.getBytes());
        JSTerm term1 = new JSTerm(term1Stream);

        String term2Input = "(% x ( y z ))";
        ByteArrayInputStream term2Stream = new ByteArrayInputStream(term2Input.getBytes());
        JSTerm term2 = new JSTerm(term2Stream);

        assertFalse(term1.equals(term2));
    }

    @Test
    public void testIsVariable() {
        String termInput = "(% x ( y z ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertTrue(term.isVariable());
    }

    @Test
    public void testIsConstant() {
        String termInput = "(% 3 ( 4 5 ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertTrue(term.isConstant());
    }

    @Test
    public void testIsFunction() {
        String termInput = "(% add ( x y ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertTrue(term.isFunction());
    }

    @Test
    public void testIsEval() {
        String termInput = "(% call add ( 1 2 ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertTrue(term.isEval());
    }

    @Test
    public void testToString() {
        String termInput = "(% call add ( 1 2 ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertEquals("call add 1 2 ", term.toString());
    }

    @Test
    public void testIsGround() {
        String termInput1 = "(% a ( b c ))";
        ByteArrayInputStream termStream1 = new ByteArrayInputStream(termInput1.getBytes());
        JSTerm term1 = new JSTerm(termStream1);

        String termInput2 = "(% 3 ( nil nil ))";
        ByteArrayInputStream termStream2 = new ByteArrayInputStream(termInput2.getBytes());
        JSTerm term2 = new JSTerm(termStream2);

        assertTrue(term2.isGround());
        assertFalse(term1.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        String termInput = "(% add ( x y ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        JSTerm standardizedTerm = term.standardizerTerm();

        assertEquals("add X1 X2 ", standardizedTerm.toString());
    }

    @Test
    public void testCall() {
        String termInput = "(% call add ( 1 2 ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        JSTerm result = term.call();

        assertEquals("3 ", result.toString());
    }

    @Test
    public void testCallFailure() {
        String termInput = "(% call add ( 1 2 3 ))";
        ByteArrayInputStream termStream = new ByteArrayInputStream(termInput.getBytes());
        JSTerm term = new JSTerm(termStream);

        assertNull(term.call());
    }
}