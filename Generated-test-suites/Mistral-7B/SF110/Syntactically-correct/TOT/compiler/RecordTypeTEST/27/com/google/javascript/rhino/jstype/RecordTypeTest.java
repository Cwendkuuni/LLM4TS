import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    RecordProperty property1 = new RecordProperty("name", registry.getStringType());
    RecordProperty property2 = new RecordProperty("age", registry.getNumberType());
    properties.put("name", property1);
    properties.put("age", property2);

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("RecordType", recordType.getClassName());
    assertTrue(recordType.isRecordType());

    // Test getters
    assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));

    // Test defining and getting properties
    recordType.defineProperty("gender", registry.getStringType());
    assertEquals(registry.getStringType(), recordType.getPropertyType("gender"));

    // Test defining and getting properties when frozen
    recordType.isFrozen = true;
    try {
      recordType.defineProperty("gender", registry.getStringType());
      fail("Should not be able to define property on frozen RecordType");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    try {
      recordType.getPropertyType("gender");
      fail("Should not be able to get property on frozen RecordType");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test record equivalence
    Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
    RecordProperty property1_2 = new RecordProperty("name", registry.getStringType());
    RecordProperty property2_2 = new RecordProperty("age", registry.getNumberType());
    properties2.put("name", property1_2);
    properties2.put("age", property2_2);

    RecordType recordType2 = new RecordType(registry, properties2);
    assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.STRICT));

    // Test record equivalence with different order of properties
    RecordType recordType3 = new RecordType(registry, properties2);
    RecordType recordType4 = new RecordType(registry, properties);
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.STRICT));

    // Test record equivalence with different property types
    RecordProperty property2_3 = new RecordProperty("age", registry.getBooleanType());
    properties3.put("age", property2_3);

    RecordType recordType5 = new RecordType(registry, properties3);
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType5, EquivalenceMethod.STRICT));

    // Test record equivalence with missing property
    RecordProperty missingProperty = new RecordProperty("missing", registry.getStringType());
    properties4.put("missing", missingProperty);

    RecordType recordType6 = new RecordType(registry, properties4);
    assertFalse(recordType.checkRecordEquivalenceHelper(recordType6, EquivalenceMethod.STRICT));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
    RecordProperty property1_1 = new RecordProperty("name", registry.getStringType());
    properties1.put("name", property1_1);

    Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
    RecordProperty property2_1 = new RecordProperty("name", registry.getStringType());
    RecordProperty property2_2 = new RecordProperty("age", registry.getNumberType());
    properties2.put("name", property2_1);
    properties2.put("age", property2_2);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Test isSubtype
    assertTrue(recordType1.isSubtype(recordType2));

    // Test getGreatestSubtypeHelper
    assertEquals(recordType2, recordType1.getGreatestSubtypeHelper(recordType2));
    assertEquals(recordType1, recordType2.getGreatestSubtypeHelper(recordType1));

    // Test getGreatestSubtypeHelper with different property types
    Map<String, RecordProperty> properties3 = new java.util.HashMap<>();
    RecordProperty property3_1 = new RecordProperty("name", registry.getStringType());
    RecordProperty property3_2 = new RecordProperty("age", registry.getBooleanType());
    properties3.put("name", property3_1);
    properties3.put("age", property3_2);
    RecordType recordType3 = new RecordType(registry, properties3);
    RecordType recordType4 = new RecordType(registry, properties2);
    RecordType recordType5 = recordType1.getGreatestSubtypeHelper(recordType4);
    assertEquals(recordType2, recordType5);

    // Test getGreatestSubtypeHelper with missing property
    Map<String, RecordProperty> properties4 = new java.util.HashMap<>();
    RecordProperty property4_1 = new RecordProperty("name", registry.getStringType());
    properties4.put("name", property4_1);
    RecordType recordType6 = new RecordType(registry, properties4);
    RecordType recordType7 = recordType1.getGreatestSubtypeHelper(recordType6);
    assertEquals(recordType1, recordType7);
  }
}