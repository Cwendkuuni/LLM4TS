import static org.junit.Assert.*;
import static com.google.javascript.rhino.JDTester.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.Map;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  /**
   * Test case for creating a simple RecordType with two properties.
   */
  @Test
  public void testSimpleRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getIntegerType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("An object with two properties x and y.", recordType.toString());

    // Check properties
    assertEquals(registry.getIntegerType(), recordType.getPropertyType("x"));
    assertEquals(registry.getStringType(), recordType.getPropertyType("y"));

    // Check type equivalence
    RecordType recordType2 = new RecordType(registry, properties, true);
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, false));

    // Check subtyping
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    RecordType recordType4 = new RecordType(registry, properties, true);
    RecordType recordType5 = new RecordType(registry, Maps.newHashMap(), true);
    RecordType recordType6 = new RecordType(registry, properties, false);

    assertTrue(recordType4.isSubtype(recordType3));
    assertTrue(recordType4.isSubtype(recordType6));

    assertFalse(recordType3.isSubtype(recordType4));
    assertFalse(recordType6.isSubtype(recordType4));

    // Check greatest subtype
    JSType greatestSubtype = recordType4.getGreatestSubtype(recordType6);
    assertEquals(recordType4.getClass(), greatestSubtype.getClass());

    // Check record type with inferred properties
    properties.clear();
    properties.put("x", new RecordProperty("x", registry.getIntegerType()));
    properties.put("y", new RecordProperty("y", registry.getStringType()));

    RecordType recordType7 = new RecordType(registry, properties, false);

    // Check type equivalence
    assertTrue(recordType4.checkRecordEquivalenceHelper(recordType7, false));

    // Check subtyping
    assertTrue(recordType4.isSubtype(recordType7));
    assertFalse(recordType7.isSubtype(recordType4));

    // Check greatest subtype
    JSType greatestSubtype2 = recordType4.getGreatestSubtype(recordType7);
    assertEquals(recordType4.getClass(), greatestSubtype2.getClass());
  }

  /**
   * Test case for creating a RecordType with duplicate properties.
   */
  @Test
  public void testDuplicateProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getIntegerType()));
    properties.put("x", new RecordProperty("x", registry.getStringType()));

    try {
      new RecordType(registry, properties, true);
      fail("Expected IllegalStateException due to duplicate properties.");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  /**
   * Test case for creating a RecordType with a missing RecordProperty.
   */
  @Test
  public void testMissingRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getIntegerType()));

    try {
      new RecordType(registry, Maps.newHashMap(), true);
      fail("Expected IllegalStateException due to missing RecordProperty.");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  /**
   * Test case for creating a RecordType with an invalid RecordProperty.
   */
  @Test
  public void testInvalidRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", null);

    try {
      new RecordType(registry, properties, true);
      fail("Expected IllegalStateException due to null RecordProperty.");
    } catch (IllegalStateException e) {
      // expected
    }
  }
}