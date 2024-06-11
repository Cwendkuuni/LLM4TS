import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

  @Test
  public void testContainsAnnotationWithValidAnnotation() {
    DetailAST ast = new DetailAST(); // Create a valid DetailAST object
    String annotation = "ValidAnnotation";
    assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
  }

  @Test
  public void testContainsAnnotationWithNullAst() {
    DetailAST ast = null;
    String annotation = "ValidAnnotation";
    try {
      AnnotationUtility.containsAnnotation(ast, annotation);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testContainsAnnotationWithNullAnnotation() {
    DetailAST ast = new DetailAST();
    String annotation = null;
    try {
      AnnotationUtility.containsAnnotation(ast, annotation);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
    DetailAST ast = new DetailAST();
    String annotation = " ";
    try {
      AnnotationUtility.containsAnnotation(ast, annotation);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }
}