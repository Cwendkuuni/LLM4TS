package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods("public protected private");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

        DetailAST modsEmpty = createMockMods("");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(modsEmpty));

        DetailAST modsPublic = createMockMods("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(modsPublic));

        DetailAST modsProtected = createMockMods("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(modsProtected));

        DetailAST modsPrivate = createMockMods("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsPrivate));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createMockNode(10, "variable");
        DetailAST parentClass = createMockNode(14, "class");
        DetailAST parentInterface = createMockNode(15, "interface");
        DetailAST parentAnnotation = createMockNode(157, "annotation");
        DetailAST parentBlock = createMockNode(154, "block");
        DetailAST parentAnonInner = createMockNode(136, "anonInner");

        DetailAST surroundingScopeClass = createMockNode(14, "class2");
        DetailAST surroundingScopeInterface = createMockNode(15, "interface2");
        DetailAST surroundingScopeAnnotation = createMockNode(157, "annotation2");
        DetailAST surroundingScopeBlock = createMockNode(154, "block2");

        // Test getSurroundingScope for nodes in the following order: class -> interface -> annotation -> block -> anonInner
        DetailAST currentNode = node;
        DetailAST expectedScope = null;
        DetailAST currentParent = parentClass;
        DetailAST surroundingScope;

        for (int i = 0; i < 6; i++) {
            surroundingScope = ScopeUtils.getSurroundingScope(currentNode);
            assertEquals(currentParent.getType(), currentParent.getType());
            assertEquals(expectedScope, surroundingScope);
            currentNode = currentParent;
            currentParent = currentParent.getParent();

            if (i == 1) {
                expectedScope = Scope.INTERFACE;
            } else if (i == 2) {
                expectedScope = Scope.ANNOTATION;
            } else if (i == 3) {
                expectedScope = Scope.BLOCK;
            } else if (i == 4) {
                expectedScope = parentAnonInner;
            } else {
                expectedScope = currentParent;
            }
        }

        // Test getSurroundingScope for null node
        assertNull(ScopeUtils.getSurroundingScope(null));

        // Test getSurroundingScope for nodes in anonInner block
        currentParent = parentBlock;
        currentNode = createMockNode(136, "anonInner2");
        assertEquals(currentParent, ScopeUtils.getSurroundingScope(currentNode));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST nodeInInterface = createMockNode(15, "interface");
        assertTrue(ScopeUtils.isInInterfaceBlock(nodeInInterface));

        DetailAST nodeOutsideInterface = createMockNode(14, "class");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeOutsideInterface));

        DetailAST nodeInClass = createMockNode(10, "classVariable");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeInClass));

        DetailAST nodeInBlock = createMockNode(154, "block");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeInBlock));

        DetailAST nodeInAnnotation = createMockNode(157, "annotation");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeInAnnotation));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST nodeInAnnotation = createMockNode(157, "annotation");
       assertTrue(ScopeUtils.isInAnnotationBlock(nodeInAnnotation));

        DetailAST nodeOutsideAnnotation = createMockNode(14, "class");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeOutsideAnnotation));

        DetailAST nodeInClass = createMockNode(10, "classVariable");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeInClass));

        DetailAST nodeInInterface = createMockNode(15, "interface");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeInInterface));

        DetailAST nodeInBlock = createMockNode(154, "block");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeInBlock));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST nodeInInterface = createMockNode(15, "interface");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInInterface));

        DetailAST nodeInAnnotation = createMockNode(157, "annotation");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInAnnotation));

        DetailAST nodeOutsideAnnotation = createMockNode(14, "class");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeOutsideAnnotation));

        DetailAST nodeInClass = createMockNode(10, "classVariable");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInClass));

        DetailAST nodeInBlock = createMockNode(154, "block");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInBlock));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST nodeInEnum = createMockNode(154, "enum");
        assertTrue(ScopeUtils.isInEnumBlock(nodeInEnum));

        DetailAST nodeOutsideEnum = createMockNode(14, "class");
        assertFalse(ScopeUtils.isInEnumBlock(nodeOutsideEnum));

        DetailAST nodeInClass = createMockNode(10, "classVariable");
        assertFalse(ScopeUtils.isInEnumBlock(nodeInClass));

        DetailAST nodeInInterface = createMockNode(15, "interface");
        assertFalse(ScopeUtils.isInEnumBlock(nodeInInterface));

        DetailAST nodeInBlock = createMockNode(154, "block");
        assertFalse(ScopeUtils.isInEnumBlock(nodeInBlock));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST nodeInCodeBlock1 = createMockNode(9, "for");
        assertTrue(ScopeUtils.isInCodeBlock(nodeInCodeBlock1));

        DetailAST nodeInCodeBlock2 = createMockNode(8, "if");
        assertTrue(ScopeUtils.isInCodeBlock(nodeInCodeBlock2));

        DetailAST nodeInCodeBlock3 = createMockNode(11, "while");
        assertTrue(ScopeUtils.isInCodeBlock(nodeInCodeBlock3));

        DetailAST nodeInCodeBlock4 = createMockNode(12, "switch");
        assertTrue(ScopeUtils.isInCodeBlock(nodeInCodeBlock4));

        DetailAST nodeOutsideCodeBlock = createMockNode(14, "class");
        assertFalse(ScopeUtils.isInCodeBlock(nodeOutsideCodeBlock));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST nodeOutermostType1 = createMockNode(14, "class");
        assertTrue(ScopeUtils.isOuterMostType(nodeOutermostType1));

        DetailAST nodeOutermostType2 = createMockNode(15, "interface");
        assertTrue(ScopeUtils.isOuterMostType(nodeOutermostType2));

        DetailAST nodeInnerType = createMockNode(154, "classInner");
        assertFalse(ScopeUtils.isOuterMostType(nodeInnerType));

        DetailAST nodeInInterface = createMockNode(15, "interfaceInner");
        assertFalse(ScopeUtils.isOuterMostType(nodeInInterface));

        DetailAST nodeInAnnotation = createMockNode(157, "annotationInner");
        assertFalse(ScopeUtils.isOuterMostType(nodeInAnnotation));

        DetailAST nodeInBlock = createMockNode(154, "blockInner");
        assertFalse(ScopeUtils.isOuterMostType(nodeInBlock));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST nodeLocalVariableDef = createMockNode(10, "localVariable");
        assertTrue(ScopeUtils.isLocalVariableDef(nodeLocalVariableDef));

        DetailAST nodeClassFieldDef = createMockNode(10, "classField");
        assertFalse(ScopeUtils.isLocalVariableDef(nodeClassFieldDef));

        DetailAST nodeEmpty = createMockNode(0, "");
        assertFalse(ScopeUtils.isLocalVariableDef(nodeEmpty));

        DetailAST nodeInInterface = createMockNode(15, "interfaceVariable");
        assertFalse(ScopeUtils.isLocalVariableDef(nodeInInterface));

        DetailAST nodeInAnnotation = createMockNode(157, "annotationVariable");
        assertFalse(ScopeUtils.isLocalVariableDef(nodeInAnnotation));

        DetailAST nodeInBlock = createMockNode(154, "blockVariable");
        assertFalse(ScopeUtils.isLocalVariableDef(nodeInBlock));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST nodeClassFieldDef = createMockNode(10, "classField");
        assertTrue(ScopeUtils.isClassFieldDef(nodeClassFieldDef));

        DetailAST nodeLocalVariableDef = createMockNode(10, "localVariable");
        assertFalse(ScopeUtils.isClassFieldDef(nodeLocalVariableDef));

        DetailAST nodeEmpty = createMockNode(0, "");
        assertFalse(ScopeUtils.isClassFieldDef(nodeEmpty));

        DetailAST nodeInInterface = createMockNode(15, "interfaceField");
        assertFalse(ScopeUtils.isClassFieldDef(nodeInInterface));

        DetailAST nodeInAnnotation = createMockNode(157, "annotationField");
        assertFalse(ScopeUtils.isClassFieldDef(nodeInAnnotation));

        DetailAST nodeInBlock = createMockNode(154, "blockField");
        assertFalse(ScopeUtils.isClassFieldDef(nodeInBlock));
    }

    @Test
    public void testIsInScope() {
        DetailAST nodeInScope = createMockNode(10, "classField");
        DetailAST surroundingScopeClass = createMockNode(14, "class");
        assertTrue(ScopeUtils.isInScope(nodeInScope, Scope.CLASS));

        DetailAST nodeOutsideScope = createMockNode(10, "classField2");
        DetailAST surroundingScopeInterface = createMockNode(15, "interface");
        assertFalse(ScopeUtils.isInScope(nodeOutsideScope, Scope.CLASS));

        DetailAST nodeInScope2 = createMockNode(10, "classField3");
        DetailAST surroundingScopeAnnotation = createMockNode(157, "annotation");
        assertFalse(ScopeUtils.isInScope(nodeInScope2, Scope.CLASS));

        DetailAST nodeInScope3 = createMockNode(10, "classField4");
        DetailAST surroundingScopeBlock = createMockNode(154, "block");
        assertFalse(ScopeUtils.isInScope(nodeInScope3, Scope.CLASS));

        DetailAST nodeInScope4 = createMockNode(10, "classField5");
        DetailAST surroundingScopeClass2 = createMockNode(14, "class2");
        assertTrue(ScopeUtils.isInScope(nodeInScope4, Scope.CLASS));
    }

    private DetailAST createMockMods(String modifierString) {
        return createMockAST("Modifiers", modifierString);
    }

    private DetailAST createMockNode(int type, String text) {
        return createMockAST("AST", String.format("Node_%d", type), null, null, null, text, null);
    }

    private DetailAST createMockNode(int type, String text, DetailAST leftSibling, DetailAST rightSibling, DetailAST parent,
            DetailAST firstChild, DetailAST lastChild) {
        return createMockAST("AST", String.format("Node_%d", type), text, leftSibling, rightSibling, parent, firstChild,
                lastChild);
    }

    private DetailAST createMockAST(String type, String name, DetailAST... children) {
        DetailAST mock = Mockito.mock(DetailAST.class);
        when(mock.getType()).thenReturn(Integer.parseInt(type));
        when(mock.getName()).thenReturn(name);
        when(mock.getFirstChild()).thenReturn((DetailAST) children[0]);
        when(mock.getNextSibling()).thenReturn(children[1]);
        when(mock.getParent()).thenReturn(children[2]);
        return mock;
    }
}