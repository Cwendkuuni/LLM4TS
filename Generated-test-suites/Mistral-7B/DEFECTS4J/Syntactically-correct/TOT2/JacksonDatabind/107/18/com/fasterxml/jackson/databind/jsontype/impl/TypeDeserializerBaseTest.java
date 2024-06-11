import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        typeDeserializerBase = new TypeDeserializerBase(
                new SimpleType(String.class),
                mock(TypeIdResolver.class),
                "typePropertyName",
                false,
                null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
    }

    @Test
    public void testBasicFunctionality() throws Exception {
        String json = "\"typePropertyName\":\"expectedValue\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        assertEquals("expectedValue", result);
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws Exception {
        String json = "\"notTypeIdPropertyName\":\"value\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
    }

    @Test
    public void testUnknownTypeId() throws Exception {
        String json = "\"unknownTypeIdPropertyName\":\"value\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        when(context.handleMissingTypeId(_baseType, anyString())).thenReturn(new SimpleType(Integer.class));

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        assertEquals(Integer.class, result.getClass());

        verify(context).handleMissingTypeId(_baseType, "unknownTypeIdPropertyName");
    }
}