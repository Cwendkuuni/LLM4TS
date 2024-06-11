import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ClassIdResolver;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertTrue(bindings.isEmpty());
    }

    @Test
    public void testSingleTypeParameter() {
        Class<List<String>> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructParametricType(List.class, String.class));
        Assert.assertEquals("String", bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.isEmpty());
    }

    @Test
    public void testTwoTypeParameters() {
        Class<Map<String, Integer>> clazz = Map.class;
        TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
        String keyTypeName = typeParameters[0].getName();
        String valueTypeName = typeParameters[1].getName();

        TypeBindings bindings = TypeBindings.create(clazz,
                JavaType.constructParametricType(Map.class, String.class, Integer.class));

        Assert.assertEquals(keyTypeName, bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0));
        Assert.assertEquals(valueTypeName, bindings.getBoundName(1));
        Assert.assertEquals(Integer.class, bindings.getBoundType(1));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeParametersCount() {
        Class<List<String>> clazz = List.class;
        TypeBindings.create(clazz, JavaType.constructParametricType(List.class, String.class, Integer.class, Double.class));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<List<String>> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructParametricType(List.class, String.class));

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        Assert.assertEquals("X", bindingsWithUnboundVariable.getBoundName(2));
        Assert.assertNull(bindingsWithUnboundVariable.getBoundType(2));
        Assert.assertTrue(bindingsWithUnboundVariable.hasUnbound("X"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindBoundTypeWithUnboundVariable() {
        Class<List<String>> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructParametricType(List.class, String.class));

        bindings = bindings.withUnboundVariable("X");
        JavaType boundType = bindings.findBoundType("X");
        Assert.assertNull(boundType);
    }

    @Test
    public void testFindBoundTypeWithBoundVariable() {
        Class<List<String>> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, JavaType.constructParametricType(List.class, String.class));

        JavaType boundType = bindings.findBoundType("0");
        Assert.assertEquals(String.class, boundType);
    }

    @Test
    public void testEqualsAndHashCode() {
        Class<List<String>> clazz1 = List.class;
        JavaType typeArg1 = JavaType.constructParametricType(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(clazz1, typeArg1);

        Class<List<String>> clazz2 = List.class;
        JavaType typeArg2 = JavaType.constructParametricType(List.class, String.class);
        TypeBindings bindings2 = TypeBindings.create(clazz2, typeArg2);

        Class<Map<String, String>> clazz3 = Map.class;
        JavaType typeArg3 = JavaType.constructParametricType(Map.class, String.class, String.class);
        TypeBindings bindings3 = TypeBindings.create(clazz3, typeArg3);

        Assert.assertEquals(bindings1, bindings1);
        Assert.assertEquals(bindings1, bindings1);
        Assert.assertEquals(bindings1, bindings2);

        Assert.assertNotEquals(bindings1, bindings3);
        Assert.assertNotEquals(bindings2, bindings3);

        Assert.assertEquals(bindings1.hashCode(), bindings1.hashCode());
        Assert.assertEquals(bindings1.hashCode(), bindings1.hashCode());
        Assert.assertEquals(bindings1.hashCode(), bindings2.hashCode());

        Assert.assertNotEquals(bindings1.hashCode(), bindings3.hashCode());
        Assert.assertNotEquals(bindings2.hashCode(), bindings3.hashCode());
    }
}