import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.InitialCapacity; // for testing purposes, to set expected capacity

public class AttributesTest {

 @Test
 public void testInitialCapacity() {
 Attributes attrs = new Attributes();
 Assert.assertEquals(Attributes.InitialCapacity, attrs.keys.length);
 }

 @Test
 public void testGetMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "value");
 String actualValue = attrs.get("key");
 String expectedValue = "value";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetIgnoreCaseMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "value");
 String actualValue = attrs.getIgnoreCase("key");
 String expectedValue = "value";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetEmptyMethod() {
 Attributes attrs = new Attributes();
 String actualValue = attrs.get("nonexistentKey");
 String expectedValue = Attributes.EmptyString;
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testPutMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "existingValue");
 attrs.put("key", "newValue");
 String actualValue = attrs.get("key");
 String expectedValue = "newValue";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKeyMethod() {
 Attributes attrs = new Attributes();
 attrs.put(null, "value");
 }

 @Test
 public void testPutNullValueMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", null);
 String actualValue = attrs.get("key");
 String expectedValue = Attributes.EmptyString;
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testPutMethodWithSameKeyButDifferentValue() {
 Attributes attrs = new Attributes();
 attrs.put("key", "existingValue");
 attrs.put("key", "newValue");
 String actualValue = attrs.get("key");
 String expectedValue = "newValue";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testPutIgnoreCaseMethod() {
 Attributes attrs = new Attributes();
 attrs.put("Key", "existingValue");
 attrs.putIgnoreCase("key", "newValue");
 String actualValue = attrs.get("key");
 String expectedValue = "newValue";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testPutMethodWithSameCaseInsensitiveKeyButDifferentValue() {
 Attributes attrs = new Attributes();
 attrs.put("Key", "existingValue");
 attrs.putIgnoreCase("key", "newValue");
 String actualValue = attrs.get("key");
 String expectedValue = "newValue";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testRemoveMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "value");
 attrs.remove("key");
 String actualValue = attrs.get("key");
 String expectedValue = Attributes.EmptyString;
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullKeyMethod() {
 Attributes attrs = new Attributes();
 attrs.remove(null);
 }

 @Test
 public void testRemoveMethodWithNonExistentKey() {
 Attributes attrs = new Attributes();
 attrs.remove("nonexistentKey");
 String actualValue = attrs.get("nonexistentKey");
 String expectedValue = Attributes.EmptyString;
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testHasKeyMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "value");
 boolean actualResult = attrs.hasKey("key");
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasKeyIgnoreCaseMethod() {
 Attributes attrs = new Attributes();
 attrs.put("Key", "value");
 boolean actualResult = attrs.hasKeyIgnoreCase("key");
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasKeyMethodWithNonExistentKey() {
 Attributes attrs = new Attributes();
 boolean actualResult = attrs.hasKey("nonexistentKey");
 boolean expectedResult = false;
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithEmptyKeyMethod() {
 Attributes attrs = new Attributes();
 attrs.put("", "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithEmptyValueMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key", "");
 }

 @Test
 public void testAddAllMethod() {
 Attributes attrs1 = new Attributes();
 attrs1.put("key1", "value1");
 Attributes attrs2 = new Attributes();
 attrs2.put("key2", "value2");
 Attributes expectedResult = new Attributes();
 expectedResult.put("key1", "value1");
 expectedResult.put("key2", "value2");
 Attributes actualResult = new Attributes();
 actualResult.addAll(attrs1);
 actualResult.addAll(attrs2);
 for (Map.Entry<String, String> entry : expectedResult.entrySet()) {
 String actualKey = actualResult.get(entry.getKey());
 String expectedKey = entry.getKey();
 String actualValue = actualResult.get(actualKey);
 String expectedValue = entry.getValue();
 assertEquals(expectedValue, actualValue);
 assertEquals(expectedKey, actualKey);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testAddAllMethodWithNullAttributes() {
 Attributes attrs = new Attributes();
 attrs.addAll(null);
 }

 @Test
 public void testSizeMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key1", "value1");
 attrs.put("key2", "value2");
 int actualSize = attrs.size();
 int expectedSize = 2;
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testCloneMethod() {
 Attributes original = new Attributes();
 original.put("key1", "value1");
 original.put("key2", "value2");
 Attributes copy = original.clone();
 for (Map.Entry<String, String> entry : original.entrySet()) {
 String clonedKey = copy.get(entry.getKey());
 String originalKey = entry.getKey();
 String clonedValue = copy.get(clonedKey);
 String originalValue = entry.getValue();
 assertEquals(originalValue, clonedValue);
 assertEquals(originalKey, clonedKey);
 }
 }

 @Test
 public void testNormalizeMethod() {
 Attributes attrs = new Attributes();
 attrs.put("key1", "value1");
 attrs.put("KEY2", "value2");
 attrs.normalize();
 String actualKey1 = attrs.get("key1");
 String expectedKey1 = "key1";
 Assert.assertEquals(expectedKey1, actualKey1);
 String actualKey2 = attrs.get("key2");
 String expectedKey2 = "key2";
 Assert.assertEquals(expectedKey2, actualKey2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullValueAndNullKey() {
 Attributes attrs = new Attributes();
 attrs.put(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullValueButNonEmptyKey() {
 Attributes attrs = new Attributes();
 attrs.put("key", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNonEmptyValueAndNullKey() {
 Attributes attrs = new Attributes();
 attrs.put(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNonEmptyKeyAndEmptyValue() {
 Attributes attrs = new Attributes();
 attrs.put("key", "");
 }

 @Test
 public void testPutMethodWithSameKeyButDifferentValuesAndCaseInsensitive() {
 Attributes attrs = new Attributes();
 attrs.put("key1", "value1");
 attrs.putIgnoreCase("key1", "value2");
 String actualValue = attrs.get("key1");
 String expectedValue = "value2";
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNegativeCapacity() {
 Attributes attrs = new Attributes();
 attrs.checkCapacity(-1);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveMethodWithNegativeIndex() {
 Attributes attrs = new Attributes();
 attrs.remove(-1);
 }
}