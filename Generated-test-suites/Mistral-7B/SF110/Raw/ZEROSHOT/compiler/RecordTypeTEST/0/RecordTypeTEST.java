import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType expectedRecordType = recordBuilder.buildRecordType("Person", properties);

    RecordType actualRecordType = new RecordType(registry, properties, true);

    assertEquals(expectedRecordType.getName(), actualRecordType.getName());
    assertEquals(expectedRecordType.getDeclared(), actualRecordType.isDeclared());
    assertEquals(expectedRecordType.getImplicitPrototype(),
        actualRecordType.getImplicitPrototype());
    assertEquals(expectedRecordType.getOwnPropertyNames(),
        actualRecordType.getOwnPropertyNames());

    assertEquals(expectedRecordType.getPropertyType("name"),
        actualRecordType.getPropertyType("name"));
    assertEquals(expectedRecordType.getPropertyType("age"),
        actualRecordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeCreationWithFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType expectedRecordType = recordBuilder.buildRecordType("Person", properties);
    RecordType actualRecordType = new RecordType(registry, properties, false);

    assertEquals(expectedRecordType.getName(), actualRecordType.getName());
    assertFalse(actualRecordType.isDeclared());
    assertEquals(expectedRecordType.getImplicitPrototype(),
        actualRecordType.getImplicitPrototype());
    assertEquals(expectedRecordType.getOwnPropertyNames(),
        actualRecordType.getOwnPropertyNames());

    assertEquals(expectedRecordType.getPropertyType("name"),
        actualRecordType.getPropertyType("name"));
    assertEquals(expectedRecordType.getPropertyType("age"),
        actualRecordType.getPropertyType("age"));

    assertFalse(actualRecordType.isFrozen());
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put(null, new RecordProperty("name", String.class));

    recordBuilder.buildRecordType("Person", properties);
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordBuilder = new RecordTypeBuilder(registry);

    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    recordType.defineProperty("name", String.class, false, null);
    recordType.defineProperty("age", Number.class, false, null);

    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Number.class, recordType.getPropertyType("age"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);
    recordType.isFrozen = true;

    recordType.defineProperty("name", String.class, false, null);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    recordType.defineProperty(null, String.class, false, null);
  }

  @Test
  public void testDefinePropertyWithDuplicateName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    recordType.defineProperty("name", String.class, false, null);
    recordType.defineProperty("name", String.class, false, null);

    Set<String> ownProperties = recordType.getOwnPropertyNames();
    assertEquals(1, ownProperties.size());
    assertTrue(ownProperties.contains("name"));
  }

  @Test
  public void testGetProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    Map<String, JSType> properties = recordType.getProperties();
    assertEquals(2, properties.size());
    assertEquals(String.class, properties.get("name"));
    assertEquals(Number.class, properties.get("age"));
  }

  @Test
  public void testIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    assertFalse(recordType.isFrozen());

    recordType.isFrozen = true;

    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), false);

    assertTrue(recordType.isSynthetic());

    recordType = new RecordType(registry, Collections.emptyMap(), true);

    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);
    RecordType recordType2 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));

    recordType1.getProperty("name").setInvariant(false);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));

    recordType1.getProperty("name").setInvariant(true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);

    ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertEquals(expectedPrototype, recordType.getImplicitPrototype());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType stringType = String.class;
    JSType numberType = Number.class;
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordType = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    JSType expectedType = recordType.toMaybeRecordType();

    assertEquals(expectedType, recordType.getGreatestSubtypeHelper(emptyRecordType));
    assertEquals(emptyRecordType, recordType.getGreatestSubtypeHelper(recordType));

    UnionType unionType = new UnionTypeBuilder(registry).build(Arrays.asList(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType
    ));

    assertEquals(emptyRecordType, recordType.getGreatestSubtypeHelper(unionType));

    JSType recordWithSameProperties = new RecordTypeBuilder(registry)
        .addProperty("name", String.class, null)
        .addProperty("age", Number.class, null)
        .build();

    assertEquals(recordType, recordType.getGreatestSubtypeHelper(recordWithSameProperties));

    JSType recordWithDifferentProperties = new RecordTypeBuilder(registry)
        .addProperty("name", String.class, null)
        .addProperty("age", Number.class, null)
        .addProperty("gender", Boolean.class, null)
        .build();

    assertEquals(emptyRecordType, recordType.getGreatestSubtypeHelper(recordWithDifferentProperties));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType stringType = String.class;
    JSType numberType = Number.class;
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordType1 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    RecordType recordType2 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    RecordType recordType3 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class),
            new RecordProperty("gender", Boolean.class)).asMap(), true);

    RecordType recordType4 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), false);

    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType1.isSubtype(emptyRecordType));
    assertTrue(recordType1.isSubtype(objType));
    assertTrue(recordType1.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType1.isSubtype(recordType4));
  }

  @Test
  public void testIsSubtypeWithRecordAndUnionTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType stringType = String.class;
    JSType numberType = Number.class;
    JSType booleanType = Boolean.class;
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    RecordType recordType1 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class)).asMap(), true);

    UnionType recordUnionType = new UnionTypeBuilder(registry)
        .addAlternate(recordType1)
        .addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))
        .build();

    RecordType recordType2 = new RecordType(registry,
        Arrays.asList(new RecordProperty("name", String.class),
            new RecordProperty("age", Number.class),
            new RecordProperty("gender", Boolean.class)).asMap(), true);

    ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType1.isSubtype(emptyRecordType));
    assertTrue(recordType1.isSubtype(objType));
    assertTrue(recordType1.isSubtype(recordUnionType));
    assertFalse(recordType1.isSubtype(recordType2));
  }
}