import static org.junit.Assert.*;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAST() {
 // Replace this with your test DetailAST instance
 DetailAST ast = new DetailAST();
 return ast;
 }

 @Test
 public void testContainsAnnotationWithNonNullAstAndAnnotation() {
 DetailAST ast = createTestAST();
 String annotation = "TestAnnotation";
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "TestAnnotation";
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAST();
 String annotation = null;
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAST();
 String annotation = " ";
 AnnotationUtility.containsAnnotation(ast, annotation);
 }

 @Test
 public void testContainsAnnotationWithAnnotationHolder() {
 DetailAST ast = createTestAST();
 String annotation = "TestAnnotation";
 DetailAST annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation, annotationHolder));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotationHolder() {
 DetailAST ast = createTestAST();
 String annotation = "TestAnnotation";
 DetailAST annotationHolder = null;
 AnnotationUtility.containsAnnotation(ast, annotation, annotationHolder);
 }

 @Test
 public void testContainsAnnotationWithMultipleAnnotations() {
 DetailAST ast = createTestAST();
 String annotation1 = "TestAnnotation1";
 String annotation2 = "TestAnnotation2";
 DetailAST annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST child1 = createTestAST();
 child1.setType(TokenTypes.ANNOTATION);
 child1.setProperty(159, annotation1);
 DetailAST child2 = createTestAST();
 child2.setType(TokenTypes.ANNOTATION);
 child2.setProperty(159, annotation2);
 annotationHolder.addChild(child1);
 annotationHolder.addChild(child2);
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation1));
 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 DetailAST ast = null;
 String annotation = "TestAnnotation";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAST();
 String annotation = null;
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyAnnotation() {
 DetailAST ast = createTestAST();
 String annotation = " ";
 AnnotationUtility.getAnnotation(ast, annotation);
 }

 @Test(expected = NullPointerException.class)
 public void testGetAnnotationWithNullAnnotationHolder() {
 DetailAST ast = createTestAST();
 DetailAST annotationHolder = null;
 AnnotationUtility.getAnnotation(ast, "TestAnnotation", annotationHolder);
 }

 @Test
 public void testGetAnnotationWithAnnotation() {
 DetailAST ast = createTestAST();
 DetailAST annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST expectedAnnotation = createTestAST();
 expectedAnnotation.setType(TokenTypes.ANNOTATION).setProperty(159, "TestAnnotation");
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation", annotationHolder);
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetAnnotationWithDifferentAnnotation() {
 DetailAST ast = createTestAST();
 DetailAST annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST expectedAnnotation = createTestAST();
 expectedAnnotation.setType(TokenTypes.ANNOTATION).setProperty(159, "TestDifferentAnnotation");
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation", annotationHolder);
 assertNull(actualAnnotation);
 }

 @Test
 public void testGetAnnotationWithMultipleAnnotations() {
 DetailAST ast = createTestAST();
 DetailAST annotationHolder = ast.findFirstToken(TokenTypes.ANNOTATION);
 DetailAST child1 = createTestAST();
 child1.setType(TokenTypes.ANNOTATION).setProperty(159, "TestAnnotation1");
 DetailAST child2 = createTestAST();
 child2.setType(TokenTypes.ANNOTATION).setProperty(159, "TestAnnotation2");
 annotationHolder.addChild(child1);
 annotationHolder.addChild(child2);
 DetailAST expectedAnnotation = child1; // Replace this with the expected DetailAST instance
 DetailAST actualAnnotation = AnnotationUtility.getAnnotation(ast, "TestAnnotation1", annotationHolder);
 assertEquals(expectedAnnotation, actualAnnotation);
 }
}