import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.types.NativeObjectType;
import com.google.javascript.rhino.jstype.types.RecordTypeType;
import com.google.javascript.rhino.types.ReferenceType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @org.junit.Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertNotNull(recordType);
    assertTrue(recordType instanceof RecordTypeType);

    RecordType recordTypeSynthetic = new RecordType(registry, recordProperties, false);
    assertNotNull(recordTypeSynthetic);
    assertTrue(recordTypeSynthetic instanceof RecordTypeType);
  }

  @org.junit.Test
  public void testRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Number.class));
    recordPropertiesB.put("newProperty", new RecordProperty("newProperty", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, RecordType.EquivalenceMethod.IDENTICAL));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, RecordType.EquivalenceMethod.EXTENDED));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    ReferenceType prototypeObjectType = (ReferenceType) recordType.getImplicitPrototype();
    assertNotNull(prototypeObjectType);
    assertTrue(prototypeObjectType instanceof NativeObjectType);
  }

  @org.junit.Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertFalse(recordType.defineProperty("newProperty", String.class, false, null));
    assertNull(recordType.getPropertyNode("newProperty"));
    assertFalse(recordType.hasProperty("newProperty"));

    assertFalse(recordType.defineProperty("name", String.class, false, null));
    assertNotNull(recordType.getPropertyNode("name"));
    assertTrue(recordType.hasProperty("name"));
  }

  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Number.class));
    recordPropertiesB.put("newProperty", new RecordProperty("newProperty", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertTrue(recordTypeA.isSubtype(greatestSubtype));
    assertTrue(greatestSubtype instanceof RecordTypeType);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertTrue(recordTypeB.isSubtype(greatestSubtype));
    assertTrue(greatestSubtype instanceof RecordTypeType);
  }

  @org.junit.Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Number.class));
    recordPropertiesB.put("newProperty", new RecordProperty("newProperty", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    JSType typeA = new RecordType(registry, recordPropertiesA, true);
    JSType typeB = new RecordType(registry, recordPropertiesB, true);
    JSType typeC = new RecordType(registry, recordPropertiesA, true);

    assertTrue(recordTypeA.isSubtype(typeA));
    assertTrue(recordTypeA.isSubtype(typeC));
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE)));
  }

  @org.junit.Test
  public void testIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertFalse(recordType.isFrozen());

    recordType = new RecordType(registry, recordProperties, true);
    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testFreezeRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    Map<String, RecordProperty> recordPropertiesToAdd = new HashMap<>();
    recordPropertiesToAdd.put("newProperty", new RecordProperty("newProperty", String.class));
    RecordType recordTypeToAdd = new RecordType(registry, recordPropertiesToAdd, false);

    assertFalse(recordType.defineProperty("newProperty", String.class, false, null));
  }

  @org.junit.Test
  public void testDefineSynthesizedProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(registry, recordProperties, false);
    assertFalse(recordType.defineProperty("age", Number.class, false, null));
    assertNull(recordType.getPropertyNode("age"));
    assertFalse(recordType.hasProperty("age"));

    UnionType unionType = new UnionTypeBuilder(registry)
        .addAlternate(registry.getNativeType(JSTypeNative.OBJECT_TYPE))
        .addAlternate(registry.getNativeType(JSTypeNative.NUMBER_TYPE))
        .build();

    recordType = new RecordType(registry, recordProperties, false);
    assertTrue(recordType.defineProperty("age", unionType, false, null));
    assertNotNull(recordType.getPropertyNode("age"));
    assertTrue(recordType.hasProperty("age"));
  }

  @org.junit.Test
  public void testDefineDeclaredProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertFalse(recordType.defineProperty("newProperty", String.class, false, null));

    assertFalse(recordType.defineProperty("name", Number.class, false, null));
    assertFalse(recordType.defineProperty("age", String.class, false, null));

    assertTrue(recordType.defineProperty("name", String.class, true, null));
    assertTrue(recordType.hasProperty("name"));
    assertNull(recordType.getPropertyNode("name"));

    Node nameNode = new Node.Name("name");
    assertTrue(recordType.defineProperty("name", String.class, true, nameNode));
    assertTrue(recordType.hasProperty("name"));
    assertNotNull(recordType.getPropertyNode("name"));
  }

  @org.junit.Test
  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    String recordTypeString = recordType.toString();
    assertTrue(recordTypeString.contains("RecordType"));
    assertTrue(recordTypeString.contains("RecordType"));
    assertTrue(recordTypeString.contains("name"));
    assertTrue(recordTypeString.contains("String"));
    assertTrue(recordTypeString.contains("age"));
    assertTrue(recordTypeString.contains("Number"));
  }

  @org.junit.Test
  public void testRecordTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Number.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    assertTrue(recordTypeA.equals(recordTypeA));
    assertTrue(recordTypeA.equals(recordTypeB));
    assertTrue(recordTypeB.equals(recordTypeA));

    recordPropertiesB.put("newProperty", new RecordProperty("newProperty", String.class));

    assertFalse(recordTypeA.equals(recordTypeB));
    assertFalse(recordTypeB.equals(recordTypeA));

    Map<String, RecordProperty> recordPropertiesC = new HashMap<>();
    recordPropertiesC.put("name", new RecordProperty("name", String.class));
    recordPropertiesC.put("age", new RecordProperty("age", Number.class));
    recordPropertiesC.put("newProperty", new RecordProperty("newProperty", String.class));

    RecordType recordTypeC = new RecordType(registry, recordPropertiesC, true);

    assertTrue(recordTypeA.equals(recordTypeC));
    assertTrue(recordTypeC.equals(recordTypeA));

    RecordType recordTypeD = new RecordType(registry, recordPropertiesA, true);
    assertTrue(recordTypeA.equals(recordTypeD));
    assertTrue(recordTypeD.equals(recordTypeA));
  }

  @org.junit.Test
  public void testHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertFalse(recordType.hasProperty("newProperty"));
  }

  @org.junit.Test
  public void testGetOwnPropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Set<String> ownProperties = recordType.getOwnPropertyNames();
    Set<String> expectedProperties = new HashSet<>(Arrays.asList("name", "age"));
    assertTrue(ownProperties.equals(expectedProperties));
  }

  @org.junit.Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    JSType nameType = recordType.getPropertyType("name");
    JSType ageType = recordType.getPropertyType("age");

    assertTrue(nameType instanceof StringType);
    assertTrue(ageType instanceof NumberType);
  }

  @org.junit.Test
  public void testGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Node nameNode = recordType.getPropertyNode("name");
    Node ageNode = recordType.getPropertyNode("age");

    assertTrue(nameNode instanceof Node.Name);
    assertTrue(ageNode instanceof Node.Name);
  }
}