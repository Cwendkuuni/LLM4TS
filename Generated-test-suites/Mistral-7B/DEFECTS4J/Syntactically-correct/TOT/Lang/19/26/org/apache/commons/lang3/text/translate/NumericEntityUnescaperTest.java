package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper translator = new NumericEntityUnescaper();

 @Test
 public void testTranslate_simple_hex_entity() throws Exception {
 char[] expected = {'&', '#', 'x', '3', 'A', ';'};
 CharSequence input = "&#x3A;";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new StringWriter(null).getBuffer().toString().toCharArray();
 assertEquals(expected.length, result.length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_simple_decimal_entity() throws Exception {
 char[] expected = {'&', '#', '3', '4', ';'};
 CharSequence input = "&#34;";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new StringWriter(null).getBuffer().toString().toCharArray();
 assertEquals(expected.length, result.length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_empty() throws Exception {
 CharSequence input = "";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_invalid_entity() throws Exception {
 CharSequence input = "&#";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 assertEquals(0, length);
 }

 @Test
 public void testTranslate_entity_with_semi_colon() throws Exception {
 char[] expected = {'&', '#', '3', '4', ';'};
 CharSequence input = "&#34;";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new StringWriter(null).getBuffer().toString().toCharArray();
 assertEquals(expected.length, result.length);
 assertArrayEquals(expected, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_hex_entity() throws Exception {
 CharSequence input = "&#xz";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_invalid_decimal_entity() throws Exception {
 CharSequence input = "&#123456";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 }

 @Test
 public void testTranslate_large_entity() throws Exception {
 int entityValue = 0x10FFFF;
 char[] expected = Character.toChars(entityValue);
 CharSequence input = "&#{x" + Integer.toHexString(entityValue).toUpperCase() + "};";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new StringWriter(null).getBuffer().toString().toCharArray();
 assertEquals(expected.length, result.length);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testTranslate_large_entity_with_semi_colon() throws Exception {
 int entityValue = 0x10FFFF;
 char[] expected = Character.toChars(entityValue);
 CharSequence input = "&#{x" + Integer.toHexString(entityValue).toUpperCase() + ";";
 int length = translator.translate(input, 0, new StringWriter(null).getBuffer());
 char[] result = new StringWriter(null).getBuffer().toString().toCharArray();
 assertEquals(expected.length, result.length);
 assertArrayEquals(expected, result);
 }
}