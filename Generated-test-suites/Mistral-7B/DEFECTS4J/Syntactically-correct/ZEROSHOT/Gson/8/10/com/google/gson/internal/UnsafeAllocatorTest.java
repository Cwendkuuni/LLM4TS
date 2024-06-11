import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatoranon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 @Test
 public void testNewInstance_ClassWithConstructor() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<Person> personClass = Person.class;
 Person person = unsafeAllocator.newInstance(personClass);
 assertNotNull(person);
 assertEquals("John Doe", person.getName());
 }

 @Test
 public void testNewInstance_ClassWithNoConstructor() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<SerializableObject> serializableObjectClass = SerializableObject.class;
 assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(serializableObjectClass);
 });
 }

 @Test
 public void testNewInstance_ClassWithInterface() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<Serializable> serializableClass = Serializable.class;
 assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(serializableClass);
 });
 }

 @Test
 public void testNewInstance_ClassWithAbstractMethod() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<AbstractClass> abstractClass = AbstractClass.class;
 assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(abstractClass);
 });
 }

 @Test
 public void testNewInstance_ClassWithPrivateConstructor() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<PrivateConstructorClass> privateConstructorClass = PrivateConstructorClass.class;
 assertThrows(IllegalAccessException.class, () -> {
 privateConstructorClass.getDeclaredConstructor().newInstance();
 });
 Person person = (Person) newInstanceFromSerialization(privateConstructorClass);
 assertNotNull(person);
 assertEquals("John Doe", person.getName());
 Person personFromUnsafe = unsafeAllocator.newInstance(privateConstructorClass);
 assertNotNull(personFromUnsafe);
 assertEquals("John Doe", personFromUnsafe.getName());
 assertEquals(person, personFromUnsafe);
 }

 @Test
 public void testNewInstance_ClassWithPublicConstructor() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<PublicConstructorClass> publicConstructorClass = PublicConstructorClass.class;
 Person person = unsafeAllocator.newInstance(PublicConstructorClass);
 assertNotNull(person);
 assertEquals("John Doe", person.getName());
 }

 @Test
 public void testNewInstance_ClassWithNoPublicOrPrivateConstructor() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<NoPublicOrPrivateConstructorClass> noPublicOrPrivateConstructorClass = NoPublicOrPrivateConstructorClass.class;
 assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(noPublicOrPrivateConstructorClass);
 });
 }

 @Test
 public void testNewInstance_ClassWithFinalClass() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<FinalClass> finalClass = FinalClass.class;
 assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(finalClass);
 });
 }

 @Test
 public void testNewInstance_ClassWithEnum() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<Color> colorClass = Color.class;
 Color color = unsafeAllocator.newInstance(colorClass);
 assertNotNull(color);
 assertEquals(Color.RED, color);
 }

 @Test
 public void testNewInstance_ClassWithArray() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<int[]> intArrayClass = int[].class;
 int[] intArray = unsafeAllocator.newInstance(intArrayClass);
 assertNotNull(intArray);
 assertEquals(0, intArray.length);
 }

 @Test
 public void testNewInstance_ClassWithPrimitiveType() throws Exception {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<int.class> intClass = int.class;
 int i = unsafeAllocator.newInstance(intClass);
 assertNotNull(i);
 assertEquals(0, i);
 }

 private static <T> T newInstanceFromSerialization(Class<T> clazz) throws Exception {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 T obj = clazz.newInstance();
 objectOutputStream.writeObject(obj);
 objectOutputStream.close();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 return (T) objectInputStream.readObject();
 }

 private static class Person implements Serializable {
 private static final long serialVersionUID = 1L;
 private String name;

 Person() {
 this.name = "John Doe";
 }

 Person(String name) {
 this.name = name;
 }

 String getName() {
 return name;
 }
 }

 private static class SerializableObject implements Serializable {
 private static final long serialVersionUID = 1L;
 }

 private static class AbstractClass {
 abstract String getName();
 }

 private static class PrivateConstructorClass implements Serializable {
 private static final long serialVersionUID = 1L;
 private String name;

 private PrivateConstructorClass() {
 this.name = "John Doe";
 }

 private static Person newInstanceFromSerialization(Class<?> clazz) throws Exception {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 Person person = new Person();
 objectOutputStream.writeObject(person);
 objectOutputStream.close();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 return (Person) objectInputStream.readObject();
 }

 private String getName() {
 return name;
 }
 }

 private static class PublicConstructorClass {
 private static final long serialVersionUID = 1L;
 private String name;

 public PublicConstructorClass() {
 this.name = "John Doe";
 }

 public String getName() {
 return name;
 }
 }

 private static class NoPublicOrPrivateConstructorClass {
 private static final long serialVersionUID = 1L;
 }

 private static class FinalClass {
 private static final long serialVersionUID = 1L;
 }

 private static enum Color {
 RED, GREEN, BLUE
 }

 private static class SerializableArray implements Serializable {
 private static final long serialVersionUID = 1L;
 private int[] values;

 private SerializableArray() {
 values = new int[1];
 }

 private SerializableArray(int[] values) {
 this.values = values;
 }

 private int[] getValues() {
 return values;
 }
 }
}