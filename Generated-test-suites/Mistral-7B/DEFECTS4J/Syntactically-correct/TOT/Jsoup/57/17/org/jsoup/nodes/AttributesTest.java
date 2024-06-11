package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes.Attribute;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testGetAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("class", "test");
 String actual = attributes.get("class");
 String expected = "test";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetIgnoreCaseAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("CLASS", "test");
 String actual = attributes.getIgnoreCase("class");
 String expected = "test";
 assertEquals(expected, actual);
 }

 @Test
 public void testHasKey() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("class", "test");
 boolean actual = attributes.hasKey("class");
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("CLASS", "test");
 boolean actual = attributes.hasKeyIgnoreCase("class");
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String actual = attributes.get(null);
 String expected = "";
 assertNotSame(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetIgnoreCaseNullAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String actual = attributes.getIgnoreCase(null);
 String expected = "";
 assertNotSame(expected, actual);
 }

 @Test
 public void testPutAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "class";
 String value = "test";
 attributes.put(key, value);
 Attribute attribute = attributes.get(key);
 String actual = attribute.getValue();
 String expected = value;
 assertEquals(expected, actual);
 }

 @Test
 public void testPutBooleanAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "checked";
 boolean value = true;
 attributes.put(key, value);
 Attribute attribute = attributes.get(key);
 assertTrue(attribute.isBoolean());
 }

 @Test
 public void testPutAttributeWithExistingKey() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "class";
 String oldValue = "test1";
 attributes.put(key, oldValue);
 String newValue = "test2";
 attributes.put(key, newValue);
 Attribute attribute = attributes.get(key);
 String actual = attribute.getValue();
 String expected = newValue;
 assertEquals(expected, actual);
 }

 @Test
 public void testPutIgnoreCaseAttributeWithExistingKey() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "CLASS";
 String oldValue = "test1";
 attributes.put(key, oldValue);
 String newValue = "test2";
 attributes.putIgnoreCase(key, newValue);
 Attribute attribute = attributes.get(key);
 String actual = attribute.getValue();
 String expected = newValue;
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "class";
 attributes.put(key, "test");
 attributes.remove(key);
 Attribute attribute = attributes.get(key);
 assertNull(attribute);
 }

 @Test
 public void testRemoveIgnoreCaseAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = "CLASS";
 attributes.put(key, "test");
 attributes.removeIgnoreCase(key);
 Attribute attribute = attributes.get(key);
 assertNull(attribute);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = null;
 attributes.remove(key);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveIgnoreCaseNullAttribute() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 String key = null;
 attributes.removeIgnoreCase(key);
 }

 @Test
 public void testSize() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("class", "test1");
 attributes.put("id", "test2");
 int actual = attributes.size();
 int expected = 2;
 assertEquals(expected, actual);
 }

 @Test
 public void testAddAll() {
 Element element1 = new Element("div");
 Attributes attributes1 = element1.attributes();
 attributes1.put("class", "test1");
 attributes1.put("id", "test1");

 Element element2 = new Element("div");
 Attributes attributes2 = element2.attributes();
 attributes2.put("class", "test2");
 attributes2.put("id", "test2");

 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);

 int actual = attributes3.size();
 int expected = 4;
 assertEquals(expected, actual);
 }

 @Test
 public void testDataset() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("data-test", "test1");
 attributes.put("data-test2", "test2");
 Map<String, String> dataset = attributes.dataset();
 String actual1 = dataset.get("data-test");
 String expected1 = "test1";
 assertEquals(expected1, actual1);
 String actual2 = dataset.get("data-test2");
 String expected2 = "test2";
 assertEquals(expected2, actual2);
 }

 @Test
 public void testHtml() {
 Element element = new Element("div");
 Attributes attributes = element.attributes();
 attributes.put("class", "test");
 String html = attributes.html();
 String expected = " class=\"test\"";
 assertEquals(expected, html);
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = new Attributes();
 attributes1.put("class", "test1");
 attributes1.put("id", "test1");

 Attributes attributes2 = new Attributes();
 attributes2.put("class", "test2");
 attributes2.put("id", "test2");

 Attributes attributes3 = new Attributes();
 attributes3.putAll(attributes1);
 attributes3.putAll(attributes2);

 Attributes attributes4 = new Attributes();
 attributes4.put("class", "test2");
 attributes4.put("id", "test3");

 boolean actual1 = attributes1.equals(attributes3);
 boolean expected1 = true;
 assertEquals(expected1, actual1);

 boolean actual2 = attributes1.equals(attributes4);
 boolean expected2 = false;
 assertEquals(expected2, actual2);
 }

 @Test
 public void testClone() {
 Attributes attributes = new Attributes();
 attributes.put("class", "test1");
 attributes.put("id", "test1");
 Attributes clonedAttributes = attributes.clone();
 boolean actual1 = attributes.equals(clonedAttributes);
 boolean expected1 = true;
 assertEquals(expected1, actual1);
 }
}