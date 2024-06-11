import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<?> superType = Collection.class;
        Class<? extends Collection> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        assertNotNull(resolver._mappings);
        assertEquals(subType, resolver._mappings.get(new ClassKey(superType)));
    }
}