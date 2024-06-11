import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializingSimpleValue() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return "test";
 }
 };
 String expected = "\"test\"";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializingComplexValue() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return mapper.writeValueAsString(new Object());
 }
 };
 String expected = "\"[object Object]\"" + "\n";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullValue() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return null;
 }
 };
 mapper.writeValueAsString(obj);
 fail("Should have thrown JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testNonExistentMethod() throws JsonProcessingException {
 Object obj = new Object() {
 // @JsonValue is missing
 };
 mapper.writerWithView(JsonView.ANY).writeValueAsString(obj);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testContextualSerialization() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return "test";
 }
 };
 Object anotherObj = new Object() {
 @JsonValue
 public Integer value() {
 return 1;
 }
 };
 ObjectMapper mapperWithContext = new ObjectMapper();
 mapperWithContext.registerSerializer(Integer.class, new JsonValueSerializer(null, null, mapperWithContext.findValueSerializer(Integer.class, null), false));
 String expected = "\"test\"";
 String actual = mapperWithContext.writeValueAsString(obj);
 assertEquals(expected, actual);
 String expectedInt = "1";
 String actualInt = mapperWithContext.writeValueAsString(anotherObj);
 assertEquals(expectedInt, actualInt);
 }

 @Test
 public void testSerializationOfEnum() throws JsonProcessingException {
 enum TestEnum {
 TEST1, TEST2
 }
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return TestEnum.TEST1.name();
 }
 };
 String expected = "\"TEST1\"";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializationOfEnumWithCustomValue() throws JsonProcessingException {
 enum TestEnum {
 TEST1("Test1Value"), TEST2("Test2Value")
 }
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return TestEnum.TEST1.name();
 }
 };
 String expected = "\"Test1Value\"";
 String actual = mapper.writeValueAsString(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializationOfNaturalTypes() throws JsonProcessingException {
 Object objInt = 123;
 String expectedInt = "123";
 String actualInt = mapper.writeValueAsString(objInt);
 assertEquals(expectedInt, actualInt);
 Object objBoolean = true;
 String expectedBoolean = "true";
 String actualBoolean = mapper.writeValueAsString(objBoolean);
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfNullEnum() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public TestEnum value() {
 return null;
 }
 };
 mapper.writeValueAsString(obj);
 fail("Should have thrown JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationOfUnknownEnum() throws JsonProcessingException {
 Object obj = new Object() {
 @JsonValue
 public String value() {
 return TestUnknownEnum.TEST.name();
 }
 };
 mapper.writeValueAsString(obj);
 fail("Should have thrown JsonMappingException");
 }

 @Test
 public void testSerializingListOfObjects() throws JsonProcessingException {
 Object listObj = new Object() {
 @JsonValue
 public Object value() {
 return new Object() {
 @JsonValue
 public String value() {
 return "test";
 }
 };
 }
 };
 Object expected = "[\"test\"]";
 Object actual = mapper.writeValueAsString(listObj);
 assertEquals(expected, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingEmptyList() throws JsonProcessingException {
 Object listObj = new Object() {
 @JsonValue
 public Object value() {
 return new Object[0];
 }
 };
 mapper.writeValueAsString(listObj);
 fail("Should have thrown JsonMappingException");
 }
}