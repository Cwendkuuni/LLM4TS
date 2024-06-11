package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

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

        DetailAST modsEmptyAndPublic = createMockMods("public ");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(modsEmptyAndPublic));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeClassInClass = createMockNode("ClassDef", nodeClass);
        DetailAST nodeMethodInClass = createMockNode("MethodDef", nodeClass);
        DetailAST nodeVariableInMethod = createMockNode("VariableDef", nodeMethod);
        DetailAST nodeVariableInClass = createMockNode("VariableDef", nodeClass);

        DetailAST nodeInterface = createMockNode("InterfaceDef");
        DetailAST nodeAnnotation = createMockNode("AnnotationDef");
        DetailAST nodeEnum = createMockNode("EnumDef");

        DetailAST nodeCodeBlock = createMockNode("CodeBlock", nodeClass);
        DetailAST nodeInterfaceBlock = createMockNode("InterfaceBlock", nodeInterface);
        DetailAST nodeAnnotationBlock = createMockNode("AnnotationBlock", nodeAnnotation);
        DetailAST nodeEnumBlock = createMockNode("EnumBlock", nodeEnum);

        DetailAST nodeClassInEnum = createMockNode("ClassDef", nodeEnum);

        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(nodeClass));
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(nodeMethodInClass));
        assertEquals(Scope.METHOD, ScopeUtils.getSurroundingScope(nodeVariableInMethod));
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(nodeVariableInClass));
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(nodeInterface));
        assertEquals(Scope.ANNOTATION, ScopeUtils.getSurroundingScope(nodeAnnotation));
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(nodeEnum));
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(nodeClassInEnum));
        assertNull(ScopeUtils.getSurroundingScope(null));

        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(nodeClassInMethod));
        assertNull(ScopeUtils.getSurroundingScope(nodeCodeBlock));
        assertNull(ScopeUtils.getSurroundingScope(nodeInterfaceBlock));
        assertNull(ScopeUtils.getSurroundingScope(nodeAnnotationBlock));
        assertNull(ScopeUtils.getSurroundingScope(nodeEnumBlock));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeInterface = createMockNode("InterfaceDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeClassInInterface = createMockNode("ClassDef", nodeInterface);
        DetailAST nodeMethodInInterface = createMockNode("MethodDef", nodeInterface);

        assertTrue(ScopeUtils.isInInterfaceBlock(nodeInterface));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeClass));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeMethod));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeVariable));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeClassInInterface));
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeMethodInInterface));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeAnnotation = createMockNode("AnnotationDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeAnnotationInClass = createMockNode("AnnotationDef", nodeClass);
        DetailAST nodeAnnotationInMethod = createMockNode("AnnotationDef", nodeMethod);

        assertTrue(ScopeUtils.isInAnnotationBlock(nodeAnnotation));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeClass));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeMethod));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeVariable));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeAnnotationInClass));
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeAnnotationInMethod));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeInterface = createMockNode("InterfaceDef");
        DetailAST nodeAnnotation = createMockNode("AnnotationDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeInterface));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAnnotation));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeClass));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeMethod));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeVariable));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeEnum = createMockNode("EnumDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeEnumInClass = createMockNode("EnumDef", nodeClass);

        assertTrue(ScopeUtils.isInEnumBlock(nodeEnum));
        assertFalse(ScopeUtils.isInEnumBlock(nodeClass));
        assertFalse(ScopeUtils.isInEnumBlock(nodeMethod));
        assertFalse(ScopeUtils.isInEnumBlock(nodeVariable));
        assertFalse(ScopeUtils.isInEnumBlock(nodeEnumInClass));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeCodeBlockClass = createMockNode("CodeBlock", nodeClass);
        DetailAST nodeCodeBlockMethod = createMockNode("CodeBlock", nodeMethod);

        assertTrue(ScopeUtils.isInCodeBlock(nodeCodeBlockClass));
        assertTrue(ScopeUtils.isInCodeBlock(nodeCodeBlockMethod));
        assertFalse(ScopeUtils.isInCodeBlock(nodeClass));
        assertFalse(ScopeUtils.isInCodeBlock(nodeMethod));
        assertFalse(ScopeUtils.isInCodeBlock(nodeVariable));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");
        DetailAST nodeCodeBlock = createMockNode("CodeBlock", nodeClass);
        DetailAST nodeInterface = createMockNode("InterfaceDef");
        DetailAST nodeAnnotation = createMockNode("AnnotationDef");
        DetailAST nodeEnum = createMockNode("EnumDef");

        assertTrue(ScopeUtils.isOuterMostType(nodeClass));
        assertTrue(ScopeUtils.isOuterMostType(nodeCodeBlock));
        assertTrue(ScopeUtils.isOuterMostType(nodeMethod));
        assertTrue(ScopeUtils.isOuterMostType(nodeVariable));
        assertFalse(ScopeUtils.isOuterMostType(nodeInterface));
        assertFalse(ScopeUtils.isOuterMostType(nodeAnnotation));
        assertFalse(ScopeUtils.isOuterMostType(nodeEnum));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeVariableInClass = createMockNode("VariableDef", nodeClass);
        DetailAST nodeVariableInMethod = createMockNode("VariableDef", nodeMethod);

        assertTrue(ScopeUtils.isLocalVariableDef(nodeVariableInMethod));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeVariable));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeClass));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeMethod));
        assertFalse(ScopeUtils.isLocalVariableDef(nodeVariableInClass));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeVariableInClass = createMockNode("VariableDef", nodeClass);

        assertFalse(ScopeUtils.isClassFieldDef(nodeVariable));
        assertTrue(ScopeUtils.isClassFieldDef(nodeVariableInClass));
        assertFalse(ScopeUtils.isClassFieldDef(nodeClass));
    }

    @Test
    public void testIsInScope() {
        DetailAST nodeClass = createMockNode("ClassDef");
        DetailAST nodeMethod = createMockNode("MethodDef");
        DetailAST nodeVariable = createMockNode("VariableDef");

        DetailAST nodeVariableInClass = createMockNode("VariableDef", nodeClass);
        DetailAST nodeClassInInterface = createMockNode("ClassDef", createMockNode("InterfaceDef"));
        DetailAST nodeMethodInInterface = createMockNode("MethodDef", createMockNode("InterfaceDef"));
        DetailAST nodeVariableInInterface = createMockNode("VariableDef", createMockNode("InterfaceDef"));

        assertTrue(ScopeUtils.isInScope(nodeVariableInClass, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeVariable, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeClass, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeMethod, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeVariableInInterface, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeClassInInterface, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(nodeMethodInInterface, Scope.CLASS));
        assertTrue(ScopeUtils.isInScope(nodeVariableInInterface, Scope.INTERFACE));

        assertTrue(ScopeUtils.isInScope(nodeClass, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(nodeMethod, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(nodeVariable, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(nodeClassInInterface, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(nodeMethodInInterface, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(nodeVariableInInterface, Scope.PACKAGE));
        assertTrue(ScopeUtils.isInScope(nodeVariableInClass, Scope.CLASS));
        assertTrue(ScopeUtils.isInScope(nodeVariable, Scope.METHOD));
        assertTrue(ScopeUtils.isInScope(nodeMethod, Scope.METHOD));
        assertTrue(ScopeUtils.isInScope(nodeClass, Scope.PACKAGE));
    }

    private DetailAST createMockMods(String mods) {
        DetailAST modsAST = new DetailAST();
        DetailAST token = new DetailAST();
        token.setType(1);
        token.setText(mods);
        modsAST.setFirstChild(token);
        return modsAST;
    }

    private DetailAST createMockNode(String... nodeTypes) {
        DetailAST nodeAST = new DetailAST();
        nodeAST.setType(nodeTypes[0].hashCode());
        return nodeAST;
    }

    private DetailAST createMockNode(String type, DetailAST parent) {
        DetailAST nodeAST = new DetailAST();
        nodeAST.setType(type.hashCode());
        nodeAST.setParent(parent);
        return nodeAST;
    }
}