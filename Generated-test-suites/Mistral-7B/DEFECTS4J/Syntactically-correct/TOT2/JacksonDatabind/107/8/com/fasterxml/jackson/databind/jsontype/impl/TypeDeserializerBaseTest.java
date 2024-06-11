import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;

import java.io.IOException;
import java.util.Map;

/**
 * Unit tests for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;
    private DeserializationContext context;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        context = jsonFactory.createContext();
        context.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        testee = new TypeDeserializerBase(null, null, null, true, null) {
            Map<String, JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();

            @Override
            protected void _findDeserializer(DeserializationContext ctxt, String typeId)
                    throws IOException {
                JsonDeserializer<Object> deser = deserializers.get(typeId);
                if (deser == null) {
                    deser = new StubDeserializer();
                    deserializers.put(typeId, deser);
                }
                super._findDeserializer(ctxt, typeId);
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer deserializer = testee.forProperty(property);
        assertEquals(TypeDeserializer.class, deserializer.getClass());
        verify(property).getName();
    }

    @Test
    public void testGetBaseTypeName() throws Exception {
        String baseTypeName = testee.baseTypeName();
        assertNotNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String propertyName = testee.getPropertyName();
        assertNotNull(propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver typeIdResolver = testee.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        Class<?> defaultImpl = testee.getDefaultImpl();
        assertNotNull(defaultImpl);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "TestType";
        JsonDeserializer<Object> deserializer = testee._findDeserializer(context, typeId);
        assertNotNull(deserializer);
        assertEquals(StubDeserializer.class, deserializer.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullContext() throws Exception {
        testee._findDeserializer(null, "TestType");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = testee._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
        assertEquals(NullifyingDeserializer.instance.getClass(), deserializer.getClass());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String typeId = "TestType";
        Object result = testee._deserializeWithNativeTypeId(mock(JsonParser.class), context, typeId);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullContext() throws Exception {
        testee._deserializeWithNativeTypeId(mock(JsonParser.class), null, null);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String typeId = "UnknownType";
        JavaType javaType = testee._handleUnknownTypeId(context, typeId);
        assertNotNull(javaType);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        String extraDesc = "Missing type id";
        JavaType javaType = testee._handleMissingTypeId(context, extraDesc);
        assertNotNull(javaType);
        assertTrue(javaType.isAbstract());
    }

    private static class StubDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return "StubDeserializedObject";
        }
    }
}