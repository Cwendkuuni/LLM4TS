package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test cases for the Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang langEnglish = Lang.instance(NameType.ENGLISH);

        // Test with English words
        assertEquals(Languages.ENGLISH, langEnglish.guessLanguage("apple"));
        assertEquals(Languages.ENGLISH, langEnglish.guessLanguage("banana"));
        assertEquals(Languages.ENGLISH, langEnglish.guessLanguage("computer"));

        // Test with non-English words
        assertEquals(Languages.GERMAN, langEnglish.guessLanguage("Apfel"));
        assertEquals(Languages.SPANISH, langEnglish.guessLanguage("manzana"));
        assertEquals(Languages.FRENCH, langEnglish.guessLanguage("ordinateur"));

        // Test with invalid language codes
        assertEquals(Languages.ANY, langEnglish.guessLanguage(""));
        assertEquals(Languages.ANY, langEnglish.guessLanguage("xxxx"));

        // Test with empty input
        assertEquals(Languages.ANY, langEnglish.guessLanguage(""));

        // Test with special characters
        assertEquals(Languages.GERMAN, langEnglish.guessLanguage("Appelstraße"));
        assertEquals(Languages.DUTCH, langEnglish.guessLanguage("Appelstraat"));

        // Test with multi-word input
        assertEquals(Languages.GERMAN, langEnglish.guessLanguage("Appel Obst"));
        assertEquals(Languages.ENGLISH, langEnglish.guessLanguage("Apple Tree"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang langEnglish = Lang.instance(NameType.ENGLISH);

        // Test with English words
        final List<String> expectedLanguages = Arrays.asList(Languages.ENGLISH.getName());
        final Languages.LanguageSet languagesApple = langEnglish.guessLanguages("apple");
        assertTrue(languagesApple.isSubsetOf(expectedLanguages));

        // Test with non-English words
        final List<String> expectedLanguagesAppel = Arrays.asList(Languages.GERMAN.getName(),
                Languages.DUTCH.getName());
        final Languages.LanguageSet languagesAppel = langEnglish.guessLanguages("Appel");
        assertTrue(languagesAppel.containsAll(expectedLanguagesAppel));

        // Test with invalid input
        final Languages.LanguageSet languagesEmpty = langEnglish.guessLanguages("");
        assertTrue(languagesEmpty.equals(Languages.NO_LANGUAGES));

        // Test with special characters
        final List<String> expectedLanguagesAppelStrasse = Arrays.asList(Languages.GERMAN.getName());
        final Languages.LanguageSet languagesAppelStrasse = langEnglish.guessLanguages("Appel Straße");
        assertTrue(languagesAppelStrasse.containsAll(expectedLanguagesAppelStrasse));

        // Test with multi-word input
        final List<String> expectedLanguagesAppleTree = Arrays.asList(Languages.ENGLISH.getName());
        final Languages.LanguageSet languagesAppleTree = langEnglish.guessLanguages("Apple Tree");
        assertTrue(languagesAppleTree.containsAll(expectedLanguagesAppleTree));
    }

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @org.junit.Test
    public void testInstance() {
        // Test with valid NameTypes
        final Lang langEnglish = Lang.instance(NameType.ENGLISH);
        final Lang langGerman = Lang.instance(NameType.GERMAN);
        final Lang langSpanish = Lang.instance(NameType.SPANISH);

        // Test with invalid NameTypes
        assertFalse(Lang.instance(null) instanceof Lang);
        assertFalse(Lang.instance(new Enum("MyCustomNameType")) instanceof Lang);

        // Test with already initialized Lang instances
        final Lang langEnglishInstance = Lang.instance(NameType.ENGLISH);
        final Lang langEnglishInstance2 = Lang.instance(NameType.ENGLISH);
        assertEquals(langEnglishInstance, langEnglishInstance2);
    }
}