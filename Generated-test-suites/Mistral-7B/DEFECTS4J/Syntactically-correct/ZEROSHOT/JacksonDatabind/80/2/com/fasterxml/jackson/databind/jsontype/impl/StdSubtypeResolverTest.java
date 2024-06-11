package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.impl.ClassTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.SubtypeResolverBase;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.Types;

public class StdSubtypeResolverTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.enable(MapperFeature.AUTO_DETECT_TYPES);
        mapper.setTypeResolverBase(new TypeIdResolverBase() {
            @Override
            public TypeIdResolver<?> getTypeIdResolver() {
                return new StdSubtypeResolver();
            }
        });
    }

    @Test
    public void testEmptyRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // empty registration should not affect resolution
        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(Collections.emptyList(), types);
    }

    @Test
    public void testRegistrationByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MySubtype1.class, MySubtype2.class);

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, types.size());
        assertTrue(types.contains(new NamedType(MySubtype1.class)));
        assertTrue(types.contains(new NamedType(MySubtype2.class)));
    }

    @Test
    public void testRegistrationByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MySubtype1.class, "MySubtype1");
        resolver.registerSubtypes(MySubtype2.class, "MySubtype2");

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, types.size());
        assertTrue(types.contains(new NamedType(MySubtype1.class, "MySubtype1")));
        assertTrue(types.contains(new NamedType(MySubtype2.class, "MySubtype2")));
    }

    @Test
    public void testRegistrationByClassAndByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MySubtype1.class, "MySubtype1");
        resolver.registerSubtypes(MySubtype2.class);

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, types.size());
        assertTrue(types.contains(new NamedType(MySubtype1.class, "MySubtype1")));
        assertTrue(types.contains(new NamedType(MySubtype2.class)));
    }

    @Test
    public void testRegistrationAndCollectionByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MySubtype1.class, MySubtype2.class);

        List<NamedType> registered = new ArrayList<>();
        resolver.registerSubtypes(new NamedType(MySubtype3.class));
        resolver.registerSubtypes(new NamedType(MySubtype4.class));
        registered.addAll(resolver.getRegisteredSubtypes());

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(4, types.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class,
                MySubtype4.class), getTypesFromList(types));
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getRegisteredTypes(registered));
    }

    @Test
    public void testRegistrationAndCollectionByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MySubtype1.class, MySubtype2.class);

        List<NamedType> registered = new ArrayList<>();
        resolver.registerSubtypes(new NamedType(MySubtype3.class, "MySubtype3"));
        resolver.registerSubtypes(new NamedType(MySubtype4.class, "MySubtype4"));
        registered.addAll(resolver.getRegisteredSubtypes());

        List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(4, types.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class,
                MySubtype4.class), getTypesFromList(types));
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getRegisteredTypes(registered));
    }

    @Test
    public void testCollectionByClass() {
        List<NamedType> types = new ArrayList<>();
        types.add(new NamedType(MySubtype1.class));
        types.add(new NamedType(MySubtype2.class));

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types.toArray(new NamedType[0]));

        List<NamedType> collected = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, collected.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getTypesFromList(collected));
    }

    @Test
    public void testCollectionByTypeId() {
        List<NamedType> types = new ArrayList<>();
        types.add(new NamedType(MySubtype1.class, "MySubtype1"));
        types.add(new NamedType(MySubtype2.class, "MySubtype2"));

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types.toArray(new NamedType[0]));

        List<NamedType> collected = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, collected.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getTypesFromList(collected));
    }

    @Test
    public void testRegistrationAndCollectionByClassAndTypeId() {
        List<NamedType> types = new ArrayList<>();
        types.add(new NamedType(MySubtype1.class, "MySubtype1"));
        types.add(new NamedType(MySubtype2.class, "MySubtype2"));
        types.add(new NamedType(MySubtype3.class));
        types.add(new NamedType(MySubtype4.class));

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types.toArray(new NamedType[0]));

        List<NamedType> collected = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(4, collected.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class,
                MySubtype4.class), getTypesFromList(collected));

        List<NamedType> registered = resolver.getRegisteredSubtypes();
        assertEquals(4, registered.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class,
                MySubtype4.class), getTypesFromList(registered));
    }

    @Test
    public void testCollectionByClassAndTypeId() {
        List<NamedType> types = new ArrayList<>();
        types.add(new NamedType(MySubtype1.class, "MySubtype1"));
        types.add(new NamedType(MySubtype2.class, "MySubtype2"));

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types.toArray(new NamedType[0]));

        List<NamedType> collected = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, collected.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getTypesFromList(collected));
    }

    @Test
    public void testCollectionByClassAndTypeIdWithEmptyRegisteredSubtypes() {
        List<NamedType> types = new ArrayList<>();
        types.add(new NamedType(MySubtype1.class));
        types.add(new NamedType(MySubtype2.class));

        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(types.toArray(new NamedType[0]));

        List<NamedType> collected = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(2, collected.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class), getTypesFromList(collected));

        assertNull(resolver.getRegisteredSubtypes());
    }

    @Test
    public void testRegistrationWithAbstractBaseClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(MyAbstractBaseSubtype.class);

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyBaseClass.class), null);
        assertEquals(1, types.size());
        assertTrue(types.get(0) instanceof NamedType);
        assertEquals(MyAbstractBaseSubtype.class, ((NamedType) types.get(0)).getType());
    }

    @Test
    public void testRegistrationWithJsonSubtypesAnnotation() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyAnnotatedBaseClass.class), null);
        assertEquals(3, types.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class),
                getTypesFromList(types));
    }

    @Test
    public void testRegistrationWithJsonSubtypesAnnotationAndRegistration() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        List<NamedType> registered = new ArrayList<>();
        resolver.registerSubtypes(MySubtype4.class);
        registered.addAll(resolver.getRegisteredSubtypes());

        List<NamedType> types = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(),
                new AnnotatedClass(MyAnnotatedBaseClass.class), null);
        assertEquals(4, types.size());
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class,
                MySubtype4.class), getTypesFromList(types));
        assertEquals(Arrays.asList(MySubtype1.class, MySubtype2.class, MySubtype3.class),
                getRegisteredTypes(registered));
    }

    private List<Class<?>> getTypesFromList(List<NamedType> namedTypes) {
        List<Class<?>> types = new ArrayList<>();
        for (NamedType namedType : namedTypes) {
            types.add(namedType.getType());
        }
        return types;
    }

    private List<Class<?>> getRegisteredTypes(List<NamedType> registered) {
        List<Class<?>> registeredTypes = new ArrayList<>();
        for (NamedType namedType : registered) {
            registeredTypes.add(namedType.getType());
        }
        return registeredTypes;
    }

    static class MyBaseClass {
    }

    static class MyAbstractBaseSubtype extends MyBaseClass {
    }

    @JsonTypeName("MySubtype1")
    static class MySubtype1 extends MyBaseClass {
    }

    @JsonTypeName("MySubtype2")
    static class MySubtype2 extends MyBaseClass {
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = MySubtype3.class, name = "MySubtype3"),
            @JsonSubTypes.Type(value = MySubtype4.class, name = "MySubtype4")
    })
    static class MyAnnotatedBaseClass extends MyBaseClass {
    }

    static class MySubtype3 extends MyBaseClass {
    }

    static class MySubtype4 extends MyBaseClass {
    }
}