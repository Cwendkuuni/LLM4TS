import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.BeanPropertyWriterImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private ObjectNode node;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.introspect(PropertyBuilder.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testBuildWriter_Basic() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("classAnnotations");
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testBuildWriter_SerializationType() throws JsonMappingException, JsonProcessingException {
        Method method = PropertyBuilder.class.getDeclaredMethod("findSerializationType", Annotated.class, boolean.class, JavaType.class);
        method.setAccessible(true);
        JavaType serializationType = (JavaType) method.invoke(propertyBuilder, null, true, null);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, serializationType, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testBuildWriter_DefaultValue() throws JsonMappingException, JsonProcessingException {
        Object defaultBean = propertyBuilder.getDefaultBean();
        String name = "beanDesc";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = propertyBuilder.getClassAnnotations().getType(name);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, type, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(defaultBean);
        Assert.assertEquals(defaultValue, result.get(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_InvalidSerializationType() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("classAnnotations");
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(null);
        writer.close();
        propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriter_NullWriter() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("classAnnotations");
        propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullPropertyDefinition() throws JsonMappingException, JsonProcessingException {
        ObjectWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, true);
        writer.writeValueAsTree(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullBeanPropertyWriter() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("classAnnotations");
        propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullPropertyName() throws JsonMappingException, JsonProcessingException {
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition("classAnnotations");
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        writer.writeValueAsTree(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullDefaultBean() throws JsonMappingException, JsonProcessingException {
        String name = "beanDesc";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = propertyBuilder.getClassAnnotations().getType(name);
        propertyBuilder.buildWriter(null, null, type, null, null, null, null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullAccessOrValue() throws JsonMappingException, JsonProcessingException {
        String name = "beanDesc";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = propertyBuilder.getClassAnnotations().getType(name);
        ObjectNode defaultBean = ObjectMapper.getInstance().createObjectNode();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null,null, type, null, null, null, null, true);
        writer.writeValueAsTree(defaultBean);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_NullValueToSuppress() throws JsonMappingException, JsonProcessingException {
        String name = "beanDesc";
        AnnotatedMember member = new StubAnnotatedMember();
        JavaType type = propertyBuilder.getClassAnnotations().getType(name);
        ObjectNode defaultBean = ObjectMapper.getInstance().createObjectNode();
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, type, null, null, null, null, true);
        writer.assignNullSerializer(null);
        writer.writeValueAsTree(defaultBean);
    }

    @Test
    public void testBuildWriter_EmptyList() throws JsonMappingException, JsonProcessingException {
        String name = "classAnnotations";
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition(name);
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(null);
        Assert.assertEquals("[{}]", result.toString());
    }

    @Test
    public void testBuildWriter_EmptyMap() throws JsonMappingException, JsonProcessingException {
        String name = "classAnnotations";
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition(name);
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(ObjectMapper.getInstance().createObjectNode());
        Assert.assertEquals("{}", result.toString());
    }

    @Test
    public void testBuildWriter_NullValue() throws JsonMappingException, JsonProcessingException {
        String name = "classAnnotations";
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition(name);
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(null);
        Assert.assertEquals("null", result.toString());
    }

    @Test
    public void testBuildWriter_EmptyArray() throws JsonMappingException, JsonProcessingException {
        String name = "classAnnotations";
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition(name);
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree(ObjectMapper.getInstance().createArrayNode());
        Assert.assertEquals("[]", result.toString());
    }

    @Test
    public void testBuildWriter_EmptyString() throws JsonMappingException, JsonProcessingException {
        String name = "classAnnotations";
        BeanPropertyDefinition propDef = propertyBuilder.getClassAnnotations().findPropertyDefinition(name);
        ObjectWriter writer = propertyBuilder.buildWriter(null, propDef, null, null, null, null, null, true);
        ObjectNode result = writer.writeValueAsTree("");
        Assert.assertEquals("\"\"", result.toString());
    }
}