package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final String TEST_WORD = "testWord";

 @Test
 public void testEnglishLanguage() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 Languages englishLanguages = Languages.getInstance(NameType.ENGLISH);
 assertEquals("English", englishLang.guessLanguage(TEST_WORD).getName());
 assertFalse(englishLanguages.getLanguageSet(TEST_WORD).isEmpty());
 }

 @Test
 public void testNonExistentLanguage() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String nonExistentLanguage = "NonExistentLanguage";
 assertNull(englishLang.guessLanguage(TEST_WORD));
 assertTrue(englishLang.getLanguages().getLanguageSet(TEST_WORD).isEmpty());
 }

 @Test
 public void testMixedLanguage() {
 Lang mixedLang = Lang.instance(NameType.MIXED);
 List<String> supportedLanguages = new ArrayList<>(Arrays.asList(
 "English", "German", "French", "Russian", "Chinese", "Japanese", "Korean", "Dutch", "Portuguese", "Italian"
 ));
 assertEquals(supportedLanguages, mixedLang.getLanguages().getLanguages());

 List<String> languages = mixedLang.guessLanguages(TEST_WORD);
 assertFalse(languages.isEmpty());
 assertTrue(languages.containsAll(supportedLanguages));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRuleFormat() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 List<LangRule> rules = englishLang.getRules();
 String invalidRule = "pattern + languages + true"; // missing colon
 rules.set(0, new LangRule(Pattern.compile(invalidRule), Collections.emptySet(), true));
 String testWord = "testWord";
 try {
 englishLang.guessLanguage(testWord);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}