import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
    }
}