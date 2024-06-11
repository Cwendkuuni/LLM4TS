import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Integer.class));
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Integer.class, listType.getContentType());
        assertEquals(false, listType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, JavaType.of(Integer.class));
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Integer.class, listType.getContentType());
        assertEquals(false, listType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
        JavaType arrayListType = listType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, arrayListType.getRawClass());
        assertEquals(Integer.class, arrayListType.getContentType());
        assertEquals(false, arrayListType.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Integer.class));
        listType._narrow(ArrayList.class);
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Integer.class));
        JavaType stringListType = listType.withContentType(JavaType.of(String.class));
        assertEquals(List.class, stringListType.getRawClass());
        assertEquals(String.class, stringListType.getContentType());
        assertEquals(false, stringListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Double>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Double.class));
        JavaType listTypeWithHandler = listType.withTypeHandler(new Object());
        assertEquals(List.class, listTypeWithHandler.getRawClass());
        assertEquals(Double.class, listTypeWithHandler.getContentType());
        assertNotNull(listTypeWithHandler.getTypeHandler());
        assertEquals(false, listTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Float>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Float.class));
        JavaType listTypeWithContentHandler = listType.withContentTypeHandler(new Object());
        assertEquals(List.class, listTypeWithContentHandler.getRawClass());
        assertEquals(Float.class, listTypeWithContentHandler.getContentType().getRawClass());
        assertNotNull(listTypeWithContentHandler.getContentType().getTypeHandler());
        assertEquals(false, listTypeWithContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Character>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Character.class));
        JavaType listTypeWithHandler = listType.withValueHandler(new Object());
        assertEquals(List.class, listTypeWithHandler.getRawClass());
        assertEquals(Character.class, listTypeWithHandler.getContentType());
        assertNotNull(listTypeWithHandler.getValueHandler());
        assertEquals(false, listTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Short>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Short.class));
        JavaType listTypeWithContentHandler = listType.withContentValueHandler(new Object());
        assertEquals(List.class, listTypeWithContentHandler.getRawClass());
        assertEquals(Short.class, listTypeWithContentHandler.getContentType().getRawClass());
        assertNotNull(listTypeWithContentHandler.getContentType().getValueHandler());
        assertEquals(false, listTypeWithContentHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Byte>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, new TypeBindings(), null, null, JavaType.of(Byte.class));
        JavaType listTypeStatic = listType.withStaticTyping();
        assertEquals(List.class, listTypeStatic.getRawClass());
        assertEquals(Byte.class, listTypeStatic.getContentType());
        assertNull(listTypeStatic.getValueHandler());
        assertNull(listTypeStatic.getTypeHandler());
        assertTrue(listTypeStatic.isStatic());
    }

    @Test
    public void testRefine() {
        Class<LinkedList<Long>> linkedListClass = LinkedList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, JavaType.of(Long.class));
        JavaType linkedListType = listType.refine(linkedListClass, new TypeBindings(), null, null);
        assertEquals(LinkedList.class, linkedListType.getRawClass());
        assertEquals(Long.class, linkedListType.getContentType());
        assertEquals(false, linkedListType.isStatic());
    }
}