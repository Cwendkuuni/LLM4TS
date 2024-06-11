package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for the LookupTranslator class.
 */
public class LookupTranslatorTest {

 private static final CharSequence[][] LOOKUP = {
 {"hello", "hi"},
 {"world", "planet"},
 {"Java", "Python"},
 {"programming", "coding"},
 {"is", "equals"},
 {"fun", "enjoyable"},
 {"and", "but"},
 {"challenging", "hard"},
 {"for", "because"},
 {"me", "I"}
 };

 @Test
 public void testTranslate() throws Exception {
 LookupTranslator translator = new LookupTranslator(LOOKUP);

 // Test typical usage
 assertEquals("hi", translator.translate(CharSequence.class.newInstance("hello"), 0, new StringWriter()));
 assertEquals("planet", translator.translate(CharSequence.class.newInstance("world"), 0, new StringWriter()));
 assertEquals("Python", translator.translate(CharSequence.class.newInstance("Java"), 0, new StringWriter()));

 // Test edge cases
 assertEquals("", translator.translate(CharSequence.class.newInstance(""), 0, new StringWriter()));
 assertEquals("coding", translator.translate(CharSequence.class.newInstance("programming"), 0, new StringWriter()));
 assertEquals("equals", translator.translate(CharSequence.class.newInstance("is"), 0, new StringWriter()));

 // Test error scenarios
 assertNull(translator.translate(CharSequence.class.newInstance(""), 1, new StringWriter())); // index out of bounds
 assertNull(translator.translate(CharSequence.class.newInstance("Java"), -1, new StringWriter())); // negative index
 assertNull(translator.translate(CharSequence.class.newInstance("JavaX"), 0, new StringWriter())); // no match
 }

 /**
 * Helper method to create a StringWriter.
 */
 private static Writer createStringWriter() {
 return new StringWriter();
 }
}