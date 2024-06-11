import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @BeforeClass
 public static void setupClass() {
 JSTypeNative.initNativeTypes();
 }

 @Before
 public void setupTest() {
 registry = new JSTypeRegistry();
 recordType = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));
 RecordType recordType2 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL)
 ));
 RecordType recordType3 = new RecordType(registry, Map.of(
 "b", new RecordProperty("b", STRING),
 "a", new RecordProperty("a", INT)
 ));

 RecordType recordType4 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", INT)
 ));

 RecordType recordType5 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL),
 "d", new RecordProperty("d", INT)
 ));

 RecordType recordType6 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL),
 "d", new RecordProperty("d", STRING)
 ));

 // Record types are subtypes of themselves
 assertTrue(recordType.isSubtype(recordType));
 // Record types are subtypes of Object
 assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 // Record types are not subtypes of non-record types
 assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.VOID_TYPE)));
 // Record types with the same properties and same types are subtypes of each other
 assertTrue(recordType1.isSubtype(recordType2));
 // Record types with the same properties but different types are not subtypes of each other
 assertFalse(recordType1.isSubtype(recordType3));
 // Record types with different properties are not subtypes of each other
 assertFalse(recordType1.isSubtype(recordType4));
 // Record types with more properties are not subtypes of record types with fewer properties
 assertFalse(recordType5.isSubtype(recordType1));
 // Record types with same properties but different order are not subtypes of each other
 assertFalse(recordType6.isSubtype(recordType1));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));
 RecordType recordType2 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL)
 ));
 RecordType recordType3 = new RecordType(registry, Map.of(
 "b", new RecordProperty("b", STRING),
 "a", new RecordProperty("a", INT)
 ));
 RecordType recordType4 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", INT)
 ));
 RecordType recordType5 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "d", new RecordProperty("d", INT)
 ));
 RecordType recordType6 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL),
 "d", new RecordProperty("d", STRING)
 ));
 RecordType recordType7 = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING),
 "c", new RecordProperty("c", BOOL),
 "e", new RecordProperty("e", INT)
 ));

 JSType greatestSubtype12 = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, greatestSubtype12);

 JSType greatestSubtype31 = recordType3.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype31);

 JSType greatestSubtype41 = recordType4.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype41);

 JSType greatestSubtype51 = recordType5.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype51);

 JSType greatestSubtype61 = recordType6.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype61);

 JSType greatestSubtype71 = recordType7.getGreatestSubtypeHelper(recordType1);
 assertEquals(recordType1, greatestSubtype71);

 JSType greatestSubtype14 = recordType1.getGreatestSubtypeHelper(recordType4);
 assertEquals(recordType4, greatestSubtype14);

 JSType greatestSubtype15 = recordType1.getGreatestSubtypeHelper(recordType5);
 assertEquals(recordType4, greatestSubtype15);

 JSType greatestSubtype16 = recordType1.getGreatestSubtypeHelper(recordType6);
 assertEquals(recordType4, greatestSubtype16);

 JSType greatestSubtype17 = recordType1.getGreatestSubtypeHelper(recordType7);
 assertEquals(recordType4, greatestSubtype17);
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));

 assertFalse(recordType.hasProperty("c"));

 recordType.defineProperty("c", BOOL, false, null);
 assertTrue(recordType.hasProperty("c"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 RecordType recordType = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));

 // Should throw an exception
 recordType.defineProperty("c", null, false, null);
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));

 assertFalse(recordType.isFrozen());

 recordType.freeze();
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyAfterFreeze() {
 RecordType recordType = new RecordType(registry, Map.of(
 "a", new RecordProperty("a", INT),
 "b", new RecordProperty("b", STRING)
 ));

 recordType.freeze();

 // Should throw an exception
 recordType.defineProperty("c", BOOL, false, null);
 }
}