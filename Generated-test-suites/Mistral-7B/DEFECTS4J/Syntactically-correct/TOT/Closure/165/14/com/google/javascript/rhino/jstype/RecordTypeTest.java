import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

 private static final JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testRecordTypeCreation() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 RecordProperty prop1 = new RecordProperty("prop1", Integer.TYPE);
 RecordProperty prop2 = new RecordProperty("prop2", String.class);
 RecordProperty prop3 = new RecordProperty("prop3", Double.TYPE);
 builder.addProperty("prop1", prop1.getType(), prop1.getPropertyNode());
 builder.addProperty("prop2", prop2.getType(), prop2.getPropertyNode());
 builder.addProperty("prop3", prop3.getType(), prop3.getPropertyNode());
 RecordType recordType = builder.build();

 // Test record type properties
 assertEquals("RecordType with 3 properties", 3, recordType.getProperties().size());
 assertEquals("RecordType property type check 1", Integer.TYPE, recordType.getPropertyType("prop1"));
 assertEquals("RecordType property type check 2", String.class, recordType.getPropertyType("prop2"));
 assertEquals("RecordType property type check 3", Double.TYPE, recordType.getPropertyType("prop3"));
 }

 @Test
 public void testGetGreatestSubtype() {
 // Create test record types
 RecordType recordType1 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE), "prop2", new RecordProperty("prop2", String.class)));

 // Test getGreatestSubtype
 JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
 assertEquals("getGreatestSubtype: recordType1 is subtype of recordType2", recordType1, greatestSubtype);

 // Test getGreatestSubtype with conflicting properties
 RecordType recordType3 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE), "prop2", new RecordProperty("prop2", Double.TYPE)));
 greatestSubtype = recordType1.getGreatestSubtype(recordType3);
 assertNotNull("getGreatestSubtype: NO_TYPE should not be returned", greatestSubtype);
 assertFalse("getGreatestSubtype: recordType1 should not be equal to NO_TYPE", greatestSubtype.isEquivalentTo(JSTypeNative.NO_TYPE));
 }

 @Test
 public void testIsSubtype() {
 // Test isSubtype with equal record types
 RecordType recordType1 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE)));
 assertTrue("IsSubtype: recordType1 is subtype of recordType2", recordType1.isSubtype(recordType2));

 // Test isSubtype with different record types
 RecordType recordType3 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE), "prop2", new RecordProperty("prop2", String.class)));
 assertFalse("IsSubtype: recordType1 is not subtype of recordType3", recordType1.isSubtype(recordType3));

 // Test isSubtype with super record type
 RecordType superRecord = new RecordType(registry, Map.of());
 RecordType recordType4 = new RecordType(registry, Map.of("prop1", new RecordProperty("prop1", Integer.TYPE), "prop2", new RecordProperty("prop2", String.class)));
 assertTrue("IsSubtype: recordType4 is subtype of superRecord", recordType4.isSubtype(superRecord));
 }
}