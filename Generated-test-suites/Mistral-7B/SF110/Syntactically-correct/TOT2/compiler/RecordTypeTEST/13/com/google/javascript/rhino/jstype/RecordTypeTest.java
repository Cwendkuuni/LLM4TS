import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.Node;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private Map<String, RecordProperty> properties;

  @Before
  public void setup() {
    registry = mock(JSTypeRegistry.class);
    properties = new HashMap<>();
  }

  @Test
  public void testRecordTypeConstructionWithDeclaredProperties() {
    RecordProperty recordProperty1 = new RecordProperty("name", JSType.STRING);
    properties.put("name", recordProperty1);

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(registry, recordType.getRegistry());
    assertEquals(Collections.unmodifiableSet(properties.keySet()),
        recordType.getOwnPropertyNames());
  }

  // ... (Add test cases for other methods)
}