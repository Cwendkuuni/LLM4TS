import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals("java.lang.String[]", referenceType.getCanonicalName());
        assertEquals(String[].class, referenceType.getType());
        assertEquals(String.class, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ObjectMapper objectMapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(String.class, javaType, null, objectMapper, false);

        ObjectMapper newObjectMapper = new ObjectMapper();
        ReferenceType newRefType = refType.withTypeHandler(newObjectMapper);

        assertEquals(objectMapper, newRefType.getTypeHandler());
        assertNotSame(objectMapper, newRefType);
        assertEquals("java.lang.String[]", newRefType.getCanonicalName());
        assertEquals(String[].class, newRefType.getType());
        assertEquals(String.class, newRefType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidTypeHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);
        refType.withTypeHandler(new Object());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ObjectMapper objectMapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(String.class, javaType, null, objectMapper, false);
        ObjectMapper newObjectMapper = new ObjectMapper();
        ReferenceType newRefType = refType.withContentTypeHandler(newObjectMapper);

        assertEquals(objectMapper, newRefType.getTypeHandler());
        assertEquals(newObjectMapper, newRefType.getContentTypeHandler());
        assertEquals("java.lang.String[]", newRefType.getCanonicalName());
        assertEquals(String[].class, newRefType.getType());
        assertEquals(String.class, newRefType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidContentTypeHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);
        refType.withContentTypeHandler(new Object());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ObjectMapper objectMapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(String.class, javaType, null, objectMapper, false);
        MyValueHandler myValueHandler = new MyValueHandler();
        ReferenceType newRefType = refType.withValueHandler(myValueHandler);

        assertEquals(myValueHandler, newRefType.getValueHandler());
        assertEquals("java.lang.String[]", newRefType.getCanonicalName());
        assertEquals(String[].class, newRefType.getType());
        assertEquals(String.class, newRefType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidValueHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);
        refType.withValueHandler(new Object());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ObjectMapper objectMapper = new ObjectMapper();
        ReferenceType refType = new ReferenceType(String.class, javaType, null, objectMapper, false);
        MyContentValueHandler myContentValueHandler = new MyContentValueHandler();
        ReferenceType newRefType = refType.withContentValueHandler(myContentValueHandler);

        assertEquals(myContentValueHandler, newRefType.getContentValueHandler());
        assertEquals("java.lang.String[]", newRefType.getCanonicalName());
        assertEquals(String[].class, newRefType.getType());
        assertEquals(String.class, newRefType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidContentValueHandler() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);
        refType.withContentValueHandler(new Object());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);
        ReferenceType newRefType = refType.withStaticTyping();

        assertTrue(newRefType.isStatic());
        assertEquals("java.lang.String[]", newRefType.getCanonicalName());
        assertEquals(String[].class, newRefType.getType());
        assertEquals(String.class, newRefType.getContentType());
    }

    @Test
    public void testReferenceTypeCanonicalName() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals("java.lang.String[]", refType.getCanonicalName());
    }

    @Test
    public void testReferenceTypeType() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(String[].class, refType.getType());
    }

    @Test
    public void testReferenceTypeContentType() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(String.class, refType.getContentType());
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(javaType, refType.getReferencedType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testReferenceTypeContainedTypeCount() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(1, refType.containedTypeCount());
    }

    @Test
    public void testReferenceTypeContainedType() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(javaType, refType.containedType(0));
    }

    @Test
    public void testReferenceTypeContainedTypeName() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals("T", refType.containedTypeName(0));
    }

    @Test
    public void testReferenceTypeGetParameterSource() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        assertEquals(String.class, refType.getParameterSource());
    }

    @Test
    public void testReferenceTypeGetErasedSignature() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        StringBuilder stringBuilder = new StringBuilder();
        refType.getErasedSignature(stringBuilder);

        assertEquals("String[]", stringBuilder.toString());
    }

    @Test
    public void testReferenceTypeGetGenericSignature() {
        JavaType javaType = TypeReference.getType(String[].class);
        ReferenceType refType = new ReferenceType(String.class, javaType, null, null, false);

        StringBuilder stringBuilder = new StringBuilder();
        refType.getGenericSignature(stringBuilder);

        assertEquals("[Ljava.lang.String;<T:Ljava.lang.String;>", stringBuilder.toString());
    }
}