package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;
 private StringWriter writer;

 @Before
 public void setUp() {
 unescaper = new NumericEntityUnescaper();
 writer = new StringWriter();
 }

 @Test
 public void testTranslateWithDefaultOptions() {
 String input = "&amp;lt;Hello&amp;#x41;World&amp;#123;&#x61;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "&lt;Hello&#x41;World&#123;a";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithSemiColonRequiredOption() {
 unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
 String input = "&amp;#123;";
 try {
 unescaper.translate(new StringReader(input), 0, writer);
 fail("Expected IllegalArgumentException with message 'Semi-colon required at end of numeric entity'");
 } catch (IllegalArgumentException e) {
 // expected
 }

 input = "&amp;lt;Hello&amp;#x41;World&amp;#123;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "&lt;Hello&#x41;World";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithSemiColonOptionalOption() {
 unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
 String input = "&amp;lt;Hello&amp;#x41;World&amp;#123";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "&lt;Hello&#x41;World#123";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColonOption() {
 unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
 String input = "&amp;lt;Hello&amp;#x41;World&amp;#123";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "&lt;Hello&#x41;World#123";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));

 input = "&amp;lt;Hello&amp;#x41;World&amp;#123";
 try {
 unescaper.translate(new StringReader(input), 0, writer);
 fail("Expected IllegalArgumentException with message 'Semi-colon required at end of numeric entity'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTranslateWithHexEntities() {
 String input = "&amp;#x26;#x61;#x6C;#x6C;#x6F;#x72;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "&amp;#xF0;a;b;c;o;d;";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithInvalidEntity() {
 String input = "&amp;#x1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 assertEquals(0, translatedLength);
 }

 @Test
 public void testTranslateWithLongEntity() {
 String input = "&amp;#1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 assertEquals(0, translatedLength);
 }

 @Test
 public void testTranslateWithValidEntities() {
 String input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "{\r\n a\n ~\u007F\uffff";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithValidEntitiesAndSemiColonRequiredOption() {
 unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
 String input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "{\r\n a\n ~\u007F\uffff";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));

 input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 try {
 unescaper.translate(new StringReader(input), 0, writer);
 fail("Expected IllegalArgumentException with message 'Semi-colon required at end of numeric entity'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTranslateWithValidEntitiesAndSemiColonOptionalOption() {
 unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
 String input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "{\r\n a\n ~\u007F\uffff";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithValidEntitiesAndErrorIfNoSemiColonOption() {
 unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
 String input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 int translatedLength = unescaper.translate(new StringReader(input), 0, writer);
 String expectedOutput = "{\r\n a\n ~\u007F\uffff";
 assertEquals(expectedOutput, writer.toString().substring(0, translatedLength));

 input = "&amp;#123; &amp;#153; &amp;#160; &amp;#x61; &amp;#xA0; &amp;#x7F &amp;#xFF;";
 try {
 unescaper.translate(new StringReader(input), 0, writer);
 fail("Expected IllegalArgumentException with message 'Semi-colon required at end of numeric entity'");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}