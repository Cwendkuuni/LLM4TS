import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> listClass = List.class;
        Class<LinkedList> linkedListClass = LinkedList.class;

        // Add mapping for List to LinkedList
        resolver.addMapping(listClass, linkedListClass);

        // Check if the mapping was added correctly
        Class<?> mappedClass = resolver._mappings.get(new ClassKey(listClass));
        assertNotNull(mappedClass);
        assertEquals(linkedListClass, mappedClass);
    }
}