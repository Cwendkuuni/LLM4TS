import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Map;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new Map<>();

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(1L, recordType.getSerialVersionUID());
    assertFalse(recordType.isFrozen());
  }

  @Test
  public void testInvalidJSTypeRegistry() {
    Map<String, RecordProperty> properties = new Map<>();

    try {
      new RecordType(null, properties);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testMapContainsNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new Map<>();
    properties.put("property1", null);

    try {
      new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testMapContainsDuplicateKeys() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new Map<>();
    properties.put("property1", new RecordProperty("type1", null));
    properties.put("property1", new RecordProperty("type2", null));

    try {
      new RecordType(registry, properties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testDeclaredParameterAndEmptyMap() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new Map<>();

    try {
      new RecordType(registry, properties, true);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testDeclaredParameterAndNonEmptyMap() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new Map<>();
    properties.put("property1", new RecordProperty("type1", null));

    RecordType recordType = new RecordType(registry, properties, false);

    // Test if properties have been set correctly
    assertFalse(recordType.isFrozen());
    assertTrue(recordType.hasProperty("property1"));
    assertEquals("type1", recordType.getPropertyType("property1").getName());
  }
}