import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.JSTestCase;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest extends JSTestCase {

  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  /**
   * Test adding a property to an empty record type builder.
   */
  public void testAddPropertyToEmptyBuilder() {
    JSType recordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getType());

    // Add a property "name" with string type.
    JSType nameType = registry.getStringType();
    Node namePropertyNode = context.mkProperty("name", nameType);
    RecordProperty nameRecordProperty = builder.addProperty("name", nameType, namePropertyNode);

    // Check that the property was added to the builder.
    RecordType builtRecordType = (RecordType) builder.build();
    assertNotNull(nameRecordProperty);
    assertEquals("name", nameRecordProperty.getName());
    assertEquals(nameType, nameRecordProperty.getType());

    // Check that the property is present in the built record type.
    Map<String, RecordProperty> properties = builtRecordType.getProperties();
    assertFalse(properties.isEmpty());
    assertTrue(properties.containsKey("name"));
  }

  /**
   * Test adding a duplicate property to a record type builder.
   */
  public void testAddDuplicateProperty() {
    // Add a property "name" with string type.
    JSTypeName nameType = registry.getTypeFactory().createName("String");
    JSType nameTypeInstance = registry.getTypeFactory().getUnionType(nameType);
    Node namePropertyNode = context.mkProperty("name", nameTypeInstance);
    RecordProperty nameRecordProperty = builder.addProperty("name", nameTypeInstance, namePropertyNode);

    // Add a duplicate property "name".
    RecordProperty duplicateNameRecordProperty = builder.addProperty("name", nameTypeInstance, namePropertyNode);

    // Check that the second addition returns null.
    assertNull(duplicateNameRecordProperty);

    // Check that the property is only added once to the built record type.
    RecordType builtRecordType = (RecordType) builder.build();
    Map<String, RecordProperty> properties = builtRecordType.getProperties();
    assertEquals(1, properties.size());
    assertTrue(properties.containsKey("name"));
  }

  /**
   * Test building an empty record type.
   */
  public void testBuildEmptyRecordType() {
    JSType recordType = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getType());
  }

  /**
   * Test building a record type with one property.
   */
  public void testBuildRecordTypeWithOneProperty() {
    // Add a property "name" with string type.
    JSTypeName nameType = registry.getTypeFactory().createName("String");
    JSType nameTypeInstance = registry.getTypeFactory().getUnionType(nameType);
    Node namePropertyNode = context.mkProperty("name", nameTypeInstance);

    RecordProperty nameRecordProperty = builder.addProperty("name", nameTypeInstance, namePropertyNode);

    // Build the record type.
    RecordType builtRecordType = (RecordType) builder.build();

    // Check the properties.
    Map<String, RecordProperty> properties = builtRecordType.getProperties();
    assertEquals(1, properties.size());
    assertTrue(properties.containsKey("name"));
    assertEquals(nameRecordProperty, properties.get("name"));

    // Check the name property type.
    JSType namePropertyType = properties.get("name").getType();
    assertEquals(nameTypeInstance, namePropertyType);
  }

  /**
   * Test building a record type with multiple properties.
   */
  public void testBuildRecordTypeWithMultipleProperties() {
    // Add a property "name" with string type.
    JSTypeName nameType = registry.getTypeFactory().createName("String");
    JSType nameTypeInstance = registry.getTypeFactory().getUnionType(nameType);
    Node namePropertyNode = context.mkProperty("name", nameTypeInstance);
    RecordProperty nameRecordProperty = builder.addProperty("name", nameTypeInstance, namePropertyNode);

    // Add a property "age" with number type.
    JSTypeName numberType = registry.getTypeFactory().createName("Number");
    JSType numberTypeInstance = registry.getTypeFactory().getPrimitiveType(numberType);
    Node agePropertyNode = context.mkProperty("age", numberTypeInstance);
    RecordProperty ageRecordProperty = builder.addProperty("age", numberTypeInstance, agePropertyNode);

    // Build the record type.
    RecordType builtRecordType = (RecordType) builder.build();

    // Check the properties.
    Map<String, RecordProperty> properties = builtRecordType.getProperties();
    assertEquals(2, properties.size());
    assertTrue(properties.containsKey("name"));
    assertTrue(properties.containsKey("age"));

    // Check the name property type.
    JSType namePropertyType = properties.get("name").getType();
    assertEquals(nameTypeInstance, namePropertyType);

    // Check the age property type.
    JSType agePropertyType = properties.get("age").getType();
    assertEquals(numberTypeInstance, agePropertyType);
  }
}