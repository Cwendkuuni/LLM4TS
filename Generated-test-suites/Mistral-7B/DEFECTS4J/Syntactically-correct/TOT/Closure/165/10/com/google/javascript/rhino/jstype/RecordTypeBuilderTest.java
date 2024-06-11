import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType actual = recordTypeBuilder.build();
 JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildRecordWithSingleProperty() {
 JSType intType = registry.getPrimitiveType(JSTypeNative.INT_TYPE);
 Node propertyNode = Node.objectLiteral();
 propertyNode.setProperty("name", Node.string("age"));
 propertyNode.setProperty("value", Node.number(42));

 RecordProperty expectedProperty = new RecordProperty(intType, propertyNode);
 recordTypeBuilder.addProperty("age", intType, propertyNode);

 JSType actual = recordTypeBuilder.build();
 RecordType expectedRecord = new RecordType(registry,
 Collections.singletonMap("age", expectedProperty));
 assertEquals(expectedRecord.getClass(), actual.getClass());
 Map<String, RecordProperty> actualProperties =
 ((RecordType) actual).getProperties();
 assertEquals(1, actualProperties.size());
 assertEquals(expectedProperty, actualProperties.get("age"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildRecordWithDuplicateProperty() {
 JSType intType = registry.getPrimitiveType(JSTypeNative.INT_TYPE);
 Node propertyNode = Node.objectLiteral();
 propertyNode.setProperty("name", Node.string("age"));
 propertyNode.setProperty("value", Node.number(42));

 recordTypeBuilder.addProperty("age", intType, propertyNode);
 recordTypeBuilder.addProperty("age", intType, propertyNode);
 }
}