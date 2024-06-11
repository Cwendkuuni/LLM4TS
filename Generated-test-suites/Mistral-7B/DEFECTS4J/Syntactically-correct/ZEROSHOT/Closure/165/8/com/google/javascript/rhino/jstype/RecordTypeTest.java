import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.test.JSTestCase;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest extends JSTestCase {

 private static JSTypeRegistry registry;
 private static RecordType intRecord;
 private static RecordType stringRecord;
 private static RecordType intStringRecord;

 @BeforeClass
 public static void setUpClass() {
 registry = new JSTypeRegistry();

 RecordProperty intProp = new RecordProperty("intProp", JSType.getIntType());
 RecordProperty stringProp = new RecordProperty("stringProp", JSType.getStringType());

 intRecord = new RecordType(registry, Map.of("intProp", intProp));
 stringRecord = new RecordType(registry, Map.of("stringProp", stringProp));
 }

 @Test
 public void testEquals() {
 RecordType record1 = intRecord;
 RecordType record2 = intRecord;
 RecordType record3 = new RecordType(registry, Map.of("intProp", JSType.getIntType()));

 assertEquals(record1, record2);
 assertEquals(record1.hashCode(), record2.hashCode());

 assertNotEquals(record1, record3);
 assertNotEquals(record3.hashCode(), record1.hashCode());
 }

 @Test
 public void testIsSubtype() {
 RecordType intSubType = new RecordType(registry, Map.of("intProp", JSType.getIntType()));
 RecordType stringSubType = new RecordType(registry, Map.of("stringProp", JSType.getStringType()));
 RecordType intStringSubType = new RecordType(registry, Map.of("intProp", JSType.getIntType(), "stringProp", JSType.getStringType()));

 assertTrue(intRecord.isSubtype(intSubType));
 assertTrue(intSubType.isSubtype(intRecord));

 assertTrue(stringRecord.isSubtype(intStringRecord));
 assertTrue(intStringRecord.isSubtype(stringRecord));

 assertFalse(intRecord.isSubtype(stringRecord));
 assertFalse(stringRecord.isSubtype(intRecord));

 assertFalse(intRecord.isSubtype(intStringRecord));
 assertFalse(intStringRecord.isSubtype(intRecord));
 }

 @Test
 public void testIsSubtypeWithUnknownTypes() {
 RecordType intRecordWithUnknown = new RecordType(registry, Map.of("intProp", JSType.getUnknownType()));
 RecordType stringRecordWithUnknown = new RecordType(registry, Map.of("stringProp", JSType.getUnknownType()));
 RecordType intStringRecordWithUnknown = new RecordType(registry, Map.of("intProp", JSType.getUnknownType(), "stringProp", JSType.getUnknownType()));

 assertFalse(intRecord.isSubtype(intRecordWithUnknown));
 assertFalse(intRecordWithUnknown.isSubtype(intRecord));

 assertFalse(stringRecord.isSubtype(intStringRecordWithUnknown));
 assertFalse(intStringRecordWithUnknown.isSubtype(stringRecord));

 assertTrue(intRecordWithUnknown.isSubtype(intRecordWithUnknown));
 assertTrue(stringRecordWithUnknown.isSubtype(stringRecordWithUnknown));

 assertTrue(intRecordWithUnknown.isSubtype(intRecord));
 assertTrue(intRecord.isSubtype(intRecordWithUnknown));

 assertTrue(stringRecordWithUnknown.isSubtype(stringRecord));
 assertTrue(stringRecord.isSubtype(stringRecordWithUnknown));

 assertTrue(intStringRecordWithUnknown.isSubtype(intStringRecordWithUnknown));
 assertTrue(intStringRecord.isSubtype(intStringRecordWithUnknown));
 }

 @Test
 public void testGetGreatestSubtype() {
 RecordType intRecord = new RecordType(registry, Map.of("intProp", JSType.getIntType()));
 RecordType stringRecord = new RecordType(registry, Map.of("stringProp", JSType.getStringType()));
 RecordType intStringRecord = new RecordType(registry, Map.of("intProp", JSType.getIntType(), "stringProp", JSType.getStringType()));
 RecordType intStringUnion = new RecordType(registry, Map.of(
 "intProp", JSType.getIntType(), "stringProp", new UnionType(registry, Set.of(JSType.getIntType(), JSType.getStringType()))));

 assertEquals(intRecord, intRecord.getGreatestSubtype(intRecord));
 assertEquals(intRecord, intRecord.getGreatestSubtype(intSubType));
 assertEquals(intRecord, intRecord.getGreatestSubtype(intStringRecord));
 assertEquals(intRecord, intRecord.getGreatestSubtype(intStringUnion));

 assertEquals(intSubType, intSubType.getGreatestSubtype(intRecord));
 assertEquals(intSubType, intSubType.getGreatestSubtype(intSubType));
 assertEquals(intSubType, intSubType.getGreatestSubtype(intStringRecord));
 assertEquals(intSubType, intSubType.getGreatestSubtype(intStringUnion));

 assertEquals(intStringRecord, intStringRecord.getGreatestSubtype(intRecord));
 assertEquals(intStringRecord, intStringRecord.getGreatestSubtype(intSubType));
 assertEquals(intStringRecord, intStringRecord.getGreatestSubtype(intStringRecord));
 assertEquals(intStringRecord, intStringRecord.getGreatestSubtype(intStringUnion));

 assertEquals(intStringUnion, intStringUnion.getGreatestSubtype(intRecord));
 assertEquals(intStringUnion, intStringUnion.getGreatestSubtype(intSubType));
 assertEquals(intStringUnion, intStringUnion.getGreatestSubtype(intStringRecord));
 assertEquals(intRecord, intStringUnion.getGreatestSubtype(intRecord));
 assertEquals(intSubType, intStringUnion.getGreatestSubtype(intSubType));
 assertEquals(intStringRecord, intStringUnion.getGreatestSubtype(intStringRecord));
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType record1 = intRecord;
 RecordType record2 = intRecord;
 RecordType record3 = new RecordType(registry, Map.of("intProp", JSType.getIntType()));
 RecordType record4 = new RecordType(registry, Map.of("stringProp", JSType.getStringType()));
 RecordType record5 = new RecordType(registry, Map.of("intProp", JSType.getIntType(), "stringProp", JSType.getStringType()));

 assertTrue(record1.isEquivalentTo(record2));
 assertTrue(record1.isEquivalentTo(intRecord));

 assertFalse(record1.isEquivalentTo(record3));
 assertFalse(record3.isEquivalentTo(record1));

 assertFalse(record1.isEquivalentTo(record4));
 assertFalse(record4.isEquivalentTo(record1));

 assertTrue(record3.isEquivalentTo(record3));
 assertTrue(record4.isEquivalentTo(record4));

 assertTrue(record5.isEquivalentTo(record5));

 assertTrue(record1.isEquivalentTo(intRecord));
 assertTrue(intRecord.isEquivalentTo(record1));

 assertTrue(record3.isEquivalentTo(new RecordType(registry, Map.of("intProp", JSType.getIntType()))));
 assertTrue(record3.isEquivalentTo(new RecordType(registry, Map.of("intProp", JSType.getUnknownType()))));
 assertTrue(record3.isEquivalentTo(new RecordType(registry, Map.of("intProp", JSType.getIntType(), "stringProp", JSType.getUnknownType()))));

 assertTrue(record5.isEquivalentTo(new RecordType(registry, Map.of("intProp", JSType.getIntType(), "stringProp", JSType.getStringType()))));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType record = new RecordType(registry, Map.of("intProp", JSType.getIntType()));
 assertEquals(JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), record.getImplicitPrototype());
 }

 @Test
 public void testToString() {
 RecordType record = new RecordType(registry, Map.of("intProp", JSType.getIntType()));
 assertEquals("{intProp: int}", record.toString());
 }

 @Test
 public void testPropertyAccess() {
 RecordType record = intRecord;
 RecordProperty intProp = record.getProperty("intProp");
 assertNotNull(intProp);
 assertEquals(JSType.getIntType(), intProp.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testPropertyAccessUnknown() {
 RecordType record = intRecord;
 record.getProperty("unknownProperty");
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testDefineProperty() {
 RecordType record = new RecordType(registry, Map.of());
 record.defineProperty("intProp", JSType.getIntType(), false, Node.NONE);
 assertEquals(JSType.getIntType(), record.getPropertyType("intProp"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDefinePropertyFrozen() {
 RecordType record = intRecord;
 try {
 record.defineProperty("intProp", JSType.getIntType(), false, Node.NONE);
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Cannot define property 'intProp' on a frozen RecordType.", e.getMessage());
 }
 }

 @Test
 public void testHasProperty() {
 RecordType record = intRecord;
 assertTrue(record.hasProperty("intProp"));
 assertFalse(record.hasProperty("unknownProperty"));
 }

 @Test
 public void testGetPropertyNode() {
 RecordType record = intRecord;
 Node node = record.getPropertyNode("intProp");
 assertNotNull(node);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetPropertyNodeUnknown() {
 RecordType record = intRecord;
 try {
 record.getPropertyNode("unknownProperty");
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Property 'unknownProperty' not found in RecordType.", e.getMessage());
 }
 }
}