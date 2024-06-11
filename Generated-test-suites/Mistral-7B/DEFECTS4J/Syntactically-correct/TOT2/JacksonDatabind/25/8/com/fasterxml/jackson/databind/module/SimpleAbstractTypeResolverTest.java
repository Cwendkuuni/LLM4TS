import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<Collection> superType = Collection.class;
        Class<LinkedList> subType = LinkedList.class;

        SimpleAbstractTypeResolver resolverInstance = resolver.addMapping(superType, subType);
        Class<? extends Collection> actualSubType = resolverInstance._mappings.get(new ClassKey(superType));

        assertEquals(subType, actualSubType);
    }
}