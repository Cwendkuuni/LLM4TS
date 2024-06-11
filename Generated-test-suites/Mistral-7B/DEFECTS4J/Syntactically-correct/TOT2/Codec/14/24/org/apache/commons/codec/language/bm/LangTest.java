import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "The quick brown fox jumps over the lazy dog.";
 String expectedLanguage = Lang.Languages.ENGLISH.getName();
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGermanLanguage() {
 String input = "Das schnelle rote Pferd springt über den faulen Hund.";
 String expectedLanguage = Lang.Languages.GERMAN.getName();
 Lang langInstance = Lang.instance(Lang.NameType.GERMAN);
 String actualLanguage = langInstance.guessLanguage(input);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testMixedLanguage() {
 String input = "The schnelle rote Pferd springt über den faulen Hund.";
 String expectedLanguage = Lang.Languages.GERMAN.getName();
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 String input = null;
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 fail("Expected IllegalArgumentException but got: " + actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 String input = "";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 fail("Expected IllegalArgumentException but got: " + actualLanguage);
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingLanguageRulesResource() {
 Lang langInstance = new Lang(Collections.emptyList(null), Languages.getInstance(Lang.NameType.ENGLISH));
 String input = "The quick brown fox jumps over the lazy dog.";
 String actualLanguage = langInstance.guessLanguage(input);
 fail("Expected IllegalStateException but got: " + actualLanguage);
 }

 @Test
 public void testMultipleMatches() {
 String input = "Schnelles rotes Pferd springt über den faulen Hund.";
 String expectedLanguage = Lang.Languages.GERMAN.getName();
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testNoMatches() {
 String input = "ㄇㄋㄴㄋㄴㄉㄋㄴㄓㄡㄐ.";
 String expectedLanguage = Lang.Languages.ANY.getName();
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(input);
 assertEquals(expectedLanguage, actualLanguage);
 }
}