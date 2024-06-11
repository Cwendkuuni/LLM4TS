package org.apache.commons.codec.language.bm;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String englishWord = "hello";
        String guessedLanguage = englishLang.guessLanguage(englishWord);

        // English should be a valid guess for the given English word
        assertEquals(NameType.ENGLISH.getName(), guessedLanguage);
    }

    @Test
    public void testGermanLanguage() {
        Lang germanLang = Lang.instance(NameType.GERMAN);

        String germanWord = "guten Tag";
        String guessedLanguage = germanLang.guessLanguage(germanWord);

        // German should be a valid guess for the given German word
        assertEquals(NameType.GERMAN.getName(), guessedLanguage);
    }

    @Test
    public void testMixedLanguageWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String mixedWord = "hallobomba";
        String guessedLanguage = englishLang.guessLanguage(mixedWord);

        // The word "hallobomba" is a mix of English ("hello") and German ("Bombe" meaning bomb)
        // It might be difficult to guess the exact language, but we expect it to be in the list of possible languages
        List<String> expectedLanguages = Arrays.asList(NameType.ENGLISH.getName(), NameType.GERMAN.getName());
        Languages.LanguageSet languages = englishLang.guessLanguages(mixedWord);

        // Check that our guess is in the list of possible languages
        Assert.assertTrue(languages.containsAny(expectedLanguages));
    }

    @Test
    public void testEmptyWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String emptyWord = "";
        String guessedLanguage = englishLang.guessLanguage(emptyWord);

        // An empty word should not return any language
        assertEquals(Languages.ANY, guessedLanguage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String nullWord = null;

        englishLang.guessLanguage(nullWord);
    }

    @Test
    public void testMultipleRulesForSamePattern() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String text = "hello world";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("English", "Dutch"));
        Set<String> languages = englishLang.guessLanguages(text);

        // The test word "hello world" matches two rules: one for "hello" and one for "world"
        // Both rules accept the language, so we expect the result to be the union of the two languages
        assertEquals(expectedLanguages, languages);
    }

    @Test
    public void testEmptyRuleFile() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_empty.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // An empty rule file should not be loaded and should throw an exception
            assertEquals("Malformed line '' in language resource 'org/apache/commons/codec/language/bm/lang_empty.txt'", e.getMessage());
            return;
        }

        fail("Loading an empty rule file should have thrown an exception");
    }

    @Test
    public void testMalformedRuleFile() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_malformed.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // A malformed rule file should not be loaded and should throw an exception
            assertEquals("Malformed line '// This is a comment' in language resource 'org/apache/commons/codec/language/bm/lang_malformed.txt'", e.getMessage());
            return;
        }

        fail("Loading a malformed rule file should have thrown an exception");
    }

    @Test
    public void testMissingLanguageFile() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        try {
            Lang missingLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_missing.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalStateException e) {
            // A missing language file should not be loaded and should throw an exception
            assertEquals("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang_missing.txt", e.getMessage());
            return;
        }

        fail("Loading a missing language file should have thrown an exception");
    }

    @Test
    public void testIncompleteRuleFile() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_incomplete.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // An incomplete rule file should not be loaded and should throw an exception
            assertEquals("Malformed line 'pattern' in language resource 'org/apache/commons/codec/language/bm/lang_incomplete.txt'", e.getMessage());
            return;
        }

        fail("Loading an incomplete rule file should have thrown an exception");
    }

    @Test
    public void testEmptyRuleWithComment() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_comment.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // An empty rule with a comment should not be loaded and should throw an exception
            assertEquals("Malformed line '' in language resource 'org/apache/commons/codec/language/bm/lang_comment.txt'", e.getMessage());
            return;
        }

        fail("Loading an empty rule with a comment should have thrown an exception");
    }

    @Test
    public void testAcceptOnMatchFalse() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_accept_false.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // A rule file with an acceptOnMatch=false rule for a language that is not in the languages set should not be loaded
            assertEquals("Malformed line 'pattern Dutch false' in language resource 'org/apache/commons/codec/language/bm/lang_accept_false.txt'", e.getMessage());
            return;
        }

        fail("Loading a rule file with an acceptOnMatch=false rule for an unsupported language should have thrown an exception");
    }

    @Test
    public void testAcceptOnMatchTrue() {
        Lang englishLang = null;

        try {
            englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_accept_true.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // A rule file with an acceptOnMatch=true rule for a language that is not in the languages set should be loaded without errors
            assertEquals(Lang.class, englishLang.getClass());
            return;
        }

        // No exception thrown, so the test passed
    }
}