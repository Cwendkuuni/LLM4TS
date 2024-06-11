import static org.junit.Assert.assertTrue;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST sampleAst = new DetailAST(); // Create a mock DetailAST
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(sampleAst, annotation));
    }
}