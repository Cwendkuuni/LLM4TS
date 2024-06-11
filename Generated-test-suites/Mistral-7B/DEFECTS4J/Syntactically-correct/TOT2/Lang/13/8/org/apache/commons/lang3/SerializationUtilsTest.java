import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException, IOException {
        Person person = new Person("John Doe", 30);
        Person clonedPerson = SerializationUtils.clone(person);
        assertEquals(person, clonedPerson);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws SerializationException, IOException {
        Person person = new Person("John Doe", 30);
        byte[] serializedPerson = SerializationUtils.serialize(person);
        Person deserializedPerson = SerializationUtils.deserialize(new ByteArrayInputStream(serializedPerson));
        assertEquals(person, deserializedPerson);
    }

    @Test
    public void testSerializeAndDeserializeByteArray() throws SerializationException, IOException {
        Person person = new Person("John Doe", 30);
        byte[] serializedPerson = SerializationUtils.serialize(person);
        Person deserializedPerson = SerializationUtils.deserialize(serializedPerson);
        assertEquals(person, deserializedPerson);
    }

    @Test
    public void testSerializeNullObject() {
        try {
            SerializationUtils.serialize(null);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeNullInput() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeClassNotFoundException() throws SerializationException, IOException {
        Person person = new Person("John Doe", 30);
        byte[] serializedPerson = SerializationUtils.serialize(person);
        ClassLoader loader = new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("Test ClassNotFoundException");
            }
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedPerson);
        try (ObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, loader)) {
            fail("Expected ClassNotFoundException");
        }
    }

    @Test
    public void testDeserializeIOException() throws SerializationException, IOException {
        Person person = new Person("John Doe", 30);
        byte[] serializedPerson = SerializationUtils.serialize(person);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedPerson);
        try (ObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, null)) {
            bais.close();
            fail("Expected IOException on closing InputStream");
        }
    }

    static class Person implements Serializable {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name);
            result = 31 * result + age;
            return result;
        }
    }
}