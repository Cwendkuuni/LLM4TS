import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;

public class RecordTypeTest {

 private TypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;
 private RecordType recordType4;

 @org.junit.Before
 public void setUp() {
 registry = new TypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 RecordProperty recordProperty = new RecordProperty();
 recordProperty.setPropertyName("name");
 recordProperty.setPropertyType(JSTypeFactory.getSimpleType(String.class));
 recordProperty.setPropertyNode(Node.newIdent("name"));
 recordType = new RecordType(registry, Maps.newHashMap(), true) {
 };
 recordType.defineSynthesizedProperty("name", JSTypeFactory.getSimpleType(String.class), recordProperty.getPropertyNode());
 recordType2 = new RecordType(registry, Maps.newHashMap(), true) {
 };
 recordType2.defineSynthesizedProperty("name", JSTypeFactory.getSimpleType(String.class), recordProperty.getPropertyNode());
 recordType2.defineSynthesizedProperty("age", JSTypeFactory.getSimpleType(int.class), Node.newIdent("age"));
 recordType3 = new RecordType(registry, Maps.newHashMap(), true) {
 };
 recordType3.defineSynthesizedProperty("name", JSTypeFactory.getSimpleType(String.class), recordProperty.getPropertyNode());
 recordType3.defineSynthesizedProperty("age", JSTypeFactory.getSimpleType(int.class), Node.newIdent("age"));
 recordType3.defineSynthesizedProperty("city", JSTypeFactory.getSimpleType(String.class), Node.newIdent("city"));
 recordType4 = new RecordType(registry, Maps.newHashMap(), true) {
 };
 recordType4.defineSynthesizedProperty("name", JSTypeFactory.getSimpleType(String.class), recordProperty.getPropertyNode());
 recordType4.defineSynthesizedProperty("age", JSTypeFactory.getSimpleType(int.class), Node.newIdent("age"));
 recordType4.defineSynthesizedProperty("city", JSTypeFactory.getSimpleType(String.class), Node.newIdent("city"));
 recordType4.defineSynthesizedProperty("state", JSTypeFactory.getSimpleType(String.class), Node.newIdent("state"));
 }

 @org.junit.Test
 public void testIsSynthetic() {
 assertTrue(recordType.isSynthetic());
 assertFalse(recordType2.isSynthetic());
 }

 @org.junit.Test
 public void testIsSubtype() {
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType intType = JSTypeFactory.getSimpleType(int.class);
 JSType stringType = JSTypeFactory.getSimpleType(String.class);

 assertTrue(recordType.isSubtype(objectType));
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType2.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordType2));
 assertFalse(recordType.isSubtype(intType));
 assertFalse(recordType.isSubtype(stringType));
 assertFalse(recordType.isSubtype(JSTypeFactory.getNullType()));
 assertFalse(intType.isSubtype(recordType));
 assertFalse(intType.isSubtype(recordType2));
 assertFalse(stringType.isSubtype(recordType));
 assertFalse(stringType.isSubtype(recordType2));
 assertFalse(JSTypeFactory.getNullType().isSubtype(recordType));
 assertFalse(JSTypeFactory.getNullType().isSubtype(recordType2));
 }

 @org.junit.Test
 public void testGetImplicitPrototype() {
 JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(objectType, recordType.getImplicitPrototype());
 }

 @org.junit.Test
 public void testDefineProperty() {
 JSType newType = new PrototypeObjectType(registry, null, null);
 newType.defineProperty("name", JSTypeFactory.getSimpleType(String.class), true, Node.newIdent("name"));
 RecordType newRecordType = new RecordType(registry, Maps.newHashMap(), true) {
 };
 newRecordType.defineProperty("name", JSTypeFactory.getSimpleType(String.class), false, Node.newIdent("name"));

 assertFalse(recordType.defineProperty("name", JSTypeFactory.getSimpleType(int.class), true, Node.newIdent("name")));
 assertTrue(recordType.defineProperty("name2", JSTypeFactory.getSimpleType(String.class), true, Node.newIdent("name2")));
 assertNull(recordType.getPropertyNode("name2"));
 assertNotNull(recordType.getPropertyType("name2"));
 assertNull(recordType.getPropertyNode("name"));
 assertNotNull(recordType.getPropertyType("name"));
 }

 @org.junit.Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, RecordType.EQUIVALENCE_BY_IDENTITY));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, RecordType.EQUIVALENCE_BY_IDENTITY));
 }

 @org.junit.Test
 public void testGetGreatestSubtypeHelper() {
 JSType recordType5 = new RecordType(registry, Maps.newHashMap(), true) {
 };
 recordType5.defineSynthesizedProperty("name", JSTypeFactory.getSimpleType(String.class), recordProperty.getPropertyNode());
 recordType5.defineSynthesizedProperty("age", JSTypeFactory.getSimpleType(int.class), Node.newIdent("age"));

 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType5);
 assertEquals(recordType4.getType(), greatestSubtype.getType());
 }

 @org.junit.Test
 public void testToString() {
 assertEquals("Record<name:string, age:number>", recordType.toString());
 assertEquals("Record<name:string, age:number, city:string, state:string>", recordType4.toString());
 }
}