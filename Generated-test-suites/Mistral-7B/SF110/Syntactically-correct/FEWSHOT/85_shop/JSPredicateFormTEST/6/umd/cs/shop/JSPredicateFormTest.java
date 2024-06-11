package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
    }

    @Test
    public void testJSPredicateFormInitWithText() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf = new JSPredicateForm(inputText);
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString().toLowerCase());
        assertEquals("predicate1", jspf.elementAt(1).toString().toLowerCase());
        assertNotNull(jspf.elementAt(2));
        assertEquals("arg1", ((JSTerm) jspf.elementAt(2)).getFunctor().toString().toLowerCase());
        assertEquals("x", ((JSTerm) jspf.elementAt(2)).getArgument().toString().toLowerCase());
        assertEquals("arg2", ((JSTerm) jspf.elementAt(3)).getFunctor().toString().toLowerCase());
        assertEquals("y", ((JSTerm) jspf.elementAt(3)).getArgument().toString().toLowerCase());
    }

    @Test
    public void testJSPredicateFormInitWithTokenizer() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(inputText));
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0).toString().toLowerCase());
        assertEquals("predicate1", jspf.elementAt(1).toString().toLowerCase());
        assertNotNull(jspf.elementAt(2));
        assertEquals("arg1", ((JSTerm) jspf.elementAt(2)).getFunctor().toString().toLowerCase());
        assertEquals("x", ((JSTerm) jspf.elementAt(2)).getArgument().toString().toLowerCase());
        assertEquals("arg2", ((JSTerm) jspf.elementAt(3)).getFunctor().toString().toLowerCase());
        assertEquals("y", ((JSTerm) jspf.elementAt(3)).getArgument().toString().toLowerCase());
    }

    @Test
    public void testPrint() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf = new JSPredicateForm(inputText);
        assertEquals("(not (predicate1 (arg1 x) (arg2 y)) )", jspf.print());
    }

    @Test
    public void testToStr() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf = new JSPredicateForm(inputText);
        assertEquals("(not (predicate1 (arg1 x) (arg2 y)) )", jspf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf = new JSPredicateForm(inputText);
        JSPredicateForm clonedJSPF = jspf.clonePF();
        assertEquals(jspf.toString(), clonedJSPF.toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        String inputText1 = "not (predicate1 (arg1 x) (arg2 y))";
        String inputText2 = "not (predicate2 (arg1 z) (arg2 w))";
        JSPredicateForm jspf1 = new JSPredicateForm(inputText1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "z");
        alpha.addSubstitution("y", "w");
        JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
        assertEquals("not (predicate2 (arg1 z) (arg2 w))", jspf2.toString());

        String inputText3 = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf3 = new JSPredicateForm(inputText3);
        JSPredicateForm jspf4 = new JSPredicateForm();
        JSSubstitution beta = jspf3.matches(jspf4, new JSSubstitution());
        assertNotNull(beta);
        assertEquals(0, beta.size());
    }

    @Test
    public void testMatches() {
        String inputText1 = "not (predicate1 (arg1 x) (arg2 y))";
        String inputText2 = "not (predicate1 (arg1 z) (arg2 w))";
        JSPredicateForm jspf1 = new JSPredicateForm(inputText1);
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("x", "z");
        alpha.addSubstitution("y", "w");
        JSPredicateForm jspf2 = new JSPredicateForm(inputText2);
        JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
        assertEquals("x", beta.getVariable("z").toString());
        assertEquals("y", beta.getVariable("w").toString());
        assertEquals("predicate1", beta.getFunction("predicate1").toString());

        String inputText3 = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf3 = new JSPredicateForm(inputText3);
        JSPredicateForm jspf4 = new JSPredicateForm();
        JSSubstitution gamma = new JSSubstitution();
        gamma.addSubstitution("arg1", "arg3");
        gamma.addSubstitution("arg2", "arg4");
        JSSubstitution beta2 = jspf3.matches(jspf4, gamma);
        assertNull(beta2);
    }

    @Test
    public void testEquals() {
        String inputText1 = "not (predicate1 (arg1 x) (arg2 y))";
        String inputText2 = "not (predicate1 (arg1 z) (arg2 w))";
        JSPredicateForm jspf1 = new JSPredicateForm(inputText1);
        JSPredicateForm jspf2 = new JSPredicateForm(inputText2);
        assertFalse(jspf1.equals(jspf2));

        String inputText3 = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf3 = new JSPredicateForm(inputText3);
        JSPredicateForm jspf4 = new JSPredicateForm(inputText3);
        assertTrue(jspf3.equals(jspf4));

        String inputText4 = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf5 = new JSPredicateForm(inputText4);
        JSPredicateForm jspf6 = jspf5.clonePF();
        assertTrue(jspf5.equals(jspf6));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String inputText = "not (predicate1 (arg1 x) (arg2 y))";
        JSPredicateForm jspf = new JSPredicateForm(inputText);
        JSPredicateForm standardizedJSPF = jspf.standarizerPredicateForm();
        assertEquals("not (predicate1 (arg1 x) (arg2 y))", standardizedJSPF.toString());

        String inputText2 = "not (predicate2 (arg1 x) (arg2 y) (arg3 z))";
        JSPredicateForm jspf2 = new JSPredicateForm(inputText2);
        JSPredicateForm standardizedJSPF2 = jspf2.standarizerPredicateForm();
        assertEquals("not (predicate2 (arg1 x) (arg2 y) (arg3 z))", standardizedJSPF2.toString());

        String inputText3 = "not (predicate1 (arg1 (predicate3 arg1 arg2)) (arg2 y))";
        JSPredicateForm jspf3 = new JSPredicateForm(inputText3);
        JSPredicateForm standardizedJSPF3 = jspf3.standarizerPredicateForm();
        assertEquals("not (predicate1 (arg1 (predicate3 arg1 arg2)) (arg2 y))", standardizedJSPF3.toString());
        assertEquals("not (predicate1 (arg1 (predicate3 arg1 arg2)) (arg2 y))", standardizedJSPF3.elementAt(0).toString());
        assertEquals("arg1", standardizedJSPF3.elementAt(1).toString());
        assertEquals("predicate3", standardizedJSPF3.elementAt(2).toString());
        assertEquals("arg1", standardizedJSPF3.elementAt(3).toString());
        assertEquals("arg2", standardizedJSPF3.elementAt(4).toString());
        assertEquals("arg2", standardizedJSPF3.elementAt(5).toString());
        assertEquals("y", standardizedJSPF3.elementAt(6).toString());
    }
}