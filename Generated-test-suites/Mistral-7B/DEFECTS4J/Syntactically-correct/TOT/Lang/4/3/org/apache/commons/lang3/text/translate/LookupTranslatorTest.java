package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for LookupTranslator class.
 */
public class LookupTranslatorTest {

 private static final String[][] LOOKUP_TABLE = {{"hello", "hi"}, {"world", "planet"}, {"java", "python"}, {"test", "success"}};

 @Test
 public void testTranslation() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "helloworldjava";
 try {
 Writer writer = new StringBuilder().append("Translated: ").createWriter();
 int index = 0;
 int length = input.length();
 while (index < length) {
 int translationLength = translator.translate(CharSequenceUtil.subSequence(input, index), 0, writer);
 index += translationLength;
 if (index < length) {
 writer.write(" ");
 }
 }
 String expectedOutput = StringUtils.join(LOOKUP_TABLE, " ");
 assertEquals(expectedOutput, writer.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLookupTable() {
 LookupTranslator translator = new LookupTranslator(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyLookupTable() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupTableWithInvalidKeyType() {
 LookupTranslator translator = new LookupTranslator(new int[][] {{"1", "2"}});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupTableWithInvalidValueType() {
 LookupTranslator translator = new LookupTranslator(new String[][] {{"hello", 1}});
 }

 @Test
 public void testTranslationWithEmptyInput() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "";
 try {
 Writer writer = new StringBuilder().createWriter();
 int index = 0;
 int length = input.length();
 while (index < length) {
 int translationLength = translator.translate(CharSequenceUtil.subSequence(input, index), 0, writer);
 index += translationLength;
 }
 assertEquals("", writer.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslationWithInputShorterThanShortestKey() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "hell";
 try {
 Writer writer = new StringBuilder().createWriter();
 int index = 0;
 int length = input.length();
 while (index < length) {
 int translationLength = translator.translate(CharSequenceUtil.subSequence(input, index), 0, writer);
 index += translationLength;
 }
 assertEquals("", writer.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslationWithInputLongerThanLongestKey() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "helloworldjavatest";
 try {
 Writer writer = new StringBuilder().append("Translated: ").createWriter();
 int index = 0;
 int length = input.length();
 while (index < length) {
 int translationLength = translator.translate(CharSequenceUtil.subSequence(input, index), 0, writer);
 index += translationLength;
 if (index < length) {
 writer.write(" ");
 }
 }
 String expectedOutput = StringUtils.join(LOOKUP_TABLE, " ") + " success";
 assertEquals(expectedOutput, writer.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testTranslationWithMismatchedKey() {
 LookupTranslator translator = new LookupTranslator(LOOKUP_TABLE);
 String input = "helloworldjava";
 try {
 Writer writer = new StringBuilder().createWriter();
 int index = 0;
 int length = input.length();
 while (index < length) {
 int translationLength = translator.translate(CharSequenceUtil.subSequence(input, index), 0, writer);
 index += translationLength;
 }
 assertEquals("hi planet python success", writer.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
}