package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private CharSequence[][] lookupTable = new CharSequence[][] {
 {"hello", "greetings"},
 {"world", "planet"},
 {"java", "programming_language"},
 {"test", "examination"}
 };

 @Test
 public void testTranslate() throws Exception {
 LookupTranslator lookupTranslator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();

 // Test translating a sequence that matches an exact key
 CharSequence input = "hello world";
 int index = 0;
 int actualLength = lookupTranslator.translate(input, index, out);
 String expected = "greetings planet";
 String actual = out.toString().substring(0, actualLength);
 assertEquals(expected, actual);

 // Test translating a sequence that is a substring of a key
 input = "java world";
 index = 0;
 actualLength = lookupTranslator.translate(input, index, out);
 expected = "programming_language ";
 actual = out.toString().substring(0, actualLength);
 assertEquals(expected, actual);

 // Test translating a sequence that is not in the lookup table
 input = "python world";
 index = 0;
 lookupTranslator.translate(input, index, out);
 assertEquals("", out.toString());

 // Test translating an empty sequence
 input = "";
 index = 0;
 lookupTranslator.translate(input, index, out);
 assertEquals("", out.toString());

 // Test translating a sequence that is longer than the longest key
 input = "helloworldhelloworldhelloworld";
 index = 0;
 lookupTranslator.translate(input, index, out);
 assertEquals("", out.toString());
 }
}