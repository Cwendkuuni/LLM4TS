package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.*;

public class ScopeUtilsTest {

 @BeforeClass
 public static void setUp() {
 // Setup your mocks or fixtures here, if necessary
 }

 @AfterClass
 public static void tearDown() {
 // Teardown here, if necessary
 }

 @Test
 public void testGetScopeFromMods_public() {
 DetailAST mods = createAST("public class Test { }");
 Scope expected = Scope.PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_protected() {
 DetailAST mods = createAST("protected class Test { }");
 Scope expected = Scope.PROTECTED;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_private() {
 DetailAST mods = createAST("private class Test { }");
 Scope expected = Scope.PRIVATE;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST mods = createAST("class Test { }");
 Scope expected = Scope.PACKAGE;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_scopeInsidePackage() {
 DetailAST node = createAST("class InnerClass { }", 1, 1, 2);
 Scope expected = Scope.PACKAGE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_scopeInsideClass() {
 DetailAST node = createAST("class OuterClass { class InnerClass { } }", 4, 1, 5);
 Scope expected = Scope.CLASS;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_scopeInsideInterface() {
 DetailAST node = createAST("interface OuterInterface { interface InnerInterface { } }", 4, 1, 5);
 Scope expected = Scope.INTERFACE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_scopeInsideAnnotation() {
 DetailAST node = createAST("@Annotation class Test { }", 2, 2, 2);
 Scope expected = Scope.ANNOTATION;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_scopeInsideAnonymousClass() {
 DetailAST node = createAST("new Object() { }", 1, 1, 3);
 Scope expected = Scope.ANONINNER;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock_inInterfaceBlock() {
 DetailAST node = createAST("interface Test { }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock_notInInterfaceBlock() {
 DetailAST node = createAST("class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_inAnnotationBlock() {
 DetailAST node = createAST("@Annotation public class Test { }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_notInAnnotationBlock() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_inInterfaceOrAnnotationBlock() {
 DetailAST node = createAST("@Annotation interface Test { }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_notInInterfaceOrAnnotationBlock() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_inEnumBlock() {
 DetailAST node = createAST("enum Test { VALUE }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_notInEnumBlock() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_inCodeBlock() {
 DetailAST node = createAST("public static void main(String[] args) { System.out.println(\"Hello, World!\"); }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_notInCodeBlock() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_outerMostType() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_notOuterMostType() {
 DetailAST node = createAST("class InnerClass { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_localVariableDef() {
 DetailAST node = createAST("int x = 5;", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_notLocalVariableDef() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_classFieldDef() {
 DetailAST node = createAST("public class Test { int x; }", 1, 1, 1);
 boolean expected = true;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_notClassFieldDef() {
 DetailAST node = createAST("public class Test { }", 1, 1, 1);
 boolean expected = false;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope_inScope() {
 DetailAST ast = createAST("public class Test { int x; }", 1, 1, 1);
 Scope scope = Scope.CLASS;
 boolean expected = true;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope_notInScope() {
 DetailAST ast = createAST("public class Test { int x; }", 1, 1, 1);
 Scope scope = Scope.INTERFACE;
 boolean expected = false;
 boolean actual = ScopeUtils.isInScope(ast, scope);
 assertEquals(expected, actual);
 }

 private DetailAST createAST(String code, int line, int column, int offset) {
 return (DetailAST) new ANTLRFileStream("Test.java").lex(new ANTLRSaxParser(Java4TokenTypesDef.class) {
 @Override
 protected DetailAST createAST(int type, int line, int column, int offset) {
 return new DetailAST(type, line, column, offset);
 }
 }).TokenStream().getTree();
 new Java4Lexer().tokenize(new ANTLRInputStream(new ByteArrayInputStream(code.getBytes())));
 return new Java4Parser(new ANTLRTreeWalker()).parse();
 }

}