import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameAndScope() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, JsonIdentityInfo.None.class, null);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertNull(objectIdInfo.getGeneratorType());
        Assert.assertNotNull(objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameStringAndScope() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, JsonIdentityInfo.None.class, null);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertNull(objectIdInfo.getGeneratorType());
        Assert.assertNotNull(objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameStringAndScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator.getClass(), null);
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator.getClass(), objectIdInfo.getGeneratorType());
        Assert.assertNotNull(objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithPropertyNameStringAndScopeAndGeneratorAndResolver() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator.getClass(), false, objectIdResolver.getClass());
        Assert.assertEquals(propertyName, objectIdInfo.getPropertyName());
        Assert.assertEquals(scope, objectIdInfo.getScope());
        Assert.assertEquals(generator.getClass(), objectIdInfo.getGeneratorType());
        Assert.assertEquals(objectIdResolver.getClass(), objectIdInfo.getResolverType());
        Assert.assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator.getClass(), false, objectIdResolver.getClass());
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        Assert.assertEquals(objectIdInfo, !objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
           }
        };
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName.getId(), scope, generator.getClass(), false, objectIdResolver.getClass());
        String expectedToString = "ObjectIdInfo: propName=propertyName, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.ObjectIdGenerator$Null, alwaysAsId=false";
        String actualToString = objectIdInfo.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructionWithNullPropertyName() {
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        new ObjectIdInfo(null, scope, generator.getClass(), false, objectIdResolver.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructionWithNullScope() {
        PropertyName propertyName = new PropertyName("propertyName");
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        new ObjectIdInfo(propertyName, null, generator.getClass(), false, objectIdResolver.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructionWithNullGenerator() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
        new ObjectIdInfo(propertyName, scope, null, false, objectIdResolver.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectIdInfoConstructionWithNullResolver() {
        PropertyName propertyName = new PropertyName("propertyName");
        Class<String> scope = String.class;
        ObjectIdGenerator<Object> generator = new ObjectIdGenerator<Object>() {
            @Override
            public Object generateId(SerializerProvider serializers, Object bean) {
                return null;
            }
        };
        new ObjectIdInfo(propertyName, scope, generator.getClass(), false, null);
    }
}