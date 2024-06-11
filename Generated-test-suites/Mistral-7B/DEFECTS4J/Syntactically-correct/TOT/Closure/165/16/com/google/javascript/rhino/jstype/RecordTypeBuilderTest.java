import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    JSType stringType = registry.getBuiltins().getStringType();
    JSType recordType = recordTypeBuilder
        .addProperty("name", stringType, null)
        .build();

    assertEquals(RecordType.class, recordType.getClass());
    RecordProperty nameProperty = recordType.getProperties().get("name");
    assertNotNull(nameProperty);
    assertEquals(stringType, nameProperty.getType());
  }

  @Test
  public void testBuildRecordWithDuplicateProperty() {
    JSType stringType = registry.getBuiltins().getStringType();
    JSType recordType = recordTypeBuilder
        .addProperty("name", stringType, null)
        .addProperty("name", stringType, null)
        .build();

    assertNotNull(recordType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testBuildRecordAfterEmptyListIsSet() {
    recordTypeBuilder.build();
    recordTypeBuilder.addProperty("name", null, null);
  }
}