import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.NameTransformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private JsonDeserializer<Object> valueDeserializer;
 private JsonDeserializer<Object> idDeserializer;
 private TypeIdResolver typeIdResolver;
 private NameTransformer nameTransformer;
 private TypeResolverBuilder typeResolverBuilder;

 @Before
 public void setup() {
 objectIdReader = mock(ObjectIdReader.class);
 valueDeserializer = mock(JsonDeserializer.class);
 idDeserializer = mock(JsonDeserializer.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 typeIdResolver = mock(TypeIdResolver.class);
 nameTransformer = mock(NameTransformer.class);
 typeResolverBuilder = mock(TypeResolverBuilder.class);

 when(objectIdReader.propertyName).thenReturn("objectId");
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
 when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
 when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));
 when(valueDeserializer.getClass()).thenReturn(FromStringDeserializer.class);

 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testGetAnnotation() {
 assertEquals(null, objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertEquals(null, objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String id = "testId";
 when(jsonParser.getCurrentName()).thenReturn("objectId");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn(id);
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");

 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);

 assertEquals(id, objectIdReader.getIdType().cast(objectIdValueProperty.getValue()));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 String id = "testId";
 when(jsonParser.getCurrentName()).thenReturn("objectId");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn(id);
 when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("testValue");

 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

 assertEquals(result, null);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 String id = "testId";

 objectIdValueProperty.set(instance, id);

 assertEquals(id, objectIdReader.getIdType().cast(objectIdValueProperty.getValue()));
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object instance = new Object();
 String id = "testId";

 Object result = objectIdValueProperty.setAndReturn(instance, id);

 assertEquals(result, instance);
 }

 @Test
 public void testWithName() {
 String newName = "newName";
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newPropertyName(newName));

 assertEquals(newName, newObjectIdValueProperty.getName());
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(newValueDeserializer);

 assertEquals(newValueDeserializer, newObjectIdValueProperty.getValueDeserializer());
 }

 @Test
 public void testWithNullProvider() {
 NullValueProvider newNullProvider = mock(NullValueProvider.class);
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withNullProvider(newNullProvider);

 assertEquals(newNullProvider, newObjectIdValueProperty.getNullProvider());
 }
}