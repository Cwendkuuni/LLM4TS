package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateSimpleEntity() throws Exception {
 String input = "&amp;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("&", out.toString().substring(0, 1));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateHexEntity() throws Exception {
 String input = "&amp;#x41;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("A", out.toString().substring(0, 1));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateDecEntity() throws Exception {
 String input = "&amp;#65";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("A", out.toString().substring(0, 1));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateInvalidEntity() throws Exception {
 String input = "&amp;#123";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateEntityWithSemiColon() throws Exception {
 String input = "&amp;#123;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString().substring(0, 1));
 assertEquals(";", out.toString().substring(1, 2));
 assertEquals(1, length);
 }

 @Test
 public void testTranslateEntityWithSemiColonOptional() throws Exception {
 String input = "&amp;#123";
 NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString().substring(0, 1));
 assertEquals(1, length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateEntityMissingSemiColon() throws Exception {
 String input = "&amp;#123";
 NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testTranslateEntityWithHexOutOfRange() throws Exception {
 String input = "&amp;#65537";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateEntityWithDecOutOfRange() throws Exception {
 String input = "&amp;#65536";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString());
 assertEquals(0, length);
 }

 @Test
 public void testTranslateMultipleEntities() throws Exception {
 String input = "&amp;#123;&amp;#45;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper();
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals("", out.toString().substring(0, 1));
 assertEquals(";", out.toString().substring(1, 2));
 assertEquals("", out.toString().substring(2, 3));
 assertEquals("", out.toString().substring(3, 4));
 assertEquals(4, length);
 }

 @Test
 public void testTranslateEntitiesWithSemiColonRequired() throws Exception {
 String input = "&amp;#123;&amp;#45;";
 NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
 StringWriter out = new StringWriter();
 int length = translator.translate(new StringQuasar(input), 0, out);
 assertEquals(";", out.toString().substring(0, 1));
 assertEquals("", out.toString().substring(1, 2));
 assertEquals("", out.toString().substring(2, 3));
 assertEquals(";", out.toString().substring(3, 4));
 assertEquals(5, length);
 }
}