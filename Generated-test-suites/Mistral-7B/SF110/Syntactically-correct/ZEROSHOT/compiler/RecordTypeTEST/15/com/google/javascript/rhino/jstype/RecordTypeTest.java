import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.Type;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.test.JSTypeTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest extends JSTypeTest {

  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
  }

  public void testRecordTypeCreationWithSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, false);
    assertNotNull(recordType);
    assertTrue(recordType.isSynthetic());
  }

  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertEquals(recordType1, recordType2);
  }

  public void testRecordTypeInequivalence() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);

    assertFalse(recordType1.equals(recordType2));
  }

  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    assertFalse(recordType.defineProperty("c", registry.getTypeByName("string"), false, null));
  }

  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);
    Type expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    Type actual = recordType.getImplicitPrototype();

    assertEquals(expected, actual);
  }

  public void testRecordTypeIsRecordType() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isRecordType());
  }

  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordProperty property3 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property4 = new RecordProperty("b", registry.getTypeByName("boolean"));
    RecordProperty property5 = new RecordProperty("c", registry.getTypeByName("string"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", property3);
    properties2.put("b", property4);
    properties2.put("c", property5);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, new HashMap<>(), true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithMissingProperty() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordProperty property3 = new RecordProperty("c", registry.getTypeByName("string"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("b", property2);
    properties2.put("c", property3);

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithIncompatiblePropertyType() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordProperty property3 = new RecordProperty("a", registry.getTypeByName("string"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", property3);
    properties2.put("b", property2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithSuperTypePropertyType() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordProperty property3 = new RecordProperty("a", registry.getTypeByName("number").getGreatestLowerBound(registry.getTypeByName("object")));
    RecordProperty property4 = new RecordProperty("c", registry.getTypeByName("string"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", property3);
    properties2.put("b", property2);
    properties2.put("c", property4);

    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithSuperTypePropertyType2() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordProperty property3 = new RecordProperty("a", registry.getTypeByName("string"));
    RecordProperty property4 = new RecordProperty("c", registry.getTypeByName("number"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", property3);
    properties2.put("b", property2);
    properties2.put("c", property4);

    RecordType recordType2 = new RecordType(registry, properties2, true);

    UnionType unionType = new UnionType(registry, Arrays.asList(recordType1, recordType2));
    RecordType recordTypeExpected = new RecordType(registry, Arrays.asList("a", "b"), true);

    RecordType recordTypeActual = unionType.getLeastUpperBound(recordTypeExpected);

    assertTrue(recordTypeExpected.isSubtype(recordTypeActual));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, new HashMap<>(), false);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeIsSubtypeRecordTypeWithEmptyRecordType() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordType recordType1 = new RecordType(registry, new HashMap<>(), true);
    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  public void testRecordTypeSubtypeWithSelf() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.isSubtype(recordType));
  }

  public void testRecordTypeIsSubtypeWithNull() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property = new RecordProperty("a", registry.getTypeByName("number"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property);

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isSubtype(null));
  }

  public void testRecordTypeIsSubtypeWithNonRecordType() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", property1);
    properties.put("b", property2);

    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isSubtype(registry.getTypeByName("string")));
  }

  public void testRecordTypeLeastUpperBound() {
    JSTypeRegistry registry = new JSTypeRegistryImpl();

    RecordProperty property1 = new RecordProperty("a", registry.getTypeByName("number"));
    RecordProperty property2 = new RecordProperty("b", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", property1);
    properties1.put("b", property2);

    RecordType recordType1 = new RecordType(registry, properties1, true);

    RecordProperty property3 = new RecordProperty("c", registry.getTypeByName("string"));
    RecordProperty property4 = new RecordProperty("d", registry.getTypeByName("boolean"));
    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", property1);
    properties2.put("b", property2);
    properties2.put("c", property3);
    properties2.put("d", property4);

    RecordType recordType2 = new RecordType(registry, properties2, true);

    List<RecordType> recordTypes = new ArrayList<>();
    recordTypes.add(recordType1);
    recordTypes.add(recordType2);

    RecordType recordTypeExpected = new RecordType(registry, Arrays.asList("a", "b", "c"), true);
    RecordType recordTypeActual = RecordType.getLeastUpperBound(recordTypes);

    assertTrue(recordTypeExpected.isSubtype(recordTypeActual));
  }
}