package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator translatorUpperCase;
 private CharSequenceTranslator translatorLowerCase;
 private CharSequenceTranslator translatorAggregate;

 @Before
 public void setUp() {
 translator = new TranslateAll();
 translatorUpperCase = new TranslateUpperCase();
 translatorLowerCase = new TranslateLowerCase();
 translatorAggregate = translator.with(translatorUpperCase, translatorLowerCase);
 }

 /**
 * Test translate method with simple CharSequence.
 */
 @Test
 public void testTranslateSimple() throws IOException {
 String input = "Hello World!";
 String expectedOutput = "48656c6c6f20576f726c6421";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 /**
 * Test translate method with empty CharSequence.
 */
 @Test
 public void testTranslateEmpty() throws IOException {
 String input = "";
 String output = translator.translate(input);
 assertNull(output);
 }

 /**
 * Test translate method with null CharSequence.
 */
 @Test(expected = NullPointerException.class)
 public void testTranslateNull() throws IOException {
 translator.translate(null);
 }

 /**
 * Test translate method with Writer.
 */
 @Test
 public void testTranslateWithWriter() throws IOException {
 String input = "Hello World!";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
 translator.translate(input, writer);
 String expectedOutput = "48656c6c6f20576f726c6421";
 assertEquals(expectedOutput, outputStream.toString());
 }

 /**
 * Test translate method with AggregateTranslator.
 */
 @Test
 public void testTranslateAggregate() throws IOException {
 String input = "Hello World!";
 String expectedOutputUpperCase = "48656c6c6f20576f726c6421";
 String expectedOutputLowerCase = "48656c6c6f20506f6c6421";
 String output = translatorAggregate.translate(input);
 String[] parts = output.split("(?<=\\G.{2})");
 String outputUpperCase = Arrays.toString(parts).substring(1, Arrays.toString(parts).length() - 1)
         .replaceAll("\\[|\\]", "").split(" ");
 assertEquals(expectedOutputUpperCase, Arrays.asList(outputUpperCase).get(0).split(""));
 String outputLowerCase = Arrays.toString(parts).substring(parts.length - outputUpperCase.length)
         .replaceAll("\\[|\\]", "").split(" ")[0].split("");
 assertEquals(expectedOutputLowerCase, outputLowerCase);
 }
}

class TranslateAll implements CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int consumed = 0;
 int len = Character.codePointCount(input, 0, input.length());
 for (int i = index; i < len; i += Character.charCount(Character.codePointAt(input, i))) {
 consumed += translateChar(Character.codePointAt(input, i), out);
 }
 return consumed;
 }

 private int translateChar(int codepoint, Writer out) throws IOException {
 if (Character.isHighSurrogate(codepoint)) {
 int highSurrogate = codepoint;
 int lowSurrogate = Character.codePointAt(input, index + 1);
 int surrogatePair = ((highSurrogate << 10) | (lowSurrogate & 0x3FF));
 out.write(Character.forDigit((surrogatePair >> 12) & 0xF, 16));
 out.write(Character.forDigit((surrogatePair >> 6) & 0xF, 16));
 out.write(Character.forDigit(surrogatePair & 0x3F, 16));
 index++;
 return 3;
 }
 out.write(Character.forDigit((codepoint >> 4) & 0xF, 16));
 out.write(Character.forDigit((codepoint >> 2) & 0xF, 16));
 out.write(Character.forDigit(codepoint & 0xF, 16));
 return 1;
 }
}

class TranslateUpperCase implements CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int consumed = 0;
 int len = Character.codePointCount(input, 0, input.length());
 for (int i = index; i < len; i += Character.charCount(Character.codePointAt(input, i))) {
 consumed += 1;
 out.write(Character.toUpperCase(Character.codePointAt(input, i)));
 }
 return consumed;
 }
}

class TranslateLowerCase implements CharSequenceTranslator {
 @Override
 public int translate(CharSequence input, int index, Writer out) throws IOException {
 int consumed = 0;
 int len = Character.codePointCount(input, 0, input.length());
 for (int i = index; i < len; i += Character.charCount(Character.codePointAt(input, i))) {
 consumed += 1;
 out.write(Character.toLowerCase(Character.codePointAt(input, i)));
 }
 return consumed;
 }
}