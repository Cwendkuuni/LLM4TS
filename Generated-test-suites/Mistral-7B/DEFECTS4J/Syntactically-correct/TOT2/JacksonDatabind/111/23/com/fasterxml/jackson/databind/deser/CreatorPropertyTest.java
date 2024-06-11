import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;
    private Object beanInstance;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Creator creator = mapper.getTypeFactory().constructCreator(Creator.class);
        AnnotatedMember member = new AnnotatedMember(null, null, new JsonProperty("name"), null);
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, -1, null, null);
        creatorProperty.setFallbackSetter(BeanUtil.findProperty(Creator.class, "property"));
        creatorProperty.setName(new PropertyName("name"));
        creatorProperty.setType(mapper.getTypeFactory().constructType(String.class));
        creatorProperty.setValueDeserializer(mapper.findValueDeserializer(String.class, null));
        creator.addPropertyCreator(new Creator.InstanceCreator() {
            @Override
            public Object createInstance(DeserializationContext ctxt) throws IOException {
                return beanInstance;
            }
        }, creatorProperty);
        beanInstance = new Object();
    }

    @Test
    public void testBasicFunctionality() throws Exception {
        String value = "test";
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"" + value + "\"}");
        creatorProperty.deserializeAndSet(parser, null, beanInstance);
        assertEquals(value, creatorProperty.get(beanInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws Exception {
        creatorProperty.setFallbackSetter(null);
        String value = "test";
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"" + value + "\"}");
        creatorProperty.deserializeAndSet(parser, null, beanInstance);
    }

    @Test
    public void testInjectValue() throws Exception {
        String injectValue = "injectedValue";
        creatorProperty.setInjectableValueId("injectableValueId");
        Object injectedValue = new Object() {
            @JsonInject
            public Object getInjectedValue() {
                return injectValue;
            }
        };

        JsonParser parser = mapper.getFactory().createParser("{}");
        creatorProperty.inject(parser, null, beanInstance);
        assertEquals(injectValue, creatorProperty.get(beanInstance));
    }

    @Test(expected = IOException.class)
    public void testNullValue() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("{}");
        creatorProperty.deserializeAndSet(parser, null, beanInstance);
    }

    @Test
    public void testIgnoreProperty() throws Exception {
        creatorProperty.markAsIgnorable();
        String value = "test";
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"" + value + "\"}");
        creatorProperty.deserializeAndSet(parser, null, beanInstance);
        assertNull(creatorProperty.get(beanInstance));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testInvalidDefinition() throws Exception {
        creatorProperty.setName(new PropertyName("name"));
        creatorProperty.setType(mapper.getTypeFactory().constructType(Integer.class));
        creatorProperty.setValueDeserializer(mapper.findValueDeserializer(String.class, null));
        creatorProperty.markAsIgnorable();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":1}");
    }

    
}