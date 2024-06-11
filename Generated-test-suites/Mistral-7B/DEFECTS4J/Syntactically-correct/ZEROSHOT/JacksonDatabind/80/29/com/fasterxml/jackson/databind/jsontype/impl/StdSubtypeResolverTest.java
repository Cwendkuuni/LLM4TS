package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.AnnotatedClassResolver;

public class StdSubtypeResolverTest {

    @Test
    public void testStandardSubtypeResolver() throws Exception {
        MapperConfig<?> config = new MapperConfig.Builder().build();
        final StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test registration of subtypes
        resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));
        resolver.registerSubtypes(new NamedType(Short.class, "ByteSubtype"));

        // Test that registered subtypes are found by class
        JavaType baseType = config.getTypeFactory().constructType(Integer.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config,
                null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(String.class, "UppercaseString")));
        assertTrue(subtypes.contains(new NamedType(Short.class, "ByteSubtype")));

        // Test that registered subtypes are found by class with annotations
        Class<?> aClass = ClassUtil.findClass(AnnotatedSubtypes.class.getName(), null);
        JavaType baseTypeAnnotation = config.getTypeFactory().constructType(aClass);
        List<NamedType> subtypesAnnotation = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeAnnotation);
        assertEquals(2, subtypesAnnotation.size());
        assertTrue(subtypesAnnotation.contains(new NamedType(String.class, "UppercaseString")));
        assertTrue(subtypesAnnotation.contains(new NamedType(Short.class, "ByteSubtype")));

        // Test that registered subtypes are found by type id
        JavaType baseTypeTypeId = config.getTypeFactory().constructType(aClass, "BaseType");
        List<NamedType> subtypesTypeId = resolver.collectAndResolveSubtypesByTypeId(config,
                null, baseTypeTypeId);
        assertEquals(2, subtypesTypeId.size());
        assertTrue(subtypesTypeId.contains(new NamedType(String.class, "UppercaseString")));
        assertTrue(subtypesTypeId.contains(new NamedType(Short.class, "ByteSubtype")));

        // Test that registered subtypes take precedence over annotated subtypes
        JavaType baseTypeAnnotated = config.getTypeFactory().constructType(String.class);
        List<NamedType> subtypesAnnotated = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeAnnotated);
        assertEquals(1, subtypesAnnotated.size());
        assertTrue(subtypesAnnotated.contains(new NamedType(String.class, "UppercaseStringAnnotated")));

        // Test that annotated subtypes are found by class
        JavaType baseTypeAnnotatedClass = config.getTypeFactory().constructType(aClass);
        List<NamedType> subtypesAnnotatedClass = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeAnnotatedClass);
        assertEquals(2, subtypesAnnotatedClass.size());
        assertTrue(subtypesAnnotatedClass.contains(new NamedType(String.class, "UppercaseStringAnnotated")));
        assertTrue(subtypesAnnotatedClass.contains(new NamedType(Short.class, "ByteSubtypeAnnotated")));

        // Test that annotated subtypes are found by class with annotations
        List<NamedType> subtypesAnnotatedAnnotation = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeAnnotation);
        assertEquals(2, subtypesAnnotatedAnnotation.size());
        assertTrue(subtypesAnnotatedAnnotation.contains(new NamedType(String.class,
                "UppercaseStringAnnotated")));
        assertTrue(subtypesAnnotatedAnnotation.contains(new NamedType(Short.class, "ByteSubtypeAnnotated")));

        // Test that annotated subtypes are found by type id
        List<NamedType> subtypesAnnotatedTypeId = resolver.collectAndResolveSubtypesByTypeId(config,
                null, baseTypeTypeId);
        assertEquals(2, subtypesAnnotatedTypeId.size());
        assertTrue(subtypesAnnotatedTypeId.contains(new NamedType(String.class,
                "UppercaseStringAnnotated")));
        assertTrue(subtypesAnnotatedTypeId.contains(new NamedType(Short.class, "ByteSubtypeAnnotated")));

        // Test that annotated subtypes take precedence over registered subtypes
        List<NamedType> subtypesTypeIdRegistered = resolver.collectAndResolveSubtypesByTypeId(config,
                null, baseTypeTypeId);
        assertEquals(2, subtypesTypeIdRegistered.size());
        assertTrue(subtypesTypeIdRegistered.contains(new NamedType(String.class,
                "UppercaseStringAnnotated")));
        assertTrue(subtypesTypeIdRegistered.contains(new NamedType(Short.class, "ByteSubtypeAnnotated")));

        // Test that registered subtypes are not found if they are not subtypes
        JavaType baseTypeNotSubtype = config.getTypeFactory().constructType(Long.class);
        List<NamedType> subtypesNotSubtype = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeNotSubtype);
        assertTrue(subtypesNotSubtype.isEmpty());

        // Test that annotated subtypes are found if the base type is a super type
        JavaType baseTypeSuperType = config.getTypeFactory().constructType(Object.class);
        List<NamedType> subtypesSuperType = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeSuperType);
        assertEquals(4, subtypesSuperType.size());
        assertTrue(subtypesSuperType.contains(new NamedType(String.class, "UppercaseString")));
        assertTrue(subtypesSuperType.contains(new NamedType(Short.class, "ByteSubtype")));
        assertTrue(subtypesSuperType.contains(new NamedType(String.class, "UppercaseStringAnnotated")));
        assertTrue(subtypesSuperType.contains(new NamedType(Short.class, "ByteSubtypeAnnotated")));

        // Test that no subtypes are found for abstract base types
        JavaType baseTypeAbstract = config.getTypeFactory().constructType(AbstractBaseType.class);
        List<NamedType> subtypesAbstract = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeAbstract);
        assertTrue(subtypesAbstract.isEmpty());

        // Test that no subtypes are found for non-annotated base types
        JavaType baseTypeNoAnnotations = config.getTypeFactory().constructType(AnnotatedSubtypesNoAnnotations.class);
        List<NamedType> subtypesNoAnnotations = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeNoAnnotations);
        assertTrue(subtypesNoAnnotations.isEmpty());

        // Test that no subtypes are found for non-public base types
        JavaType baseTypeNonPublic = config.getTypeFactory().constructType(NonPublicBaseType.class);
        List<NamedType> subtypesNonPublic = resolver.collectAndResolveSubtypesByClass(config,
                null, baseTypeNonPublic);
        assertTrue(subtypesNonPublic.isEmpty());
    }

    static class AbstractBaseType {
    }

    static class AnnotatedSubtypes {
        @JsonTypeInfo(as = As.PROPERTY, use = Id.CLASS)
        @JsonSubTypes({ @Type(value = String.class, name = "UppercaseString"),
                @Type(value = Short.class, name = "ByteSubtype"),
                @Type(value = String.class, name = "UppercaseStringAnnotated"),
                @Type(value = Short.class, name = "ByteSubtypeAnnotated") })
        public static class UppercaseString {}

        public static class ByteSubtype {}
    }

    static class AnnotatedSubtypesNoAnnotations {
        @JsonTypeInfo(as = As.PROPERTY, use = Id.CLASS)
        public static class UppercaseString {}

        public static class ByteSubtype {}
    }

    static class NonPublicBaseType {
        static class InnerClass {
        }
    }
}