import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeWithSingleProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test property getters
    assertEquals(Integer.TYPE, recordType.getPropertyType("prop1"));

    // Test property setters
    recordType.setProperty("prop1", String.class.getName());
    assertEquals(String.class.getName(), recordType.getPropertyType("prop1"));

    // Test property checking
    assertTrue(recordType.hasProperty("prop1"));
    assertFalse(recordType.hasProperty("nonExistentProperty"));

    // Test property assignment
    RecordType otherRecord = new RecordType(registry, properties, true);
    recordType = otherRecord;

    // Test record equivalence
    RecordType anotherRecord = new RecordType(registry, properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(anotherRecord, true));

    // Test record subtyping
    RecordType subTypeRecord = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType.isSubtype(subTypeRecord));

    RecordType subPropertyTypeRecord = new RecordType(registry,
        Maps.newHashMap("prop1", new RecordProperty("prop1", Integer.TYPE)), true);
    assertTrue(recordType.isSubtype(subPropertyTypeRecord));

    RecordType subPropertyTypeRecordWithString = new RecordType(registry,
        Maps.newHashMap("prop1", new RecordProperty("prop1", String.class)), true);
    assertTrue(recordType.isSubtype(subPropertyTypeRecordWithString));

    RecordType subPropertyTypeRecordWithNull = new RecordType(registry,
        Maps.newHashMap("prop1", new RecordProperty("prop1", null)), true);
    assertTrue(recordType.isSubtype(subPropertyTypeRecordWithNull));

    RecordType subPropertyTypeRecordWithIncompatibleType = new RecordType(registry,
        Maps.newHashMap("prop1", new RecordProperty("prop1", Double.TYPE)), true);
    assertFalse(recordType.isSubtype(subPropertyTypeRecordWithIncompatibleType));

    // Test record synthesis
    RecordType synthesizedRecord = new RecordType(registry, Maps.newHashMap(), false);
    assertTrue(synthesizedRecord.isSynthetic());

    // Test record freezing
    recordType.freeze();
    assertFalse(recordType.isFrozen());
    synthesizedRecord.freeze();
    assertTrue(synthesizedRecord.isFrozen());

    // Test record property definition
    recordType.defineProperty("prop2", String.class, false, null);
    assertEquals(String.class, recordType.getPropertyType("prop2"));

    // Test record property access
    RecordType recordWithAccessors = new RecordType(registry, properties, true);
    recordWithAccessors.defineProperty("prop1", Integer.TYPE, true, null);
    Object o = new Object() {{
      RecordType record = recordWithAccessors;
    }};
    assertEquals(0, o.getProperty("prop1"));
    o.setProperty("prop1", 1);
    assertEquals(1, o.getProperty("prop1"));

    // Test record property inheritance
    RecordType baseRecord = new RecordType(registry, properties, true);
    RecordType derivedRecord = new RecordType(registry,
        Maps.newHashMap(properties.entrySet().toArray(new Map.Entry[0][])),
        true);
    derivedRecord.defineProperty("prop2", String.class, false, null);
    Object base = new Object() {{
      RecordType record = baseRecord;
    }};
    Object derived = new Object() {{
      RecordType record = derivedRecord;
    }};
    base.setProperty("prop1", 1);
    derived.setProperty("prop1", 1);
    derived.setProperty("prop2", "derived");
    assertEquals(1, base.getProperty("prop1"));
    assertEquals(1, derived.getProperty("prop1"));
    assertEquals("derived", derived.getProperty("prop2"));

    // Test record property inheritance with incompatible types
    RecordType incompatibleRecord = new RecordType(registry,
        Maps.newHashMap("prop1", new RecordProperty("prop1", Integer.TYPE)), true);
    RecordType derivedIncompatibleRecord = new RecordType(registry,
        Maps.newHashMap(
            "prop1", new RecordProperty("prop1", String.class),
            "prop2", new RecordProperty("prop2", Integer.TYPE)), true);
    Object baseIncompatible = new Object() {{
      RecordType record = incompatibleRecord;
    }};
    Object derivedIncompatible = new Object() {{
      RecordType record = derivedIncompatibleRecord;
    }};
    baseIncompatible.setProperty("prop1", 1);
    derivedIncompatible.setProperty("prop1", "string");
    try {
      derivedIncompatible.setProperty("prop2", 1);
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}