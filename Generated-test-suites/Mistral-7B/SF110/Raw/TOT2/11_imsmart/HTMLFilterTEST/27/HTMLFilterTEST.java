// Your complete JUnit 4 test file should start with this comment

package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
// Test cases for public methods in HTMLFilter class

// Test filter method
@Test
public void testFilter() {
String input = "<h1>Hello World</h1>";
String expectedOutput = "&lt;h1&gt;Hello World&lt;/h1&gt;";
String output = HTMLFilter.filter(input);
assertEquals(expectedOutput, output);
}

// Test filter empty string
@Test
public void testFilterEmptyString() {
String input = "";
String expectedOutput = "";
String output = HTMLFilter.filter(input);
assertEquals(expectedOutput, output);
}

// Test filter null string
@Test
public void testFilterNullString() {
String input = null;
String expectedOutput = null;
String output = HTMLFilter.filter(input);
assertEquals(expectedOutput, output);
}

// Test filter special characters
@Test
public void testFilterSpecialCharacters() {
String input = "This is a test with !@#$%^&*()_+={}[];:'\"|,.<>/?";
String expectedOutput = "This is a test with !&amp;#64;$%&amp;#36;%&amp;#26;%&amp;#32;%&amp;#60;%&amp;#62;%&amp;#62;%&amp;#34;%&amp;#58;%&amp;#59;%&amp;#56;%&amp;#55;%&amp;#52;%&amp;#47;%&amp;#63;%&amp;#46;%&amp;#47;%&amp;#59;%&amp;#52;%&amp;#56;%&amp;#44;%&amp;#60;%&amp;#40;";
String output = HTMLFilter.filter(input);
assertEquals(expectedOutput, output);
}

// Test filter malformed input (NUL character)
@Test(expected = NullPointerException.class)
public void testFilterMalformedInput() {
String input = "\0"; // NUL character
HTMLFilter.filter(input);
}
}

// Your complete JUnit 4 test file should finish with this comment