import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructorWithAllArgs() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoNullPropertyName() {
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false);
        assertNull(objectIdInfo);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullPropertyName2() {
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoInvalidGeneratorType() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<InvalidObjectIdGenerator> generator = InvalidObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false);
    }

    // Add more test cases for edge cases and exception handling scenarios

}