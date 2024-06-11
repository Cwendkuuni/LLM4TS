import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypedType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private JSTypeFactory typeFactory;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 typeFactory = new JSTypeFactory(registry);
 }

 @Test
 public void testRecordTypeConstruction() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 RecordProperty prop1 = new RecordProperty("prop1", typeFactory.stringType());
 RecordProperty prop2 = new RecordProperty("prop2", typeFactory.numberType());
 Map<String, RecordProperty> properties = Map.of("prop1", prop1, "prop2", prop2);
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals(RecordType.class, recordType.getClass());
 }

 @Test
 public void testRecordTypeConstructionFrozen() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 RecordProperty prop1 = new RecordProperty("prop1", typeFactory.stringType());
 RecordProperty prop2 = new RecordProperty("prop2", typeFactory.numberType());
 Map<String, RecordProperty> properties = Map.of("prop1", prop1, "prop2", prop2);
 RecordType recordType = new RecordType(registry, properties, false);
 assertFalse(recordType.isFrozen());
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, Map.of(), false);
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordTypeBuilder builder1 = new RecordTypeBuilder(registry);
 RecordProperty prop1 = new RecordProperty("prop1", typeFactory.stringType());
 RecordProperty prop2 = new RecordProperty("prop2", typeFactory.numberType());
 Map<String, RecordProperty> properties1 = Map.of("prop1", prop1, "prop2", prop2);
 RecordType recordType1 = new RecordType(registry, properties1, true);

 RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
 RecordProperty prop3 = new RecordProperty("prop3", typeFactory.booleanType());
 Map<String, RecordProperty> properties2 = Map.of("prop1", prop1, "prop2", prop2, "prop3", prop3);
 RecordType recordType2 = new RecordType(registry, properties2, true);

 assertEquals(recordType1, recordType1);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.IDENTICAL));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRUCTURAL));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, Map.of(), true);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Map.of(), true);
 RecordProperty prop = new RecordProperty("prop", typeFactory.booleanType());
 assertFalse(recordType.defineProperty("prop", typeFactory.booleanType(), false, null));
 assertTrue(recordType.defineProperty("prop", typeFactory.booleanType(), true, null));
 }

 @Test
 public void testGetPropertyType() {
 RecordType recordType = new RecordType(registry, Map.of("prop", new RecordProperty("prop", typeFactory.booleanType())), true);
 assertEquals(typeFactory.booleanType(), recordType.getPropertyType("prop"));
 }

 @Test
 public void testDefineAndGetPropertyNode() {
 RecordType recordType = new RecordType(registry, Map.of("prop", new RecordProperty("prop", typeFactory.booleanType())), true);
 Node propNode = new Node(Node.IDENTIFIER, "prop");
 assertEquals(propNode, recordType.getPropertyNode("prop"));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType()), true);
 RecordType recordType2 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType(), "prop3", typeFactory.stringType()), true);
 JSType type1 = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType2, type1);

 RecordType recordType3 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType(), "prop3", typeFactory.stringType()), true);
 JSType type2 = recordType1.getGreatestSubtypeHelper(recordType3);
 assertEquals(UnionType.create(recordType1, recordType3), type2);

 JSType type3 = recordType1.getGreatestSubtypeHelper(typeFactory.objectType());
 assertEquals(JSTypeNative.NO_OBJECT_TYPE, type3);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType()), true);
 RecordType recordType2 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType(), "prop3", typeFactory.stringType()), true);
 RecordType recordType3 = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType(), "prop3", typeFactory.numberType()), true);
 RecordType recordType4 = new RecordType(registry, Map.of("prop1", typeFactory.numberType()), true);

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType3.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));
 }

 @Test
 public void testToString() {
 RecordType recordType = new RecordType(registry, Map.of("prop1", typeFactory.numberType(), "prop2", typeFactory.booleanType()), true);
 assertEquals("RecordType{[prop1: Number, prop2: Boolean]}", recordType.toString());
 }
}