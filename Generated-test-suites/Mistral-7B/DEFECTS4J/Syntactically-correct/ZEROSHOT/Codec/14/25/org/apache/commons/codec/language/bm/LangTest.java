import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LangTest {

 private static final Lang LANG_EN = Lang.instance(NameType.ENGLISH);
 private static final Lang LANG_DE = Lang.instance(NameType.GERMAN);

 @Test
 public void testGuessLanguageEnglish() {
 String text = "The quick brown fox jumps over the lazy dog.";
 String expected = Languages.ENGLISH.getLanguageName();
 String result = LANG_EN.guessLanguage(text);
 assertTrue(result.equals(expected));
 }

 @Test
 public void testGuessLanguageGerman() {
 String text = "Der schnelle braune Fuchs springt über den faulen Hund.";
 String expected = Languages.GERMAN.getLanguageName();
 String result = LANG_DE.guessLanguage(text);
 assertTrue(result.equals(expected));
 }

 @Test
 public void testGuessLanguageMixed() {
 String text = "Der schnelle braune Fuchs springt über den faulen Hund. The quick brown fox jumps over the lazy dog.";
 Set<String> expected = new HashSet<String>(Arrays.asList(Languages.ENGLISH.getLanguageName(), Languages.GERMAN.getLanguageName()));
 Set<String> result = LANG_EN.guessLanguages(text);
 assertTrue(expected.equals(result));
 }

 @Test
 public void testGuessLanguageNoMatch() {
 String text = "The quick brown fox jumps over the lazy cat.";
 Set<String> result = LANG_DE.guessLanguages(text);
 assertTrue(result.equals(Languages.ANY_LANGUAGE));
 }

 @Test
 public void testGuessLanguageEmptyString() {
 String text = "";
 Set<String> result = LANG_DE.guessLanguages(text);
 assertFalse(result.isSingleton());
 }

}