package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.DefaultCompiler;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.util.DuplicateCommandLineException;
import org.apache.commons.cli.util.HelpFormatter;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CodeChangeEvent;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Mapping;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceName;

public class ScopedAliasesTest {

  private static class TestCompiler extends DefaultCompiler {
    @Override
    public void process(CompilationUnit unit, Node root) {
      super.process(unit, root);
      if (unit.getPassManager().getPass(HotSwapCompilerPass.class) != null) {
        NodeTraversal traversal = unit.getTraversal();
        ((ScopedAliases) unit.getPassManager()
            .getPass(HotSwapCompilerPass.class))
            .process(unit.getExterns(), root);
      }
    }

    @Override
    public void report(DiagnosticType error, Node node, Object... arguments) {
      throw new RuntimeException("Compiler error: " + error + " " + arguments);
    }
  }

  @Test
  public void testScopedAliases() {
    Options options = new Options();
    options.addOption("foo", true, "Foo option");
    options.addOption("bar", 1, "Bar option");

    Parser parser = new Parser();
    SourceName inputFileName = new SourceName("Test.java");
    SourceFile inputSourceFile = new SourceFile(inputFileName, null);
    List<SourceFile> sourceFiles = Lists.newArrayList(inputSourceFile);

    CompilationUnit unit = new CompilationUnit(sourceFiles, null, null);
    unit.setOptions(options);

    TestCompiler compiler = new TestCompiler();
    ErrorReporter errorReporter = new ErrorReporter();
    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    compilerOptions.setStrictMode(false);
    compilerOptions.setEmitWarning(false);
    compilerOptions.setPrintStackTraces(false);
    compilerOptions.setEmitPreamble(false);
    compilerOptions.setEmitSourcemaps(false);
    compilerOptions.setGenerateSynchronousSourcemaps(false);
    compilerOptions.setShowInlineSources(false);
    compilerOptions.setSourceMapFile(null);
    compilerOptions.setOutputFile(null);
    compilerOptions.setKeepLineNumbers(false);
    compilerOptions.setKeepFunctionNames(false);
    compilerOptions.setKeepClassName(false);
    compilerOptions.setProcessConditionalComments(false);
    compilerOptions.setPreserveComments(false);
    compilerOptions.setPreserveConstantExpressions(true);
    compilerOptions.setCompileWith(CompilerOptions.CompileMode.SYNCHRONOUS);
    compilerOptions.setAllowExperimentalOptions(true);
    compilerOptions.setGenerateDeclarationFiles(false);
    compilerOptions.setUseECMAVersion(true);
    compilerOptions.setUseECMAVersion(5);
    compilerOptions.setECMAVersion(5);
    compilerOptions.setEmitBanner(false);
    compilerOptions.setDisableRewrittenClasses(false);
    compilerOptions.setWriteTypeAnnotations(false);
    compilerOptions.setDisableRewrittenConstructors(false);
    compilerOptions.setDisableRewrittenTypes(false);
    compilerOptions.setDisableRewrittenTypeCoercion(false);
    compilerOptions.setDisableRewrittenTypeCheck(false);
    compilerOptions.setDisableRewrittenSynchronization(false);
    compilerOptions.setDisableRewrittenStarImport(false);
    compilerOptions.setDisableRewrittenImportAll(false);
    compilerOptions.setAllowAllTypes(false);
    compilerOptions.setAllowAllTypesInSameFile(false);
    compilerOptions.setAllowJSDocAnnotations(false);
    compilerOptions.setPolyfills(Collections.emptyMap());
    compilerOptions.setAllowJsDeclarations(false);
    compilerOptions.setCompileWith(CompilerOptions.CompileMode.DEFAULT);
    compilerOptions.setAliasTransformation(new AliasTransformation() {
      @Override
      public void transform(Node n, Node parent, AliasTransformationHandler handler) {
        if (n.isVar() && n.hasChildren()) {
          Var var = n.asVar();
          String name = var.getName();
          Var aliasVar = unit.getScope().getVar(name);
          if (aliasVar != null) {
            Var newVar = NodeUtil.makeVar(n.getParent(), n.getName(), null, null, null, n.getSourcePosition());
            newVar.setInitialValue(aliasVar.getInitialValue());
            parent.replaceChild(n, newVar);
            handler.reportCodeChange(new CodeChangeEvent(n, newVar));
          }
        }
      }
    });
    compiler.setCompilerOptions(compilerOptions);

    compiler.compile(unit);

    String expectedOutput = "org.apache.commons.cli.Options";
    SourcePosition expectedPosition = new SourcePosition<>();
    expectedPosition.setPositionInformation(
        inputFileName, 1, 1, 1, 1, 1);

    assertEquals(expectedOutput, unit.getOutput().toString());
    assertEquals(expectedPosition, unit.getOutput().getSourcePosition());

    Node root = unit.getRoot();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertTrue(traversal.hasErrors());

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    Node fooAliasDefinition = aliasDefinitions.get(0);
    assertEquals("org.apache.commons.cli.Option", fooAliasDefinition.getType().getName());
    assertEquals("foo", fooAliasDefinition.getString());

    Node barAliasDefinition = aliasDefinitions.get(1);
    assertEquals("int", barAliasDefinition.getType().getName());
    assertEquals("bar", barAliasDefinition.getString());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getString());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(1, anonymousFunction.getChildCount());

    Node body = anonymousFunction.getFirstChild();
    assertEquals(3, body.getChildCount());

    Node optionsVar = body.getChildAtIndex(0);
    assertEquals("options", optionsVar.getString());
    assertEquals(Type.VAR, optionsVar.getType());

    Node optionsAssignment = body.getChildAtIndex(1);
    assertEquals("=", optionsAssignment.getString());

    Node optionsValue = body.getChildAtIndex(2);
    assertEquals("new org.apache.commons.cli.DefaultOptions()", optionsValue.getString());

    Node argsVar = body.getChildAtIndex(3);
    assertEquals("args", argsVar.getString());
    assertEquals(Type.VAR, argsVar.getType());

    Node argsAssignment = body.getChildAtIndex(4);
    assertEquals("=", argsAssignment.getString());

    Node argsValue = body.getChildAtIndex(5);
    assertEquals("process(options, args)", argsValue.getString());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());

    AliasedNode fooUsage = (AliasedNode) aliasUsages.get(0);
    assertEquals("options.foo", fooUsage.aliasReference.getString());
    assertEquals("true", fooUsage.aliasDefinition.getString());

    AliasedNode barUsage = (AliasedNode) aliasUsages.get(1);
    assertEquals("args[0]", barUsage.aliasReference.getString());
    assertEquals("java.lang.String[]", barUsage.aliasDefinition.getType().getName());
  }

  @Test
  public void testErrors() {
    Options options = new Options();

    Parser parser = new Parser();
    SourceName inputFileName = new SourceName("Test.java");
    SourceFile inputSourceFile = new SourceName(inputFileName, null);
    List<SourceFile> sourceFiles = Lists.newArrayList(inputSourceFile);

    CompilationUnit unit = new CompilationUnit(sourceFiles, null, null);

    TestCompiler compiler = new TestCompiler();

    try {
      compiler.compile(unit);
      fail();
    } catch (DuplicateCommandLineException e) {
      // Expected
    }

    String expectedError = "goog.scope must have exactly one parameter.";
    SourcePosition expectedPosition = new SourcePosition<>();
    expectedPosition.setPositionInformation(
        inputFileName, 1, 1, 1, 1, 1);

    Node root = unit.getRoot();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();

    assertTrue(traversal.hasErrors());
    assertEquals(1, aliasDefinitions.size());

    Node scopeCall = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals(2, scopeCall.getChildCount());

    Node anonymousFunction = scopeCall.getLastChild();
    assertEquals(1, anonymousFunction.getChildCount());

    Node body = anonymousFunction.getFirstChild();
    assertEquals(1, body.getChildCount());

    Node errorNode = body.getFirstChild();
    assertEquals(expectedError, errorNode.getString());
    assertEquals(expectedPosition, errorNode.getSourcePosition());
  }
}

class AliasedNode implements ScopedAliases.AliasUsage {
  private final Node aliasReference;
  private final Node aliasDefinition;

  AliasedNode(Node aliasReference, Node aliasDefinition) {
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    aliasReference.getParent().replaceChild(
        aliasReference, aliasDefinition.cloneTree());
  }
}

class AliasedTypeNode implements ScopedAliases.AliasUsage {
  private final Node typeReference;
  private final Node aliasDefinition;
  private final String aliasName;

  AliasedTypeNode(Node typeReference, Node aliasDefinition, String aliasName) {
    this.typeReference = typeReference;
    this.aliasDefinition = aliasDefinition;
    this.aliasName = aliasName;
  }

  @Override
  public void applyAlias() {
    String typeName = typeReference.getString();
    String aliasExpanded =
        Preconditions.checkNotNull(aliasDefinition.getQualifiedName());
    Preconditions.checkState(typeName.startsWith(aliasName));
    typeReference.setString(typeName.replaceFirst(aliasName, aliasExpanded));
  }
}