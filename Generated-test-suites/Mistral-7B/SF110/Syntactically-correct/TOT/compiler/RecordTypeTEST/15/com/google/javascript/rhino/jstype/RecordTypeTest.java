import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeCreation() {
 RecordProperty prop1 = new RecordProperty("name", registry.getNativeStringType());
 RecordProperty prop2 = new RecordProperty("age", registry.getNativeNumberType());

 Map<String, RecordProperty> properties = Map.of("name", prop1, "age", prop2);
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals("RecordType with declared properties", 2, recordType.getOwnPropertyNames().size());
 }

 @Test
 public void testRecordTypeCreationWithUndefinedRecordProperty() {
 RecordProperty prop = new RecordProperty("name", registry.getNativeStringType());
 Map<String, RecordProperty> properties = Map.of("name", prop);
 RecordType recordType = new RecordType(registry, properties, false);
 assertEquals("RecordType with synthesized properties", 1, recordType.getOwnPropertyNames().size());
 }

 @Test
 public void testRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 RecordType recordType2 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 RecordType recordType3 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType(), "address", registry.getNativeStringType()), true);

 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.setSynthesized(true);
 builder.addProperty("name", registry.getNativeStringType(), NodeOperations.NOP);
 builder.addProperty("age", registry.getNativeNumberType(), NodeOperations.NOP);
 builder.addProperty("address", registry.getNativeStringType(), NodeOperations.NOP);
 RecordType recordType4 = builder.build();

 assertTrue("Record types 1 and 2 are equivalent", recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
 assertFalse("Record types 1 and 3 are not equivalent", recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));
 assertTrue("Record types 1 and 4 are equivalent", recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.STRICT));
 }

 @Test
 public void testRecordTypeGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals("Implicit prototype is Object type", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }

 @Test
 public void testRecordTypeDefineProperty() {
 RecordType recordType = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);

 boolean isFrozen = recordType.isFrozen();
 recordType.defineProperty("gender", registry.getNativeStringType(), false, NodeOperations.NOP);
 assertFalse("Record is not frozen", recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeDefinePropertyWhenFrozen() {
 RecordType recordType = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 recordType.isFrozen = true; // Setting it manually as a test
 recordType.defineProperty("gender", registry.getNativeStringType(), false, NodeOperations.NOP);
 }

 @Test
 public void testRecordTypeGetGreatestSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 RecordType recordType2 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType(), "gender", registry.getNativeStringType()), true);

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals("Greatest subtype of recordType1 with recordType2", recordType2, greatestSubtype);
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType()), true);
 RecordType recordType2 = new RecordType(registry, Map.of("name", registry.getNativeStringType(), "age", registry.getNativeNumberType(), "gender", registry.getNativeStringType()), true);
 RecordType recordType3 = new RecordType(registry, Map.of("name", registry.getNativeStringType()), true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue("recordType1 is subtype of recordType2", recordType1.isSubtype(recordType2));
 assertTrue("recordType1 is subtype of Object type", objectType.isSubtype(recordType1));
 assertFalse("recordType1 is subtype of recordType3", recordType1.isSubtype(recordType3));
 }
}