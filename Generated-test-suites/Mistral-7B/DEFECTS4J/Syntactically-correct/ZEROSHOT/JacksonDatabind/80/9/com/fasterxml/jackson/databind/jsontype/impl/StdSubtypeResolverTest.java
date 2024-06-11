package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdSubtypeResolverTest {

    @Test
    public void testStdSubtypeResolver() {
        MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

        // Test empty registration
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        String baseTypeName = "com.example.base.Class";
        String subtypeTypeName1 = "com.example.subtype1.Class";
        String subtypeTypeName2 = "com.example.subtype2.Class";

        Class<?> baseType = Class.forName(baseTypeName);
        Class<?> subtypeType1 = Class.forName(subtypeTypeName1);
        Class<?> subtypeType2 = Class.forName(subtypeTypeName2);

        // Test null argument
        assertNull(resolver.collectAndResolveSubtypesByClass(config, null, null));
        assertNull(resolver.collectAndResolveSubtypesByTypeId(config, null, null));

        // Test empty argument
        assertEmpty(resolver.collectAndResolveSubtypesByClass(config, null, new JavaType[] { null }));
        assertEmpty(resolver.collectAndResolveSubtypesByTypeId(config, null, null));

        // Test registration by class
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new Class[] { subtypeType1, subtypeType2 });
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null,
                new JavaType[] { ClassType.construct(baseType) });
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(subtypeType1)) {
                assertEquals("subtype1", subtype.getName());
            } else if (subtype.getType().equals(subtypeType2)) {
                assertEquals("subtype2", subtype.getName());
            }
        }

        // Test registration by annotation
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithTypeId.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(Subtype1.class)) {
                assertEquals("subtype1", subtype.getName());
            } else if (subtype.getType().equals(Subtype2.class)) {
                assertEquals("subtype2", subtype.getName());
            }
        }

        // Test registration by annotation with custom names
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithTypeName.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(Subtype1.class)) {
                assertEquals("Subtype1", subtype.getName());
            } else if (subtype.getType().equals(Subtype2.class)) {
                assertEquals("Subtype2", subtype.getName());
            }
        }

        // Test registration by annotation with multiple subtypes
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithMultipleSubtypes.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(3, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(Subtype1.class)) {
                assertEquals("Subtype1", subtype.getName());
            } else if (subtype.getType().equals(Subtype2.class)) {
                assertEquals("Subtype2", subtype.getName());
            } else if (subtype.getType().equals(Subtype3.class)) {
                assertEquals("Subtype3", subtype.getName());
            }
        }

        // Test registration by annotation with empty subtypes array
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithEmptySubtypes.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(0, subtypes.size());

        // Test registration by annotation with non-matching subtypes
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithNonMatchingSubtypes.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(0, subtypes.size());

        // Test registration by annotation with subtypes with same name but different classes
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(BaseWithDuplicateSubtypes.class);
        subtypes = resolver.collectAndResolveSubtypesByClassId(config, null, baseType, null);
        assertEquals(2, subtypes.size());
        for (NamedType subtype : subtypes) {
            if (subtype.getType().equals(Subtype1.class)) {
                assertEquals("Subtype1", subtype.getName());
            } else if (subtype.getType().equals(Subtype2.class)) {
                assertEquals("Subtype1", subtype.getName());
            }
        }
    }

    private void assertEmpty(Collection<NamedType> collection) {
        assertTrue(collection.isEmpty());
    }

    private void assertNull(Collection<NamedType> collection) {
        assertNull(collection);
    }

    private static class BaseWithTypeId {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1 {
            @JsonTypeName("subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype2 {
            @JsonTypeName("subtype2")
            String name;
        }
    }

    private static class BaseWithTypeName {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        @JsonTypeName("BaseType")
        static class Subtype1 {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        @JsonTypeName("BaseType")
        static class Subtype2 {
            @JsonTypeName("Subtype2")
            String name;
        }
    }

    private static class BaseWithMultipleSubtypes {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1 {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype2 {
            @JsonTypeName("Subtype2")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype3 {
            @JsonTypeName("Subtype3")
            String name;
        }
    }

    private static class BaseWithEmptySubtypes {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1 {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype2 {
            @JsonTypeName("Subtype2")
            String name;
        }
    }

    private static class BaseWithNonMatchingSubtypes {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1 {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.VALUE, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype2 {
            @JsonTypeName("Subtype2")
            String name;
        }
    }

    private static class BaseWithDuplicateSubtypes {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1 {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype1Subtype {
            @JsonTypeName("Subtype1")
            String name;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, name = "type")
        static class Subtype2 {
            @JsonTypeName("Subtype2")
            String name;
        }
    }
}