import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeWithSynthesizedProperties;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordType = recordTypeBuilder
 .setName("Person")
 .addProperty("name", StringType.getInstance())
 .addProperty("age", NumberType.INT)
 .build();
 recordTypeWithSynthesizedProperties = new RecordType(registry, recordType.getProperties(), false);
 }

 @Test
 public void testIsSubtype() {
 RecordType personType = new RecordType(registry, recordType.getProperties(), true);
 ObjectType objTypeWithNameAndAge = new ObjectType(registry, null, null);
 objTypeWithNameAndAge.defineProperty("name", StringType.getInstance(), false, null);
 objTypeWithNameAndAge.defineProperty("age", NumberType.INT, false, null);

 assertTrue(personType.isSubtype(recordType));
 assertTrue(personType.isSubtype(recordTypeWithSynthesizedProperties));
 assertTrue(personType.isSubtype(objTypeWithNameAndAge));
 assertTrue(personType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(personType));
 }

 @Test
 public void testDefineProperty() {
 recordType.defineProperty("gender", StringType.getInstance(), false, null);
 RecordType recordTypeWithGender = new RecordType(registry, recordType.getProperties(), true);

 assertTrue(recordTypeWithGender.hasProperty("gender"));
 assertEquals(StringType.getInstance(), recordTypeWithGender.getPropertyType("gender"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_Frozen() {
 recordType.defineProperty("gender", StringType.getInstance(), false, null);
 recordType.isFrozen = true;
 recordType.defineProperty("gender", StringType.getInstance(), false, null);
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, recordType.getProperties(), true);
 RecordType recordType2 = new RecordType(registry, recordType.getProperties(), true);

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.BY_VALUE));
 assertFalse(recordType1.checkRecordEquivalenceHelper(new RecordType(registry, recordType.getProperties(), false), EquivalenceMethod.BY_VALUE));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType3 = new RecordType(registry, recordType.getProperties(), true);
 ObjectType objectType1 = new ObjectType(registry, null, null);
 ObjectType objectType2 = new ObjectType(registry, null, null);
 objectType1.defineProperty("name", StringType.getInstance(), false, null);
 objectType1.defineProperty("age", NumberType.INT, false, null);
 objectType2.defineProperty("name", StringType.getInstance(), false, null);
 objectType2.defineProperty("age", NumberType.INT, false, null);
 objectType2.defineProperty("id", NumberType.INT, false, null);

 assertEquals(recordType3, recordType3.getGreatestSubtypeHelper(recordType3));
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType3.getGreatestSubtypeHelper(objType1));
 assertEquals(recordType3, recordType3.getGreatestSubtypeHelper(objectType2));
 }
}