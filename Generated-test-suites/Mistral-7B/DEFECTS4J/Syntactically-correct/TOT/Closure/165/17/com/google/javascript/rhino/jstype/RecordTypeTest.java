import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private static JSTypeRegistry registry;

 @BeforeClass
 public static void setUp() {
 registry = new JSTypeRegistry();
 }

 /**
 * Test creating a simple record type.
 */
 @Test
 public void testSimpleRecordType() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", aProp);
 properties.put("b", bProp);

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("RecordType", recordType.getClass().getSimpleName());

 assertEquals("{a: String, b: Number}", recordType.toString());
 }

 /**
 * Test creating a record type with all properties inherited.
 */
 @Test
 public void testRecordTypeWithAllPropertiesInherited() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());
 RecordProperty cProp = new RecordProperty("c", registry.getStringType());

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", aProp);
 properties.put("b", bProp);
 properties.put("c", cProp);

 RecordType parentRecordType = new RecordType(registry, properties);
 RecordProperty dProp = new RecordProperty("d", registry.getBooleanType());

 Map<String, RecordProperty> childProperties = Maps.newHashMap();
 childProperties.put("d", dProp);

 RecordType childRecordType = new RecordType(registry, childProperties);
 childRecordType.setPrototype(parentRecordType);

 assertEquals("{a: String, b: Number, d: Boolean}", childRecordType.toString());
 }

 /**
 * Test record subtyping.
 */
 @Test
 public void testRecordSubtyping() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());
 RecordProperty cProp = new RecordProperty("c", registry.getStringType());

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", aProp);
 propertiesA.put("b", bProp);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", bProp);
 propertiesB.put("c", cProp);

 RecordType recordA = new RecordType(registry, propertiesA);
 RecordType recordB = new RecordType(registry, propertiesB);

 assertTrue(recordA.isSubtype(recordB));
 }

 /**
 * Test record subtyping with conflicting property types.
 */
 @Test
 public void testRecordSubtypingWithConflictingPropertyTypes() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getBooleanType());

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", aProp);
 propertiesA.put("b", bProp);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty("a", registry.getNumberType()));
 propertiesB.put("b", bProp);

 RecordType recordA = new RecordType(registry, propertiesA);
 RecordType recordB = new RecordType(registry, propertiesB);

 assertFalse(recordA.isSubtype(recordB));
 assertFalse(recordB.isSubtype(recordA));
 }

 /**
 * Test record subtyping with one record type a supertype of another.
 */
 @Test
 public void testRecordSubtypingWithOneRecordASupertypeOfAnother() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());
 RecordProperty cProp = new RecordProperty("c", registry.getStringType());

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", aProp);
 propertiesA.put("b", bProp);
 propertiesA.put("c", cProp);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", bProp);
 propertiesB.put("c", cProp);

 RecordType recordA = new RecordType(registry, propertiesA);
 RecordType recordB = new RecordType(registry, propertiesB);

 assertTrue(recordA.isSubtype(recordB));
 }

 /**
 * Test record subtyping with one record type a subtype of another.
 */
 @Test
 public void testRecordSubtypingWithOneRecordASubtypeOfAnother() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());
 RecordProperty cProp = new RecordProperty("c", registry.getStringType());

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", aProp);
 propertiesA.put("b", bProp);
 propertiesA.put("c", cProp);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", aProp);
 propertiesB.put("b", bProp);
 propertiesB.put("c", cProp);

 RecordType recordB = new RecordType(registry, propertiesB);
 RecordType recordA = new RecordType(registry, propertiesA);

 assertTrue(recordA.isSubtype(recordB));
 }

 /**
 * Test record subtyping with conflicting property names.
 */
 @Test
 public void testRecordSubtypingWithConflictingPropertyNames() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());
 RecordProperty cProp = new RecordProperty("c", registry.getStringType());

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", aProp);
 propertiesA.put("b", bProp);
 propertiesA.put("c", cProp);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("b", bProp);
 propertiesB.put("a", aProp); // note the reversed order
 propertiesB.put("c", cProp);

 RecordType recordA = new RecordType(registry, propertiesA);
 RecordType recordB = new RecordType(registry, propertiesB);

 assertTrue(recordA.isEquivalentTo(recordB));
 }

 /**
 * Test record type with properties of unknown type.
 */
 @Test
 public void testRecordTypeWithPropertiesOfUnknownType() {
 RecordProperty aProp = new RecordProperty("a", null);
 RecordProperty bProp = new RecordProperty("b", null);

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", aProp);
 properties.put("b", bProp);

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{a: UnknownType, b: UnknownType}", recordType.toString());
 }

 /**
 * Test record type with frozen properties.
 */
 @Test
 public void testRecordTypeWithFrozenProperties() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType());

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", aProp);
 properties.put("b", bProp);

 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 try {
 recordType.defineProperty("c", registry.getStringType(), false, null);
 fail("Should not be able to define a new property on a frozen record type.");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 /**
 * Test record type with properties that have nodes.
 */
 @Test
 public void testRecordTypeWithPropertiesThatHaveNodes() {
 RecordProperty aProp = new RecordProperty("a", registry.getStringType(), new Node());
 RecordProperty bProp = new RecordProperty("b", registry.getNumberType(), new Node());

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", aProp);
 properties.put("b", bProp);

 RecordType recordType = new RecordType(registry, properties);

 assertEquals("{a: String, b: Number}", recordType.toString());
 }
}