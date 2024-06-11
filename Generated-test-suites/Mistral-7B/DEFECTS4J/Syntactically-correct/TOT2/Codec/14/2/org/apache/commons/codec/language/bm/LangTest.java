import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)} method.
     */
    @Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals("ENGLISH", englishLang.getClass().getSimpleName());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with valid input.
     */
    @Test
    public void testGuessLanguageValidInput() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String input = "the quick brown fox jumps over the lazy dog";
        final Languages.LanguageSet expected = Languages.LanguageSet.ENGLISH;
        final Languages.LanguageSet actual = englishLang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with empty input.
     */
    @Test
    public void testGuessLanguageEmptyInput() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String input = "";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = englishLang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with invalid input.
     */
    @Test
    public void testGuessLanguageInvalidInput() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String input = "!@#$%^&*()_+|~`";
        final Languages.LanguageSet expected = Languages.ANY;
        final Languages.LanguageSet actual = englishLang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with edge cases.
     */
    @Test
    public void testGuessLanguageEdgeCases() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String input1 = "12345";
        final Languages.LanguageSet expected1 = Languages.ANY;
        final Languages.LanguageSet actual1 = englishLang.guessLanguages(input1);
        assertEquals(expected1, actual1);

        final String input2 = "hello, world!";
        final Languages.LanguageSet expected2 = Languages.LanguageSet.ENGLISH;
        final Languages.LanguageSet actual2 = englishLang.guessLanguages(input2);
        assertEquals(expected2, actual2);

        final String input3 = "こんにちは、世界！";
        final Languages.LanguageSet expected3 = Languages.JAPANESE;
        final Languages.LanguageSet actual3 = englishLang.guessLanguages(input3);
        assertEquals(expected3, actual3);

        final String input4 = "привет, мир!";
        final Languages.LanguageSet expected4 = Languages.RUSSIAN;
        final Languages.LanguageSet actual4 = englishLang.guessLanguages(input4);
        assertEquals(expected4, actual4);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with exception handling.
     */
    @Test(expected = IllegalStateException.class)
    public void testGuessLanguageException() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String input = "Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt";
        englishLang.guessLanguages(input);
    }
}