import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.NativeObjectType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import junit.framework.TestCase;

public class RecordTypeTest extends TestCase {

 private static JSTypeRegistry registry;
 private static RecordType recordType;
 private static RecordType recordTypeFrozen;
 private static RecordType recordTypeWithMismatchedProperties;

 @BeforeClass
 public static void setUpBeforeClass() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("property1", registry.getType(int.class));
 RecordProperty property2 = new RecordProperty("property2", registry.getType(String.class));
 Map<String, RecordProperty> properties = Map.of("property1", property1, "property2", property2);
 recordType = new RecordType(registry, properties);
 recordTypeFrozen = new RecordType(registry, properties, false);
 recordTypeWithMismatchedProperties = new RecordType(registry, Map.of("property1", property1, "property3", property2));
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType.isSynthetic());
 assertFalse(recordTypeFrozen.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeFrozen, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithMismatchedProperties, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(NativeObjectType.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProperty", registry.getType(boolean.class), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordTypeSubtype = recordType.getGreatestSubtypeHelper(registry.getType(int.class));
 JSType recordTypeSubtypeWithSameProperties = recordType.getGreatestSubtypeHelper(recordType);
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType emptyRecordType = registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);

 assertEquals(emptyRecordType, recordTypeSubtypeWithSameProperties.getGreatestSubtypeHelper(registry.getType(String.class)));
 assertEquals(objectType, recordType.getGreatestSubtypeHelper(objectType));
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordTypeFrozen.isSubtype(recordType));
 assertFalse(recordType.isSubtype(registry.getType(int.class)));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE)));
 }

 @Test
 public void testEquals() {
 assertEquals(recordType, recordType);
 assertNotEquals(recordType, recordTypeFrozen);
 }
}