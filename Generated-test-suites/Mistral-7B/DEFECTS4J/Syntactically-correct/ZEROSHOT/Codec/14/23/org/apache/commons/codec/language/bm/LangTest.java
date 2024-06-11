package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.EnumSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LangTest {

    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
    private static final Languages LANGUAGES = Languages.getInstance(NameType.ENGLISH);

    @Parameters(name = "{index}: {0} -> {1}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"apple", "english"},
                {"banane", "french"},
                {"hola", "spanish"},
                {"salut", "french"},
                {" hallo ", "german"},
                {"bonjour", "french"},
                {"adios", "spanish"},
                {"guten tag", "german"},
                {"привет", "russian"},
                {"こんにちは", "japanese"},
                {"hello world", Languages.ANY},
                {"1234567890", Languages.ANY},
                {"", Languages.ANY},
                {"!!!", Languages.ANY},
                {"!@#$%^&*()_+-=<>?:{}|,./;'[]\\", Languages.ANY},
                {"// this is a comment", Languages.ANY},
                {"/* this is a multi-line comment */", Languages.ANY},
                {"Hello, World!", Languages.ANY},
                {"你好，世界！", "chinese"}, // this is a test for the Chinese rules that are not included in the default lang.txt file
        });
    }

    private final String input;
    private final String expected;

    public LangTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final String result = lang.guessLanguage(input);
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final Languages languages = lang.guessLanguages(input);
        if (expected.equals(Languages.ANY)) {
            assertTrue(languages.contains(Languages.ANY_LANGUAGE));
        } else {
            assertEquals(expected, languages.getAny());
            assertFalse(languages.contains(Languages.ANY_LANGUAGE));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testUnableToResolveRequiredResource() {
        Lang.loadFromResource("missing:lang.txt", LANGUAGES);
    }

    @Test
    public void testLoadFromResource() {
        final Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, LANGUAGES);
        final String result = lang.guessLanguage(input);
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguagesFromResource() {
        final Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, LANGUAGES);
        final Languages languages = lang.guessLanguages(input);
        if (expected.equals(Languages.ANY)) {
            assertTrue(languages.contains(Languages.ANY_LANGUAGE));
        } else {
            assertEquals(expected, languages.getAny());
            assertFalse(languages.contains(Languages.ANY_LANGUAGE));
        }
    }

    @Test
    public void testGuessLangForChinese() {
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_zh.txt", LANGUAGES);
        final String result = lang.guessLanguage("你好，世界！");
        assertEquals("chinese", result);
    }

    @Test
    public void testGuessLangForJapanese() {
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_ja.txt", LANGUAGES);
        final String result = lang.guessLanguage("こんにちは");
        assertEquals("japanese", result);
    }
}