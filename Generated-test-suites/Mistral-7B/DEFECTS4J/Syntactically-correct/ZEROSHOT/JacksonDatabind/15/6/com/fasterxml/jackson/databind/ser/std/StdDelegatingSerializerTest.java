import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private StdSerializerFactory serializerFactory;
 private Converter<Object, ?> converter;
 private StdDelegatingSerializer delegatingSerializer;
 private JsonSerializer<Object> delegateSerializer;
 private JavaType delegateType;

 @Before
 public void setup() {
 serializerFactory = mock(StdSerializerFactory.class);
 converter = mock(Converter.class);
 delegateSerializer = mock(JsonSerializer.class);
 delegateType = mock(JavaType.class);

 delegatingSerializer = new StdDelegatingSerializer(converter);
 delegatingSerializer.withDelegate(converter, delegateType, delegateSerializer);
 }

 @Test
 public void testSerialize() throws Exception {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);

 // Test serialize method with no type serialization
 delegatingSerializer.serialize(value, gen, provider);
 verify(delegateSerializer).serialize(eq(value), eq(gen), eq(provider));

 // Test serializeWithType method with type serialization
 delegatingSerializer.serializeWithType(value, gen, provider, typeSerializer);
 verify(delegateSerializer).serializeWithType(eq(value), eq(gen), eq(provider), eq(typeSerializer));
 }

 @Test
 public void testConvertValue() throws Exception {
 Object input = new Object();
 Object output = converter.convert(input);
 assertEquals(input, delegatingSerializer.convertValue(input));
 assertEquals(output, delegatingSerializer.convertValue(input));
 }

 @Test
 public void testGetDelegateSerializer() {
 assertEquals(delegateSerializer, delegatingSerializer.getDelegatee());
 }

 @Test
 public void testGetConverter() {
 assertEquals(converter, delegatingSerializer.getConverter());
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 ObjectMapper mapper = new ObjectMapper();
 JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getSchemaProvider().schemaVisitor(mapper.getTypeFactory().constructType(JavaType.class), null);
 delegatingSerializer.acceptJsonFormatVisitor(visitor, delegateType);
 verify(delegateSerializer).acceptJsonFormatVisitor(eq(visitor), eq(delegateType));
 }

 @Test
 public void testGetSchema() {
 ObjectMapper mapper = new ObjectMapper();
 Object schema = mapper.getFactory().getSchemaProvider().findSchemaForSerializer(delegatingSerializer);
 assertEquals(schema, delegatingSerializer.getSchema(null, null));
 verify(delegateSerializer).getSchema(eq(null), eq(null));
 }

 @Test
 public void testIsEmpty() {
 Object value = null;
 assertFalse(delegatingSerializer.isEmpty(value));
 verify(delegateSerializer).isEmpty(eq(null));

 value = new Object();
 assertTrue(delegatingSerializer.isEmpty(value));
 verify(delegateSerializer).isEmpty(eq(value));
 }
}