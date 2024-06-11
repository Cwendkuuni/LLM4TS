import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertEquals("Expected name 'RecordType' for this object", "RecordType",
        recordType.getName());
    assertEquals("Expected recordType to be a record type",
        JSType.RECORD_TYPE, recordType.getType());
    assertEquals("Expected this record type to have 2 properties", 2,
        recordType.getPropertyCount());

    assertEquals("Expected 'name' property to be present", "name",
        recordType.getOwnPropertyNames().iterator().next());
    assertEquals("Expected 'name' property to be of type String",
        String.class, recordType.getPropertyType("name"));

    assertEquals("Expected 'age' property to be present", "age",
        recordType.getOwnPropertyNames().iterator().next());
    assertEquals("Expected 'age' property to be of type Number", Number.class,
        recordType.getPropertyType("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put(null, null);

    new RecordType(registry, recordProperties, true);
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeDeclared = new RecordType(registry, recordProperties, true);
    RecordType recordTypeSynthetic = new RecordType(registry, recordProperties, false);

    assertTrue("Expected synthetic record type to be synthetic",
        recordTypeSynthetic.isSynthetic());
    assertFalse("Expected declared record type to not be synthetic",
        recordTypeDeclared.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = new java.util.HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = new java.util.HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 =
        new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, false);

    assertTrue("Expected record types to be equivalent",
        recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));

    RecordType recordType3 = new RecordType(registry, recordProperties2, true);
    assertFalse("Expected record types to be different",
        recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals("Expected implicit prototype to be Object type",
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertFalse("Expected property to not exist before defining it",
        recordType.hasProperty("color"));

    recordType.defineProperty("color", String.class, false, Node.NULL);

    assertTrue("Expected property to exist after defining it",
        recordType.hasProperty("color"));
    assertEquals("Expected property to be of type String",
        String.class, recordType.getPropertyType("color"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWhileFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    recordType.isFrozen = true;
    recordType.defineProperty("color", String.class, false, Node.NULL);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = new java.util.HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = new java.util.HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("color", new RecordProperty("color", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals("Expected greatestSubtype to be the same as the record type2",
        recordType2, greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties1 = new java.util.HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordProperties2 = new java.util.HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Number.class));
    recordProperties2.put("color", new RecordProperty("color", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    RecordType recordType3 = new RecordType(registry, recordProperties1, true);
    assertTrue("Expected recordType1 to be a subtype of recordType3",
        recordType1.isSubtype(recordType3));

    RecordType recordType4 = new RecordType(registry, recordProperties2, true);
    assertTrue("Expected recordType3 to be a subtype of recordType4",
        recordType3.isSubtype(recordType4));

    RecordType recordType5 = new RecordType(registry, recordProperties3, true);
    assertFalse("Expected recordType1 to not be a subtype of recordType5",
        recordType1.isSubtype(recordType5));
  }
}