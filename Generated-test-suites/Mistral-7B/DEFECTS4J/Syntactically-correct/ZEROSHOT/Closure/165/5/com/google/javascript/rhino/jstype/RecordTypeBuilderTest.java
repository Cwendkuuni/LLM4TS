import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.JSTypeRegistryBuilder;

public class RecordTypeBuilderTest extends BaseTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistryBuilder().build();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    JSType stringType = registry.getStringType();
    Node propertyNode = createMockNode("name: string");

    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name", stringType, propertyNode);

    assertNotNull(builder);
    assertEquals(1, recordTypeBuilder.properties.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testAddPropertyWithDuplicateName() {
    JSType stringType = registry.getStringType();
    Node propertyNode = createMockNode("name: string");

    recordTypeBuilder.addProperty("name", stringType, propertyNode);

    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name", stringType, propertyNode);

    // Should throw an exception
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = recordTypeBuilder.build();

    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
  }

  @Test
  public void testBuildRecordWithProperties() {
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();

    Node nameNode = createMockNode("name: string");
    Node ageNode = createMockNode("age: number");

    recordTypeBuilder.addProperty("name", stringType, nameNode);
    recordTypeBuilder.addProperty("age", numberType, ageNode);

    JSType recordType = recordTypeBuilder.build();

    Map<String, JSType> expectedMap = new HashMap<>();
    expectedMap.put("name", stringType);
    expectedMap.put("age", numberType);

    assertEquals(expectedMap.getClass(), recordType.getType());
  }

  private Node createMockNode(String definition) {
    Node node = mock(Node.class);
    when(node.toSource()).thenReturn(definition);
    return node;
  }
}