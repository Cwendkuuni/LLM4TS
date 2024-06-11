import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SetterlessPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new JsonMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializationOfEmptyMap() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(Map.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(Map.class.getMethod("newInstance"), new Class[0]));

        String json = "{}";
        Object result = objectMapper.readValue(JsonFactory.instance.parse(json), javaType.getType());

        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testDeserializationOfMapWithKeyValue() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(Map.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(Map.class.getMethod("newInstance"), new Class[0]));

        String json = "{\"key\":1}";
        Object result = objectMapper.readValue(JsonFactory.instance.parse(json), javaType.getType());

        assertEquals(new HashMap() {{ put("key", 1); }}, result);
    }

    @Test
    public void testDeserializationOfMapWithNullValue() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(Map.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(Map.class.getMethod("newInstance"), new Class[0]));

        String json = "{\"key\":null}";
        Object result = objectMapper.readValue(JsonFactory.instance.parse(json), javaType.getType());

        assertNull(result.getClass().getComponentType().getDeclaredField("value").get(result));
        assertEquals(new HashMap() {{ put("key", null); }}, result);
    }

    @Test
    public void testDeserializationOfEmptyList() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(List.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(List.class.getMethod("newInstance"), new Class[0]));

        String json = "[]";
        Object result = objectMapper.readValue(JsonFactory.instance.parse(json), javaType.getType());

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testDeserializationOfListWithElement() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(List.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(List.class.getMethod("newInstance"), new Class[0]));

        String json = "[1]";
        Object result = objectMapper.readValue(JsonFactory.instance.parse(json), javaType.getType());

        assertEquals(new ArrayList() {{ add(1); }}, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSettingValueThroughSetter() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(List.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(List.class.getMethod("newInstance"), new Class[0]));

        Object instance = objectMapper.readValue(JsonFactory.instance.parse("[]"), javaType.getType());
        setterlessProperty.set(instance, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSettingValueThroughSetAndReturn() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class, Integer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(new AnnotatedMember(new AnnotatedMethod(List.class.getMethod("newInstance")), null), javaType),
                javaType, null, null, new AnnotatedMethod(List.class.getMethod("newInstance"), new Class[0]));

        Object instance = objectMapper.readValue(JsonFactory.instance.parse("[]"), javaType.getType());
        setterlessProperty.setAndReturn(instance, 1);
    }
}