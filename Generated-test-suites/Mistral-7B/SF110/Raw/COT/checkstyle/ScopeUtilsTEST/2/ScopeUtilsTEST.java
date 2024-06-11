import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST(); // Dummy instance for testing
        ast.setType(1); // Set type as a placeholder

        String[] modifiers = {"public", "protected", "private"};
        for (String modifier : modifiers) {
            DetailAST modifierAST = new DetailAST();
            modifierAST.setText(modifier);
            ast.addChild(modifierAST);

            Scope expectedScope = Scope.valueOf(modifier.toUpperCase());
            Scope actualScope = ScopeUtils.getScopeFromMods(ast);
            assertEquals(expectedScope, actualScope);
        }
    }

    @Test
    public void testGetScopeFromMods_NoModifierFound() {
        DetailAST ast = new DetailAST(); // Dummy instance for testing
        ast.setType(1); // Set type as a placeholder

        Scope actualScope = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, actualScope);
    }

    @Test
    public void testGetSurroundingScope_WithinClass() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as CLASS_DEF
        classNode.addChild(node);

        DetailAST expectedScopeNode = new DetailAST();
        expectedScopeNode.setType(15); // Set type as CLASS_DEF
        expectedScopeNode.setName("SomeClass"); // Set name as a placeholder

        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope.getClassScope(), actualScope);
    }

    @Test
    public void testGetSurroundingScope_WithinMethod() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST methodNode = new DetailAST();
        methodNode.setType(11); // Set type as METHOD_DEF
        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as CLASS_DEF
        methodNode.addChild(classNode);
        classNode.addChild(node);

        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_WithinAnnotation() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(157); // Set type as ANNOTATION_DEF
        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as CLASS_DEF
        annotationNode.addChild(classNode);
        classNode.addChild(node);

        Scope expectedScope = Scope.ANNOTATION;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_WithinInnerClass() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST innerClassNode = new DetailAST();
        innerClassNode.setType(154); // Set type as INNERCLASS_DEF
        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as CLASS_DEF
        innerClassNode.addChild(classNode);
        classNode.addChild(node);

        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSurroundingScope_NullParentNode() {
        DetailAST node = new DetailAST(); // Dummy instance for testing

        ScopeUtils.getSurroundingScope(node); // This should throw a NullPointerException
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST interfaceNode = new DetailAST();
        interfaceNode.setType(15); // Set type as interface
        interfaceNode.addChild(node);

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as class
        DetailAST parentNode = new DetailAST();
        parentNode.setType(14); // Set type as package
        classNode.addChild(parentNode);
        classNode.addChild(interfaceNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(157); // Set type as ANNOTATION_DEF
        annotationNode.addChild(node);

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as class
        DetailAST parentNode = new DetailAST();
        parentNode.setType(14); // Set type as package
        classNode.addChild(parentNode);
        classNode.addChild(annotationNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST interfaceNode = new DetailAST();
        interfaceNode.setType(15); // Set type as interface
        interfaceNode.addChild(node);

        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(157); // Set type as ANNOTATION_DEF
        annotationNode.addChild(node);

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as class
        DetailAST parentNode = new DetailAST();
        parentNode.setType(14); // Set type as package
        classNode.addChild(parentNode);
        classNode.addChild(interfaceNode);
        classNode.addChild(annotationNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST enumNode = new DetailAST();
        enumNode.setType(154); // Set type as ENUM_DEF
        enumNode.addChild(node);

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as class
        DetailAST parentNode = new DetailAST();
        parentNode.setType(14); // Set type as package
        classNode.addChild(parentNode);
        classNode.addChild(enumNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInEnumBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST codeBlockNode = new DetailAST();
        codeBlockNode.setType(9); // Set type as STMT_LIST
        DetailAST parentNode = new DetailAST();
        parentNode.setType(15); // Set type as class
        codeBlockNode.addChild(parentNode);
        parentNode.addChild(node);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInCodeBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST outerClassNode = new DetailAST();
        outerClassNode.setType(15); // Set type as class
        DetailAST innerClassNode = new DetailAST();
        innerClassNode.setType(154); // Set type as inner class
        DetailAST parentNode = new DetailAST();
        parentNode.setType(15); // Set type as class
        outerClassNode.addChild(parentNode);
        parentNode.addChild(innerClassNode);
        innerClassNode.addChild(node);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isOuterMostType(node);
        assertEquals(expectedResult, actualResult);

        node = new DetailAST(); // Set type as a placeholder
        DetailAST outerInterfaceNode = new DetailAST();
        outerInterfaceNode.setType(15); // Set type as interface
        DetailAST innerClassNode = new DetailAST();
        innerClassNode.setType(154); // Set type as inner class
        DetailAST parentNode2 = new DetailAST();
        parentNode2.setType(15); // Set type as class
        outerInterfaceNode.addChild(parentNode2);
        parentNode2.addChild(innerClassNode);
        innerClassNode.addChild(node);

        expectedResult = false;
        actualResult = ScopeUtils.isOuterMostType(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST varNode = new DetailAST();
        varNode.setType(10); // Set type as VAR_DEF
        DetailAST parentNode = new DetailAST();
        parentNode.setType(7); // Set type as VAR_DECL
        parentNode.addChild(node);
        varNode.addChild(parentNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST fieldNode = new DetailAST();
        fieldNode.setType(10); // Set type as VAR_DEF
        DetailAST parentNode = new DetailAST();
        parentNode.setType(156); // Set type as FIELD_DEF
        parentNode.addChild(node);
        fieldNode.addChild(parentNode);

        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isClassFieldDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInScope() {
        DetailAST node = new DetailAST(); // Dummy instance for testing
        node.setType(1); // Set type as a placeholder

        DetailAST classNode = new DetailAST();
        classNode.setType(15); // Set type as CLASS_DEF
        DetailAST fieldNode = new DetailAST();
        fieldNode.setType(10); // Set type as VAR_DEF
        DetailAST parentNode = new DetailAST();
        parentNode.setType(15); // Set type as CLASS_DEF
        DetailAST variableScopeNode = new DetailAST();
        variableScopeNode.setType(156); // Set type as FIELD_DEF
        DetailAST childNode = new DetailAST();
        childNode.setType(1); // Set type as a placeholder
        fieldNode.addChild(childNode);
        parentNode.addChild(classNode);
        variableScopeNode.addChild(parentNode);
        parentNode.addChild(fieldNode);

        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        expectedScope = Scope.FIELD;
        actualScope = ScopeUtils.isInScope(node, variableScopeNode);
        assertEquals(expectedScope, actualScope);
    }
}