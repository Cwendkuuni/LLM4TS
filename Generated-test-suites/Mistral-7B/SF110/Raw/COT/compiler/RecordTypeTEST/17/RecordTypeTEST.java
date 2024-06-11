import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.node.Node;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeFrozen;
 private RecordType recordTypeWithSynthetic;
 private RecordType recordTypeWithDifferentProperties;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty = new RecordProperty("name", registry.getStringType());
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordType = recordTypeBuilder.buildRecordType("RecordType", recordProperty);
 recordTypeFrozen = new RecordType(registry, recordType.getProperties(), true);
 recordTypeWithSynthetic = new RecordType(registry, recordType.getProperties(), false);
 recordTypeWithDifferentProperties = new RecordType(registry, recordType.getProperties(), true);
 recordTypeWithDifferentProperties.defineProperty("age", registry.getNumberType(), false, Node.NULL);
 }

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordType = new RecordType(registry, recordType.getProperties(), true);
 assertEquals(recordType.getClass(), RecordType.class);
 }

 @Test
 public void testIsSyntheticMethod() {
 assertTrue(recordTypeWithSynthetic.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeFrozen, EquivalenceMethod.IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithSynthetic, EquativityMethod.IDENTITY));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordPropertyIsNull() {
 RecordProperty recordProperty = null;
 new RecordType(registry, Map.of("property", recordProperty));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineDeclaredPropertyWithNullType() {
 recordType.defineDeclaredProperty("name", null, Node.NULL);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineDeclaredPropertyWithNullPropertyNode() {
 recordType.defineDeclaredProperty("name", registry.getStringType(), Node.NULL);
 }

 @Test
 public void testDefineSynthesizedProperty() {
 recordTypeWithSynthetic.defineSynthesizedProperty("name", registry.getStringType(), Node.NULL);
 assertTrue(recordTypeWithSynthetic.hasProperty("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithFrozenRecordType() {
 recordTypeFrozen.defineProperty("name", registry.getStringType(), false, Node.NULL);
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 recordType.defineProperty("age", registry.getNumberType(), false, Node.NULL);
 assertTrue(recordType.hasProperty("age"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithFrozenRecordType() {
 recordTypeFrozen.defineProperty("age", registry.getNumberType(), false, Node.NULL);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
 assertSame(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithDifferentProperties);
 assertSame(recordType, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordTypeFrozen));
 assertTrue(recordType.isSubtype(recordTypeWithSynthetic));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
 assertFalse(recordType.isSubtype(recordTypeWithDifferentProperties));
 }
}