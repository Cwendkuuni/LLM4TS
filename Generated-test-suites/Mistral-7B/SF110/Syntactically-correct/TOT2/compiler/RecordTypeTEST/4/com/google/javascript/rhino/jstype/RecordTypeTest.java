import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {
  @Test
  public void testRecordTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new java.util.LinkedHashMap<>();
    properties.put("property1", new RecordProperty("property1", String.class));
    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(String.class, recordType.getPropertyType("property1"));
  }
}