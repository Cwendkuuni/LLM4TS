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
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.java
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)} and {@link Lang#guessLanguages(String)}
     */
    @org.junit.Test
    public void testGuessLanguageAndLanguages() {
        // Test with English words
        Lang englishLang = Lang.instance(Lang.NameType.ENGLISH);
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("hello"));
        Set<String> englishLanguages = englishLang.guessLanguages("hello");
        assertTrue(englishLanguages.contains(Lang.Languages.ENGLISH.getName()));
        assertTrue(englishLanguages.isEmpty() || englishLanguages.contains(Lang.Languages.ENGLISH.getName()));

        // Test with non-English words
        Lang frenchLang = Lang.instance(Lang.NameType.FRENCH);
        assertEquals(Lang.Languages.FRENCH, frenchLang.guessLanguage("bonjour"));
        Set<String> frenchLanguages = frenchLang.guessLanguages("bonjour");
        assertTrue(frenchLanguages.contains(Lang.Languages.FRENCH.getName()));
        assertTrue(frenchLanguages.isEmpty() || frenchLanguages.contains(Lang.Languages.FRENCH.getName()));

        // Test with ambiguous words
        Lang spanishLang = Lang.instance(Lang.NameType.SPANISH);
        Set<String> spanishLanguages = spanishLang.guessLanguages("hola");
        assertTrue(spanishLanguages.contains(Lang.Languages.SPANISH.getName()));
        assertTrue(spanishLanguages.contains(Lang.Languages.ITALIAN.getName()));
        assertFalse(spanishLanguages.contains(Lang.Languages.ENGLISH.getName()));

        // Test with invalid words
        Lang invalidLang = Lang.instance(Lang.NameType.INVALID);
        Set<String> invalidLanguages = invalidLang.guessLanguages("test");
        assertEquals(Lang.Languages.ANY, invalidLang.guessLanguage("test"));
        assertTrue(invalidLanguages.isEmpty());
    }

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @org.junit.Test
    public void testInstance() {
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(
                Lang.Languages.ENGLISH.getName(),
                Lang.Languages.FRENCH.getName(),
                Lang.Languages.GERMAN.getName(),
                Lang.Languages.ITALIAN.getName(),
                Lang.Languages.SPANISH.getName()));

        for (Lang.NameType nameType : Lang.NameType.values()) {
            Lang lang = Lang.instance(nameType);
            Set<String> languages = lang.guessLanguages("test");
            assertEquals(expectedLanguages, languages);
        }
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)} constructor
     */
    @org.junit.Test
    public void testLangConstructor() {
        List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^[aeiou]([aeiou]*[bcdfghjklmnpqrstvwxyz]+[aeiou])?$"),
                        new HashSet<String>(Collections.singletonList(Lang.Languages.ENGLISH.getName())), true),
                new LangRule(Pattern.compile("^[aeiou]([aeiou]*[bcdfghjklmnpqrstvwxyz]+[aeiou])?$"),
                        new HashSet<String>(Collections.singletonList(Lang.Languages.FRENCH.getName())), false),
                new LangRule(Pattern.compile("^[aeiou]([aeiou]*[bcdfghjklmnpqrstvwxyz]+[aeiou])?$"),
                        new HashSet<String>(Collections.singletonList(Lang.Languages.GERMAN.getName())), false),
                new LangRule(Pattern.compile("^[aeiou]([aeiou]*[bcdfghjklmnpqrstvwxyz]+[aeiou])?$"),
                        new HashSet<String>(Collections.singletonList(Lang.Languages.ITALIAN.getName())), false),
                new LangRule(Pattern.compile("^[aeiou]([aeiou]*[bcdfghjklmnpqrstvwxyz]+[aeiou])?$"),
                        new HashSet<String>(Collections.singletonList(Lang.Languages.SPANISH.getName())), true));

        Languages languages = new Languages(EnumSet.allOf(Lang.Languages.class));
        Lang lang = new Lang(rules, languages);

        Set<String> englishLanguages = lang.guessLanguages("hello");
        assertTrue(englishLanguages.contains(Lang.Languages.ENGLISH.getName()));
        assertTrue(englishLanguages.isEmpty() || englishLanguages.contains(Lang.Languages.ENGLISH.getName()));

        Set<String> frenchLanguages = lang.guessLanguages("bonjour");
        assertTrue(frenchLanguages.contains(Lang.Languages.FRENCH.getName()));
        assertTrue(frenchLanguages.isEmpty() || frenchLanguages.contains(Lang.Languages.FRENCH.getName()));

        Set<String> germanLanguages = lang.guessLanguages("hallo");
        assertTrue(germanLanguages.contains(Lang.Languages.GERMAN.getName()));
        assertTrue(germanLanguages.isEmpty() || germanLanguages.contains(Lang.Languages.GERMAN.getName()));

        Set<String> italianLanguages = lang.guessLanguages("ciao");
        assertTrue(italianLanguages.contains(Lang.Languages.ITALIAN.getName()));
        assertTrue(italianLanguages.isEmpty() || italianLanguages.contains(Lang.Languages.ITALIAN.getName()));

        Set<String> spanishLanguages = lang.guessLanguages("hola");
        assertTrue(spanishLanguages.contains(Lang.Languages.SPANISH.getName()));
        assertTrue(spanishLanguages.contains(Lang.Languages.ITALIAN.getName()));
        assertFalse(spanishLanguages.contains(Lang.Languages.ENGLISH.getName()));
    }
}