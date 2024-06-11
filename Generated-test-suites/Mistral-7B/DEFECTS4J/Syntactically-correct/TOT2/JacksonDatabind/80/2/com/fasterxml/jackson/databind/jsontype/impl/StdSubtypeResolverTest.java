import com.fasterxml.jackson.databind.MapperFactoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        MapperFactoryMapper mapperFactory = new MapperFactoryMapper();
        mapperFactory.configure(MapperConfig.StandardConfig.builder()
                .addSubtypeResolver(new StdSubtypeResolver())
                .build());
        this.objectMapper = mapperFactory.getObjectMapper();
        this.subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testRegisterSubtypesByClass() {
        Class<Parent> parentClass = Parent.class;
        Class<Child1> child1Class = Child1.class;
        Class<Child2> child2Class = Child2.class;

        // Test registering subtypes using class
        subtypeResolver.registerSubtypes(parentClass, child1Class, child2Class);

        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);

        assertEquals(2, subtypes.size());
        assertSubtype(subtypes.get(0), Child1.class);
        assertSubtype(subtypes.get(1), Child2.class);
    }

    @Test
    public void testRegisterSubtypesByName() {
        Class<Parent> parentClass = Parent.class;
        Class<Child1> child1Class = Child1.class;
        Class<Child2> child2Class = Child2.class;

        // Test registering subtypes using class names
        subtypeResolver.registerSubtypes(parentClass, new String[]{"Child1", "Child2"});

        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);

        assertEquals(2, subtypes.size());
        assertSubtype(subtypes.get(0), Child1.class);
        assertSubtype(subtypes.get(1), Child2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesWithDuplicateName() {
        Class<Parent> parentClass = Parent.class;
        Class<Child1> child1Class = Child1.class;
        Class<Child2> child2Class = Child2.class;

        // Test registering subtypes with duplicate name
        subtypeResolver.registerSubtypes(parentClass, child1Class, child1Class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSubtypesWithNullClass() {
        Class<Parent> parentClass = Parent.class;

        // Test registering subtypes with null class
        subtypeResolver.registerSubtypes(parentClass, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByClassWithNullMapperConfig() {
        JavaType javaType = null;

        try {
            subtypeResolver.collectAndResolveSubtypesByClass(null, null, javaType);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByClassWithNullProperty() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);

        try {
            subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
        JavaType javaType = null;

        try {
            subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithEmptyList() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithBaseTypeNotRegistered() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByClassWithSubtypesRegistered() {
        Class<Parent> parentClass = Parent.class;
        Class<Child1> child1Class = Child1.class;
        Class<Child2> child2Class = Child2.class;

        subtypeResolver.registerSubtypes(parentClass, child1Class, child2Class);

        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, javaType);

        assertEquals(2, subtypes.size());
        assertSubtype(subtypes.get(0), Child1.class);
        assertSubtype(subtypes.get(1), Child2.class);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithEmptyList() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, javaType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithBaseTypeNotRegistered() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);

        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, javaType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithSubtypesRegistered() {
        Class<Parent> parentClass = Parent.class;
        Class<Child1> child1Class = Child1.class;
        Class<Child2> child2Class = Child2.class;

        subtypeResolver.registerSubtypes(parentClass, child1Class, child2Class);

        JavaType javaType = objectMapper.getTypeFactory().constructType(Parent.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, javaType);

        assertEquals(2, subtypes.size());
        assertSubtype(subtypes.get(0), Child1.class);
        assertSubtype(subtypes.get(1), Child2.class);
    }

    private void assertSubtype(NamedType namedType, Class<?> expectedType) {
        assertEquals(expectedType.getName(), namedType.getType().getName());
        assertEquals(expectedType.getName(), namedType.getName());
    }
}