import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class WritableObjectIdTest {

 private ObjectIdGenerator<Long> idGenerator;
 private JsonMapper mapper;
 private StdSerializer<Object> idWriter;
 private WritableObjectId idContainer;

 @Before
 public void setup() {
 idGenerator = new ObjectIdGenerator<Long>() {
 @Override
 public Long generateId(Object bean) {
 return new Random().nextLong();
 }
 };
 mapper = new JsonMapper();
 idWriter = mapper.findValueSerializer(Object.class);
 }

 @Test
 public void testWriteAsId() throws IOException {
 Object id = new Long(1L);
 WritableObjectId idContainer = new WritableObjectId(idGenerator);
 idContainer.id = id;

 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializers();
 ObjectIdWriter writer = new ObjectIdWriter(mapper, idWriter);

 idContainer.writeAsId(gen, provider, writer);
 gen.flush();
 String json = gen.getOutputContent();

 Long expectedId = id;
 Long actualId = mapper.readValue(json, Long.class);

 Assert.assertEquals(expectedId, actualId);
 }

 @Test
 public void testGenerateId() throws JsonGenerationException, JsonMappingException, IOException {
 Object idContainer = new WritableObjectId(idGenerator);
 Object id = idContainer.generateId(new Object());

 Assert.assertNotNull(id);
 }

 @Test(expected = IOException.class)
 public void testWriteAsId_IdIsNull() throws IOException {
 Object idContainer = new WritableObjectId(idGenerator);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializers();
 ObjectIdWriter writer = new ObjectIdWriter(mapper, idWriter);

 idContainer.writeAsId(gen, provider, writer);
 }

 @Test(expected = IOException.class)
 public void testWriteAsId_IdWritten() throws IOException {
 Object idContainer = new WritableObjectId(idGenerator);
 idContainer.id = new Long(1L);
 idContainer.idWritten = true;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializers();
 ObjectIdWriter writer = new ObjectIdWriter(mapper, idWriter);

 idContainer.writeAsId(gen, provider, writer);
 }

 @Test(expected = IOException.class)
 public void testWriteAsField_IdNull() throws IOException {
 Object idContainer = new WritableObjectId(idGenerator);
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializers();
 ObjectIdWriter writer = new ObjectIdWriter(mapper, idWriter);

 idContainer.writeAsField(gen, provider, writer);
 }

 @Test(expected = IOException.class)
 public void testWriteAsField_IdWritten() throws IOException {
 Object idContainer = new WritableObjectId(idGenerator);
 idContainer.id = new Long(1L);
 idContainer.idWritten = true;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 SerializerProvider provider = mapper.getSerializers();
 ObjectIdWriter writer = new ObjectIdWriter(mapper, idWriter);

 idContainer.writeAsField(gen, provider, writer);
 }
}