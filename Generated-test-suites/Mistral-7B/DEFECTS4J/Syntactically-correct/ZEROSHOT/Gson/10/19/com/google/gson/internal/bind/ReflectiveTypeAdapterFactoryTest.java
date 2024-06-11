import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 @Test
 public void testReflectiveTypeAdapterFactory() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 // Act
 TypeAdapter<MyClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});
 // Assert
 assertNotNull(typeAdapter);
 }

 @Test
 public void testCreateTypeAdapterNullTypeToken() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 // Act and Assert
 assertNull(reflectiveTypeAdapterFactory.create(gson, null));
 }

 @Test
 public void testCreateTypeAdapterNonObjectType() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 // Act and Assert
 assertNull(reflectiveTypeAdapterFactory.create(gson, new TypeToken<Integer>() {}));
 }

 @Test
 public void testGetBoundFieldsNullTypeToken() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 // Act and Assert
 assertNull(reflectiveTypeAdapterFactory.getBoundFields(gson, null, Object.class));
 }

 @Test
 public void testGetBoundFieldsNullRawType() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
 // Act and Assert
 assertNotNull(reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, null));
 assertTrue(reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, null).isEmpty());
 }

 @Test
 public void testGetBoundFieldsEmptyInterface() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 TypeToken<MyEmptyInterface> typeToken = new TypeToken<MyEmptyInterface>() {};
 // Act and Assert
 assertNotNull(reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, MyEmptyInterface.class));
 assertTrue(reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, MyEmptyInterface.class).isEmpty());
 }

 @Test
 public void testGetBoundFieldsClassWithFields() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 Class<MyClassWithFields> rawType = MyClassWithFields.class;
 TypeToken<MyClassWithFields> typeToken = new TypeToken<MyClassWithFields>() {};
 // Act
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, rawType);
 // Assert
 assertNotNull(boundFields);
 assertFalse(boundFields.isEmpty());
 }

 @Test
 public void testWriteReadNullValue() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
 // Act and Assert
 MyClass value = null;
 TypeAdapter<MyClass> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
 String json = typeAdapter.toJson(value);
 assertNull(json);
 MyClass deserializedValue = typeAdapter.read(new JsonReader(new StringReader(json))).getValue();
 assertNull(deserializedValue);
 }

 @Test
 public void testWriteReadEmptyInterface() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 TypeToken<MyEmptyInterface> typeToken = new TypeToken<MyEmptyInterface>() {};
 // Act and Assert
 MyEmptyInterface value = null;
 TypeAdapter<MyEmptyInterface> typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
 String json = typeAdapter.toJson(value);
 assertNull(json);
 MyEmptyInterface deserializedValue = typeAdapter.read(new JsonReader(new StringReader(json))).getValue();
 assertNull(deserializedValue);
 }

 @Test
 public void testWriteReadClassWithFields() {
 // Arrange
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 MyClassWithFields value = new MyClassWithFields("test");
 TypeToken<MyClassWithFields> typeToken = new TypeToken<MyClassWithFields>() {};
 // Act and Assert
 String json = typeAdapter.toJson(value);
 MyClassWithFields deserializedValue = typeAdapter.read(new JsonReader(new StringReader(json))).getValue();
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testExcludeField() {
 // Arrange
 Class<MyClassWithExcludedFields> rawType = MyClassWithExcludedFields.class;
 TypeToken<MyClassWithExcludedFields> typeToken = new TypeToken<MyClassWithExcludedFields>() {};
 Excluder excluder = new Excluder() {};
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 excluder
 );
 // Act
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, rawType);
 // Assert
 assertEquals(2, boundFields.size());
 assertFalse(boundFields.containsKey("name"));
 assertFalse(boundFields.containsKey("age"));
 }

 @Test
 public void testExcludeFieldByName() {
 // Arrange
 Class<MyClassWithExcludedFields> rawType = MyClassWithExcludedFields.class;
 TypeToken<MyClassWithExcludedFields> typeToken = new TypeToken<MyClassWithExcludedFields>() {};
 Excluder excluder = new Excluder() {
 @Override
 public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return false;
 }

 @Override
 public boolean excludeField(Field field, boolean serialize) {
 return serialize && field.getName().equals("age");
 }
 };
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 excluder
 );
 // Act
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, rawType);
 // Assert
 assertEquals(2, boundFields.size());
 assertTrue(boundFields.containsKey("name"));
 assertFalse(boundFields.containsKey("age"));
 }

 @Test
 public void testExcludeFieldByAnnotation() {
 // Arrange
 Class<MyClassWithExcludedAnnotatedField> rawType = MyClassWithExcludedAnnotatedField.class;
 TypeToken<MyClassWithExcludedAnnotatedField> typeToken = new TypeToken<MyClassWithExcludedAnnotatedField>() {};
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );
 // Act
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, rawType);
 // Assert
 assertEquals(2, boundFields.size());
 assertFalse(boundFields.containsKey("excludedAnnotatedField"));
 assertTrue(boundFields.containsKey("name"));
 }

 @Test
 public void testExcludeClass() {
 // Arrange
 Class<MyClassWithExcludedClass> rawType = MyClassWithExcludedClass.class;
 TypeToken<MyClassWithExcludedClass> typeToken = new TypeToken<MyClassWithExcludedClass>() {};
 Excluder excluder = new Excluder() {
 @Override
 public boolean excludeClass(Class<?> clazz, boolean serialize) {
 return serialize && clazz.isAssignableFrom(MyOtherClass.class);
 }

 @Override
 public boolean excludeField(Field field, boolean serialize) {
 return false;
 }
 };
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 excluder
 );
 // Act
 Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, rawType);
 // Assert
 assertTrue(boundFields.isEmpty());
 }
}

class MyClass {

}

class MyEmptyInterface {

}

class MyClassWithFields {
 String name;
 int age;

 public MyClassWithFields(String name) {
 this.name = name;
 }
}

class MyClassWithExcludedFields {
 @SerializedName("excludedField")
 String name;
 int age;

 public MyClassWithExcludedFields(String name, int age) {
 this.name = name;
 this.age = age;
 }
}

class MyClassWithExcludedAnnotatedField {
 @SerializedName("name")
 String name;
 @JsonAdapter(MyJsonAdapter.class)
 String excludedAnnotatedField;

 public MyClassWithExcludedAnnotatedField(String name, String excludedAnnotatedField) {
 this.name = name;
 this.excludedAnnotatedField = excludedAnnotatedField;
 }
}

class MyClassWithExcludedClass extends MyOtherClass {

}

class FieldNamingPolicy {
 @Override
 public String translateName(Field field) {
 return field.getName();
 }
}

class MyJsonAdapter {
 // empty adapter
}