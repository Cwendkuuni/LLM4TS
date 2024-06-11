import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIntegerSerializer() throws JsonProcessingException {
 Integer testInteger = 1;
 JsonGenerator gen = mock(JsonGenerator.class);

 IntegerSerializer integerSerializer = new IntegerSerializer();
 integerSerializer.serialize(testInteger, gen, objectMapper.getSerializerProvider());

 verify(gen, times(1)).writeNumber(testInteger.intValue());
 }

 @Test
 public void testIntLikeSerializer() throws JsonProcessingException {
 Number testNumber = 1;
 JsonGenerator gen = mock(JsonGenerator.class);

 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 intLikeSerializer.serialize(testNumber, gen, objectMapper.getSerializerProvider());

 verify(gen, times(1)).writeNumber(testNumber.intValue());
 }

 @Test
 public void testLongSerializer() throws JsonProcessingException {
 Long testLong = 1L;
 JsonGenerator gen = mock(JsonGenerator.class);

 LongSerializer longSerializer = new LongSerializer();
 longSerializer.serialize(testLong, gen, objectMapper.getSerializerProvider());

 verify(gen, times(1)).writeNumber(testLong.longValue());
 }

 @Test
 public void testFloatSerializer() throws JsonProcessingException {
 Float testFloat = 1.0f;
 JsonGenerator gen = mock(JsonGenerator.class);

 FloatSerializer floatSerializer = new FloatSerializer();
 floatSerializer.serialize(testFloat, gen, objectMapper.getSerializerProvider());

 verify(gen, times(1)).writeNumber(testFloat.floatValue());
 }

 @Test
 public void testDoubleSerializer() throws JsonProcessingException {
 Double testDouble = 1.0;
 JsonGenerator gen = mock(JsonGenerator.class);

 DoubleSerializer doubleSerializer = new DoubleSerializer();
 doubleSerializer.serialize(testDouble, gen, objectMapper.getSerializerProvider());

 verify(gen, times(1)).writeNumber(testDouble.doubleValue());
 }

 @Test
 public void testContextualSerializer() throws JsonProcessingException {
 Object testObject = new Object() {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 private Integer testJsonFormatInteger;

 public Integer getTestJsonFormatInteger() {
 return testJsonFormatInteger;
 }

 public void setTestJsonFormatInteger(Integer testJsonFormatInteger) {
 this.testJsonFormatInteger = testJsonFormatInteger;
 }
 };

 ObjectMapper objectMapperSpy = spy(objectMapper);
 JsonGenerator gen = mock(JsonGenerator.class);

 ObjectWriter writer = objectMapperSpy.writerWithView(new StandardSerializerView());
 writer.writeValueAsString(testObject);

 verify(gen, times(1)).writeString(testObject.getClass().getName());
 JsonFormatVisitorWrapper jsonFormatVisitorWrapper = any();
 ObjectWriter objectWriter = any();
 ObjectWriter objectWriterSpy = spy(writer);

 doReturn(objectWriterSpy).when(objectMapperSpy).findValueSerializer(eq(Integer.class), eq(JsonSerializer.NullValueProvider.class), any(), any());
 doReturn(jsonFormatVisitorWrapper).when(objectMapperSpy).findSerializerProvider();

 testObject.setTestJsonFormatInteger(1);
 ObjectWriter writerSpy = spy(writer);
 writerSpy.writeValueAsString(testObject);

 verify(objectWriterSpy, times(1)).acceptJsonFormatVisitor(eq(jsonFormatVisitorWrapper), any());
 }
}