import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertInstanceOf(RecordType.class, recordType);
    assertEquals("RecordType", recordType.getName());
    assertNotNull(recordType.getProperties());
    assertFalse(recordType.isFrozen());

    SortedMap<String, JSType> propertiesMap = recordType.getProperties();
    assertEquals(2, propertiesMap.size());
    assertEquals(String.class, propertiesMap.get("name"));
    assertEquals(Number.class, propertiesMap.get("age"));

    RecordType recordType2 = new RecordType(registry, properties, false);
    assertInstanceOf(RecordType.class, recordType2);
    assertEquals("RecordType_synthetic", recordType2.getName());
    assertEquals(recordType.getProperties(), recordType2.getProperties());
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test subtyping with same properties
    RecordType recordType1 = createRecordType(registry, "RecordType1",
        Map.of("name", String.class, "age", Number.class));
    RecordType recordType2 = createRecordType(registry, "RecordType2",
        Map.of("name", String.class, "age", Number.class));
    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));

    // Test subtyping with different property order
    RecordType recordType3 = createRecordType(registry, "RecordType3",
        Map.of("age", Number.class, "name", String.class));
    assertTrue(recordType1.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType1));

    // Test subtyping with additional properties
    RecordType recordType4 = createRecordType(registry, "RecordType4",
        Map.of("name", String.class, "age", Number.class, "city", String.class));
    assertTrue(recordType1.isSubtype(recordType4));

    // Test subtyping with missing properties
    RecordType recordType5 = createRecordType(registry, "RecordType5",
        Map.of("age", Number.class));
    assertFalse(recordType1.isSubtype(recordType5));

    // Test subtyping with incompatible property types
    RecordType recordType6 = createRecordType(registry, "RecordType6",
        Map.of("name", Number.class, "age", String.class));
    assertFalse(recordType1.isSubtype(recordType6));

    // Test subtyping with null properties
    RecordType recordType7 = createRecordType(registry, "RecordType7",
        Map.of("name", null, "age", Number.class));
    assertFalse(recordType1.isSubtype(recordType7));
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType1 = createRecordType(registry, "RecordType1",
        Map.of("name", String.class, "age", Number.class));
    RecordType recordType2 = createRecordType(registry, "RecordType2",
        Map.of("age", Number.class, "name", String.class));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    RecordType recordType3 = createRecordType(registry, "RecordType3",
        Map.of("name", String.class, "age", Number.class));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = createRecordType(registry, "RecordType",
        Map.of("name", String.class, "age", Number.class));
    recordType.freeze();

    assertFalse(recordType.isFrozen());
    recordType.freeze();
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("name", String.class, false, null);
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    try {
      recordType.getPropertyType("name");
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  private RecordType createRecordType(JSTypeRegistry registry,
      String name, Map<String, RecordProperty> properties) {
    return new RecordType(registry, properties, true);
  }
}