import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String englishWord = "Hello World";
        String expectedLanguage = Languages.ENGLISH.getLanguageName();
        String actualLanguage = lang.guessLanguage(englishWord);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGermanLanguage() {
        Lang lang = Lang.instance(NameType.GERMAN);
        String germanWord = "Hallo Welt";
        String expectedLanguage = Languages.GERMAN.getLanguageName();
        String actualLanguage = lang.guessLanguage(germanWord);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testMixedLanguage() {
        Lang lang = Lang.instance(NameType.ANY);
        String mixedWord = "Hello Welt";
        Languages.LanguageSet expectedLanguages = new Languages.LanguageSet(Arrays.asList(Languages.ENGLISH, Languages.GERMAN));
        Languages.LanguageSet actualLanguages = lang.guessLanguages(mixedWord);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullInput() {
        Lang lang = Lang.instance(NameType.ANY);
        String nullInput = null;
        lang.guessLanguage(nullInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidResourceFile() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Arrays.asList("english", "french")), false)), Languages.getInstance(NameType.ANY));
        String invalidWord = "invalidWord";
        lang.guessLanguage(invalidWord);
    }

    @Test
    public void testEmptyRuleSet() {
        Lang lang = new Lang(Arrays.asList(), Languages.getInstance(NameType.ANY));
        String emptyWord = "";
        Languages.LanguageSet actualLanguages = lang.guessLanguages(emptyWord);
        Assert.assertEquals(Languages.ANY_LANGUAGE, actualLanguages);
    }

    @Test
    public void testAcceptOnMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String acceptOnMatchWord = "the quick brown fox jumps over the lazy dog";
        String expectedLanguage = Languages.ENGLISH.getLanguageName();
        String actualLanguage = lang.guessLanguage(acceptOnMatchWord);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testRejectOnMatch() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile("the.*dog"), new HashSet<String>(Arrays.asList("french")), true)), Languages.getInstance(NameType.ANY));
        String rejectOnMatchWord = "the quick brown fox jumps over the lazy dog";
        Languages.LanguageSet expectedLanguages = new Languages.LanguageSet();
        Languages.LanguageSet actualLanguages = lang.guessLanguages(rejectOnMatchWord);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }
}