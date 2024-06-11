import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSTestCase.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest extends JSTestCase {

  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Override
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  /**
   * Test building an empty record type.
   */
  public void testEmptyRecord() {
    JSType type = builder.build();
    assertEquals(JSTypeNative.OBJECT_TYPE, type.getNativeType());
  }

  /**
   * Test building a record type with one property.
   */
  public void testSinglePropertyRecord() {
    JSType stringType = registry.getSimpleType("String");
    builder.addProperty("name", stringType, null);
    JSType recordType = builder.build();

    Map<String, RecordProperty> properties = recordType.asRecordType().getProperties();
    assertEquals(1, properties.size());
    RecordProperty property = properties.values().iterator().next();
    assertEquals(stringType, property.getType());
  }

  /**
   * Test building a record type with multiple properties.
   */
  public void testMultiplePropertiesRecord() {
    JSType stringType = registry.getSimpleType("String");
    JSType numberType = registry.getSimpleType("Number");

    builder.addProperty("name", stringType, null);
    builder.addProperty("age", numberType, null);
    JSType recordType = builder.build();

    Map<String, RecordProperty> properties = recordType.asRecordType().getProperties();
    assertEquals(2, properties.size());
  }

  /**
   * Test adding duplicate property to record type.
   */
  public void testDuplicateProperty() {
    JSType stringType = registry.getSimpleType("String");
    builder.addProperty("name", stringType, null);
    assertNull(builder.addProperty("name", stringType, null));
  }

  /**
   * Test accessing property node of a record property.
   */
  public void testAccessPropertyNode() {
    String propertyName = "property";
    JSType stringType = registry.getSimpleType("String");
    Node propertyNode = newNode("{'name': 'value'}");
    builder.addProperty(propertyName, stringType, propertyNode);

    RecordType recordType = (RecordType) builder.build();
    RecordProperty recordProperty = recordType.getProperties().values().iterator().next();
    Node actualPropertyNode = recordProperty.getPropertyNode();

    assertEquals(propertyNode, actualPropertyNode);
  }
}