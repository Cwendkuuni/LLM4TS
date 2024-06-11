import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);

        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(int[].class, baseType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = int[].class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), null, null, referencedType);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = int[].class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        JavaType newReferenceType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));

        assertEquals(Integer.class, newReferenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = int[].class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);

        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = int[].class;
        JavaType referencedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }
}