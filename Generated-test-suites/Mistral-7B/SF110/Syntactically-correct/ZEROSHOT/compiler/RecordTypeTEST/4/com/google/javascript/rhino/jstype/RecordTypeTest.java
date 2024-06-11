import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();

 Map<String, RecordProperty> recordProperties = new HashMap<>();
 recordProperties.put("a", new RecordProperty("a", registry.getStringType()));
 recordProperties.put("b", new RecordProperty("b", registry.getNumberType()));
 recordProperties.put("c", new RecordProperty("c", registry.getBooleanType()));

 recordType = new RecordType(registry, recordProperties, true);
 recordType2 = new RecordType(registry, recordProperties, false);
 recordType3 = new RecordType(registry, Arrays.asList("d", "e").stream()
     .map(k -> new RecordProperty(k, registry.getNumberType()))
     .collect(Collectors.toMap(RecordProperty::getName, Function.identity()))
     .get(Collections.emptyMap()), true);
 }

 @Test
 public void testIsSynthetic() {
 Assert.assertFalse(recordType.isSynthetic());
 Assert.assertTrue(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 Assert.assertTrue(recordType.checkRecordEquivalenceHelper(recordType,
     EquivalenceMethod.SAME_TYPES));
 Assert.assertFalse(recordType.checkRecordEquivalenceHelper(recordType2,
     EquivalenceMethod.SAME_TYPES));

 RecordType recordType4 = new RecordType(registry, recordProperties, true);
 Assert.assertTrue(recordType.checkRecordEquivalenceHelper(recordType4,
     EquivalenceMethod.SAME_TYPES));

 RecordType recordType5 = new RecordTypeBuilder(registry)
     .addProperty("a", registry.getNumberType())
     .addProperty("b", registry.getBooleanType())
     .addProperty("c", registry.getStringType())
     .build();
 Assert.assertFalse(recordType.checkRecordEquivalenceHelper(recordType5,
     EquivalenceMethod.SAME_TYPES));
 }

 @Test
 public void testIsSubtype() {
 // Record type is a subtype of itself
 Assert.assertTrue(recordType.isSubtype(recordType));

 // Record type is a subtype of its synthesized counterpart
 Assert.assertTrue(recordType.isSubtype(recordType2));

 // Record type with fewer properties is not a subtype of one with more
 Assert.assertFalse(recordType.isSubtype(recordType3));

 // Record type is a subtype of any type that is a record with the same properties
 RecordType recordType6 = new RecordTypeBuilder(registry)
     .addProperty("a", registry.getStringType())
     .addProperty("b", registry.getNumberType())
     .addProperty("c", registry.getBooleanType())
     .build();
 Assert.assertTrue(recordType.isSubtype(recordType6));

 // Object type is a subtype of any record type
 Assert.assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
     .isSubtype(recordType));
 }

 @Test
 public void testGetImplicitPrototype() {
 Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
     recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 // Test freezing
 recordType.defineProperty("frozen", registry.getBooleanType(), false,
     Node.NULL);
 Assert.assertFalse(recordType.isFrozen());
 recordType.defineProperty("frozen", registry.getBooleanType(), false,
     Node.NULL);
 Assert.assertTrue(recordType.isFrozen());

 // Test defining a property
 recordType = (RecordType) recordType.defineProperty("d", registry.getNumberType(),
     false, Node.NULL);
 Assert.assertEquals(registry.getNumberType(), recordType.getPropertyType("d"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyWithFrozenRecordType() {
 recordType.defineProperty("frozen", registry.getBooleanType(), false,
     Node.NULL);
 recordType.defineProperty("frozen", registry.getBooleanType(), false,
     Node.NULL);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 // Record type is its own greatest subtype
 Assert.assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType));

 // Empty record type is a subtype of any record type
 RecordType emptyRecordType = new RecordType(registry, Collections.emptyMap(),
     true);
 Assert.assertTrue(recordType.getGreatestSubtypeHelper(emptyRecordType)
     instanceof RecordType);

 // Record type with all properties same as another is a subtype of it
 RecordType recordType7 = new RecordTypeBuilder(registry)
     .addProperty("a", registry.getStringType())
     .addProperty("b", registry.getNumberType())
     .addProperty("c", registry.getBooleanType())
     .build();
 Assert.assertEquals(recordType7, recordType.getGreatestSubtypeHelper(recordType7));

 // Record type is a subtype of one with fewer properties if they are all
 // subtypes and the property names are the same
 RecordType recordType8 = new RecordTypeBuilder(registry)
     .addProperty("a", registry.getStringType())
     .build();
 Assert.assertEquals(recordType8, recordType.getGreatestSubtypeHelper(recordType8));

 // Record type is not a subtype of one with more properties if any property
 // is not a subtype of the corresponding property in the parent record type
 RecordType recordType9 = new RecordTypeBuilder(registry)
     .addProperty("a", registry.getStringType())
     .addProperty("d", registry.getStringType())
     .build();
 Assert.assertFalse(recordType.getGreatestSubtypeHelper(recordType9)
     instanceof RecordType);
 }
}