import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    JSType emptyRecord = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecord.getClass());
  }

  @Test
  public void testRecordWithOneProperty() {
    JSType stringType = registry.getStringType();
    JSType recordType = builder
        .addProperty("name", stringType, null)
        .build();

    RecordProperty nameProperty = builder.properties.get("name");
    assertEquals(stringType, nameProperty.getType());
    assertNotNull(nameProperty.getPropertyNode());

    assertEquals(1, builder.properties.size());
    assertEquals(recordType.getClass(), RecordType.class);
  }

  @Test
  public void testRecordWithDuplicateProperty() {
    JSType stringType = registry.getStringType();
    builder.addProperty("name", stringType, null);
    assertNull(builder.addProperty("name", stringType, null));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordWithNullType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", null, null);
  }
}