import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.std.StdCreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        valueInstantiator = new TestValueInstantiator(Object.class);
    }

    @Test
    public void testgetValueClass() throws Exception {
        assertEquals(Object.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testgetValueTypeDesc() throws Exception {
        assertEquals("java.lang.Object", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate() throws Exception {
        assertTrue(valueInstantiator.canInstantiate());
    }

    @Test
    public void testGetDelegateType() throws Exception {
        assertNull(valueInstantiator.getDelegateType(null));
    }

    @Test
    public void testCanCreateUsingDefault() throws Exception {
        assertTrue(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testCanCreateUsingDelegate() throws Exception {
        assertFalse(valueInstantiator.canCreateUsingDelegate());
    }

    @Test
    public void testGetFromObjectArguments() throws Exception {
        DeserializationConfig config = new DeserializationConfig(objectMapper.getConfig());
        SettableBeanProperty[] props = new SettableBeanProperty[]{new StdCreatorProperty("prop1"), new StdCreatorProperty("prop2")};
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        ValueInstantiator mapValueInstantiator = new MapValueInstantiator(javaType);
        assertEquals(Arrays.asList(props[0], props[1]), mapValueInstantiator.getFromObjectArguments(config));
    }

    @Test
    public void testCreateUsingDefault() throws Exception {
        Object result = valueInstantiator.createUsingDefault(null);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateFromObjectWith() throws Exception {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] args = new Object[]{arg1, arg2};
        Object result = valueInstantiator.createFromObjectWith(null, args);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateUsingDelegate() throws Exception {
        Object delegate = new Object();
        Object result = valueInstantiator.createUsingDelegate(null, delegate);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateUsingArrayDelegate() throws Exception {
        Object delegate = new Object[0];
        Object result = valueInstantiator.createUsingArrayDelegate(null, delegate);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCanCreateFromString() throws Exception {
        assertFalse(valueInstantiator.canCreateFromString());
    }

    @Test
    public void testCanCreateFromInt() throws Exception {
        assertFalse(valueInstantiator.canCreateFromInt());
    }

    @Test
    public void testCanCreateFromLong() throws Exception {
        assertFalse(valueInstantiator.canCreateFromLong());
    }

    @Test
    public void testCanCreateFromDouble() throws Exception {
        assertFalse(valueInstantiator.canCreateFromDouble());
    }

    @Test
    public void testCanCreateFromBoolean() throws Exception {
        assertFalse(valueInstantiator.canCreateFromBoolean());
    }

    @Test
    public void testCreateFromString() throws Exception {
        String value = "test";
        Object result = valueInstantiator.createFromString(null, value);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateFromInt() throws Exception {
        int value = 1;
        Object result = valueInstantiator.createFromInt(null, value);
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());
    }

    @Test
    public void testCreateFromLong() throws Exception {
        long value = 1L;
        Object result = valueInstantiator.createFromLong(null, value);
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
    }

    @Test
    public void testCreateFromDouble() throws Exception {
        double value = 1.0;
        Object result = valueInstantiator.createFromDouble(null, value);
        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
    }

    @Test
    public void testCreateFromBoolean() throws Exception {
        boolean value = true;
        Object result = valueInstantiator.createFromBoolean(null, value);
        assertNotNull(result);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testgetDefaultCreator() throws Exception {
        assertNull(valueInstantiator.getDefaultCreator());
    }

    @Test
    public void testGetDelegateCreator() throws Exception {
        assertNull(valueInstantiator.getDelegateCreator());
    }

    @Test
    public void testGetArrayDelegateCreator() throws Exception {
        assertNull(valueInstantiator.getArrayDelegateCreator());
    }

    @Test
    public void testGetWithArgsCreator() throws Exception {
        assertNull(valueInstantiator.getWithArgsCreator());
    }

    @Test
    public void testGetIncompleteParameter() throws Exception {
        assertNull(valueInstantiator.getIncompleteParameter());
    }

    static class TestValueInstantiator extends ValueInstantiator {
        TestValueInstantiator(Class<?> valueType) {
            super();
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return super.createUsingDefault(ctxt);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            return super.createFromObjectWith(ctxt, args);
        }

        @Override
        public Object createUsingDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return super.createUsingDelegate(ctxt, delegate);
        }

        @Override
        public Object createUsingArrayDelegate(DeserializationContext ctxt, Object delegate) throws IOException {
            return super.createUsingArrayDelegate(ctxt, delegate);
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return super.createFromString(ctxt, value);
        }

        @Override
        public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
            return super.createFromInt(ctxt, value);
        }

        @Override
        public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
            return super.createFromLong(ctxt, value);
        }

        @Override
        public Object createFromDouble(DeserializationContext ctxt, double value) throws IOException {
            return super.createFromDouble(ctxt, value);
        }

        @Override
        public Object createFromBoolean(DeserializationContext ctxt, boolean value) throws IOException {
            return super.createFromBoolean(ctxt, value);
        }

        @Override
        public AnnotatedWithParams getDefaultCreator() {
            Constructor<?> constructor = getClass().getDeclaredConstructor();
            return getAnnotatedWithParams(constructor);
        }

        @Override
        public AnnotatedWithParams getDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getArrayDelegateCreator() {
            return null;
        }

        @Override
        public AnnotatedWithParams getWithArgsCreator() {
            Method method = getClass().getMethod("createWithArgs", Object.class, Object.class);
            return getAnnotatedWithParams(method);
        }

        @Override
        public AnnotatedParameter getIncompleteParameter() {
            Method method = getClass().getMethod("createWithIncompleteArg", Integer.class);
            return getAnnotatedParameter(method, 0);
        }

        private static AnnotatedWithParams getAnnotatedWithParams(Constructor<?> constructor) {
            return new AnnotatedWithParams(constructor, new Annotation[0]);
        }

        private static AnnotatedWithParams getAnnotatedWithParams(Method method) {
            return new AnnotatedWithParams(method, new Annotation[]{new JsonCreator()});
        }

        private static AnnotatedParameter getAnnotatedParameter(Method method, int index) {
            return new AnnotatedParameter(method.getParameters()[index], new Annotation[]{new JsonCreator()});
        }

        @JsonCreator
        public static TestValueInstantiator create(Object arg1, Object arg2) {
            return new TestValueInstantiator();
        }

        @JsonCreator
        public static TestValueInstantiator createWithArgs(Object arg1, Object arg2) {
            return new TestValueInstantiator();
        }

        @JsonCreator
        public static TestValueInstantiator createWithIncompleteArg(int arg) {
            return new TestValueInstantiator();
        }
    }

    static class MapValueInstantiator extends StdValueInstantiator {
        MapValueInstantiator(JavaType valueType) {
            super(valueType);
        }

        @Override
        public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) throws IOException {
            BeanDeserializerBuilder builder = BeanDeserializerBuilder.vanillaBeanDeserializerBuilder(ctxt.getConfig(), valueType);
            PropertyName[] names = ((ValueType) valueType).properties().getNames();
            Set<CreatorProperty> properties = new HashSet<>();
            for (PropertyName name : names) {
                properties.add(builder.addProperty("prop" + name.getIndex(), PropertyValueBuffer.EMPTY_PROPERTY_ARRAY));
            }
            ObjectMapper mapper = objectMapper.copy();
            mapper.setDeserializationProblemHandler(new DeserializationProblemHandler() {
                @Override
                public void handleUnknownProperty(String name, Object value, DeserializationContext ctxt) throws IOException {
                    throw new IOException("Unexpected property '" + name + "'");
                }

                @Override
                public void handleMissingValue(DeserializationContext ctxt) throws IOException {
                    throw new IOException("Missing value");
                }

                @Override
                public void handleMissingProperty(DeserializationContext ctxt, String name) throws IOException {
                    throw new IOException("Missing property '" + name + "'");
                }

                @Override
                public void handleMissingCreator(DeserializationContext ctxt, JavaType type) throws IOException {
                    throw new IOException("Missing creator for type '" + type.getRawClass().getName() + "'");
                }

                @Override
                public void handleNote(String note, DeserializationContext ctxt) throws IOException {
                    throw new IOException("Deserialization note: " + note);
                }
            });
            BeanDeserializer deserializer = builder.build();
            PropertyValueBuffer buffer = deserializer.getParameterValueBuffer();
            SettableBeanProperty[] propertiesToUse = properties.toArray(new SettableBeanProperty[0]);
            return deserializer.deserialize(mapper.readValue(ctxt.getTextFactory().getTextRenderer().getText(ctxt.getParser().getText()), valueType), buffer, propertiesToUse);
        }
    }
}