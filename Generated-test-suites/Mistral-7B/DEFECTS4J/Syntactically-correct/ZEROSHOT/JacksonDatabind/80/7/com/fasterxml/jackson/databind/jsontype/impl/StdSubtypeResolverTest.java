package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StandardTypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private AnnotationIntrospector ai;
    private MapperConfig<?> config;
    private ObjectMapper mapper;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        config = mapper.getConfig();
        config.disable(MapperFeature.AUTO_DETECT_TYPES);
        config.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public Object findAnnotation(Class<?> cls) {
                return null;
            }

            @Override
            public Object findAnnotation(Class<?> cls, String qualifierName) {
                if (JsonTypeInfo.class.getName().equals(cls.getName())) {
                    return new JsonTypeInfo(new JsonTypeInfo.Id(JsonTypeInfo.Id.CLASS));
                }
                if (JsonSubTypes.class.getName().equals(cls.getName())) {
                    return new JsonSubTypes();
                }
                return null;
            }
        });
        subtypeResolver = new StdSubtypeResolver();
        ai = config.getAnnotationIntrospector();
    }

    @Test
    public void testRegisterSubtypes() throws Exception {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        subtypeResolver.registerSubtypes(new NamedType(subtype1), new NamedType(subtype2));

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));

        subtypeResolver.registerSubtypes(subtype1, subtype2);

        result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));
        assertTrue(result.contains(new NamedType(baseType)));
    }

    @Test
    public void testRegisterSubtypesArray() throws Exception {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        subtypeResolver.registerSubtypes(new NamedType[] { new NamedType(subtype1), new NamedType(subtype2) });

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(2, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));

        subtypeResolver.registerSubtypes(new Class<?>[] { subtype1, subtype2 });

        result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));
        assertTrue(result.contains(new NamedType(baseType)));
    }

    @Test
    public void testRegisterSubtypesMap() throws Exception {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        subtypeResolver.registerSubtypes(new LinkedHashMap<Class<?>, Class<?>>() {{
            put(subtype1, subtype2);
        }});

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));
        assertTrue(result.contains(new NamedType(baseType)));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        subtypeResolver.registerSubtypes(new NamedType[] { new NamedType(subtype1), new NamedType(subtype2) });

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));
        assertTrue(result.contains(new NamedType(baseType)));

        result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, subtype1);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));

        result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, subtype2);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(subtype2)));

        result = subtypeResolver.collectAndResolveSubtypesByClass(config, null, Object.class);

        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        subtypeResolver.registerSubtypes(new NamedType[] { new NamedType(subtype1), new NamedType(subtype2) });

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));
        assertTrue(result.contains(new NamedType(subtype2)));
        assertTrue(result.contains(new NamedType(baseType)));

        result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, subtype1);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(subtype1)));

        result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, subtype2);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(subtype2)));

        result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, Object.class);

        assertEquals(0, result.size());
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdWithJsonTypeInfo() throws Exception {
        Class<?> baseType = Animal.class;
        Class<?> dogType = Dog.class;
        Class<?> catType = Cat.class;

        AnnotatedClass annotatedType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, baseType);
        JsonTypeInfo jsonTypeInfo = ai.findAnnotation(annotatedType);
        TypeIdResolverBase typeIdResolver = jsonTypeInfo.getTypeIdResolver();

        AnnotatedClass dogTypeAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, dogType);
        JsonTypeInfo dogTypeJsonTypeInfo = ai.findAnnotation(dogTypeAnnotated);
        JsonSubTypes dogTypeJsonSubTypes = dogTypeJsonTypeInfo.getSubTypes();

        AnnotatedClass catTypeAnnotated = AnnotatedClassResolver.resolveWithoutSuperTypes(config, catType);
        JsonTypeInfo catTypeJsonTypeInfo = ai.findAnnotation(catTypeAnnotated);
        JsonSubTypes catTypeJsonSubTypes = catTypeJsonTypeInfo.getSubTypes();

        subtypeResolver.registerSubtypes(new NamedType[] {
            new NamedType(Dog.class),
            new NamedType(Cat.class)
        });

        List<NamedType> result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        assertEquals(3, result.size());
        assertTrue(result.contains(new NamedType(Animal.class)));
        assertTrue(result.contains(new NamedType(Dog.class)));
        assertTrue(result.contains(new NamedType(Cat.class)));

        result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, dogType);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(dogType)));

        result = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, catType);

        assertEquals(1, result.size());
        assertTrue(result.contains(new NamedType(catType)));

        List<NamedType> combinedResult = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        assertEquals(4, combinedResult.size());
        assertTrue(combinedResult.containsAll(Arrays.asList(new NamedType(Animal.class), new NamedType(Dog.class), new NamedType(Cat.class), new NamedType(baseType))));

        assertEquals(dogTypeJsonSubTypes.forEachSubtype(typeIdResolver), combinedResult);
        assertEquals(catTypeJsonSubTypes.forEachSubtype(typeIdResolver), combinedResult);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    static class Animal {
        String type;
    }

    static class Dog extends Animal {
    }

    static class Cat extends Animal {
    }
}