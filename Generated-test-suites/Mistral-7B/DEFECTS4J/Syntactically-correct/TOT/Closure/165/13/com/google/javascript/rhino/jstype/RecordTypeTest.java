import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

 @BeforeClass
 public static void setUp() {
 JSTypeRegistry registry = new JSTypeRegistry();
 // Create a simple record type with two properties
 RecordProperty prop1 = new RecordProperty("prop1", Integer.TYPE);
 RecordProperty prop2 = new RecordProperty("prop2", String.class);
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("prop1", prop1);
 properties.put("prop2", prop2);
 RecordType recordType = new RecordType(registry, properties);
 // Add this record type to the registry for testing
 registry.registerType(recordType.getName(), recordType);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType = (RecordType) registry.getTypeByName("{prop1: int, prop2: string}");
 // Test subclassing
 RecordType subType = new RecordType(registry, properties);
 subType.setName("{prop1: int, prop2: string, prop3: boolean}");
 assertTrue(recordType.isSubtype(subType));
 // Test with a superclass
 ObjectType superType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertTrue(superType.isSubtype(recordType));
 // Test with a different record type
 RecordType differentRecord = new RecordType(registry, Maps.newHashMap());
 differentRecord.setName("{prop1: int, prop3: boolean}");
 assertFalse(recordType.isSubtype(differentRecord));
 // Test with a non-record type
 UnionType unionType = new UnionType(registry, JSType.VOID, JSType.NUMBER);
 assertFalse(recordType.isSubtype(unionType));
 }

 @Test
 public void testGetGreatestSubtype() {
 RecordType recordType = (RecordType) registry.getTypeByName("{prop1: int, prop2: string}");
 // Test with an empty record type
 ObjectType emptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(emptyRecord, recordType.getGreatestSubtype(emptyRecord));
 // Test with the same record type
 assertEquals(recordType, recordType.getGreatestSubtype(recordType));
 // Test with a subrecord type
 RecordType subRecord = new RecordType(registry, Maps.newHashMap());
 subRecord.setName("{prop1: int, prop3: boolean}");
 assertEquals(subRecord, recordType.getGreatestSubtype(subRecord));
 // Test with a superrecord type
 RecordType superRecord = new RecordType(registry, Map.of("prop1", Integer.TYPE, "prop3", Boolean.TYPE));
 assertEquals(superRecord, recordType.getGreatestSubtype(superRecord));
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = (RecordType) registry.getTypeByName("{prop1: int, prop2: string}");
 // Test defining a property that already exists
 Node propertyNode = Node.newProperty(new Text("prop1"), JSType.NUMBER);
 assertFalse(recordType.defineProperty("prop1", Integer.TYPE, false, propertyNode));
 // Test defining a new property
 propertyNode = Node.newProperty(new Text("prop3"), JSType.BOOLEAN);
 assertTrue(recordType.defineProperty("prop3", Boolean.TYPE, false, propertyNode));
 }
}