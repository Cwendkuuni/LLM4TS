import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructorWithDeclaredProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(registry.getObjectType(), recordType.getImplicitPrototype());
    assertEquals(2, recordType.getOwnPropertyNames().size());
  }

  @Test
  public void testRecordTypeConstructorWithUndefinedRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    try {
      new RecordType(registry, properties, false);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @Test
  public void testRecordTypeDefinePropertyWhenFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("name", String.class, false, null));
  }

  @Test
  public void testRecordTypeIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testRecordTypeCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeCheckRecordEquivalenceHelperWithDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new java.util.LinkedHashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = new java.util.LinkedHashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, RecordType.EquivalenceMethod.IDENTITY));
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with empty record type
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE).getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test with Object type as that
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getGreatestSubtypeHelper(registry.getNativeRecordType(new RecordProperty[]{})));

    // Test with a record and a non-record type
    RecordType recordType = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    JSType otherType = registry.getNativeIntType();
    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
        recordType.getGreatestSubtypeHelper(otherType));

    // Test with two record types with the same properties
    RecordType recordType1 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType recordType2 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(recordType1, greatestSubtype);

    // Test with two record types with different properties
    RecordType recordType3 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType recordType4 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("gender", new RecordProperty("gender", String.class));
    }}, true);

    greatestSubtype = recordType3.getGreatestSubtypeHelper(recordType4);

    assertEquals(recordType3, greatestSubtype);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test with empty record type and Object type
    assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test with RecordType and ObjectType
    RecordType recordType = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test with RecordType and another RecordType with the same properties
    RecordType recordType1 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType recordType2 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    assertTrue(recordType1.isSubtype(recordType2));

    // Test with RecordType and another RecordType with different properties
    RecordType recordType3 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType recordType4 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("gender", new RecordProperty("gender", String.class));
    }}, true);

    assertFalse(recordType3.isSubtype(recordType4));

    // Test with RecordType and RecordType with one property as subtype of another
    RecordType recordType5 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Integer.class));
    }}, true);

    RecordType recordType6 = new RecordType(registry, new java.util.LinkedHashMap<>(){{
        put("name", new RecordProperty("name", String.class));
        put("age", new RecordProperty("age", Int32Union.class));
    }}, true);

    assertTrue(recordType5.isSubtype(recordType6));
  }
}