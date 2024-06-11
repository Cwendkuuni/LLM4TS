package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Multimaps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private static abstract class TestMethodCompilerPass extends MethodCompilerPass {
    TestMethodCompilerPass(AbstractCompiler compiler) {
      super(compiler);
    }

    abstract Callback getActingCallback();

    abstract SignatureStore getSignatureStore();

    abstract void assertMethodDefinitions(Map<String, Node> expectedMethodDefinitions);

    void testProcess(String inputJavaScriptCode, String expectedSourceFileName)
        throws IOException {
      CompilerOptions options = new CompilerOptions();
      SourceFile sourceFile = new SourceFile(expectedSourceFileName, inputJavaScriptCode);
      Parser parser = new Parser(null);
      Node root = parser.parse(sourceFile, null, null);

      AbstractCompiler compiler = new AbstractCompiler();
      setUpCompiler(compiler, options, sourceFile);

      CompilerPass pass = this;
      pass.process(null, root);

      assertMethodDefinitions(getExpectedMethodDefinitions(pass.methodDefinitions));
    }

    abstract Map<String, Node> getExpectedMethodDefinitions();

    void setUpCompiler(AbstractCompiler compiler, CompilerOptions options, SourceFile sourceFile) {
      compiler.setOptions(options);
      compiler.setSourceFile(sourceFile);
      compiler.setCompilerOptions(options);
      compiler.setNodeFactory(NodeFactory.getGlobalInstance());
    }
  }

  @org.junit.Test
  public void testSimpleMethodCompilerPass() throws IOException {
    String inputJavaScriptCode =
        "function Foo() {} " +
        "Foo.prototype.bar = function(x) {};";
    TestMethodCompilerPass test = new SimpleMethodCompilerPass(new AbstractCompiler());
    test.testProcess(inputJavaScriptCode, "Foo.js");
  }

  static class SimpleMethodCompilerPass extends TestMethodCompilerPass {
    @Override
    Callback getActingCallback() {
      return new NoOpCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return new NoOpSignatureStore();
    }

    @Override
    Map<String, Node> getExpectedMethodDefinitions() {
      return Multimaps.newHashMap(
          MethodCompilerPass.MethodDefinitions.create("Foo.prototype.bar",
              NodeFactory.createFunctionExpression(
                  NodeFactory.createIdentifier("Foo"),
                  List.of(NodeFactory.createIdentifier("x")))));
    }

    static class NoOpCallback extends AbstractPostOrderCallback {
    }

    static class NoOpSignatureStore implements MethodCompilerPass.SignatureStore {
      @Override
      public void reset() {
      }

      @Override
      public void addSignature(
          String functionName, Node functionNode, String sourceFile) {
      }

      @Override
      public void removeSignature(String functionName) {
      }
    }
  }
}