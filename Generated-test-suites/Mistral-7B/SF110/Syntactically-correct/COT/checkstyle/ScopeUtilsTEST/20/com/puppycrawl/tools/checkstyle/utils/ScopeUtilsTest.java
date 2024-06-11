import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(new SimpleNode("public"));
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        // Test for private and protected
        mods.removeChild(0);
        mods.addChild(new SimpleNode("protected"));
        expected = Scope.PROTECTED;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods.removeChild(0);
        mods.addChild(new SimpleNode("private"));
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        // Test for no modifier
        mods.removeAllChildren();
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, actual);
    }

    @Test
    public void testGetSurroundingScope_Package() {
        DetailAST node = new DetailAST();
        DetailAST packageScope = new DetailAST();
        packageScope.setType(15);
        node.setParent(packageScope);
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_Class() {
        DetailAST node = new DetailAST();
        DetailAST classNode = new DetailAST();
        classNode.setType(14);
        node.setParent(classNode);
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_Interface() {
        DetailAST node = new DetailAST();
        DetailAST interfaceNode = new DetailAST();
        interfaceNode.setType(15);
        node.setParent(interfaceNode);
        Scope expected = Scope.INTERFACE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_Annotation() {
        DetailAST node = new DetailAST();
        DetailAST annotationNode = new DetailAST();
        annotationNode.setType(157);
        node.setParent(annotationNode);
        Scope expected = Scope.ANNOTATION;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_AnonInner() {
        DetailAST node = new DetailAST();
        DetailAST anonInnerNode = new DetailAST();
        anonInnerNode.setType(136);
        node.setParent(anonInnerNode);
        Scope expected = Scope.ANONINNER;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testGetSurroundingScope_NullNode() {
        DetailAST node = null;
        ScopeUtils.getSurroundingScope(node);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST interfaceBlockNode = new DetailAST();
        interfaceBlockNode.setType(15);
        node.setParent(interfaceBlockNode);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST annotationBlockNode = new DetailAST();
        annotationBlockNode.setType(157);
        node.setParent(annotationBlockNode);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST interfaceOrAnnotationBlockNode = new DetailAST();
        interfaceOrAnnotationBlockNode.setType(157);
        node.setParent(interfaceOrAnnotationBlockNode);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST enumBlockNode = new DetailAST();
        enumBlockNode.setType(154);
        node.setParent(enumBlockNode);
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST codeBlockNode = new DetailAST();
        codeBlockNode.setType(9);
        node.setParent(codeBlockNode);
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node));

        DetailAST parent = new DetailAST();
        parent.setType(14);
        node.setParent(parent);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        parent.setType(35);
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        parent.setType(156);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.setType(21);
        DetailAST parent2 = new DetailAST();
        parent2.setType(96);
        node.setParent(parent2);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.setType(10);
        node.setParent(new DetailAST());
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope_SameScope() {
        DetailAST node = new DetailAST();
        DetailAST scopeNode = new DetailAST();
        scopeNode.setType(15);
        node.setParent(scopeNode);
        Scope expected = Scope.PACKAGE;
        Scope actual = Scope.PACKAGE;
        assertEquals(expected, actual);
        assertTrue(ScopeUtils.isInScope(node, expected));
    }

    @Test
    public void testIsInScope_DifferentScope() {
        DetailAST node = new DetailAST();
        DetailAST packageScopeNode = new DetailAST();
        packageScopeNode.setType(15);
        node.setParent(new DetailAST());
        node.getParent().setParent(packageScopeNode);
        Scope expected = Scope.CLASS;
        assertFalse(expected.isIn(Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(node, expected));
    }
}