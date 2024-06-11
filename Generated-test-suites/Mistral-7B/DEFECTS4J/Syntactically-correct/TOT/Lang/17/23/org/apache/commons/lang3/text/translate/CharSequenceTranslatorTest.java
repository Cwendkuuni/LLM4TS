package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator hexTranslator;
 private CharSequenceTranslator aggregateTranslator;

 @Before
 public void setUp() {
 translator = new TranslatingCharSequenceTranslator();
 hexTranslator = new HexTranslator();
 aggregateTranslator = translator.with(hexTranslator);
 }

 @Test
 public void testTranslateSimpleString() {
 String input = "Hello World!";
 String expectedOutput = "48656c6c6f20576f726c6421";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateEmptyString() {
 String input = "";
 String expectedOutput = "";
 String actualOutput = translator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateNullInput() {
 String expectedOutput = null;
 String actualOutput = translator.translate(null);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateNullWriter() {
 writer = null;
 translator.translate(input, writer);
 }

 @Test
 public void testTranslateWithAggregateTranslator() {
 String input = "Hello World! 十年永不分离!";
 String expectedOutput = "48656c6c6f20576f726c6421 53414d49544e204e756c696265745c6e65787465726e6f6d7921";
 String actualOutput = aggregateTranslator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithHexTranslator() {
 String input = "Hello  World!";
 String expectedOutput = "48656c6c6f202300686f7720576f726c6421";
 String actualOutput = hexTranslator.translate(input);
 assertEquals(expectedOutput, actualOutput);
 }
}