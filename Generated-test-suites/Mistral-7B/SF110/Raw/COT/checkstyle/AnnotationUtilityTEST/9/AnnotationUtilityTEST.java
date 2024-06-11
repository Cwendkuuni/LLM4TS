import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import org.junit.Test;

public class AnnotationUtilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAst() {
        AnnotationUtility.containsAnnotation(null, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsAnnotationNullAnnotation() {
        DetailAST ast = checkNotNull(new DetailAST());
        AnnotationUtility.containsAnnotation(ast, null);
    }

    @Test
    public void testContainsAnnotationEmptyAst() {
        DetailAST ast = checkNotNull(new DetailAST());
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotationNoAnnotation() {
        DetailAST ast = checkNotNull(new DetailAST());
        ast.setType(TokenTypes.CLASS_DEF);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test
    public void testContainsAnnotationAnnotationPresent() {
        DetailAST ast = createAnnotationAST();
        assertTrue(AnnotationUtility.containsAnnotation(ast, "annotationName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAst() {
        AnnotationUtility.getAnnotation(null, "annotationName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotation() {
        DetailAST ast = checkNotNull(new DetailAST());
        AnnotationUtility.getAnnotation(ast, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationEmptyAnnotation() {
        DetailAST ast = checkNotNull(new DetailAST());
        AnnotationUtility.getAnnotation(ast, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationNullAnnotationHolder() {
        DetailAST ast = checkNotNull(new DetailAST());
        DetailAST holder = null;
        AnnotationUtility.getAnnotation(ast, holder);
    }

    @Test
    public void testGetAnnotationAnnotationNotFound() {
        DetailAST ast = createAnnotationAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "notFoundAnnotation");
        assertNull(annotation);
    }

    @Test
    public void testGetAnnotationAnnotationFound() {
        DetailAST ast = createAnnotationAST();
        DetailAST annotation = AnnotationUtility.getAnnotation(ast, "annotationName");
        assertNotNull(annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAnnotationHolderNullAst() {
        AnnotationUtility.getAnnotationHolder(null);
    }

    @Test
    public void testGetAnnotationHolderClassDef() {
        DetailAST ast = checkNotNull(new DetailAST());
        ast.setType(TokenTypes.CLASS_DEF);
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.CLASS_DEF, holder.getType());
    }

    @Test
    public void testGetAnnotationHolderAnnotation() {
        DetailAST ast = createAnnotationAST();
        DetailAST holder = AnnotationUtility.getAnnotationHolder(ast);
        assertEquals(TokenTypes.ANNOTATION_DEF, holder.getType());
    }

    private DetailAST createAnnotationAST() {
        DetailAST ast = new DetailAST();
        ast.setType(TokenTypes.ANNOTATION_DEF);
        DetailAST annotation = new DetailAST();
        annotation.setType(TokenTypes.ANNOTATION);
        annotation.setText("@annotationName");
        DetailAST holder = new DetailAST();
        holder.setType(TokenTypes.ANNOTATION_TYPE_DEF);
        holder.setName("annotationName");
        DetailAST child = new DetailAST();
        child.setType(TokenTypes.IDENT);
        child.setText("annotationName");
        holder.addChild(child);
        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(TokenTypes.ANNOTATION);
        annotationNode.addChild(holder);
        DetailAST firstChild = new DetailAST();
        firstChild.setType(TokenTypes.IDENT);
        firstChild.setText("annotationName");
        annotationNode.addChild(firstChild);
        ast.addChild(annotationNode);
        return ast;
    }
}