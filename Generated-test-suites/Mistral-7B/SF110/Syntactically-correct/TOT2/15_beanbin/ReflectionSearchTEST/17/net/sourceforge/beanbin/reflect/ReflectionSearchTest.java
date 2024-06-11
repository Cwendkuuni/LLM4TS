import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);

        // Positive test
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));

        // Negative test
        assertFalse(reflectionSearch.hasAnnotation("NotFoundAnnotation"));
    }
}