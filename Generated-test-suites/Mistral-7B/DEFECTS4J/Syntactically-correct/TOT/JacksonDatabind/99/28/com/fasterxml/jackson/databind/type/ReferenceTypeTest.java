import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType = null;

        ReferenceType referenceType = ReferenceType.construct(stringClass, bindings, superClass, superInts, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertArrayEquals(superInts, referenceType.getSuperinterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getReferencedType());
        assertFalse(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperclass());
        assertArrayEquals(new JavaType[0], referenceType.getSuperinterfaces());
        assertNotSame(baseType.hashCode(), referenceType.getTypeId());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType = null;

        ReferenceType referenceType = new ReferenceType(stringClass, bindings, superClass, superInts, refType, anchorType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperclass());
        assertArrayEquals(new JavaType[0], referenceType.getSuperinterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());

        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        assertNotSame(referenceType, newReferenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperclass());
        assertArrayEquals(new JavaType[0], referenceType.getSuperinterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertNotSame(newContentType, referenceType.getContentType());
        assertSame(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());

        assertEquals(Integer.class, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getBindings());
        assertEquals(null, newReferenceType.getSuperclass());
        assertArrayEquals(new JavaType[0], newReferenceType.getSuperinterfaces());
        assertEquals(newContentType.hashCode(), newReferenceType.getTypeId());
        assertNull(newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
        assertNotSame(newContentType, newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getReferencedType());
        assertTrue(newReferenceType.hasContentType());
        assertTrue(newReferenceType.isReferenceType());
        assertNull(newReferenceType.getAnchorType());
        assertFalse(newReferenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidReferenceTypeUpgrade() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        // this should fail because baseType is not a TypeBase
        ReferenceType invalidReferenceType = ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testReferenceTypeEquality() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType1 = null;
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType2 = null;

        ReferenceType referenceType1 = new ReferenceType(stringClass, bindings, superClass, superInts, refType1, anchorType1);
        ReferenceType referenceType2 = new ReferenceType(stringClass, bindings, superClass, superInts, refType2, anchorType2);

        ReferenceType identicalReferenceType = new ReferenceType(stringClass, bindings, superClass, superInts, refType1, anchorType1);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(identicalReferenceType));
        assertTrue(identicalReferenceType.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));

        JavaType modifiedRefType = new JavaType() {
            @Override
            public boolean equals(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int hashCode() {
                return 42;
            }
        };

        ReferenceType modifiedReferenceType1 = new ReferenceType(stringClass, bindings, superClass, superInts, modifiedRefType, anchorType1);
        ReferenceType modifiedReferenceType2 = new ReferenceType(stringClass, bindings, superClass, superInts, modifiedRefType, anchorType2);

        assertFalse(referenceType1.equals(modifiedReferenceType1));
        assertFalse(referenceType1.equals(modifiedReferenceType2));
        assertFalse(modifiedReferenceType1.equals(referenceType1));
        assertFalse(modifiedReferenceType1.equals(modifiedReferenceType2));
        assertFalse(modifiedReferenceType2.equals(referenceType1));
        assertFalse(modifiedReferenceType2.equals(modifiedReferenceType1));
    }
}