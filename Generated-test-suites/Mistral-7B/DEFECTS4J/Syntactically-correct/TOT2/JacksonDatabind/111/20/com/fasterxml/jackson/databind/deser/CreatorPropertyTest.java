import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreatorPropertyTest {

    @Test
    public void testCreatorPropertyConstruction() {
        PropertyName name = PropertyName.construct("testProperty", CreatorProperty.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName", String.class);
        TypeDeserializer typeDeserializer = new JsonDeserializer.None(String.class);
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = "testId";
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeserializer, contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals("testProperty", creatorProperty.getName().getShortName());
        assertEquals(String.class, creatorProperty.getType());
        assertNull(creatorProperty._fallbackSetter);
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() throws JsonProcessingException {
        PropertyName name = PropertyName.construct("testProperty", CreatorProperty.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName", String.class);
        TypeDeserializer typeDeserializer = new JsonDeserializer.None(String.class);
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = "testId";
        PropertyMetadata metadata = new PropertyMetadata();
        SettableBeanProperty fallbackSetter = new SimpleValueProperty("testFallback", String.class);

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeserializer, contextAnnotations, param, index, injectableValueId, metadata, fallbackSetter);

        assertEquals("testProperty", creatorProperty.getName().getShortName());
        assertEquals(String.class, creatorProperty.getType());
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PropertyName name = PropertyName.construct("testProperty", CreatorProperty.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName", String.class);
        TypeDeserializer typeDeserializer = new JsonDeserializer.None(String.class);
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = "testId";
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeserializer, contextAnnotations, param, index, injectableValueId, metadata);

        mapper.registerModule(new SimpleModule().addDeserializer(CreatorProperty.class, new CreatorPropertyDeserializer()));
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "testProperty");
        node.put("type", "java.lang.String");

        creatorProperty.deserializeAndSet(mapper.getFactory().createParser(node), null, null);
    }

    @Test
    public void testInject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PropertyName name = PropertyName.construct("testProperty", CreatorProperty.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = PropertyName.construct("wrapperName", String.class);
        TypeDeserializer typeDeserializer = new JsonDeserializer.None(String.class);
        Annotations contextAnnotations = new Annotations();
        AnnotatedParameter param = new AnnotatedParameter();
        int index = 0;
        Object injectableValueId = "testId";
        PropertyMetadata metadata = new PropertyMetadata();

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeserializer, contextAnnotations, param, index, injectableValueId, metadata);

        mapper.registerModule(new SimpleModule().addDeserializer(String.class, new StringDeserializer()));
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "testProperty");
        node.put("type", "java.lang.String");
        node.put("injectId", "testValue");

        Object value = mapper.readTree(node);
        Object beanInstance = new Object();

        creatorProperty.inject(mapper.getDeserializationContext(), beanInstance);

        assertEquals("testValue", creatorProperty.get(beanInstance));
    }
}