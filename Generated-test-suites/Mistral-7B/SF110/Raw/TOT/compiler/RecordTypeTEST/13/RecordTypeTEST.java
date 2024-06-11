import static com.google.javascript.rhino.JSTypeFactory.createRecordType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test constructing a record type from a map of properties
   */
  @org.junit.Test
  public void testRecordTypeFromMap() {
    Map<String, RecordProperty> props = new HashMap<>();
    props.put("name", new RecordProperty("name", JSTypeFactory.STRING_TYPE));
    props.put("age", new RecordProperty("age", JSTypeFactory.INT_TYPE));

    RecordType recordType = new RecordType(registry, props, true);

    assertEquals("Object with name and age properties",
        "function RecordType(name, age)", recordType.getJsFnString());
    assertEquals("Name", JSTypeFactory.STRING_TYPE, recordType.getPropertyType("name"));
    assertEquals("Age", JSTypeFactory.INT_TYPE, recordType.getPropertyType("age"));
  }

  /**
   * Test constructing a synthesized record type from a map of properties
   */
  @org.junit.Test
  public void testRecordTypeFromMapSynthesized() {
    Map<String, RecordProperty> props = new HashMap<>();
    props.put("name", new RecordProperty("name", JSTypeFactory.STRING_TYPE));
    props.put("age", new RecordProperty("age", JSTypeFactory.INT_TYPE));

    RecordType recordType = new RecordType(registry, props, false);

    assertEquals("Object with name and age properties",
        "function RecordType$1() {return {name: null, age: 0};}",
        recordType.getJsFnString());
    assertEquals("name", JSTypeFactory.STRING_TYPE, recordType.getPropertyType("name"));
    assertEquals("age", JSTypeFactory.INT_TYPE, recordType.getPropertyType("age"));
  }

  /**
   * Test RecordType's checkRecordEquivalenceHelper method
   */
  @org.junit.Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .build();

    RecordType recordType2 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .build();

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTITY));

    RecordType recordType3 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .addProperty("city", JSTypeFactory.STRING_TYPE)
        .build();

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTITY));
  }

  /**
   * Test RecordType's getImplicitPrototype method
   */
  @org.junit.Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);

    ObjectType prototype = recordType.getImplicitPrototype();

    assertEquals("Object prototype",
        "function Object() {}", prototype.getJsFnString());
  }

  /**
   * Test RecordType's defineProperty method
   */
  @org.junit.Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);

    String propertyName = "name";
    JSType propertyType = JSTypeFactory.STRING_TYPE;
    Node propertyNode = Node.newNull();

    assertTrue(recordType.defineProperty(propertyName, propertyType, false, propertyNode));

    assertEquals("Name", propertyType, recordType.getPropertyType(propertyName));
  }

  /**
   * Test RecordType's getGreatestSubtypeHelper method
   */
  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .build();

    RecordType recordType2 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .addProperty("city", JSTypeFactory.STRING_TYPE)
        .build();

    RecordType recordType3 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .addProperty("city", JSTypeFactory.STRING_TYPE)
        .addProperty("country", JSTypeFactory.STRING_TYPE)
        .build();

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull("Greatest subtype should not be null", greatestSubtype);
    assertEquals("RecordType created from properties of recordType2",
        recordType2.getJsFnString(), greatestSubtype.getJsFnString());

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertNotNull("Greatest subtype should not be null", greatestSubtype);
    assertEquals("RecordType with properties of recordType1 and recordType3",
        recordType3.getJsFnString(), greatestSubtype.getJsFnString());
  }

  /**
   * Test RecordType's isSubtype method
   */
  @org.junit.Test
  public void testIsSubtype() {
    RecordType recordType1 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .build();

    RecordType recordType2 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .addProperty("city", JSTypeFactory.STRING_TYPE)
        .build();

    RecordType recordType3 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .addProperty("city", JSTypeFactory.STRING_TYPE)
        .addProperty("country", JSTypeFactory.STRING_TYPE)
        .build();

    RecordType recordType4 = createRecordType("name", JSTypeFactory.STRING_TYPE)
        .addProperty("age", JSTypeFactory.INT_TYPE)
        .build();

    RecordType recordType5 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType4));
    assertTrue(recordType4.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType3));
    assertFalse(recordType3.isSubtype(recordType1));
    assertFalse(recordType1.isSubtype(recordType5));
    assertFalse(recordType5.isSubtype(recordType1));
  }

  /**
   * Test RecordType's toMaybeRecordType method
   */
  @org.junit.Test
  public void testToMaybeRecordType() {
    RecordType recordType = new RecordType(registry, new HashMap<>(), true);

    RecordType maybeRecordType = recordType.toMaybeRecordType();

    assertEquals("RecordType", RecordType.class, maybeRecordType.getClass());
    assertEquals("RecordType", recordType.getJsFnString(), maybeRecordType.getJsFnString());
  }
}

enum EquivalenceMethod {
  IDENTITY,
  STRICT_PROTOTYPE_CHAIN,
  WEAK_PROTOTYPE_CHAIN,
  COVARIANT
}