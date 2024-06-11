package org.jsoup.nodes;

import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertNotNull;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertTrue;import static org.junit.Assert.fail;

import java.util.ArrayList;import java.util.Arrays;import java.util.Collections;import java.util.List;import java.util.Map;

public class AttributesTest {

 @Test
 public void testAttributesCreation() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 Assert.assertNull(attributes.get("test"));
 }

 @Test
 public void testAttributesAddGet() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("value", attributes.get("test"));
 }

 @Test
 public void testAttributesAddGetIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("value", attributes.getIgnoreCase("test"));
 Assert.assertEquals("value", attributes.getIgnoreCase("TeSt"));
 }

 @Test
 public void testAttributesAddGetMultipleValues() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value1");
 attributes.put("test", "value2");
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("value2", attributes.get("test"));
 Assert.assertEquals("value2", attributes.getIgnoreCase("test"));
 }

 @Test
 public void testAttributesGetEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 Assert.assertEquals(Attributes.EmptyString, attributes.get("test"));
 Assert.assertEquals(Attributes.EmptyString, attributes.getIgnoreCase("test"));
 }

 @Test
 public void testAttributesPutNullKey() {
 Attributes attributes = new Attributes();
 try {
 attributes.put(null, "value");
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testAttributesPutNullValue() {
 Attributes attributes = new Attributes();
 attributes.put("test", null);
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals(Attributes.EmptyString, attributes.get("test"));
 }

 @Test
 public void testAttributesRemove() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 attributes.remove("test");
 Assert.assertEquals(0, attributes.size());
 Assert.assertNull(attributes.get("test"));
 }

 @Test
 public void testAttributesRemoveIgnoreCase() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 attributes.removeIgnoreCase("test");
 Assert.assertEquals(0, attributes.size());
 Assert.assertNull(attributes.get("test"));
 }

 @Test
 public void testAttributesRemoveIgnoreCaseDifferentCase() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 attributes.removeIgnoreCase("TeSt");
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("value", attributes.get("test"));
 }

 @Test
 public void testAttributesHasKey() {
 Attributes attributes = new Attributes();
 Assert.assertFalse(attributes.hasKey("test"));
 attributes.put("test", "value");
 Assert.assertTrue(attributes.hasKey("test"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
 }

 @Test
 public void testAttributesHasKeyIgnoreCaseDifferentCase() {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("test"));
 Assert.assertTrue(attributes.hasKeyIgnoreCase("TeSt"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("teSt"));
 }

 @Test
 public void testAttributesHasKeyEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertFalse(attributes.hasKey("test"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("test"));
 }

 @Test
 public void testAttributesSize() {
 Attributes attributes = new Attributes();
 attributes.put("test1", "value1");
 attributes.put("test2", "value2");
 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testAttributesSizeEmpty() {
 Attributes attributes = new Attributes();
 Assert.assertEquals(0, attributes.size());
 }

 @Testpublic void testAttributesAddAll() {
 Attributes attributes1 = new Attributes();
 attributes1.put("test1", "value1");
 Attributes attributes2 = new Attributes();
 attributes2.put("test2", "value2");
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 attributes3.addAll(attributes2);
 List<Map.Entry<String, String>> expected = new ArrayList<>(Arrays.asList(
 new AbstractMap.SimpleEntry<>("test1", "value1"),
 new AbstractMap.SimpleEntry<>("test2", "value2")
 ));
 Assert.assertEquals(expected, attributes3.asList());
 }

 @Test
 public void testAttributesAddAllEmpty() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes3.addAll(attributes1);
 Assert.assertEquals(0, attributes3.size());
 }

 @Test
 public void testAttributesClone() {
 Attributes attributes1 = new Attributes();
 attributes1.put("test1", "value1");
 Attributes attributes2 = attributes1.clone();
 List<Map.Entry<String, String>> expected = new ArrayList<>(Arrays.asList(
 new AbstractMap.SimpleEntry<>("test1", "value1")
 ));
 Assert.assertEquals(expected, attributes2.asList());
 }

 @Test
 public void testAttributesEquals() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("test1", "value1");
 attributes2.put("test1", "value1");
 attributes3.put("test2", "value2");
 assertTrue(attributes1.equals(attributes1));
 assertTrue(attributes1.equals(attributes2));
 assertFalse(attributes1.equals(attributes3));
 assertFalse(attributes1.equals(null));
 }

 @Test
 public void testAttributesHashCode() {
 Attributes attributes1 = new Attributes();
 Attributes attributes2 = new Attributes();
 Attributes attributes3 = new Attributes();
 attributes1.put("test1", "value1");
 attributes2.put("test1", "value1");
 attributes3.put("test2", "value2");
 int hashcode1 = attributes1.hashCode();
 int hashcode2 = attributes1.hashCode();
 int hashcode3 = attributes3.hashCode();
 assertEquals(hashcode1, hashcode2);
 assertNotEquals(hashcode1, hashcode3);
 }

 @Test
 public void testAttributesNormalize() {
 Attributes attributes = new Attributes();
 attributes.put("Test", "value");
 attributes.normalize();
 assertEquals("test", attributes.get("Test"));
 }

 @Test
 public void testAttributesDataset() {
 Attributes attributes = new Attributes();
 attributes.put("data-test1", "value1");
 attributes.put("data-test2", "value2");
 Map<String, String> dataset = attributes.dataset();
 List<Map.Entry<String, String>> expected = new ArrayList<>(Arrays.asList(
 new AbstractMap.SimpleEntry<>("data-test1", "value1"),
 new AbstractMap.SimpleEntry<>("data-test2", "value2")
 ));
 Assert.assertEquals(expected, dataset.entrySet());
 }

 @Test
 public void testAttributesDatasetEmpty() {
 Attributes attributes = new Attributes();
 Map<String, String> dataset = attributes.dataset();
 assertTrue(dataset.isEmpty());
 }

 @Test
 public void testAttributesHtml() throws IOException {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 StringBuilder expected = new StringBuilder();
 expected.append(" test=\"value\"");
 Assert.assertEquals(expected.toString(), attributes.html());
 }

 @Test
 public void testAttributesToString() throws IOException {
 Attributes attributes = new Attributes();
 attributes.put("test", "value");
 Assert.assertEquals(attributes.html(), attributes.toString());
 }
}