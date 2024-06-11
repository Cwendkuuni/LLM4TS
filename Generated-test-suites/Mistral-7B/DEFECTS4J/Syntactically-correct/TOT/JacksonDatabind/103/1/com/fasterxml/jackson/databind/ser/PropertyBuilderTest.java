import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private ObjectMapper mapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws Exception {
        config = new SerializationConfig();
        mapper = new ObjectMapper(config);
        BeanFactory beanFactory = new AnnotationBeanFactory(config);
        BeanDescription beanDesc = beanFactory.getBeanDescription(PropertyBuilder.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuilderWithDefaultValues() throws Exception {
        // Test default inclusion and real property defaults
        JavaType declType = mapper.getTypeFactory().constructType(PropertyBuilder.class);
        ObjectNode result = mapper.writeValueAsTree(propertyBuilder, new TypeReference<ObjectNode>() {});
        ObjectNode expected = mapper.readTree("{\"useRealPropertyDefaults\":false,\"defaultInclusion\":{\"valueInclusion\":\"ALWAYS\"}}");
        assertEquals(expected, result);

        // Test using real property defaults
        config.setPropertyInclude(JsonProperty.Builder.standard("useRealPropertyDefaults").setValue(true));
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        result = mapper.writeValueAsTree(propertyBuilder, new TypeReference<ObjectNode>() {});
        expected = mapper.readTree("{\"useRealPropertyDefaults\":true,\"defaultInclusion\":{\"valueInclusion\":\"ALWAYS\"}}");
        assertEquals(expected, result);

        // Test excluding default property values with NON_DEFAULT
        BeanPropertyDefinition propDef = propertyBuilder._beanDesc.findPropertyDefinition("defaultBean");
        AnnotatedMember am = propDef.getGetter();
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, propDef.getType());
        Object defaultValue = BeanUtil.getDefaultValue(serializationType);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(
                new DefaultSerializerProvider(),
                propDef,
                serializationType,
                null,
                null,
                null,
                am,
                false
        );
        ObjectNode json = mapper.writeValueAsTree(writer, defaultValue);
        ObjectNode expectedJson = mapper.readTree("{\"value\":null}");
        assertEquals(expectedJson, json);

        // Test excluding default property values with NON_EMPTY
        JsonInclude.Include inclusion = JsonInclude.Include.NON_EMPTY;
        config.setPropertyInclude(JsonProperty.Builder.standard("defaultPropertyInclusion.\"PropertyBuilder.defaultBean\".").setSerialization(inclusion));
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        json = mapper.writeValueAsTree(writer, defaultValue);
        expectedJson = mapper.readTree("{}");
        assertNull(json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithInvalidSerializationType() throws Exception {
        BeanPropertyDefinition propDef = propertyBuilder._beanDesc.findPropertyDefinition("defaultBean");
        AnnotatedMember am = propDef.getGetter();
        JavaType declaredType = mapper.getTypeFactory().constructType(PropertyBuilder.class);
        ObjectNode result = mapper.writeValueAsTree(propertyBuilder, new TypeReference<ObjectNode>() {});
        ObjectNode expected = mapper.readTree("{\"useRealPropertyDefaults\":false,\"defaultInclusion\":{\"valueInclusion\":\"ALWAYS\"}}");
        assertEquals(expected, result);

        Class<? extends AnnotatedMember> amType = am.getClass();
        Object valueToSuppress = new Object(){};
        propertyBuilder.buildWriter(
                new DefaultSerializerProvider(),
                propDef,
                null,
                null,
                null,
                null,
                (AnnotatedMember) ClassUtil.constructInstance(amType, new Object[]{declaredType}),
                false
        );
    }
}