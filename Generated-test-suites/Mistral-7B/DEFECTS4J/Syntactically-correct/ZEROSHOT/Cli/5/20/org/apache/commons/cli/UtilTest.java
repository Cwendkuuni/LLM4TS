package org.apache.commons.cli.test; // Change the package name if needed

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String input = "--test";
 String expectedOutput = "test";
 String output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "--test--";
 expectedOutput = "test";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "--";
 expectedOutput = "";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);

 input = "test";
 expectedOutput = "test";
 output = Util.stripLeadingHyphens(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 String input = "\"test\"";
 String expectedOutput = "test";
 String output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "\"test \"";
 expectedOutput = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "\" test \"";
 expectedOutput = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);

 input = "test";
 expectedOutput = "test";
 output = Util.stripLeadingAndTrailingQuotes(input);
 assertEquals(expectedOutput, output);
 }
}