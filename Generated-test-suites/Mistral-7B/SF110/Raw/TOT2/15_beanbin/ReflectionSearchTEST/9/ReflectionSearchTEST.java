import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = ReflectionSearchTest.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test for a method annotation
        assertTrue(reflectionSearch.hasAnnotation("AnnotationA"));

        // Test for a class annotation
        assertTrue(reflectionSearch.hasAnnotation("Test"));

        // Test for a non-existent term
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }
}