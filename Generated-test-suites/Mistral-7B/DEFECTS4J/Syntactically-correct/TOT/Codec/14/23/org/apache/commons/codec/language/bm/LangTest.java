package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expectedLanguages = Languages.ENGLISH;
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGermanLanguage() {
 String input = "Der schnelle braune Fuchs springt über den faulen Hund";
 Lang langInstance = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet expectedLanguages = Languages.GERMAN;
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testMixedLanguage() {
 String input = "The quick brown fox jumps über den faulen Hund";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expectedLanguages = new Languages.LanguageSet(Languages.ENGLISH, Languages.GERMAN);
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = "The quick [brown fox jumps over the lazy dog";
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 fail("Expected an IllegalArgumentException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyPattern() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = "";
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 fail("Expected an IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testMultipleMatches() {
 String input = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expectedLanguages = Languages.ENGLISH;
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 assertEquals(expectedLanguages, actualLanguages);

 Lang langInstanceGerman = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet actualLanguagesGerman = langInstanceGerman.guessLanguages(input);
 assertNotEquals(expectedLanguages, actualLanguagesGerman);
 }

 @Test
 public void testEmptyRuleFile() {
 List<LangRule> rules = new ArrayList<>();
 Lang langInstance = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
 String input = "The quick brown fox jumps over the lazy dog";
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(input);
 assertEquals(Languages.ANY, actualLanguages);
 }

}