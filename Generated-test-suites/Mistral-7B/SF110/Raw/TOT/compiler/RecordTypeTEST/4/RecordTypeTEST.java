import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Number.class));
 RecordType recordType = new RecordType(registry, properties, true);

 assertEquals("RecordType", recordType.getClassName());
 assertEquals(JSType.OBJECT, recordType.getInterfaceTypes().get(0));

 String name = "John Doe";
 int age = 30;
 Node nameNode = registry.newTextNode(name);
 Node ageNode = registry.newIntNode(age);
 JSType nameType = registry.getRecordType(JSTypeNative.OBJECT_TYPE,
 new RecordProperty[] { new RecordProperty("name", String.class),
 new RecordProperty("age", Number.class) });

 recordType.defineProperty("name", nameType, false, nameNode);
 recordType.defineProperty("age", Number.class, false, ageNode);

 Object record = recordType.newInstance();
 assertEquals(name, record.get("name"));
 assertEquals(age, record.get("age"));
 }

 @Test
 public void testRecordTypeFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Number.class));
 RecordType recordType = new RecordType(registry, properties, true);
 recordType.freeze();

 try {
 recordType.defineProperty("name", String.class, false, null);
 fail("should not be able to add property after freeze");
 } catch (UnsupportedOperationException e) {
 // expected
 }

 try {
 recordType.defineProperty("age", Double.class, false, null);
 fail("should not be able to change property type after freeze");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testRecordTypeEquivalence() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Number.class));
 RecordType typeA = new RecordType(registry, propertiesA, true);

 Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
 propertiesB.put("name", new RecordProperty("name", String.class));
 propertiesB.put("age", new RecordProperty("age", Number.class));
 RecordType typeB = new RecordType(registry, propertiesB, true);

 assertTrue(typeA.checkRecordEquivalenceHelper(typeB, EquivalenceMethod.STRUCTURAL));
 }

 @Test
 public void testRecordTypeNonEquivalence() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Number.class));
 RecordType typeA = new RecordType(registry, propertiesA, true);

 Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
 propertiesB.put("name", new RecordProperty("name", String.class));
 propertiesB.put("age", new RecordProperty("age", String.class));
 RecordType typeB = new RecordType(registry, propertiesB, true);

 assertFalse(typeA.checkRecordEquivalenceHelper(typeB, EquivalenceMethod.STRUCTURAL));
 }

 @Test
 public void testRecordTypeGetGreatestSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 RecordType typeA = registry.getRecordType(
 JSTypeNative.OBJECT_TYPE,
 new RecordProperty[] {
 new RecordProperty("name", String.class),
 new RecordProperty("age", Number.class)
 });
 RecordType typeB = registry.getRecordType(
 JSTypeNative.OBJECT_TYPE,
 new RecordProperty[] {
 new RecordProperty("name", String.class),
 new RecordProperty("age", Number.class),
 new RecordProperty("city", String.class)
 });

 JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);

 assertTrue(JSTypeNative.OBJECT_TYPE.isSubtype(greatestSubtype));

 greatestSubtype = typeB.getGreatestSubtypeHelper(typeA);

 assertTrue(typeA.isSubtype(greatestSubtype));
 }

 @Test
 public void testRecordTypeDifferentPropertyTypes() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
 propertiesA.put("name", new RecordProperty("name", String.class));
 propertiesA.put("age", new RecordProperty("age", Number.class));
 RecordType typeA = new RecordType(registry, propertiesA, true);

 Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
 propertiesB.put("name", new RecordProperty("name", Number.class));
 propertiesB.put("age", new RecordProperty("age", String.class));
 RecordType typeB = new RecordType(registry, propertiesB, true);

 assertFalse(typeA.isSubtype(typeB));
 assertFalse(typeB.isSubtype(typeA));
 }
}