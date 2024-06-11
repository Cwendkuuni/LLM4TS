import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 // Test case for the instance() method
 @Test
 public void testInstance() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 assertNotNull(langEnglish);
 }

 // Test case for the loadFromResource() method
 @Test
 public void testLoadFromResource() {
 Lang langFrench = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", Languages.FRENCH);
 assertNotNull(langFrench);
 }

 // Test case for the guessLanguage() method with typical use case
 @Test
 public void testGuessLanguageTypicalUseCase() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = "hello";
 String expected = Languages.ENGLISH.getDefault();
 String actual = langEnglish.guessLanguage(word);
 assertEquals(expected, actual);
 }

 // Test case for the guessLanguage() method with edge case - empty string
 @Test
 public void testGuessLanguageEdgeCaseEmptyString() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = "";
 String expected = Languages.ANY;
 String actual = langEnglish.guessLanguage(word);
 assertEquals(expected, actual);
 }

 // Test case for the guessLanguage() method with edge case - null input
 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguageEdgeCaseNullInput() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = null;
 langEnglish.guessLanguage(word);
 }

 // Test case for the guessLanguages() method with typical use case
 @Test
 public void testGuessLanguagesTypicalUseCase() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = "hello";
 Set<String> expected = new HashSet<>(Arrays.asList(Languages.ENGLISH.getLanguages()));
 Set<String> actual = langEnglish.guessLanguages(word);
 assertEquals(expected, actual);
 }

 // Test case for the guessLanguages() method with edge case - empty string
 @Test
 public void testGuessLanguagesEdgeCaseEmptyString() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = "";
 Set<String> expected = Languages.NO_LANGUAGES;
 Set<String> actual = langEnglish.guessLanguages(word);
 assertEquals(expected, actual);
 }

 // Test case for the guessLanguages() method with edge case - null input
 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguagesEdgeCaseNullInput() {
 Lang langEnglish = Lang.instance(NameType.ENGLISH);
 String word = null;
 langEnglish.guessLanguages(word);
 }
}