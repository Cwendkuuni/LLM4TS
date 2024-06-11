import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("prop1", recordProperty1);
    recordProperties.put("prop2", recordProperty2);

    RecordType expectedRecordType = new RecordType(registry, recordProperties);
    RecordType actualRecordType = new RecordType(registry, recordProperties);

    assertEquals(expectedRecordType.toString(), actualRecordType.toString());
  }

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", recordProperty1);
    recordProperties1.put("prop2", recordProperty2);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", recordProperty1);
    recordProperties2.put("prop2", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);
    RecordProperty recordProperty3 = new RecordProperty("prop3", Boolean.TYPE);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", recordProperty1);
    recordProperties1.put("prop2", recordProperty2);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", recordProperty1);
    recordProperties2.put("prop2", recordProperty2);
    recordProperties2.put("prop3", recordProperty3);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", recordProperty1);
    recordProperties1.put("prop2", recordProperty2);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", recordProperty1);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);

    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", recordProperty1);
    recordProperties1.put("prop2", recordProperty2);

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", recordProperty1);
    recordProperties2.put("prop3", new RecordProperty("prop3", Boolean.TYPE));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    JSType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with an empty record type.
    JSType recordType1 = new RecordType(registry, Maps.newHashMap());
    JSType recordType2 = new RecordType(registry, Maps.newHashMap());
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    // Test with a record type and an object type.
    RecordProperty recordProperty = new RecordProperty("prop", Integer.TYPE);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("prop", recordProperty);
    RecordType recordType = new RecordType(registry, recordProperties);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType greatestSubtypeObject = recordType.getGreatestSubtypeHelper(objectType);
    assertEquals(objectType, greatestSubtypeObject);

    // Test with two record types with a common property.
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
    RecordType recordType1 = new RecordType(registry, recordProperties1);
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", new RecordProperty("prop1", String.class));
    recordProperties2.put("prop2", new RecordProperty("prop2", Boolean.TYPE));
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    JSType greatestSubtypeRecord = recordType1.getGreatestSubtypeHelper(recordType2);
    JSType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("prop1", Integer.TYPE)
        .build();
    assertEquals(expectedRecordType, greatestSubtypeRecord);
  }

  @Test
  public void testGetGreatestSubtypeHelperWithIncompatiblePropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordProperty recordProperty1 = new RecordProperty("prop1", Integer.TYPE);
    RecordProperty recordProperty2 = new RecordProperty("prop2", String.class);
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("prop1", recordProperty1);
    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("prop1", recordProperty1);
    recordProperties2.put("prop2", recordProperty2);

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty = new RecordProperty("prop", Integer.TYPE);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("prop", recordProperty);

    RecordType recordType = new RecordType(registry, recordProperties);

    // Test defining a property when the type is not frozen.
    Object propertyNode = new Object();
    boolean result = recordType.defineProperty("prop", Integer.TYPE, false, propertyNode);
    assertTrue(result);

    // Test defining a property when the type is frozen.
    recordType.isFrozen = true;
    result = recordType.defineProperty("prop", Integer.TYPE, false, propertyNode);
    assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordProperty recordProperty = new RecordProperty("prop", Integer.TYPE);
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("prop", recordProperty);

    RecordType recordType = new RecordType(registry, recordProperties);
    JSType resolveResult = recordType.resolveInternal(null, null);
    assertEquals(recordType, resolveResult);
  }
}