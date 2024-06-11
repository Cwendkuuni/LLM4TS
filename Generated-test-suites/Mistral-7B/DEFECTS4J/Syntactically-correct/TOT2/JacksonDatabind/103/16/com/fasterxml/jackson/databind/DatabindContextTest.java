import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
        ObjectMapper.DefaultTypingResolver.Finder finder = new ObjectMapper.DefaultTypingResolver.Finder() {
            @Override
            public JavaType findContentType(Type type, AnnotationIntrospector annotationIntrospector) {
                return defaultTypingResolver.findContentType(type, null, null);
            }

            @Override
            public JavaType findContainerType(Type type, AnnotationIntrospector annotationIntrospector) {
                return defaultTypingResolver.findContainerType(type, null, null);
            }
        };
        ObjectMapper.DefaultTypingResolver resolver = new ObjectMapper.DefaultTypingResolver(finder, null, null);

        MapperConfig<ObjectMapper> config = new MapperFactory().configure(resolver);
        this.objectMapper = new ObjectMapper(config);
    }

    @Test
    public void testGetConfig() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertNotNull(config);
        assertEquals(ObjectMapper.class, config.getClass());
    }

    @Test
    public void testIsEnabled() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertEquals(MapperFeature.DEFAULT_USE_STATIC_TYPES, context.isEnabled(MapperFeature.USE_STATIC_TYPES));
        assertEquals(MapperFeature.DEFAULT_USE_INT_PRIMITIVES, context.isEnabled(MapperFeature.USE_INT_PRIMITIVES));
    }

    @Test
    public void testGetActiveView() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertNull(context.getActiveView());
    }

    @Test
    public void testGetLocale() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertEquals(Locale.getDefault(), context.getLocale());
    }

    @Test
    public void testGetTimeZone() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertEquals(TimeZone.getDefault(), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertEquals(JsonFormat.Value.class, context.getDefaultPropertyFormat(Object.class).getClass());
    }

    @Test
    public void testGetAttribute() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        context.setAttribute("test", "hello");
        assertEquals("hello", context.getAttribute("test"));
    }

    @Test
    public void testConstructType() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        Type intType = Integer.TYPE;
        JavaType javaType = context.constructType(intType);
        assertEquals(intType, javaType.getRawType());
        assertEquals(Integer.class, javaType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType javaTypeInt = context.getTypeFactory().constructType(Integer.TYPE);
        JavaType javaTypeString = context.getTypeFactory().constructType(String.class);
        JavaType javaTypeStringInt = context.resolveSubType(javaTypeInt, "String");
        assertEquals(String.class, javaTypeStringInt.getRawClass());
        assertEquals(String.class, javaTypeStringInt.getRawType());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType javaTypeInt = context.getTypeFactory().constructType(Integer.TYPE);
        JavaType javaTypeString = context.getTypeFactory().constructType(String.class);
        context.invalidTypeIdException(javaTypeInt, "String", "Invalid type id");
    }

    @Test
    public void testObjectIdGeneratorInstance() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new AnnotatedClass(JsonAutoDetect.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, JsonAutoDetect.Id.NAME, ObjectIdGenerator.class);
        ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
        assertNotNull(objectIdGenerator);
    }

    @Test
    public void testObjectIdResolverInstance() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new AnnotatedClass(JsonAutoDetect.class);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, JsonAutoDetect.Id.CLASS, ObjectIdResolver.class);
        ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
        assertNotNull(objectIdResolver);
    }

    @Test
    public void testConverterInstance() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        Annotated annotated = new AnnotatedClass(JsonAutoDetect.class);
        Object converterDef = JsonAutoDetect.SerializationFeature.FAIL_ON_EMPTY_BEANS.getName();
        Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
        assertNotNull(converter);
    }

    @Test
    public void testReportBadDefinition() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        JavaType javaType = context.getTypeFactory().constructType(Integer.TYPE);
        String msg = "Invalid type: " + _desc(javaType.getClassName());
        Object badDefinition = context.reportBadDefinition(javaType, msg);
        assertNull(badDefinition);
    }

    @Test
    public void testFormat() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        String msg = "Error: %s";
        String extra = "java.lang.NumberFormatException: For input string: \"hello\"";
        String desc = "java.lang.NumberFormatException";
        String result = context._format(msg, extra);
        assertEquals("Error: java.lang.NumberFormatException: For input string: \"hello\"", result);
    }

    @Test
    public void testTruncate() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        String desc = "This is a very long description that should be truncated to fit the maximum error message length.";
        String result = context._truncate(desc);
        assertTrue(desc.length() > MAX_ERROR_STR_LEN);
        assertTrue(desc.startsWith(result) && desc.endsWith(result.substring(desc.length() - MAX_ERROR_STR_LEN)));
    }

    @Test
    public void testQuotedString() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        String desc = "This is a string with control characters and linefeeds.";
        String result = context._quotedString(desc);
        assertEquals("\"This is a string with control characters and linefeeds.\"", result);
    }

    @Test
    public void testColonConcat() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        String msgBase = "Error: ";
        String extra = "input \"hello\" is not a valid value for property \"name\"";
        String result = context._colonConcat(msgBase, extra);
        assertEquals("Error: input \"hello\" is not a valid value for property \"name\"", result);
    }

    @Test
    public void testDesc() throws Exception {
        DatabindContext context = objectMapper.getDeserializationContext();
        String desc = "This is a test description.";
        String result = context._desc(desc);
        assertEquals("[This is a test description]", result);
    }
}