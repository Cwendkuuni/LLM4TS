import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdInfo> expectedClass = ObjectIdInfo.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, null, null, null);
        assertEquals(expectedClass, objectIdInfo.getClass());
        assertNotNull(objectIdInfo.getPropertyName());
        assertNotNull(objectIdInfo.getScope());
        assertNotNull(objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scopeClass = String.class;
        Class<MyCustomIdGenerator> generatorClass = MyCustomIdGenerator.class;
        Class<SimpleObjectIdResolver> resolverClass = SimpleObjectIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scopeClass, generatorClass);
        assertEquals(ObjectIdInfo.class, objectIdInfo.getClass());
        assertNotNull(objectIdInfo.getPropertyName());
        assertEquals(scopeClass, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(resolverClass, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        String propertyName = "id";
        Class<String> scopeClass = String.class;
        Class<MyCustomIdGenerator> generatorClass = MyCustomIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scopeClass, generatorClass);
        assertEquals(ObjectIdInfo.class, objectIdInfo.getClass());
        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scopeClass, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertNotNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor4() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scopeClass = String.class;
        Class<MyCustomIdGenerator> generatorClass = MyCustomIdGenerator.class;
        Class<MyCustomIdResolver> resolverClass = MyCustomIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scopeClass, generatorClass, resolverClass);
        assertEquals(ObjectIdInfo.class, objectIdInfo.getClass());
        assertNotNull(objectIdInfo.getPropertyName());
        assertEquals(scopeClass, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(resolverClass, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdConstructor() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scopeClass = String.class;
        Class<MyCustomIdGenerator> generatorClass = MyCustomIdGenerator.class;
        Class<MyCustomIdResolver> resolverClass = MyCustomIdResolver.class;
        boolean alwaysAsId = true;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scopeClass, generatorClass, alwaysAsId, resolverClass);
        assertEquals(ObjectIdInfo.class, objectIdInfo.getClass());
        assertNotNull(objectIdInfo.getPropertyName());
        assertEquals(scopeClass, objectIdInfo.getScope());
        assertEquals(generatorClass, objectIdInfo.getGeneratorType());
        assertEquals(resolverClass, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdMethod() {
        PropertyName propertyName = new PropertyName("id");
        Class<String> scopeClass = String.class;
        Class<MyCustomIdGenerator> generatorClass = MyCustomIdGenerator.class;
        Class<MyCustomIdResolver> resolverClass = MyCustomIdResolver.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scopeClass, generatorClass);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertEquals(ObjectIdInfo.class, newObjectIdInfo.getClass());
        assertEquals(objectIdInfo.getPropertyName(), newObjectIdInfo.getPropertyName());
        assertEquals(objectIdInfo.getScope(), newObjectIdInfo.getScope());
        assertEquals(objectIdInfo.getGeneratorType(), newObjectIdInfo.getGeneratorType());
        assertEquals(objectIdInfo.getResolverType(), newObjectIdInfo.getResolverType());
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }
}