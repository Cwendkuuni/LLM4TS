import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.excluded.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 private static final ReflectiveTypeAdapterFactory factory;
 private static final Class<Person> personClass;

 static {
 personClass = Person.class;
 Gson builder = new GsonBuilder();
 builder.setExclusionStrategies(new Excluder());
 factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
 new FieldNamingPolicy(), builder.getExclusionStrategies());
 }

 private Person person;
 private JsonReader reader;
 private JsonWriter writer;

 @Before
 public void setUp() throws Exception {
 person = new Person("Alice", 30);
 reader = new JsonReader(new StringReader(JSON_STRING));
 writer = new JsonWriter(new StringWriter());
 }

 @Test
 public void testSerializationAndDeserialization() throws IOException {
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 String json = adapter.toJson(person);
 Person deserializedPerson = adapter.fromJson(json, Person.class);
 assertEquals(person, deserializedPerson);
 }

 @Test
 public void testSerializationAndDeserializationWithJsonAdapter() throws IOException {
 PersonWithJsonAdapter personWithJsonAdapter = new PersonWithJsonAdapter("Bob", 25);
 TypeAdapter<PersonWithJsonAdapter> adapter = factory.create(new Gson(),
 new TypeToken<PersonWithJsonAdapter>() {});
 String json = adapter.toJson(personWithJsonAdapter);
 PersonWithJsonAdapter deserializedPerson = adapter.fromJson(json, PersonWithJsonAdapter.class);
 assertEquals(personWithJsonAdapter, deserializedPerson);
 }

 @Test
 public void testSerializationAndDeserializationWithFieldsAccessible() throws Exception {
 person.setAge(40);
 ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(), new FieldNamingPolicy(), null);
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 String json = adapter.toJson(person);
 Person deserializedPerson = adapter.fromJson(json, Person.class);
 assertEquals(person, deserializedPerson);
 assertNotEquals(person.getAge(), deserializedPerson.getAge()); // fields should not be accessible
 }

 @Test
 public void testSerializationWithExcludedField() throws IOException {
 Person personWithoutName = new Person(null, 30);
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 String json = adapter.toJson(personWithoutName);
 assertNotNull(json); // even when the name is excluded, the JSON should not be null
 }

 @Test
 public void testDeserializationWithExcludedField() throws IOException {
 String json = "{\"age\":30}";
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 Person deserializedPerson = adapter.fromJson(json, Person.class);
 assertNull(deserializedPerson.getName()); // the name should not be deserialized
 }

 @Test(expected = JsonSyntaxException.class)
 public void testDeserializationWithInvalidJson() throws IOException {
 String json = "{age:30}"; // missing quotes around key
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 adapter.fromJson(json, Person.class);
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializationWithNullValue() throws IOException {
 String json = "null";
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 adapter.fromJson(json, Person.class);
 }

 @Test(expected = JsonParseException.class)
 public void testDeserializationWithNonExistentField() throws IOException {
 String json = "{\"nonExistentField\":30}";
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 adapter.fromJson(json, Person.class);
 }

 @Test
 public void testDeserializationWithMissingField() throws IOException {
 String json = "{\"age\":30}";
 TypeAdapter<Person> adapter = factory.create(new Gson(), new TypeToken<Person>() {});
 Person deserializedPerson = adapter.fromJson(json, Person.class);
 assertNotNull(deserializedPerson);
 assertNull(deserializedPerson.getName());
 }

 private static class Person {
 private final String name;
 private int age;

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
 }

 private static class PersonWithJsonAdapter {
 @JsonAdapter(PersonJsonAdapter.class)
 private String name;
 private int age;

 public PersonWithJsonAdapter(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }
 }

 private static class PersonJsonAdapter extends TypeAdapter<PersonWithJsonAdapter> {
 @Override public PersonWithJsonAdapter read(JsonReader jsonReader) throws IOException {
 return new PersonWithJsonAdapter(jsonReader.nextString(), jsonReader.nextInt());
 }

 @Override public void write(JsonWriter jsonWriter, PersonWithJsonAdapter value) throws IOException {
 jsonWriter.beginObject();
 jsonWriter.name("name");
 jsonWriter.value(value.getName());
 jsonWriter.name("age");
 jsonWriter.value(value.getAge());
 jsonWriter.endObject();
 }
 }

 private static final String JSON_STRING = "{\"name\":\"Alice\",\"age\":30}";
}