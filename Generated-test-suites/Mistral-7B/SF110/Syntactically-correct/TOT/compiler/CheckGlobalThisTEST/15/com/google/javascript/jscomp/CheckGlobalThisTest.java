import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("test.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testConstructorFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testConstructorFunction.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testPrototypeMethod() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testPrototypeMethod.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testAnnotatedFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testAnnotatedFunction.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testFunctionWithNoAnnotation.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testGlobalThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testGlobalThis.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertFalse(checkGlobalThis.compiler.getDiagnostics().isEmpty());
    assertEquals(1, checkGlobalThis.compiler.getDiagnostics().size());
    Diagnostic diagnostic = checkGlobalThis.compiler.getDiagnostics().get(0);
    assertEquals(DiagnosticType.WARNING, diagnostic.getType());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getCode());
  }

  @Test
  public void testFunctionWithPrototypeAssignment() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testFunctionWithPrototypeAssignment.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithPrototypePropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testFunctionWithPrototypePropertyAccess.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNestedPrototypePropertyAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Parser parser = new Parser(new JamKitSourceFile("testFunctionWithNestedPrototypePropertyAccess.js"));
    Node root = parser.parse();

    traverse(checkGlobalThis, root, null);
    assertTrue(checkGlobalThis.compiler.getDiagnostics().isEmpty());
  }
}