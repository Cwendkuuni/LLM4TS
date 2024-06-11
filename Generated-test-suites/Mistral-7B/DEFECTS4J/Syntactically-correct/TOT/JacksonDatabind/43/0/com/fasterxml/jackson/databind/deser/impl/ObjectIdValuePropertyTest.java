import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NamedPropertyIdentifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private BeanPropertyDefinition mockPropertyDefinition;
 private ObjectIdValueProperty objUnderTest;

 @Before
 public void setUp() throws Exception {
 mockObjectIdReader = mock(ObjectIdReader.class);
 ObjectIdReader.Generator generator = mock(ObjectIdReader.Generator.class);
 ObjectIdReader.Resolver resolver = mock(ObjectIdReader.Resolver.class);

 when(mockObjectIdReader.generator).thenReturn(generator);
 when(mockObjectIdReader.resolver).thenReturn(resolver);
 when(mockObjectIdReader.getIdType()).thenReturn(Integer.class);
 when(mockObjectIdReader.propertyName).thenReturn(new NamedPropertyIdentifier("id"));
 when(mockObjectIdReader.idProperty).thenReturn(null);

 AnnotationIntrospector introspector = new AnnotationIntrospector();
 mockPropertyDefinition = introspector.findPropertyDefinition(Object.class, "id");

 ObjectIdValueProperty.Builder builder = new ObjectIdValueProperty.Builder(
 mockObjectIdReader, new PropertyMetadata());
 objUnderTest = builder.build();
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object id = 1L;
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 Object instance = new Object();
 when(mockParser.getValueAsLong()).thenReturn((long) id);

 objUnderTest.deserializeAndSet(mockParser, context, instance);
 ReadableObjectId roid = verify(context).findObjectId(id, generator, resolver);
 roid.bindItem(instance);
 // Also verify that idProperty is called if it's not null
 verify(mockPropertyDefinition, times(1)).setAndReturn(instance, id);

 assertEquals(id, objUnderTest.getValue(instance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object id = 1L;
 JsonParser mockParser = mock(JsonParser.class);
 DeserializationContext context = mock(DeserializationContext.class);
 Object instance = new Object();
 Object result = new Object();
 Object deserializedId = 2L;
 StdKeyDeserializer mockValueDeserializer = mock(StdKeyDeserializer.class);
 TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

 when(mockParser.getValueAsLong()).thenReturn((long) id);
 when(mockValueDeserializer.deserialize(mockParser, context)).thenReturn(deserializedId);
 when(mockTypeDeserializer.deserializesFromObject(anyObject(), anyObject())).thenReturn(true);
 when(mockObjectIdReader.getValueDeserializer()).thenReturn(mockValueDeserializer);
 when(mockObjectIdReader.getTypeDeserializer()).thenReturn(mockTypeDeserializer);

 Object resultObject = objUnderTest.deserializeSetAndReturn(mockParser, context, instance);
 assertEquals(deserializedId, resultObject);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws IOException {
 Object instance = new Object();
 Object value = new Object();
 objUnderTest.set(instance, value);
 fail("Should not reach this point");
 }
}