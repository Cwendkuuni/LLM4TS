import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = null; // Replace with a suitable class
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("AnnotationName");
        assertFalse(result);

        clazz = SomeClassWithAnnotation.class; // Replace with a class having the specified annotation
        reflectionSearch = new ReflectionSearch(clazz);
        result = reflectionSearch.hasAnnotation("AnnotationName");
        assertTrue(result);
    }
}