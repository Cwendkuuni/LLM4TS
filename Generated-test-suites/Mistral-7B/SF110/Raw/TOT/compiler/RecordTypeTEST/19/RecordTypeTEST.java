import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordType;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty(String.class, "name"));
    recordProperties.put("age", new RecordProperty(Integer.class, "age"));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    // Testing isRecordType method
    assertTrue(recordType.isRecordType());

    // Testing isSubtype method
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(objectType.isSubtype(recordType));

    // Testing getImplicitPrototype method
    assertEquals(objectType, recordType.getImplicitPrototype());

    // Testing defineProperty method
    recordType.defineProperty("address", String.class, false, null);
    assertFalse(recordType.hasProperty("address"));

    // Testing getPropertyType method
    assertEquals(String.class, recordType.getPropertyType("name"));

    // Testing getOwnPropertyNames method
    Set<String> propertyNames = recordType.getOwnPropertyNames();
    assertEquals(2, propertyNames.size());
    assertTrue(propertyNames.contains("name"));
    assertTrue(propertyNames.contains("age"));

    // Testing isFrozen property
    assertFalse(recordType.isFrozen());

    // Testing defining a property in a frozen record
    try {
      recordType.defineProperty("newProperty", Integer.class, false, null);
      fail("Should not be able to define a property in a frozen record.");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new java.util.HashMap<>();
    recordProperties.put("name", new RecordProperty(String.class, "name"));
    recordProperties.put("age", new RecordProperty(Integer.class, "age"));

    RecordType recordType1 = new RecordType(registry, recordProperties, true);
    RecordType recordType2 = new RecordType(registry, recordProperties, true);

    // Testing checkRecordEquivalenceHelper method
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.LAX));

    RecordType frozenRecordType = new RecordType(registry, recordProperties, true);
    RecordType mutableRecordType = new RecordType(registry, recordProperties, false);

    // Testing checkRecordEquivalenceHelper method with frozen and mutable records
    assertFalse(frozenRecordType.checkRecordEquivalenceHelper(mutableRecordType, EquivalenceMethod.STRICT));
    assertTrue(frozenRecordType.checkRecordEquivalenceHelper(mutableRecordType, EquivalenceMethod.LAX));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = new java.util.HashMap<>();
    recordProperties1.put("name",
        new RecordProperty(String.class, "name"));
    recordProperties1.put("age", new RecordProperty(Integer.class, "age"));

    Map<String, RecordProperty> recordProperties2 = new java.util.HashMap<>();
    recordProperties2.put("name", new RecordProperty(String.class, "name"));
    recordProperties2.put("age", new RecordProperty(Integer.class, "age"));
    recordProperties2.put("city", new RecordProperty(String.class, "city"));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Testing greatestSubtypeHelper method
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(greategstSubtype);
    assertTrue(recordType1.isSubtype(greategstSubtype));
    assertTrue(recordType2.isSubtype(greategstSubtype));

    // Testing greatestSubtypeHelper method with incompatible records
    JSType incompatibleRecordType = new UnionType(registry, new ObjectType[0]);
    greatestSubtype = recordType1.getGreatestSubtypeHelper(incompatibleRecordType);
    assertNull(greategstSubtype);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties1 = new java.util.HashMap<>();
    recordProperties1.put("name", new RecordProperty(String.class, "name"));
    recordProperties1.put("age", new RecordProperty(Integer.class, "age"));

    Map<String, RecordProperty> recordProperties2 = new java.util.HashMap<>();
    recordProperties2.put("name", new RecordProperty(String.class, "name"));
    recordProperties2.put("age", new RecordProperty(Integer.class, "age"));
    recordProperties2.put("city", new RecordProperty(String.class, "city"));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    // Testing isSubtype method
    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    // Testing isSubtype method with native types
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(objectType.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(objectType));

    // Testing isSubtype method with empty record
    RecordType emptyRecordType = new RecordType(registry, new java.util.HashMap<>(), true);
    assertTrue(emptyRecordType.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(emptyRecordType));
  }
}