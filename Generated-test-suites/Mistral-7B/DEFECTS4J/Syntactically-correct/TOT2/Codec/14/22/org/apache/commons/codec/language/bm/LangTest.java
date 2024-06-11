import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final String TEST_WORD = "testWord";

 @Test
 public void testGuessLanguage_SimpleCase() {
 final Lang lang = Lang.instance(NameType.ENGLISH);
 final String result = lang.guessLanguage(TEST_WORD);
 assertTrue(result.equals(Languages.ENGLISH) || result.equals(Languages.ANY));
 }

 @Test
 public void testGuessLanguage_SingleMatch() {
 final Lang lang = Lang.instance(NameType.FRENCH);
 final List<LangRule> rules = lang.getRules();
 final String pattern = rules.get(0).getPattern().pattern();
 final String input = "testWord";
 final String expectedLanguage = rules.get(0).getLanguages().iterator().next();
 final String result = lang.guessLanguage(input);
 assertEquals(expectedLanguage, result);
 }

 @Test
 public void testGuessLanguage_MultipleMatches() {
 final Lang lang = Lang.instance(NameType.GERMAN);
 final List<LangRule> rules = lang.getRules();
 final String input = "testWord";
 final List<String> expectedLanguages = Arrays.asList(rules.get(0).getLanguages().toArray(new String[0]));
 final Languages.LanguageSet languages = lang.guessLanguages(input);
 assertEquals(languages.size(), expectedLanguages.size());
 for (String lang : expectedLanguages) {
 assertTrue(languages.contains(lang));
 }
 }

 @Test
 public void testGuessLanguage_NoMatch() {
 final Lang lang = Lang.instance(NameType.ITALIAN);
 final String input = "testWord";
 final String result = lang.guessLanguage(input);
 assertEquals(Languages.ANY, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguage_NullInput() {
 final Lang lang = Lang.instance(NameType.SPANISH);
 final String input = null;
 final String result = lang.guessLanguage(input);
 fail("Expected IllegalArgumentException, but received: " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguage_EmptyInput() {
 final Lang lang = Lang.instance(NameType.PORTUGUESE);
 final String input = "";
 final String result = lang.guessLanguage(input);
 fail("Expected IllegalArgumentException, but received: " + result);
 }
}