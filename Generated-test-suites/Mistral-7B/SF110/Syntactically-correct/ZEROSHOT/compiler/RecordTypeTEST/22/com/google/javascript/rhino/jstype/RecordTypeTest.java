import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty property = new RecordProperty("property", String.class);
 RecordProperty property2 = new RecordProperty("property2", Integer.class);
 Map<String, RecordProperty> properties = Map.of("property", property, "property2", property2);
 recordType = new RecordType(registry, properties, true);
 recordType2 = new RecordType(registry, properties, false);
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType2.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testGetImplicitPrototype() {
 JSType prototype = recordType.getImplicitPrototype();
 assertEquals(JSType.OBJECT_TYPE, prototype);
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.SUBTYPE));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.SUBTYPE));
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("property3", String.class, false, Node.UNDEFINED));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyNode() {
 recordType.defineProperty("property", String.class, false, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithTypeNull() {
 recordType.defineProperty("property", null, false, Node.UNDEFINED);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(JSType.OBJECT_TYPE);
 assertEquals(JSType.OBJECT_TYPE, greatestSubtype);

 JSType typeB = registry.voidType();
 JSType greatestSubtypeTypeB = recordType.getGreatestSubtypeHelper(typeB);
 assertEquals(JSType.NO_OBJECT_TYPE, greatestSubtypeTypeB);

 JSType recordTypeB = new RecordType(registry, properties, true);
 JSType greatestSubtypeRecordTypeB = recordType.getGreatestSubtypeHelper(recordTypeB);
 assertEquals(recordTypeB, greatestSubtypeRecordTypeB);
 }

 @Test
 public void testIsSubtype() {
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objectType.isSubtype(recordType));

 JSType intType = registry.getIntType();
 JSType recordTypeInt = new RecordType(registry, properties, true);
 recordTypeInt = recordTypeInt.setInstanceType(intType);
 assertFalse(recordType.isSubtype(recordTypeInt));

 JSType recordTypeInt2 = new RecordType(registry, properties, false);
 recordTypeInt2 = recordTypeInt2.setInstanceType(intType);
 assertTrue(recordType.isSubtype(recordTypeInt2));
 }

 @Test
 public void testIsSubtypeWithNullType() {
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeWithNullRecordType() {
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeWithSelf() {
 assertTrue(recordType.isSubtype(recordType));
 }
}