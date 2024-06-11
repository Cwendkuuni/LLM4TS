import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeDeclaration() {
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);

    assertEquals("RecordType", recordType.getName().getValue(),
        recordType.getClass().getSimpleName());
    assertEquals(RecordType.class, recordType.getClass());

    assertEquals(1, recordType.getOwnProperties().length);
    assertEquals("name", recordType.getOwnProperties()[0].getName().getValue());
    assertEquals(String.class, recordType.getOwnProperties()[0].getType());

    Object prototype = recordType.getImplicitPrototype();
    assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
  }

  @Test
  public void testRecordTypeSynthesis() {
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, properties, false);
    assertNotNull(recordType);

    assertEquals("RecordType", recordType.getName().getValue(),
        recordType.getClass().getSimpleName());
    assertEquals(RecordType.class, recordType.getClass());

    assertEquals(1, recordType.getOwnProperties().length);
    assertEquals("name", recordType.getOwnProperties()[0].getName().getValue());
    assertEquals(String.class, recordType.getOwnProperties()[0].getType());

    Object prototype = recordType.getImplicitPrototype();
    assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);

    Object instance = registry.newInstance(recordType);
    assertNotNull(instance);
    assertEquals(recordType, instance.getClass());
    assertEquals(1, instance.getPropertyNames().length);
    assertEquals("name", instance.getPropertyNames()[0]);
    assertEquals(String.class, instance.getClass().getPropertyType(
        "name"));
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> property1 = new java.util.HashMap<>();
    property1.put("name", new RecordProperty("name", String.class));
    RecordType recordType1 = new RecordType(registry, property1, true);

    Map<String, RecordProperty> property2 = new java.util.HashMap<>();
    property2.put("name", new RecordProperty("name", String.class));
    RecordType recordType2 = new RecordType(registry, property2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.BY_VALUE));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2,
        EquivalenceMethod.BY_REFERENCE));

    // Different property order
    Map<String, RecordProperty> property3 = new java.util.HashMap<>();
    property3.put("name", new RecordProperty("name", String.class));
    property3.put("age", new RecordProperty("age", Number.class));
    RecordType recordType3 = new RecordType(registry, property3, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3,
        EquivalenceMethod.BY_VALUE));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3,
        EquivalenceMethod.BY_REFERENCE));

    // Different property types
    Map<String, RecordProperty> property4 = new java.util.HashMap<>();
    property4.put("name", new RecordProperty("name", String.class));
    property4.put("age", new RecordProperty("age", Boolean.class));
    RecordType recordType4 = new RecordType(registry, property4, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4,
        EquivalenceMethod.BY_VALUE));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4,
        EquivalenceMethod.BY_REFERENCE));
  }

  @Test
  public void testRecordTypeSubtype() {
    Map<String, RecordProperty> property1 = new java.util.HashMap<>();
    property1.put("name", new RecordProperty("name", String.class));
    RecordType recordType1 = new RecordType(registry, property1, true);

    Map<String, RecordProperty> property2 = new java.util.HashMap<>();
    property2.put("name", new RecordProperty("name", String.class));
    property2.put("age", new RecordProperty("age", Number.class));
    RecordType recordType2 = new RecordType(registry, property2, true);

    RecordType subtype = recordType1.getGreatestSubtype(recordType2);
    assertNotNull(subtype);
    assertEquals(recordType2, subtype);

    Map<String, RecordProperty> property3 = new java.util.HashMap<>();
    property3.put("name", new RecordProperty("name", String.class));
    property3.put("age", new RecordProperty("age", Number.class));
    property3.put("gender", new RecordProperty("gender", String.class));
    RecordType recordType3 = new RecordType(registry, property3, true);

    subtype = recordType1.getGreatestSubtype(recordType3);
    assertNotNull(subtype);
    assertEquals(recordType3, subtype);
  }

  @Test
  public void testRecordTypeFreeze() {
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("name", String.class, false, null);
      assertFalse(true);
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    try {
      recordType.freeze();
      assertFalse(true);
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> property1 = new java.util.HashMap<>();
    property1.put("name", new RecordProperty("name", String.class));
    RecordType recordType1 = new RecordType(registry, property1, true);

    Map<String, RecordProperty> property2 = new java.util.HashMap<>();
    property2.put("name", new RecordProperty("name", String.class));
    property2.put("age", new RecordProperty("age", Number.class));
    RecordType recordType2 = new RecordType(registry, property2, true);

    assertFalse(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> property3 = new java.util.HashMap<>();
    property3.put("name", new RecordProperty("name", String.class));
    property3.put("age", new RecordProperty("age", Number.class));
    property3.put("gender", new RecordProperty("gender", String.class));
    RecordType recordType3 = new RecordType(registry, property3, true);

    RecordType subtype = new RecordType(registry, property1, false);
    assertTrue(recordType1.isSubtype(subtype));

    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType1.isSubtype(recordType3));

    assertTrue(recordType2.isSubtype(recordType3));
  }
}