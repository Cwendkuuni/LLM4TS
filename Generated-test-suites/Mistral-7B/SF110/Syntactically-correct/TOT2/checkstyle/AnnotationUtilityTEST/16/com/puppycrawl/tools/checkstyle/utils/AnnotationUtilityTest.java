import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotation_WithValidParam_ReturnsTrue() {
        DetailAST ast = mock(DetailAST.class);
        String annotation = "ValidAnnotation";
        when(ast.getType()).thenReturn(1); // Return an arbitrary valid type
        when(ast.findFirstToken(158)).thenReturn(mock(DetailAST.class));

        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        DetailAST annotationNode = mock(DetailAST.class);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
        when(annotationNode.getType()).thenReturn(159);
        when(annotationNode.getFirstChild()).thenReturn(mock(DetailAST.class));
        when(AnnotationUtility.getAnnotation(ast, annotation)).thenReturn(annotationNode);

        DetailAST annotationHolderInAST = AnnotationUtility.getAnnotationHolder(ast);
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotation_WithNullAst_ThrowsIllegalArgumentException() {
        DetailAST ast = null;

        try {
            AnnotationUtility.containsAnnotation(ast, "AnnotationName");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WithNullAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();

        try {
            AnnotationUtility.containsAnnotation(ast, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is null", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WithEmptyOrWhitespaceAnnotation_ThrowsIllegalArgumentException() {
        DetailAST ast = new DetailAST();

        try {
            AnnotationUtility.containsAnnotation(ast, " ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test
    public void testContainsAnnotation_WithInvalidAstType_ReturnsFalse() {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(2); // Return an invalid type

        assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotation_WithAnnotationHolderNull_ReturnsFalse() {
        DetailAST ast = new DetailAST();

        DetailAST annotationHolder = AnnotationUtility.getAnnotationHolder(ast);
        assertNull(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }

    @Test
    public void testContainsAnnotation_WithNoMatchingAnnotation_ReturnsFalse() {
        DetailAST ast = new DetailAST();
        DetailAST annotationHolder = new DetailAST();
        when(ast.getType()).thenReturn(1);
        when(ast.findFirstToken(158)).thenReturn(annotationHolder);

        assertFalse(AnnotationUtility.containsAnnotation(ast, "AnnotationName"));
    }
}