import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST(); // Create a mock DetailAST
        String annotation = "annotationName";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation)); // Test with a valid annotation
        
        ast = null;
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation)); // Test with a null ast
        
        ast = new DetailAST();
        String invalidAnnotation = " ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, invalidAnnotation)); // Test with an empty or whitespace annotation
    }
}