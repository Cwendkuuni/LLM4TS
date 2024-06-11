import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private Gson gson;
 private ReflectiveTypeAdapterFactory factory;

 @Before
 public void setup() {
 gson = new GsonBuilder().create();
 factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
 // A simple field naming strategy for testing purposes
 @Override public String translateName(Field f) {
 return f.getName();
 }
 }, new Excluder() {
 // A simple excluder strategy for testing purposes
 @SuppressWarnings("unchecked")
 @Override public boolean excludeField(Field f, boolean serialize) {
 return false;
 }
 });
 }

 @Test
 public void testCreateTypeAdapterForSimpleClass() {
 Class<Person> PersonClass = Person.class;
 TypeAdapter<Person> personAdapter = (TypeAdapter<Person>) factory.create(gson, new TypeToken<Person>() {});
 assertNotNull(personAdapter);
 }

 @Test
 public void testCreateTypeAdapterForInterface() {
 Class<Animal> AnimalInterface = Animal.class;
 TypeAdapter<Animal> animalAdapter = (TypeAdapter<Animal>) factory.create(gson, new TypeToken<Animal>() {});
 assertNull(animalAdapter);
 }

 @Test
 public void testSerializationAndDeserializationOfSimpleClass() throws IOException {
 Person person = new Person("John Doe", 30);
 String json = gson.toJson(person);
 Person deserializedPerson = gson.fromJson(json, Person.class);
 assertEquals(person, deserializedPerson);
 }

 @Test
 public void testSerializationAndDeserializationOfFieldWithJsonAdapter() throws IOException {
 class PersonWithJsonAdapter {
 @SerializedName("name")
 private String name;
 @JsonAdapter(DateAdapter.class)
 private Date birthDate;

 public PersonWithJsonAdapter(String name, Date birthDate) {
 this.name = name;
 this.birthDate = birthDate;
 }
 }

 PersonWithJsonAdapter personWithJsonAdapter = new PersonWithJsonAdapter("John Doe", new Date(System.currentTimeMillis()));
 String json = gson.toJson(personWithJsonAdapter);
 PersonWithJsonAdapter deserializedPersonWithJsonAdapter = gson.fromJson(json, PersonWithJsonAdapter.class);
 assertEquals(personWithJsonAdapter, deserializedPersonWithJsonAdapter);
 }

 @Test(expected = JsonSyntaxException.class)
 public void testSerializationAndDeserializationOfFieldWithInvalidType() throws IOException {
 class PersonWithInvalidField {
 private int age;
 private String invalidField = new Object();
 }

 PersonWithInvalidField personWithInvalidField = new PersonWithInvalidField();
 personWithInvalidField.age = 30;
 String json = gson.toJson(personWithInvalidField);
 fail("Expected JsonSyntaxException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testReadingFieldWithNoSetter() throws IOException {
 class PersonWithNoSetter {
 private final String name;

 public PersonWithNoSetter(String name) {
 this.name = name;
 }
 }

 PersonWithNoSetter personWithNoSetter = new PersonWithNoSetter("John Doe");
 String json = gson.toJson(personWithNoSetter);
 JsonReader jsonReader = new JsonReader(new StringReader(json));
 assertFalse(jsonReader.nextBoolean()); // nextBoolean() is used to check that we don't read anything from the JSON
 }

 @Test(expected = IllegalAccessException.class)
 public void testWritingFieldWithNoGetter() throws IOException {
 class PersonWithNoGetter {
 private int age;

 public int getAge() {
 return age;
 }

 private PersonWithNoGetter(int age) {
 this.age = age;
 }
 }

 PersonWithNoGetter personWithNoGetter = new PersonWithNoGetter(30);
 JsonWriter jsonWriter = new JsonWriter(new StringWriter());
 personWithNoGetter.write(jsonWriter, personWithNoGetter);
 fail("Expected IllegalAccessException");
 }

 @Test
 public void testReadingFieldWithExcludedName() throws IOException {
 class PersonWithExcludedName {
 @Exclude
 private String excludedName;
 private String name;

 public PersonWithExcludedName(String name, String excludedName) {
 this.name = name;
 this.excludedName = excludedName;
 }
 }

 PersonWithExcludedName personWithExcludedName = new PersonWithExcludedName("John Doe", "Johnny");
 String json = gson.toJson(personWithExcludedName);
 JsonReader jsonReader = new JsonReader(new StringReader(json));
 jsonReader.beginObject();
 assertTrue(jsonReader.nextName().equals("name"));
 String name = jsonReader.nextString(); // reads "John Doe"
 jsonReader.endObject();
 assertNull(jsonReader.nextName());
 }

 @Test
 public void testReadingFieldWithCircularReference() throws IOException {
 class PersonWithCircularReference {
 private PersonWithCircularReference partner;

 public PersonWithCircularReference(PersonWithCircularReference partner) {
 this.partner = partner;
 }
 }

 PersonWithCircularReference personWithCircularReference1 = new PersonWithCircularReference(null);
 PersonWithCircularReference personWithCircularReference2 = new PersonWithCircularReference(personWithCircularReference1);
 String json = gson.toJson(personWithCircularReference1);
 JsonReader jsonReader = new JsonReader(new StringReader(json));
 jsonReader.beginObject();
 assertFalse(jsonReader.nextName().equals("partner")); // nextName() is used to check that we don't read "partner" field
 jsonReader.endObject();
 }
}