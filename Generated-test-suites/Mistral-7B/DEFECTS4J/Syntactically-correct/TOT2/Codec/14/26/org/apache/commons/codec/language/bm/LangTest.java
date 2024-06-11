import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testGuessLanguage_English() {
 String text = "The quick brown fox jumps over the lazy dog.";
 String expectedLanguage = Lang.Languages.ENGLISH.getName();
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGuessLanguage_French() {
 String text = "Le renard rapide brun saute par-dessus le chien paresseux.";
 String expectedLanguage = Lang.Languages.FRENCH.getName();
 Lang langInstance = Lang.instance(NameType.FRENCH);
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGuessLanguage_EmptyString() {
 String text = "";
 String expectedLanguage = Lang.Languages.ANY.getName();
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguage_NullText() {
 String text = null;
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 langInstance.guessLanguage(text);
 }

 @Test
 public void testGuessLanguages_EnglishAndFrench() {
 String text = "The quick brown fox jumps over the lazy dog. Le renard rapide brun saute par-dessus le chien paresseux.";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Lang.Languages.ENGLISH.getName(), Lang.Languages.FRENCH.getName()));
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGuessLanguages_EnglishAndEmpty() {
 String text = "The quick brown fox jumps over the lazy dog.";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Lang.Languages.ENGLISH.getName(), Lang.Languages.ANY.getName()));
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGuessLanguages_NoMatch() {
 String text = "The quick brown apple.";
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Lang.Languages.ANY.getName()));
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGuessLanguages_RuleMatches() {
 String text = "The quick brown fox.";
 LangRule englishRule = new LangRule(Pattern.compile("\\bthe\\b"), new HashSet<String>(Arrays.asList(Lang.Languages.ENGLISH.getName())), true);
 LangRule frenchRule = new LangRule(Pattern.compile("\\ble\\b"), new HashSet<String>(Arrays.asList(Lang.Languages.FRENCH.getName())), true);
 List<LangRule> rules = Arrays.asList(englishRule, frenchRule);
 Lang langInstance = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Lang.Languages.ENGLISH.getName()));
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGuessLanguages_RuleDoesNotMatch() {
 String text = "The quick brown fox.";
 LangRule englishRule = new LangRule(Pattern.compile("\\bthe\\b"), new HashSet<String>(Arrays.asList(Lang.Languages.ENGLISH.getName())), false);
 LangRule frenchRule = new LangRule(Pattern.compile("\\ble\\b"), new HashSet<String>(Arrays.asList(Lang.Languages.FRENCH.getName())), true);
 List<LangRule> rules = Arrays.asList(englishRule, frenchRule);
 Lang langInstance = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
 Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Lang.Languages.ANY.getName()));
 Languages.LanguageSet actualLanguages = langInstance.guessLanguages(text);
 assertEquals(expectedLanguages, actualLanguages);
 }
}