import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdReferencePropertyTest {

 private ObjectIdReferenceProperty property;
 private ObjectIdInfo objectIdInfo;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private JsonDeserializer<Object> mockDeserializer;
 private Object pojo;

 @Before
 public void setUp() {
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 objectIdInfo = mock(ObjectIdInfo.class);
 mockDeserializer = mock(JsonDeserializer.class);
 pojo = new Object();

 property = new ObjectIdReferenceProperty(new SettableBeanProperty(mockDeserializer, "_field"), objectIdInfo);
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectNode node = ObjectMapper.parseObjectNode(mockJsonParser);
 Object id = new Object();
 when(mockJsonParser.getValueAsToken()).thenReturn(node);
 when(mockContext.findBeanProperty(_pojo.getClass(), "_field")).thenReturn(property);
 when(mockContext.findValueDeserializer(property.getName(), Object.class)).thenReturn(mockDeserializer);
 when(mockDeserializer.deserialize(mockJsonParser, mockContext, pojo)).thenReturn(id);

 property.deserializeAndSet(mockJsonParser, mockContext, pojo);
 verify(mockDeserializer).deserialize(mockJsonParser, mockContext, pojo);
 verify(property).set(pojo, id);
 }

 @Test
 public void testResolveForwardReference() throws Exception {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 property.deserializeSetAndReturn(mockJsonParser, mockContext, pojo);
 property.getMember().handleResolvedForwardReference(new Object(), ref);
 verify(mockPojo).set(ref.getValue());
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveForwardReferenceWithNoIdentityInfo() throws Exception {
 UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
 property.deserializeSetAndReturn(mockJsonParser, mockContext, pojo);
 property.getMember().handleResolvedForwardReference(new Object(), ref);
 }
}