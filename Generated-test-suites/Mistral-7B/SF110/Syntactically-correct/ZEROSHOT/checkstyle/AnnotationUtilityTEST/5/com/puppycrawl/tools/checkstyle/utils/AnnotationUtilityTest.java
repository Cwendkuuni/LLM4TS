import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class AnnotationUtilityTest {

 @Test
 public void testContainsAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 String annotation = "annotationName";
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(5)).thenReturn(mock(DetailAST.class));

 assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAst() {
 DetailAST ast = null;
 assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 String annotation = null;
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(5)).thenReturn(mock(DetailAST.class));

 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithEmptyOrWhitespaceAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 String annotation = " ";
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(5)).thenReturn(mock(DetailAST.class));

 assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
 }

 @Test
 public void testContainsAnnotationWithValidAst() {
 DetailAST ast = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(5)).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(158).getFirstChild()).thenReturn(mock(DetailAST.class));
 when(ast.findFirstToken(5).getFirstChild()).thenReturn(mock(DetailAST.class));

 DetailAST annotationHolder = mock(DetailAST.class);
 when(annotationHolder.getFirstChild()).thenReturn(mock(DetailAST.class));
 when(annotationHolder.branchContains(159)).thenReturn(true);
 when(ast.findFirstToken(5).getParent()).thenReturn(annotationHolder);

 assertTrue(AnnotationUtility.containsAnnotation(ast));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsAnnotationWithNullAstInGetAnnotationHolder() {
 DetailAST ast = null;
 assertNull(AnnotationUtility.getAnnotationHolder(ast));
 }

 @Test
 public void testGetAnnotationHolderWithType155() {
 DetailAST ast = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));

 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 checkNotNull(annotationHolder);
 assertEquals(TokenTypes.ANNOTATION, annotationHolder.getType());
 }

 @Test
 public void testGetAnnotationHolderWithType16() {
 DetailAST ast = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION);
 when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));

 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 checkNotNull(annotationHolder);
 assertEquals(TokenTypes.ANNOTATION_DEF, annotationHolder.getType());
 }

 @Test
 public void testGetAnnotationHolderWithOtherType() {
 DetailAST ast = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.CLASS_DEF);

 DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
 assertNull(annotationHolder);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 DetailAST annotationHolder = mock(DetailAST.class);
 DetailAST annotation = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotation);
 when(annotation.getType()).thenReturn(TokenTypes.ANNOTATION);
 when(annotation.getFirstChild()).thenReturn(mock(DetailAST.class));
 when(annotation.getFirstChild().getText()).thenReturn("annotationName");

 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
 checkNotNull(result);
 assertEquals(annotation, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAst() {
 DetailAST ast = null;
 assertNull(AnnotationUtility.getAnnotation(ast, "annotationName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithNullAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 String annotation = null;
 DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 DetailAST annotationHolder = mock(DetailAST.class);
 DetailAST annotation = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotation);
 when(annotation.getFirstChild()).thenReturn(mock(DetailAST.class));
 when(annotation.getFirstChild().getText()).thenReturn(" ");

 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
 assertNull(result);
 }

 @Test
 public void testGetAnnotationWithValidAstAndAnnotationName() {
 DetailAST ast = mock(DetailAST.class);
 DetailAST annotationHolder = mock(DetailAST.class);
 DetailAST annotation = mock(DetailAST.class);
 DetailAST annotationName = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotation);
 when(annotation.getFirstChild()).thenReturn(annotationName);
 when(annotationName.getText()).thenReturn("annotationName");
 when(annotationName.getNextSibling()).thenReturn(mock(DetailAST.class));
 DetailAST nextAnnotation = mock(DetailAST.class);
 when(annotationName.getNextSibling()).thenReturn(nextAnnotation);

 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
 checkNotNull(result);
 assertEquals(annotation, result);
 }

 @Test
 public void testGetAnnotationWithValidAstButNoMatchingAnnotation() {
 DetailAST ast = mock(DetailAST.class);
 DetailAST annotationHolder = mock(DetailAST.class);
 DetailAST annotation = mock(DetailAST.class);
 DetailAST annotationName = mock(DetailAST.class);
 DetailAST annotationName2 = mock(DetailAST.class);
 when(ast.getType()).thenReturn(TokenTypes.ANNOTATION_DEF);
 when(ast.findFirstToken(158)).thenReturn(annotationHolder);
 when(annotationHolder.getFirstChild()).thenReturn(annotation);
 when(annotation.getFirstChild()).thenReturn(annotationName);
 when(annotationName.getText()).thenReturn("notTheAnnotationName");
 when(annotationName.getNextSibling()).thenReturn(annotationName2);

 DetailAST result = AnnotationUtility.getAnnotation(ast, "annotationName");
 assertNull(result);
 }
}