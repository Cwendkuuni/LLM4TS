package org.apache.commons.lang3.text.translate;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;

 import org.apache.commons.lang3.CharSequenceUtils;
 import org.apache.commons.lang3.StringUtils;
 import org.apache.commons.lang3.text.translate.LookupTranslator;
 import org.junit.Test;

 /**
 * Test class for LookupTranslator
 */
 public class LookupTranslatorTest {

 /**
 * Test constructor and translation with valid lookup table
 */
 @Test
 public void testLookupTranslator() {
 CharSequence[][] lookup = { new CharSequence[]{"hello", "hi"}, new CharSequence[]{"world", "earth"},
 new CharSequence[]{"java", "scala"} };
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "hello world java";
 int index = 0;
 String expectedOutput = "hi earth scalax";
 String actualOutput = "";
 try {
 for (int i = 0; i < input.length() - index; i++) {
 int len = translator.translate(input, index + i, new StringWriter(actualOutput));
 index += len;
 if (i < input.length() - index - 1) {
 actualOutput += StringUtils.SPACE;
 }
 }
 assertEquals(expectedOutput, actualOutput.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 /**
 * Test constructor with null lookup table
 */
 @Test(expected = IllegalArgumentException.class)
 public void testLookupTranslatorNullLookup() {
 new LookupTranslator(null);
 }

 /**
 * Test translation with empty input
 */
 @Test
 public void testLookupTranslatorEmptyInput() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
 String input = "";
 String expectedOutput = "";
 String actualOutput = "";
 try {
 translator.translate(input, 0, new StringWriter(actualOutput));
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput, actualOutput.toString());
 }

 /**
 * Test translation with input shorter than the shortest lookup key
 */
 @Test
 public void testLookupTranslatorInputShorterThanShortest() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
 CharSequence input = "h";
 String expectedOutput = "";
 String actualOutput = "";
 try {
 translator.translate(input, 0, new StringWriter(actualOutput));
 } catch (IOException e) {
 e.printStackTrace();
 }
 assertEquals(expectedOutput, actualOutput.toString());
 }

 /**
 * Test translation with input longer than the longest lookup key
 */
 @Test
 public void testLookupTranslatorInputLongerThanLongest() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"hello, world".split(", ")});
 CharSequence input = "helloworldhelloworld";
 String expectedOutput = "hello world helloworld";
 String actualOutput = "";
 try {
 for (int i = 0; i < input.length() - 5; i++) {
 int len = translator.translate(input, i, new StringWriter(actualOutput));
 if (i < input.length() - 5 - 1) {
 actualOutput += StringUtils.SPACE;
 }
 }
 assertEquals(expectedOutput, actualOutput.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 /**
 * Test translation with input containing a lookup key but without a corresponding value
 */
 @Test
 public void testLookupTranslatorMissingValue() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"hello, world".split(", "), "hello", "world"});
 CharSequence input = "helloworld";
 String expectedOutput = "hello world";
 String actualOutput = "";
 try {
 for (int i = 0; i < input.length() - 5; i++) {
 int len = translator.translate(input, i, new StringWriter(actualOutput));
 if (i < input.length() - 5 - 1) {
 actualOutput += StringUtils.SPACE;
 }
 }
 assertEquals(expectedOutput, actualOutput.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 /**
 * Test translation with input containing multiple occurrences of the same lookup key
 */
 @Test
 public void testLookupTranslatorMultipleOccurrences() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"hello, world, java, java".split(", "), "hello", "world", "java"});
 Charsequence input = "helloworldjavahelloworldjava";
 String expectedOutput = "hello world javahello world java";
 String actualOutput = "";
 try {
 for (int i = 0; i < input.length() - 10; i++) {
 int len = translator.translate(input, i, new StringWriter(actualOutput));
 if (i < input.length() - 10 - 1) {
 actualOutput += StringUtils.SPACE;
 }
 }
 assertEquals(expectedOutput, actualOutput.toString());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 /**
 * Test translation with null input
 */
 @Test(expected = NullPointerException.class)
 public void testLookupTranslatorNullInput() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"hello, world".split(", ")});
 translator.translate(null, 0, new StringWriter(new StringBuilder()));
 }

 /**
 * Test translation with null output writer
 */
 @Test(expected = NullPointerException.class)
 public void testLookupTranslatorNullOutputWriter() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"hello, world".split(", ")});
 translator.translate("hello world".toCharArray(), 0, null);
 }
 }