import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static RecordType createRecordType(Map<String, RecordProperty> properties) {
 JSTypeRegistry registry = new JSTypeRegistry();
 return new RecordType(registry, properties, true);
 }

 @Test
 public void testConstructorWithDeclaredProperties() {
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("prop1", new RecordProperty(JSType.getIntType(), null));
 properties.put("prop2", new RecordProperty(JSType.getStringType(), null));
 RecordType recordType = createRecordType(properties);

 assertEquals(JSType.getRecordType(new String[]{"prop1", "prop2"},
 new JSType[]{JSType.getIntType(), JSType.getStringType()}), recordType);
 }

 @Test
 public void testConstructorWithSynthesizedProperties() {
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("prop1", new RecordProperty(JSType.getIntType(), null));
 RecordType recordType = createRecordType(properties, false);

 assertEquals(JSType.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("prop1", null);
 RecordType recordType = createRecordType(properties);
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 recordType.defineProperty("prop1", JSType.getIntType(), false, null);
 assertTrue(recordType.isSynthetic());

 recordType = new RecordType(new JSTypeRegistry(), new java.util.HashMap<>(), true);
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = createRecordType(new java.util.HashMap<>());
 RecordType recordType2 = createRecordType(new java.util.HashMap<>());
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));

 RecordType recordType3 = createRecordType(new java.util.HashMap<>());
 RecordProperty prop1 = new RecordProperty(JSType.getIntType(), null);
 RecordProperty prop2 = new RecordProperty(JSType.getStringType(), null);
 recordType3.defineProperty("prop1", prop1.getType(), false, null);
 recordType3.defineProperty("prop2", prop2.getType(), false, null);
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));

 RecordType recordType4 = createRecordType(new java.util.HashMap<>());
 recordType4.defineProperty("prop1", JSType.getIntType(), true, null);
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTICAL));

 recordType4 = createRecordType(new java.util.HashMap<>());
 recordType4.defineProperty("prop1", JSType.getIntType(), false, null);
 recordType4.defineProperty("prop2", JSType.getIntType(), false, null);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTICAL));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 assertFalse(recordType.hasProperty("prop1"));

 recordType.defineProperty("prop1", JSType.getIntType(), false, null);
 assertTrue(recordType.hasProperty("prop1"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 recordType.isFrozen = true;
 recordType.defineProperty("prop1", JSType.getIntType(), false, null);
 }

 @Test
 public void testGetPropertyType() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 recordType.defineProperty("prop1", JSType.getIntType(), false, null);
 assertEquals(JSType.getIntType(), recordType.getPropertyType("prop1"));
 }

 @Test
 public void testGetPropertyNode() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 recordType.defineProperty("prop1", JSType.getIntType(), false, null);
 assertNull(recordType.getPropertyNode("prop1"));
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = createRecordType(new java.util.HashMap<>());
 assertFalse(recordType.isFrozen());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = createRecordType(new java.util.HashMap<>());
 RecordType recordType2 = createRecordType(new java.util.HashMap<>());
 RecordType recordType3 = createRecordType(new java.util.HashMap<>());
 recordType3.defineProperty("prop1", JSType.getStringType(), false, null);
 RecordType recordType4 = createRecordType(new java.util.HashMap<>());
 recordType4.defineProperty("prop1", JSType.getIntType(), false, null);
 recordType4.defineProperty("prop2", JSType.getStringType(), false, null);

 assertEquals(JSType.getRecordType(new String[]{"prop1"}, new JSType[]{JSType.getStringType()}), recordType1.getGreatestSubtypeHelper(recordType2));
 assertEquals(JSType.getRecordType(new String[]{"prop1", "prop2"}, new JSType[]{JSType.getStringType(), JSType.getStringType()}), recordType1.getGreatestSubtypeHelper(recordType3));
 assertEquals(JSType.getRecordType(new String[]{"prop1", "prop2"}, new JSType[]{JSType.getIntType(), JSType.getStringType()}), recordType1.getGreatestSubtypeHelper(recordType4));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = createRecordType(new java.util.HashMap<>());
 RecordType recordType2 = createRecordType(new java.util.HashMap<>());
 RecordType recordType3 = createRecordType(new java.util.HashMap<>());
 recordType3.defineProperty("prop1", JSType.getIntType(), false, null);
 RecordType recordType4 = createRecordType(new java.util.HashMap<>());
 recordType4.defineProperty("prop1", JSType.getStringType(), false, null);

 assertTrue(recordType1.isSubtype(JSType.getRecordType(new String[]{}, new JSType[]{})));
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(JSType.getIntType()));
 assertFalse(recordType1.isSubtype(JSType.getStringType()));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 }
}