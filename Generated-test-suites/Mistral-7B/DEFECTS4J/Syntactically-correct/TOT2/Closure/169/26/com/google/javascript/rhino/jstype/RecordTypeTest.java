import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

  @Test
  public void testRecordTypeWithBasicProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSType.STRING));
    RecordType recordType = new RecordType(registry, properties, true);

    // Basic functionality test
    RecordType expectedRecordType = new RecordTypeBuilder(registry)
        .addProperty("name", JSType.STRING)
        .build();
    assertTrue(recordType.isEquivalentTo(expectedRecordType));
  }

  // Edge cases and exception handling tests would be added here
}