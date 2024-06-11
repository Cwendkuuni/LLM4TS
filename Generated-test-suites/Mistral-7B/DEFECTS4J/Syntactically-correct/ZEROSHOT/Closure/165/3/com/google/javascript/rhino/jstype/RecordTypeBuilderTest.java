import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder recordTypeBuilder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 recordTypeBuilder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddPropertyAndBuildEmptyRecord() {
 JSType recordType = recordTypeBuilder.build();
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getTypeClass());
 }

 @Test
 public void testAddDuplicateProperty() {
 String propertyName = "propertyName";
 JSType propertyType = JSType.STRING;
 Node propertyNode = null; // not used in test
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
 assertNull(recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode));
 }

 @Test
 public void testAddPropertyAndBuildRecordWithOneProperty() {
 String propertyName = "propertyName";
 JSType propertyType = JSType.STRING;
 Node propertyNode = null; // not used in test
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode).build();
 RecordType expectedRecordType = new RecordType(registry, Maps.singletonMap(propertyName, new RecordProperty(propertyType, propertyNode)));
 assertEquals(expectedRecordType.getTypeClass(), recordType.getTypeClass());
 }

 @Test
 public void testAddMultiplePropertiesAndBuildRecord() {
 String propertyName1 = "propertyName1";
 JSType propertyType1 = JSType.NUMBER;
 Node propertyNode1 = null; // not used in test
 String propertyName2 = "propertyName2";
 JSType propertyType2 = JSType.BOOLEAN;
 Node propertyNode2 = null; // not used in test
 RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
 JSType recordType = recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1)
 .addProperty(propertyName2, propertyType2, propertyNode2).build();
 RecordType expectedRecordType = new RecordType(registry, Maps.newHashMap("propertyName1", new RecordProperty(propertyType1, propertyNode1), "propertyName2", new RecordProperty(propertyType2, propertyNode2)));
 assertEquals(expectedRecordType.getTypeClass(), recordType.getTypeClass());
 }
}