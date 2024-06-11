package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for Lang class
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        Lang langGerman = Lang.instance(NameType.GERMAN);

        assertEquals(Lang.class, langEnglish.getClass());
        assertEquals(Lang.class, langGerman.getClass());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        Lang langGerman = Lang.instance(NameType.GERMAN);

        assertEquals(Languages.ENGLISH, langEnglish.guessLanguage("hello"));
        assertEquals(Languages.GERMAN, langGerman.guessLanguage("guten tag"));
        assertEquals(Languages.ANY, langEnglish.guessLanguage("hola"));
        assertEquals(Languages.ANY, langGerman.guessLanguage("world"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        Lang langGerman = Lang.instance(NameType.GERMAN);

        List<String> expectedLanguagesEnglish = Arrays.asList("english", "eng", "en");
        List<String> expectedLanguagesGerman = Arrays.asList("german", "de");

        Set<String> languagesEnglish = langEnglish.guessLanguages("hello").getLanguages();
        Set<String> languagesGerman = langGerman.guessLanguages("guten tag").getLanguages();

        assertEquals(expectedLanguagesEnglish, languagesEnglish);
        assertEquals(expectedLanguagesGerman, languagesGerman);

        Set<String> languagesEnglishEmpty = langEnglish.guessLanguages("").getLanguages();
        Set<String> languagesGermanEmpty = langGerman.guessLanguages("").getLanguages();

        assertTrue(languagesEnglishEmpty.isEmpty());
        assertTrue(languagesGermanEmpty.isEmpty());

        Set<String> languagesEnglishFalse = langEnglish.guessLanguages("hola").getLanguages();
        Set<String> languagesGermanFalse = langGerman.guessLanguages("world").getLanguages();

        assertFalse(languagesEnglishFalse.containsAll(expectedLanguagesEnglish));
        assertFalse(languagesGermanFalse.containsAll(expectedLanguagesGerman));
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)}.
     */
    @Test
    public void testLang() {
        NameType english = NameType.ENGLISH;
        NameType german = NameType.GERMAN;

        Set<String> englishLanguages = new HashSet<String>(Arrays.asList("english", "eng", "en"));
        Set<String> germanLanguages = new HashSet<String>(Arrays.asList("german", "de"));

        List<LangRule> rulesEnglish = Arrays.asList(
                new LangRule(Pattern.compile("^h.*$"), englishLanguages, true),
                new LangRule(Pattern.compile("^he.*$"), englishLanguages, true),
                new LangRule(Pattern.compile("^hi.*$"), Collections.emptySet(), false),
                new LangRule(Pattern.compile("^wo.*$"), Collections.emptySet(), false)
                );

        List<LangRule> rulesGerman = Arrays.asList(
                new LangRule(Pattern.compile("^g.*tag$"), germanLanguages, true),
                new LangRule(Pattern.compile("^g.*e.*n.*d.*$"), germanLanguages, true),
                new LangRule(Pattern.compile("^h.*$"), Collections.emptySet(), false),
                new LangRule(Pattern.compile("^wo.*$"), Collections.emptySet(), false)
                );

        Languages englishLanguagesInstance = new Languages(Collections.singletonMap(english, englishLanguages));
        Languages germanLanguagesInstance = new Languages(Collections.singletonMap(german, germanLanguages));

        Lang langEnglish = new Lang(rulesEnglish, englishLanguagesInstance);
        Lang langGerman = new Lang(rulesGerman, germanLanguagesInstance);

        assertEquals(rulesEnglish, langEnglish.getRules());
        assertEquals(rulesGerman, langGerman.getRules());
        assertEquals(englishLanguagesInstance, langEnglish.getLanguages());
        assertEquals(germanLanguagesInstance, langGerman.getLanguages());
    }
}