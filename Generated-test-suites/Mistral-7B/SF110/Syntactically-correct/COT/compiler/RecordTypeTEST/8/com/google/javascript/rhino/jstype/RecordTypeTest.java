import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeFrozen;
 private RecordType recordTypeWithDifferentProperties;
 private RecordType recordTypeWithSubtype;
 private RecordType recordTypeWithInvariantProperty;
 private RecordType recordTypeWithNonInvariantProperty;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty1 = new RecordProperty("prop1", JSType.STRING);
 RecordProperty recordProperty2 = new RecordProperty("prop2", JSType.NUMBER);
 Map<String, RecordProperty> properties = Map.of("prop1", recordProperty1, "prop2", recordProperty2);
 recordType = new RecordType(registry, properties, true);
 recordTypeFrozen = new RecordType(registry, properties, true);
 recordTypeFrozen.freeze();
 recordTypeWithDifferentProperties = new RecordType(registry, Map.of("prop1", recordProperty1, "prop3", recordProperty2), true);
 recordTypeWithSubtype = new RecordType(registry, Map.of("prop1", JSType.STRING), true);
 recordTypeWithInvariantProperty = new RecordType(registry, Map.of("prop1", JSType.NUMBER), true);
 recordTypeWithNonInvariantProperty = new RecordType(registry, Map.of("prop1", JSType.NUMBER), true);
 recordTypeWithNonInvariantProperty.setProperty("prop1", JSType.STRING);
 recordTypeWithSubtype = new RecordTypeBuilder(registry)
 .setSupertype(recordType)
 .addProperty("prop3", JSType.BOOLEAN)
 .build();
 }

 @Test
 public void testConstructor() {
 RecordType recordType = new RecordType(registry, Map.of(), true);
 assertNotNull(recordType);
 }

 @Test
 public void testIsSynthetic() {
 assertTrue(recordType.isSynthetic());
 assertFalse(recordTypeFrozen.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, EquivalenceMethod.STRICT));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeWithDifferentProperties, EquivalenceMethod.STRICT));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProperty", JSType.BOOLEAN, false, null));
 assertTrue(recordTypeFrozen.defineProperty("newProperty", JSType.BOOLEAN, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType emptyObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(emptyObjectType);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithSubtype);
 assertEquals(recordTypeWithSubtype, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(JSType.STRING);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordTypeWithInvariantProperty));
 assertFalse(recordType.isSubtype(recordTypeWithNonInvariantProperty));
 assertTrue(recordType.isSubtype(recordTypeWithSubtype));
 }

 @Test
 public void testIsSubtypeStatic() {
 assertTrue(RecordType.isSubtype(recordType, recordTypeWithInvariantProperty));
 assertFalse(RecordType.isSubtype(recordType, recordTypeWithNonInvariantProperty));
 assertFalse(RecordType.isSubtype(recordType, JSType.STRING));
 assertTrue(RecordType.isSubtype(recordType, recordTypeWithSubtype));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordPropertyNull() {
 new RecordType(registry, null, true);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordPropertyNullType() {
 RecordProperty recordProperty = new RecordProperty("prop", null);
 Map<String, RecordProperty> properties = Map.of("prop", recordProperty);
 new RecordType(registry, properties, true);
 }

 @Test
 public void testFreeze() {
 assertFalse(recordType.isFrozen());
 recordTypeFrozen.freeze();
 assertTrue(recordTypeFrozen.isFrozen());
 }
}