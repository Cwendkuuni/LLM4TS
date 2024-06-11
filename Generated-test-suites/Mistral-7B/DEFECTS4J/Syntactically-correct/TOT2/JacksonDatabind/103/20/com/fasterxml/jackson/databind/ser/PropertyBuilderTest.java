import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilderWithDefaultValues() throws JsonMappingException {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = BeanDescription.construct(MyClass.class, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, SerializedClass.class, null, null, null, null, false);
        Object value = new Object();
        writer.writeValueAsString(node, value);
        String jsonString = node.getValueAsString();
        assertTrue(jsonString.isEmpty() || !jsonString.contains("defaultValue")); // check if default value is not included in serialization

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(defaultBean.getClass().getSimpleName(), "MyClass");
    }

    @Test
    public void testPropertyBuilderWithCustomSerializationType() throws JsonMappingException {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = BeanDescription.construct(MyClassWithCustomSerializationType.class, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, SerializedClass.class, null, null, new TypeSerializer() {
            // implementation of custom type serializer
        }, null, false);
        Object value = new Object();
        writer.writeValueAsString(node, value);
        String jsonString = node.getValueAsString();
        assertEquals("customSerializedValue", jsonString); // check if custom serialized value is used
    }

    @Test(expected = JsonMappingException.class)
    public void testPropertyBuilderWithInvalidType() throws JsonMappingException {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = BeanDescription.construct(MyClassWithInvalidType.class, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.buildWriter(null, null, SerializedClass.class, null, null, null, null, false);
    }

    @Test
    public void testPropertyBuilderWithNullValues() throws JsonMappingException {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = BeanDescription.construct(MyClassWithNullValues.class, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, SerializedClass.class, null, null, null, null, false);
        Object value = null;
        writer.writeValueAsString(node, value);
        String jsonString = node.getValueAsString();
        assertTrue(jsonString.isEmpty() || jsonString.equals("null")); // check if null value is correctly handled
    }

    @Test
    public void testPropertyBuilderWithEmptyCollections() throws JsonMappingException {
        ObjectNode node = MapperFactory.getMapper().createObjectNode();
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = BeanDescription.construct(MyClassWithEmptyCollections.class, config);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, SerializedClass.class, null, null, null, null, false);
        List<String> list = new ArrayList<>();
        writer.writeValueAsString(node, list);
        String jsonString = node.getValueAsString();
        assertTrue(jsonString.isEmpty()); // check if empty collections are not serialized
    }
}