package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String str = "--test";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);

 String str1 = "-test";
 String expected1 = "test";
 String result1 = Util.stripLeadingHyphens(str1);
 Assert.assertEquals(expected1, result1);

 String str2 = "test";
 String expected2 = "test";
 String result2 = Util.stripLeadingHyphens(str2);
 Assert.assertEquals(expected2, result2);
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "'one two'";
 String expected = "one two";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);

 String str1 = "\"one two\"";
 String expected1 = "one two";
 String result1 = Util.stripLeadingAndTrailingQuotes(str1);
 Assert.assertEquals(expected1, result1);

 String str2 = "one two";
 String expected2 = "one two";
 String result2 = Util.stripLeadingAndTrailingQuotes(str2);
 Assert.assertEquals(expected2, result2);

 String str3 = "\"'one two'\":";
 String expected3 = "one two";
 String result3 = Util.stripLeadingAndTrailingQuotes(str3);
 Assert.assertEquals(expected3, result3);
 }

 // Test for edge cases of stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphensEdgeCases() {
 String str = "--test--";
 String expected = "test";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);

 String str1 = "--test--";
 String expected1 = "test";
 String result1 = Util.stripLeadingHyphens(str1);
 Assert.assertEquals(expected1, result1);
 }

 // Test for error scenarios of stripLeadingHyphens method
 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingHyphensError() {
 String str = null;
 Util.stripLeadingHyphens(str);
 }

 // Test for edge cases of stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotesEdgeCases() {
 String str = "\"\"";
 String expected = "";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);

 String str1 = "\"\"one two\"\"";
 String expected1 = "one two";
 String result1 = Util.stripLeadingAndTrailingQuotes(str1);
 Assert.assertEquals(expected1, result1);
 }

 // Test for error scenarios of stripLeadingAndTrailingQuotes method
 @Test(expected = IllegalArgumentException.class)
 public void testStripLeadingAndTrailingQuotesError() {
 String str = null;
 Util.stripLeadingAndTrailingQuotes(str);
 }
}