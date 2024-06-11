import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("prop1", registry.getStringType());
 RecordProperty property2 = new RecordProperty("prop2", registry.getNumberType());
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("prop1", property1);
 properties.put("prop2", property2);
 recordType = new RecordType(registry, properties, true);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, false);
 JSType recordType3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 // RecordType1 is a subtype of RecordType1
 assertTrue(recordType1.isSubtype(recordType1));

 // RecordType1 is a subtype of RecordType1 declared
 assertTrue(recordType1.isSubtype(recordType1.toMaybeRecordType()));

 // RecordType1 is a subtype of RecordType2
 assertTrue(recordType1.isSubtype(recordType2));

 // RecordType1 is a subtype of ObjectType
 assertTrue(recordType1.isSubtype(recordType3));

 // ObjectType is a subtype of RecordType1
 assertTrue(recordType3.isSubtype(recordType1));

 // RecordType1 is not a subtype of RecordType3
 assertFalse(recordType1.isSubtype(new ObjectType(registry, null, null)));

 // RecordType1 is not a subtype of null
 assertFalse(recordType1.isSubtype(null));
 }

 @Test
 public void testGetProperty() {
 assertNull(recordType.getProperty("nonExistentProperty"));

 String prop1 = "prop1";
 JSType prop1Type = recordType.getPropertyType(prop1);
 assertEquals(registry.getStringType(), prop1Type);
 }

 @Test
 public void testDefineProperty() {
 String prop3 = "prop3";
 JSType prop3Type = registry.getNumberType();
 Node prop3Node = Node.NUL;
 recordType.defineProperty(prop3, prop3Type, false, prop3Node);

 assertFalse(recordType.hasProperty(prop3));

 recordType.defineProperty(prop3, prop3Type, true, prop3Node);

 assertTrue(recordType.hasProperty(prop3));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordType() {
 RecordType recordType = null;
 String prop = "prop";
 JSType propType = registry.getStringType();
 Node propNode = Node.NUL;
 recordType.defineProperty(prop, propType, false, propNode);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyType() {
 RecordType recordType = new RecordType(registry, properties, true);
 String prop = "prop";
 Node propNode = Node.NUL;
 recordType.defineProperty(prop, null, false, propNode);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyNode() {
 RecordType recordType = new RecordType(registry, properties, true);
 String prop = "prop";
 JSType propType = registry.getStringType();
 recordType.defineProperty(prop, propType, false, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyTwice() {
 RecordType recordType = new RecordType(registry, properties, true);
 String prop = "prop";
 JSType propType = registry.getStringType();
 Node propNode = Node.NUL;
 recordType.defineProperty(prop, propType, false, propNode);
 recordType.defineProperty(prop, propType, false, propNode);
 }

 @Test
 public void testGreatestSubtype() {
 RecordType recordType1 = new RecordType(registry, properties, true);
 RecordType recordType2 = new RecordType(registry, properties, false);
 RecordType mergedType = new RecordType(registry, properties, true);

 // RecordType1 is a subtype of MergedType
 assertTrue(recordType1.isSubtype(mergedType));

 // MergedType is a subtype of RecordType2
 assertTrue(mergedType.isSubtype(recordType2));

 // RecordType1 is not a subtype of RecordType2
 assertFalse(recordType1.isSubtype(recordType2));

 // RecordType1 is not a subtype of itself
 assertFalse(recordType1.isSubtype(recordType1));

 // MergedType is the greatest subtype of RecordType1 and RecordType2
 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals(mergedType, greatestSubtype);
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
 }
}