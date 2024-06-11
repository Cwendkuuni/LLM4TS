package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());
    }

    @Test
    public void testJSPredicateFormWithText() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0).toString());
        assertEquals("x > y", pf.elementAt(1).toString());
    }

    @Test
    public void testJSPredicateFormWithStreamTokenizer() {
        String text = "not (x > y)";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
        assertEquals(2, pf.size());
        assertEquals("not", pf.elementAt(0).toString());
        assertEquals("x > y", pf.elementAt(1).toString());
    }

    @Test
    public void testJSPredicateFormInitWithInvalidText() {
        String text = "not (x > y)abc";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        assertThrows(JSParserError.class, () -> new JSPredicateForm(tokenizer));
    }

    @Test
    public void testJSPredicateFormInitWithInvalidTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        assertThrows(NullPointerException.class, () -> new JSPredicateForm(tokenizer));

        tokenizer = new StreamTokenizer(null);
        assertThrows(NullPointerException.class, () -> new JSPredicateForm(tokenizer));
    }

    @Test
    public void testPrint() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        String expected = "((not) (x > y))";
        assertEquals(expected, pf.print());
    }

    @Test
    public void testToStr() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        String expected = "((not) (x > y))";
        assertEquals(expected, pf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        String text = "not (x > y)";
        JSPredicateForm originalPF = new JSPredicateForm(text);
        JSPredicateForm clonedPF = originalPF.clonePF();
        assertEquals(originalPF, clonedPF);
    }

    @Test
    public void testApplySubstitutionPF() {
        String text = "not (x > y)";
        JSPredicateForm originalPF = new JSPredicateForm(text);
        JSSubstitution substitution = new JSSubstitution();
        JSPredicateForm transformedPF = originalPF.applySubstitutionPF(substitution);
        assertNull(transformedPF.matches(originalPF));
    }

    @Test
    public void testMatches() {
        JSPredicateForm pf1 = new JSPredicateForm("(x > y)");
        JSPredicateForm pf2 = new JSPredicateForm("(z > y)");
        JSSubstitution substitution = new JSSubstitution();
        JSSubstitution matches = pf1.matches(pf2, substitution);
        assertTrue(matches.isEmpty());

        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("z", new JSType("integer"));
        JSTerm term3 = new JSTerm("y", new JSType("integer"));
        pf1.setElementAt(term1, 1);
        pf2.setElementAt(term2, 1);
        pf1.setElementAt(term3, 2);
        pf2.setElementAt(term3, 2);

        substitution.addElement(new JSSubstitutionEntry("x", "z"));
        matches = pf1.matches(pf2, substitution);
        assertEquals(1, matches.size());
        assertEquals("z", matches.elementAt(0).getValue().toString());
    }

    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("(x > y)");
        JSPredicateForm pf2 = new JSPredicateForm("(x > y)");
        JSPredicateForm pf3 = new JSPredicateForm("(x < y)");
        assertTrue(pf1.equals(pf2));
        assertFalse(pf1.equals(pf3));
    }

    @Test
    public void testStandarizerPredicateForm() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSPredicateForm standardPF = pf.standarizerPredicateForm();
        String expected = "(not (x < y))";
        assertEquals(expected, standardPF.toStr().toString());
    }

    @Test
    public void testSize() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals(2, pf.size());
    }

    @Test
    public void testGetElementAt() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals("not", pf.elementAt(0).toString());
        assertEquals("x > y", pf.elementAt(1).toString());
    }

    @Test
    public void testSetElementAt() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("z", new JSType("integer"));
        JSTerm term2 = new JSTerm("w", new JSType("integer"));
        pf.setElementAt(term1, 1);
        pf.setElementAt(term2, 2);
        assertEquals("(z w)", pf.toStr().toString());
    }

    @Test
    public void testAddElement() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("z", new JSType("integer"));
        JSTerm term2 = new JSTerm("w", new JSType("integer"));
        pf.addElement(term1);
        pf.addElement(term2);
        assertEquals("(z) (w)", pf.toStr().toString());
    }

    @Test
    public void testAdd() {
        JSPredicateForm pf = new JSPredicateForm();
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        List<JSTerm> list = new ArrayList<>();
        list.add(term1);
        list.add(term2);
        pf.addAll(list);
        assertEquals("(x y)", pf.toStr().toString());
    }

    @Test
    public void testRemoveElementAt() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        assertEquals("not (x > y)", pf.toStr().toString());
        pf.removeElementAt(0);
        assertEquals("(x > y)", pf.toStr().toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.removeElementAt(1);
    }

    @Test
    public void testClear() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals("()", pf.toStr().toString());
        pf.clear();
        assertEquals("()", pf.toStr().toString());
    }

    @Test
    public void testContains() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        pf.addElement(term1);
        pf.addElement(term2);
        assertTrue(pf.contains(term1));
        assertTrue(pf.contains(term2));
        assertFalse(pf.contains(new JSTerm("z", new JSType("integer"))));
    }

    @Test
    public void testIndexOf() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        pf.addElement(term1);
        pf.addElement(term2);
        assertEquals(0, pf.indexOf(term1));
        assertEquals(1, pf.indexOf(term2));
        assertEquals(-1, pf.indexOf(new JSTerm("z", new JSType("integer"))));
    }

    @Test
    public void testLastIndexOf() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        pf.addElement(term1);
        pf.addElement(term2);
        assertEquals(1, pf.lastIndexOf(term2));
        assertEquals(-1, pf.lastIndexOf(new JSTerm("z", new JSType("integer"))));
    }

    @Test
    public void testRemove() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        pf.addElement(term1);
        pf.addElement(term2);
        pf.remove(term1);
        assertEquals("(y)", pf.toStr().toString());
    }

    @Test
    public void testRemoveAll() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        List<JSTerm> list = new ArrayList<>();
        list.add(term1);
        list.add(term2);
        pf.addAll(list);
        pf.removeAll(list);
        assertEquals("()", pf.toStr().toString());
    }

    @Test
    public void testRetainAll() {
        String text = "not (x > y) (z > w)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("x", new JSType("integer"));
        JSTerm term2 = new JSTerm("y", new JSType("integer"));
        JSTerm term3 = new JSTerm("z", new JSType("integer"));
        JSTerm term4 = new JSTerm("w", new JSType("integer"));
        List<JSTerm> list = new ArrayList<>();
        list.add(term1);
        list.add(term3);
        pf.retainAll(list);
        assertEquals("(x z)", pf.toStr().toString());
    }

    @Test
    public void testAddAll() {
        String text = "not (x > y)";
        JSPredicateForm pf = new JSPredicateForm(text);
        JSTerm term1 = new JSTerm("z", new JSType("integer"));
        JSTerm term2 = new JSTerm("w", new JSType("integer"));
        List<JSTerm> list = new ArrayList<>();
        list.add(term1);
        list.add(term2);
        pf.addAll(list);
        assertEquals("(z w) (not (x > y))", pf.toStr().toString());
    }
}