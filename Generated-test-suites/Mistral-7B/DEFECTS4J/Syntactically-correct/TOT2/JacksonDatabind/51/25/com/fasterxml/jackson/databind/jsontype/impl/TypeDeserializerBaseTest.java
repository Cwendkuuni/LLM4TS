import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.TestNameResolver;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.setSerializerFactory(com.fasterxml.jackson.databind.JsonSerializer.None.INSTANCE);
        mapper = new ObjectMapper(new JsonMapperBuilder(mapperConfig -> mapperConfig.addModule(module))
                .findAndRegisterModules());
        typeDeserializerBase = new TestTypeDeserializerBase();
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("name", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
                typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testPropertyName() throws Exception {
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        assertNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer_NotNullTypeId() throws Exception {
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestContext(), typeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestContext(), null);
        assertEquals(NullifyingDeserializer.instance.getClass(), deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(new TestContext()));
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext context = new TestContext();
        JavaType type = mapper.getTypeFactory().constructType(String[].class);
        String typeId = "com.example.MyClass";
        JavaType resolvedType = typeDeserializerBase._handleUnknownTypeId(context, typeId, null, type);
        assertEquals(String[].class, resolvedType.getRawClass());
    }

    @Test
    public void testDeserializeWithNativeTypeId_NotNullTypeId() throws Exception {
        String json = "{\"typeId\": \"com.example.MyClass\"}";
        ObjectNode node = mapper.readTree(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(new TestParser(node), new TestContext(), "com.example.MyClass");
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(new TestParser(null), new TestContext(), null);
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public DeserializationFeature[] features() {
            return new DeserializationFeature[0];
        }

        @Override
        public TestNameResolver getTestNameResolver() {
            return null;
        }

        @Override
        public ObjectFactory getValueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public JavaType findValueType(Class<?> rawType, JavaType contextType) {
            return null;
        }

        @Override
        public JavaType findValueType(Class<?> rawType, JavaType contextType, String typeId) {
            return null;
        }

        @Override
        public JavaType findTypeCompatibleWithValue(Object value, JavaType valueType) {
            return null;
        }

        @Override
        public JavaType findTypeFromId(JavaType type, String typeId) {
            return null;
        }

        @Override
        public void reportBindingAndDebuggingInfo(String msg) {
        }

        @Override
        public void reportBindingAndDebuggingInfo(String msg, Object data) {
        }

        @Override
        public void reportError(String msg) {
        }

        @Override
        public void reportError(String msg, Object data1, Object data2) {
        }

        @Override
        public void reportError(String msg, Throwable e) {
        }

        @Override
        public void reportError(String msg, Throwable e, Object data1, Object data2) {
        }

        @Override
        public boolean canOverrideAccess(Class<?> type, AnnotatedMember member) {
            return false;
        }

        @Override
        public Object findValueWithTypeId(Class<?> type, String typeId, JsonParser p) throws IOException {
            return null;
        }

        @Override
        public ObjectMapper getConfig() {
            return null;
        }

        @Override
        public ObjectMapper getRootValueMapper() {
            return null;
        }

        @Override
        public Object findReference(String s) throws IOException {
            return null;
        }

        @Override
        public void configure(DeserializationConfiguration cfg, DeserializationFeature feature) {
        }

        @Override
        public void configure(DeserializationConfiguration cfg, DeserializationFeature feature, boolean booleanValue) {
        }

        @Override
        public void configure(DeserializationConfiguration cfg, SerializationFeature feature) {
        }

        @Override
        public void configure(DeserializationConfiguration cfg, SerializationFeature feature, boolean booleanValue) {
        }
    }

    private static class TestParser extends BaseParser {
        public TestParser(JsonToken currentToken, JsonParser p, ObjectCodec ctxt) {
            super(currentToken, p, ctxt);
        }

        public TestParser(ObjectNode node) {
            super(node);
        }
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        public TestTypeDeserializerBase() {
            super(null, null, null, false, null);
        }
    }
}