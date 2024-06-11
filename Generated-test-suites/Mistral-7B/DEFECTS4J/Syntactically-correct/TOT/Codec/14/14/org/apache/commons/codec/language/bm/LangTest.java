package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguageDetection() {
 String input = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet guessedLanguages = langInstance.guessLanguages(input);
 assertEquals(Languages.ENGLISH, guessedLanguages.getAny());
 }

 @Test
 public void testMultiLanguageDetection() {
 String input = "Der Hase ist schnell, der Fuchs ist schneller."; // German: The hare is fast, the fox is faster.
 Lang langInstance = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet guessedLanguages = langInstance.guessLanguages(input);
 assertTrue(guessedLanguages.contains(Languages.GERMAN));
 assertTrue(guessedLanguages.contains(Languages.ENGLISH));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedRuleFile() {
 String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_malformed.txt";
 Lang langInstance = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));
 fail("Expected an IllegalArgumentException due to malformed rule file");
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingRuleFile() {
 String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_missing.txt";
 Lang langInstance = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));
 fail("Expected an IllegalStateException due to missing rule file");
 }
}