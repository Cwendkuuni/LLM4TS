import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;
import org.junit.Test;
import org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        Assert.assertNotNull(empty);
        Assert.assertEquals(0, empty.size());
        Assert.assertFalse(empty.hasUnbound("T"));
    }

    @Test
    public void testCreateSingleTypeVariable() {
        Class<List<Integer>> listOfInt = new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}}.getClass();
        JavaType javaType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listOfInt, javaType);
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("T", bindings.getBoundName(0));
        Assert.assertEquals(Integer.class, bindings.getBoundType(0));
        Assert.assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateTwoTypeVariables() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToListInt, keyType, valueType);
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0));
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(List.class, bindings.getBoundType(1).getRawClass());
        Assert.assertEquals(Integer.class, bindings.getBoundType(1).getContentType());
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        Class<List<Integer>> listOfInt = new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}}.getClass();
        JavaType javaTypeInt = JavaType.constructParametrizedType(Integer.class, Object.class);
        TypeBindings bindings = TypeBindings.create(listOfInt, javaTypeInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNames() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToListInt, valueType, keyType);
    }

    @Test
    public void testCreateIfNeededSingleTypeVariable() {
        Class<List<Integer>> listOfInt = new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}}.getClass();
        JavaType javaType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfInt, javaType);
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("T", bindings.getBoundName(0));
        Assert.assertEquals(Integer.class, bindings.getBoundType(0));
        Assert.assertFalse(bindings.hasUnbound("T"));
    }

    @Test
    public void testCreateIfNeededTwoTypeVariables() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapOfStringToListInt, keyType, valueType);
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(String.class, bindings.getBoundType(0));
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(List.class, bindings.getBoundType(1).getRawClass());
        Assert.assertEquals(Integer.class, bindings.getBoundType(1).getContentType());
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToListInt, keyType, valueType);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("V2");
        Assert.assertEquals(3, bindingsWithUnbound.size());
        Assert.assertEquals("V2", bindingsWithUnbound.getBoundName(2));
        Assert.assertNull(bindingsWithUnbound.getBoundType(2));
        Assert.assertTrue(bindingsWithUnbound.hasUnbound("V2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindBoundTypeUnknownTypeVariable() {
        TypeBindings bindings = new TypeBindings(new String[]{"T"}, new JavaType[]{Integer.class});
        bindings.findBoundType("V");
    }

    @Test
    public void testFindBoundTypeKnownTypeVariable() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToListInt, keyType, valueType);
        JavaType boundType = bindings.findBoundType("K");
        Assert.assertEquals(String.class, boundType);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindBoundTypeMismatchedTypeVariable() {
        Class<Map<String, List<Integer>>> mapOfStringToListInt = new HashMap<Map<String, List<Integer>>>(){{put("", new ArrayList<List<Integer>>(){{add(new ArrayList<Integer>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, Double.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToListInt, keyType, valueType);
        bindings.findBoundType("K");
    }

    @Test
    public void testFindBoundTypeRecursiveType() {
        Class<Map<String, List<TypeBindings>>> mapOfStringToTypeBindings = new HashMap<Map<String, List<TypeBindings>>>(){{put("", new ArrayList<List<TypeBindings>>(){{add(new ArrayList<TypeBindings>());}});}}.getClass();
        JavaType keyType = JavaType.constructParametrizedType(String.class, Object.class);
        JavaType valueType = JavaType.constructParametrizedType(List.class, TypeBindings.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringToTypeBindings, keyType, valueType);
        JavaType boundType = bindings.findBoundType("V");
        Assert.assertEquals(TypeBindings.class, boundType.getRawClass());
    }
}