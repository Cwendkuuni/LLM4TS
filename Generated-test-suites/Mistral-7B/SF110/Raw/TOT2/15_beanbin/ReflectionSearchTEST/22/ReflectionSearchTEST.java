import org.junit.Test;
import static org.junit.Assert.assertTrue;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Query;
import java.lang.annotation.Annotation;
import java.lang.reflect.Class;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        Class clazz = Annotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "Annotation";
        assertTrue(reflectionSearch.hasAnnotation(term));
    }
}