import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilityTest {

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = new DetailAST(); // valid DetailAST instance
 String annotation = "java.lang.Annotation";
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "java.lang.Annotation";
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = new DetailAST();
 String annotation = null;
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
 DetailAST ast = new DetailAST();
 String annotation = " ";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotationHolder() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 ast.setChild(annotationHolder);
 assertTrue(AnnotationUtility.containsAnnotation(ast, "java.lang.Annotation"));
 }

 @Test
 public void testGetAnnotationHolderWithValidAst() {
 DetailAST ast = new DetailAST();
 DetailAST expectedAnnotationHolder = new DetailAST();
 expectedAnnotationHolder.setType(TokenTypes.ANNOTATION);
 ast.setChild(expectedAnnotationHolder);
 DetailAST actualAnnotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(expectedAnnotationHolder, actualAnnotationHolder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 DetailAST ast = null;
 AnnotationUtility.getAnnotationHolder(ast);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST expectedAnnotation = new DetailAST();
 expectedAnnotation.setType(TokenTypes.ANNOTATION);
 annotation.setType(TokenTypes.ANNOTATION);
 annotationHolder.setChild(expectedAnnotation);
 annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DECLARATION);
 ast.setChild(annotationHolder);
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "java.lang.Annotation");
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "java.lang.Annotation";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = null;
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 String annotation = " ";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotationName() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST expectedAnnotation = new DetailAST();
 expectedAnnotation.setType(TokenTypes.ANNOTATION);
 annotation.setType(TokenTypes.ANNOTATION);
 annotationHolder.setChild(expectedAnnotation);
 annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DECLARATION);
 DetailAST annotationName = new DetailAST();
 annotationName.setText("java.lang.Annotation");
 annotation.setChild(annotationName);
 ast.setChild(annotationHolder);
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "java.lang.Annotation");
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetAnnotationWithValidAstAndNonExistentAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotation = new DetailAST();
 DetailAST expectedAnnotation = new DetailAST();
 expectedAnnotation.setType(TokenTypes.ANNOTATION);
 annotation.setType(TokenTypes.ANNOTATION);
 annotationHolder.setChild(expectedAnnotation);
 annotationHolder.setType(TokenTypes.ANNOTATION_TYPE_DECLARATION);
 ast.setChild(annotationHolder);
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "java.lang.OtherAnnotation");
 assertNull(actualAnnotation);
 }

}