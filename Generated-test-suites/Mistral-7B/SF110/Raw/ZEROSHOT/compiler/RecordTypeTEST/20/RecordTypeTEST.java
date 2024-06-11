import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", IntType.INSTANCE),
 "b", new RecordProperty("b", StringType.INSTANCE)));
 recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", IntType.INSTANCE),
 "b", new RecordProperty("b", StringType.INSTANCE), "c", new RecordProperty("c", BooleanType.INSTANCE)));
 }

 @Test
 public void testIsRecordType() {
 assertTrue(recordType.isRecordType());
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 assertFalse(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.TYPE));
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.INSTANCE));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.TYPE));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.INSTANCE));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType actual = recordType.getImplicitPrototype();
 assertEquals(expected, actual);
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("x", IntType.INSTANCE, false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullRecordProperty() {
 RecordProperty nullRecordProperty = null;
 recordType.defineProperty("x", IntType.INSTANCE, false, nullRecordProperty);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType expected = registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE);
 JSType actual = recordType.getGreatestSubtypeHelper(registry.getNullType());
 assertEquals(expected, actual);

 JSType recordTypeSubtype = new RecordType(registry, Map.of("a", new RecordProperty("a", IntType.INSTANCE),
 "b", new RecordProperty("b", StringType.INSTANCE), "c", new RecordProperty("c", BooleanType.INSTANCE)));
 JSType actual2 = recordType.getGreatestSubtypeHelper(recordTypeSubtype);
 assertEquals(recordTypeSubtype, actual2);
 }

 @Test
 public void testDefinePropertyAfterFreeze() {
 recordType.freeze();
 assertFalse(recordType.defineProperty("x", IntType.INSTANCE, false, null));
 }

 @Test
 public void testIsSubtype() {
 assertTrue(IntType.INSTANCE.isSubtype(recordType.getPropertyType("a")));
 assertFalse(recordType.isSubtype(new NativeObjectType(registry, "String")));
 assertTrue(recordType.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeWithFrozenRecordType() {
 recordType.freeze();
 assertFalse(recordType.isSubtype(recordType2));
 }

 @Test
 public void testToString() {
 String expected = "Record { a: Int, b: String }";
 String actual = recordType.toString();
 assertEquals(expected, actual);
 }
}