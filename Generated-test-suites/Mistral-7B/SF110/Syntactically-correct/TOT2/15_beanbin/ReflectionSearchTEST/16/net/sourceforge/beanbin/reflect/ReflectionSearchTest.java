import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = MyAnnotatedClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "MyAnnotation";
        assertTrue(reflectionSearch.hasAnnotation(term));
    }
}