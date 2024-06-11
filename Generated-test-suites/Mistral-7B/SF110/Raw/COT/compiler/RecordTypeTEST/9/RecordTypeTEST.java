import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordType = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE)));
 recordType2 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE), "prop2", new RecordProperty("prop2", StringType.INSTANCE)));
 recordType3 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE), "prop2", new RecordProperty("prop2", StringType.INSTANCE), "prop3", new RecordProperty("prop3", DoubleType.INSTANCE)));
 }

 @Test
 public void testConstructor() {
 RecordType recordType4 = new RecordType(registry, Map.of());
 assertNotNull(recordType4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullProperties() {
 RecordType recordType5 = new RecordType(registry, null);
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType.isSynthetic());
 assertFalse(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.STRICT));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType obj = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(obj, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.hasProperty("prop2"));
 recordType.defineProperty("prop2", StringType.INSTANCE, false, null);
 assertTrue(recordType.hasProperty("prop2"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithFrozenRecord() {
 recordType.isFrozen = true;
 recordType.defineProperty("prop2", StringType.INSTANCE, false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType2.getGreatestSubtypeHelper(recordType);
 assertNotNull(grearestSubtype);
 assertEquals(recordType.getClass(), greatestSubtype.getClass());
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordType2));
 assertFalse(recordType.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeWithNullType() {
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeWithVoidType() {
 assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.VOID_TYPE)));
 }

 @Test
 public void testIsSubtypeWithPrimitiveType() {
 assertFalse(recordType.isSubtype(IntType.INSTANCE));
 assertFalse(recordType.isSubtype(StringType.INSTANCE));
 assertFalse(DoubleType.INSTANCE.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeWithArrayType() {
 assertFalse(recordType.isSubtype(new ArrayType(registry, IntType.INSTANCE)));
 }

 @Test
 public void testIsSubtypeWithUnionType() {
 UnionType unionType = new UnionTypeBuilder(registry).addAlternate(recordType).addAlternate(StringType.INSTANCE).build();
 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testIsSubtypeWithRecordType() {
 RecordType recordTypeSubtype = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE)));
 RecordType recordTypeSuperset = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE), "prop2", new RecordProperty("prop2", StringType.INSTANCE)));
 RecordType recordTypeSuperset2 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", IntType.INSTANCE), "prop2", new RecordProperty("prop2", StringType.INSTANCE), "prop3", new RecordProperty("prop3", DoubleType.INSTANCE)));
 assertTrue(recordTypeSubtype.isSubtype(recordTypeSuperset));
 assertFalse(recordTypeSubtype.isSubtype(recordTypeSuperset2));
 }

 @Test
 public void testIsSubtypeWithObjectType() {
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(objType.isSubtype(recordType));
 }
}