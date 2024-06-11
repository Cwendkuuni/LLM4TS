import static org.junit.Assert.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private final Gson gson = new GsonBuilder().create();

 @Test
 public void testSimpleJsonAdapter() {
 TypeAdapter<String> stringAdapter = new Gson().getAdapter(String.class);
 TypeToken<String> stringToken = TypeToken.get(String.class);
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeAdapter<String> jsonAdapter = factory.create(gson, stringToken);

 assertEquals(stringAdapter, jsonAdapter);
 }

 @Test
 public void testCustomJsonAdapter() {
 JsonAdapter annotation = CustomJsonAdapter.class.getAnnotation(JsonAdapter.class);
 TypeToken<CustomJsonAdapter> adapterTypeToken = new TypeToken<CustomJsonAdapter>() {};
 TypeAdapter<CustomJsonAdapter> adapter = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor())
 .create(gson, adapterTypeToken);

 assertNotNull(adapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapter() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeToken<Integer> integerToken = new TypeToken<Integer>() {};
 JsonAdapter annotation = Integer.class.getAnnotation(JsonAdapter.class);

 try {
 factory.create(gson, integerToken);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNullJsonAdapter() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeToken<String> stringToken = new TypeToken<String>() {};
 JsonAdapter annotation = null;

 assertNull(factory.create(gson, stringToken));
 }

 @Test
 public void testNullValueJsonAdapter() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeToken<String> stringToken = TypeToken.get(String.class);
 JsonAdapter annotation = null;

 assertNull(factory.create(gson, stringToken));
 }

 @Test
 public void testNullGson() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
 TypeToken<String> stringToken = TypeToken.get(String.class);
 JsonAdapter annotation = String.class.getAnnotation(JsonAdapter.class);
 Gson gson = null;

 assertNull(factory.create(gson, stringToken));
 }
}