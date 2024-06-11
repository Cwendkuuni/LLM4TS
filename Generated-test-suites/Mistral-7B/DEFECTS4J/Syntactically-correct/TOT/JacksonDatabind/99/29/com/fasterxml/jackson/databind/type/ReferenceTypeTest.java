import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType anchorType = null;

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int[].class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertSame(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType anchorType = null;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(double[].class);

        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(double[].class, newReferenceType.getContentType().getRawClass());
        assertTrue(newReferenceType.isReferenceType());
        assertNull(newReferenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithAnchorType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

        ReferenceType newReferenceType = referenceType.withTypeHandler(null);

        assertEquals(String.class, newReferenceType.getRawClass());
        assertEquals(int[].class, newReferenceType.getContentType().getRawClass());
        assertTrue(newReferenceType.isReferenceType());
        assertSame(anchorType, newReferenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeNullReferencedType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];

        ReferenceType referenceType = ReferenceType.upgradeFrom(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeNonReferenceType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testReferenceTypeToCanonicalName() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(int[].class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

        assertEquals("[reference type, class java.lang.String<>, class [I]", referenceType.buildCanonicalName());
    }
}