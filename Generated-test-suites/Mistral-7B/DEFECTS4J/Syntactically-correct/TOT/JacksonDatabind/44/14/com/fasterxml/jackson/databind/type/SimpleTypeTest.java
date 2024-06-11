import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        JavaType javaType = new SimpleType(String.class);
        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithTypeParameters() {
        JavaType javaType = new SimpleType(List.class, TypeBindings.getDefaultInstance(), null, null);
        assertEquals(List.class, javaType.getRawClass());
        assertArrayEquals(new Class[]{Integer.class}, javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        JavaType javaType = new SimpleType(Number.class, null, Integer.class, null);
        assertEquals(Number.class, javaType.getRawClass());
        assertNull(javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
        assertEquals(Integer.class, javaType.getSuperClass());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        JavaType javaType = new SimpleType(Comparable.class, null, null, new JavaType[]{String.class, Serializable.class});
        assertEquals(Comparable.class, javaType.getRawClass());
        assertNull(javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
        assertArrayEquals(new JavaType[]{String.class, Serializable.class}, javaType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        JavaType javaType = new SimpleType(Integer.class, null, null, null, new NumberValueHandler(), null, false);
        assertEquals(Integer.class, javaType.getRawClass());
        assertNull(javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
        assertEquals(NumberValueHandler.class, javaType.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        JavaType javaType = new SimpleType(String.class, null, null, null, null, new StringTypeHandler(), false);
        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getTypeParameters());
        assertFalse(javaType.isContainerType());
        assertEquals(StringTypeHandler.class, javaType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapType() {
        new SimpleType(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollectionType() {
        new SimpleType(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArrayType() {
        new SimpleType(int[].class);
    }
}