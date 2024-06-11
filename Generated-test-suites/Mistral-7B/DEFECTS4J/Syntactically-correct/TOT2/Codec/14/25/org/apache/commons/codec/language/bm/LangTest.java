package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "The quick brown fox jumps over the lazy dog.";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("en"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testMultiLanguageWord() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "Hola, こんにちは, Hallo, Bonjour, Ciao!";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("es", "ja", "de", "fr", "it"));
        Set<String> actualLanguages = lang.guessLanguages(text);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testEmptyString() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(text);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedRule() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = new ArrayList<>(Lang.Langs.get(NameType.ENGLISH).rules);
        rules.add(new LangRule(Pattern.compile(".*"), Collections.singleton("en"), true)); // Malformed rule
        Lang newLang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String text = "Test";
        newLang.guessLanguages(text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingResourceFile() {
        Lang lang = null;
        try {
            lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/non-existent-lang.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalStateException e) {
            // expected
        }
        Assert.assertNull(lang);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAcceptOnMatchValue() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = new ArrayList<>(Lang.Langs.get(NameType.ENGLISH).rules);
        rules.add(new LangRule(Pattern.compile(".*"), Collections.singleton("en"), "invalid"));
        Lang newLang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String text = "Test";
        try {
            newLang.guessLanguages(text);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEdgeCaseWithEmptyLinesAndMultiLineComments() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_with_comments.txt";
        InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);
        Scanner scanner = new Scanner(lRulesIS, ResourceConstants.ENCODING);
        try {
            Set<String> expectedLanguages = new HashSet<>(Arrays.asList("en"));
            Set<String> actualLanguages = new HashSet<>();
            while (scanner.hasNextLine()) {
                String rawLine = scanner.nextLine();
                String line = rawLine;
                if (line.startsWith("/*")) {
                    // check for closing comment marker, otherwise discard doc comment line
                    while (!line.isEmpty() && !line.endsWith("*/")) {
                        line = scanner.nextLine();
                    }
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    continue;
                }
                Pattern pattern = Pattern.compile(parts[0]);
                String[] langs = parts[1].split("\\+");
                boolean accept = parts[2].equals("true");

                if (accept) {
                    actualLanguages.addAll(Arrays.asList(langs));
                } else {
                    actualLanguages.removeAll(Arrays.asList(langs));
                }

                if (pattern.matcher("the quick brown fox").find()) {
                    expectedLanguages.addAll(Arrays.asList("en"));
                }
            }
            Assert.assertEquals(expectedLanguages, actualLanguages);
        } finally {
            scanner.close();
        }
    }
}