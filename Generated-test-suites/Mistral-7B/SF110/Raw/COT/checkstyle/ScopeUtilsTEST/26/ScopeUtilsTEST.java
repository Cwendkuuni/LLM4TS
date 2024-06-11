import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods_PublicModifier() {
 DetailAST testAST = new DetailAST(); // Add a valid DetailAST instance for testing
 Scope expectedResult = Scope.PUBLIC;
 Scope result = ScopeUtils.getScopeFromMods(testAST);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetScopeFromMods_ProtectedModifier() {
 DetailAST testAST = new DetailAST(); // Add a valid DetailAST instance for testing
 Scope expectedResult = Scope.PROTECTED;
 Scope result = ScopeUtils.getScopeFromMods(testAST);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetScopeFromMods_PrivateModifier() {
 DetailAST testAST = new DetailAST(); // Add a valid DetailAST instance for testing
 Scope expectedResult = Scope.PRIVATE;
 Scope result = ScopeUtils.getScopeFromMods(testAST);
 assertEquals(expectedResult, result);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testGetScopeFromMods_NullModsAST() {
 DetailAST testAST = null; // Add a null DetailAST instance for testing
 Scope result = ScopeUtils.getScopeFromMods(testAST);
 }

 @Test
 public void testGetScopeFromMods_NoModifierFound() {
 DetailAST testAST = new DetailAST(); // Add a valid DetailAST instance for testing without any modifier
 Scope result = ScopeUtils.getScopeFromMods(testAST);
 assertEquals(Scope.PACKAGE, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInPackageScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance
 Scope expectedResult = Scope.PACKAGE;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInPublicScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance with a public parent
 Scope expectedResult = Scope.PUBLIC;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInProtectedScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance with a protected parent
 Scope expectedResult = Scope.PROTECTED;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInPrivateScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance with a private parent
 Scope expectedResult = Scope.PRIVATE;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInAnonymousClassScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an anonymous class
 Scope expectedResult = Scope.ANONINNER;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInInterfaceBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an interface block
 Scope expectedResult = Scope.INTERFACE;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetSurroundingScope_NodeInAnnotationBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an annotation block
 Scope expectedResult = Scope.ANNOTATION;
 Scope result = ScopeUtils.getSurroundingScope(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInInterfaceBlock_NodeInInterfaceBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an interface block
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInInterfaceBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInInterfaceBlock_NodeNotInInterfaceBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance not within an interface block
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInInterfaceBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInAnnotationBlock_NodeInAnnotationBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an annotation block
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInAnnotationBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInAnnotationBlock_NodeNotInAnnotationBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance not within an annotation block
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInAnnotationBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_NodeInInterfaceOrAnnotationBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an interface or annotation block
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_NodeNotInInterfaceOrAnnotationBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance not within an interface or annotation block
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInEnumBlock_NodeInEnumBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within an enum block
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInEnumBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInEnumBlock_NodeNotInEnumBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance not within an enum block
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInEnumBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInCodeBlock_NodeInCodeBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance within a code block
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInCodeBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInCodeBlock_NodeNotInCodeBlock() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance not within a code block
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInCodeBlock(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsOuterMostType_NodeIsOutermostType() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing an outermost type
 boolean expectedResult = true;
 boolean result = ScopeUtils.isOuterMostType(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsOuterMostType_NodeIsNotOutermostType() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing a nested type
 boolean expectedResult = false;
 boolean result = ScopeUtils.isOuterMostType(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsLocalVariableDef_NodeIsLocalVariableDefinition() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing a local variable definition
 boolean expectedResult = true;
 boolean result = ScopeUtils.isLocalVariableDef(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsLocalVariableDef_NodeIsNotLocalVariableDefinition() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing a non-local variable definition
 boolean expectedResult = false;
 boolean result = ScopeUtils.isLocalVariableDef(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsClassFieldDef_NodeIsClassFieldDefinition() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing a class field definition
 boolean expectedResult = true;
 boolean result = ScopeUtils.isClassFieldDef(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsClassFieldDef_NodeIsNotClassFieldDefinition() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance representing a non-class field definition
 boolean expectedResult = false;
 boolean result = ScopeUtils.isClassFieldDef(testNode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInScope_NodeIsInScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance
 Scope testScope = Scope.PACKAGE;
 boolean expectedResult = true;
 boolean result = ScopeUtils.isInScope(testNode, testScope);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsInScope_NodeIsNotInScope() {
 DetailAST testNode = new DetailAST(); // Add a valid DetailAST instance
 Scope testScope = Scope.INTERFACE;
 boolean expectedResult = false;
 boolean result = ScopeUtils.isInScope(testNode, testScope);
 assertEquals(expectedResult, result);
 }
}