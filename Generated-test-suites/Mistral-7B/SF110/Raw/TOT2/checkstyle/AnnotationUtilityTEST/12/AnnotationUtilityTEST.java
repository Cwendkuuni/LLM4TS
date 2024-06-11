import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithValidAstAndAnnotation() {
        // ...
    }

    @Test
    public void testContainsAnnotationWithValidAstButNoAnnotation() {
        // ...
    }

    @Test
    public void testContainsAnnotationWithNullAst() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test
    public void testContainsAnnotationWithEmptyOrSpacesAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
        assertFalse(AnnotationUtility.containsAnnotation(ast, " "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstButValidAnnotation() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithValidAstAndEmptyOrSpacesAnnotation() {
        DetailAST ast = mock(DetailAST.class);

        AnnotationUtility.containsAnnotation(ast, "");
        AnnotationUtility.containsAnnotation(ast, " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullAstAndEmptyOrSpacesAnnotation() {
        AnnotationUtility.containsAnnotation(null, "");
        AnnotationUtility.containsAnnotation(null, " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithInvalidAstType() {
        DetailAST ast = mock(DetailAST.class);
        when(getAnnotationHolder(ast).getType()).thenReturn(123);

        try {
            AnnotationUtility.containsAnnotation(ast, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationHolderWithNullAst() {
        try {
            AnnotationUtility.getAnnotationHolder(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationWithNullAst() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST ast = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);

        try {
            AnnotationUtility.getAnnotation(null, "annotation");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithNullAstAndAnnotation() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST ast = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);

        try {
            AnnotationUtility.getAnnotation(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the ast is null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithEmptyOrSpacesAnnotation() {
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationNode = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.branchContains(159)).thenReturn(true);
        when(getAnnotation(ast, ""))
            .thenReturn(annotationNode)
            .thenThrow(new IllegalArgumentException("the annotation is empty or spaces"));

        try {
            AnnotationUtility.getAnnotation(ast, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }

        try {
            AnnotationUtility.getAnnotation(ast, " ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is empty or spaces", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationWithNullAnnotationHolder() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(155);
        when(annotationHolder.getFirstChild()).thenReturn(null);

        try {
            AnnotationUtility.getAnnotation(ast, "annotation");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationWithNullAnnotationNode() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotationNode = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(155);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
        when(annotationNode.getType()).thenReturn(155);
        when(annotationNode.getFirstChild()).thenReturn(null);

        try {
            AnnotationUtility.getAnnotation(ast, "annotation");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationWithInvalidAnnotationNodeType() {
        DetailAST ast = mock(DetailAST.class);
        DetailAST annotationHolder = mock(DetailAST.class);
        DetailAST annotationNode = mock(DetailAST.class);

        when(getAnnotationHolder(ast).getType()).thenReturn(155);
        when(getAnnotationHolder(ast).getFirstChild()).thenReturn(annotationHolder);
        when(annotationHolder.getType()).thenReturn(155);
        when(annotationHolder.getFirstChild()).thenReturn(annotationNode);
        when(annotationNode.getType()).thenReturn(156);

        try {
            AnnotationUtility.getAnnotation(ast, "annotation");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the annotation is not found", e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationWithValidAstAndAnnotation() {
        // Add test case for getAnnotation method
    }

    // Add test cases for other methods

    private DetailAST mockDetailAST(int type) {
        DetailAST detailAST = mock(DetailAST.class);
        when(detailAST.getType()).thenReturn(type);
        return detailAST;
    }
}