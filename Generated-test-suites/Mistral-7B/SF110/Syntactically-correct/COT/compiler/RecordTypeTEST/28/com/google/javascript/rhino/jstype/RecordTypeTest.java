import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class RecordTypeTest {

 private static JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testRecordTypeConstructorWithDeclaredProperties() {
 RecordType recordType = new RecordType(registry, createRecordProperties());
 assertTrue(recordType.isDeclared());
 }

 @Test
 public void testRecordTypeConstructorWithUndefinedRecordProperties() {
 RecordType recordType = new RecordType(registry, createUndefinedRecordProperties());
 assertFalse(recordType.isDeclared());
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, createRecordProperties(), false);
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createRecordProperties());
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.ALL));
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithDifferentProperties() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createDifferentRecordProperties());
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.ALL));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put(null, null);
 new RecordType(registry, properties);
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, createRecordProperties());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, createRecordProperties());
 String propertyName = "testProperty";
 JSType type = JSType.STRING;
 Node propertyNode = Node.newJsonObject();
 assertFalse(recordType.defineProperty(propertyName, type, false, propertyNode));
 assertTrue(recordType.hasProperty(propertyName));
 assertTrue(recordType.getPropertyType(propertyName).equals(type));
 }

 @Test
 public void testDefinePropertyFrozen() {
 RecordType recordType = new RecordType(registry, createRecordProperties());
 recordType.freeze();
 String propertyName = "testProperty";
 JSType type = JSType.STRING;
 Node propertyNode = Node.newJsonObject();
 assertFalse(recordType.defineProperty(propertyName, type, false, propertyNode));
 assertFalse(recordType.hasProperty(propertyName));
 }

 @Test
 public void testGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createDifferentRecordProperties());
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(JSTypeNative.NO_TYPE, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createDifferentRecordProperties());
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithSameRecordType() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createRecordProperties());
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithRecordAndObjectType() {
 RecordType recordType = new RecordType(registry, createRecordProperties());
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithRecordAndSameRecordType() {
 RecordType recordType1 = new RecordType(registry, createRecordProperties());
 RecordType recordType2 = new RecordType(registry, createRecordProperties());
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 private static Map<String, RecordProperty> createRecordProperties() {
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("property1", new RecordProperty("property1", JSType.NUMBER));
 properties.put("property2", new RecordProperty("property2", JSType.STRING));
 return properties;
 }

 private static Map<String, RecordProperty> createUndefinedRecordProperties() {
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("property1", null);
 properties.put("property2", null);
 return properties;
 }

 private static Map<String, RecordProperty> createDifferentRecordProperties() {
 Map<String, RecordProperty> properties1 = new HashMap<>();
 properties1.put("property1", new RecordProperty("property1", JSType.NUMBER));
 properties1.put("property2", new RecordProperty("property2", JSType.STRING));
 Map<String, RecordProperty> properties2 = new HashMap<>();
 properties2.put("property1", new RecordProperty("property1", JSType.BOOLEAN));
 properties2.put("property3", new RecordProperty("property3", JSType.NUMBER));
 return properties2;
 }
}