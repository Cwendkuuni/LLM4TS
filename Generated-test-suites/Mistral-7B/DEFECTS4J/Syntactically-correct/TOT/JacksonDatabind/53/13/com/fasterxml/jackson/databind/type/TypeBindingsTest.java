package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeBindingsTest {

    private TypeFactory typeFactory;

    @BeforeClass
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
    }

    @Test
    public void testEmptyTypeBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        Assert.assertNotNull(bindings, "TypeBindings.emptyBindings() should not return null");
        Assert.assertEquals(bindings.size(), 0, "Empty bindings should have no elements");
        Assert.assertNull(bindings.findBoundType("UnknownName"));
    }

    @Test
    public void testCreateBindingsForSimpleClass() {
        TypeBindings bindings = TypeBindings.create(String[].class, JavaType.constructCollectionType(ArrayList.class, String.class));
        Assert.assertEquals(bindings.size(), 1, "Bindings for String[] should have one element");
        Assert.assertEquals(bindings.getBoundName(0), "T", "First bound type should be named 'T'");
        Assert.assertEquals(bindings.getBoundType(0).getTypeName(), "java.lang.String",
                "First bound type should be JavaType representing String");

        TypeBindings bindings2 = TypeBindings.create(List.class, String[].class);
        Assert.assertEquals(bindings2.size(), 1, "Bindings for List<String[]> should have one element");
        Assert.assertEquals(bindings2.getBoundName(0), "T", "First bound type should be named 'T'");
        Assert.assertEquals(bindings2.getBoundType(0).getTypeName(), "java.lang.String[]",
                "First bound type should be JavaType representing String[]");
    }

    @Test
    public void testCreateBindingsForClassWithTypeParameters() {
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, String[].class);
        Assert.assertEquals(bindings.size(), 2, "Bindings for Map<String, String[]> should have two elements");
        Assert.assertEquals(bindings.getBoundName(0), "K", "First bound type should be named 'K'");
        Assert.assertEquals(bindings.getBoundType(0).getTypeName(), "java.lang.String", "First bound type should be JavaType representing String");
        Assert.assertEquals(bindings.getBoundName(1), "V", "Second bound type should be named 'V'");
        Assert.assertEquals(bindings.getBoundType(1).getTypeName(), "java.lang.String[]",
                "Second bound type should be JavaType representing String[]");

        TypeBindings bindings2 = TypeBindings.createIfNeeded(List.class, String.class, String[].class);
        Assert.assertEquals(bindings2.size(), 2, "Bindings for List<String> and List<String[]> should have two elements");
        Assert.assertEquals(bindings2.getBoundName(0), "T", "First bound type should be named 'T'");
        Assert.assertEquals(bindings2.getBoundType(0).getTypeName(), "java.lang.String",
                "First bound type should be JavaType representing String");
        Assert.assertEquals(bindings2.getBoundName(1), "V", "Second bound type should be named 'V'");
        Assert.assertEquals(bindings2.getBoundType(1).getTypeName(), "java.lang.String[]",
                "Second bound type should be JavaType representing String[]");
    }

    @Test
    public void testCreateBindingsForArray() {
        TypeBindings bindings = TypeBindings.create(int[].class);
        Assert.assertEquals(bindings.size(), 1, "Bindings for int[] should have one element");
        Assert.assertEquals(bindings.getBoundName(0), "T", "First bound type should be named 'T'");
        Assert.assertEquals(bindings.getBoundType(0).getTypeName(), "int", "First bound type should be JavaType representing int");

        TypeBindings bindings2 = TypeBindings.createIfNeeded(int[].class);
        Assert.assertEquals(bindings2.size(), 1, "Bindings for int[] should have one element");
        Assert.assertEquals(bindings2.getBoundName(0), "T", "First bound type should be named 'T'");
        Assert.assertEquals(bindings2.getBoundType(0).getTypeName(), "int",
                "First bound type should be JavaType representing int");
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, String[].class);
        Assert.assertNull(bindings.findBoundType("UnknownName"), "Unknown name should not be found");
        Assert.assertEquals(bindings.findBoundType("K").getTypeName(), "java.lang.String", "K should be mapped to String");
        Assert.assertEquals(bindings.findBoundType("V").getTypeName(), "java.lang.String[]", "V should be mapped to String[]");
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(Map.class, String.class, JavaType.constructRawType(String[].class));
        TypeBindings bindings2 = bindings.withUnboundVariable("X");
        Assert.assertEquals(bindings2.size(), bindings.size() + 1, "Bindings with unbound variable should have one more element");
        Assert.assertEquals(bindings2.getBoundName(bindings.size()), "X", "Unbound variable should be named 'X'");
        Assert.assertNull(bindings2.findBoundType("X"));
        Assert.assertNull(bindings2.findBoundType("K"));
        Assert.assertEquals(bindings2.getBoundName(0), "K", "First bound type should still be named 'K'");
        Assert.assertEquals(bindings2.getBoundType(0).getTypeName(), "java.lang.String", "First bound type should still be mapped to String");
        Assert.assertEquals(bindings2.getBoundName(1), "V", "Second bound type should still be named 'V'");
        Assert.assertEquals(bindings2.getBoundType(1).getTypeName(), "java.lang.String[]",
                "Second bound type should still be mapped to String[]");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidCreateBindings() {
        TypeBindings.create(List.class, JavaType.constructCollectionType(ArrayList.class, Integer.TYPE, Integer.TYPE));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidCreateBindings2() {
        TypeBindings.create(List.class, String[].class, Integer.TYPE);
    }
}