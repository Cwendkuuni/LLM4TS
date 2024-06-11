package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test; import org.junit.runner.RunWith; import org.junit.runners.JUnit4;
import org.jsoup.nodes.Attributes;

@RunWith(JUnit4.class)
public class AttributesTest {

 private Attributes createAttributes() {
 return new Attributes();
 }

 @Test
 public void testGetEmpty() {
 Attributes attributes = createAttributes();
 String value = attributes.get("testKey");
 Assert.assertEquals("", value);
 }

 @Test
 public void testGet() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 String value = attributes.get("testKey");
 Assert.assertEquals("testValue", value);
 }

 @Test
 public void testGetIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 String value = attributes.getIgnoreCase("testkey");
 Assert.assertEquals("testValue", value);
 }

 @Test
 public void testPut() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 String value = attributes.get("testKey");
 Assert.assertEquals("testValue", value);
 }

 @Test
 public void testPutBoolean() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", true);
 Assert.assertTrue(attributes.hasKey("testKey"));
 Assert.assertFalse(attributes.hasKey("testKey2"));
 }

 @Test
 public void testPutSameKeyTwice() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 attributes.put("testKey", "newTestValue");
 String value = attributes.get("testKey");
 Assert.assertEquals("newTestValue", value);
 }

 @Test
 public void testRemove() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 attributes.remove("testKey");
 Assert.assertFalse(attributes.hasKey("testKey"));
 }

 @Test
 public void testRemoveIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 attributes.removeIgnoreCase("testkey");
 Assert.assertFalse(attributes.hasKey("testKey"));
 }

 @Test
 public void testHasKey() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 Assert.assertTrue(attributes.hasKey("testKey"));
 Assert.assertFalse(attributes.hasKey("testKey2"));
 }

 @Test
 public void testHasKeyIgnoreCase() {
 Attributes attributes = createAttributes();
 attributes.put("testKey", "testValue");
 Assert.assertTrue(attributes.hasKeyIgnoreCase("testKey"));
 Assert.assertFalse(attributes.hasKeyIgnoreCase("testKey2"));
 }

 @Test
 public void testSize() {
 Attributes attributes = createAttributes();
 Assert.assertEquals(0, attributes.size());
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 Assert.assertEquals(2, attributes.size());
 }

 @Test
 public void testAddAll() {
 Attributes attributes1 = createAttributes();
 attributes1.put("testKey1", "testValue1");
 Attributes attributes2 = createAttributes();
 attributes2.put("testKey2", "testValue2");
 attributes1.addAll(attributes2);
 Assert.assertEquals(2, attributes1.size());
 Assert.assertTrue(attributes1.hasKey("testKey1"));
 Assert.assertTrue(attributes1.hasKey("testKey2"));
 }

 @Test
 public void testRemoveAll() {
 Attributes attributes = createAttributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 Attributes attributesToRemove = new Attributes();
 attributesToRemove.put("testKey1");
 attributes.removeAll(attributesToRemove);
 Assert.assertEquals(1, attributes.size());
 Assert.assertFalse(attributes.hasKey("testKey1"));
 Assert.assertTrue(attributes.hasKey("testKey2"));
 }

 @Test
 public void testAsList() {
 Attributes attributes = createAttributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 List<Attribute> list = attributes.asList();
 Attribute attribute1 = list.get(0);
 Attribute attribute2 = list.get(1);
 Assert.assertEquals("testKey1", attribute1.getKey());
 Assert.assertEquals("testValue1", attribute1.getValue());
 Assert.assertEquals("testKey2", attribute2.getKey());
 Assert.assertEquals("testValue2", attribute2.getValue());
 }

 @Test
 public void testDataset() {
 Attributes attributes = createAttributes();
 attributes.put("data-testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 Map<String, String> dataset = attributes.dataset();
 Assert.assertEquals(1, dataset.size());
 Assert.assertEquals("testValue1", dataset.get("data-testKey1"));
 }

 @Test
 public void testHtml() throws IOException {
 Attributes attributes = createAttributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 StringBuilder expected = new StringBuilder();
 expected.append(" testKey1=\"testValue1\" testKey2=\"testValue2\"");
 StringBuilder actual = new StringBuilder();
 attributes.html(actual, null);
 Assert.assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testToString() throws IOException {
 Attributes attributes = createAttributes();
 attributes.put("testKey1", "testValue1");
 attributes.put("testKey2", "testValue2");
 StringBuilder expected = new StringBuilder();
 expected.append(" testKey1=\"testValue1\" testKey2=\"testValue2\"");
 String actual = attributes.toString();
 Assert.assertEquals(expected.toString(), actual);
 }

 @Test
 public void testEquals() {
 Attributes attributes1 = createAttributes();
 attributes1.put("testKey1", "testValue1");
 Attributes attributes2 = createAttributes();
 attributes2.put("testKey1", "testValue1");
 Attributes attributes3 = createAttributes();
 attributes3.put("testKey2", "testValue2");
 Assert.assertEquals(attributes1, attributes1);
 Assert.assertEquals(attributes1, attributes2);
 Assert.assertNotEquals(attributes1, attributes3);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Attributes attributes = createAttributes();
 attributes.put("testKey1", "testValue1");
 Attributes cloned = attributes.clone();
 Assert.assertEquals(attributes, cloned);
 }

}