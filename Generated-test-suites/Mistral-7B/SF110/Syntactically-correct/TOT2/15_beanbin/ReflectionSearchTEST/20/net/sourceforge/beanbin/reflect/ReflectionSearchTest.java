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

    @Test
    public void testHasAnnotation_False() {
        Class clazz = SomeClassWithoutAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "AnnotationName";
        assertFalse(reflectionSearch.hasAnnotation(term));
    }

    @Test(expected = BeanBinException.class)
    public void testHasAnnotation_Exception() {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "InvalidTerm";
        reflectionSearch.hasAnnotation(term);
    }

    @Test
    public void testHasAnnotation_EmptyString() {
        Class clazz = SomeClassWithAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "";
        assertFalse(reflectionSearch.hasAnnotation(term));
    }

    @Test(expected = NullPointerException.class)
    public void testHasAnnotation_NullTerm() {
        Class clazz = SomeClassWithAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = null;
        reflectionSearch.hasAnnotation(term);
    }
}