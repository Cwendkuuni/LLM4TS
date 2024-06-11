import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.node.ObjectInitializer;
import com.google.javascript.rhino.types.ObjectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  @org.junit.BeforeClass
  public static void setup() {
    JSTypeFactory registry = new JSTypeFactory();
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty property1 = new RecordProperty("name", registry.getNullType());
    RecordProperty property2 = new RecordProperty("age", registry.getIntType());

    properties.put("name", property1);
    properties.put("age", property2);

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("Record", recordType.getClassName());
    assertEquals("Record<name: null, age: number>", recordType.getSourceName());
    assertEquals(2, recordType.getOwnPropertyCount());

    assertNotNull(recordType.getPropertyType("name"));
    assertNull(recordType.getPropertyType("unknownProperty"));
    assertEquals(registry.getNullType(), recordType.getPropertyType("name"));
    assertEquals(registry.getIntType(), recordType.getPropertyType("age"));

    assertEquals(0, recordType.getIndexedPropertyCount());
  }

  @org.junit.Test
  public void testRecordTypeCreationWithEmptyProperties() {
    RecordType recordType = new RecordType(registry, Collections.emptyMap(), true);
    assertEquals("Record", recordType.getClassName());
    assertEquals("Record<>", recordType.getSourceName());
    assertEquals(0, recordType.getOwnPropertyCount());
  }

  @org.junit.Test
  public void testRecordTypeCreationWithSynthesizedProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty property1 = new RecordProperty("name", registry.getStringType());
    RecordProperty property2 = new RecordProperty("age", registry.getIntType());

    properties.put("name", property1);
    properties.put("age", property2);

    RecordType recordType = new RecordType(registry, properties, false);

    assertEquals("Record", recordType.getClassName());
    assertEquals("Record<name: string, age: number>", recordType.getSourceName());
    assertEquals(2, recordType.getOwnPropertyCount());

    assertNotNull(recordType.getPropertyType("name"));
    assertNotNull(recordType.getPropertyType("age"));

    ObjectType prototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), prototype);
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", registry.getStringType()));
    properties1.put("age", new RecordProperty("age", registry.getIntType()));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getIntType()));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.IDENTITY));

    RecordType recordTypeSynthetic1 = new RecordType(registry, properties1, false);
    RecordType recordTypeSynthetic2 = new RecordType(registry, properties2, false);

    assertFalse(recordTypeSynthetic1.checkRecordEquivalenceHelper(recordTypeSynthetic2, EquivalenceMethod.IDENTITY));
    assertFalse(recordTypeSynthetic2.checkRecordEquivalenceHelper(recordTypeSynthetic1, EquivalenceMethod.IDENTITY));
  }

  @org.junit.Test
  public void testRecordTypeSubtypes() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty property = new RecordProperty("name", registry.getStringType());
    properties.put("name", property);

    RecordType recordType1 = new RecordType(registry, properties, true);
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));

    ObjectType recordType3 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertTrue(recordType3.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType3));

    RecordType recordType4 = new RecordType(registry, properties, false);
    assertFalse(recordType1.isSubtype(recordType4));
    assertFalse(recordType4.isSubtype(recordType1));

    UnionType unionType = new UnionType(Arrays.asList(recordType1, recordType2));
    assertEquals(recordType1, unionType.getAlternates().get(0));
    assertEquals(recordType2, unionType.getAlternates().get(1));

    assertFalse(recordType1.isSubtype(unionType));
    assertFalse(unionType.isSubtype(recordType1));
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty property = new RecordProperty("name", registry.getStringType());
    properties.put("name", property);

    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("newProperty", registry.getIntType(), false, Node.UNDEFINED);
      fail("RecordType should not allow defining new properties once frozen.");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeGetGreatestSubtype() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordProperty property1 = new RecordProperty("name", registry.getStringType());
    RecordProperty property2 = new RecordProperty("age", registry.getIntType());

    properties.put("name", property1);
    properties.put("age", property2);

    RecordType recordType1 = new RecordType(registry, properties, true);

    RecordType recordType2 = new RecordType(registry, properties, false);

    RecordType recordType3 = new RecordType(registry, Collections.emptyMap(), true);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
    assertEquals(recordType3, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE).getGreatestSubtype(registry.getNullType()));
    assertEquals(recordType3, greatestSubtype);
  }
}

enum EquivalenceMethod {
  IDENTITY,
  STRICT_PROTOTYPE_CHAIN,
  PROTOTYPE_CHAIN,
  WITH_NULL,
  WITH_UNDEFINED,
  WITH_TOP
}