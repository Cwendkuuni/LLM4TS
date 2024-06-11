import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeWithDeclaredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties, true);

    // Create an instance of the record type and set its properties.
    RecordObject recordObject = new RecordObject(registry, recordType);
    recordObject.setProperty("name", "John Doe");
    recordObject.setProperty("age", 30);

    // Verify that the properties have the correct types.
    assertEquals(registry.stringType(), recordObject.getProperty("name"));
    assertEquals(registry.numberType(), recordObject.getProperty("age"));

    // Verify that the record type has the correct properties and types.
    assertEquals(2, recordType.getPropertyCount());
    assertEquals(registry.stringType(), recordType.getPropertyType("name"));
    assertEquals(registry.numberType(), recordType.getPropertyType("age"));

    // Verify that the record type can be assigned to an instance of itself.
    RecordType recordType2 = new RecordType(registry, properties, true);
    recordType.setValue(recordType2);
    assertTrue(recordType.isSubtype(recordType2));

    // Verify that the record type is a subtype of a record type with the same properties.
    RecordType recordType3 = new RecordType(registry, properties, true);
    RecordType recordType4 = new RecordType(registry, properties, true);
    assertTrue(recordType3.isSubtype(recordType4));

    // Verify that the record type is not a subtype of a different record type.
    RecordType recordType5 = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType3.isSubtype(recordType5));

    // Verify that the record type is a subtype of a union type that includes it.
    UnionType unionType = new UnionType(registry);
    unionType.addAlternate(recordType3);
    assertTrue(recordType3.isSubtype(unionType));

    // Verify that the record type is not a subtype of a union type that does not include it.
    UnionType unionType2 = new UnionType(registry);
    assertFalse(recordType3.isSubtype(unionType2));
  }

  @Test
  public void testRecordTypeWithInferredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", null));
    properties.put("age", new RecordProperty("age", null));

    RecordType recordType = new RecordType(registry, properties, false);

    // Create an instance of the record type and set its properties.
    RecordObject recordObject = new RecordObject(registry, recordType);
    recordObject.setProperty("name", "John Doe");
    recordObject.setProperty("age", 30);

    // Verify that the properties have been inferred and have the correct types.
    assertEquals(registry.stringType(), recordObject.getProperty("name"));
    assertEquals(registry.numberType(), recordObject.getProperty("age"));

    // Verify that the record type has the correct properties and types.
    assertEquals(2, recordType.getPropertyCount());
    assertEquals(registry.stringType(), recordType.getPropertyType("name"));
    assertEquals(registry.numberType(), recordType.getPropertyType("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", null); // Should not be null

    try {
      new RecordType(registry, properties, true);
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeWithDuplicateProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("name", new RecordProperty("name", registry.stringType())); // Should not have duplicate property

    try {
      new RecordType(registry, properties, true);
    } catch (IllegalArgumentException e) {
      assertEquals("Duplicate property name 'name'", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.stringType()));
    properties.put("age", new RecordProperty("age", registry.numberType()));

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    // Verify that record types are equivalent if they have the same properties.
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    // Verify that record types are not equivalent if they have different properties.
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.stringType()));
    properties2.put("age", new RecordProperty("age", registry.numberType()));
    properties2.put("city", new RecordProperty("city", registry.stringType()));
    RecordType recordType3 = new RecordType(registry, properties2, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));

    // Verify that record types are equivalent even if their properties have different names.
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("firstName", new RecordProperty("name", registry.stringType()));
    properties3.put("lastName", new RecordProperty("age", registry.numberType()));
    RecordType recordType4 = new RecordType(registry, properties3, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType4, true));
  }
}