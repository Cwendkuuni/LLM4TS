package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

 @Test
 public void testAttributeCreation() {
 Attribute attr = new Attribute("key", "value");
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testAttributeCreationWithParent() {
 Attributes parent = new Attributes(); // Assuming Attributes is a class that implements Map interface
 Attribute attr = new Attribute("key", "value", parent);
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 assertEquals(1, parent.size());
 }

 @Test
 public void testSettingKey() {
 Attribute attr = new Attribute("key", "value");
 attr.setKey("newKey");
 assertEquals("newKey", attr.getKey());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 Attribute attr = new Attribute(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 Attribute attr = new Attribute("key", null);
 }

 @Test
 public void testSettingValue() {
 Attribute attr = new Attribute("key", "value");
 attr.setValue("newValue");
 assertEquals("newValue", attr.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullNewValue() {
 Attribute attr = new Attribute("key", "value");
 attr.setValue(null);
 }

 @Test
 public void testGettingAndSettingBooleanAttributes() {
 Attribute attr = new Attribute("checked", "");
 assertTrue(attr.isBooleanAttribute());
 assertEquals("checked", attr.getKey());
 assertEquals("", attr.getValue());
 attr.setValue("true");
 assertTrue(attr.isBooleanAttribute());
 assertEquals("checked", attr.getKey());
 assertEquals("true", attr.getValue());
 }

 @Test
 public void testCollapsingBooleanAttribute() throws IOException {
 Attribute attr = new Attribute("checked", "");
 assertTrue(attr.shouldCollapseAttribute(Document.OutputSettings.htmlSynthetic()));
 assertEquals("checked", attr.getKey());
 assertEquals("", attr.getValue());
 }

 @Test
 public void testCollapsingNonBooleanAttribute() throws IOException {
 Attribute attr = new Attribute("key", "value");
 assertFalse(attr.shouldCollapseAttribute(Document.OutputSettings.htmlSynthetic()));
 assertEquals("key", attr.getKey());
 assertEquals("value", attr.getValue());
 }

 @Test
 public void testHtmlEncodedValue() throws IOException {
 Attribute attr = new Attribute("key", "<script>alert('XSS')</script>");
 assertEquals("key", attr.getKey());
 assertEquals("&lt;script&gt;alert('XSS')&lt;/script&gt;", attr.html());
 }

 @Test
 public void testCreateFromEncoded() {
 Attribute attr = Attribute.createFromEncoded("key", "&lt;script&gt;alert('XSS')&lt;/script&gt;");
 assertEquals("key", attr.getKey());
 assertEquals("&lt;script&gt;alert('XSS')&lt;/script&gt;", attr.getValue());
 }

 @Test
 public void testIsDataAttribute() {
 Attribute attr = new Attribute("data-key", null);
 assertTrue(attr.isDataAttribute());
 }

 @Test
 public void testIsDataAttributeFalse() {
 Attribute attr = new Attribute("key", null);
 assertFalse(attr.isDataAttribute());
 }
}