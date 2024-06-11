import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties, true);
 assertTrue(recordType instanceof RecordType);
 assertEquals(recordType.getClass(), RecordType.class);
 assertEquals(recordType.getPrettyPrint(), true);
 assertEquals(recordType.isDeclared(), true);
 assertFalse(recordType.isFrozen());

 Set<String> keys = recordType.getOwnPropertyNames();
 assertEquals(keys.size(), 2);
 assertTrue(keys.contains("name"));
 assertTrue(keys.contains("age"));
 }

 @Test
 public void testRecordTypeFrozen() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));

 RecordType recordType = new RecordType(registry, properties, true);
 recordType.freeze();
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRecordTypeNullProperties() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = null;
 RecordType recordType = new RecordType(registry, properties, true);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeNullPropertyType() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", null));
 RecordType recordType = new RecordType(registry, properties, true);
 }

 @Test
 public void testRecordTypeEquivalence() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testRecordTypeNonEquivalence() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Double.class));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testRecordTypeGetters() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties, true);

 assertEquals(recordType.isSynthetic(), false);
 assertEquals(recordType.isFrozen(), false);

 Set<String> keys = recordType.getOwnPropertyNames();
 assertEquals(keys.size(), 2);
 assertEquals(recordType.getPropertyType("name"), String.class);
 assertEquals(recordType.getPropertyType("age"), Integer.class);
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Long.class));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 RecordType recordType3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType4 = new RecordType(registry, null, true);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 }

 @Test
 public void testRecordTypeGreatestSubtype() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Long.class));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), UnionType.class);
 assertTrue(greatestSubtype.getTypes().size() > 1);
 }

 @Test
 public void testRecordTypeGreatestSubtypeEmpty() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 RecordType recordType = new RecordType(registry, properties, true);

 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), RecordType.class);
 assertEquals(greatestSubtype, recordType);
 }

 @Test
 public void testRecordTypeGreatestSubtypeIdentity() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 RecordType recordType = new RecordType(registry, properties, true);

 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), RecordType.class);
 assertEquals(greatestSubtype, recordType);
 }
}