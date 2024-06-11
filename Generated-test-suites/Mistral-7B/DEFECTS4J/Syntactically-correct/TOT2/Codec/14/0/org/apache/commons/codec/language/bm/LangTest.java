import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 Languages.LanguageSet expectedResult = Languages.ENGLISH;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);

 Assert.assertEquals("Test for English language with English text", expectedResult, actualResult);
 }

 @Test
 public void testFrenchLanguage() {
 String text = "Le renard rapide brun saute par-dessus le chien paresseux";
 Lang langInstance = Lang.instance(Lang.NameType.FRENCH);
 Languages.LanguageSet expectedResult = Languages.FRENCH;
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);

 Assert.assertEquals("Test for French language with French text", expectedResult, actualResult);
 }

 @Test
 public void testMixedLanguageText() {
 String text = "The quick brown fox jumps over the lazy dog Le renard rapide brun saute par-dessus le chien paresseux";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 Languages.LanguageSet expectedResult = new Languages.LanguageSet(Languages.ENGLISH, Languages.FRENCH);
 Languages.LanguageSet actualResult = langInstance.guessLanguages(text);

 Assert.assertEquals("Test for mixed-language text", expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCaseForPattern() {
 String text = "[^a-z]+"; // This pattern contains a negative character class
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);

 // This test case is expected to throw an IllegalArgumentException as the pattern is not valid
 langInstance.guessLanguages(text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyPattern() {
 String text = "";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);

 // This test case is expected to throw an IllegalArgumentException as the pattern is empty
 langInstance.guessLanguages(text);
 }
}