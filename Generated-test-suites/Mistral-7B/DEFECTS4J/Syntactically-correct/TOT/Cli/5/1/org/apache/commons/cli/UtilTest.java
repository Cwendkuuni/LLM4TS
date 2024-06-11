package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "--test--";
 expected = "test";
 assertEquals(expected, Util.stripLeadingHyphens(str));

 str = "test";
 assertEquals(str, Util.stripLeadingHyphens(str));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "\"one two\"";
 String expected = "one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "\"one two\"\"";
 expected = "one two";
 assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

 str = "one two";
 assertEquals(str, Util.stripLeadingAndTrailingQuotes(str));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithEmptyString() {
 String str = "";
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensWithNullString() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithEmptyString() {
 String str = "";
 Util.stripLeadingAndTrailingQuotes(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesWithNullString() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}