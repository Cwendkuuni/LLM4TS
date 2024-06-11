import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.isSubtype;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  @org.junit.Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();

    RecordProperty recordProperty1 = new RecordProperty("prop1", registry.stringType());
    RecordProperty recordProperty2 = new RecordProperty("prop2", registry.numberType());
    properties.put("prop1", recordProperty1);
    properties.put("prop2", recordProperty2);

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType", recordType.getClass().getSimpleName());

    assertEquals(false, recordType.isFrozen());
    RecordProperty recordProperty3 = new RecordProperty("prop3", registry.booleanType());
    recordType.defineProperty("prop3", registry.booleanType(), false, null);
    assertTrue(recordType.isFrozen());

    RecordType frozenRecordType = new RecordType(registry, properties, true);
    // Freeze the record type again
    frozenRecordType.isFrozen = true;

    // Verify that RecordType is not equal to itself after freezing
    assertFalse(recordType.equals(frozenRecordType));
  }

  @org.junit.Test
  public void testRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.stringType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.WEAK));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));

    // Differing properties
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.numberType()));

    RecordType recordType3 = new RecordType(registry, properties3, true);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.WEAK));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));

    // Differing types
    RecordProperty recordProperty = new RecordProperty("prop1", registry.numberType());
    RecordType recordType4 = new RecordType(registry, properties1, true);
    recordType4.defineProperty("prop1", registry.numberType(), false, null);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTICAL));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.WEAK));
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.STRICT));
  }

  @org.junit.Test
  public void testRecordSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    RecordType recordType3 = new RecordType(registry, Collections.emptyMap(), true);
    assertTrue(isSubtype(recordType1, recordType2));
    assertTrue(isSubtype(recordType1, recordType3));
    assertTrue(isSubtype(recordType2, recordType1));
    assertFalse(isSubtype(recordType2, recordType3));

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties3.put("prop3", new RecordProperty("prop3", registry.booleanType()));
    RecordType recordType4 = new RecordType(registry, properties3, true);

    assertFalse(isSubtype(recordType1, recordType4));
  }

  @org.junit.Test
  public void testRecordIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test base case
    RecordType recordType1 = new RecordType(registry, Collections.emptyMap(), true);
    assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test non-record type
    JSType nonRecordType = registry.getNativeType(JSTypeNative.NO_TYPE);
    assertFalse(recordType1.isSubtype(nonRecordType));

    // Test record type with fewer properties
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType2 = new RecordType(registry, properties1, true);
    assertTrue(recordType2.isSubtype(recordType1));

    // Test record type with more properties
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType3 = new RecordType(registry, properties2, true);
    assertFalse(recordType3.isSubtype(recordType1));

    // Test record types with differing property types
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType4 = new RecordType(registry, properties3, true);
    RecordType recordType5 = new RecordType(registry, properties1, true);
    assertFalse(recordType4.isSubtype(recordType5));
  }

  @org.junit.Test
  public void testRecordGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties2.put("prop2", new RecordProperty("prop2", registry.numberType()));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("prop1", new RecordProperty("prop1", registry.stringType()));
    properties3.put("prop2", new RecordProperty("prop2", registry.numberType()));
    properties3.put("prop3", new RecordProperty("prop3", registry.booleanType()));
    RecordType recordType3 = new RecordType(registry, properties3, true);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType2, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType2, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtypeHelper(recordType1);
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType3, greatestSubtype);
  }

  @org.junit.Test
  public void testRecordGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", new RecordProperty("prop1", registry.stringType()));
    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }
}