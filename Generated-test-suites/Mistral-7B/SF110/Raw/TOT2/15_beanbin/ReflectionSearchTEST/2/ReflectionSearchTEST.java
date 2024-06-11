import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_AnnotationPresent() {
        Class clazz = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        boolean result = reflectionSearch.hasAnnotation("MyAnnotation");
        assertTrue(result);
    }
}