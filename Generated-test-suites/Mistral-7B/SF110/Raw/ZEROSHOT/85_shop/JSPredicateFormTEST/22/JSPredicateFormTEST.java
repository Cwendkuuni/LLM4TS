import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

    @Test
    public void testEmptyConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testStringConstructor() {
        String input = "(! p)";
        JSPredicateForm jspf = new JSPredicateForm(input);
        assertEquals(1, jspf.size());
        assertEquals("!p", jspf.elementAt(0).toString());
    }

    @Test
    public void testStreamTokenizerConstructor() {
        String input = "(! p)";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        assertEquals(1, jspf.size());
        assertEquals("!p", jspf.elementAt(0).toString());
    }

    @Test(expected = JSParserError.class)
    public void testInvalidInputConstructor() {
        String input = "()";
        new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        fail("Expected JSParserError to be thrown.");
    }

    @Test
    public void testPrintMethod() {
        String input = "(! p)";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        JSUtil.print("Expected: (! p)");
        jspf.print();
        JSUtil.println("Actual:");
    }

    @Test
    public void testToStringMethod() {
        String input = "(! p)";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        String expected = "( !p )";
        String actual = jspf.toStr().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testClonePFMethod() {
        String input = "(! p)";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        JSPredicateForm clonedPF = jspf.clonePF();
        assertTrue(jspf.equals(clonedPF));
    }

    @Test
    public void testApplySubstitutionPFMethod() {
        String input1 = "(! p)";
        JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input2 = "(q)";
        JSSubstitution substitution = new JSSubstitution();
        substitution.addBinding("p", new JSTerm("q"));

        JSPredicateForm result = jspf1.applySubstitutionPF(substitution);
        String expected = "( !q )";
        String actual = result.toStr().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesMethod() {
        String input1 = "(! p)";
        JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input2 = "(q)";
        JSSubstitution expectedSubstitution = new JSSubstitution();
        expectedSubstitution.addBinding("p", new JSTerm("q"));

        JSSubstitution resultSubstitution = jspf1.matches(new JSPredicateForm(new StreamTokenizer(new StringReader(input2))));
        assertEquals(expectedSubstitution, resultSubstitution);
    }

    @Test
    public void testMatchesMethodWithEmptySubstitution() {
        String input1 = "(! p)";
        JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input2 = "(p)";
        JSSubstitution emptySubstitution = new JSSubstitution();

        JSSubstitution resultSubstitution = jspf1.matches(new JSPredicateForm(new StreamTokenizer(new StringReader(input2))), emptySubstitution);
        assertTrue(resultSubstitution.isEmpty());
    }

    @Test
    public void testMatchesMethodFailure() {
        String input1 = "(! p)";
        JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input2 = "(q)";
        JSSubstitution emptySubstitution = new JSSubstitution();

        JSSubstitution resultSubstitution = jspf1.matches(new JSPredicateForm(new StreamTokenizer(new StringReader("(r)"))), emptySubstitution);
        assertTrue(resultSubstitution.isEmpty());
        assertFalse(jspf1.equals(new JSPredicateForm(new StreamTokenizer(new StringReader("(r)")))));
    }

    @Test
    public void testEqualsMethod() {
        String input1 = "(! p)";
        JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input2 = "(q)";
        JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader(input1)));
        String input3 = "(q)";
        JSPredicateForm jspf3 = new JSPredicateForm(new StreamTokenizer(new StringReader(input2)));

        assertTrue(jspf1.equals(jspf2));
        assertFalse(jspf1.equals(jspf3));
    }

    @Test
    public void testStandarizerPredicateFormMethod() {
        String input = "((!p) (q r))";
        JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
        String expected = "(!p) (q r)";
        String actual = jspf.standarizerPredicateForm().toStr().toString();
        assertEquals(expected, actual);
    }
}