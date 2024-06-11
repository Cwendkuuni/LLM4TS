import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUpClass() {
    registry = new JSTypeRegistry();
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.stringType()));
    properties.put("property2", new RecordProperty("property2", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
    assertEquals("Object", recordType.getName());
    assertTrue(recordType.isRecordType());

    Set<String> propertyNames = recordType.getOwnPropertyNames();
    assertEquals(2, propertyNames.size());
    assert(propertyNames.containsAll(Arrays.asList("property1", "property2")));

    JSType property1Type = recordType.getPropertyType("property1");
    assertEquals(registry.stringType(), property1Type);

    JSType property2Type = recordType.getPropertyType("property2");
    assertEquals(registry.numberType(), property2Type);
  }

  @org.junit.Test
  public void testRecordTypeCreationFrozen() {
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.stringType()));
    properties.put("property2", new RecordProperty("property2", registry.numberType()));

    RecordType recordType = new RecordType(registry, properties, false);
    assertNotNull(recordType);
    assertEquals("Object", recordType.getName());
    assertTrue(recordType.isRecordType());

    Set<String> propertyNames = recordType.getOwnPropertyNames();
    assertEquals(2, propertyNames.size());
    assert(propertyNames.containsAll(Arrays.asList("property1", "property2")));

    try {
      recordType.defineProperty("property1", registry.booleanType(), false, Node.NULL);
      fail("Expected defineProperty to fail when the record is frozen.");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("property1", new RecordProperty("property1", registry.stringType()));
    properties1.put("property2", new RecordProperty("property2", registry.numberType()));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("property1", new RecordProperty("property1", registry.stringType()));
    properties2.put("property2", new RecordProperty("property2", registry.numberType()));
    properties2.put("property3", new RecordProperty("property3", registry.booleanType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, false);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRUCTURAL));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, new java.util.LinkedHashMap<>(), true);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @org.junit.Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType typeBefore = recordType.getPropertyType("property1");
    assertEquals(registry.stringType(), typeBefore);

    JSType typeAfter = recordType.defineProperty("property1", registry.numberType(), false, Node.NULL);
    assertEquals(registry.numberType(), typeAfter);
    // The new property value should not be accessible via old property name
    assertNull(recordType.getPropertyType("property1"));
    assertEquals(registry.numberType(), recordType.getPropertyType("property1_new"));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties, true);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType, greatestSubtype);

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("property1", new RecordProperty("property1", registry.stringType()));
    properties2.put("property2", new RecordProperty("property2", registry.numberType()));

    RecordType recordType2 = new RecordType(registry, properties2, true);
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);

    // Both records should be equivalent
    assertTrue(recordType.isEquivalentTo(recordType2));
    assertEquals(recordType, greatestSubtype);

    Map<String, RecordProperty> properties3 = new java.util.LinkedHashMap<>();
    properties3.put("property1", new RecordProperty("property1", registry.stringType()));
    properties3.put("property2", new RecordProperty("property2", registry.numberType()));
    properties3.put("property3", new RecordProperty("property3", registry.booleanType()));

    RecordType recordType3 = new RecordType(registry, properties3, true);
    greatestSubtype = recordType.getGreatestSubtypeHelper(recordType3);

    // RecordType3 is a subtype of RecordType1 as it has all the same properties
    assertTrue(recordType3.isSubtype(recordType));
    // But it is not a supertype as it has an additional property
    assertFalse(recordType.isSubtype(recordType3));
    // RecordType1 is not a supertype of RecordType3
    assertFalse(recordType3.isSubtype(recordType));
    // The greatest common subtype is empty
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeSubtype() {
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", registry.stringType()));

    RecordType recordType = new RecordType(registry, properties, true);
    RecordType subType = new RecordType(registry, properties, false);

    assertTrue(recordType.isSubtype(subType));
  }
}