import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeSubtypeTester;
import com.google.javascript.rhino.testing.Values;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    // When: Creating a record type from the properties
    RecordType recordType = new RecordType(registry, properties);

    // Then: The record type has the expected properties
    assertEquals("{a: A, b: B}", recordType.toString());
    assertEquals(2, recordType.getPropertyCount());
    assertEquals(aType, recordType.getPropertyType("a"));
    assertEquals(bType, recordType.getPropertyType("b"));
  }

  @Test
  public void testRecordTypeEquivalence() {
    // Given: Two record types with the same properties
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", aType));
    propertiesA.put("b", new RecordProperty("b", bType));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap(propertiesA);

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    // When: Testing the equivalence of the record types
    TypeEquivalenceTester tester = new TypeEquivalenceTester();
    assertTrue(tester.assertTypeEquivalence(recordTypeA, recordTypeB));
  }

  @Test
  public void testRecordTypeSubtype() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: A record type with the same properties, but in a different order
    Map<String, RecordProperty> propertiesReordered = Maps.newHashMap(properties);
    propertiesReordered.remove("a");
    propertiesReordered.remove("b");
    propertiesReordered.put("b", properties.get("b"));
    propertiesReordered.put("a", properties.get("a"));

    RecordType recordTypeReordered = new RecordType(registry, propertiesReordered);

    // When: Testing the subtype relationship between the record types
    TypeSubtypeTester tester = new TypeSubtypeTester();
    assertTrue(tester.assertSubtype(recordType, recordTypeReordered));
  }

  @Test
  public void testRecordTypeSubtypeWithDifferentPropertyOrder() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B",
        new JSType.RecordType(registry, Map.of("a", new JSType.UnknownType(), "b", new JSType.UnknownType())));
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: A record type with the same properties, but in a different order
    Map<String, RecordProperty> propertiesReordered = Maps.newHashMap(properties);
    propertiesReordered.remove("a");
    propertiesReordered.remove("b");
    propertiesReordered.put("b", new RecordProperty("b", properties.get("b")));
    propertiesReordered.put("a", new RecordProperty("a", properties.get("a")));

    RecordType recordTypeReordered = new RecordType(registry, propertiesReordered);

    // When: Testing the subtype relationship between the record types
    assertFalse(tester.assertSubtype(recordType, recordTypeReordered));
  }

  @Test
  public void testRecordTypeSubtypeWithDifferentPropertyTypes() {
    // Given: A record type with properties a and b of type A and B, respectively
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: A record type with the same properties, but with property b of type C instead
    Map<String, RecordProperty> propertiesWithC = Maps.newHashMap(properties);
    propertiesWithC.put("b", new RecordProperty("b", registry.defineType("C", new JSType.UnknownType())));

    RecordType recordTypeWithC = new RecordType(registry, propertiesWithC);

    // When: Testing the subtype relationship between the record types
    assertFalse(tester.assertSubtype(recordType, recordTypeWithC));
  }

  @Test
  public void testRecordTypeSubtypeWithEmptyRecordType() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: The empty record type
    RecordType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(JSType.EMPTY);

    // When: Testing the subtype relationship between the record types
    assertFalse(tester.assertSubtype(recordType, emptyRecordType));
  }

  @Test
  public void testRecordTypeSubtypeWithIntersectionType() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: A type that is the intersection of record type and another type
    UnionType intersectionType = new UnionTypeBuilder(registry)
        .addAlternate(recordType)
        .addAlternate(registry.getNativeType(JSTypeNative.NUMBER_TYPE))
        .build();

    // When: Testing the subtype relationship between the record type and the intersection type
    assertTrue(tester.assertSubtype(recordType, intersectionType));
  }

  @Test
  public void testRecordTypeToString() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // When: Calling the toString method on the record type
    String recordTypeString = recordType.toString();

    // Then: The string representation of the record type is correct
    assertEquals("{a: A, b: B}", recordTypeString);
  }

  @Test
  public void testRecordTypeGetProperty() {
    // Given: A record type with properties a and b
    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeType aType = registry.defineType("A", new JSType.UnknownType());
    JSTypeType bType = registry.defineType("B", new JSType.UnknownType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", aType));
    properties.put("b", new RecordProperty("b", bType));

    RecordType recordType = new RecordType(registry, properties);

    // Given: A value of the record type
    Object recordValue = Values.createRecord(recordType, "a", Values.NULL, "b", Values.TRUE);

    // When: Calling the getProperty method on the record type with property name "a"
    JSType propertyTypeA = recordType.getPropertyType("a");

    // Then: The property type of property "a" is correct
    assertEquals(aType, propertyTypeA);

    // When: Calling the getProperty method on the record type with property name "c"
    JSType propertyTypeC = recordType.getPropertyType("c");

    // Then: The property type of property "c" is equal to the NO_TYPE type
    assertEquals(JSTypeNative.NO_TYPE, propertyTypeC);
  }
}