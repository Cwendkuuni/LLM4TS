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
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyWithEmptyRecord() {
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getClass());
 }

 @Test(expected = NullPointerException.class)
 public void testAddDuplicateProperty() {
 String propertyName = "testProperty";
 recordTypeBuilder.addProperty(propertyName, new IntType(registry), null);
 recordTypeBuilder.addProperty(propertyName, new StringType(registry), null);
 }

 @Test
 public void testAddPropertyToRecord() {
 String propertyName = "testProperty";
 JSType propertyType = new IntType(registry);
 Node propertyNode = new IntLiteralNode(1);
 RecordProperty recordProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 assertNotNull(recordProperty);
 assertEquals(propertyType, recordProperty.getType());
 assertNotNull(recordProperty.getPropertyNode());
 }

 @Test
 public void testBuildRecordType() {
 String propertyName1 = "property1";
 String propertyName2 = "property2";
 JSType propertyType1 = new IntType(registry);
 JSType propertyType2 = new StringType(registry);
 Node propertyNode1 = new IntLiteralNode(1);
 Node propertyNode2 = new StringLiteralNode("test");
 recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
 recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType recordType = recordTypeBuilder.build();
 assertNotNull(recordType);
 assertFalse(recordType instanceof JSTypeNative);
 RecordType record = (RecordType) recordType;
 assertEquals(2, record.getPropertyNames().size());
 assertEquals(propertyName1, record.getPropertyNames().get(0));
 assertEquals(propertyType1, record.getProperties().get(propertyName1).getType());
 assertEquals(propertyName2, record.getPropertyNames().get(1));
 assertEquals(propertyType2, record.getProperties().get(propertyName2).getType());
 }
}