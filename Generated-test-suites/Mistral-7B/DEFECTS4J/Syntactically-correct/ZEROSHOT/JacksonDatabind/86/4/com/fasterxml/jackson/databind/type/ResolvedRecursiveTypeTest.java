package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Test;

/**
 * Test class for {@link ResolvedRecursiveType}
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(obj);
        assertEquals(erasedType, obj.getRawClass());
        assertNull(obj.getSelfReferencedType());
        assertEquals(obj.getClass(), obj.getClass());
    }

    /**
     * Test setReference method.
     */
    @Test
    public void testSetReference() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);

        obj.setReference(ref);
        assertNotNull(obj.getSelfReferencedType());
        assertEquals(ref, obj.getSelfReferencedType());

        try {
            obj.setReference(ref);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj.setReference(ref);

        JavaType result = obj.getSelfReferencedType();
        assertEquals(ref, result);

        obj = new ResolvedRecursiveType(erasedType, bindings);
        result = obj.getSelfReferencedType();
        assertNull(result);
    }

    /**
     * Test getGenericSignature method.
     */
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj.setReference(ref);

        obj.getGenericSignature(sb);
        String expected = ref.getGenericSignature(sb).toString();
        assertEquals(expected, sb.toString());

        obj = new ResolvedRecursiveType(erasedType, bindings);
        sb.setLength(0);
        obj.getGenericSignature(sb);
        assertEquals("[]", sb.toString());
    }

    /**
     * Test getErasedSignature method.
     */
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        JavaType ref = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj.setReference(ref);

        obj.getErasedSignature(sb);
        String expected = ref.getErasedSignature(sb).toString();
        assertEquals(expected, sb.toString());

        obj = new ResolvedRecursiveType(erasedType, bindings);
        sb.setLength(0);
        obj.getErasedSignature(sb);
        assertEquals("", sb.toString());
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new TypeBase(String.class, bindings, null, null, 0, null, null, false);

        JavaType result = obj.withContentType(contentType);
        assertEquals(obj, result);
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = obj.withTypeHandler(handler);
        assertEquals(obj, result);
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = obj.withContentTypeHandler(handler);
        assertEquals(obj, result);
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = obj.withValueHandler(handler);
        assertEquals(obj, result);
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        JavaType result = obj.withContentValueHandler(handler);
        assertEquals(obj, result);
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);

        JavaType result = obj.withStaticTyping();
        assertEquals(obj, result);
    }

    /**
     * Test _narrow method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = String.class;

        obj._narrow(subclass);
        fail("UnsupportedOperationException expected");
    }

    /**
     * Test refine method.
     */
    @Test
    public void testRefine() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = Object[].class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new TypeBase(List.class, bindings, null, null, 0, null, null, false);
        JavaType[] superInterfaces = new JavaType[] { new TypeBase(Serializable.class, bindings, null, null, 0, null, null, false) };

        JavaType result = obj.refine(rawType, newBindings, superClass, superInterfaces);
        assertNull(result);
    }

    /**
     * Test isContainerType method.
     */
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);

        boolean result = obj.isContainerType();
        assertFalse(result);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType obj = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj.setReference(ref);

        String expected = "[recursive type; [I@" + ref.toString().hashCode() + "]";
        String result = obj.toString();
        assertEquals(expected, result);

        obj = new ResolvedRecursiveType(erasedType, bindings);
        result = obj.toString();
        assertEquals("{[recursive type; UNRESOLVED]}", result);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType obj1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType obj2 = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        JavaType ref2 = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj1.setReference(ref1);
        obj2.setReference(ref2);

        assertTrue(obj1.equals(obj2));

        obj1 = new ResolvedRecursiveType(erasedType, bindings);
        obj2 = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(obj1.equals(obj2));

        obj1.setReference(ref1);
        obj2.setReference(ref2);
        obj1.setRawClass(String.class);
        obj2.setRawClass(Integer.class);

        assertFalse(obj1.equals(obj2));

        obj1 = new ResolvedRecursiveType(erasedType, bindings);
        obj2 = new ResolvedRecursiveType(erasedType, bindings);

        ref1 = new TypeBase(Integer[].class, bindings, null, null, 0, null, null, false);
        ref2 = new TypeBase(Integer.TYPE, bindings, null, null, 0, null, null, false);
        obj1.setReference(ref1);
        obj2.setReference(ref2);

        assertFalse(obj1.equals(obj2));
    }
}