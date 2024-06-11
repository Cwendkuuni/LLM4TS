import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordProperty recordProperty;
 private PrototypeObjectType prototypeObjectType;
 private JSType recordType1;
 private JSType recordType2;
 private JSType unionType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordProperty = new RecordProperty("propertyName", JSType.STRING_TYPE);
 prototypeObjectType = new PrototypeObjectType(registry, null, null);
 recordType = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 recordType1 = new RecordType(registry, Map.of("propertyName", recordProperty), false);
 recordType2 = new RecordType(registry, Map.of("propertyName1", new RecordProperty("propertyName1", JSType.NUMBER_TYPE),
                                                 "propertyName2", new RecordProperty("propertyName2", JSType.BOOLEAN_TYPE)), true);
 }

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordType = new RecordType(registry, Map.of(), true);
 assertNotNull(recordType);
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType1.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType3 = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 RecordType recordType4 = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 assertTrue(recordType3.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTICAL));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordType recordType = new RecordType(registry, Map.of(), true);
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expectedImplicitPrototype = registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedImplicitPrototype, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("propertyName", JSType.STRING_TYPE, false, null));
 assertTrue(recordType.defineProperty("propertyName", JSType.STRING_TYPE, true, null));
 }

 @Test
 public void testDefineProperty_WithFrozenRecordType() {
 RecordType frozenRecordType = new RecordType(registry, Map.of(), true);
 frozenRecordType.isFrozen = true;
 assertFalse(frozenRecordType.defineProperty("propertyName", JSType.STRING_TYPE, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType expected = registry.getNativeType(JSTypeNative.NO_TYPE);
 assertEquals(expected, recordType.getGreatestSubtypeHelper(JSType.NUMBER_TYPE));

 JSType recordTypeRecord = new RecordType(registry, Map.of(), true);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionType = unionTypeBuilder.addAlternate(recordType)
 .addAlternate(JSType.NUMBER_TYPE)
 .build();
 JSType expectedUnionType = registry.getNativeType(JSTypeNative.OBJECT_TYPE)
 .getGreatestSubtype(JSType.NUMBER_TYPE);
 JSType result = recordType.getGreatestSubtypeHelper(expectedUnionType);
 assertEquals(expectedUnionType, result);
 }

 @Test
 public void testToMaybeRecordType() {
 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeA = new RecordType(registry, Map.of(), true);
 RecordType recordTypeB = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 RecordType recordTypeC = new RecordType(registry, Map.of(), true);
 RecordType recordTypeD = new RecordType(registry, Map.of("propertyName1", new RecordProperty("propertyName1", JSType.NUMBER_TYPE),
                                                         "propertyName2", new RecordProperty("propertyName2", JSType.BOOLEAN_TYPE)), true);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 JSType unionType = unionTypeBuilder.addAlternate(recordTypeB)
 .addAlternate(JSType.STRING_TYPE)
 .build();

 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(recordTypeA));
 assertTrue(recordTypeB.isSubtype(objectType));
 assertFalse(recordTypeA.isSubtype(recordTypeD));
 assertFalse(recordTypeA.isSubtype(unionType));
 }

 @Test
 public void testIsSubtype_WithFrozenRecordType() {
 RecordType recordTypeA = new RecordType(registry, Map.of(), true);
 RecordType recordTypeB = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 RecordType recordTypeB_frozen = new RecordType(registry, Map.of("propertyName", recordProperty), true);
 recordTypeB_frozen.isFrozen = true;

 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(recordTypeA));
 assertFalse(recordTypeB_frozen.isSubtype(recordTypeA));
 assertFalse(recordTypeA.isSubtype(recordTypeB_frozen));
 }
}