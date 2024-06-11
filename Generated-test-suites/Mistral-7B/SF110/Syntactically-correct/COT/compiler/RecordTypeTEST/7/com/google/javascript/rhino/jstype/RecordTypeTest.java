import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.Node;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private static RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties, true);
 }

 @SuppressWarnings("unchecked")
 private static Map<String, RecordProperty> createPropertiesMap() {
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("name1", new RecordProperty("name1", Integer.TYPE));
 properties.put("name2", new RecordProperty("name2", Double.TYPE));
 return properties;
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testRecordTypeConstructor() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = createRecordType(registry, properties);
 assertEquals(RecordType.class, recordType.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testIsSynthetic() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = createRecordType(registry, properties);
 assertFalse(recordType.isSynthetic());

 RecordType syntheticRecordType = createRecordType(registry, properties, false);
 assertTrue(syntheticRecordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties1 = createPropertiesMap();
 Map<String, RecordProperty> properties2 = createPropertiesMap();
 RecordType recordType1 = createRecordType(registry, properties1);
 RecordType recordType2 = createRecordType(registry, properties2);
 RecordType recordType3 = createRecordType(registry, properties1);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, new EquivalenceMethod() {}));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, new EquivalenceMethod() {}));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, new EquivalenceMethod() {}));
 }

 @Test
 public void testGetImplicitPrototype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = createRecordType(registry, createPropertiesMap());
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = createRecordType(registry, properties);

 String propertyName = "property";
 JSType propertyType = Integer.TYPE;
 Node propertyNode = Node.newIntLiteral(42);

 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 assertTrue(recordType.hasProperty(propertyName));

 RecordType recordTypeWithProperty = new RecordType(registry, properties, true) {
 @Override
 public boolean defineProperty(String propertyName, JSType type,
 boolean inferred, Node propertyNode) {
 return super.defineProperty(propertyName, type, true, propertyNode);
 }
 };

 assertTrue(recordTypeWithProperty.defineProperty(propertyName, propertyType, false, propertyNode));
 assertEquals(propertyType, recordTypeWithProperty.getPropertyType(propertyName));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = createPropertiesMap();
 RecordType recordType = createRecordType(registry, properties);

 String propertyName = "property";
 RecordProperty recordProperty = null;

 try {
 recordType.defineProperty(propertyName, Integer.TYPE, true, Node.newIdentifier("property"));
 fail("IllegalStateException not thrown.");
 } catch (IllegalStateException e) {
 assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
 }
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, createPropertiesMap());
 RecordType recordType2 = createRecordType(registry, createPropertiesMap());
 RecordType recordType3 = createRecordType(registry, Arrays.asList("name", "age").stream().collect(HashMap::new, (m, k) -> m.put(k, new RecordProperty(k, Integer.TYPE)), Map::putAll).map(HashMap::new).get(0));
 JSType recordType3Type = recordType3.getPropertyType("name");
 JSType recordType1Type = recordType1.getPropertyType("name");
 JSType recordType2Type = recordType2.getPropertyType("name");
 JSType recordType4 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType recordType5 = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

 assertEquals(recordType1.getImplicitPrototype(), recordType1.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType1.getImplicitPrototype(), recordType1.getGreatestSubtypeHelper(recordType3));
 assertEquals(recordType3, recordType3.getGreatestSubtypeHelper(recordType1));
 assertEquals(recordType4, recordType1.getGreatestSubtypeHelper(recordType4));
 assertEquals(recordType5, recordType1.getGreatestSubtypeHelper(recordType5));
 assertNotEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType2));
 assertNotEquals(recordType1, recordType1.getGreatestSubtypeHelper(recordType5));
 assertNotEquals(recordType1, recordType2.getGreatestSubtypeHelper(recordType1));
 assertNotEquals(recordType1, recordType2.getGreatestSubtypeHelper(recordType5));
 assertNotEquals(recordType1, recordType5.getGreatestSubtypeHelper(recordType1));
 assertNotEquals(recordType1, recordType5.getGreatestSubtypeHelper(recordType2));
 assertNotEquals(recordType1, recordType5.getGreatestSubtypeHelper(recordType5));
 }

 @Test
 public void testIsSubtype() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, createPropertiesMap());
 RecordType recordType2 = createRecordType(registry, Arrays.asList("name", "age").stream().collect(HashMap::new, (m, k) -> m.put(k, new RecordProperty(k, Integer.TYPE)), Map::putAll).map(HashMap::new).get(0));
 RecordType recordType3 = createRecordType(registry, Arrays.asList("name", "age", "city").stream().collect(HashMap::new, (m, k) -> m.put(k, new RecordProperty(k, Integer.TYPE)), Map::putAll).map(HashMap::new).get(0));
 RecordType recordType4 = createRecordType(registry, createPropertiesMap());
 RecordType recordType5 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType4));
 assertTrue(recordType5.isSubtype(recordType1));
 assertTrue(recordType5.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType2.isSubtype(recordType1));
 assertFalse(recordType2.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType3.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithEmptyRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, createPropertiesMap());
 RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType1.isSubtype(emptyRecordType));
 assertTrue(emptyRecordType.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeWithUnknownRecordType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType1 = createRecordType(registry, createPropertiesMap());
 ObjectType unknownRecordType = registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);

 assertFalse(recordType1.isSubtype(unknownRecordType));
 assertFalse(unknownRecordType.isSubtype(recordType1));
 }

 @Test
 public void testToString() {
 JSTypeRegistry registry = new JSTypeRegistry();
 RecordType recordType = createRecordType(registry, createPropertiesMap());
 assertEquals("RecordType(@[\"name1\":Integer, \"name2\":Double])", recordType.toString());
 }

}