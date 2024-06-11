import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.NUMBER);
 RecordProperty recordProperty3 = new RecordProperty("prop3", JSType.BOOLEAN);
 mapProperties(recordProperty1, recordProperty2, recordProperty3);
 recordType = new RecordType(registry, createRecordPropertiesMap());
 }

 private void mapProperties(RecordProperty... recordProperties) {
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 for (RecordProperty recordProperty : recordProperties) {
 recordPropertiesMap.put(recordProperty.getName(), recordProperty);
 }
 recordType = new RecordType(registry, recordPropertiesMap);
 }

 private Map<String, RecordProperty> createRecordPropertiesMap() {
 return Maps.newHashMap(recordPropertyMap());
 }

 private static Map<String, Object> recordPropertyMap() {
 return Maps.newHashMap(
 Maps.newEntry("prop1", new RecordProperty("prop1", JSType.STRING)),
 Maps.newEntry("prop2", new RecordProperty("prop2", JSType.NUMBER)),
 Maps.newEntry("prop3", new RecordProperty("prop3", JSType.BOOLEAN))
 );
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType2 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType3 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType4 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType5 = new RecordType(registry, createRecordPropertiesMap());

 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertTrue(recordType1.isEquivalentTo(recordType3));
 assertTrue(recordType1.isEquivalentTo(recordType4));
 assertTrue(recordType1.isEquivalentTo(recordType5));

 RecordType recordType6 = new RecordType(registry, createRecordPropertiesMapWithDifferentOrder());
 assertFalse(recordType1.isEquivalentTo(recordType6));

 RecordType recordType7 = new RecordType(registry, createRecordPropertiesMapWithNullProperty());
 assertFalse(recordType1.isEquivalentTo(recordType7));

 RecordType recordType8 = new RecordType(registry, createRecordPropertiesMapWithDifferentPropertyTypes());
 assertFalse(recordType1.isEquivalentTo(recordType8));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeID());
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType2 = new RecordType(registry, createRecordPropertiesMapWithSubtypeProperties());
 RecordType recordType3 = new RecordType(registry, createRecordPropertiesMapWithDifferentPropertyOrder());
 RecordType recordType4 = new RecordType(registry, createRecordPropertiesMapWithDifferentPropertyTypes());
 RecordType recordType5 = new RecordType(registry, createRecordPropertiesMapWithNullProperty());
 RecordType recordType6 = new RecordType(registry, JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getRecordType());
 RecordType recordType7 = new RecordType(registry, createRecordPropertiesMapWithEmptyProperties());

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType5));
 assertFalse(recordType1.isSubtype(recordType6));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NULL)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.VOID)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.NUMBER)));
 assertFalse(recordType1.isSubtype(registry.getNativeType(JSTypeNative.STRING)));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, createRecordPropertiesMap());
 RecordType recordType2 = new RecordType(registry, createRecordPropertiesMapWithSubtypeProperties());
 RecordType recordType3 = new RecordType(registry, createRecordPropertiesMapWithEmptyProperties());
 RecordType recordType4 = new RecordType(registry, createRecordPropertiesMapWithNullProperty());
 RecordType recordType5 = new RecordType(registry, createRecordPropertiesMapWithDifferentPropertyTypes());

 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
 assertEquals(recordType3, recordType1.getGreatestSubtypeHelper(recordType3));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType4));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType5));
 assertEquals(recordType1, recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NULL)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.VOID)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.BOOLEAN)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NUMBER)));
 assertNull(recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.STRING)));
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, createRecordPropertiesMap());
 assertFalse(recordType.defineProperty("prop1", JSType.NUMBER, false, null));
 assertTrue(recordType.hasProperty("prop1"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 RecordType recordType = new RecordType(registry, createRecordPropertiesMap());
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", JSType.NUMBER, false, null);
 }

 private Map<String, RecordProperty> createRecordPropertiesMapWithSubtypeProperties() {
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.NUMBER);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.STRING);
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 recordPropertiesMap.put("prop1", recordProperty1);
 recordPropertiesMap.put("prop2", recordProperty2);
 return recordPropertiesMap;
 }

 private Map<String, RecordProperty> createRecordPropertiesMapWithDifferentOrder() {
 RecordProperty recordProperty1 = new RecordProperty("prop2", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("prop1", JSType.NUMBER);
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 recordPropertiesMap.put("prop1", recordProperty1);
 recordPropertiesMap.put("prop2", recordProperty2);
 return recordPropertiesMap;
 }

 private Map<String, RecordProperty> createRecordPropertiesMapWithNullProperty() {
 RecordProperty recordProperty = new RecordProperty("prop1", JSType.NUMBER);
 recordProperty.setNullValue(true);
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 recordPropertiesMap.put("prop1", recordProperty);
 return recordPropertiesMap;
 }

 private Map<String, RecordProperty> createRecordPropertiesMapWithDifferentPropertyTypes() {
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.NUMBER);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.BOOLEAN);
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 recordPropertiesMap.put("prop1", recordProperty1);
 recordPropertiesMap.put("prop2", recordProperty2);
 return recordPropertiesMap;
 }

 private Map<String, RecordProperty> createRecordPropertiesMapWithEmptyProperties() {
 Map<String, RecordProperty> recordPropertiesMap = Maps.newHashMap();
 return recordPropertiesMap;
 }
}