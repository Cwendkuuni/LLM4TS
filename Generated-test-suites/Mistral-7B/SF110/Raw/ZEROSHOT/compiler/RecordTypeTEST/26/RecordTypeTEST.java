import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithProperties;
 private RecordType recordTypeFrozen;
 private RecordType recordTypeNullProperties;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("name", registry.getStringType());
 RecordProperty property2 = new RecordProperty("age", registry.getNumberType());
 Map<String, RecordProperty> properties = Map.of("name", property1, "age", property2);
 recordType = new RecordType(registry, properties);
 recordTypeWithProperties = new RecordType(registry, properties, true);
 recordTypeFrozen = new RecordType(registry, properties);
 recordTypeFrozen.freeze();
 recordTypeNullProperties = new RecordType(registry, Map.of());
 }

 @Test
 public void testRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperties() {
 Map<String, RecordProperty> nullProperties = null;
 try {
 new RecordType(registry, nullProperties);
 } catch (IllegalStateException e) {
 assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
 }
 }

 @Test
 public void testDeclaredProperties() {
 assertEquals(2, recordType.getOwnPropertyNames().size());
 assertEquals(recordTypeWithProperties, recordType.toMaybeRecordType());
 }

 @Test
 public void testSynthesizedProperties() {
 assertEquals(2, recordTypeFrozen.getOwnPropertyNames().size());
 assertFalse(recordTypeFrozen.isSynthetic());
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getClass());
 }

 @Test
 public void testDefineProperty() {
 String propertyName = "newProperty";
 JSType newPropertyType = registry.getNumberType();
 Node newPropertyNode = Node.newIdent("newProperty");
 recordType.defineProperty(propertyName, newPropertyType, false, newPropertyNode);
 assertEquals(3, recordType.getOwnPropertyNames().size());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 recordTypeFrozen.defineProperty("newProperty", registry.getNumberType(), false, Node.UNDEFINED);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyDeclared() {
 recordType.defineProperty("name", registry.getStringType(), true, Node.UNDEFINED);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordTypeSubtype = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);
 assertEquals(objectType.getClass(), greatestSubtype.getClass());

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeSubtype);
 assertEquals(recordType.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testIsSubtype() {
 JSType recordTypeSubtype = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType recordTypeWithNullProperties = new RecordType(registry, Map.of());
 JSType nullType = registry.getNullType();
 JSType numberType = registry.getNumberType();
 JSType stringType = registry.getStringType();
 JSType recordTypeWithDifferentProperty = new RecordType(registry, Map.of("name", stringType, "age", numberType));

 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordTypeWithProperties));
 assertFalse(recordType.isSubtype(objectType));
 assertFalse(recordType.isSubtype(registry.getNullType()));
 assertFalse(recordType.isSubtype(nullType));
 assertFalse(recordType.isSubtype(registry.getBooleanType()));
 assertFalse(recordType.isSubtype(numberType));
 assertFalse(recordType.isSubtype(stringType));
 assertFalse(recordType.isSubtype(recordTypeWithNullProperties));
 assertFalse(recordType.isSubtype(recordTypeWithDifferentProperty));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordType() {
 RecordType recordType1 = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 RecordType recordType2 = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 RecordType recordType3 = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType(), "color", registry.getStringType()));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeRecordTypeToObjectType() {
 RecordType recordType = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertFalse(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeObjectTypeToRecordType() {
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, Map.of("name", registry.getStringType(), "age", registry.getNumberType()));
 assertTrue(objectType.isSubtype(recordType));
 }
}