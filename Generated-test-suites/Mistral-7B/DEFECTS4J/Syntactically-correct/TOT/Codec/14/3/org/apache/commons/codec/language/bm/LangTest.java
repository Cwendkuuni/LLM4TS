package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @org.junit.Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.class, englishLang.getClass());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";
        final String englishLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(Languages.ENGLISH, englishLanguage);

        final String frenchWord = "bonjour";
        final String frenchLanguage = englishLang.guessLanguage(frenchWord);
        assertEquals(Languages.FRENCH, frenchLanguage);

        final String germanWord = "guten tag";
        final String germanLanguage = englishLang.guessLanguage(germanWord);
        assertEquals(Languages.GERMAN, germanLanguage);

        final String randomWord = "random";
        final String randomLanguage = englishLang.guessLanguage(randomWord);
        assertEquals(Languages.ANY, randomLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final String englishWord = "hello";
        final Languages.LanguageSet englishLanguages = englishLang.guessLanguages(englishWord);
        final List<String> englishLanguageNames = englishLanguages.getLanguages();
        assertTrue(englishLanguageNames.contains(Languages.ENGLISH.getName()));
        assertFalse(englishLanguageNames.contains(Languages.FRENCH.getName()));
        assertFalse(englishLanguageNames.contains(Languages.GERMAN.getName()));

        final String frenchWord = "bonjour";
        final Languages.LanguageSet frenchLanguages = englishLang.guessLanguages(frenchWord);
        final List<String> frenchLanguageNames = frenchLanguages.getLanguages();
        assertTrue(frenchLanguageNames.contains(Languages.FRENCH.getName()));
        assertFalse(frenchLanguageNames.contains(Languages.ENGLISH.getName()));
        assertFalse(frenchLanguageNames.contains(Languages.GERMAN.getName()));

        final String germanWord = "guten tag";
        final Languages.LanguageSet germanLanguages = englishLang.guessLanguages(germanWord);
        final List<String> germanLanguageNames = germanLanguages.getLanguages();
        assertTrue(germanLanguageNames.contains(Languages.GERMAN.getName()));
        assertFalse(germanLanguageNames.contains(Languages.ENGLISH.getName()));
        assertFalse(germanLanguageNames.contains(Languages.FRENCH.getName()));

        final String randomWord = "random";
        final Languages.LanguageSet randomLanguages = englishLang.guessLanguages(randomWord);
        final List<String> randomLanguageNames = randomLanguages.getLanguages();
        assertTrue(randomLanguageNames.contains(Languages.ANY.getName()));
        assertFalse(randomLanguageNames.contains(Languages.ENGLISH.getName()));
        assertFalse(randomLanguageNames.contains(Languages.FRENCH.getName()));
        assertFalse(randomLanguageNames.contains(Languages.GERMAN.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with multiple matches.
     */
    @org.junit.Test
    public void testGuessLanguagesMultipleMatches() {
        final Lang bmLang = Lang.instance(NameType.BM);

        final String bmWord = "mama";
        final Languages.LanguageSet bmLanguages = bmLang.guessLanguages(bmWord);
        final List<String> bmLanguageNames = bmLanguages.getLanguages();
        assertTrue(bmLanguageNames.contains(Languages.BANGLA.getName()));
        assertTrue(bmLanguageNames.contains(Languages.BELARUSIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.BOSNIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.BRETON.getName()));
        assertTrue(bmLanguageNames.contains(Languages.BULGARIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.CZECH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.DUTCH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.ENGLISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.FARSI.getName()));
        assertTrue(bmLanguageNames.contains(Languages.FINNISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.FRENCH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.GERMAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.GREEK.getName()));
        assertTrue(bmLanguageNames.contains(Languages.HEBREW.getName()));
        assertTrue(bmLanguageNames.contains(Languages.HUNGARIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.INDONESIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.IRISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.ITALIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.JAPANESE.getName()));
        assertTrue(bmLanguageNames.contains(Languages.KOREAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.LATVIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.LITHUANIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.MALAYALAM.getName()));
        assertTrue(bmLanguageNames.contains(Languages.MALTESE.getName()));
        assertTrue(bmLanguageNames.contains(Languages.NORWEGIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.POLISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.PORTUGUESE.getName()));
        assertTrue(bmLanguageNames.contains(Languages.ROMANIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.RUSSIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.SLOVAK.getName()));
        assertTrue(bmLanguageNames.contains(Languages.SLOVENIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.SPANISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.SWEDISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.TAGALOG.getName()));
        assertTrue(bmLanguageNames.contains(Languages.TURKISH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.UKRAINIAN.getName()));
        assertTrue(bmLanguageNames.contains(Languages.VIETNAMESE.getName()));
        assertTrue(bmLanguageNames.contains(Languages.WELSH.getName()));
        assertTrue(bmLanguageNames.contains(Languages.YIDDISH.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with no matches.
     */
    @org.junit.Test
    public void testGuessLanguagesNoMatches() {
        final Lang bmLang = Lang.instance(NameType.BM);

        final String bmWord = "random";
        final Languages.LanguageSet bmLanguages = bmLang.guessLanguages(bmWord);
        final List<String> bmLanguageNames = bmLanguages.getLanguages();
        assertEquals(1, bmLanguageNames.size());
        assertEquals(Languages.ANY.getName(), bmLanguageNames.get(0));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with empty input.
     */
    @org.junit.Test
    public void testGuessLanguagesEmptyInput() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final String emptyInput = "";
        final Languages.LanguageSet englishLanguages = englishLang.guessLanguages(emptyInput);
        final List<String> englishLanguageNames = englishLanguages.getLanguages();
        assertTrue(englishLanguageNames.isEmpty());
    }
}