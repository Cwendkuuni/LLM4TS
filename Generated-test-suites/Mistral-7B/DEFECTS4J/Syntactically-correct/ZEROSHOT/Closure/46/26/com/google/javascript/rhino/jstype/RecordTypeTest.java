import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

 private static JSTypeRegistry registry;
 private static RecordType recordTypeA;
 private static RecordType recordTypeB;
 private static RecordType recordTypeC;
 private static RecordType recordTypeD;
 private static RecordType recordTypeEmpty;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 recordTypeA = new RecordType(registry, Maps.newHashMap());
 recordTypeA.defineProperty("propA", JSType.STRING_TYPE, false, null);
 recordTypeB = new RecordType(registry, Maps.newHashMap());
 recordTypeB.defineProperty("propB", JSType.NUMBER_TYPE, false, null);
 recordTypeC = new RecordType(registry, Maps.newHashMap());
 recordTypeC.defineProperty("propA", JSType.STRING_TYPE, false, null);
 recordTypeC.defineProperty("propB", JSType.NUMBER_TYPE, false, null);
 recordTypeD = new RecordType(registry, Maps.newHashMap());
 recordTypeD.defineProperty("propA", JSType.STRING_TYPE, false, null);
 recordTypeD.defineProperty("propB", recordTypeA, false, null);
 recordTypeEmpty = new RecordType(registry, Maps.newHashMap());
 }

 @Test
 public void testIsSubtype() {
 // Test with empty record type
 assertEquals(recordTypeEmpty, recordTypeEmpty.toMaybeRecordType());
 assertTrue(recordTypeEmpty.isSubtype(recordTypeEmpty));
 assertTrue(recordTypeEmpty.isSubtype(JSType.OBJECT_TYPE));
 assertFalse(recordTypeEmpty.isSubtype(recordTypeA));

 // Test with record type A
 assertEquals(recordTypeA, recordTypeA.toMaybeRecordType());
 assertTrue(recordTypeA.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(JSType.OBJECT_TYPE));
 assertTrue(recordTypeA.isSubtype(recordTypeC));
 assertFalse(recordTypeA.isSubtype(recordTypeB));

 // Test with record type B
 assertEquals(recordTypeB, recordTypeB.toMaybeRecordType());
 assertTrue(recordTypeB.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(JSType.OBJECT_TYPE));
 assertFalse(recordTypeB.isSubtype(recordTypeA));
 assertFalse(recordTypeB.isSubtype(recordTypeC));

 // Test with record type C
 assertEquals(recordTypeC, recordTypeC.toMaybeRecordType());
 assertTrue(recordTypeC.isSubtype(recordTypeC));
 assertTrue(recordTypeC.isSubtype(JSType.OBJECT_TYPE));
 assertTrue(recordTypeC.isSubtype(recordTypeA));
 assertTrue(recordTypeC.isSubtype(recordTypeC));

 // Test with record type D
 assertEquals(recordTypeD, recordTypeD.toMaybeRecordType());
 assertTrue(recordTypeD.isSubtype(recordTypeD));
 assertTrue(recordTypeD.isSubtype(JSType.OBJECT_TYPE));
 assertTrue(recordTypeD.isSubtype(recordTypeA));
 assertFalse(recordTypeD.isSubtype(recordTypeB));
 }

 @Test
 public void testIsEquivalentTo() {
 // Test with empty record types
 assertTrue(recordTypeEmpty.isEquivalentTo(recordTypeEmpty));

 // Test with equivalent record types
 assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
 assertTrue(recordTypeA.isEquivalentTo(recordTypeC));

 // Test with non-equivalent record types
 assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
 assertFalse(recordTypeB.isEquivalentTo(recordTypeA));
 assertFalse(recordTypeA.isEquivalentTo(recordTypeD));
 }

 @Test
 public void testGetLeastSupertype() {
 // Test with empty record types
 assertEquals(recordTypeEmpty, recordTypeEmpty.getLeastSupertype(recordTypeEmpty));
 assertEquals(JSType.OBJECT_TYPE, recordTypeEmpty.getLeastSupertype(JSType.OBJECT_TYPE));
 assertNull(recordTypeEmpty.getLeastSupertype(recordTypeA));

 // Test with record type A
 assertEquals(recordTypeA, recordTypeA.getLeastSupertype(recordTypeA));
 assertEquals(JSType.OBJECT_TYPE, recordTypeA.getLeastSupertype(JSType.OBJECT_TYPE));
 assertEquals(recordTypeC, recordTypeA.getLeastSupertype(recordTypeC));
 assertNull(recordTypeA.getLeastSupertype(recordTypeB));

 // Test with record type B
 assertEquals(recordTypeB, recordTypeB.getLeastSupertype(recordTypeB));
 assertEquals(JSType.OBJECT_TYPE, recordTypeB.getLeastSupertype(JSType.OBJECT_TYPE));
 assertNull(recordTypeB.getLeastSupertype(recordTypeA));
 assertNull(recordTypeB.getLeastSupertype(recordTypeC));

 // Test with record type C
 assertEquals(recordTypeC, recordTypeC.getLeastSupertype(recordTypeC));
 assertEquals(JSType.OBJECT_TYPE, recordTypeC.getLeastSupertype(JSType.OBJECT_TYPE));
 assertEquals(recordTypeC, recordTypeC.getLeastSupertype(recordTypeA));
 assertEquals(recordTypeC, recordTypeC.getLeastSupertype(recordTypeC));

 // Test with record type D
 assertEquals(recordTypeD, recordTypeD.getLeastSupertype(recordTypeD));
 assertEquals(JSType.OBJECT_TYPE, recordTypeD.getLeastSupertype(JSType.OBJECT_TYPE));
 assertEquals(recordTypeA, recordTypeD.getLeastSupertype(recordTypeA));
 assertNull(recordTypeD.getLeastSupertype(recordTypeB));
 }

 @Test
 public void testDefineProperty() {
 // Test defining property with frozen record type
 recordTypeA.freeze();
 assertFalse(recordTypeA.defineProperty("propX", JSType.STRING_TYPE, false, null));

 // Test defining property with non-frozen record type
 assertEquals(recordTypeA, recordTypeA.toMaybeRecordType());
 recordTypeA.defineProperty("propX", JSType.STRING_TYPE, false, null);
 assertEquals(recordTypeA, recordTypeA.toMaybeRecordType());
 assertNotNull(recordTypeA.getPropertyType("propX"));
 }
}