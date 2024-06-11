import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = MyAnnotation.class; // replace MyAnnotation with the class containing the annotation you want to test
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        Class clazz = MyInterface.class; // replace MyInterface with an interface that has a subclass with the annotation you want to test
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("*Interface"));
    }

    @Test
    public void testHasAnnotationFalse() {
        Class clazz = Object.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
    }
}