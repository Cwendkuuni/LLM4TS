import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 RecordProperty property1 = new RecordProperty("property1", Integer.TYPE);
 RecordProperty property2 = new RecordProperty("property2", String.class);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("property1", property1);
 properties.put("property2", property2);
 recordType = new RecordType(registry, properties);
 }

 @Test
 public void testIsEquivalentTo() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());

 assertTrue(recordType1.isEquivalentTo(recordType1));
 assertTrue(recordType1.isEquivalentTo(recordType2));
 assertFalse(recordType1.isEquivalentTo(recordType3));
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry, properties);
 RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
 RecordType recordType5 = new RecordType(registry, properties);
 RecordType recordType6 = new RecordType(registry,
 Maps.<String, RecordProperty>of("property1", new RecordProperty("property1", String.class)));

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertFalse(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType4));
 assertFalse(recordType1.isSubtype(recordType5));
 assertFalse(recordType1.isSubtype(recordType6));

 RecordType recordType7 = new RecordType(registry,
 Maps.<String, RecordProperty>of("property1", new RecordProperty("property1", Integer.TYPE),
 "property2", new RecordProperty("property2", String.class)));
 assertTrue(recordType1.isSubtype(recordType7));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(registry, properties);
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
 recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, properties);
 Node node = new Node();
 recordType.defineProperty("property3", Integer.TYPE, false, node);
 assertTrue(recordType.hasProperty("property3"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyFrozen() {
 RecordType recordType = new RecordType(registry, properties);
 recordType.isFrozen = true;
 recordType.defineProperty("property3", Integer.TYPE, false, null);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordType recordType1 = new RecordType(registry, properties);
 RecordType recordType2 = new RecordType(registry,
 Maps.<String, RecordProperty>of("property1", new RecordProperty("property1", Boolean.TYPE),
 "property2", new RecordProperty("property2", String.class)));
 RecordType recordType3 = new RecordType(registry,
 Maps.<String, RecordProperty>of("property1", new RecordProperty("property1", Integer.TYPE),
 "property2", new RecordProperty("property2", String.class),
 "property3", new RecordProperty("property3", Boolean.TYPE)));

 JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType3, greatestSubtype);
 }
}