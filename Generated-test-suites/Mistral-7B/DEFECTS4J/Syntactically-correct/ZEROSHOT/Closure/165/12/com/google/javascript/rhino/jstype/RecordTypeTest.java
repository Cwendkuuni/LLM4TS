import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @org.junit.Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    properties.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(JSType.RECORD_TYPE, recordType.getTypeClass());
    assertEquals("RecordType", recordType.getName());
    assertEquals("{name: String, age: Number}", recordType.getSource());
    assertEquals(2, recordType.getProperties().size());

    assertSame(JSType.STRING_TYPE, recordType.getPropertyType("name"));
    assertSame(JSType.NUMBER_TYPE, recordType.getPropertyType("age"));

    assertTrue(recordType.getImplicitPrototype() instanceof ObjectType);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype().getTypeClass());
  }

  @org.junit.Test
  public void testRecordTypeProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    properties.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(JSType.RECORD_TYPE, recordType.getTypeClass());
    assertEquals("RecordType", recordType.getName());
    assertEquals("{name: String, age: Number}", recordType.getSource());
    assertEquals(2, recordType.getProperties().size());

    assertSame(JSType.STRING_TYPE, recordType.getPropertyType("name"));
    assertSame(JSType.NUMBER_TYPE, recordType.getPropertyType("age"));

    // Test getPropertyNode
    Node namePropertyNode = recordType.getPropertyNode("name");
    Node agePropertyNode = recordType.getPropertyNode("age");

    assertNotNull(namePropertyNode);
    assertNotNull(agePropertyNode);

    // Test hasProperty
    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));

    // Test defineProperty
    try {
      recordType.defineProperty("name", JSType.BOOLEAN_TYPE, false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
      assertEquals("Cannot define property 'name' on a frozen record type", e.getMessage());
    }

    try {
      recordType.defineProperty("age", JSType.VOID_TYPE, false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
      assertEquals("Cannot define property 'age' on a frozen record type", e.getMessage());
    }

    // Test getGreatestSubtype
    RecordType subTypeRecord = new RecordType(registry, properties);
    RecordType superTypeRecord = new RecordType(registry, Maps.newHashMap());
    superTypeRecord.defineProperty("name", JSType.STRING_TYPE, false, null);
    superTypeRecord.defineProperty("age", JSType.NUMBER_TYPE, false, null);
    superTypeRecord.defineProperty("city", JSType.STRING_TYPE, false, null);

    JSType greatestSubtype = recordType.getGreatestSubtype(subTypeRecord);

    assertSame(recordType, greatestSubtype);

    greatestSubtype = recordType.getGreatestSubtype(superTypeRecord);

    assertEquals(recordType, greatestSubtype);

    // Test isSubtype
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    recordType2.defineProperty("name", JSType.STRING_TYPE, false, null);

    assertTrue(recordType1.isSubtype(recordType2));

    RecordType recordType3 = new RecordType(registry, properties);
    recordType3.defineProperty("city", JSType.STRING_TYPE, false, null);

    assertFalse(recordType1.isSubtype(recordType3));

    // Test isEquivalentTo
    RecordType recordType4 = new RecordType(registry, properties);

    assertTrue(recordType.isEquivalentTo(recordType4));

    RecordType recordType5 = new RecordType(registry, Maps.newHashMap());

    assertFalse(recordType.isEquivalentTo(recordType5));

    // Test getGreatestSubtypeHelper
    JSType greatestSubtypeHelper = recordType.getGreatestSubtypeHelper(JSType.STRING_TYPE);

    assertEquals(JSTypeNative.NO_TYPE, greatestSubtypeHelper);

    greatestSubtypeHelper = recordType.getGreatestSubtypeHelper(recordType2);

    assertEquals(recordType, greatestSubtypeHelper);

    // Test toMaybeRecordType
    RecordType maybeRecordType = new RecordType(registry, Maps.newHashMap());

    assertSame(maybeRecordType, maybeRecordType.toMaybeRecordType());

    assertSame(recordType, recordType.toMaybeRecordType());

    // Test defineProperty with inferred type
    JSType inferredType = JSType.getUnknownType();
    RecordType recordType6 = new RecordType(registry, properties);

    recordType6.defineProperty("name", inferredType, true, null);

    assertFalse(recordType6.isFrozen());

    SortedMap<String, JSType> propertiesMap = recordType6.getProperties();

    assertEquals(JSType.STRING_TYPE, propertiesMap.get("name"));
  }

  @org.junit.Test
  public void testRecordTypeIntersection() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    propertiesA.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    propertiesB.put("city", new RecordProperty("city", JSType.STRING_TYPE));

    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(
        new RecordTypeBuilder(registry)
            .addProperty("name", JSType.STRING_TYPE, null)
            .build(),
        greatestSubtype
    );
  }

  @org.junit.Test
  public void testRecordTypeUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    propertiesA.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("city", new RecordProperty("city", JSType.STRING_TYPE));
    propertiesB.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordTypeB = new RecordType(registry, propertiesB);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);

    unionTypeBuilder.addAlternate(recordTypeA);
    unionTypeBuilder.addAlternate(recordTypeB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(unionTypeBuilder.build(), greatestSubtype);
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    properties.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, properties);

    recordType.freeze();

    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("name", JSType.BOOLEAN_TYPE, false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
      assertEquals("Cannot define property 'name' on a frozen record type", e.getMessage());
    }

    try {
      recordType.defineProperty("age", JSType.VOID_TYPE, false, null);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
      assertEquals("Cannot define property 'age' on a frozen record type", e.getMessage());
    }
  }

  @org.junit.Test
  public void testRecordTypeImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    properties.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, properties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getTypeClass());
  }

  @org.junit.Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    propertiesA.put("age", new RecordProperty("age", JSType.NUMBER_TYPE));

    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", JSType.STRING_TYPE));

    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("name", new RecordProperty("name", JSType.STRING_TYPE));
    propertiesC.put("city", new RecordProperty("city", JSType.STRING_TYPE));

    RecordType recordTypeC = new RecordType(registry, propertiesC);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
  }
}