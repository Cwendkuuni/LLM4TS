import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        Class annotatedClass = AnnotatedClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(annotatedClass);
        assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
        assertTrue(reflectionSearch.hasAnnotation("Annotation1@"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }
}