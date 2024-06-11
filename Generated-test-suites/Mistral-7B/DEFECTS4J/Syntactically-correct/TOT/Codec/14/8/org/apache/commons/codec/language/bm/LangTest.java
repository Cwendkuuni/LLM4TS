package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String expectedLanguage = Lang.Languages.ENGLISH.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testMixedLanguage() {
 String text = "Der schnelle grüne Bär springt über den faulen Hund";
 Lang langInstance = Lang.instance(NameType.GERMAN);
 String expectedLanguage = Lang.Languages.GERMAN.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testUnsupportedLanguage() {
 String text = "你好，世界";
 Lang langInstance = Lang.instance(NameType.CHINESE);
 String expectedLanguage = Lang.Languages.ANY.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = null;
 langInstance.guessLanguage(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String input = "";
 langInstance.guessLanguage(input);
 }
}