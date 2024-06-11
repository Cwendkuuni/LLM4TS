import org.junit.Assert.*;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMapping() {
        Class<List<Integer>> listClass = List.class.getTypeParameters()[0].getType();
        Class<LinkedList<Integer>> linkedListClass = LinkedList.class;
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, linkedListClass);

        Class<? extends List<Integer>> mappedList = resolver.findTypeMapping(null, new ClassType(listClass)).rawClass();
        Assert.assertEquals(LinkedList.class, mappedList);
    }
}