import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final Lang ENGLISH_LANGUAGE = Lang.instance(NameType.ENGLISH);

 @Test
 public void testEnglishLanguageRules() {
 String englishWord = "hello";
 assertEquals(Languages.ENGLISH, ENGLISH_LANGUAGE.guessLanguage(englishWord));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedLanguageRules() {
 try {
 Lang.loadFromResource("org/apache/commons/codec/language/bm/invalidLangRules.txt", Languages.getInstance(NameType.ENGLISH));
 fail("Expected IllegalArgumentException not thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAcceptOnMatchRule() {
 String text = "example";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("English", "German"));
 Set<String> actualLanguages = ENGLISH_LANGUAGE.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testRejectOnMatchRule() {
 String text = "example";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("French", "Italian"));
 Set<String> actualLanguages = ENGLISH_LANGUAGE.guessLanguages(text);
 assertTrue(expectedLanguages.isEmpty());
 }

 @Test
 public void testMixedRuleSet() {
 String text = "example";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("English", "German", "French", "Italian"));
 Set<String> actualLanguages = ENGLISH_LANGUAGE.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testEmptyRuleSet() {
 String text = "example";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Languages.ANY));
 Set<String> actualLanguages = ENGLISH_LANGUAGE.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testNullText() {
 String text = null;
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Languages.ANY));
 Set<String> actualLanguages = ENGLISH_LANGUAGE.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }
}