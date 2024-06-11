import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getClassId(), referenceType.getContentType().getClassId());
        assertTrue(referenceType instanceof ReferenceType);
    }

    @Test
    public void testSetters() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        JavaType newTypeHandler = LongType.getInstance();
        JavaType newContentTypeHandler = DoubleType.getInstance();
        JavaType newValueHandler = FloatType.getInstance();
        JavaType newContentValueHandler = BigDecimalType.getInstance();

        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler)
                .withContentTypeHandler(newContentTypeHandler)
                .withValueHandler(newValueHandler)
                .withContentValueHandler(newContentValueHandler)
                .withStaticTyping();

        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertEquals(newContentTypeHandler, newReferenceType.getContentTypeHandler());
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testGetters() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        JavaType referencedType = referenceType.getReferencedType();
        assertTrue(referencedType instanceof JavaType);
        assertEquals(IntType.class, referencedType.getClass());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = IntType.getInstance();
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);

        Class<?> cls2 = String.class;
        JavaType refType2 = LongType.getInstance();
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        assertFalse(referenceType1.equals(referenceType2));

        Class<?> cls3 = String.class;
        JavaType refType3 = IntType.getInstance();
        ReferenceType referenceType3 = new ReferenceType(cls3, refType3, null, null, false);

        assertTrue(referenceType1.equals(referenceType3));
    }

    @Test
    public void testCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.String<int>]";

        assertEquals(expectedCanonicalName, canonicalName);
    }
}