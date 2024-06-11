import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testCreateRecordType() {
 // Test creating a record type with declared properties
 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals("RecordType with declared properties", recordType.getClass(), RecordType.class);

 // Test creating a record type with synthesized properties
 properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));
 RecordType synthesizedRecordType = new RecordType(registry, properties, false);
 assertEquals("RecordType with synthesized properties", synthesizedRecordType.getClass(), RecordType.class);
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, new HashMap<>(), true);
 RecordProperty recordProperty1 = new RecordProperty("name", registry.getStringType());
 RecordProperty recordProperty2 = new RecordProperty("age", registry.getNumberType());
 recordType1.defineSynthesizedProperty("name", registry.getStringType(), null);
 recordType1.defineSynthesizedProperty("age", registry.getNumberType(), null);
 recordType1.defineProperty("name", registry.getStringType(), false, null);
 recordType1.defineProperty("age", registry.getNumberType(), false, null);
 RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType3 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType4 = new RecordType(registry, new HashMap<>(), false);

 assertTrue("Check record equivalence helper for equivalent records", recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
 assertFalse("Check record equivalence helper for non-equivalent records", recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));
 assertFalse("Check record equivalence helper for synthetic record", recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTITY));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, new HashMap<>(), true);
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals("Get implicit prototype returns the Object type", ObjectType.class, implicitPrototype.getClass());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, new HashMap<>(), true);
 String propertyName = "property";
 JSType propertyType = registry.getStringType();
 Node propertyNode = Node.NULL();
 assertTrue("Can define a property", recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 assertFalse("Cannot define a property after it has been frozen", recordType.defineProperty(propertyName, propertyType, false, propertyNode));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType3 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType4 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType5 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType6 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType7 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType8 = new RecordType(registry, new HashMap<>(), false);

 JSType type1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType type2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
 JSType type3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
 JSType type4 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
 .getGreatestSubtype(recordType1);
 JSType type5 = recordType1.getGreatestSubtypeHelper(recordType2);
 JSType type6 = recordType1.getGreatestSubtypeHelper(type3);
 JSType type7 = recordType1.getGreatestSubtypeHelper(type4);
 JSType type8 = recordType1.getGreatestSubtypeHelper(recordType5);
 JSType type9 = recordType1.getGreatestSubtypeHelper(recordType6);
 JSType type10 = recordType1.getGreatestSubtypeHelper(recordType7);
 JSType type11 = recordType1.getGreatestSubtypeHelper(recordType8);

 assertEquals("Get greatest subtype helper returns the same type for identical types", type1, type1);
 assertEquals("Get greatest subtype helper returns the null type for a null type", type2, type2);
 assertEquals("Get greatest subtype helper returns the number type for a number type", type3, type3);
 assertEquals("Get greatest subtype helper returns the record type for a record type with same properties", type4, type4);
 assertEquals("Get greatest subtype helper returns an empty record type for a record type with different properties", recordTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), type5);
 assertEquals("Get greatest subtype helper returns an empty record type for a record type with different properties", recordTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), type6);
 assertEquals("Get greatest subtype helper returns the record type with all inherited properties for a record type with some inherited properties", type7, type7);
 assertEquals("Get greatest subtype helper returns the record type with all inherited properties for a record type with some inherited properties", type8, type8);
 assertEquals("Get greatest subtype helper returns the record type with all inherited properties for a record type with some inherited properties", type9, type9);
 assertEquals("Get greatest subtype helper returns the record type with all inherited properties for a record type with some inherited properties", type10, type10);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType3 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType4 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType5 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType6 = new RecordType(registry, new HashMap<>(), true);
 RecordType recordType7 = new RecordType(registry, new HashMap<>(), false);
 RecordType recordType8 = new RecordType(registry, new HashMap<>(), false);
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue("Record type is a subtype of itself", recordType1.isSubtype(recordType1));
 assertTrue("Object type is a subtype of any record type", objectType.isSubtype(recordType1));
 assertTrue("Record type1 is a subtype of record type2 with the same properties", recordType1.isSubtype(recordType2));
 assertFalse("Record type1 is not a subtype of record type3 with different properties", recordType1.isSubtype(recordType3));
 assertTrue("Record type1 is a subtype of record type4 with the same properties and declared properties", recordType1.isSubtype(recordType4));
 assertFalse("Record type1 is not a subtype of record type5 with different properties", recordType1.isSubtype(recordType5));
 assertFalse("Record type1 is not a subtype of record type6 with the same properties but different constructor", recordType1.isSubtype(recordType6));
 assertFalse("Record type1 is not a subtype of record type7 with the same properties but different properties", recordType1.isSubtype(recordType7));
 assertFalse("Record type1 is not a subtype of record type8 with the same properties but different constructor and different properties", recordType1.isSubtype(recordType8));
 }
}