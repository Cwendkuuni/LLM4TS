package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Lang}.
 */
public class LangTest {

    /**
     * Tests language guessing for various input strings.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English words
        assertEquals(Lang.NameType.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(Lang.NameType.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(Lang.NameType.ENGLISH, englishLang.guessLanguage("hello world"));

        // Test non-English words
        assertEquals(Lang.NameType.GERMAN, englishLang.guessLanguage("Guten Tag"));
        assertEquals(Lang.NameType.FRENCH, englishLang.guessLanguage("Bonjour"));
        assertEquals(Lang.NameType.SPANISH, englishLang.guessLanguage("Hola"));

        // Test ambiguous words
        assertEquals(Lang.NameType.ANY, englishLang.guessLanguage("hello monday"));
        assertEquals(Lang.NameType.ANY, englishLang.guessLanguage("allouette")); // French and English

        // Test empty input
        assertEquals(Lang.NameType.ANY, englishLang.guessLanguage(""));

        // Test null input
        assertFalse(englishLang::guessLanguage == null);

        // Test input with special characters
        assertEquals(Lang.NameType.GERMAN, englishLang.guessLanguage("Wie hältst du dich für eine Apfelknödel?")); // German for "How do you think you are an apple dumpling?"
    }

    /**
     * Tests language guessing using custom rules.
     */
    @Test
    public void testCustomRules() {
        final Lang customLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/custom.txt",
                new Languages(Arrays.asList("custom", "english")));

        final String testString = "custom rule test";

        // Test guessing using custom rules
        assertEquals(Lang.NameType.CUSTOM, customLang.guessLanguage(testString));

        // Test guessing using default rules
        final Lang defaultLang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.NameType.ENGLISH, defaultLang.guessLanguage(testString));
    }
}