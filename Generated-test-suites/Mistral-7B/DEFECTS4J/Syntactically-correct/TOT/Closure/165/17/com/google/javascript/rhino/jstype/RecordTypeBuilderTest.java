import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType numberType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    Node propertyNode = createNode("prop1", INT_LITERAL, 1);

    RecordTypeBuilder recordBuilder = builder.addProperty("prop1", numberType, propertyNode)
        .addProperty("prop2", stringType, null);
    JSType recordType = recordBuilder.build();

    RecordType expectedRecordType = new RecordType(registry,
        ImmutableMap.of("prop1", new RecordProperty(numberType, propertyNode),
          "prop2", new RecordProperty(stringType, null)));

    assertEquals(expectedRecordType.getClass(), recordType.getClass());
    assertEquals(expectedRecordType.getProperties().size(), recordType.getProperties().size());
    for (RecordProperty prop : expectedRecordType.getProperties().values()) {
      assertEquals(prop.getName(), recordType.getProperties().get(prop.getName()).getName());
      assertEquals(prop.getType().getClass(), recordType.getProperties().get(prop.getName()).getType().getClass());
    }
  }

  @Test
  public void testEmptyRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass(), recordType.getClass());
  }

  private Node createNode(String name, int nodeType, int value) {
    Node node = new Node();
    node.setType(nameNode(name));
    node.setType(new ArrayType(node.getType(), INT_TYPE));
    node.setInt32(value);
    return node;
  }
}