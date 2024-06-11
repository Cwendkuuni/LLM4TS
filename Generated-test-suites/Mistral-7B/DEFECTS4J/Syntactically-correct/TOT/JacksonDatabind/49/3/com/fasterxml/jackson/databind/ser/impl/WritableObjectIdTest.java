import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.ObjectIdGeneratorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

 private ObjectIdGenerator<?> idGenerator;
 private SerializerProvider provider;
 private ObjectMapper mapper;
 private JsonGenerator gen;
 private ByteArrayOutputStream outputStream;
 private Object testObject;
 private ObjectIdWriter idWriter;

 @Before
 public void setup() {
 idGenerator = mock(ObjectIdGenerator.class);
 provider = mock(SerializerProvider.class);
 mapper = new ObjectMapper();
 idWriter = mock(ObjectIdWriter.class);

 when(idGenerator.generateId(any())).thenReturn("generatedId");
 when(idGenerator.getSerializerProvider()).thenReturn(provider);
 when(idGenerator.getTypeId()).thenReturn(ObjectIdGeneratorType.INTERNAL.toString());

 outputStream = new ByteArrayOutputStream();
 gen = mapper.getPolymorphicSerializer().findValueSerializer(int.class, Object.class, provider);
 gen.setOutputType(Object.class);
 gen.setRootValueSerializer(new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeValue(value);
 }
 });

 testObject = new WritableObjectId(idGenerator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 idWriter = new ObjectIdWriter("testPropertyName", new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeValue(value);
 }
 });

 testObject.writeAsId(gen, provider, idWriter);

 assertEquals("generatedId", gen.getOutputValueAsString());
 }

 @Test
 public void testGenerateId() {
 Object id = testObject.generateId(testObject);
 assertEquals("generatedId", id);
 }

 @Test
 public void testWriteAsField() throws IOException {
 idWriter = new ObjectIdWriter("testPropertyName", new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeValue(value);
 }
 });

 testObject.writeAsField(gen, provider, idWriter);

 assertTrue(testObject.idWritten);
 assertEquals("generatedId", gen.getOutputValueAsString());
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullId() throws IOException {
 testObject = new WritableObjectId(idGenerator);
 idWriter = new ObjectIdWriter("testPropertyName", new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeValue(value);
 }
 });

 testObject.writeAsId(gen, provider, idWriter);
 idWriter.alwaysAsId = true;
 testObject.writeAsId(gen, provider, idWriter);
 idWriter.serializer = null;
 testObject.writeAsId(gen, provider, idWriter);
 }
}

class ObjectIdWriter {
 Serializer<Object> serializer;
 String propertyName;
 boolean alwaysAsId;

 public ObjectIdWriter(String propertyName, Serializer<Object> serializer) {
 this.propertyName = propertyName;
 this.serializer = serializer;
 }
}