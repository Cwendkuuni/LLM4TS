import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {

@Test
public void testAddGetRemove() {
Attributes attributes = new Attributes();

// Test adding an attribute
attributes.put("key1", "value1");
Assert.assertEquals("value1", attributes.get("key1"));

// Test getting an non-existent attribute
Assert.assertEquals("", attributes.get("key2"));

// Test removing an attribute
attributes.put("key1", null);
Assert.assertEquals("", attributes.get("key1"));

// Test adding and removing multiple attributes
attributes.put("key2", "value2");
attributes.put("key3", "value3");
Assert.assertEquals("value2", attributes.get("key2"));
Assert.assertEquals("value3", attributes.get("key3"));
attributes.remove("key2");
Assert.assertEquals("", attributes.get("key2"));
}

@Test
public void testAddAll() {
Attributes attributes1 = new Attributes();
Attributes attributes2 = new Attributes();
attributes2.put("key1", "value1");
attributes2.put("key2", "value2");

attributes1.addAll(attributes2);
Assert.assertEquals(2, attributes1.size());
Assert.assertEquals("value1", attributes1.get("key1"));
Assert.assertEquals("value2", attributes1.get("key2"));
}

@Test
public void testHasKey() {
Attributes attributes = new Attributes();

// Test hasKey with non-existent key
Assert.assertFalse(attributes.hasKey("key1"));

// Test hasKey with existing key
attributes.put("key1", "value1");
Assert.assertTrue(attributes.hasKey("key1"));
}

@Test
public void testHasKeyIgnoreCase() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");

// Test hasKeyIgnoreCase with non-existent key
Assert.assertFalse(attributes.hasKeyIgnoreCase("KEY1"));

// Test hasKeyIgnoreCase with existing key
Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
}

@Test
public void testPutIgnoreCase() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.putIgnoreCase("Key1", "value2");
Assert.assertEquals("value2", attributes.get("key1"));
}

@Test
public void testPutBooleanAttribute() {
Attributes attributes = new Attributes();
attributes.put("key1", true);
Assert.assertFalse(attributes.get("key1").isEmpty());
attributes.remove("key1");
}

@Test
public void testIterator() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");

Iterator<Attribute> iterator = attributes.iterator();
List<Attribute> attributesList = new ArrayList<>();

while (iterator.hasNext()) {
attributesList.add(iterator.next());
}

Assert.assertEquals(2, attributesList.size());
Assert.assertEquals("key1", attributesList.get(0).getKey());
Assert.assertEquals("value1", attributesList.get(0).getValue());
Assert.assertEquals("key2", attributesList.get(1).getKey());
Assert.assertEquals("value2", attributesList.get(1).getValue());
}

@Test
public void testAsList() {
Attributes attributes = new Attributes();
attributes.put("key1", "value1");
attributes.put("key2", "value2");

List<Attribute> attributesList = attributes.asList();

Assert.assertEquals(2, attributesList.size());
Assert.assertEquals("key1", attributesList.get(0).getKey());
Assert.assertEquals("value1", attributesList.get(0).getValue());
Assert.assertEquals("key2", attributesList.get(1).getKey());
Assert.assertEquals("value2", attributesList.get(1).getValue());
}

@Test
public void testDataset() {
Attributes attributes = new Attributes();
attributes.put("data-key1", "value1");
attributes.put("data-key2", "value2");
Map<String, String> dataset = attributes.dataset();

Assert.assertEquals(2, dataset.size());
Assert.assertEquals("key1", dataset.keySet().iterator().next());
Assert.assertEquals("value1", dataset.get("key1"));
Assert.assertEquals("key2", dataset.keySet().iterator().next());
Assert.assertEquals("value2", dataset.get("key2"));
}

@Test
public void testClone() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
attributes1.put("key2", "value2");
Attributes attributes2 = attributes1.clone();

Assert.assertEquals(2, attributes2.size());
Assert.assertEquals("value1", attributes2.get("key1"));
Assert.assertEquals("value2", attributes2.get("key2"));
Assert.assertNotSame(attributes1, attributes2);
}

@Test
public void testHashCode() {
Attributes attributes1 = new Attributes();
attributes1.put("key1", "value1");
attributes1.put("key2", "value2");
Attributes attributes2 = new Attributes();
attributes2.put("key1", "value1");
attributes2.put("key3", "value3");

Assert.assertEquals(attributes1.hashCode(), attributes1.hashCode());
Assert.assertEquals(attributes1.hashCode(), attributes1.clone().hashCode());
Assert.assertEquals(attributes1.hashCode(), attributes1.clone().clone().hashCode());
Assert.assertNotEquals(attributes1.hashCode(), attributes2.hashCode());
Assert.assertNotEquals(attributes1.hashCode(), attributes2.clone().hashCode());
Assert.assertNotEquals(attributes1.hashCode(), attributes2.clone().clone().hashCode());
}
}