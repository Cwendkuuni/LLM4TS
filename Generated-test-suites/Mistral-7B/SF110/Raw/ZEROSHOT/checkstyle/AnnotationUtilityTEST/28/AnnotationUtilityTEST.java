import static org.junit.Assert.*;
import static com.google.common.base.CharMatcher.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.annotation.MockAnnotation;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnnotationUtility.class)
public class AnnotationUtilityTest {

 private DetailAST createMockAst() {
 return PowerMockito.mock(DetailAST.class);
 }

 @Test
 public void testContainsAnnotationNullAst() {
 AnnotationUtility utility = new AnnotationUtility();
 try {
 utility.containsAnnotation(null, "TestAnnotation");
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the ast is null", e.getMessage());
 }
 }

 @Test
 public void testContainsAnnotationNullAnnotation() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 try {
 utility.containsAnnotation(ast, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the annotation is null", e.getMessage());
 }
 }

 @Test
 public void testContainsAnnotationEmptyAnnotation() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 try {
 utility.containsAnnotation(ast, " ");
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the annotation is empty or spaces", e.getMessage());
 }
 }

 @Test
 @MockAnnotation(classes = Annotation.class, value = "TestAnnotation")
 public void testContainsAnnotationWithAnnotation(Annotation annotation) {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);
 DetailAST annotationNode = PowerMockito.mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotationNode);

 PowerMockito.whenNew(DetailAST.class)
 .withParameters(TokenTypes.ANNOTATION, "TestAnnotation")
 .thenReturn(annotationNode);

 PowerMockito.when(annotationHolder.branchContains(159)).thenReturn(true);

 assertTrue(utility.containsAnnotation(ast));
 }

 @Test
 public void testContainsAnnotationWithoutAnnotation() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
 assertFalse(utility.containsAnnotation(ast));
 }

 @Test
 public void testGetAnnotationNullAst() {
 AnnotationUtility utility = new AnnotationUtility();
 try {
 utility.getAnnotation(null, "TestAnnotation");
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the ast is null", e.getMessage());
 }
 }

 @Test
 @MockAnnotation(classes = Annotation.class, value = "TestAnnotation")
 public void testGetAnnotationWithAnnotation(Annotation annotation) {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);
 DetailAST annotationNode = PowerMockito.mock(DetailAST.class);
 DetailAST nameNode = PowerMockito.mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
 when(annotationNode.getFirstChild()).thenReturn(nameNode);
 when(nameNode.getText()).thenReturn("TestAnnotation");

 DetailAST result = utility.getAnnotation(ast, "TestAnnotation");
 assertEquals(annotationNode, result);
 }

 @Test
 public void testGetAnnotationNullAnnotation() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 try {
 utility.getAnnotation(ast, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the annotation is null", e.getMessage());
 }
 }

 @Test
 public void testGetAnnotationEmptyAnnotationName() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 try {
 utility.getAnnotation(ast, " ");
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 assertEquals("the annotation is empty or spaces", e.getMessage());
 }
 }

 @Test
 @MockAnnotation(classes = Annotation.class, value = "TestAnnotation")
 public void testGetAnnotationWithMultipleAnnotations(Annotation annotation) {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 DetailAST annotationHolder = PowerMockito.mock(DetailAST.class);
 DetailAST annotationNode1 = PowerMockito.mock(DetailAST.class);
 DetailAST annotationNode2 = PowerMockito.mock(DetailAST.class);
 DetailAST nameNode1 = PowerMockito.mock(DetailAST.class);
 DetailAST nameNode2 = PowerMockito.mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild())
 .thenReturn(annotationNode1)
 .thenReturn(annotationNode2);
 when(annotationNode1.getFirstChild()).thenReturn(nameNode1);
 when(nameNode1.getText()).thenReturn("TestAnnotation1");
 when(annotationNode2.getFirstChild()).thenReturn(nameNode2);
 when(nameNode2.getText()).thenReturn("TestAnnotation2");

 DetailAST result = utility.getAnnotation(ast, "TestAnnotation");
 assertNull(result);

 DetailAST result1 = utility.getAnnotation(ast, "TestAnnotation1");
 assertEquals(annotationNode1, result1);

 DetailAST result2 = utility.getAnnotation(ast, "TestAnnotation2");
 assertEquals(annotationNode2, result2);
 }

 @Test
 public void testGetAnnotationWithoutAnnotation() {
 AnnotationUtility utility = new AnnotationUtility();
 DetailAST ast = createMockAst();
 when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);
 assertNull(utility.getAnnotation(ast, "TestAnnotation"));
 }
}