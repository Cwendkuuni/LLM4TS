package org.apache.commons.lang3.text.translate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for the NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

 @Test
 public void testSimpleNumericEntity() {
 char[] input = {'&', '#', 'x', '3', 'A', ';'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(cs, 0, writer);
 char[] output = outputStream.toByteArray();
 String expectedOutput = " À";
 assertEquals(expectedOutput.length(), output.length);
 for (int i = 0; i < expectedOutput.length(); i++) {
 assertEquals(expectedOutput.charAt(i), output[i]);
 }
 }

 @Test
 public void testSimpleNumericEntityWithoutSemicolon() {
 char[] input = {'&', '#', 'x', '3'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(cs, 0, writer);
 char[] output = outputStream.toByteArray();
 String expectedOutput = " À";
 assertEquals(expectedOutput.length(), output.length);
 for (int i = 0; i < expectedOutput.length(); i++) {
 assertEquals(expectedOutput.charAt(i), output[i]);
 }
 }

 @Test
 public void testInvalidHexNumericEntity() {
 char[] input = {'&', '#', 'x', 'z', ';'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(cs, 0, writer);
 assertEquals(0, length);
 }

 @Test
 public void testInvalidDecimalNumericEntity() {
 char[] input = {'&', '#', 'a', '1', ';'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(cs, 0, writer);
 assertEquals(0, length);
 }

 @Test(expected = IOException.class)
 public void testNegativeIndex() {
 char[] input = {'&', '#', 'x', '3', 'A', ';'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(cs, -1, writer);
 }

 @Test(expected = IOException.class)
 public void testNullInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
 int length = unescaper.translate(null, 0, writer);
 }

 @Test(expected = IOException.class)
 public void testNullWriter() {
 char[] input = {'&', '#', 'x', '3', 'A', ';'};
 CharSequence cs = new String(input);
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(cs, 0, null);
 }
}