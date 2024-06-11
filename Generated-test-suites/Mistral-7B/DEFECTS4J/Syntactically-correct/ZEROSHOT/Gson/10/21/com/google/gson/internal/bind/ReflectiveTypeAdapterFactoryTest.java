import com.google.gson.*;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private ReflectiveTypeAdapterFactory factory;
 private Gson gson;

 @BeforeClass
 public static void setupClass() {
 gson = new GsonBuilder().create();
 }

 @Before
 public void setup() {
 Class<?> clazz = TestClass.class;
 factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingPolicy(), new Excluder());
 }

 @Test
 public void testCreateAdapter() {
 TypeAdapter<TestClass> adapter = factory.create(gson, new TypeToken<TestClass>() {});
 assertNotNull(adapter);
 }

 @Test
 public void testReadAndWrite() throws IOException {
 TestClass original = new TestClass("John Doe", 30, new ArrayList<>());
 TestClass deserialized = null;

 String jsonString = "{\"name\":\"Jane Doe\",\"age\":40,\"hobbies\":[\"reading\",\"painting\"]}";
 JsonReader reader = new JsonReader(new StringReader(jsonString));
 reader.setLenient(true);

 deserialized = gson.fromJson(reader, TestClass.class);
 reader.close();

 assertEquals(original.name, deserialized.name);
 assertEquals(original.age, deserialized.age);
 assertEquals(original.hobbies, deserialized.hobbies);

 assertArrayEquals(original.hobbies.toArray(), deserialized.hobbies.toArray());

 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 gson.toJson(deserialized, jsonWriter);
 jsonWriter.close();

 TestClass readBack = gson.fromJson(new StringReader(writer.toString()), TestClass.class);
 assertEquals(deserialized, readBack);
 }

 @Test
 public void testExcludedFields() throws IOException {
 TestClass original = new TestClass("John Doe", 30, new ArrayList<>());
 original.excludedField = new ExcludedClass("excluded");

 String jsonString = "{\"name\":\"Jane Doe\",\"age\":40,\"excluded\":{\"excludedField\":\"excludedValue\"}}";
 JsonReader reader = new JsonReader(new StringReader(jsonString));
 reader.setLenient(true);

 TestClass deserialized = gson.fromJson(reader, TestClass.class);
 reader.close();

 assertNull(deserialized.excludedField);

 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 gson.toJson(deserialized, jsonWriter);
 jsonWriter.close();

 String result = writer.toString();
 assertFalse(result.contains("excludedField"));
 }

 @Test
 public void testSerializedName() throws IOException {
 TestClass original = new TestClass("_John Doe_", 30, new ArrayList<>());
 original.fieldWithSerializedName = new ArrayList<>();
 original.fieldWithSerializedName.add("_hobby1_");
 original.fieldWithSerializedName.add("_hobby2_");

 String jsonString = "{\"name\":\"_Jane Doe_\",\"age\":40,\"hobbies\":[\"hobby1\",\"hobby2\"]}";
 JsonReader reader = new JsonReader(new StringReader(jsonString));
 reader.setLenient(true);

 TestClass deserialized = gson.fromJson(reader, TestClass.class);
 reader.close();

 assertEquals(original.fieldWithSerializedName, deserialized.fieldWithSerializedName);

 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 gson.toJson(deserialized, jsonWriter);
 jsonWriter.close();

 String result = writer.toString();
 assertTrue(result.contains("hobbies"));
 assertTrue(result.contains("hobby1"));
 assertTrue(result.contains("hobby2"));
 }

 @Test
 public void testAlternateNames() throws IOException {
 TestClass original = new TestClass("_John Doe_", 30, new ArrayList<>());
 original.fieldWithAlternateName = new ArrayList<>();
 original.fieldWithAlternateName.add("_first_hobby_");
 original.fieldWithAlternateName.add("_second_hobby_");

 String jsonString = "{\"name\":\"_Jane Doe_\",\"age\":40,\"hobbies\":[\"first_hobby\",\"second_hobby\"]}";
 JsonReader reader = new JsonReader(new StringReader(jsonString));
 reader.setLenient(true);

 TestClass deserialized = gson.fromJson(reader, TestClass.class);
 reader.close();

 assertEquals(original.fieldWithAlternateName, deserialized.fieldWithAlternateName);

 StringWriter writer = new StringWriter();
 JsonWriter jsonWriter = new JsonWriter(writer);
 gson.toJson(deserialized, jsonWriter);
 jsonWriter.close();

 String result = writer.toString();
 assertTrue(result.contains("first_hobby"));
 assertTrue(result.contains("second_hobby"));
 }
}

public class TestClass {
 private String name;
 private int age;
 private ArrayList<String> hobbies;
 private ExcludedClass excludedField;
 @SerializedName("fieldWithSerializedName")
 private ArrayList<String> fieldWithSerializedName;
 @SerializedName({"fieldWithAlternateName", "alt_name"})
 private ArrayList<String> fieldWithAlternateName;

 // Getters and Setters

 static class ExcludedClass {
 private String excludedField;

 // Getters and Setters
 }
}