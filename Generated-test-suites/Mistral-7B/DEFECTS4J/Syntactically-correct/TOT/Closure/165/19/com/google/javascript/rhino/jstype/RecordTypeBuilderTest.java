import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testEmptyRecord() {
    JSType record = builder.build();
    assertNotNull(record);
    assertTrue(record instanceof JSTypeNative &&
               record.getClass() == JSTypeNative.OBJECT_TYPE);
  }

  @Test
  public void testSimpleRecord() {
    JSType stringType = registry.getBuiltins().getStringType();
    JSType intType = registry.getBuiltins().getIntType();

    builder.addProperty("name", stringType, null);
    builder.addProperty("age", intType, null);

    JSType record = builder.build();
    assertNotNull(record);
    assertTrue(record instanceof RecordType);

    RecordType recordType = (RecordType) record;
    Map<String, RecordProperty> properties = recordType.getProperties();
    assertEquals("name", properties.keySet().iterator().next());
    assertEquals(stringType, properties.get("name").getType());

    assertEquals("age", properties.keySet().iterator().next());
    assertEquals(intType, properties.get("age").getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testDuplicateProperty() {
    JSType stringType = registry.getBuiltins().getStringType();

    builder.addProperty("name", stringType, null);
    builder.addProperty("name", stringType, null);

    builder.build();
  }
}