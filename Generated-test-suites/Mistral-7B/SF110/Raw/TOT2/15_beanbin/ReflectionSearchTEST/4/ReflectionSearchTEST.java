import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
    }
}