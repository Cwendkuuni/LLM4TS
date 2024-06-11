import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

 private static JSTypeRegistry registry;

 @BeforeClass
 public static void setup() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeCreation() {
 // Test creating a record type with simple properties
 String[] propertyNames = {"name", "age"};
 RecordProperty[] propertyTypes = {new RecordProperty(registry, "String"),
 new RecordProperty(registry, "Number")};
 RecordType recordType = new RecordType(registry,
 Maps.newHashMap());
 for (int i = 0; i < propertyNames.length; i++) {
 recordType.defineProperty(propertyNames[i], propertyTypes[i].getType(),
 false, new Node());
 }

 // Test getting property names
 assertEquals(2, recordType.getPropertyNames().length);
 for (String propertyName : propertyNames) {
 assertEquals(propertyName, recordType.getPropertyName(0));
 }

 // Test getting property types
 JSType nameType = recordType.getPropertyType("name");
 JSType ageType = recordType.getPropertyType("age");
 assertEquals(JSType.isString(), nameType.isString());
 assertEquals(JSType.isNumber(), ageType.isNumber());
 }

 @Test
 public void testRecordTypeEquivalence() {
 RecordProperty nameType = new RecordProperty(registry, "String");
 RecordProperty ageType = new RecordProperty(registry, "Number");
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", nameType.getType(), false, new Node());
 recordType1.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("name", nameType.getType(), false, new Node());
 recordType2.defineProperty("age", ageType.getType(), false, new Node());

 // Test that record types are equivalent when they have the same properties
 // with the same types
 assertTrue(recordType1.isEquivalentTo(recordType2));

 // Test that record types are not equivalent when they have different properties
 RecordProperty idType = new RecordProperty(registry, "Number");
 recordType1.defineProperty("id", idType.getType(), false, new Node());
 assertFalse(recordType1.isEquivalentTo(recordType2));
 }

 @Test
 public void testRecordTypeSubtyping() {
 RecordProperty nameType = new RecordProperty(registry, "String");
 RecordProperty ageType = new RecordProperty(registry, "Number");
 RecordProperty idType = new RecordProperty(registry, "Number");
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", nameType.getType(), false, new Node());
 recordType1.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("name", nameType.getType(), false, new Node());
 recordType2.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("id", idType.getType(), false, new Node());

 // Test that record type1 is a supertype of record type2 since it has all
 // of the properties of record type2
 assertTrue(recordType1.isSubtype(recordType2));
 // Test that record type2 is not a supertype of record type1 since it has
 // an additional property
 assertFalse(recordType2.isSubtype(recordType1));

 // Test that record type1 is a supertype of itself
 assertTrue(recordType1.isSubtype(recordType1));

 // Test that record type1 is a supertype of a record type with the same
 // properties but with inferred types
 JSTypeRegistry newRegistry = new JSTypeRegistry();
 RecordType recordType3 = new RecordType(newRegistry, Maps.newHashMap());
 recordType3.defineProperty("name", null, true, new Node());
 recordType3.defineProperty("age", null, true, new Node());
 assertTrue(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testRecordTypeLeastSupertype() {
 RecordProperty nameType = new RecordProperty(registry, "String");
 RecordProperty ageType = new RecordProperty(registry, "Number");
 RecordProperty idType = new RecordProperty(registry, "Number");
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", nameType.getType(), false, new Node());
 recordType1.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("name", nameType.getType(), false, new Node());
 recordType2.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("id", idType.getType(), false, new Node());
 recordType3.defineProperty("name", nameType.getType(), false, new Node());
 recordType3.defineProperty("age", ageType.getType(), false, new Node());
 recordType3.defineProperty("id", idType.getType(), false, new Node());
 recordType4.defineProperty("id", idType.getType(), false, new Node());

 // Test that the least supertype of record type1 and record type2 is a record
 // type with both "name" and "age" properties
 JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
 RecordType leastSupertypeRecordType = leastSupertype.toMaybeRecordType();
 assertEquals(2, leastSupertypeRecordType.getPropertyNames().length);
 assertTrue(leastSupertypeRecordType.hasProperty("name"));
 assertTrue(leastSupertypeRecordType.hasProperty("age"));

 // Test that the least supertype of record type1 and record type3 is a record
 // type with both "name", "age", and "id" properties
 RecordType property3RecordType = new RecordType(registry, Maps.newHashMap());
 property3RecordType.defineProperty("id", idType.getType(), false, new Node());
 leastSupertype = recordType1.getLeastSupertype(property3RecordType);
 leastSupertypeRecordType = leastSupertype.toMaybeRecordType();
 assertEquals(3, leastSupertypeRecordType.getPropertyNames().length);
 assertTrue(leastSupertypeRecordType.hasProperty("name"));
 assertTrue(leastSupertypeRecordType.hasProperty("age"));
 assertTrue(leastSupertypeRecordType.hasProperty("id"));

 // Test that the least supertype of record type1 and record type4 is record type1
 leastSupertype = recordType1.getLeastSupertype(recordType4);
 leastSupertypeRecordType = leastSupertype.toMaybeRecordType();
 assertEquals(2, leastSupertypeRecordType.getPropertyNames().length);
 assertTrue(leastSupertypeRecordType.isEquivalentTo(recordType1));
 }

 @Test
 public void testRecordTypeGreatestSubtype() {
 RecordProperty nameType = new RecordProperty(registry, "String");
 RecordProperty ageType = new RecordProperty(registry, "Number");
 RecordProperty idType = new RecordProperty(registry, "Number");
 RecordType recordType1 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 recordType1.defineProperty("name", nameType.getType(), false, new Node());
 recordType1.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("name", nameType.getType(), false, new Node());
 recordType2.defineProperty("age", ageType.getType(), false, new Node());
 recordType2.defineProperty("id", idType.getType(), false, new Node());
 recordType3.defineProperty("name", nameType.getType(), false, new Node());
 recordType3.defineProperty("age", ageType.getType(), false, new Node());
 recordType3.defineProperty("id", idType.getType(), false, new Node());
 recordType4.defineProperty("id", idType.getType(), false, new Node());
 recordType4.defineProperty("name", nameType.getType(), false, new Node());
 recordType4.defineProperty("age", ageType.getType(), false, new Node());

 // Test that the greatest subtype of record type1 and record type2 is a record
 // type with both "name" and "age" properties
 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 RecordType greatestSubtypeRecordType = greatestSubtype.toMaybeRecordType();
 assertEquals(2, greatestSubtypeRecordType.getPropertyNames().length);
 assertTrue(greatestSubtypeRecordType.hasProperty("name"));
 assertTrue(greatestSubtypeRecordType.hasProperty("age"));

 // Test that the greatest subtype of record type1 and record type3 is record type3
 greatestSubtype = recordType1.getGreatestSubtype(recordType3);
 greatestSubtypeRecordType = greatestSubtype.toMaybeRecordType();
 assertEquals(3, greatestSubtypeRecordType.getPropertyNames().length);
 assertTrue(greatestSubtypeRecordType.isEquivalentTo(recordType3));

 // Test that the greatest subtype of record type1 and record type4 is record type1
 greatestSubtype = recordType1.getGreatestSubtype(recordType4);
 greatestSubtypeRecordType = greatestSubtype.toMaybeRecordType();
 assertEquals(2, greatestSubtypeRecordType.getPropertyNames().length);
 assertTrue(greatestSubtypeRecordType.isEquivalentTo(recordType1));
 }
}