package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.connect;
import static org.jsoup.Jsoup.parse;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 Validate.notNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertThat(cleanDocument.body(), is(nullValue()));
 }

 @Test
 public void testIsValidDocument() {
 Document validDocument = parse("<html><body><p>Hello World</p></body></html>");
 Document invalidDocument = parse("<html><body><script>alert('XSS');</script></body></html>");

 assertThat(cleaner.isValid(validDocument), is(true));
 assertThat(cleaner.isValid(invalidDocument), is(false));
 }

 @Test
 public void testCopySafeNodes() {
 Document dirtyDocument = parse("<html><body><p id='test1' class='testClass'><img src='test.jpg' alt='test' title='test'/><script>alert('XSS');</script></p></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

 Element pElement = cleanDocument.body().getElementsByTag("p").first();

 assertThat(pElement.select("script").size(), is(0));
 assertThat(pElement.id(), is("test1"));
 assertThat(pElement.classes(), hasItem("testClass"));
 assertThat(pElement.images().get(0).src(), is("test.jpg"));
 assertThat(pElement.images().get(0).attributes().get("alt"), is("test"));
 assertThat(numDiscarded, is(1));
 }

 @Test
 public void testIsSafeTagAndAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("id", "test1");
 attributes.put("class", "testClass");
 Element element = new Element("div", null, attributes);

 assertThat(cleaner.whitelist.isSafeTag("div", element), is(true));
 assertThat(cleaner.whitelist.isSafeAttribute("div", element, attributes.get("id")), is(true));
 assertThat(cleaner.whitelist.isSafeAttribute("div", element, attributes.get("class")), is(true));
 }

 @Test
 public void testIsNotSafeTagAndAttribute() {
 Attributes attributes = new Attributes();
 attributes.put("script", "alert('XSS')");
 Element element = new Element("div", null, attributes);

 assertThat(cleaner.whitelist.isSafeTag("div", element), is(false));
 for (String attributeName : Arrays.asList("script")) {
 String attributeValue = attributes.get(attributeName);
 assertThat(cleaner.whitelist.isSafeAttribute("div", element, attributeName), is(false));
 }
 }
}