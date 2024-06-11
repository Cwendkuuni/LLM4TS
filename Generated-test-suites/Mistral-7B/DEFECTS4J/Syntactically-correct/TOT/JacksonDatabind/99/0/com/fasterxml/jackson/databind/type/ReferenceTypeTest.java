import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeFromTypeBase() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType = ReferenceType.construct(baseType, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeWithNullRefType() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeWithInvalidBaseType() {
        Class<String> cls = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testReferenceTypeWithContentType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newContentType = referenceType.withContentType(contentType);
        assertEquals(cls, newContentType.getRawClass());
        assertEquals(contentType, newContentType.getContentType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(contentType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.<Object>getValueHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.withValueHandler(handler);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.withContentValueHandler(handler);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(contentType, newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.<Object>getValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);
        JavaType newReferenceType = referenceType.withStaticTyping();
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(refType, newReferenceType.getContentType());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testReferenceTypeRefine() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType refinedType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false).refine(String[].class, TypeBindings.emptyBindings(), null, new JavaType[0]);
        assertEquals(cls, refinedType.getRawClass());
        assertEquals(refType, refinedType.getContentType());
    }
}