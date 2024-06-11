package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

 // Test case for filter method with null input
 @Test(expected = NullPointerException.class)
 public void testFilterNullInput() {
 String message = null;
 HTMLFilter.filter(message);
 }

 // Test case for filter method with empty input
 @Test
 public void testFilterEmptyInput() {
 String message = "";
 String expected = "";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 // Test case for filter method with valid input
 @Test
 public void testFilterValidInput() {
 String message = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 // Test case for filter method with input containing '&'
 @Test
 public void testFilterInputContainingAmpersand() {
 String message = "The ampersand symbol is &.";
 String expected = "The ampersand symbol is &amp;.";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 // Test case for filter method with input containing '>'
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingGreaterThan() {
 String message = "<html>";
 String expectedErrorMessage = "Input contains '>', which cannot be filtered.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '<' at the beginning
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingLessThanAtBeginning() {
 String message = "<html>";
 String expectedErrorMessage = "Input cannot start with '<'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '<' at the end
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingLessThanAtEnd() {
 String message = "html>";
 String expectedErrorMessage = "Input cannot end with '<'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '>" at the beginning
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingGreaterThanAtBeginning() {
 String message = ">html";
 String expectedErrorMessage = "Input cannot start with '>'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '>' at the end
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingGreaterThanAtEnd() {
 String message = "html>";
 String expectedErrorMessage = "Input cannot end with '>'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '&' at the beginning
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingAmpersandAtBeginning() {
 String message = "&html";
 String expectedErrorMessage = "Input cannot start with '&'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '&' at the end
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingAmpersandAtEnd() {
 String message = "html&";
 String expectedErrorMessage = "Input cannot end with '&'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '\"' at the beginning
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingDoubleQuotesAtBeginning() {
 String message = "\"html";
 String expectedErrorMessage = "Input cannot start with '\"'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing '\"' at the end
 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputContainingDoubleQuotesAtEnd() {
 String message = "html\"";
 String expectedErrorMessage = "Input cannot end with '\"'.";
 String result = null;
 try {
 result = HTMLFilter.filter(message);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 // Test case for filter method with input containing both '&' and '>'
 @Test
 public void testFilterInputContainingBothAmpersandAndGreaterThan() {
 String message = "The ampersand symbol is &gt;.";
 String expected = "The ampersand symbol is &amp;gt;.";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 // Test case for filter method with input containing both '<' and '>'
 @Test
 public void testFilterInputContainingBothLessThanAndGreaterThan() {
 String message = "<html>The <b>text</b> is &gt; great&lt;!";
 String expected = "&lt;html&gt;&lt;The &lt;b&gt;text&lt;/b&gt;&gt; is &amp;gt; great&lt;!&lt;/html&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 // Test case for filter method with input containing '<', '>', '&', and '\"'
 @Test
 public void testFilterInputContainingAllSpecialCharacters() {
 String message = "<html>&quot;The <b>text</b> is <i>great</i>! &gt; &lt;hello&gt; world&quot;";
 String expected = "&lt;html&gt;&amp;quot;The &lt;b&gt;text&lt;/b&gt;&amp;gt; is &amp;lt;i&gt;great&lt;/i&gt;&amp;gt;! &lt;?hello&gt; world&amp;quot;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }
}