package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper getNumericEntityUnescaper(OPTION... options) {
 return new NumericEntityUnescaper(options);
 }

 @Test
 public void testTranslateWithDefaultOptions() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "&num;Hello &#x1F449; World!";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 🤓 World!", result.substring(0, length));
 }

 @Test
 public void testTranslateWithSemiColonRequired() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&num;Hello &#x1F449; World";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 🤓 World", result.substring(0, length));
 Assert.assertEquals("", result.substring(result.length() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithSemiColonRequiredAndNoSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
 String input = "&num;Hello &#x1F449; World";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 fail("Expected IllegalArgumentException, but no exception was thrown");
 }

 @Test
 public void testTranslateWithSemiColonOptional() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&num;Hello &#x1F449; World!";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 🤓 World!", result.substring(0, length));
 }

 @Test
 public void testTranslateWithErrorIfNoSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&num;Hello &#x1F449; World";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 fail("Expected IllegalArgumentException, but no exception was thrown");
 }

 @Test
 public void testTranslateWithHexValue() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 String input = "&num;Hello &#x1F303;&#x1F304; World!";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 👴🏽👵🏽 World!", result.substring(0, length));
 }

 @Test
 public void testTranslateWithHexValueAndSemiColonRequired() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.semiColonRequired);
 String input = "&num;Hello &#x1F303;&#x1F304; World";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 👴🏽👵мя", result.substring(0, length));
 Assert.assertEquals("", result.substring(result.length() - 1));
 }

 @Test
 public void testTranslateWithHexValueAndSemiColonOptional() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&num;Hello &#x1F303;&#x1F304; World!";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 String result = output.toString();
 assertEquals("numHello 👴🏽👵🏽 World!", result.substring(0, length));
 }

 @Test
 public void testTranslateWithHexValueAndErrorIfNoSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = getNumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&num;Hello &#x1F303;&#x1F304; World";
 StringWriter output = new StringWriter();
 int length = unescaper.translate(new StringReader(input), 0, output);
 fail("Expected IllegalArgumentException, but no exception was thrown");
 }

}