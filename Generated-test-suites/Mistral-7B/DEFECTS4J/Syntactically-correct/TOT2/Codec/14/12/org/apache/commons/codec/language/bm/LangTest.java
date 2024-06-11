package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Initialize Lang instance before all tests.
     */
    @BeforeClass
    public static void setup() {
        Lang.instance(NameType.ENGLISH);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        String[] words = {"hello", "hola", "bonjour", "guten tag", "salut", "こんにちは"};
        String[] expectedLanguages = {"en", "es", "fr", "de", "fr", "ja"};

        for (int i = 0; i < words.length; i++) {
            String result = Lang.instance(NameType.ENGLISH).guessLanguage(words[i]);
            assertEquals(expectedLanguages[i], result);
        }
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        String word = "hej";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("da", "nb", "nn", "sv"));

        Set<String> result = Lang.instance(NameType.ENGLISH).guessLanguages(word);
        assertEquals(expectedLanguages, result);

        // Test with an empty result
        word = "abcdefg";
        result = Lang.instance(NameType.ENGLISH).guessLanguages(word);
        assertEquals(Collections.emptySet(), result);

        // Test with no match
        word = "你好";
        result = Lang.instance(NameType.ENGLISH).guessLanguages(word);
        assertEquals(Languages.ANY, result);
    }

    /**
     * Test method for handling exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionHandling() {
        Lang.instance(NameType.ENGLISH).guessLanguage(null);
    }

    /**
     * Test method for handling invalid NameType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameType() {
        Lang.instance(NameType.INVALID);
    }
}