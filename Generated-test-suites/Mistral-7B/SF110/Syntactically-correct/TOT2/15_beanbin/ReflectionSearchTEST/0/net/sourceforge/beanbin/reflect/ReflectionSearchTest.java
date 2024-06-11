import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Annotation;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = Annotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
    }
}