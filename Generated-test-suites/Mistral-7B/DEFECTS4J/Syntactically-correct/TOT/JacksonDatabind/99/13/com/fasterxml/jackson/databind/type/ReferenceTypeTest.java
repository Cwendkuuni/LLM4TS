import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType instanceof ReferenceType);
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = {};
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, bindings, superClass, superInterfaces, refType);

        ReferenceType referenceTypeWithHandler = referenceType.withContentTypeHandler(new IdentityContentTypeHandler());

        assertEquals(stringClass, referenceTypeWithHandler.getRawClass());
        assertEquals(Integer.class, referenceTypeWithHandler.getContentType().getRawClass());
        assertEquals(IdentityContentTypeHandler.class, referenceTypeWithHandler.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = {};
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, bindings, superClass, superInterfaces, refType);

        ReferenceType referenceTypeWithHandler = referenceType.withValueHandler(new IdentityValueHandler());

        assertEquals(stringClass, referenceTypeWithHandler.getRawClass());
        assertEquals(Integer.class, referenceTypeWithHandler.getContentType().getRawClass());
        assertEquals(IdentityValueHandler.class, referenceTypeWithHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeFromInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test(expected = NullPointerException.class)
    public void testReferenceTypeUpgradeFromNullReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }
}