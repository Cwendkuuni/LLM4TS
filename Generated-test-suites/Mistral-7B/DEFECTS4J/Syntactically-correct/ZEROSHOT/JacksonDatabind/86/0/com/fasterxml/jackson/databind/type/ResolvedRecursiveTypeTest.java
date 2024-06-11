package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeReference;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        TypeFactory tf = new TypeFactory();
        TypeBindings bindings = TypeBindings.empty();

        // Test with null _referencedType
        JavaType type1 = new ResolvedRecursiveType(String.class, bindings);
        assertNotNull(type1);
        assertNull(type1.getSelfReferencedType());

        // Test with self reference
        JavaType refType = tf.constructType(Object.class);
        JavaType type2 = new ResolvedRecursiveType(Object.class, bindings);
        type2.setReference(refType);
        JavaType expectedType = refType;
        assertSame(expectedType, type2.getSelfReferencedType());

        // Test get signatures
        StringBuilder sb = new StringBuilder();
        JavaType type3 = new ResolvedRecursiveType(int[].class, bindings);
        type3.setReference(new ArrayType(IntType.instance, null));
        assertSame(sb.append("[Ljava.lang.int;").toString(), type3.getErasedSignature(sb).toString());
        assertSame(sb.append("Ljava.lang.reflect.ArrayType;Ljava/lang/Class;[B").toString(),
                type3.getGenericSignature(sb).toString());

        // Test simple equals
        JavaType type4 = new ResolvedRecursiveType(String.class, bindings);
        JavaType type5 = new ResolvedRecursiveType(String.class, bindings);
        assertSame(type4, type5);

        // Test equals with self reference
        JavaType type6 = new ResolvedRecursiveType(Object.class, bindings);
        JavaType type7 = new ResolvedRecursiveType(Object.class, bindings);
        JavaType refType2 = tf.constructType(String.class);
        JavaType type8 = new ResolvedRecursiveType(Object.class, bindings);
        type8.setReference(refType2);
        assertNotSame(type6, type8);

        type6.setReference(refType);
        assertSame(type6, type8);

        // Test toString
        JavaType type9 = new ResolvedRecursiveType(Type.class, bindings);
        JavaType refType3 = tf.constructType(String.class);
        type9.setReference(refType3);
        assertSame("[[recursive type; java.lang.String]", type9.toString());

        // Test equals with different classes
        JavaType type10 = new ResolvedRecursiveType(int[].class, bindings);
        JavaType type11 = new ResolvedRecursiveType(String.class, bindings);
        assertNotSame(type10, type11);
    }
}