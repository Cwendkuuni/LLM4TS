import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSTermTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        JSTerm jsterm = new JSTerm();
        assertTrue(jsterm.isEmpty());
    }

    @org.junit.Test
    public void testConstructorWithStreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
        JSTerm jsterm = new JSTerm(tokenizer);
        assertEquals("+(1.0 2.0)", jsterm.toString());
    }

    @org.junit.Test(expected = JSParserError.class)
    public void testInvalidConstruction() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalid"));
        new JSTerm(tokenizer);
    }

    @org.junit.Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 (list 2 3))"));
        JSTerm jsterm = new JSTerm();
        JSTerm list = jsterm.parseList(tokenizer);
        assertEquals("(2.0 3.0)", list.toString());
    }

    @org.junit.Test
    public void testPrintList() {
        JSTerm jsterm = new JSTerm();
        JSTerm list = jsterm.parseList(new StreamTokenizer(new StringReader("(1 2 3)")));
        jsterm.printList(list);
        assertEquals("1.0 2.0 3.0", jsterm.toString());
    }

    @org.junit.Test
    public void testCloneT() {
        JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
        JSTerm cloned = jsterm.cloneT();
        assertEquals("+(1.0 2.0)", cloned.toString());
    }

    @org.junit.Test
    public void testApplySubstitutionT() {
        JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
        JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
        JSSubstitution substitution = new JSSubstitution();
        substitution.addMapping(new JSPair("x", jsterm2));
        JSTerm result = jsterm1.applySubstitutionT(substitution);
        assertEquals("(y)", result.toString());
    }

    // Add more test cases here for other methods as needed
}