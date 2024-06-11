package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 // Test for stripLeadingHyphens method
 @Test
 public void testStripLeadingHyphens() {
 String str = "--test-string";
 String expected = "test-string";
 String actual = Util.stripLeadingHyphens(str);
 assertEquals(expected, actual);

 String str1 = "-test-string";
 String expected1 = "test-string";
 String actual1 = Util.stripLeadingHyphens(str1);
 assertEquals(expected1, actual1);

 String str2 = "test-string";
 String expected2 = "test-string";
 String actual2 = Util.stripLeadingHyphens(str2);
 assertEquals(expected2, actual2);
 }

 // Test for stripLeadingAndTrailingQuotes method
 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String str = "'one two'";
 String expected = "one two";
 String actual = Util.stripLeadingAndTrailingQuotes(str);
 assertEquals(expected, actual);

 String str1 = "one two";
 String expected1 = "one two";
 String actual1 = Util.stripLeadingAndTrailingQuotes(str1);
 assertEquals(expected1, actual1);

 String str2 = '"one two"';
 String expected2 = "one two";
 String actual2 = Util.stripLeadingAndTrailingQuotes(str2);
 assertEquals(expected2, actual2);

 String str3 = "one 'two'";
 String expected3 = "one two";
 String actual3 = Util.stripLeadingAndTrailingQuotes(str3);
 assertEquals(expected3, actual3);

 String str4 = "one 'two' three";
 String expected4 = "one two three";
 String actual4 = Util.stripLeadingAndTrailingQuotes(str4);
 assertEquals(expected4, actual4);
}

 // Test for edge cases
 @Test
 public void testEdgeCases() {
 String str1 = "---test-string---";
 String expected1 = "test-string";
 String actual1 = Util.stripLeadingHyphens(str1);
 assertEquals(expected1, actual1);

 String str2 = "--test-string--";
 String expected2 = "test-string";
 String actual2 = Util.stripLeadingHyphens(str2);
 assertEquals(expected2, actual2);

 String str3 = "--test-string-- --another-string--";
 String expected3 = "test-string another-string";
 String actual3 = Util.stripLeadingHyphens(str3);
 assertEquals(expected3, actual3);

 String str4 = "\"\"\"test-string\"\"\"";
 String expected4 = "test-string";
 String actual4 = Util.stripLeadingAndTrailingQuotes(str4);
 assertEquals(expected4, actual4);

 String str5 = "\"test-string\"";
 String expected5 = "test-string";
 String actual5 = Util.stripLeadingAndTrailingQuotes(str5);
 assertEquals(expected5, actual5);

 String str6 = "\"test-string\" \"another-string\"";
 String expected6 = "test-string another-string";
 String actual6 = Util.stripLeadingAndTrailingQuotes(str6);
 assertEquals(expected6, actual6);
}

 // Test for error scenarios
 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 Util.stripLeadingHyphens(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException2() {
 Util.stripLeadingAndTrailingQuotes(null);
 }
}