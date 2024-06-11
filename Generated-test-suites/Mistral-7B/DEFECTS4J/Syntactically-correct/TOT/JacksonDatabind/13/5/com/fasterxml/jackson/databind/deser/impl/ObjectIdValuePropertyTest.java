import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty property;

 @Before
 public void setUp() throws Exception {
 ObjectNode testObject = JsonFactory.instance.createObjectNode();
 StdDeserializer idDeserializer = new StdDeserializer<>();
 IdType idType = IdType.INT;
 ObjectIdReader idReader = new ObjectIdReader(new PropertyName("id"), idType, null, idDeserializer);
 property = new ObjectIdValueProperty(idReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectParser parser = JsonFactory.instance.getParser(new ObjectMapper());
 JsonNode jsonNode = JsonFactory.instance.createObjectNode();
 jsonNode.put("id", 1);
 parser.setCodecContext(new MinimalJacksonAnnotationIntrospector());
 property.deserializeAndSet(parser, new TestContext(), new Object());

 Object id = property.getValue();
 Assert.assertEquals(1, id);
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectParser parser = JsonFactory.instance.getParser(new ObjectMapper());
 JsonNode jsonNode = JsonFactory.instance.createObjectNode();
 jsonNode.put("id", null);
 property.deserializeSetAndReturn(parser, new TestContext(), new Object());

 Assert.assertNull(property.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws Exception {
 Object obj = new Object();
 property.set(obj, null);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 Object obj = new Object();
 Integer id = 1;
 property.setAndReturn(obj, id);

 Object idPropertyValue = property.get(obj);
 Assert.assertEquals(id, idPropertyValue);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetAndReturnWithNullId() throws Exception {
 Object obj = new Object();
 property.setAndReturn(obj, null);
 }
}