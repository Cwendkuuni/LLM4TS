import com.google.gson.Gson;
import com.google.gson.JsonAdapter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws IOException, IllegalAccessException {
        // Given
        Class<?> testClass = TestClass.class;
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        when(constructorConstructor.get(TypeToken.get(TestClass.class)))
                .thenReturn(new TestClassConstructor(TestClass.class));
        FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
        Excluder excluder = mock(Excluder.class);
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                constructorConstructor, fieldNamingPolicy, excluder);

        // When
        TypeAdapter<TestClass> typeAdapter = reflectiveTypeAdapterFactory.create(new Gson(),
                TypeToken.get(TestClass.class));

        // Then
        Assert.assertNotNull(typeAdapter);

        // Test serialization
        TestClass testInstance = new TestClass("testValue1", "testValue2");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("testField1", "testValue1");
        jsonObject.addProperty("testField2", "testValue2");
        typeAdapter.write(new JsonWriter(System.out), testInstance);

        // Test deserialization
        JsonElement jsonElement = new JsonObject()
                .add("testField1", new JsonPrimitive("testValue1"))
                .add("testField2", new JsonPrimitive("testValue2"));
        TestClass deserializedTestInstance = typeAdapter.read(new JsonReader(new StringReader(jsonElement.toString())))
                .getValue();

        Assert.assertEquals(testInstance, deserializedTestInstance);
    }

    private static class TestClass {
        @SerializedName("testField1")
        private String testField1;
        @SerializedName("testField2")
        private String testField2;

        TestClass(String testField1, String testField2) {
            this.testField1 = testField1;
            this.testField2 = testField2;
        }

        public String getTestField1() {
            return testField1;
        }

        public void setTestField1(String testField1) {
            this.testField1 = testField1;
        }

        public String getTestField2() {
            return testField2;
        }

        public void setTestField2(String testField2) {
            this.testField2 = testField2;
        }
    }

    private static class TestClassConstructor implements ObjectConstructor<TestClass> {
        private final Class<TestClass> clazz;

        TestClassConstructor(Class<TestClass> clazz) {
            this.clazz = clazz;
        }

        @Override
        public TestClass construct() {
            try {
                TestClass testClass = clazz.newInstance();
                return testClass;
            } catch (IllegalAccessException | InstantiationException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public Type getType() {
            return clazz;
        }
    }

    private static final class FieldNamingPolicy implements FieldNamingStrategy {
        static final String LOWER_CASE_WITH_UNDERSCORES = "LOWER_CASE_WITH_UNDERSCORES";

        @Override
        public String translateName(Field field) {
            String name = field.getName();
            int length = name.length();
            if (Modifier.isStatic(field.getModifiers())) {
                name = name.toUpperCase();
            } else {
                StringBuilder sb = new StringBuilder(length);
                for (int i = 0; i < length; ++i) {
                    char c = name.charAt(i);
                    if (Character.isUpperCase(c)) {
                        sb.append("_").append(Character.toLowerCase(c));
                    } else {
                        sb.append(c);
                    }
                }
                name = sb.toString();
            }
            return name;
        }
    }
}

class StringReader {
    String data;

    StringReader(String data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        if (data == null || data.length() == 0) {
            return -1;
        }
        char c = data.charAt(0);
        data = data.substring(1);
        return c;
    }
}