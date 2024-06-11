package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest {

 /**
  * Test method for {@link Util#stripLeadingHyphens(String)} with no leading hyphens.
  */
 @Test
 public void testStripLeadingHyphensNoLeadingHyphens() {
 String str = "someString";
 assertEquals("someString", Util.stripLeadingHyphens(str));
 }

 /**
  * Test method for {@link Util#stripLeadingHyphens(String)} with leading hyphens.
  */
 @Test
 public void testStripLeadingHyphensLeadingHyphens() {
 String str = "--someString";
 assertEquals("someString", Util.stripLeadingHyphens(str));
 }

 /**
  * Test method for {@link Util#stripLeadingHyphens(String)} with no hyphens.
  */
 @Test
 public void testStripLeadingHyphensNoHyphens() {
 String str = "someString";
 assertEquals("someString", Util.stripLeadingHyphens(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with no quotes.
  */
 @Test
 public void testStripLeadingAndTrailingQuotesNoQuotes() {
 String str = "someString";
 assertEquals("someString", Util.stripLeadingAndTrailingQuotes(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with leading quotes.
  */
 @Test
 public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
 String str = "\"someString\"";
 assertEquals("someString", Util.stripLeadingAndTrailingQuotes(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with trailing quotes.
  */
 @Test
 public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
 String str = "\"someString\"";
 assertEquals("someString", Util.stripLeadingAndTrailingQuotes(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with leading and trailing quotes.
  */
 @Test
 public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes() {
 String str = "\"someString\"";
 assertEquals("someString", Util.stripLeadingAndTrailingQuotes(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with empty string.
  */
 @Test
 public void testStripLeadingAndTrailingQuotesEmptyString() {
 String str = "";
 assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
 }

 /**
  * Test method for {@link Util#stripLeadingAndTrailingQuotes(String)} with null string.
  */
 @Test(expected = NullPointerException.class)
 public void testStripLeadingAndTrailingQuotesNullString() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}