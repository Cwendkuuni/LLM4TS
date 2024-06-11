import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final class TestData {
 @SerializedName("name") String name;
 @SerializedName("age") int age;
 @SerializedName("list") List<String> list;
 }

 @Test
 public void testReflectiveTypeAdapterFactory() {
 Gson gson = new GsonBuilder()
 .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
 .setExcluder(new Excluder())
 .create();
 TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(TestData.class),
 new FieldNamingStrategy() {
 @Override public String translateName(Field field) {
 return field.getName();
 }
 },
 new Excluder()
 );
 TypeAdapter<TestData> testDataAdapter = factory.create(gson, new TypeToken<TestData>() {});

 TestData testData = new TestData();
 testData.name = "John Doe";
 testData.age = 30;
 testData.list = new ArrayList<>();
 testData.list.add("apple");
 testData.list.add("banana");

 String json = gson.toJson(testData);
 System.out.println(json); // {"name":"John Doe","age":30,"list":["apple","banana"]}

 TestData deserializedTestData = gson.fromJson(json, TestData.class);
 assertEquals(testData.name, deserializedTestData.name);
 assertEquals(testData.age, deserializedTestData.age);
 assertEquals(testData.list, deserializedTestData.list);

 // Test for null value
 TestData nullTestData = null;
 String jsonForNull = gson.toJson(nullTestData);
 TestData deserializedNullTestData = gson.fromJson(jsonForNull, TestData.class);
 assertNull(deserializedNullTestData);

 // Test for empty list
 TestData emptyListTestData = new TestData();
 emptyListTestData.name = "John Doe";
 emptyListTestData.age = 30;
 emptyListTestData.list = new ArrayList<>();
 String jsonForEmptyList = gson.toJson(emptyListTestData);
 TestData deserializedEmptyListTestData = gson.fromJson(jsonForEmptyList, TestData.class);
 assertEquals(emptyListTestData.name, deserializedEmptyListTestData.name);
 assertEquals(emptyListTestData.age, deserializedEmptyListTestData.age);
 assertEquals(Collections.emptyList(), deserializedEmptyListTestData.list);
 }
}