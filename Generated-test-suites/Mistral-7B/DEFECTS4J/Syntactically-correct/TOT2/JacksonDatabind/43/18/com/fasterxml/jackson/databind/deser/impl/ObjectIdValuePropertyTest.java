import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

  private ObjectIdValueProperty objectIdValueProperty;
  private ObjectMapper objectMapper;

  @Before
  public void setup() {
    ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
    PropertyMetadata metadata = mock(PropertyMetadata.class);
    objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    objectMapper = new JsonMapper();
  }

  @Test
  public void testDeserializeAndSet() throws IOException {
    ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put("id", "123");

    JsonParser jsonParser = objectMapper.getFactory().createParser(objectNode);
    DeserializationContext context = mock(DeserializationContext.class);
    Object instance = new Object();

    when(objectIdValueProperty.deserializeSetAndReturn(jsonParser, context, instance))
      .thenReturn("123");

    objectIdValueProperty.deserializeAndSet(jsonParser, context, instance);

    assertEquals("123", objectIdValueProperty.getValue(instance));
  }
}