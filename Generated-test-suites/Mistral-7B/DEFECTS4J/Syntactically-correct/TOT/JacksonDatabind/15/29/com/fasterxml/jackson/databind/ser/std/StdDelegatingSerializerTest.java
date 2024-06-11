import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDelegateSerializerWithSimpleObjects() throws JsonGenerationException, JsonMappingException, IOException {
        // Given
        Converter<Integer, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Integer input = 123;
        String expectedOutput = "123";

        // When
        String output = serializeAndGetOutput(serializer, input);

        // Then
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDelegateSerializerWithCollections() throws JsonGenerationException, JsonMappingException, IOException {
        // Given
        Converter<List<Integer>, List<String>> converter = new ListStringConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        List<Integer> input = Arrays.asList(1, 2, 3);
        List<String> expectedOutput = Arrays.asList("1", "2", "3");

        // When
        String output = serializeAndGetOutput(serializer, input);

        // Then
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    public void testDelegateSerializerWithNestedObjects() throws JsonGenerationException, JsonMappingException, IOException {
        // Given
        Converter<Person, PersonDto> converter = new PersonConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Person input = new Person("Alice", 25);
        PersonDto expectedOutput = new PersonDto("Alice", 25);

        // When
        String output = serializeAndGetOutput(serializer, input);

        // Then
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    public void testDelegateSerializerWithNullInput() throws JsonGenerationException, JsonMappingException, IOException {
        // Given
        Converter<Object, String> converter = new StringNullConverter();
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);
        Object input = null;

        // When & Then
        String output = serializeAndGetOutput(serializer, input);
        assertTrue(output.isEmpty());
    }

    private String serializeAndGetOutput(StdDelegatingSerializer<Object> serializer, Object input)
            throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.writeValue(writer, serializer.serializeValues(input, null, null));
        return writer.toString();
    }

    private static class SimpleStringConverter implements Converter<Integer, String> {
        @Override
        public String convert(Integer s) {
            return s.toString();
        }
    }

    private static class ListStringConverter implements Converter<List<Integer>, List<String>> {
        @Override
        public List<String> convert(List<Integer> s) {
            return new ArrayList<>(s.stream().map(String::toString).collect(Collectors.toList()));
        }
    }

    private static class PersonConverter implements Converter<Person, PersonDto> {
        @Override
        public PersonDto convert(Person s) {
            return new PersonDto(s.getName(), s.getAge());
        }
    }

    private static class Person {
        private final String name;
        private final int age;

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
    }

    private static class PersonDto {
        private final String name;
        private final int age;

        public PersonDto(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "PersonDto{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    private static class StringNullConverter implements Converter<Object, String> {
        @Override
        public String convert(Object o) {
            return o == null ? "" : o.toString();
        }
    }
}