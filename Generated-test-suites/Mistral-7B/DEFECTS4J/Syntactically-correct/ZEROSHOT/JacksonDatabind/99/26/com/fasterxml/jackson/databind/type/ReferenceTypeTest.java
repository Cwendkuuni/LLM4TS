import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        StringBuilder expected = new StringBuilder("I");
        StringBuilder actual = referenceType.getErasedSignature();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        StringBuilder expected = new StringBuilder("Ljava/lang/Integer;<Ljava/lang/String;>");
        StringBuilder actual = referenceType.getGenericSignature();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertFalse(referenceType.isAnchorType());
    }
}