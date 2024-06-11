package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes. PutAttribute;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;

import java.util.Arrays;
import static org.junit.Assert.*;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes attrs = new Attributes();
 Attribute attr = new Attribute("key", "value", attrs);
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 assertEquals(attrs, attr.parent);
 }

 @Test
 public void testSettingAttributeKey() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @Test
 public void testSettingAttributeValue() {
 Attribute attr = new Attribute("key", "value");
 String newValue = "newValue";
 assertEquals("value", attr.getValue());
 assertEquals("value", attr.toString());
 attr.setValue(newValue);
 assertEquals("newValue", attr.getValue());
 assertEquals("newValue", attr.toString());
 }

 @Test
 public void testHtmlRepresentation() throws IOException {
 Attribute attr = new Attribute("key", "value");
 StringBuilder sb = StringUtil.borrowBuilder();
 Document doc = new Document("");
 attr.html(sb, doc.outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testBooleanAttributeCollapse() throws IOException {
 Attribute attr = new Attribute("checked", null);
 StringBuilder sb = StringUtil.borrowBuilder();
 Document doc = new Document("", Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 attr.html(sb, doc.outputSettings());
 String expected = "checked";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testBooleanAttributeNoCollapse() throws IOException {
 Attribute attr = new Attribute("checked", "checked");
 StringBuilder sb = StringUtil.borrowBuilder();
 Document doc = new Document("", Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 attr.html(sb, doc.outputSettings());
 String expected = "checked=\"checked\"";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testHtmlRepresentationWithParent() throws IOException {
 Attributes attrs = new Attributes();
 Attribute attr = new Attribute("key", "value", attrs);
 StringBuilder sb = StringUtil.borrowBuilder();
 Document doc = new Document("");
 attr.html(sb, doc.outputSettings());
 String expected = "key=\"value\"";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPutAttributeAndGetAttribute() {
 Element element = new Element("div");
 PutAttribute put = new PutAttribute("key", "value", element);
 Attribute attribute = element.attribute("key");
 assertEquals(put, attribute);
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testPutAttributeWithDuplicateKey() {
 Element element = new Element("div");
 PutAttribute put1 = new PutAttribute("key", "value1", element);
 PutAttribute put2 = new PutAttribute("key", "value2", element);
 Attribute attribute = element.attribute("key");
 assertEquals(put2, attribute);
 assertEquals("value2", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAttributeWithNullKey() {
 Element element = new Element("div");
 PutAttribute put = new PutAttribute(null, "value", element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAttributeWithNullValue() {
 Element element = new Element("div");
 PutAttribute put = new PutAttribute("key", null, element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAttributeWithEmptyKey() {
 Element element = new Element("div");
 PutAttribute put = new PutAttribute("", "value", element);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAttributeWithEmptyValue() {
 Element element = new Element("div");
 PutAttribute put = new PutAttribute("key", "", element);
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attr = new Attribute("data-key", null);
 assertTrue(attr.isDataAttribute());
 Attribute attr2 = new Attribute("key", null);
 assertFalse(attr2.isDataAttribute());
 }
}