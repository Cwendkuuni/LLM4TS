import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.testing.PolymerTestingRuntime;
import com.google.javascript.rhino.testing.TypeChecker;

import java.util.Map;

public class RecordTypeBuilderTest extends JSTestCase {

  private static final RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);

  /**
   * Test adding a property to an empty record type.
   */
  public void testAddPropertyToEmptyRecord() {
    JSType recordType = recordTypeBuilder.build();
    assertInstanceof(recordType, JSTypeNative.OBJECT_TYPE);

    RecordTypeBuilder newRecordBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    JSType newRecordType = newRecordBuilder.addProperty("name", TypeChecker.STRING, null).build();
    assertNotNull(newRecordType);
    assertNotSame(recordType, newRecordType);
  }

  /**
   * Test adding a duplicate property to a record type.
   */
  public void testAddDuplicateProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);

    try {
      recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);
      fail("Should not be able to add a duplicate property");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test building an empty record type and then adding a property.
   */
  public void testBuildEmptyRecordThenAddProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    JSType emptyRecordType = recordTypeBuilder.build();
    assertInstanceof(emptyRecordType, JSTypeNative.OBJECT_TYPE);

    RecordProperty property = recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);
    JSType recordType = recordTypeBuilder.build();

    assertEquals(ImmutableMap.of("name", property.getType()), recordType.getProperties());
  }

  /**
   * Test building a record type with multiple properties.
   */
  public void testBuildRecordTypeWithMultipleProperties() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);
    recordTypeBuilder.addProperty("age", TypeChecker.NUMBER, null);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(2, recordType.getProperties().size());

    PolymerTestingRuntime runtime = new PolymerTestingRuntime();
    runtime.init();
    JSType expectedRecordType = runtime.getGlobal("RecordTypeBuilderTest").getMember("expectedRecordType");
    assertEquals(expectedRecordType, recordType);
  }

  /**
   * Test building a record type with a malformed property node.
   */
  public void testBuildRecordTypeWithMalformedPropertyNode() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    Node malformedPropertyNode = new Node(); // Assume malformed property node

    try {
      recordTypeBuilder.addProperty("name", TypeChecker.STRING, malformedPropertyNode);
      fail("Should not be able to add a property with a malformed node");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Test building a record type with a string as property type.
   */
  public void testBuildRecordTypeWithStringProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTypeNative.STRING, recordType.getProperties().get("name"));
  }

  /**
   * Test building a record type with a number as property type.
   */
  public void testBuildRecordTypeWithNumberProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("age", TypeChecker.NUMBER, null);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTypeNative.NUMBER, recordType.getProperties().get("age"));
  }

  /**
   * Test building a record type with a boolean as property type.
   */
  public void testBuildRecordTypeWithBooleanProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("isEmployee", TypeChecker.BOOLEAN, null);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTypeNative.BOOLEAN, recordType.getProperties().get("isEmployee"));
  }

  /**
   * Test building a record type with a custom type as property type.
   */
  public void testBuildRecordTypeWithCustomTypeProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    JSType customType = TypeChecker.createNamedType("MyCustomType");
    recordTypeBuilder.addProperty("myProperty", customType, null);

    JSType recordType = recordTypeBuilder.build();
    assertEquals(customType, recordType.getProperties().get("myProperty"));
  }

  /**
   * Test getting the properties of a record type.
   */
  public void testGetRecordTypeProperties() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(TypeChecker.TYPECHECKER);
    recordTypeBuilder.addProperty("name", TypeChecker.STRING, null);
    recordTypeBuilder.addProperty("age", TypeChecker.NUMBER, null);

    JSType recordType = recordTypeBuilder.build();
    Map<String, JSType> expectedProperties = ImmutableMap.of("name", TypeChecker.STRING, "age", TypeChecker.NUMBER);
    assertEquals(expectedProperties, recordType.getProperties());
  }
}