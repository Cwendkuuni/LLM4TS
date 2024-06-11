import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordTypeFrozen;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty = new RecordProperty("propertyKey", IntType.getInstance());
 recordType = new RecordType(registry, Map.of("propertyKey", recordProperty));
 recordType2 = new RecordType(registry, Map.of("propertyKey", recordProperty), false);
 recordTypeFrozen = new RecordType(registry, Map.of("propertyKey", recordProperty), true);
 }

 @Test
 public void testRecordTypeConstructor_Declared() {
 RecordType expectedRecordType = new RecordType(registry, Map.of("propertyKey", recordProperty));
 assertEquals(expectedRecordType.getClass(), recordType.getClass());
 }

 @Test
 public void testRecordTypeConstructor_UndDeclared() {
 RecordType expectedRecordType = new RecordType(registry, Map.of("propertyKey", recordProperty), false);
 assertEquals(expectedRecordType.getClass(), recordType2.getClass());
 }

 @Test
 public void testRecordTypeConstructor_Frozen() {
 RecordType expectedRecordType = new RecordType(registry, Map.of("propertyKey", recordProperty), true);
 assertEquals(expectedRecordType.getClass(), recordTypeFrozen.getClass());
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType2.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.STRICT));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_Frozen() {
 recordTypeFrozen.defineProperty("newProperty", StringType.getInstance(), false, Node.newNull());
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_NotDeclared() {
 recordType.defineProperty("newProperty", StringType.getInstance(), false, Node.newNull());
 }

 @Test
 public void testGetImplicitPrototype() {
 assertSame(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordTypeGreatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE));
 assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), recordTypeGreatestSubtype);

 JSType recordType2GreatestSubtype = recordType2.getGreatestSubtypeHelper(recordType);
 assertSame(recordType, recordType2GreatestSubtype);

 RecordType recordType3 = new RecordType(registry, Map.of("propertyKey1", IntType.getInstance()), Map.of("propertyKey2", StringType.getInstance()));
 JSType recordType3GreatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);
 assertSame(recordType, recordType3GreatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordType));
 assertFalse(recordType.isSubtype(new IntType(registry)));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.ARRAY_TYPE)));
 assertFalse(recordType2.isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_RecordTypeToRecordType() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtype_RecordTypeToOtherRecordType() {
 assertFalse(recordType.isSubtype(new RecordType(registry, Map.of("propertyKey1", IntType.getInstance()))));
 }

 @Test
 public void testIsSubtype_OtherRecordTypeToRecordType() {
 assertFalse(new RecordType(registry, Map.of("propertyKey1", IntType.getInstance())).isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_RecordTypeToObjectType() {
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 }

 @Test
 public void testIsSubtype_ObjectTypeToRecordType() {
 assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_RecordTypeToArrayType() {
 assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.ARRAY_TYPE)));
 }

 @Test
 public void testIsSubtype_ArrayTypeToRecordType() {
 assertFalse(registry.getNativeType(JSTypeNative.ARRAY_TYPE).isSubtype(recordType));
 }
}