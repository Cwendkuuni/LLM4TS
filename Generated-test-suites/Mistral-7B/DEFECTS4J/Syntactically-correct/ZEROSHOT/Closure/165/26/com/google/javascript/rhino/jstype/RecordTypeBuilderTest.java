import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

 @Test
 public void testEmptyRecordTypeBuilder() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
 JSType recordType = recordTypeBuilder.build();
 assertInstanceOf(JSTypeNative.OBJECT_TYPE, recordType);
 }

 @Test
 public void testRecordTypeBuilderWithOneProperty() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
 String propertyName = "testProperty";
 JSType propertyType = JSType.STRING;
 Node propertyNode = new Node(); // A dummy node for testing purpose
 RecordTypeBuilder addedProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 JSType recordType = addedProperty.build();
 RecordType expectedRecordType = new RecordType(
 new RecordTypeRegistry(Collections.emptyMap()), Collections.singletonMap(propertyName, new RecordProperty(propertyType, propertyNode)));
 assertInstanceOf(RecordType.class, recordType);
 assertEquals(expectedRecordType, recordType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRecordTypeBuilderWithDuplicatePropertyName() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
 String propertyName = "testProperty";
 JSType propertyType = JSType.STRING;
 Node propertyNode = new Node(); // A dummy node for testing purpose
 RecordTypeBuilder addedProperty1 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 RecordProperty addedProperty2 = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 }

 @Test
 public void testRecordTypeBuilderWithMultipleProperties() {
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(null);
 String propertyName1 = "property1";
 JSType propertyType1 = JSType.NUMBER;
 Node propertyNode1 = new Node(); // A dummy node for testing purpose
 String propertyName2 = "property2";
 JSType propertyType2 = JSType.BOOLEAN;
 Node propertyNode2 = new Node(); // A dummy node for testing purpose
 RecordTypeBuilder addedProperty1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
 RecordTypeBuilder addedProperty2 = recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);
 JSType recordType = addedProperty2.build();
 RecordType expectedRecordType = new RecordType(
 new RecordTypeRegistry(Collections.emptyMap()), Maps.newHashMap(
     Maps.entry(propertyName1, new RecordProperty(propertyType1, propertyNode1)),
     Maps.entry(propertyName2, new RecordProperty(propertyType2, propertyNode2))));
 assertInstanceOf(RecordType.class, recordType);
 assertEquals(expectedRecordType, recordType);
 }
}