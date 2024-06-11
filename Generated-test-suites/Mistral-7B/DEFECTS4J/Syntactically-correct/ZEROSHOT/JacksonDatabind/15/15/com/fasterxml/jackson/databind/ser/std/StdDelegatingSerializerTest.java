import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer<Object> serializer;
 private JsonSerializer<Object> delegateSerializer;
 private Converter<Object,?> converter;

 @Before
 public void setup() {
 converter = mock(Converter.class);
 delegateSerializer = mock(JsonSerializer.class);
 serializer = new StdDelegatingSerializer<>(converter, null, delegateSerializer);
 }

 @Test
 public void testSerializeWithDefaultConverter() throws JsonGenerationException, JsonMappingException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);

 when(converter.convert(value)).thenReturn(new Object());
 when(delegateSerializer.serialize(any(), eq(gen), eq(provider))).thenReturn(null);

 serializer.serialize(value, gen, provider);
 verify(delegateSerializer).serialize(any(), eq(gen), eq(provider));
 }

 @Test
 public void testSerializeWithCustomConverter() throws JsonGenerationException, JsonMappingException {
 Object value = new Object();
 Object delegateValue = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);

 when(converter.convert(value)).thenReturn(delegateValue);
 when(delegateSerializer.serialize(eq(delegateValue), eq(gen), eq(provider))).thenReturn(null);

 serializer.serialize(value, gen, provider);
 verify(delegateSerializer).serialize(eq(delegateValue), eq(gen), eq(provider));
 }

 @Test(expected = IllegalStateException.class)
 public void testCannotSerializeToSameType() throws JsonGenerationException, JsonMappingException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSer = mock(TypeSerializer.class);

 when(converter.convert(value)).thenReturn(value);
 when(delegateSerializer.serialize(any(), eq(gen), eq(provider))).thenReturn(null);

 serializer.serialize(value, gen, provider);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);

 serializer.resolve(provider);
 verify(delegateSerializer).resolve(provider);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 Object value = new Object();
 BeanProperty property = mock(BeanProperty.class);

 JsonSerializer<?> delegateSerializerInstance = new JsonSerializer<>(){};
 when(delegateSerializer.createContextual(eq(provider), any())).thenReturn(delegateSerializerInstance);

 JsonSerializer<?> result = serializer.createContextual(provider, property);
 assertThat(result).isEqualTo(delegateSerializerInstance);
 }

 @Test
 public void testGetDelegatee() {
 JsonSerializer<?> expected = delegateSerializer;
 JsonSerializer<?> actual = serializer.getDelegatee();
 assertThat(actual).isEqualTo(expected);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 Type typeHint = new TypeToken<Object>() {}.getType();

 when(delegateSerializer.getSchema(provider, typeHint)).thenReturn(null);

 JsonNode result = serializer.getSchema(provider, typeHint);
 assertThat(result).isNull();
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
 }
}