import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> generator;
 private ObjectMapper mapper;
 private JsonGenerator gen;
 private SerializerProvider provider;
 private SerializableString name;
 private ObjectIdWriter w;

 @Before
 public void setup() {
 generator = mock(ObjectIdGenerator.class);
 mapper = new ObjectMapper();
 gen = mock(JsonGenerator.class);
 provider = mock(SerializerProvider.class);
 w = new ObjectIdWriter(null, null, null);
 }

 @Test
 public void testWriteAsId_WithIdAndIdWritten() throws IOException, JsonMappingException, JsonGenerationException {
 Object id = 1L;
 when(generator.generateId(any())).thenReturn(id);
 when(gen.canWriteObjectId()).thenReturn(true);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.id = id;
 writableObjectId.idWritten = true;

 w.propertyName = new SerializableString("name");
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 StdSerializer<Object> objectSerializer = mock(StdSerializer.class);
 w. serializer = objectSerializer;
 w.alwaysAsId = false;

 boolean result = writableObjectId.writeAsId(gen, provider, w);

 verify(gen).writeObjectRef(eq(String.valueOf(id)));
 assertEquals(true, result);
 }

 @Test
 public void testWriteAsId_WithoutIdAndIdWritten() throws IOException, JsonMappingException, JsonGenerationException {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 w.propertyName = new SerializableString("name");
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 StdSerializer<Object> objectSerializer = mock(StdSerializer.class);
 w.serializer = objectSerializer;
 w.alwaysAsId = false;

 boolean result = writableObjectId.writeAsId(gen, provider, w);

 verify(gen, never()).writeObjectRef(anyString());
 assertEquals(false, result);
 }

 @Test
 public void testWriteAsId_WithWalwaysAsIdTrue() throws IOException, JsonMappingException, JsonGenerationException {
 Object id = 1L;
 when(generator.generateId(any())).thenReturn(id);
 when(gen.canWriteObjectId()).thenReturn(false);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.id = id;
 w.propertyName = null;
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 StdSerializer<Object> objectSerializer = mock(StdSerializer.class);
 w.serializer = objectSerializer;
 w.alwaysAsId = true;

 boolean result = writableObjectId.writeAsId(gen, provider, w);

 verify(provider).getSerializerProvider().findValueSerializer(eq(id.getClass()), any());
 verify(objectSerializer).serialize(id, gen, provider);
 assertEquals(true, result);
 }

 @Test
 public void testGenerateId() throws IOException {
 Object id = 1L;
 when(generator.generateId(any())).thenReturn(id);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 Object result = writableObjectId.generateId(null);

 verify(generator).generateId(null);
 assertEquals(id, result);
 }

 @Test
 public void testWriteAsField() throws IOException, JsonMappingException, JsonGenerationException {
 Object id = 1L;
 when(generator.generateId(any())).thenReturn(id);
 when(gen.canWriteObjectId()).thenReturn(false);
 when(gen.writeFieldName(anyString())).thenReturn(this);

 WritableObjectId writableObjectId = new WritableObjectId(generator);
 w.propertyName = new SerializableString("name");
 TypeSerializer typeSerializer = mock(TypeSerializer.class);
 StdSerializer<Object> objectSerializer = mock(StdSerializer.class);
 w.serializer = objectSerializer;
 w.alwaysAsId = false;

 writableObjectId.writeAsField(gen, provider, w);

 verify(gen).writeFieldName(eq("name"));
 verify(objectSerializer).serialize(id, gen, provider);
 }
}

class ObjectIdWriter {
 private ObjectIdGenerator<?> generator;
 private SerializerProvider provider;
 private SerializableString propertyName;
 private StdSerializer<?> serializer;
 private boolean alwaysAsId;

 public ObjectIdWriter(ObjectIdGenerator<?> generator, SerializerProvider provider, StdSerializer<?> serializer) {
 this.generator = generator;
 this.provider = provider;
 this.serializer = serializer;
 this.alwaysAsId = false;
 }
}