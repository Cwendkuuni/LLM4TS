import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

 @Test
 public void testReflectiveTypeAdapterFactory() {
 // Given
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 assertNotNull(adapter);
 }

 @Test
 public void testCreateAdapterForPrimitiveTypes() {
 // Given
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<Integer> adapter = (TypeAdapter<Integer>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<Integer>() {});

 // Then
 assertNull(adapter);
 }

 @Test
 public void testCreateAdapterForInterfaceTypes() {
 // Given
 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<?> adapter = reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyInterface>() {});

 // Then
 assertNull(adapter);
 }

 @Test
 public void testCreateAdapterForSimpleClassWithDefaultNameAndNoAnnotation() {
 // Given
 class MyClass {
 private int field1;
 private String field2;

 public MyClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.setField1(1);
 obj.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.getField1(), deserializedObj.getField1());
 assertEquals(obj.getField2(), deserializedObj.getField2());
 }

 @Test
 public void testCreateAdapterForSimpleClassWithCustomNameAndNoAnnotation() {
 // Given
 class MyClass {
 @SerializedName("myField1")
 private int field1;
 private String field2;

 public MyClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.setField1(1);
 obj.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.getField1(), deserializedObj.getField1());
 assertEquals(obj.getField2(), deserializedObj.getField2());
 }

 @Test
 public void testCreateAdapterForSimpleClassWithCustomNameAndAlternateAnnotation() {
 // Given
 class MyClass {
 @SerializedName("myField1")
 @SerializedName("myFieldAlternative")
 private int field1;
 private String field2;

 public MyClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.setField1(1);
 obj.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.getField1(), deserializedObj.getField1());
 assertEquals(obj.getField2(), deserializedObj.getField2());
 }

 @Test
 public void testCreateAdapterForNestedClassWithDefaultNameAndNoAnnotation() {
 // Given
 class MyClass {
 private MyNestedClass nested = new MyNestedClass();

 public static class MyNestedClass {
 private int field1;
 private String field2;

 public MyNestedClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.nested.setField1(1);
 obj.nested.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.nested.getField1(), deserializedObj.nested.getField1());
 assertEquals(obj.nested.getField2(), deserializedObj.nested.getField2());
 }

 @Test
 public void testCreateAdapterForNestedClassWithCustomNameAndNoAnnotation() {
 // Given
 class MyClass {
 private MyNestedClass nested;

 public static class MyNestedClass {
 @SerializedName("myNestedField1")
 private int field1;
 private String field2;

 public MyNestedClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.nested = new MyNestedClass();
 obj.nested.setField1(1);
 obj.nested.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.nested.getField1(), deserializedObj.nested.getField1());
 assertEquals(obj.nested.getField2(), deserializedObj.nested.getField2());
 }

 @Test
 public void testCreateAdapterForNestedClassWithCustomNameAndAlternateAnnotation() {
 // Given
 class MyClass {
 private MyNestedClass nested;

 public static class MyNestedClass {
 @SerializedName("myNestedField1")
 @SerializedName("myNestedFieldAlternative")
 private int field1;
 private String field2;

 public MyNestedClass() {
 }

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
 }
 }

 Gson gson = new GsonBuilder().create();
 ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
 new ConstructorConstructor(),
 new FieldNamingPolicy(),
 new Excluder()
 );

 // When
 TypeAdapter<MyClass> adapter = (TypeAdapter<MyClass>) reflectiveTypeAdapterFactory.create(gson, new TypeToken<MyClass>() {});

 // Then
 MyClass obj = new MyClass();
 obj.nested = new MyNestedClass();
 obj.nested.setField1(1);
 obj.nested.setField2("Test");
 String json = gson.toJson(obj);
 MyClass deserializedObj = gson.fromJson(json, MyClass.class);
 assertEquals(obj.nested.getField1(), deserializedObj.nested.getField1());
 assertEquals(obj.nested.getField2(), deserializedObj.nested.getField2());
 }
}