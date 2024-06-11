import org.junit.Test;
import static org.junit.Assert.assertTrue;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        Class clazz = String.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));
    }
}