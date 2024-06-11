package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test cases for {@link Lang} class.
 *
 * @author John Doe
 */
public class LangTest {

    // Test case for Lang constructor
    @org.junit.Test
    public void testLangInstance() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        assertNotNull(lang);
    }

    // Test case for guessLanguage method with typical usage
    @org.junit.Test
    public void testGuessLanguageTypical() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "Hello World";
        final Languages.LanguageSet expected = Languages.newLanguageSet("en", "en_US");
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguage method with empty input
    @org.junit.Test
    public void testGuessLanguageEmptyInput() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguage method with null input
    @org.junit.Test(expected = NullPointerException.class)
    public void testGuessLanguageNullInput() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = null;
        lang.guessLanguages(word);
    }

    // Test case for guessLanguages method with typical usage and multiple languages
    @org.junit.Test
    public void testGuessLanguagesTypicalMultiple() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "Schöne Grüße";
        final Languages.LanguageSet expected = Languages.newLanguageSet("de", "de_DE");
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with no languages matched
    @org.junit.Test
    public void testGuessLanguagesNoMatch() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "Xyz123";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with multiple rules matched
    @org.junit.Test
    public void testGuessLanguagesMultipleMatches() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "Hej Hej";
        final Languages.LanguageSet expected = Languages.newLanguageSet("da", "da_DK", "no", "no_NO");
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with edge case - long input
    @org.junit.Test
    public void testGuessLanguagesLongInput() {
        final NameType nameType = NameType.EN;
        final Lang lang = Lang.instance(nameType);
        final String word = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with edge case - empty lines in resource file
    @org.junit.Test
    public void testGuessLanguagesEmptyLines() {
        final NameType nameType = NameType.EN;
        final String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_empty_lines.txt";
        final Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(nameType));
        final String word = "Hello World";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with edge case - invalid pattern in resource file
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGuessLanguagesInvalidPattern() {
        final NameType nameType = NameType.EN;
        final String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_invalid_pattern.txt";
        final Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(nameType));
        final String word = "Hello World";
        lang.guessLanguages(word);
    }

    // Test case for guessLanguages method with edge case - comment in resource file
    @org.junit.Test
    public void testGuessLanguagesComment() {
        final NameType nameType = NameType.EN;
        final String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_comment.txt";
        final Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(nameType));
        final String word = "Hello World";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }

    // Test case for guessLanguages method with edge case - multiline comment in resource file
    @org.junit.Test
    public void testGuessLanguagesMultilineComment() {
        final NameType nameType = NameType.EN;
        final String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_multiline_comment.txt";
        final Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(nameType));
        final String word = "Hello World";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }
}