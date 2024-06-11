import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordType;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry = new JSTypeRegistry();
 private Map<String, RecordProperty> properties;

 @Test
 public void testRecordTypeConstructorWithDeclaredProperties() {
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isDeclared());
 }

 @Test
 public void testRecordTypeConstructorWithUndefinedRecordProperty() {
 RecordType recordType = new RecordType(registry, properties);
 assertFalse(recordType.hasProperty("undefinedProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeConstructorWithNullRecordProperty() {
 RecordType recordType = new RecordType(registry, null);
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertEquals(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY), true);

 RecordType recordType3 = new RecordType(registry, new HashMap<>());
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = new ObjectType(registry, null, null);
 RecordType recordType = new RecordType(registry, properties);
 assertEquals(implicitPrototype, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, properties);
 String propertyName = "propertyName";
 JSType propertyType = new IntType(registry);
 Node propertyNode = new Node_StringLiteral("propertyName");
 assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithFrozenRecordType() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.setFrozen(true);
 String propertyName = "propertyName";
 JSType propertyType = new IntType(registry);
 Node propertyNode = new Node_StringLiteral("propertyName");
 assertFalse(recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 }

 @Test
 public void testGetGreatestSubtypeHelperWithEmptyRecordType() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeWithSameProperties() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeWithAdditionalProperties() {
 RecordType recordType1 = new RecordType(registry, properties);
 Map<String, RecordProperty> additionalProperties = new HashMap<>();
 additionalProperties.put("additionalProperty", new RecordProperty("additionalProperty", new IntType(registry), null));
 RecordType recordType2 = new RecordType(registry, new HashMap<>());
 recordType2.addProperties(additionalProperties);
 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithRecordTypeWithDifferentPropertyTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordProperty property = new RecordProperty("property", new IntType(registry), null);
 properties.put("property", property);
 RecordType recordType2 = new RecordType(registry, properties);
 properties.put("property", new StringType(registry));
 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType1, greatestSubtype);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetGreatestSubtypeHelperWithRecordTypeWithNullProperty() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, null);
 RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 fail("This should not be reached.");
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isSubtype(recordType2));

 RecordType recordType3 = new RecordType(registry, new HashMap<>());
 assertFalse(recordType1.isSubtype(recordType3));

 RecordType recordType4 = new ObjectType(registry, null, null);
 assertFalse(recordType1.isSubtype(recordType4));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndObjectType() {
 ObjectType objectType = new ObjectType(registry, null, null);
 RecordType recordType = new RecordType(registry, properties);
 assertTrue(recordType.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithLessProperties() {
 RecordType recordType1 = new RecordType(registry, properties);
 String propertyToRemove = "propertyToRemove";
 properties.remove(propertyToRemove);
 RecordType recordType2 = new RecordType(registry, properties);
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithDifferentPropertyTypes() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordProperty property = new RecordProperty("property", new IntType(registry), null);
 properties.put("property", property);
 RecordType recordType2 = new RecordType(registry, properties);
 properties.put("property", new StringType(registry));
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithSamePropertiesButDifferentOrder() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, new HashMap<>());
 for (String propertyName : properties.keySet()) {
 recordType2.defineProperty(propertyName, recordType1.getPropertyType(propertyName), false, null);
 }
 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithRecordTypeAndRecordTypeWithDifferentFrozenStatus() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 recordType1.setFrozen(true);
 assertFalse(recordType1.isSubtype(recordType2));
 }
}