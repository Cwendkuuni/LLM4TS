import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
    private SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

    @Test
    public void testAddMapping() {
        Class<List<Integer>> listInt = List.class.getTypeParameters()[0].getType();
        Class<LinkedList<Integer>> linkedListInt = LinkedList.class;
        resolver.addMapping(List.class, LinkedList.class);
        assertNotNull(resolver._mappings.get(new ClassKey(listInt)));
        assertTrue(resolver._mappings.get(new ClassKey(listInt)) == linkedListInt);
    }
}