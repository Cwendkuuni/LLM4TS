import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseTestCase;
import com.google.javascript.rhino.testing.TypeCheckMode;
import com.google.common.collect.Maps;
import static com.google.javascript.rhino.testing.Assertions.assertType;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest extends BaseTestCase {

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
 assertType(record, JSTypeNative.OBJECT_TYPE, TypeCheckMode.STRICT);
 }

 @Test
 public void testRecordWithOneProperty() {
 String propertyName = "name";
 JSType propertyType = JSType.STRING;
 Node propertyNode = createMockNode("name", JSType.STRING);

 builder.addProperty(propertyName, propertyType, propertyNode);
 JSType record = builder.build();

 Map<String, JSType> properties = record.getProperties();
 assertNotNull(properties);
 assertEquals(1, properties.size());
 assertEquals(propertyType, properties.get(propertyName));
 }

 @Test
 public void testRecordWithDuplicateProperty() {
 String propertyName = "name";
 JSType propertyType = JSType.STRING;
 Node propertyNode = createMockNode("name", JSType.STRING);

 builder.addProperty(propertyName, propertyType, propertyNode);
 builder.addProperty(propertyName, propertyType, propertyNode);

 JSType record = builder.build();
 assertNull(record);
 }

 @Test
 public void testRecordWithValidProperties() {
 String propertyName1 = "name";
 JSType propertyType1 = JSType.STRING;
 Node propertyNode1 = createMockNode("name", JSType.STRING);

 String propertyName2 = "age";
 JSType propertyType2 = JSType.NUMBER;
 Node propertyNode2 = createMockNode("age", JSType.NUMBER);

 builder.addProperty(propertyName1, propertyType1, propertyNode1);
 builder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType record = builder.build();

 Map<String, JSType> properties = record.getProperties();
 assertEquals(2, properties.size());
 assertEquals(propertyType1, properties.get(propertyName1));
 assertEquals(propertyType2, properties.get(propertyName2));
 }

 private Node createMockNode(String name, JSType type) {
 Node node = mockNode(Node.class);
 when(node.getName()).thenReturn(name);
 when(node.getType()).thenReturn(type);
 return node;
 }
}