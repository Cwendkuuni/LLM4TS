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
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        assertEquals(nameType, lang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Basic test case: A simple English word
        final String englishWord = "Hello";
        final String expectedLanguage = Languages.ENGLISH.getCode();
        final String actualLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(expectedLanguage, actualLanguage);

        // Test case: A complex English word
        final String complexEnglishWord = "Supercalifragilisticexpialidocious";
        final String actualComplexEnglishLanguage = englishLang.guessLanguage(complexEnglishWord);
        assertEquals(expectedLanguage, actualComplexEnglishLanguage);

        // Test case: A non-English word
        final String nonEnglishWord = "Hola";
        final String expectedNonEnglishLanguage = Languages.SPANISH.getCode();
        final String actualNonEnglishLanguage = englishLang.guessLanguage(nonEnglishWord);
        assertFalse(expectedNonEnglishLanguage.equals(actualNonEnglishLanguage));

        // Test case: An empty string
        final String emptyString = "";
        final String actualEmptyStringLanguage = englishLang.guessLanguage(emptyString);
        assertEquals(Languages.ANY, actualEmptyStringLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Basic test case: A simple English word
        final String englishWord = "Hello";
        final List<String> expectedLanguages = Arrays.asList(Languages.ENGLISH.getCode());
        final Set<String> actualLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(expectedLanguages, actualLanguages.stream().map(Languages::getCode).collect(Collectors.toList()));

        // Test case: A complex English word
        final String complexEnglishWord = "Supercalifragilisticexpialidocious";
        final Set<String> actualComplexEnglishLanguages = englishLang.guessLanguages(complexEnglishWord);
        assertEquals(expectedLanguages, actualComplexEnglishLanguages.stream().map(Languages::getCode).collect(Collectors.toList()));

        // Test case: A non-English word
        final String nonEnglishWord = "Hola";
        final Set<String> expectedNonEnglishLanguages = EnumSet.of(Languages.SPANISH.getCode());
        final Set<String> actualNonEnglishLanguages = englishLang.guessLanguages(nonEnglishWord);
        assertEquals(expectedNonEnglishLanguages, actualNonEnglishLanguages);

        // Test case: A non-English word with multiple possible languages
        final String nonEnglishWordWithMultiplePossibilities = "BuenosDias";
        final Set<String> expectedNonEnglishWithMultiplePossibilitiesLanguages = EnumSet.of(Languages.SPANISH.getCode(),
                Languages.PORTUGUESE.getCode());
        final Set<String> actualNonEnglishWithMultiplePossibilitiesLanguages = englishLang.guessLanguages(nonEnglishWordWithMultiplePossibilities);
        assertTrue(expectedNonEnglishWithMultiplePossibilitiesLanguages.containsAll(actualNonEnglishWithMultiplePossibilitiesLanguages));

        // Test case: An empty string
        final String emptyString = "";
        final Set<String> actualEmptyStringLanguages = englishLang.guessLanguages(emptyString);
        assertEquals(Collections.singleton(Languages.ANY.getCode()), actualEmptyStringLanguages);
    }

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testLoadFromResource() {
        final NameType nameType = NameType.GERMAN;
        final Lang lang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(nameType));
        assertEquals(nameType, lang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguageFromResource() {
        final NameType nameType = NameType.GERMAN;
        final Lang lang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(nameType));

        // Basic test case: A simple German word
        final String germanWord = "Hallo";
        final String expectedLanguage = Languages.GERMAN.getCode();
        final String actualLanguage = lang.guessLanguage(germanWord);
        assertEquals(expectedLanguage, actualLanguage);

        // Test case: A complex German word
        final String complexGermanWord = "Supercalifragilisticexpialidocious";
        final String actualComplexGermanLanguage = lang.guessLanguage(complexGermanWord);
        assertEquals(expectedLanguage, actualComplexGermanLanguage);

        // Test case: A non-German word
        final String nonGermanWord = "Hola";
        final String actualNonGermanLanguage = lang.guessLanguage(nonGermanWord);
        assertFalse(expectedLanguage.equals(actualNonGermanLanguage));

        // Test case: An empty string
        final String emptyString = "";
        final String actualEmptyStringLanguage = lang.guessLanguage(emptyString);
        assertEquals(Languages.ANY, actualEmptyStringLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguagesFromResource() {
        final NameType nameType = NameType.GERMAN;
        final Lang lang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(nameType));

        // Basic test case: A simple German word
        final String germanWord = "Hallo";
        final List<String> expectedLanguages = Arrays.asList(Languages.GERMAN.getCode());
        final Set<String> actualLanguages = lang.guessLanguages(germanWord);
        assertEquals(expectedLanguages, actualLanguages.stream().map(Languages::getCode).collect(Collectors.toList()));

        // Test case: A complex German word
        final String complexGermanWord = "Supercalifragilisticexpialidocious";
        final Set<String> actualComplexGermanLanguages = lang.guessLanguages(complexGermanWord);
        assertEquals(expectedLanguages, actualComplexGermanLanguages.stream().map(Languages::getCode).collect(Collectors.toList()));

        // Test case: A non-German word
        final String nonGermanWord = "Hola";
        final Set<String> expectedNonGermanLanguages = EnumSet.of(Languages.SPANISH.getCode());
        final Set<String> actualNonGermanLanguages = lang.guessLanguages(nonGermanWord);
        assertEquals(expectedNonGermanLanguages, actualNonGermanLanguages);

        // Test case: A non-German word with multiple possible languages
        final String nonGermanWordWithMultiplePossibilities = "BuenosDias";
        final Set<String> expectedNonGermanWithMultiplePossibilitiesLanguages = EnumSet.of(Languages.SPANISH.getCode(),
                Languages.PORTUGUESE.getCode());
        final Set<String> actualNonGermanWithMultiplePossibilitiesLanguages = lang.guessLanguages(nonGermanWordWithMultiplePossibilities);
        assertTrue(expectedNonGermanWithMultiplePossibilitiesLanguages.containsAll(actualNonGermanWithMultiplePossibilitiesLanguages));

        // Test case: An empty string
        final String emptyString = "";
        final Set<String> actualEmptyStringLanguages = lang.guessLanguages(emptyString);
        assertEquals(Collections.singleton(Languages.ANY.getCode()), actualEmptyStringLanguages);
    }
}