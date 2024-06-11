import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordProperty recordProperty;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordProperty = new RecordProperty("testProperty", registry.getStringType());
 recordType = new RecordType(registry, Map.of("testProperty", recordProperty));
 }

 @Test
 public void testRecordTypeConstructorWithDeclaredProperty() {
 RecordType expectedRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), true);
 assertEquals(expectedRecordType, recordType);
 }

 @Test
 public void testRecordTypeConstructorWithUndefinedRecordProperty() {
 RecordType expectedRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), false);
 assertEquals(expectedRecordType, recordType);
 }

 @Test
 public void testIsSyntheticMethod() {
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithSameRecords() {
 RecordType sameRecord = new RecordType(registry,
 Map.of("testProperty", recordProperty), true);
 assertTrue(recordType.checkRecordEquivalenceHelper(sameRecord,
 EquivalenceMethod.IDENTITY_CHECK));
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithDifferentRecords() {
 RecordType differentRecord = new RecordType(registry,
 Map.of("testProperty", recordProperty.withName("testProperty2")), true);
 assertFalse(recordType.checkRecordEquivalenceHelper(differentRecord,
 EquivalenceMethod.IDENTITY_CHECK));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeWithNullRecordProperty() {
 RecordType recordTypeWithNullRecordProperty = new RecordType(registry,
 null, true);
 }

 @Test
 public void testGetImplicitPrototypeMethod() {
 ObjectType expectedImplicitPrototype = registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedImplicitPrototype, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefinePropertyMethodWithFrozenRecordType() {
 assertFalse(recordType.defineProperty("testProperty", registry.getStringType(),
 false, null));
 }

 @Test
 public void testDefinePropertyMethodWithNonFrozenRecordType() {
 assertTrue(recordType.defineProperty("testProperty2", registry.getStringType(),
 false, null));
 assertNotNull(recordType.getPropertyType("testProperty2"));
 }

 @Test
 public void testGetGreatestSubtypeHelperWithSameRecordTypes() {
 RecordType sameRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), true);
 JSType expectedGreatestSubtype = registry.getNativeObjectType(
 JSTypeNative.OBJECT_TYPE);
 assertEquals(expectedGreatestSubtype, recordType.getGreatestSubtypeHelper(sameRecordType));
 }

 @Test
 public void testGetGreatestSubtypeHelperWithDifferentRecordTypes() {
 RecordType differentRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), false);
 JSType expectedGreatestSubtype = registry.getNativeObjectType(
 JSTypeNative.NO_OBJECT_TYPE);
 assertEquals(expectedGreatestSubtype, recordType.getGreatestSubtypeHelper(differentRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithSameTypes() {
 RecordType sameRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), true);
 assertTrue(recordType.isSubtype(sameRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithDifferentTypes() {
 RecordType differentRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty), false);
 assertFalse(recordType.isSubtype(differentRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithObjectType() {
 assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 }

 @Test
 public void testIsSubtypeMethodWithEmptyRecordType() {
 RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).toMaybeRecordType();
 assertTrue(recordType.isSubtype(emptyRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithDerivedRecordType() {
 RecordProperty derivedRecordProperty = recordProperty.withOptional(true);
 RecordType derivedRecordType = new RecordType(registry,
 Map.of("testProperty", derivedRecordProperty), true);
 assertTrue(recordType.isSubtype(derivedRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getNumberType());
 RecordType incompatibleRecordType = new RecordType(registry,
 Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType2() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getBooleanType());
 RecordType incompatibleRecordType = new RecordType(registry,
 Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType3() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getArrayType(registry.getStringType(), 0));
 RecordType incompatibleRecordType = new RecordType(registry,
 Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType4() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getUnionType(registry.getStringType(), registry.getBooleanType()));
 RecordType incompatibleRecordType = new RecordType(registry,
 Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType5() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getRecordType(Map.of("testProperty3", registry.getStringType())));
 RecordType incompatibleRecordType = new RecordType(registry, Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType6() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getRecordType(Map.of("testProperty3", registry.getStringType()), "AnotherRecordType"));
 RecordType incompatibleRecordType = new RecordType(registry, Map.of("testProperty2", incompatibleRecordProperty), true);
 assertFalse(recordType.isSubtype(incompatibleRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType7() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getRecordType(Map.of("testProperty3", registry.getStringType()), "AnotherRecordType"));
 RecordType derivedRecordType = new RecordType(registry, Map.of("testProperty2", incompatibleRecordProperty), true);
 RecordType expectedRecordType = new RecordType(registry,
 Map.of("testProperty", recordProperty, "testProperty2", registry.getStringType()), true);
 assertTrue(recordType.isSubtype(expectedRecordType));
 }

 @Test
 public void testIsSubtypeMethodWithIncompatibleRecordType8() {
 RecordProperty incompatibleRecordProperty = new RecordProperty("testProperty2", registry.getRecordType(Map.of("testProperty3", registry.getStringType()), "AnotherRecordType"));
 RecordType derivedRecordType = new RecordType(registry, Map.of("testProperty2", incompatibleRecordProperty), true);
 RecordType expectedRecordType = new RecordType(registry, Map.of("testProperty", recordProperty), true);
 assertFalse(expectedRecordType.isSubtype(derivedRecordType));
 }
}