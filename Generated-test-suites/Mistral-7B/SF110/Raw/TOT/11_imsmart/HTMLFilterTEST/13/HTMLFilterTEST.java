package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
   // Expert 1's test case
   @Test
   public void testFilter() {
      String input = "<h1>Hello & World!</h1>";
      String expectedOutput = "&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;";
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   // Expert 2's test cases
   @Test
   public void testFilterEmptyString() {
      String input = "";
      String expectedOutput = "";
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testFilterNullString() {
      String input = null;
      String expectedOutput = null;
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testFilterSpecialCases() {
      String input = "&<>'\"";
      String expectedOutput = "&amp;&lt;&gt;&amp;amp;&quot;";
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   // Expert 3's test cases
   @Test(expected = NullPointerException.class)
   public void testFilterNullInputArray() {
      char[] inputChars = null;
      String expectedOutput = null;
      try {
         String output = HTMLFilter.filter(new String(inputChars));
      } catch (NullPointerException e) {
         assertEquals(expectedOutput, null);
      }
   }

   @Test
   public void testFilterLongInput() {
      String longInput = "A long string with more than 10000 characters.";
      String expectedOutput = new String(new char[10001]).replace('\0', ' ').replaceAll(" ", "x");
      String output = HTMLFilter.filter(longInput);
      assertTrue(expectedOutput.length() > output.length());
   }

   @Test
   public void testFilterCaseSensitivity() {
      String input = "<h1>Hello & World!</h1>";
      String expectedOutput = "&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;";
      String output = HTMLFilter.filter(input.toLowerCase());
      assertEquals(expectedOutput, output);
   }

   // Add any other test cases if needed

   // Edge cases
   @Test
   public void testFilterEmptyFilteredString() {
      String input = "";
      String expectedOutput = "";
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   @Test
   public void testFilterFilteredStringWithOnlySpecialChars() {
      String input = "&<>'\"";
      String expectedOutput = "&amp;&amp;&lt;&gt;&amp;amp;&amp;amp;&quot;";
      String output = HTMLFilter.filter(input);
      assertEquals(expectedOutput, output);
   }

   // Error scenarios
   @Test(expected = IllegalArgumentException.class)
   public void testFilterNegativeInputLength() {
      String input = new String(new char[0]);
      String expectedOutput = null;
      try {
         String output = HTMLFilter.filter(input);
      } catch (IllegalArgumentException e) {
         assertEquals(expectedOutput, null);
      }
   }

   @Test(expected = NullPointerException.class)
   public void testFilterNullContentArray() {
      char[] inputChars = null;
      String expectedOutput = null;
      try {
         String output = HTMLFilter.filter(new String(inputChars));
      } catch (NullPointerException e) {
         assertEquals(expectedOutput, null);
      }
   }
}