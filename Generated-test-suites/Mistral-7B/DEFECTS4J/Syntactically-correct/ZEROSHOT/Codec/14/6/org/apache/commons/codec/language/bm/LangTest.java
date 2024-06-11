package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    public void testInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
        assertEquals(NameType.ENGLISH, englishLang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English language
        String englishWord = "hello";
        String expectedEnglishLanguage = "en";
        String actualEnglishLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(expectedEnglishLanguage, actualEnglishLanguage);

        // Test other languages
        String frenchWord = "bonjour";
        String expectedFrenchLanguage = "fr";
        String actualFrenchLanguage = englishLang.guessLanguage(frenchWord);
        assertNotEquals(expectedEnglishLanguage, actualFrenchLanguage);
        assertEquals(expectedFrenchLanguage, actualFrenchLanguage);

        // Test no language match
        String randomWord = "RandomWord";
        String expectedNoLanguage = Lang.Languages.ANY.getName();
        String actualNoLanguage = englishLang.guessLanguage(randomWord);
        assertEquals(expectedNoLanguage, actualNoLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English language
        String englishWord = "hello";
        Set<String> expectedEnglishLanguages = Collections.singleton("en");
        Set<String> actualEnglishLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(expectedEnglishLanguages, actualEnglishLanguages);

        // Test other languages
        String frenchWord = "bonjour";
        Set<String> expectedFrenchLanguages = Collections.singleton("fr");
        Set<String> actualFrenchLanguages = englishLang.guessLanguages(frenchWord);
        assertEquals(expectedFrenchLanguages, actualFrenchLanguages);

        // Test multiple languages
        String spanishWord = "hola";
        Set<String> expectedSpanishLanguages = Collections.singleton("es");;
        Set<String> actualSpanishLanguages = englishLang.guessLanguages(spanishWord);
        assertEquals(expectedSpanishLanguages, actualSpanishLanguages);

        Set<String> expectedLanguages = Arrays.asList("en", "fr", "es").asList();
        Set<String> actualLanguages = englishLang.guessLanguages("hello bonjour hola");
        assertEquals(expectedLanguages, actualLanguages);

        // Test no language match
        String randomWord = "RandomWord";
        Set<String> expectedNoLanguages = Collections.singleton(Lang.Languages.ANY.getName());
        Set<String> actualNoLanguages = englishLang.guessLanguages(randomWord);
        assertEquals(expectedNoLanguages, actualNoLanguages);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String, EnumSet)}.
     */
    public void testGuessLanguageWithPriority() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Lang frenchLang = Lang.instance(NameType.FRENCH);

        // Test English language with no priority
        String englishWord = "hello";
        String expectedEnglishLanguage = "en";
        String actualEnglishLanguage = englishLang.guessLanguage(englishWord, null);
        assertEquals(expectedEnglishLanguage, actualEnglishLanguage);

        // Test French language with higher priority
        Set<NameType> priorities = EnumSet.of(NameType.FRENCH);
        String frenchWord = "bonjour";
        String expectedFrenchLanguage = "fr";
        String actualFrenchLanguage = englishLang.guessLanguage(frenchWord, priorities);
        assertEquals(expectedFrenchLanguage, actualFrenchLanguage);

        // Test English language with higher priority
        priorities = EnumSet.of(NameType.ENGLISH);
        String englishWithPriorityWord = "hello";
        String actualEnglishWithPriorityLanguage = englishLang.guessLanguage(frenchWord, priorities);
        assertEquals(expectedEnglishLanguage, actualEnglishWithPriorityLanguage);

        // Test no language match
        String randomWord = "RandomWord";
        String expectedNoLanguage = Lang.Languages.ANY.getName();
        String actualNoLanguage = englishLang.guessLanguage(randomWord, priorities);
        assertEquals(expectedNoLanguage, actualNoLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String, EnumSet)}.
     */
    public void testGuessLanguagesWithPriority() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Lang frenchLang = Lang.instance(NameType.FRENCH);

        // Test English language with no priority
        String englishWord = "hello";
        Set<String> expectedEnglishLanguages = Collections.singleton("en");
        Set<String> actualEnglishLanguages = englishLang.guessLanguages(englishWord, null);
        assertEquals(expectedEnglishLanguages, actualEnglishLanguages);

        // Test French language with higher priority
        Set<NameType> priorities = EnumSet.of(NameType.FRENCH);
        String frenchWord = "bonjour";
        Set<String> expectedFrenchLanguages = Collections.singleton("fr");
        Set<String> actualFrenchLanguages = englishLang.guessLanguages(frenchWord, priorities);
        assertEquals(expectedFrenchLanguages, actualFrenchLanguages);

        Set<String> expectedLanguages = Arrays.asList("en", "fr").asList();
        Set<String> actualLanguages = englishLang.guessLanguages("hello bonjour", priorities);
        assertEquals(expectedLanguages, actualLanguages);

        // Test multiple languages with priorities
        Set<NameType> multiplePriorities = EnumSet.of(NameType.ENGLISH, NameType.FRENCH);
        String englishWithPriorityWord = "hello";
        Set<String> expectedEnglishWithPriorityLanguages = Arrays.asList("en", "fr").asList();
        Set<String> actualEnglishWithPriorityLanguages = englishLang.guessLanguages(frenchWord, multiplePriorities);
        assertEquals(expectedEnglishWithPriorityLanguages, actualEnglishWithPriorityLanguages);

        Set<String> expectedLanguagesWithPriority = Arrays.asList("en", "fr", "es").asList();
        Set<String> actualLanguagesWithPriority = englishLang.guessLanguages("hello bonjour hola", multiplePriorities);
        assertEquals(expectedLanguagesWithPriority, actualLanguagesWithPriority);

        // Test no language match
        String randomWord = "RandomWord";
        Set<String> expectedNoLanguages = Collections.singleton(Lang.Languages.ANY.getName());
        Set<String> actualNoLanguages = englishLang.guessLanguages(randomWord, priorities);
        assertEquals(expectedNoLanguages, actualNoLanguages);
    }
}