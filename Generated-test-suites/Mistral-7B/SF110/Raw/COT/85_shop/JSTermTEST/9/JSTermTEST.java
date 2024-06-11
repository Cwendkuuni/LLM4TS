package umd.cs.shop;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JSTermTest {

    @Test
    public void testJSTermConstructor_Empty() {
        JSTerm jsterm = new JSTerm();
        // Add assertions for empty JSTerm instance here
    }

    @Test
    public void testJSTermConstructor_StreamTokenizer() throws IOException {
        String input = "(% term1 + term2)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsterm = new JSTerm(tokenizer);

        // Add assertions for term constructed from the input here

        tokenizer.nextToken(); // Consume the remaining ')' token
    }

    @Test
    public void testParseList_EmptyList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(nil)"));
        JSTerm jsterm = new JSTerm();
        JSTerm list = jsterm.parseList(tokenizer);

        // Add assertions for parsed empty list here
    }

    @Test
    public void testParseList_ListWithOneElement() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term1)"));
        JSTerm jsterm = new JSTerm();
        JSTerm list = jsterm.parseList(tokenizer);

        // Add assertions for parsed list with one element here
    }

    @Test(expected = JSParserError.class)
    public void testParseList_InvalidInput() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(term1 + term2)")); // List should not contain '+'
        JSTerm jsterm = new JSTerm();
        jsterm.parseList(tokenizer);
    }

    @Test
    public void testPrintList() {
        JSTerm jsterm = new JSTerm();
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        JSTerm term2 = new JSTerm();
        term2.addElement("term2");
        jsterm.addElement(term1);
        jsterm.addElement(term2);

        String expected = "(term1 term2)";
        String actual = jsterm.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsVariable() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("variable");
        boolean isVariable = jsterm.isVariable();

        Assert.assertTrue(isVariable);
    }

    @Test
    public void testIsConstant() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("constant");
        boolean isConstant = jsterm.isConstant();

        Assert.assertTrue(isConstant);
    }

    @Test
    public void testIsFunction() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("function");
        boolean isFunction = jsterm.isFunction();

        Assert.assertTrue(isFunction);
    }

    @Test
    public void testIsEval() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeEval(true);
        boolean isEval = jsterm.isEval();

        Assert.assertTrue(isEval);
    }

    @Test
    public void testMakeFunction() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeFunction();
        boolean isVariable = jsterm.isVariable();
        boolean isConstant = jsterm.isConstant();
        boolean isFunction = jsterm.isFunction();

        Assert.assertFalse(isVariable);
        Assert.assertFalse(isConstant);
        Assert.assertTrue(isFunction);
    }

    @Test
    public void testMakeVariable() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeVariable();
        boolean isVariable = jsterm.isVariable();
        boolean isConstant = jsterm.isConstant();
        boolean isFunction = jsterm.isFunction();

        Assert.assertTrue(isVariable);
        Assert.assertFalse(isConstant);
        Assert.assertFalse(isFunction);
    }

    @Test
    public void testMakeConstant() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeConstant();
        boolean isVariable = jsterm.isVariable();
        boolean isConstant = jsterm.isConstant();
        boolean isFunction = jsterm.isFunction();

        Assert.assertFalse(isVariable);
        Assert.assertTrue(isConstant);
        Assert.assertFalse(isFunction);
    }

    @Test
    public void testMakeEval() {
        JSTerm jsterm = new JSTerm();
        jsterm.makeEval(true);
        boolean isVariable = jsterm.isVariable();
        boolean isConstant = jsterm.isConstant();
        boolean isFunction = jsterm.isFunction();
        boolean isEval = jsterm.isEval();

        Assert.assertFalse(isVariable);
        Assert.assertFalse(isConstant);
        Assert.assertFalse(isFunction);
        Assert.assertTrue(isEval);
    }

    @Test
    public void testToString() {
        JSTerm jsterm = new JSTerm();
        jsterm.addElement("function");
        jsterm.addElement("term1");
        jsterm.addElement("term2");

        String expected = "function term1 term2";
        String actual = jsterm.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("term1");
        term1.makeConstant();

        JSTerm term2 = new JSTerm();
        term2.addElement("term1");
        term2.makeConstant();

        JSTerm term3 = new JSTerm();
        term3.addElement("term2");
        term3.makeConstant();

        JSTerm term4 = new JSTerm();
        term4.addElement("term3");
        term4.makeConstant();

        List<JSTerm> terms = new ArrayList<>();
        terms.add(term1);
        terms.add(term2);

        for (JSTerm t : terms) {
            for (JSTerm u : terms) {
                boolean equals = t.equals(u);
                if (t == u) {
                    Assert.assertTrue(equals);
                } else {
                    Assert.assertFalse(equals);
                }
            }
        }

        JSTerm term5 = new JSTerm();
        term5.addElement("term1");
        term5.makeVariable();

        Assert.assertFalse(term1.equals(term5));
        Assert.assertFalse(term5.equals(term1));
    }
}