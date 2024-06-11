import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    RecordProperty property1 = new RecordProperty("prop1", JSTypeFactory.booleanType());
    RecordProperty property2 = new RecordProperty("prop2", JSTypeFactory.numberType());
    RecordProperty property3 = new RecordProperty("prop3", JSTypeFactory.stringType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", property1);
    properties.put("prop2", property2);
    properties.put("prop3", property3);

    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentProperties() {
    Map<String, RecordProperty> differentProperties = Maps.newHashMap();
    differentProperties.put("prop1", property1);
    differentProperties.put("prop4", new RecordProperty("prop4", JSTypeFactory.booleanType()));

    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, differentProperties);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToNull() {
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isEquivalentTo(null));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    RecordType recordType3 = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));
  }

  @Test
  public void testIsSubtypeEmptyRecord() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtypeRecordWithProperty() {
    RecordType recordTypeWithProp = new RecordType(registry, Map.of("prop1", property1));

    assertTrue(recordType.isSubtype(recordTypeWithProp));
  }

  @Test
  public void testIsSubtypeRecordWithIncompatibleProperty() {
    RecordType recordTypeWithIncompatibleProp = new RecordType(registry, Map.of("prop1", JSTypeFactory.stringType(), "prop2", property2));

    assertFalse(recordType.isSubtype(recordTypeWithIncompatibleProp));
  }

  @Test
  public void testIsSubtypeRecordWithExtraProperty() {
    RecordType recordTypeWithExtraProperty = new RecordType(registry, Map.of("prop1", property1, "prop4", JSTypeFactory.booleanType()));

    assertFalse(recordType.isSubtype(recordTypeWithExtraProperty));
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, Map.of("prop1", property1, "prop4", JSTypeFactory.booleanType()));

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertTrue(recordType1.isSubtype(leastSupertype));
    assertTrue(recordType2.isSubtype(leastSupertype));
  }

  @Test
  public void testGetLeastSupertypeEmptyRecord() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getLeastSupertype(recordType));
  }

  @Test
  public void testGetLeastSupertypeRecordWithSameProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertEquals(recordType1, recordType2.getLeastSupertype(recordType1));
  }

  @Test
  public void testGetLeastSupertypeRecordWithIncompatibleProperties() {
    RecordType recordType1 = new RecordType(registry, Map.of("prop1", property1, "prop2", property2));
    RecordType recordType2 = new RecordType(registry, Map.of("prop1", property1, "prop3", property3));

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // Case 1: The properties are compatible.
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    assertEquals(unionTypeBuilder.build(), recordType1.getLeastSupertype(recordType2));
    assertEquals(unionTypeBuilder.build(), recordType2.getLeastSupertype(recordType1));

    // Case 2: The properties are incompatible.
    unionTypeBuilder.clear();
    unionTypeBuilder.addAlternate(recordType1);
    unionTypeBuilder.addAlternate(recordType2);
    assertEquals(objectType, recordType1.getLeastSupertype(recordType2));
    assertEquals(objectType, recordType2.getLeastSupertype(recordType1));
  }

  @Test
  public void testGetGreatestSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, Map.of("prop1", property1, "prop3", property3));

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertTrue(recordType2.isSubtype(greatestSubtype));
    assertTrue(recordType1.isSubtype(greatestSubtype));
  }

  @Test
  public void testGetGreatestSubtypeEmptyRecord() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    assertEquals(recordType, recordType.getGreatestSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testGetGreatestSubtypeRecordWithSameProperties() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);

    assertEquals(recordType1, recordType2.getGreatestSubtype(recordType1));
  }

  @Test
  public void testGetGreatestSubtypeRecordWithIncompatibleProperties() {
    RecordType recordType1 = new RecordType(registry, Map.of("prop1", property1, "prop2", property2));
    RecordType recordType2 = new RecordType(registry, Map.of("prop1", property1, "prop3", property3));

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1, greatestSubtype);
  }

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, properties);

    JSType newPropertyType = JSTypeFactory.stringType();
    Node newPropertyNode = Node.NEW_IDENTIFIER("newProp");

    assertTrue(recordType.defineProperty("prop1", property1, false, null));
    assertTrue(recordType.defineProperty("prop1", property1, false, null));
    assertTrue(recordType.defineProperty("prop2", property2, false, null));
    assertTrue(recordType.defineProperty("prop3", property3, false, null));

    assertFalse(recordType.defineProperty("prop1", property1, false, null));
    assertFalse(recordType.defineProperty("prop1", property1, false, null));

    assertTrue(recordType.defineProperty("newProp", newPropertyType, false, newPropertyNode));
  }

  @Test
  public void testDefinePropertyFrozen() {
    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;

    assertFalse(recordType.defineProperty("prop1", property1, false, null));
    assertFalse(recordType.defineProperty("prop1", property1, false, null));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @Test
  public void testResolveInternal() {
    RecordProperty property = new RecordProperty("prop1", JSTypeFactory.booleanType());
    RecordType recordType = new RecordType(registry, Map.of("prop1", property));

    JSType resolvedType = recordType.resolve(null, null);

    assertEquals(JSTypeFactory.getRecordType(registry, Map.of("prop1", property.getType())), resolvedType);
  }
}