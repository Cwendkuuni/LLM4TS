import static org.junit.Assert.*;
import static com.google.common.base.CharMatcher.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

 private DetailAST createTestAst() {
 return new DetailAST() {
 // A dummy implementation of DetailAST for testing purposes
 };
 }

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = new DetailAST();
 String annotation = "MyAnnotation";
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 ast.setChild(annotationHolder);
 DetailAST annotationElement = new DetailAST();
 annotationElement.setType(TokenTypes.ANNOTATION_TYPE_IDENTIFIER);
 annotationElement.setText("MyAnnotation");
 annotationHolder.setFirstChild(annotationElement);
 DetailAST element = new DetailAST();
 element.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
 element.setText("value");
 annotationElement.setNextSibling(element);

 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 AnnotationUtility.containsAnnotation(null, "Annotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = "";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));

 String annotationWithSpaces = " MyAnnotation ";
 assertFalse(AnnotationUtility.containsAnnotation(ast, annotationWithSpaces));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 assertFalse(AnnotationUtility.containsAnnotation(ast, null));
 }

 @Test
 public void testContainsAnnotationWithValidAst() {
 DetailAST ast = createTestAst();
 assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithNullAst() {
 AnnotationUtility.getAnnotationHolder(null);
 }

 @Test
 public void testGetAnnotationHolderWithAnnotationTypeIdentifier() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 annotationHolder.setType(TokenTypes.ANNOTATION);
 DetailAST annotationTypeIdentifier = new DetailAST();
 annotationTypeIdentifier.setType(TokenTypes.ANNOTATION_TYPE_IDENTIFIER);
 annotationTypeIdentifier.setText("MyAnnotation");
 annotationHolder.setFirstChild(annotationTypeIdentifier);
 ast.setChild(annotationHolder);

 DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
 assertEquals(annotationHolder, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationHolderWithInvalidAstType() {
 DetailAST ast = new DetailAST();
 ast.setType(TokenTypes.COMMENT);

 try {
 AnnotationUtility.getAnnotationHolder(ast);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotationTypeIdentifier = new DetailAST();
 annotationTypeIdentifier.setType(TokenTypes.ANNOTATION_TYPE_IDENTIFIER);
 annotationTypeIdentifier.setText("MyAnnotation");
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setFirstChild(annotationTypeIdentifier);
 DetailAST annotationElement = new DetailAST();
 annotationElement.setType(TokenTypes.ANNOTATION_MEMBER_DECLARATION);
 annotationElement.setText("value");
 DetailAST annotationMemberValue = new DetailAST();
 annotationMemberValue.setType(TokenTypes.ANNOTATION_MEMBER_VALUE);
 annotationMemberValue.setText("value");
 annotationElement.setNextSibling(annotationMemberValue);
 annotationHolder.setFirstChild(annotationElement);
 DetailAST result = AnnotationUtility.getAnnotation(ast, "MyAnnotation");
 assertEquals(annotationElement, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 AnnotationUtility.getAnnotation(null, "Annotation");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = createTestAst();
 AnnotationUtility.getAnnotation(ast, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
 DetailAST ast = createTestAst();
 String annotation = "";
 AnnotationUtility.getAnnotation(ast, annotation);

 String annotationWithSpaces = " MyAnnotation ";
 AnnotationUtility.getAnnotation(ast, annotationWithSpaces);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithInvalidAstType() {
 DetailAST ast = new DetailAST();
 ast.setType(TokenTypes.COMMENT);
 String annotation = "MyAnnotation";
 try {
 AnnotationUtility.getAnnotation(ast, annotation);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testGetAnnotationWithValidAstAndAnnotationNotFound() {
 DetailAST ast = new DetailAST();
 DetailAST annotationHolder = new DetailAST();
 DetailAST annotationTypeIdentifier = new DetailAST();
 annotationTypeIdentifier.setType(TokenTypes.ANNOTATION_TYPE_IDENTIFIER);
 annotationTypeIdentifier.setText("MyAnnotation");
 annotationHolder.setType(TokenTypes.ANNOTATION);
 annotationHolder.setFirstChild(annotationTypeIdentifier);
 ast.setChild(annotationHolder);

 DetailAST result = AnnotationUtility.getAnnotation(ast, "OtherAnnotation");
 assertNull(result);
 }
}