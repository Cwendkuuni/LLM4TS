import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = MyAnnotation.class;
        ReflectionSearch search = new ReflectionSearch(clazz);
        assertTrue(search.hasAnnotation("MyAnnotation"));
    }
}

// MyAnnotation is a custom annotation class
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {}