import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() throws Exception {
        Class clazz = MyAnnotationClass.class; // replace with the actual class to test
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test with a matching annotation
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

        // Test with a non-existent annotation
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));

        // Test with a wildcard annotation
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation*"));
    }
}