import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setUp() {
 constructorConstructor = new ConstructorConstructor();
 gson = new GsonBuilder().setPrePreProcessor(new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)).create();
 }

 @Test
 public void testNullType() {
 TypeAdapter<Object> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<Object>() {});
 assertNull(adapter);
 }

 @Test
 public void testJsonAdapterAnnotatedType() {
 JsonAdapter annotation = MyClass.class.getAnnotation(JsonAdapter.class);
 TypeAdapter<MyClass> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<MyClass>() {});
 assertEquals(MyTypeAdapter.class, adapter.getClass());
 }

 @Test
 public void testTypeAdapterAnnotatedType() {
 MyTypeAdapter myTypeAdapter = new MyTypeAdapter();
 JsonAdapter annotation = MyClass.class.getAnnotation(JsonAdapter.class);
 annotation.value(MyTypeAdapter.class);
 TypeAdapter<MyClass> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<MyClass>() {});
 assertEquals(myTypeAdapter, adapter);
 }

 @Test
 public void testTypeAdapterFactoryAnnotatedType() {
 MyTypeAdapterFactory myTypeAdapterFactory = new MyTypeAdapterFactory();
 JsonAdapter annotation = MyClass.class.getAnnotation(JsonAdapter.class);
 annotation.value(MyTypeAdapterFactory.class);
 TypeAdapter<MyClass> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<MyClass>() {});
 assertNotNull(adapter);
 assertEquals(MyTypeAdapter.class, adapter.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapterValue() {
 JsonAdapter annotation = MyClass.class.getAnnotation(JsonAdapter.class);
 annotation.value("InvalidClass");
 TypeAdapter<MyClass> adapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, new TypeToken<MyClass>() {});
 fail("IllegalArgumentException not thrown.");
 }
}

class MyClass {
 @JsonAdapter(MyTypeAdapter.class)
 public static class MyNestedClass {
 }
}

class MyTypeAdapter implements TypeAdapter<MyClass> {
 @Override
 public MyClass read(JsonReader in) {
 return null;
 }

 @Override
 public void write(JsonWriter out, MyClass value) {
 fail("Should not be called.");
 }
}

class MyTypeAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
 return new MyTypeAdapter();
 }
}