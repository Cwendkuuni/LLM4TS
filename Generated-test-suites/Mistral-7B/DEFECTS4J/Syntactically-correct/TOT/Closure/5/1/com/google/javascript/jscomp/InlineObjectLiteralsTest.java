package com.google.javascript.jscomp.tests;

import static com.google.javascript.rhino.NodeProperty.HAS_PROPERTY;
import static com.google.javascript.rhino.NodeProperty.IS_GETPROPERY;
import static com.google.javascript.rhino.NodeProperty.IS_NAME;
import static com.google.javascript.rhino.NodeProperty.IS_VAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.InliningBehavior;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private static final String TEST_CODE =
      "function foo() { var x = {a: 1, b: 2, c: 3}; x.d = 4; }";

  public void testInlineObjectLiteralsPass() throws Exception {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(TEST_CODE));

    assertEquals(IR.FUNCTION, compiler.getRootNode().getNodeType());
    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check there are no more object literals in the code
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isObjectLit()) {
          fail("Object literal found: " + node);
        }
      }
    }, NodeUtil.MATCH_ALL);

    // Check variables were inlined
    Set<String> variables = Sets.newHashSet("a", "b", "c", "d");
    for (Iterator<Scope> scopeIt = rootScope.getParentScopeChain().iterator();
         scopeIt.hasNext();) {
      Scope scope = scopeIt.next();
      for (Iterator<Var> varIt = scope.getVars(); varIt.hasNext();) {
        Var var = varIt.next();
        if (!variables.contains(var.name)) continue;
        ReferenceCollection refs = compiler.getReferenceMap().getReferences(var);
        assertFalse(refs.references.isEmpty());
        assertFalse(refs.isWellDefined());
        Reference initial = refs.getInitializingReference();
        assertNotNull(initial);
        ReferenceCollection initialRefs = compiler.getReferenceMap().getReferences(initial);
        assertEquals(1, initialRefs.references.size());
        Reference init = initialRefs.references.get(0);
        Node initNode = init.getNode();
        assertTrue(initNode.isAssign());
        Node lhs = initNode.getFirstChild();
        assertTrue(lhs.isName());
        String varName = lhs.getString();
        assertEquals(varName, var.name);
        Node rhs = initNode.getNext();
        assertTrue(rhs.isObjectLit());

        // Check if all properties of the inlined object are present
        for (Iterator<Reference> refIt = refs.references.iterator(); refIt.hasNext();) {
          Reference ref = refIt.next();
          if (ref == init) continue;

          Node node = ref.getNode();
          if (node.isGetProp()) {
            String propertyName = node.getString();
            assertEquals(propertyName, Lists.newArrayList("a", "b", "c", "d")
                    .get(variables.indexOf(var.name)) );
          }
        }
      }
    }
  }

  /**
   * Test that an object literal with a self-referential property is not inlined.
   */
  public void testSelfReferentialObjectLiteral() throws Exception {
    String testCode = "function foo() { var x = {a: 1, b: {c: x}}; }";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(testCode));

    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check variables were not inlined
    Set<String> variables = Sets.newHashSet("a");
    for (Iterator<Scope> scopeIt = rootScope.getParentScopeChain().iterator();
         scopeIt.hasNext();) {
      Scope scope = scopeIt.next();
      for (Iterator<Var> varIt = scope.getVars(); varIt.hasNext();) {
        Var var = varIt.next();
        if (!variables.contains(var.name)) continue;
        ReferenceCollection refs = compiler.getReferenceMap().getReferences(var);
        assertTrue(refs.references.isEmpty());
        assertFalse(refs.isWellDefined());
      }
    }

    // Check the object literal was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isObjectLit()) {
          fail("Object literal found: " + node);
        }
      }
    }, NodeUtil.MATCH_ALL);
  }

  /**
   * Test that an object literal with an exported property is not inlined.
   */
  public void testExportedProperty() throws Exception {
    String testCode = "function foo() { export let x = {a: 1}; }";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(testCode));

    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check variables were not inlined
    Set<String> variables = Sets.newHashSet("a");
    for (Iterator<Scope> scopeIt = rootScope.getParentScopeChain().iterator();
         scopeIt.hasNext();) {
      Scope scope = scopeIt.next();
      for (Iterator<Var> varIt = scope.getVars(); varIt.hasNext();) {
        Var var = varIt.next();
        if (!variables.contains(var.name)) continue;
        ReferenceCollection refs = compiler.getReferenceMap().getReferences(var);
        assertTrue(refs.references.isEmpty());
        assertFalse(refs.isWellDefined());
      }
    }

    // Check the object literal was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isObjectLit()) {
          fail("Object literal found: " + node);
        }
      }
    }, NodeUtil.MATCH_ALL);
  }

  /**
   * Test that a global variable is not inlined by the InlineObjectLiterals pass.
   */
  public void testGlobalVariable() throws Exception {
    String testCode = "var x = {a: 1}";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(testCode));

    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check the object literal was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isObjectLit()) {
          fail("Object literal found: " + node);
        }
      }
    }, NodeUtil.MATCH_ALL);

    // Check the global variable was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isVar() && node.getString().equals("x")) {
          ReferenceCollection refs = compiler.getReferenceMap().getReferences(node);
          assertTrue(refs.references.isEmpty());
          assertFalse(refs.isWellDefined());
        }
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
  }

  /**
   * Test that a variable that has already been assigned in the code is not inlined.
   */
  public void testAlreadyAssignedVariable() throws Exception {
    String testCode = "function foo() { var x = 1; x = {a: 1}; }";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(testCode));

    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check the object literal was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isObjectLit()) {
          fail("Object literal found: " + node);
        }
      }
    }, NodeUtil.MATCH_ALL);

    // Check the variable was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isVar() && node.getString().equals("x")) {
          ReferenceCollection refs = compiler.getReferenceMap().getReferences(node);
          assertTrue(refs.references.isEmpty());
          assertFalse(refs.isWellDefined());
        }
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
  }

  /**
   * Test that a variable that is not assigned to an object literal is not inlined.
   */
  public void testVariableNotAssignedToObjectLiteral() throws Exception {
    String testCode = "function foo() { var x = 1; }";

    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, createSafeNameIdSupplier());
    pass.process(null, parse(testCode));

    Node root = compiler.getRootNode();
    Scope rootScope = root.getFunctionScope();

    // Check the variable was not inlined
    NodeUtil.visitPreOrder(root, new NodeUtil.Visitor() {
      @Override
      public void visit(Node node) {
        if (node.isVar() && node.getString().equals("x")) {
          ReferenceCollection refs = compiler.getReferenceMap().getReferences(node);
          assertTrue(refs.references.isEmpty());
          assertFalse(refs.isWellDefined());
        }
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
  }

  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = newAbstractCompiler();
    compiler.init();
    return compiler;
  }

  private Node parse(String code) throws Exception {
    Node root = parser.parse(code, parser.options(), null);
    RootNode rootNode = (RootNode) root;
    rootNode.setSourceName("test.js");
    return root;
  }

  private static AbstractCompiler newAbstractCompiler() {
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.addPass(new InliningBehavior());
    return compiler;
  }

  private static Supplier<String> createSafeNameIdSupplier() {
    return new Supplier<String>() {
      String id = 0;

      @Override
      public String get() {
        return String.valueOf(++id);
      }
    };
  }
}