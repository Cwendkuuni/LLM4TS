import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SetterlessPropertyTest {

 private SettableBeanProperty setterlessProperty;
 private Object testInstance;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private Object collectionOrMapValue;

 @Before
 public void setUp() throws Exception {
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
 JavaType type = mock(JavaType.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 SetterlessProperty setterlessPropertyConstructorArg = mock(SetterlessProperty.class);

 setterlessProperty = new SetterlessProperty(propDef, type, typeDeserializer, null, annotatedMethod);
 testInstance = createTestInstance();
 collectionOrMapValue = createCollectionOrMapValue();
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException, IOException {
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn("testValue");
 when(annotatedMethod.getAnnotated()).thenReturn(mock(JsonGetter.class));

 setterlessProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 verify(testInstance, times(1)).getClass();
 verify(testInstance, times(1)).getClass().getMethod(setterlessProperty.getName().getShortName(), (Class[]) null);
 verify(mockJsonParser).nextToken();
 verify(mockJsonParser).nextToken();
 verify(mockJsonParser, times(1)).getValueAsString();
 verify(mockContext).reportBadDefinition(any(), any());

 assertEquals(collectionOrMapValue, testInstance.getClass().getMethod(setterlessProperty.getName().getShortName(), (Class[]) null).invoke(testInstance, (Object[]) null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 setterlessProperty.set(testInstance, new Object());
 }

 @Test
 public void testDeserializeSetAndReturn() throws JsonProcessingException, IOException {
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.getValueAsString()).thenReturn("testValue");
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(mockJsonParser.nextValue()).thenReturn(mockJsonParser);
 when(mockJsonParser.nextValue()).thenReturn(mockContext);
 when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 Object result = setterlessProperty.deserializeSetAndReturn(mockJsonParser, mockContext, testInstance);
 assertEquals(testInstance, result);
 }

 private Object createCollectionOrMapValue() {
 return new Object() {
 };
 }

 private Object createTestInstance() {
 return new Object() {
 private Collection<Object> collection;

 @JsonGetter
 public Collection<Object> getCollection() {
 return collection;
 }

 @JsonSetter
 public void setCollection(Collection<Object> collection) {
 this.collection = collection;
 }
 };
 }
}