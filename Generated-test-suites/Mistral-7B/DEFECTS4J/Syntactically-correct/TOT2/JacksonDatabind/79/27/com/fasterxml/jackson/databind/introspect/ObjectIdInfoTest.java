
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(resolver, objectIdInfo.getResolverType());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullPropertyName() {
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false, resolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullScope() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, generator, false, resolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullGenerator() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, null, false, resolver);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectIdInfoNullResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoEmptyPropertyName() {
        PropertyName propertyName = new PropertyName("");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoEmptyScopeClassName() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomScope> scope = null;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoEmptyGeneratorClassName() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = null;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoEmptyResolverClassName() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = null;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<MyCustomObjectIdGenerator> generator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> resolver = MyCustomObjectIdResolver.class;
        Class<MyCustomScope> scope = MyCustomScope.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo withAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertNotSame(objectIdInfo, withAlwaysAsId);
        assertTrue(withAlwaysAsId.getAlwaysAsId());
    }
}
