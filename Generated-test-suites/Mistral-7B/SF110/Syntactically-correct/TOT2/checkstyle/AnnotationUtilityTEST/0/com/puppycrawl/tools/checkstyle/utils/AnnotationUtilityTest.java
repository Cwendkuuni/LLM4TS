import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class AnnotationUtilityTest {

  @Test
  public void testContainsAnnotationWithValidAstAndAnnotation() {
    DetailAST ast = new DetailAST(); // assume a valid DetailAST instance
    String annotation = "ValidAnnotation";
    assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
  }

  @Test
  public void testContainsAnnotationWithValidAst() {
    DetailAST ast = new DetailAST(); // assume a valid DetailAST instance
    assertTrue(AnnotationUtility.containsAnnotation(ast));
  }

  @Test
  public void testGetAnnotationHolderWithValidAst() {
    DetailAST ast = new DetailAST(); // assume a valid DetailAST instance
    DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
    assertNotNull(annotationHolder);
  }

  @Test
  public void testGetAnnotationWithValidAstAndAnnotation() {
    DetailAST ast = new DetailAST(); // assume a valid DetailAST instance
    String annotation = "ValidAnnotation";
    DetailAST expectedAnnotation = new DetailAST(); // assume a valid DetailAST instance
    DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, annotation);
    assertEquals(expectedAnnotation, actualAnnotation);
  }
}