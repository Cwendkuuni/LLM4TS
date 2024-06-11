import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*; // for importing all necessary classes

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormEmptyConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormStringConstructor() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(4, jspf.size());
        String expectedFirstElement = "!p";
        String actualFirstElement = jspf.elementAt(0).toString();
        assertEquals(expectedFirstElement, actualFirstElement);
    }

    @Test
    public void testJSPredicateFormStreamTokenizerConstructor() {
        String text = "(! p(x) & (q(y) | r(z)))";
        StringReader sr = new StringReader(text);
        StreamTokenizer st = new StreamTokenizer(sr);
        JSPredicateForm jspf = new JSPredicateForm(st);
        assertEquals(4, jspf.size());
        String expectedFirstElement = "!p";
        String actualFirstElement = jspf.elementAt(0).toString();
        assertEquals(expectedFirstElement, actualFirstElement);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitializationInvalidInput() {
        String text = "(! p(x) & (q(y) | r))"; // missing 'z' in r(z)
        StringReader sr = new StringReader(text);
        StreamTokenizer st = new StreamTokenizer(sr);
        new JSPredicateForm(st);
    }

    @Test
    public void testPrintMethod() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        jspf.print();
        String expectedOutput = "(!!p & (q(y) | r(z)))";
        String actualOutput = JSUtil.readOutput(); // assuming JSUtil.println is used in the print method
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testToStringMethod() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String expectedString = "(!p & (q(y) | r(z)))";
        String actualString = jspf.toString().toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testClonePFMethod() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm clonedPF = jspf.clonePF();
        assertEquals(jspf.toString(), clonedPF.toString());
    }

    @Test
    public void testApplySubstitutionPFMethod() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String substitutionVariable = "a";
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding(substitutionVariable, new Term("c"));
        JSPredicateForm expectedResult = new JSPredicateForm("(! p(x) & (q(a) | r(z)))");
        JSPredicateForm result = jspf.applySubstitutionPF(alpha);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testMatchesMethod() {
        String text1 = "(! p(x) & (q(y) | r(z)))";
        String text2 = "(! p(a) & (q(b) | r(b)))";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("x", new Term("a"));
        alpha.addBinding("y", new Term("b"));
        JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
        JSPredicateForm expectedResult = new JSPredicateForm("(! p(a) & (q(b) | r(b)))");
        JSPredicateForm result = new JSPredicateForm(text2);
        JSSubstitution beta = jspf1.matches(result);
        assertEquals(expectedResult.toString(), result.toString());
        assertTrue(beta.isEmpty());

        String text3 = "(! p(x) & (q(y) | r(z)))";
        String text4 = "(! p(a) & (q(b) | r(c)))";
        JSPredicateForm jspf3 = new JSPredicateForm(text3);
        JSPredicateForm result4 = new JSPredicateForm(text4);
        assertFalse(jspf3.matches(result4).isEmpty());
    }

    @Test
    public void testEqualsMethod() {
        String text1 = "(! p(x) & (q(y) | r(z)))";
        String text2 = "(! p(a) & (q(b) | r(z)))";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertFalse(jspf1.equals(jspf2));

        String text3 = "(! p(x) & (q(y) | r(z)))";
        String text4 = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf3 = new JSPredicateForm(text3);
        JSPredicateForm jspf4 = new JSPredicateForm(text4);
        assertTrue(jspf3.equals(jspf4));

        String text5 = "(! p(x) & (q(y) | r(z)))";
        String text6 = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf5 = new JSPredicateForm(text5);
        JSPredicateForm jspf6 = jspf5.clonePF();
        assertTrue(jspf5.equals(jspf6));
    }

    @Test
    public void testStandarizerPredicateFormMethod() {
        String text = "(! p(x) & (q(y) | r(z)))";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm standardizedPF = jspf.standarizerPredicateForm();
        String expectedOutput = "(!p & ∨(q(y) & r(z)))";
        String actualOutput = standardizedPF.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}