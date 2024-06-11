package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("org.jsoup.parser.ParserQueryParser")
public class CleanerTest {

 @Test
 public void testCleanerInstance() {
 Cleaner cleaner = new Cleaner(new Whitelist());
 Validate.notNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendText("Hello <script>world</script>");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(new Whitelist());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("Hello", cleanDocument.body().text());
 assertNull(cleanDocument.body().childNodes().get(0).nodeName());
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendText("Hello <script>world</script>");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(new Whitelist());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocument_valid() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendText("Hello <p>world</p>");
 dirtyDocument.setBody(body);

 Cleaner cleaner = new Cleaner(new Whitelist());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendText("Hello <script>world</script> <p>Hello <b>world</b></p>");
 dirtyDocument.setBody(body);

 Element dest = new Element("div");
 int numDiscarded = Cleaner.copySafeNodes(body, dest);

 assertEquals(1, numDiscarded);
 assertEquals(1, dest.childNodes().size());
 assertTrue(dest.childNodes().get(0) instanceof TextNode);
 assertEquals("Hello ", ((TextNode) dest.childNodes().get(0)).text());
 }

 @Test
 public void testCopySafeNodes_safe_tag() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendElement(new Element("p").text("Hello <b>world</b>"));
 dirtyDocument.setBody(body);

 Element dest = new Element("div");
 int numDiscarded = Cleaner.copySafeNodes(body, dest);

 assertEquals(0, numDiscarded);
 assertEquals(1, dest.childNodes().size());
 assertTrue(dest.childNodes().get(0) instanceof Element);
 assertTrue(((Element) dest.childNodes().get(0)).tagName().equals("p"));
 }

 @Test
 public void testCopySafeNodes_notSafeTag() {
 Document dirtyDocument = Document.createShell("http://example.com");
 Element body = new Element("body");
 body.appendElement(new Element("script").text("alert('hello')"));
 dirtyDocument.setBody(body);

 Element dest = new Element("div");
 int numDiscarded = Cleaner.copySafeNodes(body, dest);

 assertEquals(1, numDiscarded);
 assertNull(dest.childNodes());
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceEl = new Element("script");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("src", "http://script.com");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner().createSafeElement(sourceEl);

 assertEquals("script", meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 assertTrue(meta.el.attribute("src") == null);
 }

 @Test
 public void testCreateSafeElement_safeAttribute() {
 Element sourceEl = new Element("p");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("class", "test");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist().addTag("p", new Whitelist.AttributeSpec("class"))).createSafeElement(sourceEl);

 assertEquals("p", meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 assertTrue(meta.el.attribute("class").getValue().equals("test"));
 }

 @Test
 public void testCreateSafeElement_notSafeAttribute() {
 Element sourceEl = new Element("p");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("xss", "alert('hello')");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist()).createSafeElement(sourceEl);

 assertEquals("p", meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 assertNull(meta.el.attribute("xss"));
 }

 @Test
 public void testCreateSafeElement_multipleAttributes() {
 Element sourceEl = new Element("input");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("type", "text");
 sourceAttrs.put("id", "myId");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist().addTag("input", new Whitelist.AttributeSpec("type"), new Whitelist.AttributeSpec("id"))).createSafeElement(sourceEl);

 assertEquals("input", meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 assertTrue(meta.el.attribute("type").getValue().equals("text"));
 assertTrue(meta.el.attribute("id").getValue().equals("myId"));
 }

 @Test
 public void testCreateSafeElement_multipleAttributes_notSafe() {
 Element sourceEl = new Element("input");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("type", "text");
 sourceAttrs.put("xss", "alert('hello')");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist()).createSafeElement(sourceEl);

 assertEquals("input", meta.el.tagName());
 assertEquals(1, meta.numAttribsDiscarded);
 assertNull(meta.el.attribute("xss"));
 }

 @Test
 public void testCreateSafeElement_enforcedAttribute() {
 Element sourceEl = new Element("input");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("type", "text");
 sourceAttrs.put("required", "");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist().addTag("input", new Whitelist.AttributeSpec("type"), new Whitelist.AttributeSpec("required"))).createSafeElement(sourceEl);

 assertEquals("input", meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 assertTrue(meta.el.attribute("required").getValue().equals("required"));
 }

 @Test
 public void testCreateSafeElement_multipleEnforcedAttributes() {
 Element sourceEl = new Element("input");
 Attributes sourceAttrs = new Attributes();
 sourceAttrs.put("type", "text");
 sourceAttrs.put("required", "");
 sourceAttrs.put("placeholder", "Name");
 sourceEl.attributes(sourceAttrs);

 ElementMeta meta = new Cleaner(new Whitelist().addTag("input", new Whitelist.AttributeSpec("type"), new Whitelist.AttributeSpec("required"), new Whitelist.AttributeSpec("placeholder"))).createSafeElement(sourceEl);

 assertEquals("input", meta.el.tagName());
 assertEquals(0, meta.numAttribsDiscarded);
 assertTrue(meta.el.attribute("required").getValue().equals("required"));
 assertTrue(meta.el.attribute("placeholder").getValue().equals("Name"));
 }

 @Test
 public void testIsSafeTag() {
 Cleaner cleaner = new Cleaner(new Whitelist());

 assertTrue(cleaner.whitelist.isSafeTag("p"));
 assertFalse(cleaner.whitelist.isSafeTag("script"));
 }

 @Test
 public void testIsSafeAttribute() {
 Cleaner cleaner = new Cleaner(new Whitelist().addTag("p", new Whitelist.AttributeSpec("class")));

 assertTrue(cleaner.whitelist.isSafeAttribute("p", new Element("p"), new Attribute("class")));
 assertFalse(cleaner.whitelist.isSafeAttribute("p", new Element("p"), new Attribute("xss")));
 }

 @Test
 public void testGetEnforcedAttributes() {
 Cleaner cleaner = new Cleaner(new Whitelist().addTag("input", new Whitelist.AttributeSpec("type"), new Whitelist.AttributeSpec("required"), new Whitelist.AttributeSpec("placeholder")));

 List<Attribute> enforcedAttrs = cleaner.whitelist.getEnforcedAttributes("input");
 List<Attribute> expectedEnforcedAttrs = new ArrayList<>();
 expectedEnforcedAttrs.add(new Attribute("type", ""));
 expectedEnforcedAttrs.add(new Attribute("required", "required"));
 expectedEnforcedAttrs.add(new Attribute("placeholder", ""));

 assertEquals(expectedEnforcedAttrs, enforcedAttrs);
 }
}