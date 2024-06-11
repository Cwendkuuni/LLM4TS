import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 properties.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{ a: Int, b: String }", recordType.toString());
 }

 @Test
 public void testRecordTypeEquals() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
 propertiesA.put("b", new RecordProperty("b", registry.getStringType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
 propertiesB.put("c", new RecordProperty("c", registry.getStringType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("b", new RecordProperty("b", registry.getStringType()));
 propertiesB.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 assertTrue(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testRecordTypeIsSubtypeFailure() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> propertiesA = new Maps.HashMap<>();
 propertiesA.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = new Maps.HashMap<>();
 propertiesB.put("b", new RecordProperty("b", registry.getIntType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 assertFalse(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testRecordTypeToMaybeRecordType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(recordType, recordType.toMaybeRecordType());
 }

 @Test
 public void testRecordTypeGetImplicitPrototype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 ObjectType implicitPrototype = recordType.getImplicitPrototype();

 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getName());
 }

 @Test
 public void testRecordTypeDefineProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 assertFalse(recordType.hasProperty("b"));

 recordType.defineProperty("b", registry.getStringType(), false, null);
 assertTrue(recordType.hasProperty("b"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeDefinePropertyFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();
 recordType.defineProperty("b", registry.getStringType(), false, null);
 }

 @Test
 public void testRecordTypeFreeze() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 recordType.freeze();
 assertFalse(recordType.defineProperty("b", registry.getStringType(), false, null));
 }

 @Test
 public void testRecordTypeGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 RecordType typeA = new RecordType(registry, Maps.newHashMap());
 RecordType typeB =
 new RecordType(registry, Maps.<String, RecordProperty>of("a", registry.getIntType()));
 RecordType typeC =
 new RecordType(registry, Maps.<String, RecordProperty>of("a", registry.getStringType()));
 RecordType typeD = new RecordType(registry, Maps.<String, RecordProperty>of("a", registry.getIntType(), "b", registry.getStringType()));

 JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);
 assertEquals(typeB, greatestSubtype);

 greatestSubtype = typeA.getGreatestSubtypeHelper(typeC);
 assertEquals(typeA, greatestSubtype);

 greatestSubtype = typeA.getGreatestSubtypeHelper(typeD);
 assertEquals(typeD, greatestSubtype);
 }

 @Test
 public void testRecordTypeHasProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 assertTrue(recordType.hasProperty("a"));
 assertFalse(recordType.hasProperty("b"));
 }

 @Test
 public void testRecordTypeGetPropertyType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(registry.getIntType(), recordType.getPropertyType("a"));
 assertNull(recordType.getPropertyType("b"));
 }

 @Test
 public void testRecordTypeGetPropertyNode() {
 RecordTypeRegistry registry = new RecordTypeRegistry();
 Map<String, RecordProperty> properties = new Maps.HashMap<>();
 properties.put("a", new RecordProperty("a", registry.getIntType()));
 RecordType recordType = new RecordType(registry, properties);

 Node propertyNode = recordType.getPropertyNode("a");
 assertNotNull(propertyNode);
 }
}