import static org.junit.Assert.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
    }
}