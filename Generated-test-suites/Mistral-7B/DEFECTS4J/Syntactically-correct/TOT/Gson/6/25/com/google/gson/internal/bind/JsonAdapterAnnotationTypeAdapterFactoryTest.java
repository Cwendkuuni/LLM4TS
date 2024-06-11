import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private JsonAdapterAnnotationTypeAdapterFactory factory;
 private Gson gson;

 @Before
 public void setup() {
 gson = new Gson();
 factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor(gson));
 }

 @Test
 public void testStringAdapter() {
 JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
 TypeAdapter<String> stringTypeAdapter = factory.create(gson, new TypeToken<String>() {}.getType());
 assertNotNull(stringTypeAdapter);
 String jsonString = "\"test string\"";
 String deserializedString = stringTypeAdapter.fromJson(jsonString);
 assertEquals("test string", deserializedString);
 }

 @Test
 public void testCustomAdapter() {
 MyCustomClass customClassAnnotation = MyCustomClass.class.getAnnotation(JsonAdapter.class);
 TypeToken<MyCustomClass> customClassToken = new TypeToken<MyCustomClass>() {}.getType();
 TypeAdapter<MyCustomClass> customClassTypeAdapter = factory.create(gson, customClassToken);
 assertNotNull(customClassTypeAdapter);
 MyCustomClass jsonCustomClass = new Gson()
 .fromJson("{\"name\":\"test custom class\", \"age\":3}", MyCustomClass.class);
 MyCustomClass deserializedCustomClass = customClassTypeAdapter.fromJson(new Gson().toJson(jsonCustomClass));
 assertEquals(jsonCustomClass.getName(), deserializedCustomClass.getName());
 assertEquals(jsonCustomClass.getAge(), deserializedCustomClass.getAge());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAdapter() {
 JsonAdapter annotation = Integer.class.getAnnotation(JsonAdapter.class);
 TypeToken<Integer> integerToken = new TypeToken<Integer>() {}.getType();
 assertNull(factory.create(gson, integerToken));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAdapter() {
 JsonAdapter annotation = null;
 TypeToken<String> stringToken = new TypeToken<String>() {}.getType();
 assertNull(factory.create(gson, stringToken));
 }
}

class MyCustomClass {
 String name;
 int age;

 // getters and setters omitted for brevity
}