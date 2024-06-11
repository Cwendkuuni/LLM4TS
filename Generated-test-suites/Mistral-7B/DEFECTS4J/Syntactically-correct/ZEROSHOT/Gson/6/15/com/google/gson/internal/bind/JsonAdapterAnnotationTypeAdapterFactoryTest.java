import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

 private ConstructorConstructor constructorConstructor;
 private Gson gson;

 @Before
 public void setUp() {
 constructorConstructor = new ConstructorConstructor();
 gson = new GsonBuilder().setConstructorConstructor(constructorConstructor).create();
 }

 @Test
 public void testNullType() {
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
 TypeAdapter<String> adapter = factory.create(gson, null);
 Assert.assertNull(adapter);
 }

 @Test
 public void testJsonAdapterClass() {
 JsonAdapter annotation = new JsonAdapter(StringToIntAdapter.class);
 TypeToken<String> stringType = new TypeToken<String>() {};
 TypeAdapter<String> stringAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, stringType);
 TypeAdapter<Integer> intAdapter = annotation.value().asSubtype(TypeAdapterFactory.class)
 .create(gson, stringType);

 Assert.assertEquals(intAdapter, stringAdapter);
 }

 @Test
 public void testJsonAdapterFactoryClass() {
 JsonAdapter annotation = new JsonAdapter(StringToIntAdapterFactory.class);
 TypeToken<String> stringType = new TypeToken<String>() {};
 TypeAdapter<String> stringAdapter = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor)
 .create(gson, stringType);
 TypeAdapterFactory factory = annotation.value().asSubtype(TypeAdapterFactory.class);
 TypeAdapter<Integer> intAdapter = factory.create(gson, stringType);

 Assert.assertEquals(intAdapter, stringAdapter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonAdapter() {
 JsonAdapter annotation = new JsonAdapter("InvalidType");
 TypeToken<String> stringType = new TypeToken<String>() {};
 JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

 factory.create(gson, stringType);
 }

 private static class StringToIntAdapter {}

 private static class StringToIntAdapterFactory implements TypeAdapterFactory {
 @Override
 public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
 return null;
 }
 }
}