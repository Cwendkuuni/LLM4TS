import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordTypeConstructionWithValidProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.HashMap<>();
    properties.put("property1", new RecordProperty("property1", new IntType()));
    properties.put("property2", new RecordProperty("property2", new StringType()));

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals(2, recordType.getOwnPropertyNames().size());
  }

  @Test
  public void testRecordTypeConstructionWithEmptyProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.HashMap<>();

    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
    assertEquals(0, recordType.getOwnPropertyNames().size());
  }

  @Test
  public void testRecordTypeConstructionWithNullProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = null;

    try {
      new RecordType(registry, properties);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
}