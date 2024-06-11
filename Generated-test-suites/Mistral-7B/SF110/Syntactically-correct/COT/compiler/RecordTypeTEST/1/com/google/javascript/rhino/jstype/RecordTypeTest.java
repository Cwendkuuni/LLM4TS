import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Map;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.PrototypeObjectType;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithProperties;
 private RecordType recordTypeWithFrozen;
 private RecordType recordTypeWithIncompatibleProperties;
 private RecordType recordTypeWithEmptySubtype;
 private RecordType recordTypeWithDifferentImplicitPrototype;

 @Before
 public void setup() {
 registry = mock(JSTypeRegistry.class);
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.NUMBER);
 Map<String, RecordProperty> propertiesMap = new Map<>();
 propertiesMap.put("prop1", recordProperty1);
 propertiesMap.put("prop2", recordProperty2);
 recordType = new RecordType(registry, propertiesMap, true);
 recordTypeWithProperties = new RecordType(registry, propertiesMap, false);
 recordTypeWithFrozen = new RecordType(registry, propertiesMap, true);
 recordTypeWithIncompatibleProperties = new RecordType(registry, propertiesMap, true);
 recordTypeWithEmptySubtype = new RecordType(registry, null, true);
 recordTypeWithDifferentImplicitPrototype = new RecordType(registry, propertiesMap, true);
 when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
 new PrototypeObjectType(registry, null, null));
 }

 @Test
 public void testRecordTypeConstructorWithDeclaredProperties() {
 RecordType recordType = new RecordType(registry, propertiesMap, true);
 assertEquals("RecordType with declared properties", recordType.getClass(),
 RecordType.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructorWithNullRecordProperty() {
 Map<String, RecordProperty> propertiesMap = new HashMap<>();
 RecordProperty recordProperty = null;
 propertiesMap.put("prop", recordProperty);
 new RecordType(registry, propertiesMap, true);
 }

 @Test
 public void testIsSyntheticMethod() {
 assertTrue("RecordType is synthetic", recordTypeWithProperties.isSynthetic());
 assertFalse("RecordType is not synthetic", recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithEqualRecords() {
 assertTrue("Equal records are equivalent", recordType.checkRecordEquivalenceHelper(
 recordTypeWithProperties, RecordType.EquivalenceMethod.STRICT));
 }

 @Test
 public void testCheckRecordEquivalenceHelperWithDifferentRecords() {
 assertFalse("Different records are not equivalent", recordType.checkRecordEquivalenceHelper(
 recordTypeWithIncompatibleProperties, RecordType.EquivalenceMethod.STRICT));
 }

 @Test
 public void testGetImplicitPrototypeMethod() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals("Implicit prototype is ObjectType",
 JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeID());
 }

 @Test
 public void testDefinePropertyMethodWithFrozenRecord() {
 assertFalse("Cannot define property on frozen record", recordTypeWithFrozen.defineProperty(
 "prop", JSType.STRING, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelperWithEmptySubtype() {
 JSType greatestSubtype = recordTypeWithEmptySubtype.getGreatestSubtypeHelper(JSType.ANY);
 assertEquals("Greatest subtype is NoType", JSTypeNative.NO_TYPE, greatestSubtype.getTypeID());
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeWithIncompatibleProperties() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithIncompatibleProperties);
 assertEquals("Greatest subtype is NoType", JSTypeNative.NO_TYPE, greatestSubtype.getTypeID());
 }

 @Test
 public void testIsSubtypeMethodWithEmptyRecordType() {
 assertFalse("Empty record type is not a subtype of any record type", recordTypeWithEmptySubtype.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeMethodWithCompatibleRecords() {
 assertTrue("Compatible records are subtypes of each other", recordType.isSubtype(recordTypeWithProperties));
 }

 @Test
 public void testIsSubtypeMethodWithDifferentImplicitPrototypes() {
 assertFalse("Record types with different implicit prototypes are not subtypes of each other", recordTypeWithDifferentImplicitPrototype.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeMethodWithObjectTypeAsParent() {
 assertTrue("ObjectType is a subtype of any record type", recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
 }

 @Test
 public void testIsSubtypeMethodWithRecordTypeAsChild() {
 assertTrue("RecordType is a subtype of its parent record type", recordTypeWithProperties.isSubtype(recordType));
 }

 @Test
 public void testUnionTypeWithIncompatibleProperties() {
 JSType unionType = recordType.getPropertyType("prop1").getLeastUpperBound(
 recordTypeWithIncompatibleProperties.getPropertyType("prop1"));
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(recordTypeWithProperties);
 JSType expectedUnionType = unionTypeBuilder.build();
 assertEquals("Union types are equal", expectedUnionType, unionType);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeWithInheritedProperties() {
 RecordType recordTypeWithInheritedProperties = new RecordType(registry, propertiesMap, true);
 recordTypeWithInheritedProperties.defineProperty("prop3", JSType.STRING, false, null);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithInheritedProperties);
 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(recordTypeWithProperties);
 unionTypeBuilder.addAlternate(recordTypeWithInheritedProperties);
 JSType expectedGreatestSubtype = unionTypeBuilder.build();
 assertEquals("Greatest subtypes are equal", expectedGreatestSubtype, greatestSubtype);
 }
}