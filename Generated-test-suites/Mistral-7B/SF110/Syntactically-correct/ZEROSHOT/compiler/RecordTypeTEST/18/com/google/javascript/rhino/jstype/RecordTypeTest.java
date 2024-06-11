import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeConstruction() {
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class, null));
 properties.put("age", new RecordProperty("age", Integer.class, null));
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals("RecordType", recordType.getClassName());
 assertEquals(Integer.TYPE, recordType.getPropertyType("age").getType());
 }

 @Test
 public void testRecordTypeConstructionWithFrozen() {
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class, null));
 properties.put("age", new RecordProperty("age", Integer.class, null));
 RecordType recordType = new RecordType(registry, properties, false);
 assertFalse(recordType.isFrozen());

 RecordType frozenRecordType = new RecordType(registry, properties, true);
 assertTrue(frozenRecordType.isFrozen());
 }

 @Test
 public void testIsSynthetic() {
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class, null));
 properties.put("age", new RecordProperty("age", Integer.class, null));
 RecordType recordType = new RecordType(registry, properties, false);
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 Map<String, RecordProperty> properties1 = new LinkedHashMap<>();
 properties1.put("name", new RecordProperty("name", String.class, null));
 properties1.put("age", new RecordProperty("age", Integer.class, null));
 RecordType recordType1 = new RecordType(registry, properties1, true);

 Map<String, RecordProperty> properties2 = new LinkedHashMap<>();
 properties2.put("name", new RecordProperty("name", String.class, null));
 properties2.put("age", new RecordProperty("age", Integer.class, null));
 RecordType recordType2 = new RecordType(registry, properties2, true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.CHAINED));

 properties2.put("city", new RecordProperty("city", String.class, null));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.CHAINED));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, null, true);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class, null));
 RecordType recordType = new RecordType(registry, properties, true);

 assertFalse(recordType.hasProperty("color"));

 recordType.defineProperty("color", Integer.TYPE, false, null);
 assertTrue(recordType.hasProperty("color"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDefineProperty_Frozen() {
 Map<String, RecordProperty> properties = new LinkedHashMap<>();
 properties.put("name", new RecordProperty("name", String.class, null));
 RecordType recordType = new RecordType(registry, properties, true);

 recordType.defineProperty("color", Integer.TYPE, false, null);
 fail("Should not be able to define property on a frozen record type");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordType(registry, null, true);
 RecordType recordType3 = new RecordType(registry, null, true);
 RecordType recordType4 = new RecordType(registry, null, true);

 recordType1 = (RecordType) recordType1.getPropertyType("foo").getGreatestSubtype(recordType2);
 assertNotNull(recordType1);

 recordType1 = (RecordType) recordType1.getPropertyType("bar").getGreatestSubtype(recordType3);
 assertNotNull(recordType1);

 recordType1 = (RecordType) recordType1.getPropertyType("baz").getGreatestSubtype(recordType4);
 assertNull(recordType1);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, null, true);
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType2 = new RecordType(registry, null, true);
 RecordType recordType3 = new RecordTypeBuilder(registry).buildRecordType("{ foo: int, bar: string }");

 assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertTrue(recordType1.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType2.isSubtype(recordType1));
 assertFalse(recordType2.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType3.isSubtype(recordType2));
 assertTrue(recordType3.isSubtype(objType));
 }

 @Test
 public void testIsSubtype_RecordToRecord() {
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordTypeBuilder(registry).buildRecordType("{ foo: int, bar: string }");

 assertTrue(recordType1.isSubtype(recordType2));

 recordType2 = new RecordTypeBuilder(registry).buildRecordType("{ foo: int, bar: string, baz: int }");
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtype_RecordToObject() {
 RecordType recordType = new RecordType(registry, null, true);
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(recordType.isSubtype(objType));
 }

 @Test
 public void testIsSubtype_ObjectToRecord() {
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordType recordType = new RecordType(registry, null, true);

 assertTrue(objType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_RecordToUnion() {
 RecordType recordType = new RecordType(registry, null, true);
 UnionType unionType = new UnionTypeBuilder(registry).addAlternate(recordType).build();

 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtype_UnionToRecord() {
 UnionType unionType = new UnionTypeBuilder(registry).addAlternate(new RecordType(registry, null, true)).build();
 RecordType recordType = new RecordType(registry, null, true);

 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtype_RecordToRecordWithInvariant() {
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordTypeBuilder(registry).buildRecordType("{ foo: int, bar: int }");

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtype_RecordToRecordWithNonInvariant() {
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordTypeBuilder(registry).buildRecordType("{ foo: int, bar: String }");

 assertFalse(recordType1.isSubtype(recordType2));
 }
}