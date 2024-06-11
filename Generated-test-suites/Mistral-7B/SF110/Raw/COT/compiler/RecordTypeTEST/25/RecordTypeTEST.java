import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private static JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;

 @BeforeClass
 public static void setUpBeforeClass() {
 registry = new JSTypeRegistry();
 }

 @Before
 public void setUp() {
 Map<String, RecordProperty> properties = new HashMap<>();
 RecordProperty prop1 = new RecordProperty("prop1", registry.getBooleanType());
 RecordProperty prop2 = new RecordProperty("prop2", registry.getStringType());
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);

 recordType = new RecordType(registry, properties, true);
 recordType2 = new RecordType(registry, properties, false);
 }

 @Test
 public void testRecordTypeConstructor() {
 // Test constructor with declared record type
 assertNotNull(recordType);
 assertEquals(recordType.getClass(), RecordType.class);
 assertFalse(recordType.isFrozen());

 // Test constructor with synthesized record type
 assertNotNull(recordType2);
 assertEquals(recordType2.getClass(), RecordType.class);
 assertTrue(recordType2.isFrozen());
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType.isSynthetic());
 assertFalse(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 Map<String, RecordProperty> nullProperties = new HashMap<>();
 // recordType = new RecordType(registry, nullProperties); // Throws exception
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(implicitPrototype.getClass(), ObjectType.class);
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProp", registry.getIntType(), false, null));
 assertTrue(recordType.defineProperty("newProp", registry.getIntType(), true, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 recordType.isFrozen = false; // Temporarily disable isFrozen
 assertFalse(recordType.defineProperty("newProp", registry.getIntType(), false, null));
 recordType.isFrozen = true; // Re-enable isFrozen
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyNullPropertyName() {
 assertFalse(recordType.defineProperty(null, registry.getIntType(), false, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyNullPropertyType() {
 assertFalse(recordType.defineProperty("newProp", null, false, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyNullPropertyNode() {
 assertFalse(recordType.defineProperty("newProp", registry.getIntType(), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getObjectType());
 assertEquals(greatestSubtype.getClass(), registry.getNativeObjectType(JSTypeNative.NO_TYPE).getClass());

 JSType objType = registry.getObjectType();
 JSType greatestSubtypeObj = recordType.getGreatestSubtypeHelper(objType);
 assertEquals(greatestSubtypeObj.getClass(), objType.getClass());

 JSType recordTypeSubtype = registry.getRecordType(new HashMap<>());
 JSType greatestSubtypeRecord = recordType.getGreatestSubtypeHelper(recordTypeSubtype);
 assertNotNull(greatestSubtypeRecord);
 }

 @Test
 public void testToMaybeRecordType() {
 assertEquals(recordType, recordType.toMaybeRecordType());
 assertEquals(recordType2, recordType2.toMaybeRecordType());
 }

 @Test
 public void testIsSubtype() {
 // ObjectType is a subtype of RecordType
 assertTrue(registry.getObjectType().isSubtype(recordType));
 // RecordType is not a subtype of ObjectType
 assertFalse(recordType.isSubtype(registry.getObjectType()));
 // RecordType is a subtype of itself
 assertTrue(recordType.isSubtype(recordType));
 // RecordType is a subtype of another RecordType with the same properties
 RecordType recordType3 = new RecordType(registry, properties, true);
 assertTrue(recordType.isSubtype(recordType3));
 // RecordType is not a subtype of another RecordType with different properties
 RecordType recordType4 = new RecordType(registry, new HashMap<>(), true);
 assertFalse(recordType.isSubtype(recordType4));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordType() {
 // RecordType is a subtype of another RecordType
 assertTrue(recordType.isSubtype(recordType2));
 // Another RecordType is a subtype of this RecordType
 assertTrue(recordType2.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToObjectType() {
 // RecordType is not a subtype of ObjectType
 assertFalse(recordType.isSubtype(registry.getObjectType()));
 // ObjectType is a subtype of RecordType
 assertTrue(registry.getObjectType().isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToEmptyRecordType() {
 // RecordType is not a subtype of empty RecordType
 assertFalse(recordType.isSubtype(registry.getEmptyRecordType()));
 // Empty RecordType is a subtype of RecordType
 assertTrue(registry.getEmptyRecordType().isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToNull() {
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeRecordTypeToDifferentRegistry() {
 JSTypeRegistry registry2 = new JSTypeRegistry();
 RecordType recordTypeRegistry2 = new RecordType(registry2, properties, true);
 assertFalse(recordType.isSubtype(recordTypeRegistry2));
 }

 @Test
 public void testIsSubtypeRecordTypeToDifferentProperties() {
 Map<String, RecordProperty> properties2 = new HashMap<>();
 properties2.put("prop3", new RecordProperty("prop3", registry.getStringType()));
 RecordType recordTypeProperties2 = new RecordType(registry, properties2, true);
 assertFalse(recordType.isSubtype(recordTypeProperties2));
 }

 @Test
 public void testIsSubtypeRecordTypeToDifferentType() {
 RecordType recordTypeDifferentType = new RecordType(registry, properties, false);
 assertFalse(recordType.isSubtype(recordTypeDifferentType));
 }

 @Test
 public void testIsSubtypeRecordTypeToUnionType() {
 // RecordType is not a subtype of UnionType
 assertFalse(recordType.isSubtype(new UnionTypeBuilder(registry).build()));
 // UnionType with RecordType as one of the alternatives is a subtype of RecordType
 UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
 unionBuilder.addAlternate(recordType);
 JSType unionType = unionBuilder.build();
 assertTrue(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithNullProperty() {
 Map<String, RecordProperty> propertiesNull = new HashMap<>();
 RecordType recordTypeNull = new RecordType(registry, propertiesNull, true);
 assertFalse(recordType.isSubtype(recordTypeNull));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithEmptyProperty() {
 RecordType recordTypeEmpty = new RecordType(registry, new HashMap<>(), true);
 assertFalse(recordType.isSubtype(recordTypeEmpty));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithSingleProperty() {
 Map<String, RecordProperty> propertiesSingle = new HashMap<>();
 propertiesSingle.put("prop3", new RecordProperty("prop3", registry.getBooleanType()));
 RecordType recordTypeSingle = new RecordType(registry, propertiesSingle, true);
 assertFalse(recordType.isSubtype(recordTypeSingle));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithDifferentProperty() {
 Map<String, RecordProperty> propertiesDifferent = new HashMap<>();
 propertiesDifferent.put("prop3", new RecordProperty("prop3", registry.getStringType()));
 RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent, true);
 assertFalse(recordType.isSubtype(recordTypeDifferent));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithInvariantProperty() {
 Map<String, RecordProperty> propertiesInvariant = new HashMap<>();
 propertiesInvariant.put("prop3", new RecordProperty("prop3", registry.getIntType()));
 RecordType recordTypeInvariant = new RecordType(registry, propertiesInvariant, true);
 assertTrue(recordType.isSubtype(recordTypeInvariant));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithSubtypeProperty() {
 Map<String, RecordProperty> propertiesSubtype = new HashMap<>();
 propertiesSubtype.put("prop3", registry.getBooleanType());
 RecordType recordTypeSubtype = new RecordType(registry, propertiesSubtype, true);
 assertTrue(recordType.isSubtype(recordTypeSubtype));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithSupertypeProperty() {
 Map<String, RecordProperty> propertiesSupertype = new HashMap<>();
 propertiesSupertype.put("prop3", registry.getIntType());
 RecordType recordTypeSupertype = new RecordType(registry, propertiesSupertype, true);
 assertTrue(recordTypeSupertype.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithNullPropertyNode() {
 Map<String, RecordProperty> propertiesNullNode = new HashMap<>();
 propertiesNullNode.put("prop3", new RecordProperty("prop3", null));
 RecordType recordTypeNullNode = new RecordType(registry, propertiesNullNode, true);
 assertFalse(recordType.isSubtype(recordTypeNullNode));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithEmptyPropertyNode() {
 Map<String, RecordProperty> propertiesEmptyNode = new HashMap<>();
 propertiesEmptyNode.put("prop3", new RecordProperty("prop3", new Node()));
 RecordType recordTypeEmptyNode = new RecordType(registry, propertiesEmptyNode, true);
 assertFalse(recordType.isSubtype(recordTypeEmptyNode));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordTypeWithDifferentPropertyNode() {
 Map<String, RecordProperty> propertiesDifferentNode = new HashMap<>();
 propertiesDifferentNode.put("prop3", new RecordProperty("prop3", new Node()));
 RecordType recordTypeDifferentNode = new RecordType(registry, propertiesDifferentNode, true);
 assertFalse(recordType.isSubtype(recordTypeDifferentNode));
 }
}