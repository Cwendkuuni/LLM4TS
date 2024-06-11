package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyConstructor() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testStringConstructor() {
        JSPredicateForm jspf = new JSPredicateForm("not ( a = b )");
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertEquals("( a = b)", jspf.elementAt(1).toString());
    }

    @Test
    public void testStreamTokenizerConstructor() {
        String input = "not ( a = b )";
        StringReader reader = new StringReader(input);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(2, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString());
        assertEquals("( a = b)", jspf.elementAt(1).toString());
    }

    @Test
    public void testPrint() throws IOException {
        String input = "not ( a = b )";
        StringReader reader = new StringReader(input);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        jspf.print();
        String expectedOutput = "Predicateform.print(): ( not ( a = b ) )";
        assertEquals(expectedOutput, JSUtil.out.toString());
    }

    @Test
    public void testToString() {
        JSPredicateForm jspf = new JSPredicateForm("not ( a = b )");
        String expectedOutput = "((not) (a = b))";
        assertEquals(expectedOutput, jspf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm("not ( a = b )");
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf.size(), cloned.size());
        assertFalse(jspf.equals(cloned));
        assertTrue(jspf.toString().equals(cloned.toString()));
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm("not ( a = b )");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("a", "X");
        alpha.addBinding("b", "Y");
        JSPredicateForm result = jspf.applySubstitutionPF(alpha);
        String expectedOutput = "not ( X = Y )";
        assertEquals(expectedOutput, result.toStr().toString());
    }

    @Test
    public void testMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("not ( a = b )");
        JSPredicateForm jspf2 = new JSPredicateForm("( a = c )");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = jspf1.matches(jspf2, alpha);
        assertTrue(beta.isFailure());

        JSTerm t1 = new JSTerm("a");
        JSTerm t2 = new JSTerm("b");
        JSTerm t3 = new JSTerm("c");
        JSPredicateForm jspf3 = new JSPredicateForm("not ( a = b )");
        JSPredicateForm jspf4 = new JSPredicateForm("not ( a = c )");
        beta = jspf3.matches(jspf4, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("a", "X");
        alpha.addBinding("b", "Y");
        alpha.addBinding("c", "Z");
        beta = jspf1.matches(jspf4, alpha);
        List<String> expectedBindings = Arrays.asList("X=a", "Y=b", "Z=c");
        List<String> actualBindings = new ArrayList<>(beta.getBindings().size());
        for (JSSubstitution.Binding binding : beta.getBindings()) {
            actualBindings.add(binding.toString());
        }
        assertEquals(expectedBindings, actualBindings);

        JSPredicateForm jspf5 = new JSPredicateForm("not (( a = b ) and ( b = c ))");
        JSPredicateForm jspf6 = new JSPredicateForm("not ( c = a )");
        beta = jspf5.matches(jspf6, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("a", "X");
        alpha.addBinding("b", "Y");
        alpha.addBinding("c", "Z");
        beta = jspf5.matches(jspf6, alpha);
        expectedBindings = Arrays.asList("X=a", "Y=b", "Z=c");
        actualBindings = new ArrayList<>(beta.getBindings().size());
        for (JSSubstitution.Binding binding : beta.getBindings()) {
            actualBindings.add(binding.toString());
        }
        assertEquals(expectedBindings, actualBindings);

        JSPredicateForm jspf7 = new JSPredicateForm("not (( a = b ) and ( b = c ))");
        JSPredicateForm jspf8 = new JSPredicateForm("not (( a = c ) and ( c = b ))");
        beta = jspf5.matches(jspf8, alpha);
        assertEquals(Collections.emptyList(), beta.getBindings());
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("not ( a = b )");
        JSPredicateForm jspf2 = new JSPredicateForm("not ( a = b )");
        assertTrue(jspf1.equals(jspf2));

        JSPredicateForm jspf3 = new JSPredicateForm("not ( a = b )");
        JSTerm t1 = new JSTerm("a");
        JSTerm t2 = new JSTerm("b");
        JSTerm t3 = new JSTerm("c");
        JSPredicateForm jspf4 = new JSPredicateForm("not ( " + t1.toString() + " = " + t2.toString() + " )");
        JSPredicateForm jspf5 = new JSPredicateForm("not ( " + t2.toString() + " = " + t3.toString() + " )");
        JSPredicateForm jspf6 = new JSPredicateForm("not ( " + t3.toString() + " = " + t1.toString() + " )");
        assertFalse(jspf1.equals(jspf4));
        assertFalse(jspf1.equals(jspf5));
        assertFalse(jspf1.equals(jspf6));

        JSPredicateForm jspf9 = new JSPredicateForm("not ( ( a = b ) and ( b = c ))");
        JSPredicateForm jspf10 = new JSPredicateForm("not ( ( b = c ) and ( c = a ))");
        assertTrue(jspf9.equals(jspf10));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm("not ( a = b )");
        JSPredicateForm result = jspf.standarizerPredicateForm();
        assertEquals("not ( a = b )", jspf.toString());
        assertEquals("(not) (= (a) (b))", result.toString());
    }
}