import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;
 private RecordProperty recordProperty;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordProperty = new RecordProperty("propertyName", registry.getStringType());
 recordType = new RecordType(registry, Map.of("propertyName", recordProperty));
 recordType2 = new RecordType(registry, Map.of("propertyName", recordProperty));
 recordType3 = new RecordType(registry, Map.of("propertyName", recordProperty), false);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 assertTrue(recordType3.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(expected, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProperty", registry.getBooleanType(), false, null));
 assertTrue(recordType3.defineProperty("newProperty", registry.getBooleanType(), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordTypeSubtype = new RecordTypeBuilder(registry)
 .addProperty("propertyName", registry.getStringType())
 .build();
 JSType expected = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
 assertEquals(expected, recordType.getGreatestSubtypeHelper(recordTypeSubtype));
 }

 @Test
 public void testIsSubtype() {
 // RecordType is a subtype of ObjectType
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .isSubtype(recordType));
 // RecordType is a subtype of itself
 assertTrue(recordType.isSubtype(recordType));
 // RecordType is not a subtype of null
 assertFalse(recordType.isSubtype(null));
 // RecordType is not a subtype of a non-record type
 assertFalse(registry.getNativeIntType().isSubtype(recordType));
 // RecordType with same properties is a subtype of RecordType with different properties
 assertTrue(recordType.isSubtype(recordType2));
 // RecordType is not a subtype of a RecordType with different properties
 assertFalse(recordType.isSubtype(new RecordTypeBuilder(registry)
 .addProperty("newProperty", registry.getStringType())
 .build()));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordType() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentProperties() {
 assertFalse(recordType.isSubtype(new RecordTypeBuilder(registry)
 .addProperty("newProperty", registry.getStringType())
 .build()));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeFrozen() {
 RecordType frozenRecordType = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 assertFalse(frozenRecordType.isSubtype(new RecordTypeBuilder(registry)
 .addProperty("newProperty", registry.getStringType())
 .build()));
 }

 @Test
 public void testIsSubtypeRecordTypeToObjectType() {
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 }

 @Test
 public void testIsSubtypeObjectTypeToRecordType() {
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistry() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 JSType recordTypeRegistry2 = new RecordTypeBuilder(registry2)
 .addProperty("propertyName", registry.getStringType())
 .build();
 assertFalse(recordType.isSubtype(recordTypeRegistry2));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistryFrozen() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2Frozen = new RecordType(registry2, Map.of("propertyName", recordProperty), true);
 assertFalse(recordType.isSubtype(recordTypeRegistry2Frozen));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistryFrozen2() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2 = new RecordType(registry2, Map.of("propertyName", recordProperty), true);
 RecordType recordTypeRegistry3 = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 assertFalse(recordTypeRegistry2.isSubtype(recordTypeRegistry3));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistryFrozen3() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2 = new RecordType(registry2, Map.of("propertyName", recordProperty), true);
 RecordType recordTypeRegistry3 = new RecordType(registry, Map.of("propertyName", recordProperty), false);
 assertFalse(recordTypeRegistry2.isSubtype(recordTypeRegistry3));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistryFrozen4() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2 = new RecordType(registry2, Map.of("propertyName", recordProperty), false);
 RecordType recordTypeRegistry3 = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 assertFalse(recordTypeRegistry2.isSubtype(recordTypeRegistry3));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeDifferentRegistryFrozen5() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2 = new RecordType(registry2, Map.of("propertyName", recordProperty), true);
 RecordType recordTypeRegistry3 = new RecordType(registry, Map.of("propertyName", recordProperty), false);
 RecordType recordTypeRegistry4 = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 assertFalse(recordTypeRegistry2.isSubtype(recordTypeRegistry4));
 }
}