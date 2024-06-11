import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testEmptyRecord() {
 JSType recordType = builder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
 }

 @Test
 public void testAddProperty() {
 String name = "name";
 JSType intType = registry.getIntType();
 Node propertyNode = createNode("property", "name", "int");
 builder.addProperty(name, intType, propertyNode);
 JSType recordType = builder.build();
 RecordType record = (RecordType) recordType;
 RecordProperty property = record.getProperties().get(name);
 assertEquals(intType, property.getType());
 }

 @Test
 public void testAddDuplicateProperty() {
 String name = "name";
 JSType intType = registry.getIntType();
 Node propertyNode = createNode("property", "name", "int");
 builder.addProperty(name, intType, propertyNode);
 RecordProperty property = builder.addProperty(name, intType, propertyNode);
 assertNull(property);
 }

 @Test
 public void testBuildRecordWithProperties() {
 String name1 = "name1";
 String name2 = "name2";
 JSType intType = registry.getIntType();
 JSType stringType = registry.getStringType();
 Node propertyNode1 = createNode("property", "name1", "int");
 Node propertyNode2 = createNode("property", "name2", "string");
 builder.addProperty(name1, intType, propertyNode1);
 builder.addProperty(name2, stringType, propertyNode2);
 JSType recordType = builder.build();
 RecordType record = (RecordType) recordType;
 Map<String, RecordProperty> properties = record.getProperties();
 assertEquals(2, properties.size());
 RecordProperty property1 = properties.get(name1);
 assertEquals(intType, property1.getType());
 RecordProperty property2 = properties.get(name2);
 assertEquals(stringType, property2.getType());
 }

 private Node createNode(String type, String name, String nodeType) {
 Node propertyNode = new Node_anon();
 propertyNode.setType(Node.NNUM);
 propertyNode.setClassName("PropertyDescriptor");
 propertyNode.setProperty("name", new Node_anon().setType(Node.STRING).setValue(name));
 propertyNode.setProperty("type", new Node_anon().setType(Node.NAME).setValue(type));
 propertyNode.setProperty("nodeType", new Node_anon().setType(Node.NAME).setValue(nodeType));
 return propertyNode;
 }

}