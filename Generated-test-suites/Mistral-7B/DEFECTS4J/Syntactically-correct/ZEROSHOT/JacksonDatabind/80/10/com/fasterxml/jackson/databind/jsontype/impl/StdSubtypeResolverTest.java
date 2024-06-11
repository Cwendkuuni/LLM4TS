package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.TypeUtil;
import com.fasterxml.jackson.databind.jsontype.impl.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

    private AnnotationIntrospector introspector;
    private MapperConfig config;
    private ClassIntrospector classIntrospector;
    private StdSubtypeResolver resolver;

    @Before
    public void setUp() {
        introspector = mock(AnnotationIntrospector.class);
        config = new MapperConfig();
        classIntrospector = mock(ClassIntrospector.class);
        resolver = new StdSubtypeResolver();
        when(introspector.findAnnotation(Class.class, JsonSubTypes.class))
                .thenReturn(mock(JsonSubTypes.class));
        when(classIntrospector.findMemberAnnotations(Mockito.any(AnnotatedClass.class),
                Mockito.eq(JsonSubTypes.class))).thenReturn(Collections.emptyList());
        when(classIntrospector.findMemberAnnotations(Mockito.any(AnnotatedClass.class),
                Mockito.eq(JsonTypeName.class))).thenReturn(Collections.emptyList());
    }

    @Test
    public void testRegisterSubtypes() {
        // test registering subtypes using an array
        Class<?> baseType = Object.class;
        Class<?>[] subTypes = new Class[] { String.class, Integer.class };
        resolver.registerSubtypes(subTypes);
        Set<NamedType> registeredSubtypes = resolver.getRegisteredSubtypes();

        assertEquals(2, registeredSubtypes.size());
        for (NamedType registeredSubtype : registeredSubtypes) {
            assertEquals(registeredSubtype.getType(), registeredSubtype.getName());
            assertEquals(baseType, registeredSubtype.getBaseType());
        }

        // test registering subtypes using a List
        List<Class<?>> subTypesList = Arrays.asList(String.class, Integer.class);
        resolver.registerSubtypes(subTypesList);
        registeredSubtypes = resolver.getRegisteredSubtypes();

        assertEquals(2, registeredSubtypes.size());
        for (NamedType registeredSubtype : registeredSubtypes) {
            assertEquals(registeredSubtype.getType(), registeredSubtype.getName());
            assertEquals(baseType, registeredSubtype.getBaseType());
        }
    }

    @Test
    public void testResolveByClassForSerialization() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // test resolving subtypes by class for serialization
        JavaType baseTypeJavaType = TypeUtil.constructType(baseType);
        AnnotatedMember property = mock(AnnotatedMember.class);
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(property));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, property,
                baseTypeJavaType);

        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getType(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }

    @Test
    public void testResolveByClassForDeserialization() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // test resolving subtypes by class for deserialization
        JavaType baseTypeJavaType = TypeUtil.constructType(baseType);
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(null));

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeJavaType);

        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getType(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }

    @Test
    public void testResolveByClassForDeserializationWithName() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // test resolving subtypes by class for deserialization with name
        JavaType baseTypeJavaType = TypeUtil.constructType(baseType);
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(null));

        Map<String, NamedType> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("stringType", new NamedType(subType1, "stringType"));
        subtypesByName.put("integerType", new NamedType(subType2, "integerType"));

        List<NamedType> resolvedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeJavaType,
                subtypesByName);

        assertEquals(2, resolvedSubtypes.size());
        for (NamedType subtype : resolvedSubtypes) {
            assertEquals(subtype.getName(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }

    @Test
    public void testResolveByTypeIdForSerialization() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // setup type id resolver
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(null));

        // test resolving subtypes by type id for serialization
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, null);

        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getType(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }

    @Test
    public void testResolveByTypeIdForDeserialization() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // setup type id resolver
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(null));

        Map<String, NamedType> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("stringType", new NamedType(subType1, "stringType"));
        subtypesByName.put("integerType", new NamedType(subType2, "integerType"));

        // test resolving subtypes by type id for deserialization
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType,
                subtypesByName);

        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            assertEquals(subtype.getName(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }

    @Test
    public void testEmptyCollections() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;

        // setup type id resolver
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.emptyList());

        // test resolving subtypes by type id for serialization
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, null);

        assertEquals(0, subtypes.size());

        // test resolving subtypes by class for serialization
        AnnotatedMember property = mock(AnnotatedMember.class);
        AnnotatedClass baseTypeAnnotatedClassForSerialization = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClassForSerialization);
        when(baseTypeAnnotatedClassForSerialization.getAnnotatedMembers()).thenReturn(Collections.singletonList(property));

        subtypes = resolver.collectAndResolveSubtypesByClass(config, property, null);

        assertEquals(0, subtypes.size());

        // test resolving subtypes by type id for deserialization
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, subtypesByName);

        assertEquals(0, subtypes.size());

        // test resolving subtypes by class for deserialization
        subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, null);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testResolveByClassForDeserializationWithTypeName() throws Exception {
        // setup base class
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        // setup subtypes
        when(introspector.findAnnotation(subType1, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { String.class }));
        when(introspector.findAnnotation(subType2, JsonSubTypes.class))
                .thenReturn(new JsonSubTypes(Type.STATIC, value = new Class[] { Integer.class }));

        // setup type id resolver
        AnnotatedClass baseTypeAnnotatedClass = mock(AnnotatedClass.class);
        when(classIntrospector.findAnnotatedClass(baseType)).thenReturn(baseTypeAnnotatedClass);
        when(baseTypeAnnotatedClass.getAnnotatedMembers()).thenReturn(Collections.singletonList(null));

        // test resolving subtypes by class for deserialization with explicit type name
        Map<String, NamedType> subtypesByName = new LinkedHashMap<>();
        subtypesByName.put("stringType", new NamedType(subType1, "stringType"));
        subtypesByName.put("integerType", new NamedType(subType2, "integerType"));

        List<NamedType> resolvedSubtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType,
                subtypesByName);

        assertEquals(2, resolvedSubtypes.size());
        for (NamedType subtype : resolvedSubtypes) {
            assertEquals(subtype.getName(), subtype.getName());
            assertEquals(baseType, subtype.getBaseType());
        }
    }
}