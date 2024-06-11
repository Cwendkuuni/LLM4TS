import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Test
 public void testEmptyRecordType() {
 recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
 JSType recordType = recordTypeBuilder.build();
 Assertions.assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
 }

 @Test
 public void testBuildRecordTypeWithOneProperty() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);

 JSType numberType = registry.getBuiltins().getNumberType();
 Node propertyNode = new Node();
 propertyNode.setType(Node.INT_TYPE);

 RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap(
 "propertyName", new RecordProperty(numberType, propertyNode)));
 recordTypeBuilder = recordTypeBuilder.addProperty("propertyName", numberType, propertyNode);
 JSType actualRecordType = recordTypeBuilder.build();

 Assertions.assertEquals(expectedRecordType, actualRecordType);
 }

 @Test
 public void testBuildRecordTypeWithDuplicateProperty() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);

 JSType numberType = registry.getBuiltins().getNumberType();
 Node propertyNode1 = new Node();
 propertyNode1.setType(Node.INT_TYPE);

 JSType stringType = registry.getBuiltins().getStringType();
 Node propertyNode2 = new Node();
 propertyNode2.setType(Node.STRING_TYPE);

 recordTypeBuilder = recordTypeBuilder.addProperty("propertyName", numberType, propertyNode1);
 recordTypeBuilder = recordTypeBuilder.addProperty("propertyName", stringType, propertyNode2);

 Assertions.assertNull(recordTypeBuilder.build());
 }

 @Test
 public void testBuildRecordTypeWithTwoProperties() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);

 JSType numberType = registry.getBuiltins().getNumberType();
 Node propertyNode1 = new Node();
 propertyNode1.setType(Node.INT_TYPE);

 JSType stringType = registry.getBuiltins().getStringType();
 Node propertyNode2 = new Node();
 propertyNode2.setType(Node.STRING_TYPE);

 RecordType expectedRecordType = new RecordType(registry, Map.of(
 "propertyName1", new RecordProperty(numberType, propertyNode1),
 "propertyName2", new RecordProperty(stringType, propertyNode2)));
 recordTypeBuilder = recordTypeBuilder.addProperty("propertyName1", numberType, propertyNode1)
 .addProperty("propertyName2", stringType, propertyNode2);
 JSType actualRecordType = recordTypeBuilder.build();

 Assertions.assertEquals(expectedRecordType, actualRecordType);
 }
}