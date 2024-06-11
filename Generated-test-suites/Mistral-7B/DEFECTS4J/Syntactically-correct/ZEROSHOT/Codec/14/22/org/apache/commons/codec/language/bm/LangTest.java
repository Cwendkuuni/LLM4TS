package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.java
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "Morgen";
        final String expectedLanguage = "de";

        final String result = lang.guessLanguage(word);
        assertTrue(result.equals(expectedLanguage) || result.equals(Languages.ANY.toString()));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "Morgen";

        final Set<String> expectedLanguages = EnumSet.of("de");
        final Set<String> result = lang.guessLanguages(word);

        assertEquals(expectedLanguages, result);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an unknown word
     */
    @org.junit.Test
    public void testGuessLanguageUnknownWord() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "unknownWord";

        final String result = lang.guessLanguage(word);
        assertEquals(Languages.ANY.toString(), result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an unknown word
     */
    @org.junit.Test
    public void testGuessLanguagesUnknownWord() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "unknownWord";

        final Set<String> result = lang.guessLanguages(word);
        assertEquals(Collections.emptySet(), result);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an empty word
     */
    @org.junit.Test
    public void testGuessLanguageEmptyWord() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "";

        final String result = lang.guessLanguage(word);
        assertEquals(Languages.ANY.toString(), result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an empty word
     */
    @org.junit.Test
    public void testGuessLanguagesEmptyWord() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String word = "";

        final Set<String> result = lang.guessLanguages(word);
        assertEquals(Collections.emptySet(), result);
    }

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @org.junit.Test
    public void testInstance() {
        final Lang germanInstance = Lang.instance(NameType.GERMAN);
        final Lang englishInstance = Lang.instance(NameType.ENGLISH);

        assertEquals(germanInstance.getClass(), englishInstance.getClass());
        assertFalse(germanInstance.equals(englishInstance));
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)}
     */
    @org.junit.Test
    public void testLangConstructor() {
        final List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile("^Morgen$"), EnumSet.of("de"), true),
                new LangRule(Pattern.compile("^Morgen.*sonntag$"), EnumSet.of("de", "de_AT"), true),
                new LangRule(Pattern.compile("^Mor.*$"), EnumSet.of("en"), false));
        final Languages languages = new Languages(Arrays.asList("de", "de_AT", "en"));

        final Lang lang = new Lang(rules, languages);

        assertEquals(rules, lang.getRules());
        assertEquals(languages, lang.getLanguages());
    }
}