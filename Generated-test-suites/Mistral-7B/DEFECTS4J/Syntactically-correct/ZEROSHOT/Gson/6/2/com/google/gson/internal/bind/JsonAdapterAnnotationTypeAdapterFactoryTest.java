import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

import static org.junit.Assert.assertNotNull;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

 @Before
 public void setup() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testCreate_JsonAdapterAnnotationPresent_ReturnsTypeAdapter() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 JsonAdapter jsonAdapterAnnotation = stringTypeToken.getRawType().getAnnotation(JsonAdapter.class);

 when(constructorConstructor.get(TypeToken.get(TypeAdapter.class)))
 .thenReturn(mock(TypeAdapter.class));

 TypeAdapter<String> stringTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);

 assertNotNull(stringTypeAdapter);
 }

 @Test
 public void testCreate_JsonAdapterAnnotationAbsent_ReturnsNull() {
 TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {};

 TypeAdapter<Integer> integerTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, integerTypeToken);

 assertNull(integerTypeAdapter);
 }

 @Test
 public void testGetTypeAdapter_TypeAdapter_ReturnsTypeAdapter() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 Class<TypeAdapter<String>> typeAdapterClass = TypeAdapter.class;
 TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);

 when(constructorConstructor.get(TypeToken.get(typeAdapterClass)))
 .thenReturn(stringTypeAdapter);

 TypeAdapter<String> returnedTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null);

 assertEquals(stringTypeAdapter, returnedTypeAdapter);
 }

 @Test
 public void testGetTypeAdapter_TypeAdapterFactory_ReturnsTypeAdapter() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 Class<TypeAdapterFactory> typeAdapterFactoryClass = TypeAdapterFactory.class;
 TypeAdapterFactory typeAdapterFactory = mock(TypeAdapterFactory.class);
 TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);

 when(constructorConstructor.get(TypeToken.get(typeAdapterFactoryClass)))
 .thenReturn(typeAdapterFactory);
 when(typeAdapterFactory.create(gson, stringTypeToken))
 .thenReturn(stringTypeAdapter);

 TypeAdapter<String> returnedTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null);

 assertEquals(stringTypeAdapter, returnedTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetTypeAdapter_InvalidJsonAdapter_ThrowsIllegalArgumentException() {
 TypeToken<String> stringTypeToken = new TypeToken<String>() {};
 Class<?> invalidJsonAdapter = Object.class;

 TypeAdapter<String> returnedTypeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, stringTypeToken, null);

 fail("IllegalArgumentException expected but not thrown.");
 }
}