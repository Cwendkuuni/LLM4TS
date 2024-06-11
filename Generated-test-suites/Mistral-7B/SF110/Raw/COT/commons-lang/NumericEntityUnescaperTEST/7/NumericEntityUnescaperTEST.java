package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

  @Test
  public void testNumericEntityUnescaperConstructorWithOptions() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
    assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
  }

  @Test
  public void testNumericEntityUnescaperConstructorWithoutOptions() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
  }

  @Test
  public void testIsSet() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
    assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateWithInvalidNumericEntity() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    String testInput = "&####";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    fail("Expected IllegalArgumentException");
  }

  @Test
  public void testTranslateWithValidNumericEntity() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    String testInput = "this is a &amp; test &#123;abcd&#125;";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    assertEquals("this is a &amp; test &abc&;", out.toString());
    assertEquals(15, length);
  }

  @Test
  public void testTranslateWithValidNumericEntityHex() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    String testInput = "this is a &amp; test &#x41;&#x42;&#x43;&#x44;&#x45;&#x46;";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    assertEquals("this is a &amp; test ABCDE", out.toString());
    assertEquals(21, length);
  }

  @Test
  public void testTranslateWithValidNumericEntityNoSemiColon() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
    String testInput = "this is a &amp; test &#123;abcd";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    assertEquals("this is a &amp; test abcd", out.toString());
    assertEquals(13, length);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateWithInvalidNumericEntityNoSemiColon() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
    String testInput = "this is a &amp; test &#123;abcd";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    fail("Expected IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateWithInvalidNumericEntitySemiColonRequired() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
    String testInput = "this is a &amp; test &#123;abcd";
    StringWriter out = new StringWriter();
    int length = unescaper.translate(new StringQuasar(testInput), 0, out);
    fail("Expected IllegalArgumentException");
  }
}