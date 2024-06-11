package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Lang class.
 */
public class LangTest {

    /**
     * Test for the English language rules.
     */
    @Test
    public void testEnglishLanguageRules() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test with English words
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("goodbye"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("world"));

        // Test with a word that matches multiple languages, English should be preferred
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello world"));

        // Test with a word that doesn't match any language
        assertNull(englishLang.guessLanguage("สวัสดี"));
    }

    /**
     * Test for the German language rules.
     */
    @Test
    public void testGermanLanguageRules() {
        final Lang germanLang = Lang.instance(NameType.GERMAN);

        // Test with German words
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("hallo"));
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("guten Tag"));
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("Welt"));

        // Test with a word that matches multiple languages, German should be preferred
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("hallo Welt"));

        // Test with a word that doesn't match any language
        assertNull(germanLang.guessLanguage("hello"));
    }

    /**
     * Test for the Spanish language rules.
     */
    @Test
    public void testSpanishLanguageRules() {
        final Lang spanishLang = Lang.instance(NameType.SPANISH);

        // Test with Spanish words
        assertEquals(NameType.SPANISH, spanishLang.guessLanguage("hola"));
        assertEquals(NameType.SPANISH, spanishLang.guessLanguage("adios"));
        assertEquals(NameType.SPANISH, spanishLang.guessLanguage("mundo"));

        // Test with a word that matches multiple languages, Spanish should be preferred
        assertEquals(NameType.SPANISH, spanishLang.guessLanguage("hola mundo"));

        // Test with a word that doesn't match any language
        assertNull(spanishLang.guessLanguage("hallo"));
    }

    /**
     * Test for the multi-language support.
     */
    @Test
    public void testMultiLanguageSupport() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final Lang germanLang = Lang.instance(NameType.GERMAN);
        final Lang spanishLang = Lang.instance(NameType.SPANISH);

        // Test with a word that matches multiple languages
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("hallo Welt"));
        assertEquals(NameType.SPANISH, spanishLang.guessLanguage("hola mundo"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello world"));

        // Test with a word that matches only one language, but not the expected one
        assertEquals(NameType.GERMAN, germanLang.guessLanguage("hallo Welt Morgen"));

        // Test with a word that doesn't match any language
        assertNull(englishLang.guessLanguage("hallo Morgen"));
    }

    /**
     * Test for the language guessing when no language is found.
     */
    @Test
    public void testNoLanguageFound() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test with a word that doesn't match any language
        assertNull(englishLang.guessLanguage("สวัสดี"));

        // Test with an empty string
        assertNull(englishLang.guessLanguage(""));

        // Test with a null string
        assertNull(englishLang.guessLanguage(null));
    }
}