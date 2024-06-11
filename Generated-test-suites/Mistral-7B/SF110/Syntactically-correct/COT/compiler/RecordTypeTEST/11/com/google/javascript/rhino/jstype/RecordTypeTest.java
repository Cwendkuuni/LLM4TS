import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeFrozen;
 private RecordType recordTypeEmpty;
 private RecordType recordTypeWithSubtypes;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordType = recordTypeBuilder.buildRecordType("recordType",
 new RecordProperty[] {
 new RecordProperty("prop1", JSType.STRING),
 new RecordProperty("prop2", JSType.NUMBER)
 });
 recordTypeFrozen = new RecordType(registry, recordType.getProperties(), true);
 recordTypeEmpty = new RecordType(registry, null, true);
 recordTypeWithSubtypes = new RecordType(registry, recordType.getProperties(), false);
 }

 @Test
 public void testRecordTypeConstructor() {
 RecordType recordTypeInstance = new RecordType(registry, recordType.getProperties(), true);
 assertEquals(recordType.getName(), recordTypeInstance.getName());
 assertEquals(recordType.getProperties().size(), recordTypeInstance.getProperties().size());
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordTypeWithSubtypes.isSynthetic());
 assertFalse(recordType.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordTypeInstance1 = new RecordType(registry, recordType.getProperties(), true);
 RecordType recordTypeInstance2 = new RecordType(registry, recordType.getProperties(), true);
 assertTrue(recordType.checkRecordEquivalenceHelper(recordTypeInstance1, RecordType.EquivalenceMethod.STRICT));
 assertTrue(recordType.checkRecordEquivalenceHelper(recordTypeInstance2, RecordType.EquivalenceMethod.STRICT));
 assertTrue(recordType.checkRecordEquivalenceHelper(recordTypeInstance1, recordTypeInstance2, RecordType.EquivalenceMethod.STRICT));

 RecordType recordTypeInstance3 = new RecordType(registry, recordType.getProperties(), true);
 recordTypeInstance3.setPropertyType("prop1", JSType.NUMBER);
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeInstance1, recordTypeInstance3, RecordType.EquivalenceMethod.STRICT));

 RecordType recordTypeInstance4 = new RecordType(registry, recordType.getProperties(), true);
 recordTypeInstance4.setPropertyType("prop2", JSType.BOOLEAN);
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeInstance1, recordTypeInstance4, RecordType.EquivalenceMethod.STRICT));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 new RecordType(registry, null, true);
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("prop3", JSType.STRING, false, null));
 assertTrue(recordType.defineProperty("prop3", JSType.STRING, true, null));
 assertTrue(recordType.hasProperty("prop3"));
 }

 @Test
 public void testFrozenPropertyDefinition() {
 assertFalse(recordTypeFrozen.defineProperty("prop3", JSType.STRING, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType typeA = new UnionTypeBuilder(registry)
 .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
 .addAlternate(recordType)
 .build();
 JSType typeB = new UnionTypeBuilder(registry)
 .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
 .addAlternate(recordTypeWithSubtypes)
 .build();
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(typeA);
 assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(typeB);
 assertEquals(recordTypeWithSubtypes, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertFalse(JSTypeNative.STRING.isSubtype(recordType));
 assertTrue(recordType.isSubtype(JSTypeNative.OBJECT_TYPE));
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordTypeFrozen));
 assertTrue(recordType.isSubtype(recordTypeWithSubtypes));
 assertFalse(recordTypeWithSubtypes.isSubtype(recordType));
 }

 @Test
 public void testToString() {
 assertEquals("RecordType[name=\"recordType\", properties=[Prop[name=\"prop1\", type=\"String\"], Prop[name=\"prop2\", type=\"Number\"]]]]", recordType.toString());
 }
}