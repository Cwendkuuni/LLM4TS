package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Test case for {@link LookupTranslator} class
 *
 * @author John Doe
 */
public class LookupTranslatorTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][] { { "one", "uno" }, { "two", "dos" }, { "three", "tres" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(3, translator.shortest);
        assertEquals(3, translator.longest);
    }

    /**
     * Test translate method with valid input
     */
    @Test
    public void testTranslateValidInput() {
        CharSequence[][] lookup = new CharSequence[][] { { "one", "uno" }, { "two", "dos" }, { "three", "tres" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        // test with valid input sequence
        int index = 0;
        translator.translate(new String("onetwo"), index, out);
        assertEquals("unoDos", out.toString());
        index += 3;

        translator.translate(new String("onethree"), index, out);
        assertEquals("unoTres", out.toString());
        index += 4;

        translator.translate(new String("twoone"), index, out);
        assertEquals("dosUno", out.toString());
        index += 3;

        // test with empty input
        translator.translate(new String(""), index, out);
        assertNull(out.toString());
    }

    /**
     * Test translate method with invalid input
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateInvalidInput() {
        CharSequence[][] lookup = new CharSequence[][] { { "one", "uno" }, { "two", "dos" }, { "three", "tres" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        // test with invalid input sequence length
        int index = 0;
        translator.translate(new String("onetwothreefour"), index, out);
    }
}