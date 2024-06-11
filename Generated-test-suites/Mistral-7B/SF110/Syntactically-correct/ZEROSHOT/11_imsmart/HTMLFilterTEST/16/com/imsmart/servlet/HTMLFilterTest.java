package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterEmptyInput() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleCharacterInput() {
 char character = 'A';
 String input = Character.toString(character);
 String expectedOutput = Character.toString(character);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialCharacters() {
 String input = "<>&\"'";
 String expectedOutput = "&lt;&gt;&amp;&quot;&#39;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterLongInput() {
 String input = "<html><head><title>Test</title></head><body><p>This is a <b>test</b> of the <a href=\"http://www.google.com\">HTML</a> filter.</p></body></html>";
 String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;This is a &lt;b&gt;test&lt;/b&gt; of the &lt;a href=&quot;http://www.google.com&quot;&gt;HTML&lt;/a&gt; filter.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

}