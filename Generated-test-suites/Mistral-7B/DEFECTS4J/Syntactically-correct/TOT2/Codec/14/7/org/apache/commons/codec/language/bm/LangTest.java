package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for {@link Lang}.
 */
public class LangTest {

    /** The lang instance to test. */
    private static Lang lang;

    /**
     * Sets up the test.
     */
    @BeforeClass
    public static void setup() {
        lang = Lang.instance(NameType.ENGLISH);
    }

    /**
     * Tests the initialization of the lang instance.
     */
    @Test
    public void testInit() {
        assertNotNull(lang);
    }

    /**
     * Tests the guessLanguage method with simple English words.
     */
    @Test
    public void testGuessLanguageSimpleEnglish() {
        List<String> words = Arrays.asList("hello", "world", "apple", "banana", "cat", "dog");
        for (String word : words) {
            String language = lang.guessLanguage(word);
            assertTrue(Collections.singleton(Languages.ENGLISH).contains(language));
        }
    }

    /**
     * Tests the guessLanguages method with simple English words.
     */
    @Test
    public void testGuessLanguagesSimpleEnglish() {
        List<String> words = Arrays.asList("hello", "world", "apple", "banana", "cat", "dog");
        for (String word : words) {
            Languages.LanguageSet languages = lang.guessLanguages(word);
            assertTrue(languages.contains(Languages.ENGLISH));
            assertTrue(languages.size() <= Languages.ENGLISH_LANGUAGES.size());
        }
    }

    /**
     * Tests the guessLanguage method with English words containing special characters.
     */
    @Test
    public void testGuessLanguageEnglishWithSpecialChars() {
        List<String> words = Arrays.asList("hello!", "world’s", "apple#banana", "cat&dog", "déjà vu");
        for (String word : words) {
            String language = lang.guessLanguage(word);
            assertTrue(Collections.singleton(Languages.ENGLISH).contains(language));
        }
    }

    /**
     * Tests the guessLanguages method with English words containing special characters.
     */
    @Test
    public void testGuessLanguagesEnglishWithSpecialChars() {
        List<String> words = Arrays.asList("hello!", "world’s", "apple#banana", "cat&dog", "déjà vu");
        for (String word : words) {
            Languages.LanguageSet languages = lang.guessLanguages(word);
            assertTrue(languages.contains(Languages.ENGLISH));
            assertTrue(languages.size() <= Languages.ENGLISH_LANGUAGES.size());
        }
    }

    /**
     * Tests the guessLanguage method with words in other languages.
     */
    @Test
    public void testGuessLanguageOtherLanguages() {
        List<String> words = Arrays.asList("hola", "bonjour", "merci", "guten tag", "salut", "ciao");
        for (String word : words) {
            String language = lang.guessLanguage(word);
            assertFalse(Collections.singleton(Languages.ENGLISH).contains(language));
            assertTrue(Collections.singleton(Languages.getLanguageName(language)).isPresent());
        }
    }

    /**
     * Tests the guessLanguages method with words in other languages.
     */
    @Test
    public void testGuessLanguagesOtherLanguages() {
        List<String> words = Arrays.asList("hola", "bonjour", "merci", "guten tag", "salut", "ciao");
        for (String word : words) {
            Languages.LanguageSet languages = lang.guessLanguages(word);
            assertTrue(languages.size() > 1);
            assertTrue(languages.containsAll(Languages.ENGLISH_LANGUAGES));
        }
    }

    /**
     * Tests the guessLanguage method with empty and null strings.
     */
    @Test
    public void testGuessLanguageEmptyAndNullStrings() {
        String emptyString = "";
        String nullString = null;
        String language = "";

        // Test with empty string
        language = lang.guessLanguage(emptyString);
        assertEquals(Languages.ANY, language);

        // Test with null string
        language = lang.guessLanguage(nullString);
        assertNull(language);
    }

    /**
     * Tests the guessLanguages method with empty and null strings.
     */
    @Test
    public void testGuessLanguagesEmptyAndNullStrings() {
        String emptyString = "";
        String nullString = null;
        Languages.LanguageSet languages;

        // Test with empty string
        languages = lang.guessLanguages(emptyString);
        assertTrue(languages.isEmpty());

        // Test with null string
        languages = lang.guessLanguages(nullString);
        assertNull(languages);
    }

    /**
     * Tests the guessLanguage method with an empty list.
     */
    @Test
    public void testGuessLanguageEmptyList() {
        List<String> words = Collections.emptyList();
        String language = lang.guessLanguage(StringUtils.join(words, " "));
        assertEquals(Languages.ANY, language);
    }

    /**
     * Tests the guessLanguages method with an empty list.
     */
    @Test
    public void testGuessLanguagesEmptyList() {
        List<String> words = Collections.emptyList();
        Languages.LanguageSet languages = lang.guessLanguages(StringUtils.join(words, " "));
        assertTrue(languages.isEmpty());
    }

    /**
     * Tests the guessLanguage method with an empty rule set.
     */
    @Test
    public void testGuessLanguageEmptyRuleSet() {
        List<LangRule> rules = Collections.emptyList();
        Lang emptyLang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String language = emptyLang.guessLanguage("test");
        assertEquals(Languages.ANY, language);
    }

    /**
     * Tests the guessLanguages method with an empty rule set.
     */
    @Test
    public void testGuessLanguagesEmptyRuleSet() {
        List<LangRule> rules = Collections.emptyList();
        Lang emptyLang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        Languages.LanguageSet languages = emptyLang.guessLanguages("test");
        assertTrue(languages.isEmpty());
    }
}