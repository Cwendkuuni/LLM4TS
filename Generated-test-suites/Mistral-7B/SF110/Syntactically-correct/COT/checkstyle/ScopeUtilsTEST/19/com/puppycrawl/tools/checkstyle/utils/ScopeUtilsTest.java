import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DefaultAST(); // Create a sample DetailAST for testing
        Scope expectedScope = PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);

        // Test for other scopes
        DetailAST modsWithProtected = createModsAST("protected");
        actualScope = ScopeUtils.getScopeFromMods(modsWithProtected);
        assertEquals(PROTECTED, actualScope);

        DetailAST modsWithPrivate = createModsAST("private");
        actualScope = ScopeUtils.getScopeFromMods(modsWithPrivate);
        assertEquals(PRIVATE, actualScope);

        DetailAST modsWithNoModifiers = new DefaultAST();
        actualScope = ScopeUtils.getScopeFromMods(modsWithNoModifiers);
        assertEquals(PACKAGE, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromModsNullMods() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromModsEmptyMods() {
        DetailAST emptyMods = new DefaultAST();
        ScopeUtils.getScopeFromMods(emptyMods);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DefaultAST(); // Create a sample DetailAST for testing
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);

        // Test for other scopes
        DetailAST nodeInPackageScope = createNodeInPackageScope();
        actualScope = ScopeUtils.getSurroundingScope(nodeInPackageScope);
        assertEquals(PACKAGE, actualScope);

        DetailAST nodeInPublicScope = createNodeInPublicScope();
        actualScope = ScopeUtils.getSurroundingScope(nodeInPublicScope);
        assertEquals(PUBLIC, actualScope);

        DetailAST nodeInProtectedScope = createNodeInProtectedScope();
        actualScope = ScopeUtils.getSurroundingScope(nodeInProtectedScope);
        assertEquals(PROTECTED, actualScope);

        DetailAST nodeInPrivateScope = createNodeInPrivateScope();
        actualScope = ScopeUtils.getSurroundingScope(nodeInPrivateScope);
        assertEquals(PRIVATE, actualScope);

        DetailAST nodeInInterfaceBlock = createNodeInInterfaceBlock();
        actualScope = ScopeUtils.getSurroundingScope(nodeInInterfaceBlock);
        assertEquals(INTERFACE, actualScope);

        DetailAST nodeInAnnotationBlock = createNodeInAnnotationBlock();
        actualScope = ScopeUtils.getSurroundingScope(nodeInAnnotationBlock);
        assertEquals(ANNOTATION, actualScope);

        DetailAST nodeInEnumBlock = createNodeInEnumBlock();
        actualScope = ScopeUtils.getSurroundingScope(nodeInEnumBlock);
        assertEquals(ENUM, actualScope);

        DetailAST nodeInCodeBlock = createNodeInCodeBlock();
        actualScope = ScopeUtils.getSurroundingScope(nodeInCodeBlock);
        assertEquals(CODE, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSurroundingScopeNullNode() {
        ScopeUtils.getSurroundingScope(null);
    }

    // Add more test methods here for other methods in ScopeUtils class

    private DetailAST createModsAST(String... modifierStrings) {
        // Implement this method to create a DetailAST for testing getScopeFromMods()
        // ...
    }

    private DetailAST createNodeInPackageScope() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInPublicScope() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInProtectedScope() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInPrivateScope() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInInterfaceBlock() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInAnnotationBlock() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInEnumBlock() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }

    private DetailAST createNodeInCodeBlock() {
        // Implement this method to create a DetailAST for testing getSurroundingScope()
        // ...
    }
}