import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.BeforeClass
  public static void setup() {
    // Set up the registry
    registry.put(String.class, String.TYPE);
    registry.put(Integer.TYPE, INT.TYPE);
    registry.put(Integer[].class, INT_ARRAY.TYPE);
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", STRING_TYPE));
    properties.put("age", new RecordProperty("age", INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getPrettyName(), "RecordType{name: String, age: int}");
    assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.isSynthetic(), false);

    // Test property access
    assertEquals(recordType.getPropertyType("name"), STRING_TYPE);
    assertEquals(recordType.getPropertyType("age"), INT_TYPE);

    // Test property exclusion
    RecordProperty excludedProp = new RecordProperty("excluded", INT_TYPE);
    RecordType excludedRecordType = new RecordType(registry, Arrays.asList(properties.get("name"), excludedProp), false);
    assertEquals(excludedRecordType.getPropertyType("excluded"), UNKNOWN_TYPE);
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", STRING_TYPE));
    properties1.put("age", new RecordProperty("age", INT_TYPE));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", STRING_TYPE));
    properties2.put("age", new RecordProperty("age", INT_TYPE));
    properties2.put("city", new RecordProperty("city", STRING_TYPE));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    // Test equality
    assertTrue(recordType1.equals(recordType1));
    assertTrue(recordType1.equals(recordType1));

    // Test non-equality
    assertFalse(recordType1.equals(null));
    assertFalse(recordType1.equals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test equivalence with another record type
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));

    // Test non-equivalence with a different record type
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("name", new RecordProperty("name", STRING_TYPE));
    properties3.put("age", new RecordProperty("age", INT_TYPE));
    properties3.put("gender", new RecordProperty("gender", BOOL_TYPE));
    RecordType recordType3 = new RecordType(registry, properties3, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, true));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", STRING_TYPE));
    properties1.put("age", new RecordProperty("age", INT_TYPE));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", STRING_TYPE));
    properties2.put("age", new RecordProperty("age", INT_TYPE));
    properties2.put("city", new RecordProperty("city", STRING_TYPE));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    // Test subtyping
    assertTrue(recordType1.isSubtype(recordType2));

    // Test non-subtyping
    JSType recordType3 = INT_TYPE;
    assertFalse(recordType1.isSubtype(recordType3));

    // Test non-subtyping due to different property types
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("name", new RecordProperty("name", STRING_TYPE));
    properties3.put("age", new RecordProperty("age", INT_TYPE));
    properties3.put("city", new RecordProperty("city", BOOL_TYPE));
    RecordType recordType3 = new RecordType(registry, properties3, true);

    assertFalse(recordType1.isSubtype(recordType3));

    // Test subtyping with a record type having an additional property
    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.put("name", new RecordProperty("name", STRING_TYPE));
    properties4.put("age", new RecordProperty("age", INT_TYPE));
    properties4.put("city", new RecordProperty("city", STRING_TYPE));
    RecordType recordType4 = new RecordType(registry, properties4, true);

    assertTrue(recordType1.isSubtype(recordType4));

    // Test subtyping with a record type having a property with a more specific type
    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("name", new RecordProperty("name", STRING_TYPE));
    properties5.put("age", new RecordProperty("age", INT_TYPE));
    properties5.put("city", new RecordProperty("city", INT_TYPE));
    RecordType recordType5 = new RecordType(registry, properties5, true);

    assertTrue(recordType1.isSubtype(recordType5));
  }

  @org.junit.Test
  public void testRecordTypeIsFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", STRING_TYPE));
    properties.put("age", new RecordProperty("age", INT_TYPE));
    RecordType recordType = new RecordType(registry, properties, true);

    // Test non-frozen state
    assertFalse(recordType.isFrozen());

    // Test freezing
    recordType.freeze();
    assertTrue(recordType.isFrozen());

    // Test immutability
    try {
      recordType.defineProperty("name", STRING_TYPE, false, Node.UNDEF);
      fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}