import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static RecordType recordType;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
    RecordProperty property1 = new RecordProperty("prop1", registry.getNullType());
    RecordProperty property2 = new RecordProperty("prop2", registry.getStringType());

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("prop1", property1);
    properties.put("prop2", property2);

    recordType = new RecordType(registry, properties, true);
  }

  @org.junit.Test
  public void testRecordTypeIsFrozenByDefault() {
    assertFalse(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeIsDeclared() {
    assertTrue(recordType.isDeclared());
  }

  @org.junit.Test
  public void testCheckRecordEquivalenceHelperTrue() {
    RecordType recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
  }

  @org.junit.Test
  public void testCheckRecordEquivalenceHelperFalse() {
    RecordType recordType2 = new RecordType(registry, properties, false);
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    JSType implicitPrototype = recordType.getImplicitPrototype();
    assertNotNull(implicitPrototype);
    assertTrue(implicitPrototype instanceof ObjectType);
  }

  @org.junit.Test
  public void testDefineProperty() {
    String propertyName = "newProperty";
    JSType newPropertyType = registry.getBooleanType();
    Node newPropertyNode = Node.NIL;

    recordType.defineProperty(propertyName, newPropertyType, false, newPropertyNode);

    assertTrue(recordType.hasProperty(propertyName));
    assertTrue(recordType.isPropertyTypeDeclared(propertyName));
    assertEquals(newPropertyType, recordType.getPropertyType(propertyName));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozen() {
    recordType.isFrozen = true;
    String propertyName = "newProperty";
    JSType newPropertyType = registry.getBooleanType();
    Node newPropertyNode = Node.NIL;

    recordType.defineProperty(propertyName, newPropertyType, false, newPropertyNode);
  }

  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    JSType anotherRecord = new RecordType(registry, properties, false);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecord);

    assertNotNull(greatestSubtype);
    assertTrue(greatestSubtype instanceof ObjectType);
  }

  @org.junit.Test
  public void testGetGreatestSubtypeHelperDifferentRecordTypes() {
    JSType anotherRecord = new RecordType(registry, properties, true);
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(anotherRecord);

    assertNull(greatestSubtype);
  }

  @org.junit.Test
  public void testIsSubtype() {
    RecordType anotherRecord = new RecordType(registry, properties, true);
    assertTrue(recordType.isSubtype(anotherRecord));
  }

  @org.junit.Test
  public void testIsSubtypeDifferentRecordTypes() {
    RecordType anotherRecord = new RecordType(registry, properties, false);
    assertFalse(recordType.isSubtype(anotherRecord));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeVsObjectType() {
    assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @org.junit.Test
  public void testIsSubtypeObjectTypeVsRecordType() {
    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeVsRecordTypeWithLessProperties() {
    String propertyName = "deleteProperty";
    Map<String, RecordProperty> propertiesLess = new HashMap<>();
    propertiesLess.putAll(properties);
    propertiesLess.remove(propertyName);

    RecordType recordTypeLess = new RecordType(registry, propertiesLess, true);

    assertFalse(recordType.isSubtype(recordTypeLess));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithSamePropertiesButDifferentTypes() {
    RecordProperty property1Different = new RecordProperty("prop1", registry.getNumberType());
    Map<String, RecordProperty> propertiesDifferent = new HashMap<>();
    propertiesDifferent.putAll(properties);
    propertiesDifferent.put("prop1", property1Different);

    RecordType recordTypeDifferent = new RecordType(registry, propertiesDifferent, true);

    assertFalse(recordType.isSubtype(recordTypeDifferent));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithSamePropertiesButDeclaredDifferently() {
    Map<String, RecordProperty> propertiesDeclaredDifferently = new HashMap<>();
    propertiesDeclaredDifferently.putAll(properties);
    propertiesDeclaredDifferently.forEach((key, value) -> value.setDeclared(false));

    RecordType recordTypeDeclaredDifferently = new RecordType(registry, propertiesDeclaredDifferently, true);

    assertFalse(recordType.isSubtype(recordTypeDeclaredDifferently));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfDifferentTypesButSameTypeName() {
    RecordProperty property1DifferentType = new RecordProperty("prop1", registry.getNumberType());
    Map<String, RecordProperty> propertiesDifferentType = new HashMap<>();
    propertiesDifferentType.putAll(properties);
    propertiesDifferentType.put("prop1", property1DifferentType);

    RecordType recordTypeDifferentType = new RecordType(registry, propertiesDifferentType, true);

    assertFalse(recordType.isSubtype(recordTypeDifferentType));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfSameTypesButDifferentOrder() {
    Map<String, RecordProperty> propertiesDifferentOrder = new HashMap<>();
    propertiesDifferentOrder.putAll(properties);
    String propertyName1 = properties.keySet().iterator().next();
    String propertyName2 = properties.keySet().iterator().next();
    String temp = propertyName1;
    propertyName1 = propertyName2;
    propertyName2 = temp;

    RecordType recordTypeDifferentOrder = new RecordType(registry, propertiesDifferentOrder, true);

    assertFalse(recordType.isSubtype(recordTypeDifferentOrder));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfSameTypesButDifferentNames() {
    Map<String, RecordProperty> propertiesDifferentNames = new HashMap<>();
    propertiesDifferentNames.putAll(properties);
    propertiesDifferentNames.remove("prop1");
    propertiesDifferentNames.put("newName", property1);

    RecordType recordTypeDifferentNames = new RecordType(registry, propertiesDifferentNames, true);

    assertFalse(recordType.isSubtype(recordTypeDifferentNames));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfSameTypesButNullPropertyTypes() {
    Map<String, RecordProperty> propertiesNullTypes = new HashMap<>();
    propertiesNullTypes.putAll(properties);
    for (RecordProperty prop : properties.values()) {
      prop.setType(null);
    }

    RecordType recordTypeNullTypes = new RecordType(registry, propertiesNullTypes, true);

    assertFalse(recordType.isSubtype(recordTypeNullTypes));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfSameTypesButDifferentNodeTypes() {
    RecordProperty propertyNodeDifferent = new RecordProperty("prop1", registry.getNumberType());
    propertyNodeDifferent.setPropertyNode(Node.newIntLiteral(1));
    Map<String, RecordProperty> propertiesNodeDifferent = new HashMap<>();
    propertiesNodeDifferent.putAll(properties);
    propertiesNodeDifferent.put("prop1", propertyNodeDifferent);

    RecordType recordTypeNodeDifferent = new RecordType(registry, propertiesNodeDifferent, true);

    assertFalse(recordType.isSubtype(recordTypeNodeDifferent));
  }

  @org.junit.Test
  public void testIsSubtypeRecordTypeWithPropertiesOfSameTypesButDifferentPropertyNodes() {
    RecordProperty propertyNodeDifferent = new RecordProperty("prop1", registry.getNumberType());
    propertyNodeDifferent.setPropertyNode(Node.newIntLiteral(2));
    Map<String, RecordProperty> propertiesNodeDifferent = new HashMap<>();
    propertiesNodeDifferent.putAll(properties);
    propertiesNodeDifferent.put("prop1", propertyNodeDifferent);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordType);
    unionTypeBuilder.addAlternate(new RecordType(registry, propertiesNodeDifferent, true));

    JSType recordTypeWithNodeDifferent = unionTypeBuilder.build();

    assertTrue(recordType.isSubtype(recordTypeWithNodeDifferent));
  }

  enum EquivalenceMethod {
    IDENTICAL,
    PROTOTYPE_ONLY,
    PROPERTY_TYPES_ONLY,
    PROPERTY_NAMES_ONLY,
    PROPERTY_TYPES_AND_NAMES,
    SUBTYPE
  }
}