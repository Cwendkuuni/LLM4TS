package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * JUnit test file for the Lang class.
 *
 */
public class LangTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEnglish);
    }

    /**
     * Test guessLanguage method with English rules.
     */
    @org.junit.Test
    public void testGuessLanguageEnglish() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String text1 = "island";
        String expected = Languages.ENGLISH.getName();
        String result = langEnglish.guessLanguage(text1);
        assertEquals(expected, result);

        String text2 = "Hallo";
        expected = Languages.GERMAN.getName();
        result = langEnglish.guessLanguage(text2);
        assertFalse(Languages.areEqual(expected, result));

        String text3 = "Hello";
        expected = Languages.ENGLISH.getName();
        result = langEnglish.guessLanguage(text3);
        assertEquals(expected, result);

        String text4 = "bonjour";
        expected = Languages.FRENCH.getName();
        result = langEnglish.guessLanguage(text4);
        assertEquals(expected, result);

        String text5 = "こんにちは";
        expected = Languages.JAPANESE.getName();
        result = langEnglish.guessLanguage(text5);
        assertFalse(Languages.areEqual(expected, result));
    }

    /**
     * Test guessLanguages method with English rules.
     */
    @org.junit.Test
    public void testGuessLanguagesEnglish() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String text1 = "island";
        Set<String> expected = EnumSet.of(Languages.ENGLISH.getName());
        Set<String> result = langEnglish.guessLanguages(text1);
        assertEquals(expected, result);

        String text2 = "Hallo";
        Set<String> empty = Collections.emptySet();
        result = langEnglish.guessLanguages(text2);
        assertEquals(empty, result);

        String text3 = "Hello";
        expected = EnumSet.of(Languages.ENGLISH.getName());
        result = langEnglish.guessLanguages(text3);
        assertEquals(expected, result);

        String text4 = "bonjour";
        Set<String> french = EnumSet.of(Languages.FRENCH.getName());
        result = langEnglish.guessLanguages(text4);
        assertTrue(french.containsAll(result));

        String text5 = "こんにちは";
        empty = Collections.emptySet();
        result = langEnglish.guessLanguages(text5);
        assertEquals(empty, result);
    }

    /**
     * Test guessLanguage method with German rules.
     */
    @org.junit.Test
    public void testGuessLanguageGerman() {
        Lang langGerman = Lang.instance(NameType.GERMAN);

        String text1 = "island";
        String result = langGerman.guessLanguage(text1);
        assertFalse(Languages.areEqual(Languages.GERMAN.getName(), result));

        String text2 = "Hallo";
        String expected = Languages.GERMAN.getName();
        result = langGerman.guessLanguage(text2);
        assertEquals(expected, result);

        String text3 = "Hello";
        result = langGerman.guessLanguage(text3);
        assertFalse(Languages.areEqual(Languages.GERMAN.getName(), result));

        String text4 = "bonjour";
        result = langGerman.guessLanguage(text4);
        assertFalse(Languages.areEqual(Languages.GERMAN.getName(), result));

        String text5 = "こんにちは";
        result = langGerman.guessLanguage(text5);
        assertFalse(Languages.areEqual(Languages.GERMAN.getName(), result));

        String text6 = "Guten Tag";
        String expected = Languages.GERMAN.getName();
        result = langGerman.guessLanguage(text6);
        assertEquals(expected, result);
    }

    /**
     * Test guessLanguages method with German rules.
     */
    @org.junit.Test
    public void testGuessLanguagesGerman() {
        Lang langGerman = Lang.instance(NameType.GERMAN);

        String text1 = "island";
        Set<String> empty = Collections.emptySet();
        Set<String> result = langGerman.guessLanguages(text1);
        assertEquals(empty, result);

        String text2 = "Hallo";
        Set<String> expected = EnumSet.of(Languages.GERMAN.getName());
        Set<String> resultSet = langGerman.guessLanguages(text2);
        assertTrue(expected.containsAll(resultSet));

        String text3 = "Hello";
        empty = Collections.emptySet();
        result = langGerman.guessLanguages(text3);
        assertEquals(empty, result);

        String text4 = "bonjour";
        empty = Collections.emptySet();
        result = langGerman.guessLanguages(text4);
        assertEquals(empty, result);

        String text5 = "こんにちは";
        empty = Collections.emptySet();
        result = langGerman.guessLanguages(text5);
        assertEquals(empty, result);

        String text6 = "Guten Tag";
        Set<String> german = EnumSet.of(Languages.GERMAN.getName());
        result = langGerman.guessLanguages(text6);
        assertTrue(german.containsAll(result));
    }

    /**
     * Test guessLanguage method with French rules.
     */
    @org.junit.Test
    public void testGuessLanguageFrench() {
        Lang langFrench = Lang.instance(NameType.FRENCH);

        String text1 = "island";
        String result = langFrench.guessLanguage(text1);
        assertFalse(Languages.areEqual(Languages.FRENCH.getName(), result));

        String text2 = "Hallo";
        result = langFrench.guessLanguage(text2);
        assertFalse(Languages.areEqual(Languages.FRENCH.getName(), result));

        String text3 = "Hello";
        result = langFrench.guessLanguage(text3);
        assertFalse(Languages.areEqual(Languages.FRENCH.getName(), result));

        String text4 = "islande";
        String expected = Languages.FRENCH.getName();
        result = langFrench.guessLanguage(text4);
        assertEquals(expected, result);

        String text5 = "bonjour";
        expected = Languages.FRENCH.getName();
        result = langFrench.guessLanguage(text5);
        assertEquals(expected, result);

        String text6 = "こんにちは";
        result = langFrench.guessLanguage(text6);
        assertFalse(Languages.areEqual(Languages.FRENCH.getName(), result));
    }

    /**
     * Test guessLanguages method with French rules.
     */
    @org.junit.Test
    public void testGuessLanguagesFrench() {
        Lang langFrench = Lang.instance(NameType.FRENCH);

        String text1 = "island";
        Set<String> empty = Collections.emptySet();
        Set<String> result = langFrench.guessLanguages(text1);
        assertEquals(empty, result);

        String text2 = "Hallo";
        Set<String> empty2 = Collections.emptySet();
        result = langFrench.guessLanguages(text2);
        assertEquals(empty2, result);

        String text3 = "Hello";
        empty = Collections.emptySet();
        result = langFrench.guessLanguages(text3);
        assertEquals(empty, result);

        String text4 = "islande";
        Set<String> expected = EnumSet.of(Languages.FRENCH.getName());
        Set<String> resultSet = langFrench.guessLanguages(text4);
        assertTrue(expected.containsAll(resultSet));

        String text5 = "bonjour";
        Set<String> french = EnumSet.of(Languages.FRENCH.getName());
        result = langFrench.guessLanguages(text5);
        assertTrue(french.containsAll(result));

        String text6 = "こんにちは";
        empty = Collections.emptySet();
        result = langFrench.guessLanguages(text6);
        assertEquals(empty, result);
    }

    /**
     * Test guessLanguage method with Japanese rules.
     */
    @org.junit.Test
    public void testGuessLanguageJapanese() {
        Lang langJapanese = Lang.instance(NameType.JAPANESE);

        String text1 = "island";
        String result = langJapanese.guessLanguage(text1);
        assertFalse(Languages.areEqual(Languages.JAPANESE.getName(), result));

        String text2 = "Hallo";
        result = langJapanese.guessLanguage(text2);
        assertFalse(Languages.areEqual(Languages.JAPANESE.getName(), result));

        String text3 = "Hello";
        result = langJapanese.guessLanguage(text3);
        assertFalse(Languages.areEqual(Languages.JAPANESE.getName(), result));

        String text4 = "island";
        result = langJapanese.guessLanguage(text4);
        assertFalse(Languages.areEqual(Languages.JAPANESE.getName(), result));

        String text5 = "こんにちは";
        String expected = Languages.JAPANESE.getName();
        result = langJapanese.guessLanguage(text5);
        assertEquals(expected, result);

        String text6 = "bonjour";
        result = langJapanese.guessLanguage(text6);
        assertFalse(Languages.areEqual(Languages.JAPANESE.getName(), result));
    }

    /**
     * Test guessLanguages method with Japanese rules.
     */
    @org.junit.Test
    public void testGuessLanguagesJapanese() {
        Lang langJapanese = Lang.instance(NameType.JAPANESE);

        String text1 = "island";
        Set<String> empty = Collections.emptySet();
        Set<String> result = langJapanese.guessLanguages(text1);
        assertEquals(empty, result);

        String text2 = "Hallo";
        Set<String> empty2 = Collections.emptySet();
        result = langJapanese.guessLanguages(text2);
        assertEquals(empty2, result);

        String text3 = "Hello";
        empty = Collections.emptySet();
        result = langJapanese.guessLanguages(text3);
        assertEquals(empty, result);

        String text4 = "island";
        Set<String> empty3 = Collections.emptySet();
        result = langJapanese.guessLanguages(text4);
        assertEquals(empty3, result);

        String text5 = "こんにちは";
        Set<String> japanese = EnumSet.of(Languages.JAPANESE.getName());
        Set<String> resultSet = langJapanese.guessLanguages(text5);
        assertTrue(japanese.containsAll(resultSet));

        String text6 = "bonjour";
        Set<String> empty4 = Collections.emptySet();
        result = langJapanese.guessLanguages(text6);
        assertEquals(empty4, result);
    }
}