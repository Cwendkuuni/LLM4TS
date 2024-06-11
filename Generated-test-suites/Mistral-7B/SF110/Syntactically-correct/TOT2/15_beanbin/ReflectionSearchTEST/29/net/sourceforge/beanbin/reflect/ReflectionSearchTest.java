import org.junit.Test;
import static org.junit.Assert.assertTrue;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.MyAnnotation;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test(expected = BeanBinException.class)
    public void testHasAnnotationWithException() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Exception.class);
        reflectionSearch.hasAnnotation("Exception");
    }

    @Test
    public void testHasAnnotationWithEmptyClass() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationWithNoAnnotationClass() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationWithDifferentPackage() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnotherPackage.MyAnnotation.class);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(WildcardAnnotation.class);
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
    }
}