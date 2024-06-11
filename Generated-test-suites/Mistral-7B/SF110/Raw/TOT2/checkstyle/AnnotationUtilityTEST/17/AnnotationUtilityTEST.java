import static org.junit.Assert.assertTrue;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming a valid DetailAST can be created
        assertTrue(AnnotationUtility.containsAnnotation(ast, "SomeAnnotation"));
    }
}