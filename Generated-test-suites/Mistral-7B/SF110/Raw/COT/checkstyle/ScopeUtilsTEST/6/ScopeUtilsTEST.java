import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_public() {
        DetailAST mods = new DetailAST(); // Assuming you have a way to create a DetailAST with "public" as first child
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_protected() {
        DetailAST mods = new DetailAST(); // Assuming you have a way to create a DetailAST with "protected" as first child
        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_private() {
        DetailAST mods = new DetailAST(); // Assuming you have a way to create a DetailAST with "private" as first child
        Scope expected = Scope.PRIVATE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_emptyMods() {
        DetailAST mods = new DetailAST(); // Empty DetailAST
        ScopeUtils.getScopeFromMods(mods);
    }

    @Test
    public void testGetScopeFromMods_mixedModifiers() {
        DetailAST mods = new DetailAST(); // Assuming you have a way to create a DetailAST with "public protected private" as children
        Scope expected = Scope.MULTIPLE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_classWithPublicModifiers() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a class with public modifiers
        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_classWithProtectedModifiers() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a class with protected modifiers
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_classWithPrivateModifiers() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a class with private modifiers
        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_interface() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an interface
        Scope expected = Scope.INTERFACE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_annotation() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an annotation
        Scope expected = Scope.ANNOTATION;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_anonymousClass() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an anonymous class
        Scope expected = Scope.ANONINNER;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock_interfaceBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an interface block
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock_nonInterfaceBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-interface block
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock_annotationBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an annotation block
        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock_nonAnnotationBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-annotation block
        boolean expected = false;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_interfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an interface or annotation block
        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_nonInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-interface and non-annotation block
        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock_enumBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an enum block
        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock_nonEnumBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-enum block
        boolean expected = false;
        boolean actual = ScopeUtils.isInEnumBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock_codeBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a code block
        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock_nonCodeBlock() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-code block
        boolean expected = false;
        boolean actual = ScopeUtils.isInCodeBlock(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType_outermostType() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing an outermost type
        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType_nonOuterMostType() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-outermost type
        boolean expected = false;
        boolean actual = ScopeUtils.isOuterMostType(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef_localVariableDef() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a local variable definition
        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef_nonLocalVariableDef() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-local variable definition
        boolean expected = false;
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef_classFieldDef() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a class field definition
        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef_nonClassFieldDef() {
        DetailAST node = new DetailAST(); // Assuming you have a way to create a DetailAST representing a non-class field definition
        boolean expected = false;
        boolean actual = ScopeUtils.isClassFieldDef(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_inScope() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a DetailAST
        Scope scope = Scope.PACKAGE;
        boolean expected = true;
        boolean actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_notInScope() {
        DetailAST ast = new DetailAST(); // Assuming you have a way to create a DetailAST
        Scope scope = Scope.INTERFACE;
        boolean expected = false;
        boolean actual = ScopeUtils.isInScope(ast, scope);
        assertEquals(expected, actual);
    }
}