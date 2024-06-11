import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import junit.framework.TestCase;

public class StdTypeResolverBuilderTest {

    @SuppressWarnings("unchecked")
    private static <T> TypeReference<T> typeRef(Class<T> clazz) {
        return new TypeReference<T>() {};
    }

    @SuppressWarnings("rawtypes")
    private static JavaType javaType(Class clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    @SuppressWarnings("rawtypes")
    private static NamedType namedType(Class clazz, String name) {
        return new StandardNamedType(clazz, name);
    }

    @SuppressWarnings("rawtypes")
    private static Collection<NamedType> namedTypes(Class a, Class b) {
        return java.util.Arrays.asList(namedType(a, null), namedType(b, null));
    }

    @BeforeClass
    public static void setup() throws Exception {
        MapperFactory mapperFactory = new MapperFactory();
        mapper = mapperFactory.getMapper();
    }

    private static ObjectMapper mapper;

    @Test
    public void testDefaultConstruction() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder.getIdType());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, mock(TypeIdResolver.class));
    }

    @Test
    public void testBuildTypeSerializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = javaType(String.class);
        Collection<NamedType> subtypes = namedTypes(Integer.class, Double.class);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.PROPERTY;
        serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), baseType, subtypes);
        assertNull(serializer);

        idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        assertEquals(AsArrayTypeSerializer.class, serializer.getClass());

        idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        assertEquals(AsWrapperTypeSerializer.class, serializer.getClass());

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        assertEquals(AsExternalTypeSerializer.class, serializer.getClass());

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        assertNotSame(AsExternalTypeSerializer.class, serializer.getClass());
    }

    @Test
    public void testBuildTypeDeserializer() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        JavaType baseType = javaType(String.class);
        Collection<NamedType> subtypes = namedTypes(Integer.class, Double.class);

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNull(deserializer);

        idType = JsonTypeInfo.Id.PROPERTY;
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);

        idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AsArrayTypeDeserializer);

        idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AsWrapperTypeDeserializer);

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);

        idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        idRes = mock(TypeIdResolver.class);
        when(idRes.getId(any(Object.class), anyInt(), anyInt())).thenReturn("externalId");
        builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AsExternalTypeDeserializer);
        assertEquals("externalId", deserializer.getTypeId());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("test");
        assertEquals("test", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertEquals(Void.class, builder.getDefaultImpl());
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertFalse(builder.isTypeIdVisible());
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testInclusionWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
    }

    @Test
    public void testInclusionProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }

    @Test
    public void testInclusionWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());
    }

    @Test
    public void testInclusionExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder.getIncludeAs());
    }

    @Test(expected = IllegalStateException.class)
    public void testInclusionUnknown() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.UNKNOWN);
    }

    @Test
    public void testIdTypeClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
    }

    @Test
    public void testIdTypeMinimalClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, builder.getIdType());
    }

    @Test
    public void testIdTypeName() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NAME, null);
        assertEquals(JsonTypeInfo.Id.NAME, builder.getIdType());
    }

    @Test
    public void testIdTypeNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
    }

    @Test
    public void testIdTypeCustom() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        assertEquals(JsonTypeInfo.Id.CUSTOM, builder.getIdType());
    }

    @Test
    public void testCustomIdResolver() {
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        when(idRes.getId(any(Object.class), anyInt(), anyInt())).thenReturn("customId");

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.PROPERTY, idRes);
        assertEquals(idRes, builder.getCustomIdResolver());
        assertEquals("customId", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImplVoid() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Void.class);
        assertEquals(Void.class, builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplNoClass() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(NoClass.class);
        assertEquals(NoClass.class, builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplString() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @JsonDeserialize(using = MyDeserializer.class)
    static class MyClass {}

    static class MyDeserializer extends StdDeserializer<MyClass> {}

    @Test
    public void testBuildTypeDeserializerMyClass() throws Exception {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        when(idRes.getId(any(Object.class), anyInt(), anyInt())).thenReturn("myId");

        JavaType baseType = typeRef(MyClass.class);
        Collection<NamedType> subtypes = java.util.Collections.emptyList();

        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), baseType, subtypes);
        assertTrue(deserializer instanceof AsPropertyTypeDeserializer);
        assertEquals("myId", deserializer.getTypeId());
        assertEquals(MyDeserializer.class, deserializer.getClass());
    }
}