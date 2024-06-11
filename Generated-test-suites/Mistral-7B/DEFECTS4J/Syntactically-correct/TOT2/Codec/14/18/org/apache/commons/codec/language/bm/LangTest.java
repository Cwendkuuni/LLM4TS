import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.LangTest;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

 // Test for constructor and getting instance
 @Test
 public void testInstance() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 Assert.assertNotNull(lang);
 }

 // Test for guessLanguage method with typical use case
 @Test
 public void testGuessLanguageTypicalUseCase() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 String text = "Hello World";
 String expected = Languages.ENGLISH.getLanguage();
 String actual = lang.guessLanguage(text);
 Assert.assertEquals(expected, actual);
 }

 // Test for guessLanguages method with typical use case and multiple languages
 @Test
 public void testGuessLanguagesTypicalUseCase() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 String text = "Hello World";
 Languages.LanguageSet expected = Languages.ENGLISH.getLanguageSet();
 Languages.LanguageSet actual = lang.guessLanguages(text);
 Assert.assertEquals(expected, actual);
 }

 // Test for guessLanguage method with edge case - empty string
 @Test
 public void testGuessLanguageEmptyString() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 String text = "";
 String expected = Languages.ANY;
 String actual = lang.guessLanguage(text);
 Assert.assertEquals(expected, actual);
 }

 // Test for guessLanguages method with edge case - null input
 @Test(expected = NullPointerException.class)
 public void testGuessLanguagesNullInput() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 String text = null;
 lang.guessLanguages(text);
 }

 // Test for guessLanguages method with edge case - invalid input
 @Test
 public void testGuessLanguagesInvalidInput() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 String text = "!@#$%^&*()_+-=[]{}|;:,.<>?/{};\"'`\\";
 Languages.LanguageSet expected = Languages.ANY_LANGUAGE;
 Languages.LanguageSet actual = lang.guessLanguages(text);
 Assert.assertEquals(expected, actual);
 }
}