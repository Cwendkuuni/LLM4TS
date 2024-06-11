import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 ObjectReader reader = mapper.readerForListOf(Object.class);
 List<Object> result = reader.readValue(new String("[]").getBytes());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testDeserializeSingleValueArray() throws IOException {
 ObjectReader reader = mapper.readerForListOf(Integer.class);
 List<Object> result = reader.readValue(new String("[1]").getBytes());
 assertEquals(1, result.size());
 assertEquals(1, result.get(0));
 }

 @Test
 public void testDeserializeComplexArray() throws IOException {
 ObjectReader reader = mapper.readerForListOf(MyClass.class);
 List<Object> result = reader.readValue(new String("[{\"name\":\"Alice\",\"age\":30},{\"name\":\"Bob\",\"age\":25}]").getBytes());
 assertEquals(2, result.size());
 MyClass firstElement = (MyClass) result.get(0);
 assertEquals("Alice", firstElement.getName());
 assertEquals(30, firstElement.getAge());
 MyClass secondElement = (MyClass) result.get(1);
 assertEquals("Bob", secondElement.getName());
 assertEquals(25, secondElement.getAge());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 ObjectReader reader = mapper.readerForListOf(String.class);
 reader.readValue(new String("{\"name\":\"Alice\"}").getBytes()); // expected JsonProcessingException
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidCollectionType() throws IOException {
 ObjectReader reader = mapper.readerForListOf(String[].class);
 reader.readValue(new String("[1,2,3]").getBytes()); // expected JsonMappingException
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullJson() throws IOException {
 ObjectReader reader = mapper.readerForListOf(String[].class);
 reader.readValue(new String(null).getBytes()); // expected IOException
 }
}

class MyClass {
 private String name;
 private int age;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
}