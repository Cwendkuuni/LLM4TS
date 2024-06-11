import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = SomeClassWithAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "AnnotationName";
        assertTrue(reflectionSearch.hasAnnotation(term));
    }
}