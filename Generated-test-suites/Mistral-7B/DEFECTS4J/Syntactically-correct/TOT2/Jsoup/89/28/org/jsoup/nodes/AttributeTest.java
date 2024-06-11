package org.junit.tests.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class AttributeTest {
// Test cases for constructors
@Test
public void testAttributeCreation() {
Attribute attribute = new Attribute("key", "value");
Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

// Check key and value
assertEquals("key", attribute.getKey());
assertEquals("value", attribute.getValue());
assertEquals("key", attributeWithParent.getKey());
assertEquals("value", attributeWithParent.getValue());
}

// Test cases for getters
@Test
public void testGetKey() {
Attribute attribute = new Attribute("key", "value");
assertEquals("key", attribute.getKey());
}

@Test
public void testSetValue() {
Attribute attribute = new Attribute("key", "value");
String newValue = "newValue";
attribute.setValue(newValue);
assertEquals("newValue", attribute.getValue());
}

// Test cases for html() method
@Test
public void testHtml() throws IOException, SerializationException {
Attribute attribute = new Attribute("key", "value");
StringBuilder expectedHtml = new StringBuilder("key=\"value\"");
StringBuilder actualHtml = new StringBuilder();
attribute.html(actualHtml, new Document("").outputSettings());
assertEquals(expectedHtml.toString(), actualHtml.toString());
}

// Test cases for edge cases and error scenarios
@Test
public void testEmptyKeyOrValue() {
Attribute attribute1 = new Attribute("", "");
Attribute attribute2 = new Attribute("", "");

assertEquals(Attributes.NotFound, attribute1.parent.indexOfKey(attribute1.key));
assertEquals(Attributes.NotFound, attribute2.parent.indexOfKey(attribute2.key));
}

@Test(expected = IllegalArgumentException.class)
public void testNullKey() {
new Attribute(null, "value");
}

@Test(expected = IllegalArgumentException.class)
public void testNullValue() {
new Attribute("key", null);
}

@Test
public void testInvalidKey() {
Attribute attribute = new Attribute("key#", "value");
assertEquals(Attributes.NotFound, attribute.parent.indexOfKey(attribute.key));
}

@Test
public void testParentAttributeExistence() {
Attributes parentAttributes = new Attributes();
Attribute attribute1 = new Attribute("key1", "value1", parentAttributes);
Attribute attribute2 = new Attribute("key2", "value2", parentAttributes);

assertEquals(0, parentAttributes.size());
assertEquals("key1", parentAttributes.keySet().iterator().next());
assertEquals("value1", parentAttributes.get("key1"));
}

// Test cases for other methods (e.g., toString(), createFromEncoded(String unencodedKey, String encodedValue), etc.)
// ...
}