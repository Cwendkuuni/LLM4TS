import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST(); // Create a valid DetailAST instance
        String annotation = "MyAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        DetailAST ast = null;
        String annotation = "MyAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation); // This will throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAnnotation() {
        DetailAST ast = new DetailAST();
        String annotation = null;
        AnnotationUtility.containsAnnotation(ast, annotation); // This will throw an exception
    }
}