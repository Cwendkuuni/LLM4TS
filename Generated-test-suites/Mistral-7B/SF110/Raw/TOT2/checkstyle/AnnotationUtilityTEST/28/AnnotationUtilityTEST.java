import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;

public class AnnotationUtilityTest {

    @Test
    public void testContainsAnnotationWithNullAST() {
        assertFalse(AnnotationUtility.containsAnnotation(null, "ValidAnnotation"));
    }

    @Test
    public void testContainsAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @Test
    public void testContainsAnnotationWithEmptyAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, ""));
    }

    @Test
    public void testContainsAnnotationWithValidAnnotationAndAST() {
        DetailAST ast = new DetailAST();
        String annotation = "ValidAnnotation";
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testGetAnnotationWithNullAST() {
        assertNull(AnnotationUtility.getAnnotation(null, "ValidAnnotation"));
    }

    @Test
    public void testGetAnnotationWithNullAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, null));
    }

    @Test
    public void testGetAnnotationWithEmptyOrWhitespaceAnnotation() {
        DetailAST ast = new DetailAST();
        assertNull(AnnotationUtility.getAnnotation(ast, ""));
        assertNull(AnnotationUtility.getAnnotation(ast, "   "));
    }

    @Test
    public void testGetAnnotationWithValidAnnotationAndAST() {
        DetailAST ast = new DetailAST();
        String annotation = "ValidAnnotation";
        DetailAST annotationNode = AnnotationUtility.getAnnotation(ast, annotation);
        assertNotNull(annotationNode);
    }

    @Test
    public void testContainsAnnotationWithMultipleAnnotations() {
        DetailAST ast = new DetailAST();
        String annotation = "ValidAnnotation";
        // Assuming you have a way to add multiple annotations to the DetailAST
        assertTrue(AnnotationUtility.containsAnnotation(ast, annotation));
    }

    @Test
    public void testContainsAnnotationWithNoAnnotation() {
        DetailAST ast = new DetailAST();
        assertFalse(AnnotationUtility.containsAnnotation(ast, "InvalidAnnotation"));
    }

    @Test
    public void testGetAnnotationWithAnnotationUnderAnotherNode() {
        DetailAST ast = new DetailAST();
        String annotation = "ValidAnnotation";
        DetailAST anotherNode = new DetailAST();
        DetailAST annotationNode = new DetailAST();
        // Assuming you have a way to add the nodes to the DetailAST in the given structure
        assertNull(AnnotationUtility.getAnnotation(ast, annotation));
    }

}