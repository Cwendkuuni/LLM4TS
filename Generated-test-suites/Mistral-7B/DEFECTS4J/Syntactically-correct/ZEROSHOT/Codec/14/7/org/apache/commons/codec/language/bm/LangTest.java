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
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.lang3.StringUtils;

/**
 * JUnit test file for the Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        assertEquals(Languages.ENGLISH, lang.guessLanguage("apple"));
        assertEquals(Languages.GERMAN, lang.guessLanguage("Apfel"));
        assertEquals(Languages.FRENCH, lang.guessLanguage("pomme"));
        assertEquals(Languages.SPANISH, lang.guessLanguage("manzana"));
        assertEquals(Languages.ITALIAN, lang.guessLanguage("mela"));
        assertEquals(Languages.RUSSIAN, lang.guessLanguage("яблоко"));
        assertEquals(Languages.ANY, lang.guessLanguage("test"));
        assertEquals(Languages.ANY, lang.guessLanguage("12345"));
        assertEquals(Languages.ANY, lang.guessLanguage(""));
        assertEquals(Languages.ANY, lang.guessLanguage(null));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final Set<String> expected = EnumSet.of("en", "de", "fr", "es", "it", "ru");
        final Set<String> actual = lang.guessLanguages("apple");
        assertEquals(expected, actual);

        final Set<String> expected2 = Collections.singleton("ru");
        final Set<String> actual2 = lang.guessLanguages("яблоко");
        assertEquals(expected2, actual2);

        final Set<String> expected3 = Collections.emptySet();
        final Set<String> actual3 = lang.guessLanguages("test");
        assertEquals(expected3, actual3);

        final Set<String> expected4 = Collections.singleton("any");
        final Set<String> actual4 = lang.guessLanguages("12345");
        assertEquals(expected4, actual4);

        final Set<String> expected5 = EnumSet.of("en", "de", "fr", "es", "it", "ru");
        final Set<String> actual5 = lang.guessLanguages("");
        assertEquals(expected5, actual5);

        final Set<String> expected6 = EnumSet.of("en", "de", "fr", "es", "it", "ru");
        final Set<String> actual6 = lang.guessLanguages(null);
        assertEquals(expected6, actual6);
    }

    /**
     * Test method for {@link LangRule#matches(String)}.
     */
    @org.junit.Test
    public void testLangRuleMatches() {
        final String text = "apple";
        final LangRule rule1 = new LangRule(Pattern.compile("^a((p|ple)l{0,1}e)$"), EnumSet.of("en", "de"), true);
        assertTrue(rule1.matches(text));

        final LangRule rule2 = new LangRule(Pattern.compile("^[\\u0410-\\u044F]+$"), EnumSet.of("ru"), true);
        assertFalse(rule2.matches(text));

        final LangRule rule3 = new LangRule(Pattern.compile("^[a-z]+$"), EnumSet.of("en", "fr", "es", "it"), false);
        assertFalse(rule3.matches(text));

        final LangRule rule4 = new LangRule(Pattern.compile("^[\\d]+$"), Collections.singleton("any"), false);
        assertFalse(rule4.matches(text));

        final LangRule rule5 = new LangRule(Pattern.compile("^[\\s]+$"), Collections.singleton("any"), false);
        assertFalse(rule5.matches(text));

        final LangRule rule6 = new LangRule(Pattern.compile("^$"), Collections.singleton("any"), false);
        assertTrue(rule6.matches(StringUtils.EMPTY));

        final LangRule rule7 = new LangRule(Pattern.compile("^(?![\\w\\s]+[\\d]+)[\\w\\s]+$"), Collections.singleton("any"), true);
        assertTrue(rule7.matches("test"));
    }
}