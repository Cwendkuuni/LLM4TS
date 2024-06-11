package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.MapIterator;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class JSPredicateFormTest {

    @Test
    public void testEmptyJSPredicateForm() {
        final JSPredicateForm jsp = new JSPredicateForm();
        assertEquals(0, jsp.size());
        jsp.print();
    }

    @Test
    public void testJSPredicateFormWithString() {
        final String text = "not (a = b)";
        final JSPredicateForm jsp = new JSPredicateForm(text);
        assertEquals(2, jsp.size());
        assertEquals("not", jsp.elementAt(0).toString());
        assertEquals("a = b", jsp.elementAt(1).toString());
        jsp.print();
    }

    @Test
    public void testJSPredicateFormWithStreamTokenizer() {
        final String text = "not (a = b)";
        final StringReader reader = new StringReader(text);
        final StreamTokenizer tokenizer = new StreamTokenizer(reader);
        final JSPredicateForm jsp = new JSPredicateForm(tokenizer);
        assertEquals(2, jsp.size());
        assertEquals("not", jsp.elementAt(0).toString());
        assertEquals("a = b", jsp.elementAt(1).toString());
        jsp.print();
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInvalidInput() {
        final String text = "not (a = b)--";
        final StringReader reader = new StringReader(text);
        final StreamTokenizer tokenizer = new StreamTokenizer(reader);
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testJSPredicateFormWithNestedNot() {
        final String text = "not not (a = b)";
        final JSPredicateForm jsp = new JSPredicateForm(text);
        assertEquals(3, jsp.size());
        assertEquals("not", jsp.elementAt(0).toString());
        assertEquals("not", jsp.elementAt(1).toString());
        assertEquals("a = b", jsp.elementAt(2).toString());
        jsp.print();
    }

    @Test
    public void testJSPredicateFormWithCall() {
        final String text = "not (call f (a = b))";
        final JSPredicateForm jsp = new JSPredicateForm(text);
        assertEquals(3, jsp.size());
        assertEquals("not", jsp.elementAt(0).toString());
        assertEquals("call", jsp.elementAt(1).toString());
        assertEquals("f", jsp.elementAt(2).toString());
        assertEquals("[not (a = b)]", jsp.elementAt(3).toString());
        jsp.print();
    }

    @Test
    public void testJSPredicateFormToString() {
        final String text = "not (a = b)";
        final JSPredicateForm jsp = new JSPredicateForm(text);
        final String expected = "not (a = b)";
        assertEquals(expected, jsp.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClone() {
        final String text = "not (a = b)";
        final JSPredicateForm original = new JSPredicateForm(text);
        final JSPredicateForm clone = original.clonePF();
        assertEquals(original.size(), clone.size());
        for (int i = 0; i < original.size(); i++) {
            final Object o = original.elementAt(i);
            final Object c = clone.elementAt(i);
            if (o instanceof String) {
                assertEquals(o.toString(), c.toString());
            } else if (o instanceof JSTerm) {
                assertEquals(o.hashCode(), c.hashCode());
                assertEquals(o.toString(), c.toString());
            }
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitution() {
        final String text1 = "not (a = b)";
        final String text2 = "not (c = d)";
        final JSSubstitution alpha = new JSSubstitution();
        alpha.put("a", "c");
        alpha.put("b", "d");
        final JSPredicateForm original1 = new JSPredicateForm(text1);
        final JSPredicateForm original2 = new JSPredicateForm(text2);
        final JSPredicateForm expected = new JSPredicateForm(new String("not (c = d)"));
        final JSPredicateForm result = original1.applySubstitutionPF(alpha);
        assertEquals(expected, result);
    }

    @Test
    public void testJSPredicateFormMatches() {
        final String text1 = "not (a = b)";
        final String text2 = "not (c = d)";
        final JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        final JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        final JSSubstitution substitution = new JSSubstitution();
        final JSSubstitution match = predicateForm1.matches(predicateForm2);
        assertTrue(match.isEmpty());

        final String text3 = "not (a = b)";
        final String text4 = "not (a = a)";
        final JSPredicateForm predicateForm3 = new JSPredicateForm(text3);
        final JSPredicateForm predicateForm4 = new JSPredicateForm(text4);
        final JSSubstitution substitution2 = new JSSubstitution();
        final JSSubstitution match2 = predicateForm3.matches(predicateForm4, substitution2);
        assertFalse(match2.isEmpty());
        assertEquals(1, match2.size());
        assertEquals("a", match2.getKeyAt(0));
        assertEquals("a", match2.getValueAt(0));

        final String text5 = "not (a = b)";
        final String text6 = "not (b = a)";
        final JSPredicateForm predicateForm5 = new JSPredicateForm(text5);
        final JSPredicateForm predicateForm6 = new JSPredicateForm(text6);
        final JSSubstitution substitution3 = new JSSubstitution();
        final JSSubstitution match3 = predicateForm5.matches(predicateForm6, substitution3);
        assertFalse(match3.isEmpty());
        assertEquals(1, match3.size());
        assertEquals("b", match3.getKeyAt(0));
        assertEquals("a", match3.getValueAt(0));

        final String text7 = "not (a = b)";
        final String text8 = "not (a = a)";
        final JSPredicateForm predicateForm7 = new JSPredicateForm(text7);
        final JSPredicateForm predicateForm8 = new JSPredicateForm(text8);
        final JSSubstitution substitution4 = new JSSubstitution();
        final JSSubstitution match4 = predicateForm7.matches(predicateForm8, substitution4);
        assertTrue(match4.isEmpty());
    }

    @Test
    public void testJSPredicateFormEquals() {
        final String text1 = "not (a = b)";
        final String text2 = "not (b = a)";
        final JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        final JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        assertFalse(predicateForm1.equals(predicateForm2));

        final String text3 = "not (a = a)";
        final String text4 = "not (a = a)";
        final JSPredicateForm predicateForm3 = new JSPredicateForm(text3);
        final JSPredicateForm predicateForm4 = new JSPredicateForm(text4);
        assertTrue(predicateForm3.equals(predicateForm4));

        final String text5 = "not (a = b)";
        final String text6 = "not (c = d)";
        final JSPredicateForm predicateForm5 = new JSPredicateForm(text5);
        final JSPredicateForm predicateForm6 = new JSPredicateForm(text6);
        assertFalse(predicateForm5.equals(predicateForm6));

        final String text7 = "not (a = b)";
        final String text8 = "not (a = b)";
        final JSPredicateForm predicateForm7 = new JSPredicateForm(text7);
        final JSPredicateForm predicateForm8 = new JSPredicateForm(text8);
        assertTrue(predicateForm7.equals(predicateForm8));
    }

    @Test
    public void testJSPredicateFormStandardizer() {
        final String text1 = "not (a = b)";
        final JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        final JSPredicateForm standardized = predicateForm1.standarizerPredicateForm();
        final String expected = "not (not (a = b))";
        assertEquals(expected, standardized.toStr().toString());
        assertEquals(3, standardized.size());
        assertEquals("not", standardized.elementAt(0).toString());
        assertEquals("not", standardized.elementAt(1).toString());
        assertEquals("a = b", standardized.elementAt(2).toString());
    }

    @Test
    public void testJSPredicateFormMatchesWithSubstitution() {
        final String text1 = "not (a = b)";
        final String text2 = "not (c = d)";
        final JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        final JSPredicateForm predicateForm2 = new JSPredicateForm(text2);

        final JSSubstitution substitution = new JSSubstitution();
        final JSSubstitution match = predicateForm1.matches(predicateForm2, substitution);
        assertTrue(match.isEmpty());

        final String text3 = "not (a = b)";
        final String text4 = "not (b = a)";
        final JSPredicateForm predicateForm3 = new JSPredicateForm(text3);
        final JSPredicateForm predicateForm4 = new JSPredicateForm(text4);

        final JSSubstitution substitution2 = new JSSubstitution();
        final JSSubstitution match2 = predicateForm3.matches(predicateForm4, substitution2);
        assertFalse(match2.isEmpty());
        assertEquals(1, match2.size());
        assertEquals("b", match2.getKeyAt(0));
        assertEquals("a", match2.getValueAt(0));

        final String text5 = "not (a = b)";
        final String text6 = "not (a = a)";
        final JSPredicateForm predicateForm5 = new JSPredicateForm(text5);
        final JSPredicateForm predicateForm6 = new JSPredicateForm(text6);

        final JSSubstitution substitution3 = new JSSubstitution();
        final JSSubstitution match3 = predicateForm5.matches(predicateForm6, substitution3);
        assertTrue(match3.isEmpty());

        final String text7 = "not (a = b)";
        final String text8 = "not (b = a)";
        final JSPredicateForm predicateForm7 = new JSPredicateForm(text7);
        final JSPredicateForm predicateForm8 = new JSPredicateForm(text8);

        final JSSubstitution substitution4 = new JSSubstitution();
        final JSSubstitution match4 = predicateForm7.matches(predicateForm8, substitution4);
        assertFalse(match4.isEmpty());
        assertEquals(1, match4.size());
        assertEquals("b", match4.getKeyAt(0));
        assertEquals("a", match4.getValueAt(0));

        final Map<String, String> expectedSubstitution = new HashedMap<>();
        expectedSubstitution.put("a", "b");

        final JSPredicateForm predicateForm9 = new JSPredicateForm("not (a = a)");
        final JSPredicateForm predicateForm10 = new JSPredicateForm("not (b = b)");

        final JSSubstitution substitution5 = new JSSubstitution();
        final JSSubstitution match5 = predicateForm9.matches(predicateForm10, substitution5);
        assertEquals(expectedSubstitution, match5);
    }

    @Test
    public void testJSPredicateFormStandarizer() {
        final String text1 = "not (a = b)";
        final JSPredicateForm predicateForm1 = new JSPredicateForm(text1);
        final JSPredicateForm standarized = predicateForm1.standarizerPredicateForm();
        assertEquals(3, standarized.size());
        assertEquals("not", standarized.elementAt(0).toString());
        assertEquals("not", standarized.elementAt(1).toString());
        assertEquals("a = b", standarized.elementAt(2).toString());

        final String text2 = "not (call f (a = b))";
        final JSPredicateForm predicateForm2 = new JSPredicateForm(text2);
        final JSPredicateForm standarized2 = predicateForm2.standarizerPredicateForm();
        assertEquals(5, standarized2.size());
        assertEquals("not", standarized2.elementAt(0).toString());
        assertEquals("call", standarized2.elementAt(1).toString());
        assertEquals("not", standarized2.elementAt(2).toString());
        assertEquals("f", standarized2.elementAt(3).toString());
        assertEquals("[not (a = b)]", standarized2.elementAt(4).toString());

        final String text3 = "not (not (a = b))";
        final JSPredicateForm predicateForm3 = new JSPredicateForm(text3);
        final JSPredicateForm standarized3 = predicateForm3.standarizerPredicateForm();
        assertEquals(3, standarized3.size());
        assertEquals("not", standarized3.elementAt(0).toString());
        assertEquals("not", standarized3.elementAt(1).toString());
        assertEquals("a = b", standarized3.elementAt(2).toString());

        final String text4 = "not (not (call f (a = b)))";
        final JSPredicateForm predicateForm4 = new JSPredicateForm(text4);
        final JSPredicateForm standarized4 = predicateForm4.standarizerPredicateForm();
        assertEquals(6, standarized4.size());
        assertEquals("not", standarized4.elementAt(0).toString());
        assertEquals("call", standarized4.elementAt(1).toString());
        assertEquals("not", standarized4.elementAt(2).toString());
        assertEquals("f", standarized4.elementAt(3).toString());
        assertEquals("[not (a = b)]", standarized4.elementAt(4).toString());
        assertEquals("not", standarized4.elementAt(5).toString());
    }
}