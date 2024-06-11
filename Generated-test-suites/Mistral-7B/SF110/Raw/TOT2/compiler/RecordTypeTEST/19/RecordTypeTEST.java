import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
  // ... (import statements and class declaration omitted for brevity)

  @Test
  public void testRecordTypeConstruction() {
    Map<String, RecordProperty> properties = new HashMap<>();
    RecordType recordType = new RecordType(registry, properties);
    // Basic functionality check: ensure recordType is not null
    assertNotNull(recordType);

    // Edge case: empty properties map
    Map<String, RecordProperty> emptyProperties = new HashMap<>();
    RecordType emptyRecordType = new RecordType(registry, emptyProperties);
    // Check that the recordType is empty
    assertTrue(emptyRecordType.getOwnPropertyNames().isEmpty());

    // Exception handling: null properties map
    Map<String, RecordProperty> nullProperties = null;
    try {
      new RecordType(registry, nullProperties);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}