package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Before
 public void setup() {
 CharSequence[][] lookup = {{" foo", " bar"}, {" baz", " qux"}};
 lookupTranslator = new LookupTranslator(lookup);
 }

 @Test
 public void testTranslateWithValidInput() throws Exception {
 CharSequence input = "foobaz";
 int expectedLength = 3; // length of the longest matched sequence
 int actualLength = lookupTranslator.translate(input, 0, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 String expectedOutput = "barqux";
 String actualOutput = lookupTranslator.translate(input, 0, new StringWriter(64)).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithPartialInput() throws Exception {
 CharSequence input = "foobar";
 int expectedLength = 3;
 int actualLength = lookupTranslator.translate(input, 2, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 String expectedOutput = "bar";
 String actualOutput = lookupTranslator.translate(input, 2, new StringWriter(64)).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithNoMatchingInput() throws Exception {
 CharSequence input = "quxfoobar";
 int expectedLength = 0;
 int actualLength = lookupTranslator.translate(input, 0, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 assertNull(lookupTranslator.translate(input, 0, new StringWriter(64)));
 }

 @Test
 public void testTranslateWithEmptyInput() throws Exception {
 CharSequence input = StringUtils.EMPTY;
 int expectedLength = 0;
 int actualLength = lookupTranslator.translate(input, 0, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 assertNull(lookupTranslator.translate(input, 0, new StringWriter(64)));
 }

 @Test
 public void testTranslateWithInputLongerThanLookupTable() throws Exception {
 CharSequence input = "foobarbazquxfoo";
 int expectedLength = 3;
 int actualLength = lookupTranslator.translate(input, 0, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 String expectedOutput = "barqux";
 String actualOutput = lookupTranslator.translate(input, 0, new StringWriter(64)).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testTranslateWithInputShorterThanShortestLookupSequence() throws Exception {
 CharSequence input = "fo";
 int expectedLength = 0;
 int actualLength = lookupTranslator.translate(input, 0, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 assertNull(lookupTranslator.translate(input, 0, new StringWriter(64)));
 }

 @Test
 public void testTranslateWithInputShorterThanShortestLookupSequenceWithOverlap() throws Exception {
 CharSequence input = "foo";
 int expectedLength = 3;
 int actualLength = lookupTranslator.translate(input, 1, new StringWriter(64)).size();
 assertEquals(expectedLength, actualLength);
 String expectedOutput = "bar";
 String actualOutput = lookupTranslator.translate(input, 1, new StringWriter(64)).toString();
 assertEquals(expectedOutput, actualOutput);
 }
}