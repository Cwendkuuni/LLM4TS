import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private RecordType createRecordType(JSTypeRegistry registry, Map<String, RecordProperty> properties) {
 return new RecordType(registry, properties, true);
 }

 @Test
 public void testRecordTypeConstructor() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty(JSType.STRING, null));
 properties.put("age", new RecordProperty(JSType.NUMBER, null));
 RecordType recordType = createRecordType(registry, properties);
 assertEquals(RecordType.class, recordType.getClass());
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), new java.util.HashMap<>());
 assertFalse(recordType.isSynthetic());
 RecordType syntheticRecordType = new RecordType(new JSTypeRegistry(), new java.util.HashMap<>(), false);
 assertTrue(syntheticRecordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType type1 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type2 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type3 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type4 = createRecordType(registry, new java.util.HashMap<>());
 RecordProperty nameProp = new RecordProperty(JSType.STRING, null);
 RecordProperty ageProp = new RecordProperty(JSType.NUMBER, null);
 RecordProperty idProp = new RecordProperty(JSType.NUMBER, null);
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", nameProp);
 properties1.put("age", ageProp);
 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", nameProp);
 properties2.put("age", ageProp);
 properties2.put("id", idProp);
 Map<String, RecordProperty> properties3 = new java.util.HashMap<>();
 properties3.put("name", nameProp);
 Map<String, RecordProperty> properties4 = new java.util.HashMap<>();
 properties4.put("age", ageProp);

 assertTrue(type1.checkRecordEquivalenceHelper(type1, EquivalenceMethod.STRUCTURAL));
 assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.STRUCTURAL));
 assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.STRUCTURAL));
 assertFalse(type1.checkRecordEquivalenceHelper(type4, EquivalenceMethod.STRUCTURAL));

 assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.IDENTICAL));
 assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.STRUCTURAL));
 assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.IDENTICAL));
 assertFalse(type1.checkRecordEquivalenceHelper(type3, EquivalenceMethod.STRUCTURAL));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", null);
 createRecordType(registry, properties);
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), new java.util.HashMap<>());
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(ObjectType.class, implicitPrototype.getClass());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), new java.util.HashMap<>());
 assertFalse(recordType.defineProperty("name", JSType.STRING, false, null));
 recordType.defineProperty("name", JSType.STRING, true, null);
 assertTrue(recordType.hasProperty("name"));
 }

 @Test
 public void testDefineProperty_Frozen() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), new java.util.HashMap<>());
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty("name", JSType.STRING, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType type1 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type2 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type3 = createRecordType(registry, new java.util.HashMap<>());
 RecordProperty nameProp = new RecordProperty(JSType.STRING, null);
 RecordProperty ageProp = new RecordProperty(JSType.NUMBER, null);
 RecordProperty idProp = new RecordProperty(JSType.NUMBER, null);
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", nameProp);
 properties1.put("age", ageProp);
 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", nameProp);
 properties2.put("age", ageProp);
 properties2.put("id", idProp);
 Map<String, RecordProperty> properties3 = new java.util.HashMap<>();
 properties3.put("name", nameProp);
 Map<String, RecordProperty> properties4 = new java.util.HashMap<>();
 properties4.put("age", ageProp);

 JSType type1Type = type1.getType();
 JSType type2Type = type2.getType();
 JSType type3Type = type3.getType();
 JSType type4Type = type4.getType();
 JSType greatestSubtype = type1.getGreatestSubtypeHelper(type2);
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(type3);
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(type4);
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(type1);
 assertEquals(type1Type, greatestSubtype);

 greatestSubtype = type1.getGreatestSubtypeHelper(type2Type);
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(type3Type);
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(type4Type);
 assertEquals(type1Type, greatestSubtype);

 greatestSubtype = type1.getGreatestSubtypeHelper(JSTypeFactory.booleanType);
 assertEquals(JSTypeFactory.booleanType, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(JSTypeFactory.stringType);
 assertEquals(JSTypeFactory.stringType, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(JSTypeFactory.numberType);
 assertEquals(JSTypeFactory.numberType, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(JSTypeFactory.nullType);
 assertEquals(JSTypeFactory.nullType, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(JSTypeFactory.objectType);
 assertEquals(type1Type, greatestSubtype);

 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties1));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties2));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties3));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties4));
 assertEquals(type1Type, greatestSubtype);

 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties1, false));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties2, false));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties3, false));
 assertEquals(type1Type, greatestSubtype);
 greatestSubtype = type1.getGreatestSubtypeHelper(new RecordType(registry, properties4, false));
 assertEquals(type1Type, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType type1 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type2 = createRecordType(registry, new java.util.HashMap<>());
 RecordType type3 = createRecordType(registry, new java.util.HashMap<>());
 RecordProperty nameProp = new RecordProperty(JSType.STRING, null);
 RecordProperty ageProp = new RecordProperty(JSType.NUMBER, null);
 RecordProperty idProp = new RecordProperty(JSType.NUMBER, null);
 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", nameProp);
 properties1.put("age", ageProp);
 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", nameProp);
 properties2.put("age", ageProp);
 properties2.put("id", idProp);
 Map<String, RecordProperty> properties3 = new java.util.HashMap<>();
 properties3.put("name", nameProp);
 Map<String, RecordProperty> properties4 = new java.util.HashMap<>();
 properties4.put("age", ageProp);

 assertTrue(type1.isSubtype(type1));
 assertTrue(type1.isSubtype(type1.getType()));
 assertTrue(type1.isSubtype(JSTypeFactory.objectType));
 assertTrue(type1.isSubtype(JSTypeFactory.objectType.getType()));

 assertTrue(type1.isSubtype(type2));
 assertTrue(type1.isSubtype(type2.getType()));
 assertTrue(type2.isSubtype(type1));
 assertTrue(type2.isSubtype(type1.getType()));

 assertFalse(type1.isSubtype(type3));
 assertFalse(type1.isSubtype(type3.getType()));
 assertFalse(type3.isSubtype(type1));
 assertFalse(type3.isSubtype(type1.getType()));

 assertFalse(type1.isSubtype(null));
 assertFalse(type1.isSubtype(JSTypeFactory.booleanType));
 assertFalse(type1.isSubtype(JSTypeFactory.stringType));
 assertFalse(type1.isSubtype(JSTypeFactory.numberType));

 assertTrue(RecordType.isSubtype(type1, type2));
 assertTrue(RecordType.isSubtype(type1, type2.getType()));
 assertFalse(RecordType.isSubtype(type2, type1));
 assertFalse(RecordType.isSubtype(type2, type1.getType()));

 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties1)));
 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties1).getType()));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties1), type1));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties1), type1.getType()));

 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties2)));
 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties2).getType()));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties2), type1));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties2), type1.getType()));

 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties3)));
 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties3).getType()));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties3), type1));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties3), type1.getType()));

 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties4)));
 assertTrue(RecordType.isSubtype(type1, new RecordType(registry, properties4).getType()));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties4), type1));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties4), type1.getType()));

 assertTrue(RecordType.isSubtype(new RecordType(registry, properties1), new RecordType(registry, properties2)));
 assertTrue(RecordType.isSubtype(new RecordType(registry, properties1).getType(), new RecordType(registry, properties2).getType()));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties2), new RecordType(registry, properties1)));
 assertFalse(RecordType.isSubtype(new RecordType(registry, properties2).getType(), new RecordType(registry, properties1).getType()));
 }

 @Test
 public void testToString() {
 RecordType recordType = createRecordType(new JSTypeRegistry(), new java.util.HashMap<>());
 assertEquals("RecordType{}", recordType.toString());
 }

}