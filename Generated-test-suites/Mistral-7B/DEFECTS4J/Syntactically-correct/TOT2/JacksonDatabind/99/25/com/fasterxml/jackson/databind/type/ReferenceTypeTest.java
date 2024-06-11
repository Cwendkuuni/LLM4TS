import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = JavaType.construct(Integer.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(intClass, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(Integer.class, referenceType.getClass());
        assertEquals(bindings, referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertEquals(false, referenceType.isStatic());
    }
}