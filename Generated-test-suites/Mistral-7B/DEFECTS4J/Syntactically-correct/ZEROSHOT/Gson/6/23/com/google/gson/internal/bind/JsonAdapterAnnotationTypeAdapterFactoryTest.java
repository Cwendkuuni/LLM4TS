import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;
 private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;

 @Before
 public void setUp() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = new GsonBuilder().setPrePreProcessor(mock(PreProcessor.class)).create();
 jsonAdapterAnnotationTypeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 }

 @Test
 public void testCreateNullTypeToken() {
 assertNull(jsonAdapterAnnotationTypeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreateWithJsonAdapterAnnotation() {
 Class<String> stringClass = String.class;
 TypeToken<String> stringTypeToken = TypeToken.get(stringClass);
 JsonAdapter jsonAdapterAnnotation = stringClass.getAnnotation(JsonAdapter.class);
 TypeAdapter<String> stringTypeAdapter = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(String.class))).thenReturn(stringTypeAdapter);

 TypeAdapter<String> actualTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, stringTypeToken);
 assertNotNull(actualTypeAdapter);
 assertSame(stringTypeAdapter, actualTypeAdapter);
 }

 @Test
 public void testCreateWithTypeAdapter() {
 Class<CustomType> customTypeClass = CustomType.class;
 TypeToken<CustomType> customTypeTypeToken = TypeToken.get(customTypeClass);
 TypeAdapter<CustomType> customTypeTypeAdapter = mock(TypeAdapter.class);
 when(constructorConstructor.get(TypeToken.get(CustomType.class))).thenReturn(customTypeTypeAdapter);

 JsonAdapter jsonAdapterAnnotation = customTypeClass.getAnnotation(JsonAdapter.class);
 TypeAdapter<CustomType> actualTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, customTypeTypeToken);
 assertNotNull(actualTypeAdapter);
 assertSame(customTypeTypeAdapter, actualTypeAdapter);
 }

 @Test
 public void testCreateWithTypeAdapterFactory() {
 Class<List<Integer>> listOfIntegerType = List.class.cast(new Class[]{Integer.class}[0].getClass());
 TypeToken<List<Integer>> listOfIntegerTypeToken = TypeToken.get(listOfIntegerType);
 CustomListTypeAdapterFactory customListTypeAdapterFactory = mock(CustomListTypeAdapterFactory.class);
 when(constructorConstructor.get(TypeToken.get(CustomListTypeAdapterFactory.class))).thenReturn(customListTypeAdapterFactory);

 JsonAdapter jsonAdapterAnnotation = listOfIntegerType.getAnnotation(JsonAdapter.class);
 TypeAdapterFactory<List<Integer>> customListTypeAdapterFactoryInstance = mock(TypeAdapterFactory.class);
 when(customListTypeAdapterFactory.create(gson, listOfIntegerType)).thenReturn(customListTypeAdapterFactoryInstance);
 when(customListTypeAdapterFactoryInstance.create(gson, listOfIntegerType)).thenReturn(mock(TypeAdapter.class));

 TypeAdapter<List<Integer>> actualTypeAdapter = jsonAdapterAnnotationTypeAdapterFactory.create(gson, listOfIntegerTypeToken);
 assertNotNull(actualTypeAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateWithInvalidJsonAdapterValue() {
 Class<Integer> integerClass = Integer.class;
 TypeToken<Integer> integerTypeToken = TypeToken.get(integerClass);
 Integer invalidJsonAdapterValue = 42;

 JsonAdapter jsonAdapterAnnotation = integerClass.getAnnotation(JsonAdapter.class);
 jsonAdapterAnnotation.value(invalidJsonAdapterValue);

 try {
 jsonAdapterAnnotationTypeAdapterFactory.create(gson, integerTypeToken);
 } catch (IllegalArgumentException e) {
 throw new AssertionError("The expected IllegalArgumentException was not thrown.", e);
 }
 }
}

class CustomType {
}

class CustomListTypeAdapterFactory implements TypeAdapterFactory<List<?>> {
 @Override
 public <T> TypeAdapter<List<T>> create(Gson gson, TypeToken<List<T>> typeToken) {
 return null;
 }
}