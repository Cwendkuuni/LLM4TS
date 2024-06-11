import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));

 RecordType recordType = new RecordType(registry, properties, true);
 RecordType recordTypeSynthetic = new RecordType(registry, properties, false);

 assertEquals("RecordType", recordType.getClassName());
 assertEquals("RecordType", recordTypeSynthetic.getClassName());
 assertEquals(true, recordType.isDeclared());
 assertEquals(false, recordTypeSynthetic.isDeclared());
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));

 RecordType recordType = new RecordType(registry, properties, true);
 recordType.defineProperty("newProperty", registry.getStringType(), false, null);

 assertEquals(3, recordType.getOwnPropertyNames().size());
 assertTrue(recordType.hasProperty("name"));
 assertTrue(recordType.hasProperty("age"));
 assertTrue(recordType.hasProperty("newProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put(null, null);

 try {
 new RecordType(registry, properties, true);
 } catch (IllegalStateException e) {
 assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
 }
 }

 @Test
 public void testGetOwnPropertyNames() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));

 RecordType recordType = new RecordType(registry, properties, true);
 Set<String> getOwnPropertyNames = recordType.getOwnPropertyNames();

 assertEquals(2, getOwnPropertyNames.size());
 assertTrue(getOwnPropertyNames.contains("name"));
 assertTrue(getOwnPropertyNames.contains("age"));
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType1 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new java.util.LinkedHashMap<>(), false);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType1));
 assertTrue(recordType.isSubtype(objectType));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(
 JSTypeNative.NUMBER_TYPE)));
 assertFalse(recordType.isSubtype(recordType2));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, new java.util.LinkedHashMap<>(), true);

 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, true);
 RecordType recordType3 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new java.util.LinkedHashMap<>(), false);
 RecordType recordType3 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType4 = new RecordType(registry, new java.util.LinkedHashMap<>(), false);
 RecordType recordType5 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType6 = new RecordType(registry, new java.util.LinkedHashMap<>(), false);

 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType3));
 assertEquals(recordType1, recordType3.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType1, recordType5.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType5, recordType1.getGreatestSubtypeHelper(recordType5));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType4));
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), recordType1.getGreatestSubtypeHelper(recordType6));
 }

 @Test
 public void testIsSynthetic() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new java.util.LinkedHashMap<>(), false);

 assertTrue(recordType1.isSynthetic() == false);
 assertTrue(recordType2.isSynthetic() == true);
 }

 @Test
 public void testFreeze() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));

 RecordType recordType = new RecordType(registry, properties, true);
 recordType.defineProperty("newProperty", registry.getStringType(), false, null);
 recordType.setFrozen(true);

 assertTrue(recordType.isFrozen());
 assertFalse(recordType.defineProperty("newProperty", registry.getStringType(), false, null));
 }
}