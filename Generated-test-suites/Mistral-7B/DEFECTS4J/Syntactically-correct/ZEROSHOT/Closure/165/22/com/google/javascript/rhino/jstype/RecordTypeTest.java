import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty prop1 = new RecordProperty("prop1", registry.getIntType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getStringType());
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 assertEquals(recordType1, recordType2);
 assertEquals(recordType1, recordType1);
 assertNotEquals(recordType1, recordType3);
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("prop3", registry.getIntType(),
 false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordTypeType = recordType.getGreatestSubtypeHelper(registry.getNullType());
 JSType intType = registry.getIntType();
 JSType recordTypeWithInt = new RecordTypeBuilder(registry)
 .addProperty("prop1", intType, null).build();
 JSType recordTypeWithNull = new RecordTypeBuilder(registry)
 .addProperty("prop1", intType).build();
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType recordTypeWithObject = new RecordTypeBuilder(registry)
 .addProperty("prop1", objectType, null).build();
 JSType unionType = new UnionTypeBuilder(registry)
 .addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE))
 .addAlternate(intType)
 .addAlternate(recordTypeWithInt)
 .addAlternate(recordTypeWithNull)
 .addAlternate(recordType)
 .addAlternate(recordTypeType)
 .addAlternate(objectType)
 .build();

 assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType));
 assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordTypeType));
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getGreatestSubtypeHelper(objectType));
 assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE),
 recordType.getGreatestSubtypeHelper(registry.getNullType()));
 assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE),
 recordType.getGreatestSubtypeHelper(intType));
 assertEquals(recordTypeWithInt, recordType.getGreatestSubtypeHelper(recordTypeWithInt));
 assertEquals(recordTypeWithNull, recordType.getGreatestSubtypeHelper(recordTypeWithNull));
 assertEquals(unionType, recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeA = new RecordTypeBuilder(registry)
 .addProperty("prop1", registry.getIntType()).build();
 RecordType recordTypeB = new RecordTypeBuilder(registry)
 .addProperty("prop1", registry.getIntType())
 .addProperty("prop2", registry.getStringType()).build();
 RecordType recordTypeC = new RecordTypeBuilder(registry)
 .addProperty("prop1", registry.getIntType())
 .addProperty("prop3", registry.getStringType()).build();
 RecordType recordTypeD = new RecordTypeBuilder(registry)
 .addProperty("prop1", registry.getStringType())
 .addProperty("prop2", registry.getStringType()).build();

 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertFalse(recordTypeA.isSubtype(registry.getNullType()));
 assertFalse(recordTypeA.isSubtype(recordTypeC));
 assertFalse(recordTypeA.isSubtype(recordTypeD));
 }

 @Test
 public void testToMaybeRecordType() {
 assertEquals(recordType, recordType.toMaybeRecordType());
 }
}