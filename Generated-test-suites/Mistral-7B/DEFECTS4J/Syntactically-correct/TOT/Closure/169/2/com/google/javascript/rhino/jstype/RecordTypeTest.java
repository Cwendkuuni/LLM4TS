import com.google.javascript.rhino.JSCoreTypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertEquals("{ name : String, age : Number }", recordType.toString());
  }

  @Test
  public void testRecordTypeGetters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);

    assertEquals(JSCoreTypes.STRING_TYPE, recordType.getPropertyType("name"));
    assertEquals(JSCoreTypes.NUMBER_TYPE, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordAProperties = new HashMap<>();
    recordAProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordAProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    RecordType recordAType = new RecordType(registry, recordAProperties, true);

    Map<String, RecordProperty> recordBProperties = new HashMap<>();
    recordBProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordBProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    recordBProperties.put("city", new RecordProperty("city", JSCoreTypes.STRING_TYPE));
    RecordType recordBType = new RecordType(registry, recordBProperties, true);

    assertTrue(recordAType.isSubtype(recordAType));
    assertTrue(recordBType.isSubtype(recordAType));
    assertTrue(recordAType.isSubtype(recordBType));

    JSType recordCType = new ObjectType(registry, null, null);
    assertFalse(recordCType.isSubtype(recordAType));
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordAProperties = new HashMap<>();
    recordAProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordAProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    RecordType recordAType = new RecordType(registry, recordAProperties, true);

    Map<String, RecordProperty> recordBProperties = new HashMap<>();
    recordBProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    recordBProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    RecordType recordBType = new RecordType(registry, recordBProperties, true);

    assertTrue(recordAType.checkRecordEquivalenceHelper(recordBType, true));
  }

  @Test
  public void testRecordTypeWithNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", null);

    try {
      new RecordType(registry, recordProperties, true);
      fail("Expected exception: IllegalStateException");
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testRecordTypeWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    new RecordType(registry, recordProperties, true);
    assertNull(recordProperties);
  }

  @Test
  public void testRecordTypeWithMismatchedPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.BOOLEAN_TYPE));

    try {
      new RecordType(registry, recordProperties, true);
      fail("Expected exception: IllegalStateException");
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testRecordTypeWithDuplicatePropertyNames() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.NUMBER_TYPE));

    try {
      new RecordType(registry, recordProperties, true);
      fail("Expected exception: IllegalStateException");
    } catch (IllegalStateException e) {
      assertNotNull(e.getMessage());
    }
  }

  @Test
  public void testRecordTypeWithUnknownProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.UNKNOWN_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    otherRecordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecordType, false));
  }

  @Test
  public void testRecordTypeWithUnknownPropertiesAndTolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.UNKNOWN_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    otherRecordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecordType, true));
  }

  @Test
  public void testRecordTypeWithMissingProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecordType, false));
  }

  @Test
  public void testRecordTypeWithMissingPropertiesAndTolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecordType, true));
  }

  @Test
  public void testRecordTypeWithExtraProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    otherRecordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    otherRecordProperties.put("city", new RecordProperty("city", JSCoreTypes.STRING_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecordType, false));
  }

  @Test
  public void testRecordTypeWithExtraPropertiesAndTolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    recordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    Map<String, RecordProperty> otherRecordProperties = new HashMap<>();
    otherRecordProperties.put("name", new RecordProperty("name", JSCoreTypes.STRING_TYPE));
    otherRecordProperties.put("age", new RecordProperty("age", JSCoreTypes.NUMBER_TYPE));
    otherRecordProperties.put("city", new RecordProperty("city", JSCoreTypes.STRING_TYPE));

    RecordType otherRecordType = new RecordType(registry, otherRecordProperties, true);

    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecordType, true));
  }
}