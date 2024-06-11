import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest extends JSTestCase {

  private static final String RECORD_TYPE_BUILDER_CLASS_NAME = "RecordTypeBuilder";

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  // Test case for addProperty method with valid arguments
  public void testAddProperty() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property2 = recordTypeBuilder.addProperty("age", numberType, createMockNode("age", "10"));

    assertNotNull(property1);
    assertNotNull(property2);
    assertNotNull(recordTypeBuilder.build());
  }

  // Test case for addProperty method with duplicate property name
  public void testAddPropertyDuplicate() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"John\""));

    assertNotNull(property1);

    RecordProperty property2 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"Jane\""));

    assertNull(property2);
    assertNotNull(recordTypeBuilder.build());
  }

  // Test case for build method with empty record
  public void testBuildEmptyRecord() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType recordType = recordTypeBuilder.build();

    assertEquals(objectType, recordType);
  }

  // Test case for build method with non-empty record
  public void testBuildNonEmptyRecord() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property2 = recordTypeBuilder.addProperty("age", numberType, createMockNode("age", "10"));

    JSType recordType = recordTypeBuilder.build();

    RecordType expectedRecordType = new RecordType(registry,
        Collections.unmodifiableMap(Collections.singletonMap("name", property1)
            .put("age", property2)));

    assertEquals(expectedRecordType.getClass(), recordType.getClass());
  }

  // Test case for build method with invalid arguments
  public void testBuildInvalidArguments() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    try {
      recordTypeBuilder.build();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  private Node createMockNode(String name, String value) {
    return new Object() {
      @SuppressWarnings("unused")
      Node asNode() {
        return new Object() {
          @SuppressWarnings("unused")
          String getName() {
            return name;
          }

          @SuppressWarnings("unused")
          String getValueString() {
            return value;
          }
        };
      }
    }.asNode();
  }

  // Test case for toString method of RecordType
  public void testToString() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property2 = recordTypeBuilder.addProperty("age", numberType, createMockNode("age", "10"));

    JSType recordType = recordTypeBuilder.build();

    String result = recordType.toString();

    String expectedResult = String.format("{%s: %s, %s: %s}", "name", "string", "age", "number");

    assertEquals(expectedResult, result);
  }

  // Test case for equals method of RecordType
  public void testEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder1 = new RecordTypeBuilder(registry);
    RecordTypeBuilder recordTypeBuilder2 = new RecordTypeBuilder(registry);

    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder1.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property2 = recordTypeBuilder1.addProperty("age", numberType, createMockNode("age", "10"));

    JSType recordType1 = recordTypeBuilder1.build();

    RecordProperty property3 = recordTypeBuilder2.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property4 = recordTypeBuilder2.addProperty("age", numberType, createMockNode("age", "11"));

    JSType recordType2 = recordTypeBuilder2.build();

    RecordType recordType3 = new RecordType(registry,
        Collections.unmodifiableMap(Collections.singletonMap("name", property1)
            .put("age", property2)));

    RecordType recordType4 = new RecordType(registry,
        Collections.unmodifiableMap(Collections.singletonMap("name", property3)
            .put("age", property4)));

    assertTrue(recordType1.equals(recordType3));
    assertFalse(recordType1.equals(recordType2));
  }

  // Test case for hashCode method of RecordType
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, createMockNode("name", "\"John\""));
    RecordProperty property2 = recordTypeBuilder.addProperty("age", numberType, createMockNode("age", "10"));

    JSType recordType = recordTypeBuilder.build();

    RecordType recordTypeCopy = new RecordType(registry,
        Collections.unmodifiableMap(Collections.singletonMap("name", property1)
            .put("age", property2)));

    int recordTypeHashCode = recordType.hashCode();
    int recordTypeCopyHashCode = recordTypeCopy.hashCode();

    assertEquals(recordTypeHashCode, recordTypeCopyHashCode);
  }
}