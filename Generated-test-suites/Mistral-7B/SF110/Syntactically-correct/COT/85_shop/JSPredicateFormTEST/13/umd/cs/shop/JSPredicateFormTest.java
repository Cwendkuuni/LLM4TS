package umd.cs.shop; // make sure to import all necessary packages

import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {
    

    // Test constructor with null argument
    @Test(expected = JSParserError.class)
    public void testConstructorWithNullArgument() {
        new JSPredicateForm(null);
    }

    // Test constructor with empty String argument
    @Test
    public void testConstructorWithEmptyStringArgument() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSPredicateForm expected = new JSPredicateForm();
        assertEquals(expected, jspf);
    }

    // Test constructor with valid String argument
    @Test
    public void testConstructorWithValidStringArgument() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm expected = new JSPredicateForm("not", "x<5", "y>10");
        assertEquals(expected, jspf);
    }

    // Test constructor with valid StreamTokenizer argument
    @Test
    public void testConstructorWithValidStreamTokenizerArgument() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        JSPredicateForm expected = new JSPredicateForm("not", "x<5", "y>10");
        assertEquals(expected, jspf);
    }

    // Test JSPredicateFormInit method with invalid StreamTokenizer argument
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidStreamTokenizerArgument() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) %%% )";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        new JSPredicateForm(tokenizer);
    }

    // Test print method
    @Test
    public void testPrint() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String expected = "((NOT) ( (x<5) ) (y>10) )";
        assertEquals(expected, jspf.print());
    }

    // Test toStr method
    @Test
    public void testToStr() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        String expected = "((NOT) (x<5) (y>10))";
        assertEquals(expected, jspf.toStr().toString());
    }

    // Test clonePF method
    @Test
    public void testClonePF() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf, cloned);
    }

    // Test applySubstitutionPF method
    @Test
    public void testApplySubstitutionPF() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( a < 5 ) and ( z > 10 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "a");
        alpha.addSubstitution("y", "z");
        JSPredicateForm result = jspf1.applySubstitutionPF(alpha);
        JSPredicateForm expected = new JSPredicateForm("not", "a<5", "z>10");
        assertEquals(expected, result);
    }

    // Test equals method
    @Test
    public void testEquals() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertTrue(jspf1.equals(jspf2));
    }

    // Test equals method with different sizes
    @Test
    public void testEqualsWithDifferentSizes() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( x < 5 ) and ( y > 10 ) and ( z > 11 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertFalse(jspf1.equals(jspf2));
    }

    // Test equals method with different functor
    @Test
    public void testEqualsWithDifferentFunctor() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "and ( x < 5 ) and ( y > 10 )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSPredicateForm jspf2 = new JSPredicateForm(text2);
        assertFalse(jspf1.equals(jspf2));
    }

    // Test matches method with empty alpha
    @Test
    public void testMatchesWithEmptyAlpha() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = jspf1.matches(new JSPredicateForm(), alpha);
        assertTrue(result.isEmpty());
    }

    // Test matches method with non-empty alpha
    @Test
    public void testMatchesWithNonEmptyAlpha() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( a < 5 ) and ( b > 10 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "a");
        alpha.addSubstitution("y", "b");
        JSSubstitution result = jspf1.matches(new JSPredicateForm(text2), alpha);
        JSSubstitution expected = new JSSubstitution();
        expected.addSubstitution("a", "x");
        expected.addSubstitution("b", "y");
        assertEquals(expected, result);
    }

    // Test matches method with mismatched sizes
    @Test
    public void testMatchesWithMismatchedSizes() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( x < 5 ) and ( y > 10 ) and ( z > 11 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = jspf1.matches(new JSPredicateForm(text2), alpha);
        assertTrue(result.fail());
    }

    // Test matches method with mismatched functor
    @Test
    public void testMatchesWithMismatchedFunctor() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "and ( x < 5 ) and ( y > 10 )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = jspf1.matches(new JSPredicateForm(text2), alpha);
        assertTrue(result.fail());
    }

    // Test matches method with mismatched terms
    @Test
    public void testMatchesWithMismatchedTerms() {
        String text1 = "not ( ( x < 5 ) and ( y > 10 ) )";
        String text2 = "not ( ( a < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf1 = new JSPredicateForm(text1);
        JSTerm term1 = new JSTerm("x<5");
        JSTerm term2 = new JSTerm("a<5");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "a");
        JSSubstitution result = jspf1.matches(new JSPredicateForm("not", term1, term2), alpha);
        assertTrue(result.fail());
    }

    // Test standarizerPredicateForm method
    @Test
    public void testStandarizerPredicateForm() {
        String text = "not ( ( x < 5 ) and ( y > 10 ) )";
        JSPredicateForm jspf = new JSPredicateForm(text);
        JSPredicateForm expected = new JSPredicateForm("not", "x<5", "y>10");
        JSPredicateForm result = jspf.standarizerPredicateForm();
        assertEquals(expected, result);
    }

    
}