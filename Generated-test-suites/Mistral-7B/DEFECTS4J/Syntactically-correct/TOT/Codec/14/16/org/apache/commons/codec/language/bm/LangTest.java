package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Lang guessing utility.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("hello world"));

        assertEquals(Languages.ANY, englishLang.guessLanguage("hola"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("bonjour"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("pronto"));

        assertEquals(Languages.ANY, englishLang.guessLanguage(""));
        assertEquals(Languages.ANY, englishLang.guessLanguage(" "));
        assertEquals(Languages.ANY, englishLang.guessLanguage(null));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final List<String> expected = Arrays.asList("en", "eng", "english");
        final Languages.LanguageSet english = englishLang.guessLanguages("hello");
        assertEquals(expected, english.getLanguages());

        final List<String> expectedSpanish = Arrays.asList("es", "español");
        final Languages.LanguageSet spanish = englishLang.guessLanguages("hola");
        assertEquals(expectedSpanish, spanish.getLanguages());

        final List<String> empty = Collections.emptyList();
        final Languages.LanguageSet emptyLangs = englishLang.guessLanguages("");
        assertEquals(empty, emptyLangs.getLanguages());

        final List<String> noLanguages = Arrays.asList();
        final Languages.LanguageSet noLangs = englishLang.guessLanguages("nothing");
        assertEquals(noLanguages, noLangs.getLanguages());
    }
}