import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.JsonWriteContext.JsonWriteContextBuilder;
import com.fasterxml.jackson.core.util.DupDetector;

public class JsonWriteContextTest {

 private JsonWriteContext _context;
 private DupDetector _dups;

 @Before
 public void setUp() {
 _context = JsonWriteContext.createRootContext();
 _dups = new DupDetector();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 // Test valid field name
 _context.writeFieldName("field1");
 assertEquals("{\"field1\"}", _context.toString());

 // Test writing the same field name again
 _context.writeFieldName("field1");
 _context.withDupDetector(_dups);
 try {
 _context.writeFieldName("field1");
 fail("Expected JsonGenerationException to be thrown");
 } catch (JsonGenerationException e) {
 // Expected
 }
 }

 @Test
 public void testWriteValue() throws JsonProcessingException {
 // Test writing simple value
 _context.setCurrentValue(1);
 _context.writeValue();
 assertEquals("/1", _context.toString());

 // Test writing simple value with field name
 _context = _context.createChildObjectContext();
 _context.writeFieldName("field1");
 _context.setCurrentValue(2);
 _context.writeValue();
 assertEquals("{\"field1\":2}", _context.toString());

 // Test writing multiple values
 _context.reset();
 _context = _context.createChildArrayContext();
 _context.writeValue();
 _context.setCurrentValue(1);
 _context.writeValue();
 _context.setCurrentValue(3);
 _context.writeValue();
 assertEquals("[1,2,3]", _context.toString());
 }

 @Test
 public void testWriteFieldNameWithEmptyString() throws JsonProcessingException {
 _context.writeFieldName("");
 _context.withDupDetector(_dups);
 try {
 _context.writeFieldName("");
 fail("Expected JsonGenerationException to be thrown");
 } catch (JsonGenerationException e) {
 // Expected
 }
 }

 @Test
 public void testWriteFieldNameWithNull() throws JsonProcessingException {
 _context.writeFieldName(null);
 _context.withDupDetector(_dups);
 try {
 _context.writeFieldName(null);
 fail("Expected JsonGenerationException to be thrown");
 } catch (JsonGenerationException e) {
 // Expected
 }
 }

 @Test
 public void testWriteValueWithNull() throws JsonProcessingException {
 _context.setCurrentValue(null);
 _context.writeValue();
 assertEquals("/null", _context.toString());
 }

 @Test
 public void testWriteValueWithEmptyString() throws JsonProcessingException {
 _context.setCurrentValue("");
 _context.writeValue();
 assertEquals("/", _context.toString());
 }
}