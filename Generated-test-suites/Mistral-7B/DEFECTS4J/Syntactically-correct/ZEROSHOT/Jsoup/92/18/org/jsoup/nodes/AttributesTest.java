import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AttributesTest {

 private Attributes attributes;

 @Before
 public void setUp() {
 attributes = new Attributes();
 }

 @After
 public void tearDown() {
 attributes = null;
 }

 @Test
 public void testEmptyAttributes() {
 assertEquals("{}", attributes.toString());
 assertFalse(attributes.hasKey("test"));
 }

 @Test
 public void testAddAttribute() {
 attributes.put("test", "value");
 assertEquals("{\"test\":\"value\"}", attributes.toString());
 assertTrue(attributes.hasKey("test"));
 assertEquals("value", attributes.get("test"));
 }

 @Test
 public void testIgnoreCaseAddAttribute() {
 attributes.putIgnoreCase("Test", "Value");
 assertEquals("{\"test\":\"value\"}", attributes.toString());
 assertTrue(attributes.hasKey("test"));
 assertEquals("value", attributes.get("test"));
 }

 @Test
 public void testAddMultipleAttributes() {
 attributes.put("test1", "value1");
 attributes.put("test2", "value2");
 assertEquals("{\"test1\":\"value1\",\"test2\":\"value2\"}", attributes.toString());
 assertTrue(attributes.hasKey("test1"));
 assertEquals("value1", attributes.get("test1"));
 assertTrue(attributes.hasKey("test2"));
 assertEquals("value2", attributes.get("test2"));
 }

 @Test
 public void testAddMultipleAttributesIgnoreCase() {
 attributes.putIgnoreCase("Test1", "Value1");
 attributes.putIgnoreCase("Test2", "Value2");
 assertEquals("{\"test1\":\"value1\",\"test2\":\"value2\"}", attributes.toString());
 assertTrue(attributes.hasKey("test1"));
 assertEquals("value1", attributes.get("test1"));
 assertTrue(attributes.hasKey("test2"));
 assertEquals("value2", attributes.get("test2"));
 }

 @Test
 public void testPutNullValue() {
 attributes.put("test", null);
 assertEquals("{\"test\":null}", attributes.toString());
 assertTrue(attributes.hasKey("test"));
 assertNull(attributes.get("test"));
 }

 @Test
 public void testPutNullKey() {
 attributes.put(null, "value");
 assertFalse(attributes.hasKey(null));
 }

 @Test
 public void testPutNullKeyIgnoreCase() {
 attributes.putIgnoreCase(null, "value");
 assertFalse(attributes.hasKey(null));
 }

 @Test
 public void testReplaceAttribute() {
 attributes.put("test", "oldValue");
 attributes.put("test", "newValue");
 assertEquals("{\"test\":\"newValue\"}", attributes.toString());
 assertTrue(attributes.hasKey("test"));
 assertEquals("newValue", attributes.get("test"));
 }

 @Test
 public void testReplaceAttributeIgnoreCase() {
 attributes.putIgnoreCase("Test", "oldValue");
 attributes.putIgnoreCase("Test", "newValue");
 assertEquals("{\"test\":\"newValue\"}", attributes.toString());
 assertTrue(attributes.hasKey("test"));
 assertEquals("newValue", attributes.get("test"));
 }

 @Test
 public void testRemoveAttribute() {
 attributes.put("test", "value");
 attributes.remove("test");
 assertEquals("{}", attributes.toString());
 assertFalse(attributes.hasKey("test"));
 }

 @Test
 public void testRemoveAttributeIgnoreCase() {
 attributes.putIgnoreCase("Test", "value");
 attributes.removeIgnoreCase("Test");
 assertEquals("{}", attributes.toString());
 assertFalse(attributes.hasKey("test"));
 }

 @Test
 public void testCloneAttributes() {
 Attributes clonedAttributes = attributes.clone();
 assertEquals(attributes.toString(), clonedAttributes.toString());
 assertSame(attributes.getClass(), clonedAttributes.getClass());
 }

 @Test
 public void testGetBooleanAttribute() {
 attributes.put("test", "true");
 assertTrue(attributes.get("test") == null);
 attributes.put("test", "false");
 assertFalse(attributes.hasKey("test"));
 attributes.put("test", "");
 assertFalse(attributes.hasKey("test"));
 attributes.put("test", "false");
 assertFalse(attributes.getBoolean("test"));
 attributes.put("test", "true");
 assertTrue(attributes.getBoolean("test"));
 }

 @Test
 public void testPutBooleanAttribute() {
 attributes.put("test", "false");
 attributes.put("test", true);
 assertTrue(attributes.getBoolean("test"));
 attributes.put("test", false);
 assertFalse(attributes.getBoolean("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutBooleanAttributeInvalidArgument() {
 attributes.put("test", 42);
 }

 @Test
 public void testPutIgnoreCaseBooleanAttribute() {
 attributes.putIgnoreCase("test", "true");
 assertTrue(attributes.getBoolean("test"));
 attributes.putIgnoreCase("test", "false");
 assertFalse(attributes.getBoolean("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutIgnoreCaseBooleanAttributeInvalidArgument() {
 attributes.putIgnoreCase("test", 42);
 }

 @Test
 public void testRemoveBooleanAttribute() {
 attributes.put("test", "true");
 attributes.remove("test");
 assertFalse(attributes.hasKey("test"));
 }
}