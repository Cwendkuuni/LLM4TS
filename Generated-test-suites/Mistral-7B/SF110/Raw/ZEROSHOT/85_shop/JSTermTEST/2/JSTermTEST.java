import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("")); // empty constructor
        final JSTerm jsterm = new JSTerm(tokenizer);
        assertNotNull(jsterm);
        assertTrue(jsterm.isEmpty());
        assertEquals(0, jsterm.size());

        tokenizer.reset(); // constructor with StreamTokenizer
        tokenizer.nextToken(); // consume "Expecting Term"
        final JSTerm term = new JSTerm(tokenizer);
        assertNotNull(term);
        assertFalse(term.isEmpty());
        assertEquals(1, term.size());
        assertEquals(term.elementAt(0), "Term");
    }

    @Test
    public void testJSTermAddElement() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        jsterm.addElement("constant");
        assertEquals(jsterm.elementAt(0), "constant");

        jsterm.addElement(new Double(42));
        assertEquals(jsterm.elementAt(1), "42.0");

        jsterm.addElement("variable");
        assertEquals(jsterm.elementAt(2), "variable");

        jsterm.addElement("function");
        assertEquals(jsterm.elementAt(3), "function");

        jsterm.addElement(new JSTerm(tokenizer));
        jsterm.addElement(new JSTerm(tokenizer));
        List<Object> expectedList = new ArrayList<>();
        expectedList.add("function");
        expectedList.add(new JSTerm(tokenizer));
        expectedList.add(new JSTerm(tokenizer));
        assertEquals(jsterm.toList(), expectedList);
    }

    @Test
    public void testJSTermIsVariable() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        jsterm.addElement("variable");
        assertTrue(jsterm.isVariable());

        jsterm.addElement("constant");
        assertFalse(jsterm.isVariable());

        jsterm.addElement("function");
        assertFalse(jsterm.isVariable());

        jsterm.addElement("nil");
        assertFalse(jsterm.isVariable());
    }

    @Test
    public void testJSTermIsConstant() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        jsterm.addElement("constant");
        assertTrue(jsterm.isConstant());

        jsterm.addElement("variable");
        assertFalse(jsterm.isConstant());

        jsterm.addElement("function");
        assertFalse(jsterm.isConstant());

        jsterm.addElement("nil");
        assertFalse(jsterm.isConstant());
    }

    @Test
    public void testJSTermIsFunction() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        jsterm.addElement("function");
        assertTrue(jsterm.isFunction());

        jsterm.addElement("constant");
        assertFalse(jsterm.isFunction());

        jsterm.addElement("variable");
        assertFalse(jsterm.isFunction());

        jsterm.addElement("nil");
        assertFalse(jsterm.isFunction());
    }

    @Test
    public void testJSTermIsEmpty() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();
        assertTrue(jsterm.isEmpty());

        jsterm.addElement("constant");
        assertFalse(jsterm.isEmpty());
    }

    @Test
    public void testJSTermSize() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        assertEquals(0, jsterm.size());
        jsterm.addElement("constant");
        assertEquals(1, jsterm.size());
        jsterm.addElement(new Double(42));
        assertEquals(2, jsterm.size());
        jsterm.addElement("variable");
        assertEquals(3, jsterm.size());
        jsterm.addElement("function");
        assertEquals(4, jsterm.size());
        jsterm.addElement(new JSTerm(tokenizer));
        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(6, jsterm.size());
    }

    @Test
    public void testJSTermElementAt() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        assertEquals(jsterm.elementAt(0), "Term");

        jsterm.addElement("constant");
        assertEquals(jsterm.elementAt(0), "constant");

        jsterm.addElement(new Double(42));
        assertEquals(jsterm.elementAt(1), "42.0");

        jsterm.addElement("variable");
        assertEquals(jsterm.elementAt(2), "variable");

        jsterm.addElement("function");
        assertEquals(jsterm.elementAt(3), "function");

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.elementAt(4), new JSTerm(tokenizer));

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.elementAt(5), new JSTerm(tokenizer));
    }

    @Test
    public void testJSTermToList() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        List<Object> expectedList = new ArrayList<>();
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement("constant");
        expectedList.add("constant");
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement(new Double(42));
        expectedList.add("constant");
        expectedList.add("42.0");
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement("variable");
        expectedList.add("constant");
        expectedList.add("42.0");
        expectedList.add("variable");
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement("function");
        expectedList.add("constant");
        expectedList.add("42.0");
        expectedList.add("variable");
        expectedList.add("function");
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement(new JSTerm(tokenizer));
        expectedList.add("constant");
        expectedList.add("42.0");
        expectedList.add("variable");
        expectedList.add("function");
        expectedList.add(new JSTerm(tokenizer));
        assertEquals(jsterm.toList(), expectedList);

        jsterm.addElement(new JSTerm(tokenizer));
        expectedList.add("constant");
        expectedList.add("42.0");
        expectedList.add("variable");
        expectedList.add("function");
        expectedList.add(new JSTerm(tokenizer));
        expectedList.add(new JSTerm(tokenizer));
        assertEquals(jsterm.toList(), expectedList);
    }

    @Test
    public void testJSTermToString() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();
        assertEquals(jsterm.toString(), "()");

        jsterm.addElement("constant");
        assertEquals(jsterm.toString(), "constant");

        jsterm.addElement(new Double(42));
        assertEquals(jsterm.toString(), "42.0");

        jsterm.addElement("variable");
        assertEquals(jsterm.toString(), "variable");

        jsterm.addElement("function");
        assertEquals(jsterm.toString(), "function");

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.toString(), "(" + new JSTerm(tokenizer).toString() + ")");

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.toString(), "(" + new JSTerm(tokenizer).toString() + " " + new JSTerm(tokenizer).toString() + ")");
    }

    @Test
    public void testJSTermIsGround() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        assertTrue(jsterm.isGround());

        jsterm.addElement("constant");
        assertTrue(jsterm.isGround());

        jsterm.addElement(new Double(42));
        assertTrue(jsterm.isGround());

        jsterm.addElement("variable");
        assertFalse(jsterm.isGround());

        jsterm.addElement("function");
        assertFalse(jsterm.isGround());

        jsterm.addElement(new JSTerm(tokenizer));
        assertFalse(jsterm.isGround());

        jsterm.addElement(new JSTerm(tokenizer));
        assertFalse(jsterm.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        assertEquals(jsterm.standardizerTerm().toString(), "()");

        jsterm.addElement("constant");
        assertEquals(jsterm.standardizerTerm().toString(), "constant");

        jsterm.addElement(new Double(42));
        assertEquals(jsterm.standardizerTerm().toString(), "42.0");

        jsterm.addElement("variable");
        assertEquals(jsterm.standardizerTerm().toString(), "variable" + String.valueOf(JSJshopVars.VarCounter));

        jsterm.addElement("function");
        assertEquals(jsterm.standardizerTerm().toString(), "function" + String.valueOf(JSJshopVars.VarCounter));

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.standardizerTerm().toString(), "(" + new JSTerm(tokenizer).standardizerTerm().toString() + ")");

        jsterm.addElement(new JSTerm(tokenizer));
        assertEquals(jsterm.standardizerTerm().toString(), "(" + new JSTerm(tokenizer).standardizerTerm().toString() + " " + new JSTerm(tokenizer).standardizerTerm().toString() + ")");
    }

    @Test
    public void testJSTermCall() {
        final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        final JSTerm jsterm = new JSTerm();

        assertEquals(jsterm.call().toString(), "()");

        jsterm.addElement("not");
        assertEquals(jsterm.call().toString(), "not()");

        jsterm.addElement(new Double(42));
        assertEquals(jsterm.call().toString(), "42.0");

        jsterm.addElement("+");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "6.0");

        jsterm.addElement("-");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "2.0");

        jsterm.addElement("*");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "8.0");

        jsterm.addElement("/");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "2.0");

        jsterm.addElement("%");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "0.0");

        jsterm.addElement("function");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "function(4.0, 2.0)");

        jsterm.addElement("list");
        jsterm.addElement(new Double(4));
        jsterm.addElement(new Double(2));
        assertEquals(jsterm.call().toString(), "nil"); // The call of a list term should return nil.
    }
}