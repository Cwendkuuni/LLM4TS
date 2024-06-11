import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSTerm;

public class JSTermTest {

    @Test
    public void testJSTermEmptyConstructor() {
        JSTerm jsTerm = new JSTerm();
        assertTrue(jsTerm.isEmpty());
    }

    @Test
    public void testJSTermStreamTokenizerConstructor() {
        String input = "(* term-constructor *)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("term-constructor");
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        assertEquals(expected, jsTerm.getElements());
    }

    @Test(expected = JSParserError.class)
    public void testJSTermStreamTokenizerConstructorWithError() {
        String input = "(* term-constructor *) Expecting Term";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        new JSTerm(tokenizer);
    }

    @Test
    public void testJSTermVariableTermConstructor() {
        String input = "(* term-constructor *) x";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("x");
        expected.add(true);
        expected.add(false);
        expected.add(false);
        assertEquals(expected, jsTerm.getElements());
    }

    @Test
    public void testJSTermConstantTermConstructor() {
        String input = "(* term-constructor *) 5";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("5");
        expected.add(false);
        expected.add(true);
        expected.add(false);
        assertEquals(expected, jsTerm.getElements());
    }

    @Test
    public void testJSTermFunctionTermConstructor() {
        String input = "(* term-constructor *) (call term-constructor term-constructor)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("call");
        expected.add(true);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        assertEquals(expected, jsTerm.getElements());
    }

    @Test
    public void testJSTermFunctionTermConstructorWithEval() {
        String input = "(* term-constructor *) (call term-constructor term-constructor)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        tokenizer.pushBack();
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("call");
        expected.add(true);
        expected.add(true);
        expected.add(true);
        expected.add(false);
        assertEquals(expected, jsTerm.getElements());
    }

    @Test
    public void testJSTermListTermConstructor() {
        String input = "(* term-constructor *) (list term-constructor term-constructor term-constructor)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add("list");
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(new JSTerm());
        expected.add(new JSTerm());
        expected.add(new JSTerm());
        assertEquals(expected, jsTerm.getElements());
    }

    @Test
    public void testJSTermParseList() {
        String input = "(* term-constructor *) (list term-constructor term-constructor term-constructor)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm();
        JSTerm result = jsTerm.parseList(tokenizer);
        List<Object> expected = new ArrayList<>();
        expected.add(new JSTerm());
        expected.add(new JSTerm());
        expected.add(new JSTerm());
        assertEquals(expected, result.getElements());
    }

    @Test
    public void testJSTermPrint() {
        String input = "(* term-constructor *) (call term-constructor term-constructor)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSTerm jsTerm = new JSTerm(tokenizer);
        String expected = "call term-constructor term-constructor";
        assertEquals(expected, jsTerm.toString());
    }

    @Test
    public void testJSTermIsGround() {
        JSTerm jsTerm1 = new JSTerm();
        jsTerm1.addElement(new JSTerm("x"));
        jsTerm1.addElement(new JSTerm("5"));
        JSTerm jsTerm2 = new JSTerm();
        jsTerm2.addElement(new JSTerm("x"));
        jsTerm2.addElement(new JSTerm("x"));
        assertTrue(jsTerm1.isGround());
        assertFalse(jsTerm2.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm jsTerm = new JSTerm();
        jsTerm.addElement(new JSTerm("x"));
        jsTerm.addElement(new JSTerm("y"));
        JSTerm expected = new JSTerm();
        expected.addElement(String.valueOf(JSJshopVars.VarCounter++));
        expected.add(new JSTerm(String.valueOf(JSJshopVars.VarCounter++)));
        assertEquals(expected, jsTerm.standardizerTerm().getElements());
    }

    @Test
    public void testJSTermCall() {
        JSTerm jsTerm = new JSTerm();
        jsTerm.addElement(new JSTerm("*"));
        jsTerm.addElement(new JSTerm("x"));
        jsTerm.addElement(new JSTerm("5"));
        JSTerm result = jsTerm.call();
        assertEquals(5.0, result.evaluate());
    }
}