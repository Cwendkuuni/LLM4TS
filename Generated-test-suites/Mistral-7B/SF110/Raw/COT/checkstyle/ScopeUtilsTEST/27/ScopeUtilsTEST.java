import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;

public class ScopeUtilsTest {

 private DetailAST testAst;

 @Before
 public void setup() {
 // Create a sample DetailAST for testing
 // Replace this with a real DetailAST from your IDE or codebase
 DetailAST rootNode = new DefaultAST();
 DetailAST packageDecl = new DefaultAST();
 packageDecl.setType(15);
 packageDecl.addChild(new DefaultAST("package", "com.example"));
 rootNode.addChild(packageDecl);

 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST mods = new DefaultAST();
 DetailAST publicMod = new DefaultAST("public");
 DetailAST classMod = new DefaultAST("class");
 mods.addChild(publicMod);
 mods.addChild(classMod);
 classNode.addChild(mods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 rootNode.addChild(classNode);

 testAst = classNode;
 }

 @Test
 public void testGetScopeFromMods() {
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(testAst.getFirstChild());
 assertEquals(expected, actual);

 testAst = testAst.getFirstChild().getNextSibling();
 expected = Scope.PROTECTED;
 actual = ScopeUtils.getScopeFromMods(testAst);
 assertEquals(expected, actual);

 testAst = testAst.getNextSibling();
 expected = Scope.PRIVATE;
 actual = ScopeUtils.getScopeFromMods(testAst);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_publicClass() {
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getSurroundingScope(testAst);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_protectedClass() {
 DetailAST protectedClassNode = new DefaultAST();
 protectedClassNode.setType(14);
 protectedClassNode.addChild(new DefaultAST("protected", "class"));
 protectedClassNode.addChild(new DefaultAST("MyProtectedClass"));
 DetailAST protectedMods = new DefaultAST();
 DetailAST protectedMod = new DefaultAST("protected");
 protectedMods.addChild(protectedMod);
 protectedClassNode.addChild(protectedMods);
 DetailAST protectedClassBody = new DefaultAST();
 protectedClassNode.addChild(protectedClassBody);
 testAst = protectedClassNode;

 Scope expected = Scope.PROTECTED;
 Scope actual = ScopeUtils.getSurroundingScope(testAst);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_privateClass() {
 DetailAST privateClassNode = new DefaultAST();
 privateClassNode.setType(14);
 privateClassNode.addChild(new DefaultAST("private", "class"));
 privateClassNode.addChild(new DefaultAST("MyPrivateClass"));
 DetailAST privateMods = new DefaultAST();
 DetailAST privateMod = new DefaultAST("private");
 privateMods.addChild(privateMod);
 privateClassNode.addChild(privateMods);
 DetailAST privateClassBody = new DefaultAST();
 privateClassNode.addChild(privateClassBody);
 testAst = privateClassNode;

 Scope expected = Scope.PRIVATE;
 Scope actual = ScopeUtils.getSurroundingScope(testAst);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_interface() {
 DetailAST interfaceNode = new DefaultAST();
 interfaceNode.setType(15);
 interfaceNode.addChild(new DefaultAST("interface"));
 interfaceNode.addChild(new DefaultAST("MyInterface"));
 DetailAST interfaceMods = new DefaultAST();
 interfaceNode.addChild(interfaceMods);
 testAst = interfaceNode;

 Scope expected = Scope.INTERFACE;
 Scope actual = ScopeUtils.getSurroundingScope(testAst);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSurroundingScope_anoninner() {
 DetailAST anoninnerNode = new DefaultAST();
 anoninnerNode.setType(136);
 testAst = anoninnerNode;

 try {
 ScopeUtils.getSurroundingScope(testAst);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsInInterfaceBlock_interface() {
 DetailAST interfaceNode = new DefaultAST();
 interfaceNode.setType(15);
 interfaceNode.addChild(new DefaultAST("interface"));
 interfaceNode.addChild(new DefaultAST("MyInterface"));
 DetailAST interfaceBody = new DefaultAST();
 interfaceNode.addChild(interfaceBody);
 testAst = interfaceNode;

 assertTrue(ScopeUtils.isInInterfaceBlock(testAst));
 }

 @Test
 public void testIsInInterfaceBlock_notInterface() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 testAst = classNode;

 assertFalse(ScopeUtils.isInInterfaceBlock(testAst));
 }

 @Test
 public void testIsInAnnotationBlock_annotation() {
 DetailAST annotationNode = new DefaultAST();
 annotationNode.setType(157);
 annotationNode.addChild(new DefaultAST("@MyAnnotation"));
 testAst = annotationNode;

 assertTrue(ScopeUtils.isInAnnotationBlock(testAst));
 }

 @Test
 public void testIsInAnnotationBlock_notAnnotation() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 testAst = classNode;

 assertFalse(ScopeUtils.isInAnnotationBlock(testAst));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_interface() {
 DetailAST interfaceNode = new DefaultAST();
 interfaceNode.setType(15);
 interfaceNode.addChild(new DefaultAST("interface"));
 interfaceNode.addChild(new DefaultAST("MyInterface"));
 DetailAST interfaceBody = new DefaultAST();
 interfaceNode.addChild(interfaceBody);
 testAst = interfaceNode;

 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(testAst));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_annotation() {
 DetailAST annotationNode = new DefaultAST();
 annotationNode.setType(157);
 annotationNode.addChild(new DefaultAST("@MyAnnotation"));
 testAst = annotationNode;

 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(testAst));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_class() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 testAst = classNode;

 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(testAst));
 }

 @Test
 public void testIsInEnumBlock_enum() {
 DetailAST enumNode = new DefaultAST();
 enumNode.setType(154);
 enumNode.addChild(new DefaultAST("enum"));
 enumNode.addChild(new DefaultAST("MyEnum"));
 DetailAST enumBody = new DefaultAST();
 enumNode.addChild(enumBody);
 testAst = enumNode;

 assertTrue(ScopeUtils.isInEnumBlock(testAst));
 }

 @Test
 public void testIsInEnumBlock_notEnum() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 testAst = classNode;

 assertFalse(ScopeUtils.isInEnumBlock(testAst));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST codeBlockNode = new DefaultAST();
 codeBlockNode.setType(9);
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 DetailAST methodNode = new DefaultAST();
 methodNode.setType(3);
 DetailAST methodMods = new DefaultAST();
 DetailAST methodMod = new DefaultAST("public");
 methodMods.addChild(methodMod);
 methodNode.addChild(methodMods);
 DetailAST methodBody = new DefaultAST();
 methodNode.addChild(methodBody);
 classBody.addChild(methodNode);
 testAst = codeBlockNode;

 assertTrue(ScopeUtils.isInCodeBlock(testAst));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 testAst = classNode;

 assertTrue(ScopeUtils.isOuterMostType(testAst));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST variableNode = new DefaultAST();
 variableNode.setType(10);
 DetailAST variableMods = new DefaultAST();
 DetailAST variableMod = new DefaultAST("final");
 variableMods.addChild(variableMod);
 variableNode.addChild(variableMods);
 testAst = variableNode;

 assertTrue(ScopeUtils.isLocalVariableDef(testAst));
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST fieldNode = new DefaultAST();
 fieldNode.setType(10);
 testAst = fieldNode;

 assertFalse(ScopeUtils.isClassFieldDef(testAst));
 }

 @Test
 public void testIsInScope() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 DetailAST methodNode = new DefaultAST();
 methodNode.setType(3);
 DetailAST methodMods = new DefaultAST();
 DetailAST methodMod = new DefaultAST("public");
 methodMods.addChild(methodMod);
 methodNode.addChild(methodMods);
 DetailAST methodBody = new DefaultAST();
 methodNode.addChild(methodBody);
 classBody.addChild(methodNode);
 DetailAST fieldNode = new DefaultAST();
 fieldNode.setType(10);
 DetailAST fieldMods = new DefaultAST();
 DetailAST fieldMod = new DefaultAST("public");
 fieldMods.addChild(fieldMod);
 fieldNode.addChild(fieldMods);
 DetailAST fieldValue = new DefaultAST();
 fieldNode.addChild(fieldValue);
 methodBody.addChild(fieldNode);
 Scope expected = Scope.PUBLIC;
 testAst = fieldNode;

 assertTrue(ScopeUtils.isInScope(testAst, expected));
 }

 @Test
 public void testIsInScope_wrongScope() {
 DetailAST classNode = new DefaultAST();
 classNode.setType(14);
 classNode.addChild(new DefaultAST("public", "class"));
 classNode.addChild(new DefaultAST("MyClass"));
 DetailAST classMods = new DefaultAST();
 DetailAST classMod = new DefaultAST("public");
 classMods.addChild(classMod);
 classNode.addChild(classMods);
 DetailAST classBody = new DefaultAST();
 classNode.addChild(classBody);
 DetailAST methodNode = new DefaultAST();
 methodNode.setType(3);
 DetailAST methodMods = new DefaultAST();
 DetailAST methodMod = new DefaultAST("public");
 methodMods.addChild(methodMod);
 methodNode.addChild(methodMods);
 DetailAST methodBody = new DefaultAST();
 methodNode.addChild(methodBody);
 classBody.addChild(methodNode);
 DetailAST fieldNode = new DefaultAST();
 fieldNode.setType(10);
 DetailAST fieldMods = new DefaultAST();
 DetailAST fieldMod = new DefaultAST("protected");
 fieldMods.addChild(fieldMod);
 fieldNode.addChild(fieldMods);
 DetailAST fieldValue = new DefaultAST();
 fieldNode.addChild(fieldValue);
 methodBody.addChild(fieldNode);
 Scope expected = Scope.PRIVATE;
 testAst = fieldNode;

 assertFalse(ScopeUtils.isInScope(testAst, expected));
 }
}