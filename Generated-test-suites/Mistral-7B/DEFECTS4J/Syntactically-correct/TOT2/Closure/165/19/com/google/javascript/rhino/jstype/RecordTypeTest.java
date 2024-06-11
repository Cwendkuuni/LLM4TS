import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {
  @Test
  public void testIsEquivalentTo() {
    // Create two RecordType instances with the same properties
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.STRING));
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("prop1", new RecordProperty("prop1", JSType.NUMBER));
    RecordType recordType2 = new RecordType(registry, properties);

    // Check that recordType1 is equivalent to recordType2
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }
}