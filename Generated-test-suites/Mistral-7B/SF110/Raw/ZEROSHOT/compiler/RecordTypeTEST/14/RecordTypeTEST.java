import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RunWith(org.junit.runner.JUnit4.class)
public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
    assertEquals("Record", recordType.getClassName());
    assertEquals(JSType.RECORD, recordType.getType());
    assertTrue(recordType.isRecordType());
    assertEquals(Arrays.asList("name", "age"), recordType.getOwnPropertyNames());
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isFrozen());

    try {
      recordType.defineProperty("newProperty", JSType.STRING, false, null);
      assertFalse(true);
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", JSType.STRING));
    properties1.put("age", new RecordProperty("age", JSType.NUMBER));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", JSType.STRING));
    properties2.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType type1 = new RecordType(registry, properties1, true);
    RecordType type2 = new RecordType(registry, properties2, true);

    assertTrue(type1.checkRecordEquivalenceHelper(type2, EquivalenceMethod.BY_NAME));

    // Differing properties
    properties2.put("city", new RecordProperty("city", JSType.STRING));
    RecordType type2b = new RecordType(registry, properties2, true);
    assertFalse(type1.checkRecordEquivalenceHelper(type2b, EquivalenceMethod.BY_NAME));
  }

  @Test
  public void testRecordTypeCheckSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", JSType.STRING));
    properties1.put("age", new RecordProperty("age", JSType.NUMBER));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", JSType.STRING));
    properties2.put("age", new RecordProperty("age", JSType.NUMBER));
    properties2.put("city", new RecordProperty("city", JSType.STRING));

    RecordType type1 = new RecordType(registry, properties1, true);
    RecordType type2 = new RecordType(registry, properties2, true);

    assertTrue(type1.isSubtype(type2));

    // Differing properties
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("name", new RecordProperty("name", JSType.STRING));
    properties3.put("age", new RecordProperty("age", JSType.NUMBER));
    properties3.put("city", new RecordProperty("city", JSType.NUMBER));

    assertFalse(type1.isSubtype(new RecordType(registry, properties3, false)));
    assertFalse(type1.isSubtype(JSType.NULL));
    assertFalse(type1.isSubtype(JSType.NUMBER));
    assertFalse(type1.isSubtype(JSType.BOOLEAN));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = new HashMap<>();
    properties1.put("name", new RecordProperty("name", JSType.STRING));
    properties1.put("age", new RecordProperty("age", JSType.NUMBER));

    Map<String, RecordProperty> properties2 = new HashMap<>();
    properties2.put("name", new RecordProperty("name", JSType.STRING));
    properties2.put("age", new RecordProperty("age", JSType.NUMBER));
    properties2.put("city", new RecordProperty("city", JSType.STRING));

    RecordType type1 = new RecordType(registry, properties1, true);
    RecordType type2 = new RecordType(registry, properties2, true);

    JSType greatestSubtype = type1.getGreatestSubtypeHelper(type2);
    assertEquals(type2, greatestSubtype);

    // Empty record type
    JSType emptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType greatestEmpty = type1.getGreatestSubtypeHelper(emptyRecord);
    assertEquals(emptyRecord, greatestEmpty);

    // Incompatible record types
    Map<String, RecordProperty> properties3 = new HashMap<>();
    properties3.put("name", new RecordProperty("name", JSType.STRING));
    properties3.put("age", new RecordProperty("age", JSType.NUMBER));
    properties3.put("city", new RecordProperty("city", JSType.NUMBER));

    RecordType type3 = new RecordType(registry, properties3, false);
    JSType greatestSubtype3 = type1.getGreatestSubtypeHelper(type3);
    assertEquals(JSType.NO_OBJECT_TYPE, greatestSubtype3);
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);

    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertFalse(recordType.hasProperty("nonExisting"));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(JSType.STRING, recordType.getPropertyType("name"));
    assertEquals(JSType.NUMBER, recordType.getPropertyType("age"));
    assertNull(recordType.getPropertyType("nonExisting"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name",
        new RecordProperty("name", JSType.STRING, new Node("name", null)));
    properties.put("age", new RecordProperty("age", JSType.NUMBER, null));

    RecordType recordType = new RecordType(registry, properties, true);

    Node nameNode = recordType.getPropertyNode("name");
    assertNotNull(nameNode);
    assertEquals("name", nameNode.toString());

    Node ageNode = recordType.getPropertyNode("age");
    assertNull(ageNode);
  }

  @Test
  public void testRecordTypeSubtypeWithUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    properties.put("age", new RecordProperty("age", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);

    // Union type with a property of the same name
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    builder.addAlternate(objectType);
    builder.addAlternate(new RecordType(registry, properties, false));
    JSType unionType = builder.build();

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(unionType);
    assertEquals(unionType, greatestSubtype);
  }

  private enum EquivalenceMethod {
    BY_NAME,
    BY_TYPE
  }
}