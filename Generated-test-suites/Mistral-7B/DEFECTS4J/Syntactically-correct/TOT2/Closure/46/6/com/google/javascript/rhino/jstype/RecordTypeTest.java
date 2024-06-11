import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private static RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 String name = "RecordTypeTest";
 return new RecordType(registry, properties) {
 @Override
 public String getClassName() {
 return name;
 }
 };
 }

 @Test
 public void testCreateRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty(String.class, "John Doe"));
 properties.put("age", new RecordProperty(Number.class, 30));
 RecordType recordType = createRecordType(registry, properties);

 assertEquals("RecordTypeTest", recordType.getClassName());
 assertEquals(2, recordType.getPropertyCount());

 String name = recordType.getProperty("name").getName();
 assertEquals("name", name);
 assertEquals(String.class, recordType.getProperty("name").getType());
 assertEquals("John Doe", recordType.getProperty("name").getValue());

 Number age = recordType.getProperty("age").getValue();
 assertEquals(30, age.intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRecordProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", new RecordProperty(String.class, "John Doe"));
 RecordProperty nullProp = null;
 properties.put("nullProp", nullProp);

 try {
 createRecordType(registry, properties);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetLeastSupertype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType recordType1 = createRecordType(registry, Maps.newHashMap());
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 RecordType recordType3 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe"), "age", new RecordProperty(Number.class, 30)));

 JSType lessSuperType = recordType1.getLeastSupertype(recordType2);
 assertEquals(recordType2, lessSuperType);

 JSType greatestSubType = recordType3.getGreatestSubtype(recordType2);
 assertEquals(recordType3, greatestSubType);
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType recordType1 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe"), "age", new RecordProperty(Number.class, 30)));
 RecordType recordType3 = new NativeObjectType(registry, "Object");

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test(expected = IllegalStateException.class)
 public void testIsSubtypeFrozen() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType recordType = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 recordType.freeze();

 try {
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 assertTrue(recordType.isSubtype(recordType2));
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testIsEquivalentTo() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType recordType1 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 RecordType recordType2 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "John Doe")));
 RecordType recordType3 = createRecordType(registry, Maps.newHashMap("name", new RecordProperty(String.class, "Jane Doe")));

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }
}