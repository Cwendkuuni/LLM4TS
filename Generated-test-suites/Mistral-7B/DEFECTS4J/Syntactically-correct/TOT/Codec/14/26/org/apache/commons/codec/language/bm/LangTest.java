package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Lang.
 */
public class LangTest {

 @Test
 public void testGuessLanguage() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String englishWord = "hello";
 String guessedLanguage = englishLang.guessLanguage(englishWord);
 assertEquals("English", guessedLanguage);

 Lang germanLang = Lang.instance(NameType.GERMAN);
 String germanWord = "hallo";
 String guessedLanguage2 = germanLang.guessLanguage(germanWord);
 assertEquals("German", guessedLanguage2);

 Lang anyLang = Lang.instance(NameType.ANY);
 String anyWord = "salut";
 String guessedLanguage3 = anyLang.guessLanguage(anyWord);
 assertNotNull(guessedLanguage3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedRuleLine() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String malformedRuleLine = "invalid_rule";
 InputStream inputStream = new ByteArrayInputStream(malformedRuleLine.getBytes());
 try {
 Lang ruleLang = Lang.loadFromResource("invalid_rule_resource", englishLang.getLanguages());
 fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected exception
 } finally {
 inputStream.close();
 }
 }
}