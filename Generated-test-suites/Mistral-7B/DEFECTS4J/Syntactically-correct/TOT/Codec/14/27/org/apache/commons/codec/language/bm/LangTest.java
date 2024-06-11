package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;
import static org.apache.commons.codec.language.bm.Lang.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for Lang utility.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);
        assertNotNull(englishLang);
        assertEquals(english, englishLang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        // English words
        final String englishWord1 = "hello";
        final String englishWord2 = "world";
        assertEquals(english, englishLang.guessLanguage(englishWord1));
        assertEquals(english, englishLang.guessLanguage(englishWord2));

        // Non-English words
        final String frenchWord1 = "bonjour";
        final String spanishWord1 = "hola";
        final String germanWord1 = "guten tag";
        final String chineseWord1 = "你好";

        // English words with non-English characters
        final String englishWordWithNonEngChar = "Hello, 世界!";

        final NameType french = NameType.FRENCH;
        final NameType spanish = NameType.SPANISH;
        final NameType german = NameType.GERMAN;
        final NameType chinese = NameType.CHINESE;

        final Lang frenchLang = Lang.instance(french);
        final Lang spanishLang = Lang.instance(spanish);
        final Lang germanLang = Lang.instance(german);
        final Lang chineseLang = Lang.instance(chinese);

        // Test guessing languages for non-English words
        assertEquals(french, frenchLang.guessLanguage(frenchWord1));
        assertEquals(spanish, spanishLang.guessLanguage(spanishWord1));
        assertEquals(german, germanLang.guessLanguage(germanWord1));
        assertEquals(chinese, chineseLang.guessLanguage(chineseWord1));

        // Test guessing languages for English words with non-English characters
        assertEquals(english, englishLang.guessLanguage(englishWordWithNonEngChar));

        // Test guessing languages for invalid words
        assertEquals(Languages.ANY, Lang.instance(NameType.INVALID).guessLanguage(""));
        assertEquals(Languages.ANY, Lang.instance(NameType.INVALID).guessLanguage(null));
        assertEquals(Languages.ANY, Lang.instance(NameType.INVALID).guessLanguage(" "));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        // English words
        final String englishWord1 = "hello";
        final String englishWord2 = "world";

        // Non-English words
        final String frenchWord1 = "bonjour";
        final String spanishWord1 = "hola";
        final String germanWord1 = "guten tag";
        final String chineseWord1 = "你好";

        // English words with non-English characters
        final String englishWordWithNonEngChar = "Hello, 世界!";

        // Test guessing languages for English words
        final List<String> expectedLanguagesForEnglishWord1 = Arrays.asList("en");
        final List<String> expectedLanguagesForEnglishWord2 = Collections.emptyList();
        final List<String> languagesForEnglishWord1 = englishLang.guessLanguages(englishWord1);
        final List<String> languagesForEnglishWord2 = englishLang.guessLanguages(englishWord2);
        assertEquals(expectedLanguagesForEnglishWord1, languagesForEnglishWord1);
        assertEquals(expectedLanguagesForEnglishWord2, languagesForEnglishWord2);

        // Test guessing languages for non-English words
        final List<String> expectedLanguagesForFrenchWord1 = Arrays.asList("fr");
        final List<String> languagesForFrenchWord1 = frenchLang.guessLanguages(frenchWord1);
        assertEquals(expectedLanguagesForFrenchWord1, languagesForFrenchWord1);

        final List<String> expectedLanguagesForSpanishWord1 = Arrays.asList("es");
        final List<String> languagesForSpanishWord1 = spanishLang.guessLanguages(spanishWord1);
        assertEquals(expectedLanguagesForSpanishWord1, languagesForSpanishWord1);

        final List<String> expectedLanguagesForGermanWord1 = Arrays.asList("de");
        final List<String> languagesForGermanWord1 = germanLang.guessLanguages(germanWord1);
        assertEquals(expectedLanguagesForGermanWord1, languagesForGermanWord1);

        final List<String> expectedLanguagesForChineseWord1 = Arrays.asList("zh");
        final List<String> languagesForChineseWord1 = chineseLang.guessLanguages(chineseWord1);
        assertEquals(expectedLanguagesForChineseWord1, languagesForChineseWord1);

        // Test guessing languages for English words with non-English characters
        final List<String> expectedLanguagesForEnglishWordWithNonEngChar = Arrays.asList("en");
        final List<String> languagesForEnglishWordWithNonEngChar = englishLang.guessLanguages(englishWordWithNonEngChar);
        assertEquals(expectedLanguagesForEnglishWordWithNonEngChar, languagesForEnglishWordWithNonEngChar);

        // Test guessing languages for invalid words
        final List<String> languagesForInvalidWord1 = Lang.instance(NameType.INVALID).guessLanguages("");
        final List<String> languagesForInvalidWord2 = Lang.instance(NameType.INVALID).guessLanguages(null);
        final List<String> languagesForInvalidWord3 = Lang.instance(NameType.INVALID).guessLanguages(" ");
        assertEquals(Collections.emptyList(), languagesForInvalidWord1);
        assertEquals(Collections.emptyList(), languagesForInvalidWord2);
        assertEquals(Collections.emptyList(), languagesForInvalidWord3);

        // Test guessing languages for ambiguous words
        final String ambiguousWord = "hello";
        final List<String> expectedLanguagesForAmbiguousWord = Arrays.asList("en", "hi");
        final List<String> languagesForAmbiguousWord = englishLang.guessLanguages(ambiguousWord);
        assertEquals(expectedLanguagesForAmbiguousWord, languagesForAmbiguousWord);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String, EnumSet)}.
     */
    @Test
    public void testGuessLanguagesWithFilters() {
        final NameType english = NameType.ENGLISH;
        final Lang englishLang = Lang.instance(english);

        // English words
        final String englishWord1 = "hello";
        final String englishWord2 = "world";

        // Non-English words
        final String frenchWord1 = "bonjour";
        final String spanishWord1 = "hola";
        final String germanWord1 = "guten tag";
        final String chineseWord1 = "你好";

        // English words with non-English characters
        final String englishWordWithNonEngChar = "Hello, 世界!";

        // Test guessing languages for English words with filters
        final EnumSet<NameType> filters = EnumSet.of(NameType.ENGLISH, NameType.FRENCH);
        final List<String> expectedLanguagesForEnglishWord1WithFilters = Arrays.asList("en");
        final List<String> languagesForEnglishWord1WithFilters = englishLang.guessLanguages(englishWord1, filters);
        assertEquals(expectedLanguagesForEnglishWord1WithFilters, languagesForEnglishWord1WithFilters);

        final List<String> expectedLanguagesForEnglishWord1WithOtherFilters = Arrays.asList();
        final List<String> languagesForEnglishWord1WithOtherFilters = englishLang.guessLanguages(englishWord1, EnumSet.of(NameType.GERMAN));
        assertEquals(expectedLanguagesForEnglishWord1WithOtherFilters, languagesForEnglishWord1WithOtherFilters);

        final List<String> expectedLanguagesForEnglishWord2WithFilters = Collections.emptyList();
        final List<String> languagesForEnglishWord2WithFilters = englishLang.guessLanguages(englishWord2, filters);
        assertEquals(expectedLanguagesForEnglishWord2WithFilters, languagesForEnglishWord2WithFilters);

        // Test guessing languages for non-English words with filters
        final List<String> expectedLanguagesForFrenchWord1WithFilters = Arrays.asList("fr");
        final List<String> languagesForFrenchWord1WithFilters = frenchLang.guessLanguages(frenchWord1, filters);
        assertEquals(expectedLanguagesForFrenchWord1WithFilters, languagesForFrenchWord1WithFilters);

        final List<String> expectedLanguagesForFrenchWord1WithOtherFilters = Collections.emptyList();
        final List<String> languagesForFrenchWord1WithOtherFilters = frenchLang.guessLanguages(frenchWord1, EnumSet.of(NameType.GERMAN));
        assertEquals(expectedLanguagesForFrenchWord1WithOtherFilters, languagesForFrenchWord1WithOtherFilters);

        final List<String> expectedLanguagesForSpanishWord1WithFilters = Arrays.asList();
        final List<String> languagesForSpanishWord1WithFilters = spanishLang.guessLanguages(spanishWord1, filters);
        assertEquals(expectedLanguagesForSpanishWord1WithFilters, languagesForSpanishWord1WithFilters);

        final List<String> expectedLanguagesForSpanishWord1WithOtherFilters = Collections.emptyList();
        final List<String> languagesForSpanishWord1WithOtherFilters = spanishLang.guessLanguages(spanishWord1, EnumSet.of(NameType.GERMAN));
        assertEquals(expectedLanguagesForSpanishWord1WithOtherFilters, languagesForSpanishWord1WithOtherFilters);

        final List<String> expectedLanguagesForGermanWord1WithFilters = Collections.emptyList();
        final List<String> languagesForGermanWord1WithFilters = germanLang.guessLanguages(germanWord1, filters);
        assertEquals(expectedLanguagesForGermanWord1WithFilters, languagesForGermanWord1WithFilters);

        final List<String> expectedLanguagesForGermanWord1WithOtherFilters = Arrays.asList("de");
        final List<String> languagesForGermanWord1WithOtherFilters = germanLang.guessLanguages(germanWord1, EnumSet.of(NameType.ENGLISH));
        assertEquals(expectedLanguagesForGermanWord1WithOtherFilters, languagesForGermanWord1WithOtherFilters);

        final List<String> expectedLanguagesForChineseWord1WithFilters = Collections.emptyList();
        final List<String> languagesForChineseWord1WithFilters = chineseLang.guessLanguages(chineseWord1, filters);
        assertEquals(expectedLanguagesForChineseWord1WithFilters, languagesForChineseWord1WithFilters);

        final List<String> expectedLanguagesForChineseWord1WithOtherFilters = Collections.emptyList();
        final List<String> languagesForChineseWord1WithOtherFilters = chineseLang.guessLanguages(chineseWord1, EnumSet.of(NameType.ENGLISH));
        assertEquals(expectedLanguagesForChineseWord1WithOtherFilters, languagesForChineseWord1WithOtherFilters);

        // Test guessing languages for English words with non-English characters with filters
        final List<String> expectedLanguagesForEnglishWordWithNonEngCharWithFilters = Arrays.asList("en");
        final List<String> languagesForEnglishWordWithNonEngCharWithFilters = englishLang.guessLanguages(englishWordWithNonEngChar, filters);
        assertEquals(expectedLanguagesForEnglishWordWithNonEngCharWithFilters, languagesForEnglishWordWithNonEngCharWithFilters);

        final List<String> expectedLanguagesForEnglishWordWithNonEngCharWithOtherFilters = Collections.emptyList();
        final List<String> languagesForEnglishWordWithNonEngCharWithOtherFilters = englishLang.guessLanguages(englishWordWithNonEngChar, EnumSet.of(NameType.GERMAN));
        assertEquals(expectedLanguagesForEnglishWordWithNonEngCharWithOtherFilters, languagesForEnglishWordWithNonEngCharWithOtherFilters);
    }
}