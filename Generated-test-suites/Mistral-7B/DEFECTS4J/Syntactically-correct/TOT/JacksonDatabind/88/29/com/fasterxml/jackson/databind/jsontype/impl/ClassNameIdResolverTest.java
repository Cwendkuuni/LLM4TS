package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassNameIdResolver Test Cases")
public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;

    @BeforeAll
    public static void initialize() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new SimpleModule()
                .addValueProcessor(new JacksonAnnotationIntrospector().findValueProcessor(JsonTypeInfo.Id.CLASS))
                .addValueProcessor(new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory())));
    }

    @BeforeEach
    public void setUp() {
        typeFactory = objectMapper.getTypeFactory();
    }

    @DisplayName("Test idFromValue method with basic classes")
    @Test
    public void testIdFromValueBasicClasses() throws IOException {
        String id = "java.lang.String";
        Object value = "Hello World";
        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValue method with complex classes")
    @Test
    public void testIdFromValueComplexClasses() throws IOException {
        String id = "com.example.complex.Person";
        Person value = new Person("John Doe", 30);

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(id, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(id, actualId);
    }

    @DisplayName("Test idFromValueAndType method with basic classes")
    @Test
    public void testIdFromValueAndTypeBasicClasses() throws IOException {
        String id = "java.lang.String";
        Object value = "Hello World";
        String valueType = String.class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, String.class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValueAndType method with complex classes")
    @Test
    public void testIdFromValueAndTypeComplexClasses() throws IOException {
        String id = "com.example.complex.Person";
        Person value = new Person("John Doe", 30);
        String valueType = Person.class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, Person.class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test typeFromId method with basic classes")
    @Test
    public void testTypeFromIdBasicClasses() throws IOException {
        String id = "java.lang.String";
        JavaType expectedType = typeFactory.constructType(String.class);

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test typeFromId method with complex classes")
    @Test
    public void testTypeFromIdComplexClasses() throws IOException {
        String id = "com.example.complex.Person";
        JavaType expectedType = typeFactory.constructType(Person.class);

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test idFromValue method with Enum classes")
    @Test
    public void testIdFromValueEnumClasses() throws IOException {
        String id = "java.lang.Enum.String";
        Enum value = Enum.valueOf(String.class, "VALUE");
        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValueAndType method with Enum classes")
    @Test
    public void testIdFromValueAndTypeEnumClasses() throws IOException {
        String id = "java.lang.Enum.String";
        Enum value = Enum.valueOf(String.class, "VALUE");
        String valueType = String.class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, String.class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test typeFromId method with Enum classes")
    @Test
    public void testTypeFromIdEnumClasses() throws IOException {
        String id = "java.lang.Enum.String";
        JavaType expectedType = typeFactory.constructType(String.class.getEnumConstants()[0].getClass());

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test idFromValue method with custom collections")
    @Test
    public void testIdFromValueCustomCollections() throws IOException {
        String id = "java.util.EnumMap<java.lang.Enum, java.lang.String>";
        EnumMap<Enum, String> value = new EnumMap<>(Enum.class, new HashMap<>());
        value.put(Enum.VAL1, "VAL1");

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValueAndType method with custom collections")
    @Test
    public void testIdFromValueAndTypeCustomCollections() throws IOException {
        String id = "java.util.EnumMap<java.lang.Enum, java.lang.String>";
        EnumMap<Enum, String> value = new EnumMap<>(Enum.class, new HashMap<>());
        value.put(Enum.VAL1, "VAL1");
        String valueType = Map.class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, Map.class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test typeFromId method with custom collections")
    @Test
    public void testTypeFromIdCustomCollections() throws IOException {
        String id = "java.util.EnumMap<java.lang.Enum, java.lang.String>";
        JavaType expectedType = typeFactory.constructMapType(EnumMap.class, Enum.class, String.class);

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test idFromValue method with custom arrays")
    @Test
    public void testIdFromValueCustomArrays() throws IOException {
        String id = "java.lang.Object[]";
        Object[] value = new Object[]{1, 2, 3};

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValueAndType method with custom arrays")
    @Test
    public void testIdFromValueAndTypeCustomArrays() throws IOException {
        String id = "java.lang.Object[]";
        Object[] value = new Object[]{1, 2, 3};
        String valueType = Object[].class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, Object[].class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test typeFromId method with custom arrays")
    @Test
    public void testTypeFromIdCustomArrays() throws IOException {
        String id = "java.lang.Object[]";
        JavaType expectedType = typeFactory.constructArrayType(Object.class, 3);

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test idFromValue method with interfaces and generic classes")
    @Test
    public void testIdFromValueInterfacesAndGenericClasses() throws IOException {
        String id = "com.example.MyInterface<com.example.MyClass>";
        MyClass value = new MyClass();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(value), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValue(value);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test idFromValueAndType method with interfaces and generic classes")
    @Test
    public void testIdFromValueAndTypeInterfacesAndGenericClasses() throws IOException {
        String id = "com.example.MyInterface<com.example.MyClass>";
        MyClass value = new MyClass();
        String valueType = MyClass.class.getName();

        String expectedId = id;

        JsonTypeInfo jsonTypeInfo = objectMapper.readValue(new ObjectMapper().writeValueAsString(new ValueWrapper(value, valueType)), JsonTypeInfo.class);
        assertEquals(expectedId, jsonTypeInfo.getId());

        String actualId = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).idFromValueAndType(value, MyClass.class);
        assertEquals(expectedId, actualId);
    }

    @DisplayName("Test typeFromId method with interfaces and generic classes")
    @Test
    public void testTypeFromIdInterfacesAndGenericClasses() throws IOException {
        String id = "com.example.MyInterface<com.example.MyClass>";
        JavaType expectedType = typeFactory.constructType(MyInterface.class, MyClass.class);

        JavaType actualType = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory).typeFromId(null, id);
        assertEquals(expectedType, actualType);
    }

    @DisplayName("Test getDescForKnownTypeIds method")
    @Test
    public void testGetDescForKnownTypeIds() {
        String desc = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory()).getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }

    private static class ValueWrapper {
        private final Object value;
        private final String valueType;

        public ValueWrapper(Object value, String valueType) {
            this.value = value;
            this.valueType = valueType;
        }

        public Object getValue() {
            return value;
        }

        public String getValueType() {
            return valueType;
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
}