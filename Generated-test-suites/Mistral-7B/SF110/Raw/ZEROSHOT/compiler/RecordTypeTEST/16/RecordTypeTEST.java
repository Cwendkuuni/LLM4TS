import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;
 private RecordType frozenRecordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();

 RecordProperty prop1 = new RecordProperty("prop1", INT);
 RecordProperty prop2 = new RecordProperty("prop2", STRING);
 RecordProperty prop3 = new RecordProperty("prop3", BOOL);

 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 properties.put("prop3", prop3);

 recordType = new RecordType(registry, properties, true);
 recordType2 = new RecordType(registry, properties, false);
 recordType3 = new RecordType(registry, properties, true);
 frozenRecordType = new RecordType(registry, properties, true);
 frozenRecordType.freeze();
 }

 @Test
 public void testIsSynthetic() {
 assertEquals(false, recordType.isSynthetic());
 assertEquals(true, recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertEquals(true, recordType.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.DEEP));
 assertEquals(false, recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.DEEP));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 try {
 recordType.defineProperty("prop4", INT, false, null);
 fail("Expected defineProperty to fail when record is frozen.");
 } catch (IllegalStateException e) {
 // expected
 }

 try {
 recordType3.defineProperty("prop4", INT, false, null);
 assertEquals(INT, recordType3.getPropertyType("prop4"));
 } catch (IllegalStateException e) {
 fail("Expected defineProperty to succeed when record is not frozen.");
 }
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 assertEquals(OBJECT_TYPE, recordType.getGreatestSubtypeHelper(OBJECT_TYPE));

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 unionTypeBuilder.addAlternate(objType);

 RecordType recordType4 = new RecordType(registry, properties, true);
 unionTypeBuilder.addAlternate(recordType4);
 JSType greatestSubtype = unionTypeBuilder.build();

 assertEquals(recordType.getGreatestSubtypeHelper(recordType), recordType);
 assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType3));
 assertEquals(recordType3, recordType3.getGreatestSubtypeHelper(recordType));
 assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordType4));
 assertEquals(recordType4, recordType4.getGreatestSubtypeHelper(recordType));
 assertEquals(recordType, greatestSubtype);
 assertEquals(recordType, greatestSubtype.getGreatestSubtypeHelper(recordType));
 assertEquals(recordType, greatestSubtype.getGreatestSubtypeHelper(recordType3));
 assertEquals(recordType3, greatestSubtype.getGreatestSubtypeHelper(recordType4));
 assertEquals(recordType4, greatestSubtype.getGreatestSubtypeHelper(recordType));
 }

 @Test
 public void testIsSubtype() {
 // All records are subtypes of ObjectType
 assertEquals(true, recordType.isSubtype(OBJECT_TYPE));
 assertEquals(true, recordType2.isSubtype(OBJECT_TYPE));
 assertEquals(true, recordType3.isSubtype(OBJECT_TYPE));
 assertEquals(true, frozenRecordType.isSubtype(OBJECT_TYPE));

 // RecordType is not a subtype of NonRecordType
 assertFalse(recordType.isSubtype(INT));
 assertFalse(recordType2.isSubtype(INT));
 assertFalse(recordType3.isSubtype(INT));
 assertFalse(frozenRecordType.isSubtype(INT));

 // RecordType is a subtype of itself
 assertEquals(true, recordType.isSubtype(recordType));
 assertEquals(true, recordType2.isSubtype(recordType2));
 assertEquals(true, recordType3.isSubtype(recordType3));
 assertEquals(true, frozenRecordType.isSubtype(frozenRecordType));

 // RecordType1 is a subtype of RecordType2 if they have the same properties
 // with the same types
 assertEquals(true, recordType.isSubtype(recordType3));
 assertEquals(true, recordType3.isSubtype(recordType));

 // RecordType1 is not a subtype of RecordType2 if they have different properties
 // or if the types of corresponding properties are not invariant
 assertFalse(recordType.isSubtype(new RecordType(registry, properties, true, Arrays.asList("prop4", INT.getInstance()))));
 assertFalse(recordType3.isSubtype(new RecordType(registry, properties, true, Arrays.asList("prop4", STRING.getInstance()))));
 assertFalse(recordType.isSubtype(new RecordType(registry, properties, true, Arrays.asList("prop5", INT.getInstance()))));
 assertFalse(recordType3.isSubtype(new RecordType(registry, properties, true, Arrays.asList("prop5", STRING.getInstance()))));
 }

 @Test
 public void testIsSubtypeRecordTypeToNonRecordType() {
 // A record type is not a subtype of a non-record type
 assertFalse(recordType.isSubtype(INT));
 assertFalse(recordType2.isSubtype(INT));
 assertFalse(recordType3.isSubtype(INT));
 assertFalse(frozenRecordType.isSubtype(INT));
 }

 @Test
 public void testIsSubtypeRecordTypeToRecordType() {
 // RecordType1 is a subtype of RecordType2 if they have the same properties
 // with the same types
 assertEquals(true, recordType.isSubtype(recordType3));
 assertEquals(true, recordType3.isSubtype(recordType));

 // RecordType1 is not a subtype of RecordType2 if they have different properties
 // or if the types of corresponding properties are not invariant
 assertFalse(recordType.isSubtype(new RecordType(registry, Arrays.asList("prop4", INT.getInstance()), true)));
 assertFalse(recordType3.isSubtype(new RecordType(registry, Arrays.asList("prop4", STRING.getInstance()), true)));
 assertFalse(recordType.isSubtype(new RecordType(registry, Arrays.asList("prop5", INT.getInstance()), true)));
 assertFalse(recordType3.isSubtype(new RecordType(registry, Arrays.asList("prop5", STRING.getInstance()), true)));
 }
}