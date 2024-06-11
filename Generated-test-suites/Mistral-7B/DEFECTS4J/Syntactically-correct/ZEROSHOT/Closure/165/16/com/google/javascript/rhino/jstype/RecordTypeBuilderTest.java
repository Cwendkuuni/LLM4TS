import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private Node propertyNode;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 }

 @Test
 public void testEmptyRecord() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
 }

 @Test
 public void testBuildRecordWithOneProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 JSType propertyType = registry.getStringType();
 Node propertyNode = Node.newObjectLiteral();
 propertyNode.putProperty("name", Node.newString("John"));
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = recordTypeBuilder.build();
 assertTrue(recordType instanceof RecordType);
 RecordType record = (RecordType) recordType;
 Map<String, RecordProperty> properties = record.getProperties();
 assertEquals(1, properties.size());
 RecordProperty property = properties.get(propertyName);
 assertEquals(propertyType, property.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithDuplicateName() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 JSType propertyType = registry.getStringType();
 Node propertyNode = Node.newObjectLiteral();
 propertyNode.putProperty("name", Node.newString("John"));
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullPropertyType() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 String propertyName = "name";
 Node propertyNode = Node.newObjectLiteral();
 propertyNode.putProperty("name", Node.newString("John"));
 recordTypeBuilder.addProperty(propertyName, null, propertyNode);
 }
}