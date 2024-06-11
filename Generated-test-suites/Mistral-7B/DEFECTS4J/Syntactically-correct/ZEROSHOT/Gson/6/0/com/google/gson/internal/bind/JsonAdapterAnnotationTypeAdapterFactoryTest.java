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
 public void setup() {
 constructorConstructor = mock(ConstructorConstructor.class);
 gson = mock(Gson.class);
 }

 @Test
 public void testNullTypeToken() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<?> typeAdapter = factory.create(gson, null);
 assertNull(typeAdapter);
 }

 @Test
 public void testNullAnnotation() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
 TypeAdapter<Integer> typeAdapter = factory.create(gson, typeToken);
 assertNull(typeAdapter);
 }

 @Test
 public void testJsonAdapterAnnotatedClass() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 JsonAdapter jsonAdapterAnnotation = new JsonAdapter(MyJsonAdapter.class);
 TypeToken<MyData> typeToken = new TypeToken<MyData>() {};
 TypeAdapter<MyData> typeAdapter = factory.create(gson, typeToken);
 assertEquals(MyJsonAdapter.class, typeAdapter.getClass());
 }

 @Test
 public void testJsonAdapterAnnotatedInterface() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 JsonAdapter jsonAdapterAnnotation = new JsonAdapter(MyJsonAdapter.class);
 TypeToken<MyInterface> typeToken = new TypeToken<MyInterface>() {};
 TypeAdapter<MyInterface> typeAdapter = factory.create(gson, typeToken);
 assertEquals(MyJsonAdapter.class, typeAdapter.getClass());
 }

 @Test
 public void testJsonAdapterFactoryAnnotatedClass() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 JsonAdapterFactory jsonAdapterFactoryAnnotation = new JsonAdapterFactory(MyJsonAdapterFactory.class);
 TypeToken<MyData> typeToken = new TypeToken<MyData>() {};
 TypeAdapter<MyData> typeAdapter = factory.create(gson, typeToken);
 assertEquals(MyJsonAdapterFactory.class, typeAdapter.getClass().getAnnotation(JsonAdapter.class).value().getClass());
 }

 @Test
 public void testJsonAdapterFactoryAnnotatedInterface() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 JsonAdapterFactory jsonAdapterFactoryAnnotation = new JsonAdapterFactory(MyJsonAdapterFactory.class);
 TypeToken<MyInterface> typeToken = new TypeToken<MyInterface>() {};
 TypeAdapter<MyInterface> typeAdapter = factory.create(gson, typeToken);
 assertEquals(MyJsonAdapterFactory.class, typeAdapter.getClass().getAnnotation(JsonAdapter.class).value().getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonAdapterValueNotTypeAdapterOrFactory() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 JsonAdapter jsonAdapterAnnotation = new JsonAdapter("not a TypeAdapter or TypeAdapterFactory");
 TypeToken<MyData> typeToken = new TypeToken<MyData>() {};
 TypeAdapter<MyData> typeAdapter = factory.create(gson, typeToken);
 fail("Should throw IllegalArgumentException");
 }
}

class MyData {}
class MyJsonAdapter {}
class MyJsonAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return null;
 }
}
class MyInterface {}