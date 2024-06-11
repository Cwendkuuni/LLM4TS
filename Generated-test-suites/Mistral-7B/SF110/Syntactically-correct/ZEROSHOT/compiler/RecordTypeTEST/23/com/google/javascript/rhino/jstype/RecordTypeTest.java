import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getName(), "AnonymousRecordType");
    assertTrue(recordType.isDeclared());
    assertFalse(recordType.isFrozen());

    assertEquals(recordType.getGreatestSubtypeHelper(ObjectType.INSTANCE),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    RecordType frozenRecordType = new RecordType(registry, properties, false);
    assertFalse(frozenRecordType.isDeclared());
    assertFalse(frozenRecordType.isFrozen());
    assertTrue(frozenRecordType.isSynthetic());

    assertEquals(frozenRecordType.getGreatestSubtypeHelper(ObjectType.INSTANCE),
        recordType);
  }

  @Test
  public void testIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    RecordType frozenRecordType = new RecordType(registry, properties, false);
    assertFalse(frozenRecordType.isFrozen());
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isSynthetic());

    RecordType syntheticRecordType = new RecordType(registry, properties, false);
    assertTrue(syntheticRecordType.isSynthetic());
  }

  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(recordType.getPropertyType("name"), StringType.INSTANCE);
    assertEquals(recordType.getPropertyType("age"), NumberType.INT_TYPE);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", null);
    new RecordType(registry, properties, true);
  }

  @Test
  public void testGetOwnPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    Set<String> propertyNames = recordType.getOwnPropertyNames();
    assertEquals(propertyNames.size(), 2);
    assertTrue(propertyNames.containsAll(Arrays.asList("age", "name")));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testModifyDeclaredProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    try {
      recordType.defineProperty("name", NumberType.INT_TYPE, false, null);
    } catch (IllegalArgumentException e) {
      fail("expected UnsupportedOperationException, but got " + e.getClass().getName());
    }

    try {
      recordType.defineProperty("name", StringType.INSTANCE, true, null);
    } catch (UnsupportedOperationException e) {
      fail("expected IllegalArgumentException, but got " + e.getClass().getName());
    }

    try {
      recordType.defineProperty("age", StringType.INSTANCE, true, null);
    } catch (IllegalArgumentException e) {
      fail("expected UnsupportedOperationException, but got " + e.getClass().getName());
    }
  }

  @Test
  public void testModifySyntheticProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, false);

    recordType.defineProperty("name", StringType.INSTANCE, true, null);
    recordType.defineProperty("age", NumberType.INT_TYPE, true, null);

    assertEquals(recordType.getPropertyType("name"), StringType.INSTANCE);
    assertEquals(recordType.getPropertyType("age"), NumberType.INT_TYPE);
  }

  @Test
  public void testEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesA.put("age", new RecordProperty("age", NumberType.INT_TYPE));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT_TYPE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordType recordTypeC = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, EquivalenceMethod.FAST));
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, EquivalenceMethod.FAST));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), EquivalenceMethod.FAST));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), EquivalenceMethod.STRICT));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesA.put("age", new RecordProperty("age", NumberType.INT_TYPE));

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesB.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    propertiesB.put("height", new RecordProperty("height", NumberType.INT_TYPE));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertFalse(recordTypeA.isSubtype(objectType));
    assertFalse(objectType.isSubtype(recordTypeA));

    Map<String, RecordProperty> propertiesC = new HashMap<>();
    propertiesC.put("name", new RecordProperty("name", StringType.INSTANCE));
    propertiesC.put("age", new RecordProperty("age", NumberType.INT_TYPE));
    propertiesC.put("city", new RecordProperty("city", StringType.INSTANCE));

    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.INSTANCE));
    properties.put("age", new RecordProperty("age", NumberType.INT_TYPE));

    RecordType recordType = new RecordType(registry, properties, true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(implicitPrototype.getClass(), ObjectType.class);
    assertEquals(implicitPrototype.getName(), "Object");
    assertEquals(implicitPrototype.getPropertyType("toString"),
        registry.getNativeFunctionType(
            StringType.INSTANCE, new Type[0], null));
  }
}