package net.sourceforge.beanbin.reflect;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test annotation exists
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Annotation"));

        // Test annotation does not exist
        assertFalse(reflectionSearch.hasAnnotation("nonexistent.annotation.Annotation"));

        // Test annotation name match with class name
        assertTrue(reflectionSearch.hasAnnotation(clazz.getName()));

        // Test annotation name match with class simple name
        assertTrue(reflectionSearch.hasAnnotation(clazz.getSimpleName()));

        // Test annotation name match with class package
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Object"));

        // Test annotation name does not match with class simple name
        assertFalse(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.ReflectionSearch"));
    }
}