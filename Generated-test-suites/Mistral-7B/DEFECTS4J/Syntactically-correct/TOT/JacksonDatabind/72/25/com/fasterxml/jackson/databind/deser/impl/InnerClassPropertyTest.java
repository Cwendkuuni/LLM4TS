import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest extends TestCase {

    private static class TestClass {
        @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
        private InnerClass innerClass;

        public InnerClass getInnerClass() {
            return innerClass;
        }

        public void setInnerClass(InnerClass innerClass) {
            this.innerClass = innerClass;
        }

        public static class InnerClass {
            private int value;

            public InnerClass() {
            }

            public InnerClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }

    @org.junit.Test
    public void testInnerClassPropertyDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"innerClass\":{\"value\":42}}";
        TestClass testClass = objectMapper.readValue(json, TestClass.class);
        assertEquals(42, testClass.getInnerClass().getValue());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"innerClass\":{}}";
        TestClass testClass = objectMapper.readValue(json, TestClass.class);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBrokenDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"innerClass\":{ \"value\": \"not a number\"}}";
        TestClass testClass = objectMapper.readValue(json, TestClass.class);
    }

    static class InnerClassPropertyInnerClassDeserializer extends JsonDeserializer<InnerClass> {
        @Override
        public InnerClass deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            AnnotatedConstructor constructor = null;
            Constructor<?> innerClassConstructor = TestClass.InnerClass.class.getDeclaredConstructor();
            innerClassConstructor.setAccessible(true);
            try {
                constructor = new AnnotatedConstructor(null, innerClassConstructor, null, null);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Missing constructor for InnerClass", e);
            }
            return new InnerClassProperty(null, constructor).getValueDeserializer().deserialize(jp, ctxt, null);
        }
    }
}