import static org.junit.Assert.*;
import com.google.common.base.Preconditions;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.junit.Test;

public class AnnotationUtilityTest {
    @Test
    public void testContainsAnnotationWithValidAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "InvalidAnnotation";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = " \t\r\fannotation ";
        assertFalse(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithValidAST() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        assertTrue(AnnotationUtility.containsAnnotation(ast));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationWithNullASTException() {
        DetailAST ast = null;
        String annotation = "InvalidAnnotation";
        AnnotationUtility.containsAnnotation(ast, annotation);
    }

    @Test
    public void testGetAnnotationHolderWithValidAST() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        DetailAST result = AnnotationUtility.getAnnotationHolder(ast);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationHolderWithNullAST() {
        DetailAST ast = null;
        assertNull(AnnotationUtility.getAnnotationHolder(ast));
    }

    @Test
    public void testGetAnnotationWithValidASTAndAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = "ValidAnnotation";
        DetailAST result = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        DetailAST ast = null;
        String annotation = "InvalidAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = "";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithWhitespaceAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = " \t\r\fannotation ";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithDifferentAnnotation() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a valid DetailAST instance
        String annotation = "DifferentAnnotation";
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }
}