import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(JavaType.class, referenceType.getTypeParameters()[0].getType());
        assertEquals(javaStringType, referenceType.getContentType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object typeHandler = new Object();

        ReferenceType originalReferenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        ReferenceType modifiedReferenceType = originalReferenceType.withTypeHandler(typeHandler);

        assertNotSame(originalReferenceType, modifiedReferenceType);
        assertEquals(typeHandler, modifiedReferenceType.getTypeHandler());
        assertEquals(originalReferenceType.getContentType(), modifiedReferenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentTypeHandler = new Object();

        ReferenceType originalReferenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        ReferenceType modifiedReferenceType = originalReferenceType.withContentTypeHandler(contentTypeHandler);

        assertNotSame(originalReferenceType, modifiedReferenceType);
        assertEquals(originalReferenceType.getContentType(), modifiedReferenceType.getContentType());
        assertEquals(contentTypeHandler, modifiedReferenceType.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object valueHandler = new Object();

        ReferenceType originalReferenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        ReferenceType modifiedReferenceType = originalReferenceType.withValueHandler(valueHandler);

        assertNotSame(originalReferenceType, modifiedReferenceType);
        assertEquals(valueHandler, modifiedReferenceType.getValueHandler());
        assertEquals(originalReferenceType.getContentType(), modifiedReferenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentValueHandler = new Object();

        ReferenceType originalReferenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        ReferenceType modifiedReferenceType = originalReferenceType.withContentValueHandler(contentValueHandler);

        assertNotSame(originalReferenceType, modifiedReferenceType);
        assertEquals(contentValueHandler, modifiedReferenceType.getValueHandler());
        assertEquals(originalReferenceType.getContentType(), modifiedReferenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        ReferenceType staticTypingReferenceType = referenceType.withStaticTyping();

        assertNotSame(referenceType, staticTypingReferenceType);
        assertTrue(staticTypingReferenceType.isStatic());
        assertEquals(javaStringType, staticTypingReferenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object invalidTypeHandler = null;

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        referenceType.withTypeHandler(invalidTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object invalidContentTypeHandler = null;

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        referenceType.withContentTypeHandler(invalidContentTypeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object invalidValueHandler = null;

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        referenceType.withValueHandler(invalidValueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
        Object invalidContentValueHandler = null;

        ReferenceType referenceType = ReferenceType.construct(String.class, javaStringType, null, null);
        referenceType.withContentValueHandler(invalidContentValueHandler);
    }
}