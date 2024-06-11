import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setUp() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 }

 @Test
 public void testNullTypeToken() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Object> adapter = factory.create(gson, null);
 verify(gson, never()).newTypeAdapter(any());
 assertNull(adapter);
 }

 @Test
 public void testMissingJsonAdapterAnnotation() {
 Class<Integer> integerType = Integer.TYPE;
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Integer> adapter = factory.create(gson, new TypeToken<Integer>() {}.getType());
 verify(gson, never()).newTypeAdapter(integerType);
 assertNull(adapter);
 }

 @Test
 public void testJsonAdapterValueIsTypeAdapter() {
 Class<String> stringType = String.class;
 JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> adapter = factory.create(gson, new TypeToken<String>() {}.getType());
 TypeAdapter<String> stringAdapter = mock(StringAdapter.class);
 when(constructorConstructor.get(TypeToken.get(StringAdapter.class))).thenReturn(stringAdapter);
 verify(constructorConstructor).get(TypeToken.get(StringAdapter.class));
 verify(stringAdapter).nullSafe();
 assertEquals(stringAdapter, adapter);
 }

 @Test
 public void testJsonAdapterValueIsTypeAdapterFactory() {
 Class<Number> numberType = Number.class;
 JsonAdapter annotation = new JsonAdapter(NumberAdapterFactory.class);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Number> adapter = factory.create(gson, new TypeToken<Number>() {}.getType());
 TypeAdapterFactory numberAdapterFactory = mock(NumberAdapterFactory.class);
 when(numberAdapterFactory.create(gson, any())).thenReturn(mock(TypeAdapter.class));
 when(constructorConstructor.get(TypeToken.get(NumberAdapterFactory.class))).thenReturn(numberAdapterFactory);
 verify(constructorConstructor).get(TypeToken.get(NumberAdapterFactory.class));
 TypeAdapter<Number> numberAdapter = mock(TypeAdapter.class);
 verify(numberAdapter).nullSafe();
 assertEquals(numberAdapter, adapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueIsNotTypeAdapterOrTypeAdapterFactory() {
 Class<Object> objectType = Object.class;
 JsonAdapter annotation = new JsonAdapter("String");
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<Object> adapter = factory.create(gson, new TypeToken<Object>() {}.getType());
 verify(gson, never()).newTypeAdapter(objectType);
 assertNull(adapter);
 }
}