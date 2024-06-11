import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordType registry = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 JSType recordType = registry.getType();
 assertEquals(JSType.IS_RECORD, recordType.isRecordType());
 }

 @Test
 public void testRecordTypeGetImplicitPrototype() {
 RecordType recordType = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(ObjectType.class, implicitPrototype.getClass());
 }

 @Test
 public void testRecordTypeDefineProperty() {
 RecordType recordType = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 recordType.defineProperty("gender", String.class, false, null);
 assertFalse(recordType.hasProperty("gender"));
 }

 @Test
 public void testRecordTypeFrozen() {
 RecordType recordType = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 recordType.defineProperty("gender", String.class, false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("gender", String.class, false, null);
 assertFalse(recordType.hasProperty("gender"));
 }

 @Test
 public void testRecordTypeCheckRecordEquivalenceHelper() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 record1.isFrozen = true;
 record2.isFrozen = true;
 assertTrue(record1.checkRecordEquivalenceHelper(record2, EquivalenceMethod.IDENTICAL));
 }

 @Test
 public void testRecordTypeGetGreatestSubtypeHelper() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class),
 "city", new RecordProperty("city", String.class)));
 ObjectType objType = new ObjectType(new JSTypeRegistry(), null);
 JSType record1Subtype = record1.getGreatestSubtypeHelper(record2);
 assertTrue(objType.equals(record1Subtype));

 RecordType record3 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class),
 "city", new RecordProperty("city", String.class),
 "country", new RecordProperty("country", String.class)));
 JSType record3Subtype = record1.getGreatestSubtypeHelper(record3);
 assertEquals(record1, record3Subtype);
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class),
 "city", new RecordProperty("city", String.class)));
 RecordType record3 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class),
 "city", new RecordProperty("city", Number.class)));
 assertTrue(record1.isSubtype(record2));
 assertFalse(record1.isSubtype(record3));
 }

 @Test
 public void testRecordTypeIsSubtypeWithInvariantProperties() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class),
 "city", new RecordProperty("city", Integer.class)));
 assertTrue(record1.isSubtype(record2));
 }

 @Test
 public void testRecordTypeIsSubtypeWithNonInvariantProperties() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Double.class),
 "city", new RecordProperty("city", Integer.class)));
 assertFalse(record1.isSubtype(record2));
 }

 @Test
 public void testRecordTypeIsSubtypeWithNullProperties() {
 RecordType record1 = new RecordType(new JSTypeRegistry(),
 Map.of("name", new RecordProperty("name", String.class),
 "age", new RecordProperty("age", Integer.class)));
 RecordType record2 = new RecordType(new JSTypeRegistry(),
 Map.of("name", null, "age", new RecordProperty("age", Integer.class)));
 assertFalse(record1.isSubtype(record2));
 }
}