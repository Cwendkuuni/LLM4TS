package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesTest {

private Attributes attributes;

@Before
public void setUp() {
attributes = new Attributes();
}

@Test
public void testGetEmpty() {
assertEquals("", attributes.get(""));
assertEquals(0, attributes.size());
}

@Test
public void testPutGet() {
attributes.put("key", "value");
assertEquals("value", attributes.get("key"));
assertEquals(1, attributes.size());
}

@Test
public void testPutIgnoreCase() {
attributes.putIgnoreCase("KEY", "value");
assertEquals("value", attributes.get("key"));
assertEquals("value", attributes.get("KEY"));
assertEquals(1, attributes.size());
}

@Test
public void testPutBoolean() {
attributes.put("booleanKey", true);
assertNull(attributes.get("booleanKey"));
assertTrue(attributes.hasKey("booleanKey"));
}

@Test
public void testPutSameKey() {
attributes.put("key", "value1");
attributes.put("key", "value2");
assertEquals("value2", attributes.get("key"));
}

@Test
public void testSize() {
attributes.put("key1", "value1");
attributes.put("key2", "value2");
assertEquals(2, attributes.size());
}

@Test
public void testHasKey() {
attributes.put("key1", "value1");
assertTrue(attributes.hasKey("key1"));
}

@Test
public void testHasKeyIgnoreCase() {
attributes.put("KEY1", "value1");
assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
}

@Test
public void testHasKeyMissing() {
assertFalse(attributes.hasKey("missingKey"));
}

@Test
public void testRemove() {
attributes.put("key", "value");
assertEquals("value", attributes.get("key"));
attributes.remove("key");
assertEquals("", attributes.get("key"));
assertEquals(0, attributes.size());
}

@Test
public void testRemoveIgnoreCase() {
attributes.putIgnoreCase("KEY", "value");
assertEquals("value", attributes.get("KEY"));
attributes.removeIgnoreCase("KEY");
assertEquals("", attributes.get("KEY"));
assertEquals(0, attributes.size());
}

@Test
public void testPutAll() {
Attributes otherAttributes = new Attributes();
otherAttributes.put("otherKey1", "otherValue1");
otherAttributes.put("otherKey2", "otherValue2");

attributes.putAll(otherAttributes);

assertEquals("otherValue1", attributes.get("otherKey1"));
assertEquals("otherValue2", attributes.get("otherKey2"));
assertEquals(2, attributes.size());
}

@Test
public void testPutAllSameKeys() {
Attributes otherAttributes = new Attributes();
otherAttributes.put("key", "otherValue");

attributes.putAll(otherAttributes);

assertEquals("otherValue", attributes.get("key"));
assertEquals(1, attributes.size());
}

@Test
public void testPutAllEmpty() {
Attributes otherAttributes = new Attributes();

attributes.putAll(otherAttributes);

assertEquals(0, attributes.size());
}

@Test
public void testIterator() {
List<Map.Entry<String, String>> expected = new ArrayList<>();
expected.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
expected.add(new AbstractMap.SimpleEntry<>("key2", "value2"));

Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");

List<Map.Entry<String, String>> actual = new ArrayList<>(attributes.asList());

assertEquals(expected, actual);
}

@Test
public void testDataset() {
Attributes attributes = new Attributes();
attributes.put("dataKey1", "dataValue1");
attributes.put("dataKey2", "dataValue2");

Map<String, String> dataset = attributes.dataset();

assertEquals(expectedSize(attributes), dataset.size());
assertEquals(expectedDataKeyValue("dataKey1", "dataValue1"), dataset.get("dataKey1"));
assertEquals(expectedDataKeyValue("dataKey2", "dataValue2"), dataset.get("dataKey2"));
}

@Test
public void testHtml() throws IOException {
String expectedHtml = " <key1=value1> <key2=value2>";
String html = attributes.html();
assertEquals(expectedHtml, html);
}

@Test
public void testClone() {
Attributes clonedAttributes = attributes.clone();
assertEquals(attributes, clonedAttributes);
assertEquals(expectedSize(attributes), clonedAttributes.size());
assertEquals(expectedKeyValue("key", "value"), clonedAttributes.get("key"));
}

private int expectedSize(Attributes attributes) {
return attributes.size();
}

private String expectedDataKeyValue(String dataKey, String dataValue) {
return "data-" + dataKey + "=\"" + dataValue + "\"";
}

private String expectedKeyValue(String key, String value) {
return key + "=\"" + value + "\"";
}
}