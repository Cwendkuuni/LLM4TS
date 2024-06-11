import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationTest;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link ValueInstantiator} class
 */
public class ValueInstantiatorTest extends DeserializationTest {

    @Test
    public void testDefaultInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(String.class);
        assertEquals("Default instantiator for String", instantiator.createUsingDefault(context), "");
    }

    @Test
    public void testCreateFromObjectWith() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyClassWithProperties.class);
        MyClassWithProperties obj = new MyClassWithProperties(1, "Test");
        ObjectMapper mapper2 = new ObjectMapper();
        ObjectWriter writer = mapper2.writerWithTypeValidator(null);
        String json = writer.writeValueAsString(obj);
        MyClassWithProperties deser = instantiator.createFromObjectWith(context, json.substring(1, json.length() - 1).split(","));
        assertEquals("Test", deser.property2);
        assertEquals(1, deser.property1);
    }

    @Test
    public void testCreateFromString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyStringClass.class);
        assertEquals("TestString", instantiator.createFromString(context, "TestString"));
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyBooleanClass.class);
        assertTrue(instantiator.createFromBoolean(context, true));
        assertFalse(instantiator.createFromBoolean(context, false));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidInstantiation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyClassWithInvalidInstantiation.class);
        instantiator.createFromObjectWith(context, new Object[]{"test1", "test2"});
    }

    @Test(expected = JsonMappingException.class)
    public void testStringCreatorNotFound() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ValueInstantiator instantiator = mapper.findValueInstantiator(MyClassWithoutStringCreator.class);
        instantiator.createFromString(context, "Test");
    }

    /**
     * Class used for testing
     */
    static class MyClassWithProperties {
        private int property1;
        private String property2;

        public MyClassWithProperties(int property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        public int getProperty1() {
            return property1;
        }

        public String getProperty2() {
            return property2;
        }
    }

    /**
     * Class used for testing
     */
    static class MyClassWithoutStringCreator {

    }

    /**
     * Class used for testing
     */
    static class MyClassWithInvalidInstantiation {
        public MyClassWithInvalidInstantiation(String property1, String property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        private String property1;
        private int property2;
    }

    /**
     * Class used for testing
     */
    static class MyStringClass {

    }

    /**
     * Class used for testing
     */
    static class MyBooleanClass {

    }
}