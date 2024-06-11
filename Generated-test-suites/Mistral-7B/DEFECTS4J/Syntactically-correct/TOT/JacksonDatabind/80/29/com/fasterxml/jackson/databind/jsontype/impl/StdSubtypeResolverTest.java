package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class StdSubtypeResolverTest {

    private ObjectMapper mapper;

    @BeforeClass
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        resolver.registerSubtypes(new Class<?>[]{subType1, subType2});
        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("typeId", baseType.getName());

        // Test with valid input
        ObjectNode result1 = mapper.readerForUpdating(baseNode)
                .reader(new TypeReference<ObjectNode>() {})
                .readValueAs(ObjectNode.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertNotNull(subtypes);
        assertEquals(subtypes.size(), 2);
        assertEquals(subtypes.get(0).getType(), subType1);
        assertEquals(subtypes.get(1).getType(), subType2);

        // Test with null input
        ObjectNode baseNodeNull = mapper.createObjectNode();
        List<NamedType> subtypesNull = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, null);
        assertEquals(subtypesNull.size(), 0);
    }

    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        class MyClass1 {
            // Empty class
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        class MyClass2 {
            // Empty class
        }

        resolver.registerSubtypes(new Class<?>[]{MyClass1.class, MyClass2.class});
        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("type", "MyClass1");

        ObjectNode result = mapper.readerForUpdating(baseNode)
                .reader(new TypeReference<ObjectNode>() {})
                .readValueAs(ObjectNode.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertNotNull(subtypes);
        assertEquals(subtypes.size(), 2);
        assertEquals(subtypes.get(0).getName(), "MyClass1");
        assertEquals(subtypes.get(1).getName(), "MyClass2");
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_EmptyAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        // Test with empty annotations
        ObjectNode baseNode = mapper.createObjectNode();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_EmptyRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        // Test with empty registered subtypes
        resolver.registerSubtypes(new NamedType[0]);
        ObjectNode baseNode = mapper.createObjectNode();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        // Test with null baseType
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, null);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByClassId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subType1 = Integer.class;

        resolver.registerSubtypes(new Class<?>[]{subType1});
        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("classId", baseType.getName());

        ObjectNode result = mapper.readerForUpdating(baseNode)
                .reader(new TypeReference<ObjectNode>() {})
                .readValueAs(ObjectNode.class);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);
        assertNotNull(subtypes);
        assertEquals(subtypes.size(), 1);
        assertEquals(subtypes.get(0).getType(), subType1);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyAnnotations() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        ObjectNode baseNode = mapper.createObjectNode();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyRegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        ObjectNode baseNode = mapper.createObjectNode();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_NullBaseType() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        ObjectNode baseNode = mapper.createObjectNode();
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, null);
        assertEquals(subtypes.size(), 0);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_RegisteredSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subType1 = String.class;
        Class<?> subType2 = Integer.class;

        resolver.registerSubtypes(new Class<?>[]{subType1, subType2});
        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("classId", baseType.getName());

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertNotNull(subtypes);
        assertEquals(subtypes.size(), 2);
        assertEquals(subtypes.get(0).getType(), subType1);
        assertEquals(subtypes.get(1).getType(), subType2);
    }

    @Test
    public void testCollectAndResolveSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        class MyClass1 {
            // Empty class
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        class MyClass2 {
            // Empty class
        }

        resolver.registerSubtypes(new Class<?>[]{MyClass1.class, MyClass2.class});
        ObjectNode baseNode = mapper.createObjectNode();
        baseNode.put("type", "MyClass1");

        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);
        assertNotNull(subtypes);
        assertEquals(subtypes.size(), 2);
        assertEquals(subtypes.get(0).getName(), "MyClass1");
        assertEquals(subtypes.get(1).getName(), "MyClass2");
    }
}