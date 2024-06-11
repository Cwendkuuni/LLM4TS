import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import java.io.StringReader;

public class JSTermTest {

    @Test
    public void testJSTermConstructorWithNoArguments() {
        JSTerm jsterm = new JSTerm();
        assertFalse(jsterm.isVariable);
        assertFalse(jsterm.isConstant);
        assertFalse(jsterm.isFunction);
        assertFalse(jsterm.isEval);
    }

    @Test
    public void testJSTermConstructorWithStreamTokenizerValidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertTrue(jsterm.isFunction);
        assertEquals("+", jsterm.elementAt(0).toString());
        assertEquals(2, jsterm.size());
        JSTerm firstTerm = jsterm.elementAt(1);
        assertTrue(firstTerm.isNumber());
        assertEquals("1", firstTerm.toStr().toString());
        JSTerm secondTerm = jsterm.elementAt(2);
        assertTrue(secondTerm.isNumber());
        assertEquals("2", secondTerm.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testJSTermConstructorWithStreamTokenizerInvalidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2) a"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testParseListSimpleList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list 1 2)"));
        JSTerm list = new JSTerm(tokenizer).elementAt(1);
        assertEquals(2, list.size());
        JSTerm firstTerm = list.elementAt(0);
        assertTrue(firstTerm.isNumber());
        assertEquals("1", firstTerm.toStr().toString());
        JSTerm secondTerm = list.elementAt(1);
        assertTrue(secondTerm.isNumber());
        assertEquals("2", secondTerm.toStr().toString());
    }

    @Test
    public void testParseListListWithNestedLists() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list (list 1 2) (list 3 4))"));
        JSTerm list = new JSTerm(tokenizer).elementAt(1);
        assertEquals(3, list.size());
        JSTerm firstTerm = list.elementAt(0);
        assertTrue(firstTerm.isList());
        JSTerm firstList = firstTerm.elementAt(0);
        assertEquals(2, firstList.size());
        JSTerm firstListFirstTerm = firstList.elementAt(0);
        assertTrue(firstListFirstTerm.isNumber());
        assertEquals("1", firstListFirstTerm.toStr().toString());
        JSTerm firstListSecondTerm = firstList.elementAt(1);
        assertTrue(firstListSecondTerm.isNumber());
        assertEquals("2", firstListSecondTerm.toStr().toString());
        JSTerm secondTerm = list.elementAt(1);
        assertTrue(secondTerm.isList());
        JSTerm secondList = secondTerm.elementAt(0);
        assertEquals(2, secondList.size());
        JSTerm secondListFirstTerm = secondList.elementAt(0);
        assertTrue(secondListFirstTerm.isNumber());
        assertEquals("3", secondListFirstTerm.toStr().toString());
        JSTerm secondListSecondTerm = secondList.elementAt(1);
        assertTrue(secondListSecondTerm.isNumber());
        assertEquals("4", secondListSecondTerm.toStr().toString());
    }

    @Test(expected = JSParserError.class)
    public void testParseListInvalidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list 1 2 3)"));
        new JSTerm(tokenizer);
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("x");
        JSTerm term3 = new JSTerm("(+ 1 x)");
        JSTerm term4 = new JSTerm("(standardizerTerm term1)");
        JSTerm term5 = new JSTerm("(standardizerTerm term2)");
        JSTerm term6 = new JSTerm("(standardizerTerm term3)");
        assertEquals("1", term4.toStr().toString());
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), term5.toStr().toString());
        assertEquals("(+ 1 " + term5.toStr().toString() + ")", term6.toStr().toString());
    }

    @Test
    public void testCall() {
        JSTerm term1 = new JSTerm("1");
        JSTerm term2 = new JSTerm("x");
        JSTerm term3 = new JSTerm("(+ 1 x)");
        JSTerm term4 = new JSTerm("(call term3 term1)");
        JSTerm term5 = new JSTerm("(call term3 term2)");
        assertEquals("2", term4.toStr().toString());
        assertEquals("3", term5.toStr().toString());
    }

    @Test
    public void testCallEmptyTerm() {
        JSTerm term = new JSTerm();
        JSTerm result = term.call();
        assertEquals("", result.toStr().toString());
    }

    @Test
    public void testCallEmptyList() {
        JSTerm term = new JSTerm("(list)");
        JSTerm result = term.call();
        assertEquals("nil", result.toStr().toString());
    }
}