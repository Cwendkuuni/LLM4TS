package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)} method.
     */
    @org.junit.Test
    public void testInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, englishLang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English language
        String englishWord = "hello";
        String englishLanguage = englishLang.guessLanguage(englishWord);
        assertTrue(englishLanguage.equals(Languages.ENGLISH));

        // Test other languages
        String spanishWord = "hola";
        String spanishLanguage = englishLang.guessLanguage(spanishWord);
        assertFalse(spanishLanguage.equals(Languages.ENGLISH));
        assertTrue(spanishLanguage.equals(Languages.SPANISH));

        String russianWord = "привет";
        String russianLanguage = englishLang.guessLanguage(russianWord);
        assertFalse(russianLanguage.equals(Languages.ENGLISH));
        assertTrue(russianLanguage.equals(Languages.RUSSIAN));

        String germanWord = "hallo";
        String germanLanguage = englishLang.guessLanguage(germanWord);
        assertFalse(germanLanguage.equals(Languages.ENGLISH));
        assertTrue(germanLanguage.equals(Languages.GERMAN));

        // Test no match
        String frenchWord = "bonjour";
        String frenchLanguage = englishLang.guessLanguage(frenchWord);
        assertFalse(frenchLanguage.equals(Languages.ENGLISH));
        assertFalse(frenchLanguage.equals(Languages.SPANISH));
        assertFalse(frenchLanguage.equals(Languages.RUSSIAN));
        assertFalse(frenchLanguage.equals(Languages.GERMAN));
        assertTrue(frenchLanguage.equals(Languages.FRENCH));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} method.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English language
        String englishWord = "hello";
        Set<String> englishLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(Arrays.asList(Languages.ENGLISH), englishLanguages);

        // Test other languages
        String spanishWord = "hola";
        Set<String> spanishLanguages = englishLang.guessLanguages(spanishWord);
        assertEquals(Arrays.asList(Languages.SPANISH), spanishLanguages);

        String russianWord = "привет";
        Set<String> russianLanguages = englishLang.guessLanguages(russianWord);
        assertEquals(Arrays.asList(Languages.RUSSIAN), russianLanguages);

        String germanWord = "hallo";
        Set<String> germanLanguages = englishLang.guessLanguages(germanWord);
        assertEquals(Arrays.asList(Languages.GERMAN), germanLanguages);

        // Test multiple languages
        String frenchWord = "bonjour";
        Set<String> frenchLanguages = englishLang.guessLanguages(frenchWord);
        assertEquals(EnumSet.of(Languages.FRENCH), frenchLanguages);

        // Test no match
        String chineseWord = "你好";
        Set<String> chineseLanguages = englishLang.guessLanguages(chineseWord);
        assertTrue(chineseLanguages.isEmpty());
    }

    /**
     * Test method for {@link Lang#loadFromResource(String, Languages)} method.
     */
    @org.junit.Test
    public void testLoadFromResource() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Languages englishLanguages = Languages.getInstance(NameType.ENGLISH);
        Lang loadedEnglishLang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, englishLanguages);

        // Check loaded Lang instance has the same rules and languages as the static one
        assertEquals(englishLang.getNameType(), loadedEnglishLang.getNameType());
        assertEquals(englishLang.rules, loadedEnglishLang.rules);
        assertEquals(englishLanguages, loadedEnglishLang.languages);
    }
}