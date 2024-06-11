package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

 private Attributes createAttributes() {
 return new Attributes();
 }

 @Test
 public void testGet() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertEquals("testValue", attributes.get("test"));
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertEquals("testValue", attributes.getIgnoreCase("Test"));
 }

 @Test
 public void testGetWhenAttributesIsNull() {
 Attributes attributes = createAttributes();
 assertEquals("", attributes.get("test"));
 }

 @Test
 public void testPut() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertEquals("testValue", attributes.get("test"));
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = createAttributes();
 attributes.put("test", true);
 assertTrue(attributes.hasKey("test"));
 assertFalse(attributes.get("test").getValue().equals("false"));
 }

 @Test
 public void testPutAttribute() {
 Attributes attributes = createAttributes();
 Attribute attribute = new Attribute("test", "testValue");
 attributes.put(attribute);
 assertEquals("testValue", attributes.get("test").getValue());
 }

 @Test
 public void testRemove() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 attributes.remove("test");
 assertFalse(attributes.hasKey("test"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 attributes.removeIgnoreCase("test");
 assertFalse(attributes.hasKeyIgnoreCase("test"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertTrue(attributes.hasKey("test"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertTrue(attributes.hasKeyIgnoreCase("test"));
 }

 @Test
 public void testSize() {
 Attributes attributes = createAttributes();
 attributes.put("test1", "testValue1");
 attributes.put("test2", "testValue2");
 assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = createAttributes();
 Attributes attributes2 = createAttributes();
 attributes1.put("test1", "testValue1");
 attributes2.put("test2", "testValue2");
 attributes1.addAll(attributes2);
 assertEquals(2, attributes1.size());
 assertEquals("testValue1", attributes1.get("test1"));
 assertEquals("testValue2", attributes1.get("test2"));
 }

 @Test
 public void testAsList() {
 Attributes attributes = createAttributes();
 attributes.put("test1", "testValue1");
 attributes.put("test2", "testValue2");
 List<Attribute> list = attributes.asList();
 assertEquals(2, list.size());
 assertEquals("testValue1", list.get(0).getValue());
 assertEquals("testValue2", list.get(1).getValue());
 }

 @Test
 public void testDataset() {
 Attributes attributes = createAttributes();
 attributes.put("data-test1", "testValue1");
 attributes.put("data-test2", "testValue2");
 Map<String, String> dataset = attributes.dataset();
 assertEquals(2, dataset.size());
 assertEquals("testValue1", dataset.get("data-test1"));
 assertEquals("testValue2", dataset.get("data-test2"));
 }

 @Test
 public void testHtml() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertEquals("<attr name=\"test\" value=\"testValue\"/>", attributes.html());
 }

 @Test
 public void testToString() {
 Attributes attributes = createAttributes();
 attributes.put("test", "testValue");
 assertEquals("<attr name=\"test\" value=\"testValue\"/>", attributes.toString());
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = createAttributes();attributes1.put("test1", "testValue1");
 Attributes attributes2 = createAttributes();attributes2.put("test1", "testValue1");
 assertTrue(attributes1.equals(attributes2));
 }

 @Test
 public void testHashCode() {
 Attributes attributes1 = createAttributes();attributes1.put("test1", "testValue1");
 Attributes attributes2 = createAttributes();attributes2.put("test1", "testValue1");
 assertEquals(attributes1.hashCode(), attributes2.hashCode());
 }

 @Test
 public void testClone() {
 Attributes attributes = createAttributes();attributes.put("test", "testValue");
 Attributes clonedAttributes = attributes.clone();
 assertEquals("testValue", clonedAttributes.get("test").getValue());
 }
}