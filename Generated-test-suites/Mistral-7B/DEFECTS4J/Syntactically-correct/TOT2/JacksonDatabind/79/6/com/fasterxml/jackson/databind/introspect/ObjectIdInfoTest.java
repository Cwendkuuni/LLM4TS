import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        PropertyName name = new PropertyName("testProperty");
        Class<ObjectIdGenerator<Object>> generator = ObjectIdGenerator.Id.class;
        Class<ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        Class<Object> scope = Object.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, resolver);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }
}