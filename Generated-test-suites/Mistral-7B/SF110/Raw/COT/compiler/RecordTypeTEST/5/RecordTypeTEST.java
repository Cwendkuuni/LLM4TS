import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;
 private RecordProperty recordProperty;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 recordProperty = new RecordProperty("name", registry.getStringType());
 recordType = new RecordType(registry, new HashMap<String, RecordProperty>() {{
 put("name", recordProperty);
 }});
 recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
 put("name", recordProperty);
 put("age", registry.getNUMBER_TYPE());
 }});
 recordType3 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
 put("name", recordProperty);
 put("age", registry.getNUMBER_TYPE());
 put("city", registry.getStringType());
 }});
 }

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordType = new RecordType(registry, null, true);
 assertNotNull(recordType);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 assertTrue(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType,
 RecordType.EquivalenceMethod.IDENTICAL));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2,
 RecordType.EquivalenceMethod.IDENTICAL));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType,
 RecordType.EquivaleceMethod.PROTOTYPE));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2,
 RecordType.EquivalenceMethod.PROTOTYPE));
 }

 @Test
 public void testGetImplicitPrototype() {
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype);
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("test", JSType.ANY, false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWithNullPropertyNode() {
 recordType.defineProperty("test", JSType.ANY, false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);
 assertEquals(JSType.ANY, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(JSType.ANY);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.BOOLEAN_TYPE));
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertEquals(recordType, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.BOOLEAN_TYPE)));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
 assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.STRING_TYPE)));
 }

 @Test
 public void testIsSubtypeWithRecordTypes() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 assertFalse(recordType.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeWithUnionTypes() {
 RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>() {{
 put("name", registry.getStringType());
 put("age", registry.getNUMBER_TYPE());
 put("city", registry.getStringType());
 put("friends", registry.getArrayType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), 0));
 }});

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
 unionTypeType = unionTypeBuilder.build();

 assertTrue(recordType.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType));
 assertFalse(recordType1.isSubtype(unionTypeType));
 assertFalse(unionTypeType.isSubtype(recordType));
 }

 @Test
 public void testDefinePropertyFrozen() {
 recordType.isFrozen = true;
 assertFalse(recordType.defineProperty("test", JSType.ANY, false, null));
 }
}