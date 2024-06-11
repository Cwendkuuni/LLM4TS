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

 Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties, true);

 assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 assertEquals(2, recordType.properties.size());
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties, true);

 recordType.defineProperty("gender", String.class, false, null);
 assertEquals(3, recordType.properties.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozenRecord() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties, true);
 recordType.isFrozen = true;
 recordType.defineProperty("gender", String.class, false, null);
 }

 @Test
 public void testRecordEquivalence() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);

 Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 recordProperties2.put("gender", new RecordProperty("gender", String.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 }

 @Test
 public void testRecordEquivalenceWithDifferentOrder() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 recordProperties1.put("name", new RecordProperty("name", String.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);

 Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
 }

 @Test
 public void testRecordSubtypeWithIncompatibleTypes() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);

 Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
 recordProperties2.put("name", new RecordProperty("name", Double.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordSubtypeWithParentRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);

 Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 recordProperties2.put("gender", new RecordProperty("gender", String.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testRecordSubtypeWithSameRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties1 = new Maps.HashMap<>();
 recordProperties1.put("name", new RecordProperty("name", String.class));
 recordProperties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, recordProperties1, true);

 Map<String, RecordProperty> recordProperties2 = new Maps.HashMap<>();
 recordProperties2.put("name", new RecordProperty("name", String.class));
 recordProperties2.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType2 = new RecordType(registry, recordProperties2, true);

 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 }

 @Test
 public void testRecordSubtypeWithObjectType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = new Maps.HashMap<>();
 recordProperties.put("name", new RecordProperty("name", String.class));
 recordProperties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, recordProperties, true);

 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
 }

 @Test
 public void testRecordSubtypeWithEmptyRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 RecordType recordType = new RecordType(registry, Maps.newTreeMap(), true);
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
 }

 @Test
 public void testRecordSubtypeWithNoRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();

 assertFalse(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).isSubtype(new RecordType(registry, Maps.newTreeMap(), true)));
 }
}