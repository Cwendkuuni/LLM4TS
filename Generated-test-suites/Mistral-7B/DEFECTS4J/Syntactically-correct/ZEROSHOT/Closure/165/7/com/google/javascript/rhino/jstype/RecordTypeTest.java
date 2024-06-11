import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 properties.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(registry, recordType.getTypeRegistry());
 assertEquals(SortedMap.class, recordType.getProperties().getClass());
 assertEquals(2, recordType.getProperties().size());
 }

 @Test
 public void testRecordTypeProperties() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 properties.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordType = new RecordType(registry, properties);

 assertEquals(JSType.getBooleanType(), recordType.getPropertyType("a"));
 assertEquals(JSType.getNumberType(), recordType.getPropertyType("b"));
 }

 @Test
 public void testRecordTypeIsSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesB.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 RecordType recordTypeAB = new RecordType(registry, propertiesA);

 assertTrue(recordTypeA.isSubtype(recordTypeAB));
 assertFalse(recordTypeA.isSubtype(recordTypeB));
 }

 @Test
 public void testRecordTypeGreatestSubtype() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesB.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 Map<String, RecordProperty> propertiesC = Maps.newHashMap();
 propertiesC.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesC.put("b", new RecordProperty("b", JSType.getBooleanType()));
 RecordType recordTypeC = new RecordType(registry, propertiesC);

 RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
 assertSame(recordTypeC, greatestSubtype);
 }

 @Test
 public void testRecordTypeIsEquivalent() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesB.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 Map<String, RecordProperty> propertiesC = Maps.newHashMap();
 propertiesC.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesC.put("b", new RecordProperty("b", JSType.getBooleanType()));
 RecordType recordTypeC = new RecordType(registry, propertiesC);

 assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
 assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
 assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
 }

 @Test
 public void testRecordTypeToString() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);

 String expected = "{a: boolean}";
 String actual = recordType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testRecordTypeHasProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);

 assertTrue(recordType.hasProperty("a"));
 assertFalse(recordType.hasProperty("b"));
 }

 @Test
 public void testRecordTypeDefineProperty() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);

 JSType newPropertyType = JSType.getNumberType();

 recordType.defineProperty("b", newPropertyType, false, null);

 assertEquals(newPropertyType, recordType.getPropertyType("b"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordTypeDefinePropertyFrozen() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 JSType newPropertyType = JSType.getNumberType();

 recordType.defineProperty("b", newPropertyType, false, null);
 }

 @Test
 public void testRecordTypeFreeze() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);

 recordType.freeze();

 RecordType recordTypeFrozen = new RecordType(registry, properties);

 assertSame(recordType, recordTypeFrozen);
 }

 @Test
 public void testRecordTypeUnfreeze() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordType = new RecordType(registry, properties);
 recordType.freeze();

 RecordType recordTypeUnfrozen = new RecordType(registry, properties);
 recordTypeUnfrozen.unfreeze();

 assertNotSame(recordType, recordTypeUnfrozen);
 }

 @Test
 public void testRecordTypeUnionType() {
 RecordTypeRegistry registry = new RecordTypeRegistry();

 Map<String, RecordProperty> propertiesA = Maps.newHashMap();
 propertiesA.put("a", new RecordProperty("a", JSType.getBooleanType()));
 RecordType recordTypeA = new RecordType(registry, propertiesA);

 Map<String, RecordProperty> propertiesB = Maps.newHashMap();
 propertiesB.put("a", new RecordProperty("a", JSType.getBooleanType()));
 propertiesB.put("b", new RecordProperty("b", JSType.getNumberType()));
 RecordType recordTypeB = new RecordType(registry, propertiesB);

 UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
 for (ObjectType alt : registry.getEachReferenceTypeWithProperty("a")) {
 if (alt.isSubtype(recordTypeB)) {
 unionTypeBuilder.addAlternate(alt);
 }
 }
 JSType unionType = unionTypeBuilder.build();

 RecordType recordTypeUnion = new RecordType(registry, propertiesA);
 JSType greatestSubtype = recordTypeA.getGreatestSubtype(unionType);

 assertSame(recordTypeB, greatestSubtype);
 }
}