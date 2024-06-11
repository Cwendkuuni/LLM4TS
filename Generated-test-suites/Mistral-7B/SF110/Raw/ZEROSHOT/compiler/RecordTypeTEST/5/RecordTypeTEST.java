import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.JSTypeNative.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.node.ObjectInitializer;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testRecordTypeConstruction() {
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty(String.class, null));
 properties.put("age", new RecordProperty(Number.class, null));
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals(recordType.getClass(), RecordType.class);
 assertEquals(recordType.getName(), "RecordType");
 assertEquals(recordType.getDeclared(), true);
 assertEquals(recordType.isFrozen(), false);
 }

 @Test
 public void testRecordTypeConstructionWithFrozen() {
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty(String.class, null));
 properties.put("age", new RecordProperty(Number.class, null));
 RecordType recordType = new RecordType(registry, properties, false);
 assertEquals(recordType.getClass(), RecordType.class);
 assertEquals(recordType.getName(), "RecordType");
 assertEquals(recordType.getDeclared(), false);
 assertEquals(recordType.isFrozen(), true);
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), false);
 assertTrue(recordType.isSynthetic());
 RecordType declaredRecordType = new RecordType(registry, new java.util.HashMap<>(), true);
 assertFalse(declaredRecordType.isSynthetic());
 }

 @Test
 public void testRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, new java.util.HashMap<>(), true);
 RecordType recordType2 = new RecordType(registry, new java.util.HashMap<>(), true);
 RecordType recordType3 = new RecordType(registry, new java.util.HashMap<>(), true);
 RecordType recordType4 = new RecordType(registry, new java.util.HashMap<>(), true);

 recordType1.defineProperty("name", String.class, true, createString("John"));
 recordType1.defineProperty("age", Number.class, true, createInt(25));
 recordType2.defineProperty("name", String.class, true, createString("John"));
 recordType2.defineProperty("age", Number.class, true, createInt(30));
 recordType3.defineProperty("name", String.class, true, createString("John"));
 recordType3.defineProperty("age", Number.class, true, createInt(25));
 recordType4.defineProperty("name", String.class, true, createString("Jane"));
 recordType4.defineProperty("age", Number.class, true, createInt(25));

 // Same record types
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.STRUCTURAL));
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRUCTURAL));

 // Different record types with same properties
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRUCTURAL));

 // Different record types with different properties
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.STRUCTURAL));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), true);
 assertEquals(recordType.getImplicitPrototype(), Object.class);
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType.defineProperty("name", String.class, false, createString("John"));
 assertFalse(recordType.isFrozen());
 recordType.defineProperty("age", Number.class, false, createInt(25));
 assertFalse(recordType.isFrozen());
 // Freeze the record type
 recordType.freeze();
 assertTrue(recordType.isFrozen());
 // Attempt to define a property after freezing
 recordType.defineProperty("city", String.class, false, createString("New York"));
 assertFalse(recordType.hasProperty("city"));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType1.defineProperty("name", String.class, true, createString("John"));
 recordType1.defineProperty("age", Number.class, true, createInt(25));

 RecordType recordType2 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType2.defineProperty("name", String.class, true, createString("John"));
 recordType2.defineProperty("age", Number.class, true, createInt(30));
 recordType2.defineProperty("city", String.class, true, createString("New York"));

 RecordType recordType3 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType3.defineProperty("name", String.class, true, createString("John"));
 recordType3.defineProperty("age", Number.class, true, createInt(25));
 recordType3.defineProperty("city", String.class, true, createString("Paris"));

 RecordType recordType4 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType4.defineProperty("name", String.class, true, createString("John"));
 recordType4.defineProperty("age", Number.class, true, createInt(25));
 recordType4.defineProperty("city", String.class, true, createString("Berlin"));

 RecordType recordType5 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType5.defineProperty("name", String.class, true, createString("John"));
 recordType5.defineProperty("age", Number.class, true, createInt(25));
 recordType5.defineProperty("city", UnionType.class, true, new ObjectInitializer());
 recordType5.defineProperty("city.value", String.class, true, createString("New York"));
 recordType5.defineProperty("city.value2", String.class, true, createString("Paris"));

 UnionType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), UnionType.class);
 assertEquals(greatestSubtype.getAlternates().size(), 1);
 assertEquals(greatestSubtype.getAlternates().get(0).getClass(), recordType2.getClass());

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), UnionType.class);
 assertEquals(greatestSubtype.getAlternates().size(), 2);
 assertEquals(greatestSubtype.getAlternates().get(0).getClass(), recordType3.getClass());
 assertEquals(greatestSubtype.getAlternates().get(1).getClass(), recordType2.getClass());

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
 assertEquals(greatestSubtype, Object.class);

 greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
 assertNotNull(greatestSubtype);
 assertEquals(greatestSubtype.getClass(), UnionType.class);
 assertEquals(greatestSubtype.getAlternates().size(), 2);
 assertEquals(greatestSubtype.getAlternates().get(0).getClass(), recordType5.getClass());
 UnionTypeBuilder builder = new UnionTypeBuilder(registry);
 builder.addAlternate(recordType3);
 UnionType alternative = builder.build();
 assertEquals(greatestSubtype.getAlternates().get(1).getClass(), alternative.getClass());
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType1.defineProperty("name", String.class, true, createString("John"));
 recordType1.defineProperty("age", Number.class, true, createInt(25));

 RecordType recordType2 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType2.defineProperty("name", String.class, true, createString("John"));
 recordType2.defineProperty("age", Number.class, true, createInt(30));
 recordType2.defineProperty("city", String.class, true, createString("New York"));

 RecordType recordType3 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType3.defineProperty("name", String.class, true, createString("John"));
 recordType3.defineProperty("age", Number.class, true, createInt(25));
 recordType3.defineProperty("city", String.class, true, createString("Paris"));

 RecordType recordType4 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType4.defineProperty("name", String.class, true, createString("John"));
 recordType4.defineProperty("age", Number.class, true, createInt(25));
 recordType4.defineProperty("city", String.class, true, createString("Berlin"));

 RecordType recordType5 = new RecordType(registry, new java.util.HashMap<>(), true);
 recordType5.defineProperty("name", String.class, true, createString("John"));
 recordType5.defineProperty("age", Number.class, true, createInt(25));
 recordType5.defineProperty("city", UnionType.class, true, new ObjectInitializer());
 recordType5.defineProperty("city.value", String.class, true, createString("New York"));
 recordType5.defineProperty("city.value2", String.class, true, createString("Paris"));

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType3));
 assertTrue(recordType3.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType4.isSubtype(recordType1));
 assertFalse(recordType1.isSubtype(recordType5));
 assertFalse(recordType5.isSubtype(recordType1));
 }

 @Test
 public void testIsSubtypeRecordTypeToObjectType() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), true);
 assertTrue(Object.class.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeObjectTypeToRecordType() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), true);
 assertFalse(recordType.isSubtype(Object.class));
 }

 @Test
 public void testIsSubtypeObjectTypeToObjectType() {
 assertTrue(Object.class.isSubtype(Object.class));
 }

 @Test
 public void testRecordTypeToMaybeRecordType() {
 RecordType recordType = new RecordType(registry, new java.util.HashMap<>(), true);
 assertEquals(recordType.getClass(), RecordType.class);
 assertEquals(recordType.toMaybeRecordType().getClass(), RecordType.class);
 }
}