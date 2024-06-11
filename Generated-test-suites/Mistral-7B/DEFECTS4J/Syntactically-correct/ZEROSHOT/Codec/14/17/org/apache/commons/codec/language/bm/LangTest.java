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

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        // Test with empty input
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));

        // Test with English words
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("world"));
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("java"));
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("programming"));

        // Test with non-English words
        assertEquals(Lang.Languages.FRENCH, lang.guessLanguage("bonjour"));
        assertEquals(Lang.Languages.GERMAN, lang.guessLanguage("guten Tag"));
        assertEquals(Lang.Languages.SPANISH, lang.guessLanguage("hola"));
        assertEquals(Lang.Languages.ITALIAN, lang.guessLanguage("ciao"));

        // Test with ambiguous words
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("hello")); // Could be English or Dutch
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("guten")); // Could be German or Dutch
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("ciao")); // Could be Italian or Spanish
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("hola")); // Could be Spanish or Portuguese

        // Test with incorrect words
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("asdf"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("1234"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("!"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        // Test with empty input
        assertEquals(Collections.emptySet(), lang.guessLanguages(""));

        // Test with English words
        assertEquals(EnumSet.of(Lang.Languages.ENGLISH), lang.guessLanguages("hello"));
        assertEquals(EnumSet.of(Lang.Languages.ENGLISH), lang.guessLanguages("world"));
        assertEquals(EnumSet.of(Lang.Languages.ENGLISH), lang.guessLanguages("java"));
        assertEquals(EnumSet.of(Lang.Languages.ENGLISH), lang.guessLanguages("programming"));

        // Test with non-English words
        assertEquals(EnumSet.of(Lang.Languages.FRENCH), lang.guessLanguages("bonjour"));
        assertEquals(EnumSet.of(Lang.Languages.GERMAN), lang.guessLanguages("guten Tag"));
        assertEquals(EnumSet.of(Lang.Languages.SPANISH), lang.guessLanguages("hola"));
        assertEquals(EnumSet.of(Lang.Languages.ITALIAN), lang.guessLanguages("ciao"));

        // Test with ambiguous words
        assertEquals(EnumSet.of(Lang.Languages.DUTCH, Lang.Languages.ENGLISH), lang.guessLanguages("hello"));
        assertEquals(EnumSet.of(Lang.Languages.GERMAN, Lang.Languages.DUTCH), lang.guessLanguages("guten"));
        assertEquals(EnumSet.of(Lang.Languages.ITALIAN, Lang.Languages.SPANISH), lang.guessLanguages("ciao"));
        assertEquals(EnumSet.of(Lang.Languages.SPANISH, Lang.Languages.PORTUGUESE), lang.guessLanguages("hola"));

        // Test with incorrect words
        assertEquals(Collections.emptySet(), lang.guessLanguages("asdf"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("1234"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("!"));

        // Test with symbols
        assertEquals(Collections.emptySet(), lang.guessLanguages("!"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("@"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("#"));

        // Test with numbers
        assertEquals(Collections.emptySet(), lang.guessLanguages("1234"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("12345"));
        assertEquals(Collections.emptySet(), lang.guessLanguages("0123456789"));

        // Test with empty lines and comments
        assertEquals(Collections.emptySet(), lang.guessLanguages(""));
        assertEquals(Collections.emptySet(), lang.guessLanguages("// this is a comment"));
        assertEquals(Collections.emptySet(), lang.guessLanguages(""));
    }

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @org.junit.Test
    public void testInstance() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final Lang french = Lang.instance(NameType.FRENCH);
        final Lang german = Lang.instance(NameType.GERMAN);
        final Lang spanish = Lang.instance(NameType.SPANISH);
        final Lang italian = Lang.instance(NameType.ITALIAN);

        // Test with null NameType
        assertFalse(Lang.instance(null) instanceof Lang);

        // Test with valid NameType
        assertTrue(english instanceof Lang);
        assertTrue(french instanceof Lang);
        assertTrue(german instanceof Lang);
        assertTrue(spanish instanceof Lang);
        assertTrue(italian instanceof Lang);

        // Test with different NameTypes
        assertFalse(english.equals(french));
        assertFalse(english.equals(german));
        assertFalse(english.equals(spanish));
        assertFalse(english.equals(italian));
    }
}