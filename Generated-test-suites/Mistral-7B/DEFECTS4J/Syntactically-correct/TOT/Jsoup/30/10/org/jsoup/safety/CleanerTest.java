package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_WITH_SAFE_TAGS = "<html><body>" +
         "<p id='safeP'>Safe paragraph with <strong>strong</strong> tag and class 'safeClass'.</p>" +
         "</body></html>";

 private static final String HTML_WITH_UNSAFE_TAGS = "<html><body>" +
         "<p id='unsafeP'>Malicious script tag <script src='malicious.js'></script></p>" +
         "</body></html>";

 private static final String HTML_WITH_INVALID_TAGS = "<html><body>" +
         "<p id='invalid'>Invalid tag <invalid> with unallowed attribute 'x' class='unsafeClass'>Invalid content</invalid></p>" +
         "</body></html>";

 private static final String HTML_WITH_INVALID_ATTRIBUTES = "<html><body>" +
         "<p id='attributes'>Valid tag <a href='https://www.google.com'>Link</a> with unallowed attributes 'onclick'='alert(\"Hello\")'></p>" +
         "</body></html>";

 @Test
 public void test_Cleaner_with_valid_html() {
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check that only safe nodes are present in the cleaned document
 Assert.assertEquals("Cleaned document should contain only safe nodes",
         1, cleanDocument.body().childNodeSize());

 Element safeP = cleanDocument.selectFirst("#safeP");
 Assert.assertTrue("Safe paragraph should be present in the cleaned document", safeP != null);
 Assert.assertEquals("Paragraph text should be preserved", "Safe paragraph with <strong>strong</strong> tag and class 'safeClass'.", safeP.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_Cleaner_with_null_whitelist() {
 new Cleaner(null);
 }

 @Test
 public void test_Cleaner_with_invalid_html() {
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document invalidDocument = Jsoup.parse(HTML_WITH_INVALID_TAGS);
 Document cleanInvalidDocument = cleaner.clean(invalidDocument);

 // Check that the cleaned document is empty
 Assert.assertEquals("Cleaned document should be empty", 0, cleanInvalidDocument.body().childNodeSize());
 }

 @Test
 public void test_Cleaner_with_script_tag() {
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(HTML_WITH_UNSAFE_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check that the script tag is removed
 Assert.assertEquals("Cleaned document should not contain script tag", 0, cleanDocument.body().childNodeSize());
 }

 @Test
 public void test_Cleaner_with_invalid_attributes() {
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(HTML_WITH_INVALID_ATTRIBUTES);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check that the cleaned document is empty
 Assert.assertEquals("Cleaned document should be empty", 0, cleanDocument.body().childNodeSize());
 }

 @Test
 public void test_Cleaner_with_valid_html_and_validation() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(HTML_WITH_SAFE_TAGS);
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Check that the document is valid
 Assert.assertTrue("Document should be valid", isValid);
 }

 @Test
 public void test_Cleaner_with_invalid_html_and_validation() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document invalidDocument = Jsoup.parse(HTML_WITH_INVALID_TAGS);
 boolean isInvalid = cleaner.isValid(invalidDocument);

 // Check that the document is invalid
 Assert.assertFalse("Document should be invalid", isInvalid);
 }
}