import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 recordType = new RecordType(registry, properties, true);

 properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Float.class));
 recordType2 = new RecordType(registry, properties, false);

 properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Double.class));
 recordType3 = new RecordType(registry, properties, false);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 assertTrue(recordType2.isSynthetic());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.hasProperty("test"));

 Node testNode = new Node("test");
 recordType.defineProperty("test", Boolean.TYPE, false, testNode);
 assertTrue(recordType.hasProperty("test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyType() {
 recordType.defineProperty("test", null, false, new Node("test"));
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.STRICT));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(NativeObjectType.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType4 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", String.class)).asMap(), false);

 JSType type1 = recordType.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, type1);

 JSType type2 = recordType.getGreatestSubtypeHelper(recordType3);
 assertEquals(recordType3, type2);

 JSType type3 = recordType.getGreatestSubtypeHelper(recordType4);
 assertEquals(NativeObjectType.OBJECT_TYPE, type3);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType5 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Integer.class)).asMap(), true);

 assertTrue(recordType.isSubtype(recordType5));
 assertFalse(recordType.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeWithNull() {
 assertFalse(recordType.isSubtype(null));
 }

 @Test
 public void testIsSubtypeWithString() {
 assertFalse(recordType.isSubtype(""));
 }

 @Test
 public void testIsSubtypeWithNumber() {
 assertFalse(recordType.isSubtype(1));
 }

 @Test
 public void testIsSubtypeWithObject() {
 assertFalse(recordType.isSubtype(new Object()));
 }

 @Test
 public void testIsSubtypeWithRecordType() {
 RecordType recordType6 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Integer.class)).asMap(), true);

 assertTrue(recordType.isSubtype(recordType6));
 }

 @Test
 public void testIsSubtypeWithRecordTypeDifferentProperties() {
 RecordType recordType6 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", String.class)).asMap(), true);

 assertFalse(recordType.isSubtype(recordType6));
 }

 @Test
 public void testIsSubtypeWithSubtype() {
 RecordType recordType7 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Integer.class),
         new RecordProperty("gender", String.class)).asMap(), true);

 RecordType recordType8 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Integer.class)).asMap(), true);

 assertTrue(recordType7.isSubtype(recordType8));
 }

 @Test
 public void testIsSubtypeWithIncompatiblePropertyType() {
 RecordType recordType9 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Integer.class),
         new RecordProperty("gender", String.class)).asMap(), true);

 RecordType recordType10 = new RecordType(registry, Arrays.asList(
         new RecordProperty("name", String.class),
         new RecordProperty("age", Float.class),
         new RecordProperty("gender", String.class)).asMap(), true);

 assertFalse(recordType9.isSubtype(recordType10));
 }
}