import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private StdDeserializer mockIdDeserializer;
 private Object id;

 @Before
 public void setUp() {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockIdDeserializer = mock(StdDeserializer.class);
 id = new Object();
 when(mockIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(id);
 objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), IdType.STRING, mockIdDeserializer);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 Object instance = new Object();
 DeserializationContext deserializationContext = mock(DeserializationContext.class);
 Object expectedId = "123";
 Object idProperty = mock(SettableBeanProperty.class);
 when(mockObjectIdReader.getIdType()).thenReturn(IdType.STRING);
 when(mockObjectIdReader.getIdProperty()).thenReturn(idProperty);
 when(jsonParser.getValueAsString()).thenReturn(expectedId);
 when(deserializationContext.findObjectId(eq(expectedId), any(), any())).thenReturn(mock(ReadableObjectId.class));
 when(idProperty.getMember()).thenReturn(mock(AnnotatedMember.class));
 when(idProperty.setAndReturn(instance, eq(expectedId))).thenReturn(instance);

 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertEquals(expectedId, id);
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 Object instance = new Object();
 DeserializationContext deserializationContext = mock(DeserializationContext.class);

 objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
 assertNull(id);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 objectIdValueProperty.set(instance, value);
 }
}