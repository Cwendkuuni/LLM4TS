import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializingSimpleValue() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotation();
 String expectedOutput = "\"someValue\"";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodReturnsNull() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationNullReturn();
 String expectedOutput = "null";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodThrowsIOException() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationIOException();
 String expectedOutput = "\"errorMessage\"";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.registerSerializer(input.getClass(), new CustomSerializerForMyClass());
 String actualOutput = mapperWithCustomSerializer.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodThrowsRuntimeException() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationRuntimeException();
 String expectedOutput = "\"errorMessage\"";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 ObjectMapper mapperWithCustomSerializer = new ObjectMapper();
 mapperWithCustomSerializer.registerSerializer(input.getClass(), new CustomSerializerForMyClass());
 String actualOutput = mapperWithCustomSerializer.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodReturnsComplexObject() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationComplexObject();
 String expectedOutput = "{\"field1\":\"value1\",\"field2\":\"value2\"}";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodReturnsCollection() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationCollection();
 String expectedOutput = "[\"value1\",\"value2\",\"value3\"]";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodReturnsEnum() throws JsonProcessingException {
 Object input = MyClassWithJsonValueAnnotationEnum.VALUE_ONE;
 String expectedOutput = "\"VALUE_ONE\"";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSerializingValueMethodReturnsEnumWithCustomValues() throws JsonProcessingException {
 Object input = MyClassWithJsonValueAnnotationEnumCustomValues.VALUE_ONE;
 String expectedOutput = "\"CustomValueOne\"";

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializingValueMethodThrowsJsonMappingException() throws JsonProcessingException {
 Object input = new MyClassWithJsonValueAnnotationJsonMappingException();

 serializer = mapper.findValueSerializer(input.getClass(), null);
 String actualOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);

 fail("Expected JsonMappingException to be thrown but no exception was thrown.");
 }
}

class MyClassWithJsonValueAnnotation {
 @JsonValue
 public String getSomeValue() {
 return "someValue";
 }
}

class MyClassWithJsonValueAnnotationNullReturn {
 @JsonValue
 public String getSomeValue() {
 return null;
 }
}

class MyClassWithJsonValueAnnotationIOException {
 @JsonValue
 public String getSomeValue() throws IOException {
 throw new IOException("errorMessage");
 }
}

class MyClassWithJsonValueAnnotationRuntimeException {
 @JsonValue
 public String getSomeValue() {
 throw new RuntimeException("errorMessage");
 }
}

class MyClassWithJsonValueAnnotationComplexObject {
 @JsonValue
 public ComplexObject getSomeComplexObject() {
 return new ComplexObject("field1", "value1");
 }
}

class ComplexObject {
 private String field1;

 public ComplexObject(String field1, String field2) {
 this.field1 = field1;
 }

 public String getField1() {
 return field1;
 }
}

class MyClassWithJsonValueAnnotationCollection {
 @JsonValue
 public String[] getSomeValues() {
 return new String[]{"value1", "value2", "value3"};
 }
}

class MyClassWithJsonValueAnnotationEnum {
 public static final MyClassWithJsonValueAnnotationEnum VALUE_ONE = new ValueOne();
 public static final MyClassWithJsonValueAnnotationEnum VALUE_TWO = new ValueTwo();

 static class ValueOne {}
 static class ValueTwo {}
}

class MyClassWithJsonValueAnnotationEnumCustomValues {
 public static final MyClassWithJsonValueAnnotationEnumCustomValues VALUE_ONE = new ValueOne();
 public static final MyClassWithJsonValueAnnotationEnumCustomValues VALUE_TWO = new ValueTwo();

 static class ValueOne {
 @JsonValue
 public String getCustomValue() {
 return "CustomValueOne";
 }
 }
 static class ValueTwo {
 @JsonValue
 public String getCustomValue() {
 return "CustomValueTwo";
 }
 }
}

class CustomSerializerForMyClass extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("errorMessage");
 }
}