package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 private String input;
 private String expectedOutput;
 private StringWriter out;
 private NumericEntityUnescaper unescaper;

 @Test
 public void testDefaultConstructor() {
 unescaper = new NumericEntityUnescaper();
 assertFalse(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testConstructorWithOptions() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 }

 @Test
 public void testIsSetMethod() {
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 assertTrue(unescaper.isSet(OPTION.semiColonRequired));
 assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
 }

 @Test
 public void testTranslateMethod_NoEntityFound() {
 input = "This is a test.";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals("", out.toString());
 }

 @Test
 public void testTranslateMethod_SimpleNumericEntity() {
 input = "&num;";
 expectedOutput = "0";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_HexNumericEntity() {
 input = "&#x1F44D;";
 expectedOutput = "\uD83E\uDD1D";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_SimpleNumericEntityWithSemiColon() {
 input = "&num;";
 expectedOutput = "0";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_HexNumericEntityWithSemiColon() {
 input = "&#x1F44D;";
 expectedOutput = "\uD83E\uDD1D";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethod_NoSemiColonAndErrorIfNoSemiColon() {
 input = "&num";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethod_NoSemiColonAndErrorIfNoSemiColon_MultipleEntities() {
 input = "&num&num";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateMethod_InvalidHexEntity() {
 input = "&#xz1F44D;";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateMethod_InvalidDecimalEntity() {
 input = "&#z1F44D;";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslateMethod_EntityOutOfRange() {
 input = "&&#65536;";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper();
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethod_SemiColonRequiredButNoSemiColon_SingleEntity() {
 input = "&num";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethod_SemiColonRequiredButNoSemiColon_MultipleEntities() {
 input = "&num&num";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 unescaper.translate(new StringReader(input), 0, out);
 }

 @Test
 public void testTranslateMethod_SemiColonOptional() {
 input = "&num";
 expectedOutput = "0";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_SemiColonOptional_MultipleEntities_SingleEntityWithSemiColon() {
 input = "&num; &num";
 expectedOutput = "00";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_SemiColonOptional_MultipleEntities_BothEntitiesMissingSemiColon() {
 input = "&num &num";
 expectedOutput = "00";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

 @Test
 public void testTranslateMethod_SemiColonOptional_MultipleEntities_FirstEntityWithSemiColon_SecondEntityMissingSemiColon() {
 input = "&num; &num";
 expectedOutput = "00";
 out = new StringWriter();
 unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 unescaper.translate(new StringReader(input), 0, out);
 assertEquals(expectedOutput, out.toString());
 }

}