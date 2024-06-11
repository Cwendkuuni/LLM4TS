import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.isSubtype;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.testing.PrivateTester;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @org.junit.BeforeClass
  public static void setup() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RegistryTester.initRegistry(registry);
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));

    RecordType synthesizedRecordType = new RecordType(registry, properties, false);
    assertEquals(String.class, synthesizedRecordType.getPropertyType("name"));
    assertEquals(Integer.class, synthesizedRecordType.getPropertyType("age"));
  }

  @org.junit.Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordEquivalence() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));

    RecordType recordType3 = new RecordType(registry, properties2, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));

    RecordType recordType4 = new RecordType(registry, properties1, false);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType4, EquivalenceMethod.IDENTITY));

    RecordType recordType5 = new RecordType(registry, properties2, false);
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType5, EquivalenceMethod.IDENTITY));
  }

  @org.junit.Test
  public void testRecordTypeGetImplicitPrototype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype);
  }

  @org.junit.Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);

    recordType.defineProperty("gender", String.class, false, null);
    assertTrue(recordType.hasProperty("gender"));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    recordType.defineProperty("gender", String.class, false, null);
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> recordProperties1 = new HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = new HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertNotNull(grearestSubtype);
    assertEquals(recordType2.getClass(), greatestSubtype.getClass());

    RecordType recordType3 = new RecordType(registry, recordProperties2, true);
    JSType greatestSubtype3 = recordType1.getGreatestSubtype(recordType3);
    assertNotNull(grearestSubtype3);
    assertEquals(recordType3.getClass(), greatestSubtype3.getClass());

    JSType emptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestSubtypeEmpty = recordType1.getGreatestSubtype(emptyRecord);
    assertNotNull(grearestSubtypeEmpty);
    assertEquals(JSTypeNative.OBJECT_TYPE, greatestSubtypeEmpty);

    JSType recordType4 = new RecordType(registry, recordProperties1, false);
    JSType greatestSubtype4 = recordType1.getGreatestSubtype(recordType4);
    assertNotNull(grearestSubtype4);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype4);
  }

  @org.junit.Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordProperty> recordProperties1 = new HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = new HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    assertTrue(isSubtype(recordType1, recordType2));
    assertTrue(isSubtype(recordType2, recordType1));

    RecordType recordType3 = new RecordType(registry, recordProperties2, false);
    assertFalse(isSubtype(recordType1, recordType3));
    assertFalse(isSubtype(recordType3, recordType1));

    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(isSubtype(recordType1, objectType));
    assertTrue(isSubtype(objectType, recordType1));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeWithProperties() {
    Map<String, RecordProperty> recordProperties1 = new HashMap<>();
    recordProperties1.put("name", new RecordProperty("name", String.class));
    recordProperties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordProperties2 = new HashMap<>();
    recordProperties2.put("name", new RecordProperty("name", String.class));
    recordProperties2.put("age", new RecordProperty("age", Integer.class));
    recordProperties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(registry, recordProperties1, true);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    ObjectType objectTypeWithName = new ObjectType(registry, null, null) {
      @Override
      public String getName() {
        return "ObjectWithName";
      }
    };
    objectTypeWithName.defineProperty("name", String.class, true, null);

    ObjectType objectTypeWithNameAndAge = new ObjectType(registry, null, null) {
      @Override
      public String getName() {
        return "ObjectWithNameAndAge";
      }
    };
    objectTypeWithNameAndAge.defineProperty("name", String.class, true, null);
    objectTypeWithNameAndAge.defineProperty("age", Integer.class, true, null);

    ObjectType objectTypeWithNameGender = new ObjectType(registry, null, null) {
      @Override
      public String getName() {
        return "ObjectWithNameGender";
      }
    };
    objectTypeWithNameGender.defineProperty("name", String.class, true, null);
    objectTypeWithNameGender.defineProperty("gender", String.class, true, null);

    JSType objectTypeWithNameAsSubtype = registry.getNativeType(
        new ObjectType(registry, null, null) {
          @Override
          public String getName() {
            return "ObjectWithName";
          }
        });

    assertTrue(isSubtype(recordType1, objectTypeWithName));
    assertTrue(isSubtype(objectTypeWithName, recordType1));
    assertFalse(isSubtype(recordType1, objectTypeWithNameAndAge));
    assertFalse(isSubtype(objectTypeWithNameAndAge, recordType1));
    assertFalse(isSubtype(recordType1, objectTypeWithNameGender));
    assertFalse(isSubtype(objectTypeWithNameGender, recordType1));
    assertTrue(isSubtype(recordType1, objectTypeWithNameAsSubtype));
    assertTrue(isSubtype(objectTypeWithNameAsSubtype, recordType1));
  }

  @org.junit.Test
  public void testRecordTypeIsSubtypeWithUnionTypes() {
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    ObjectType objectType1 = new ObjectType(registry, null, null) {
      @Override
      public String getName() {
        return "ObjectWithName";
      }
    };
    objectType1.defineProperty("name", String.class, true, null);

    ObjectType objectType2 = new ObjectType(registry, null, null) {
      @Override
      public String getName() {
        return "ObjectWithNumber";
      }
    };
    objectType2.defineProperty("number", Integer.class, true, null);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(objectType1);
    unionTypeBuilder.addAlternate(objectType2);
    JSType unionType = unionTypeBuilder.build();

    assertTrue(isSubtype(recordType, unionType));
    assertTrue(isSubtype(unionType, recordType));
  }

  private static final class RegistryTester {
    @org.junit.ClassRule
    public static final JSTypeRegistry registry = new JSTypeRegistry();

    @org.junit.ClassRule
    public static final PrivateTester privateTester = new PrivateTester();

    @org.junit.BeforeClass
    public static void initRegistry(JSTypeRegistry registry) {
      registry.init();
    }
  }

  private enum EquivalenceMethod {
    IDENTITY,
    COVARIANT,
    CONTRAVARIANT
  }
}