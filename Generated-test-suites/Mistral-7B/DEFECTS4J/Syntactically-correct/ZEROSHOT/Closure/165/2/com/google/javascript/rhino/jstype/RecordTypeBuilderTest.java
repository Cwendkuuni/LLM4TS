import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

 private JSTypeRegistry registry;
 private RecordTypeBuilder builder;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 builder = new RecordTypeBuilder(registry);
 }

 @Test
 public void testAddProperty() {
 RecordProperty property = builder.addProperty("name", String.class, null);
 assertTrue(property != null);
 }

 @Test(expected = NullPointerException.class)
 public void testAddPropertyWithNullRegistry() {
 RecordTypeBuilder builder = new RecordTypeBuilder(null);
 builder.addProperty("name", String.class, null);
 }

 @Test(expected = NullPointerException.class)
 public void testAddPropertyWithNullType() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(null, String.class, null);
 }

 @Test(expected = NullPointerException.class)
 public void testAddPropertyWithNullName() {
 RecordTypeBuilder builder = new RecordTypeBuilder(registry);
 builder.addProperty(null, String.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPropertyWithDuplicateName() {
 RecordProperty firstProperty = builder.addProperty("name", String.class, null);
 RecordProperty secondProperty = builder.addProperty("name", String.class, null);
 }

 @Test
 public void testBuildEmptyRecord() {
 JSType recordType = builder.build();
 assertTrue(recordType instanceof JSTypeNative);
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
 }

 @Test
 public void testBuildRecordWithSingleProperty() {
 RecordProperty property = builder.addProperty("name", String.class, null);
 JSType recordType = builder.build();
 assertTrue(recordType instanceof RecordType);
 RecordType record = (RecordType) recordType;
 assertEquals(1, record.getPropertyCount());
 assertEquals(property.getType(), record.getProperty("name").getType());
 }

 @Test
 public void testBuildRecordWithMultipleProperties() {
 RecordProperty nameProperty = builder.addProperty("name", String.class, null);
 RecordProperty ageProperty = builder.addProperty("age", Integer.TYPE, null);
 JSType recordType = builder.build();
 assertTrue(recordType instanceof RecordType);
 RecordType record = (RecordType) recordType;
 assertEquals(2, record.getPropertyCount());
 assertEquals(nameProperty.getType(), record.getProperty("name").getType());
 assertEquals(ageProperty.getType(), record.getProperty("age").getType());
 }
}