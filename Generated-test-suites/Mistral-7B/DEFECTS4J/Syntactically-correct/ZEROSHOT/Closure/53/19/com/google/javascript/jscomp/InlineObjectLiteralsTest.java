package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestSourceFile;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private CompilerPass inlineObjectLiterals;
  private CompilerPassManager passManager;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    passManager = new CompilerPassManager(new CompilerOptions());
  }

  @Test
  public void testInlineObjectLiterals() {
    TestSourceFile sourceFile = new TestSourceFile("test.js");
    Node rootNode = sourceFile.getRootNode();

    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);
    when(compiler.getReferenceCollector()).thenReturn(referenceCollection);

    // Test case for an object literal assignment.
    Node objectLiteralNode = new Node(Token.OBJECTLIT);
    Node keyNode1 = new Node(Token.STRING, "key1");
    Node keyNode2 = new Node(Token.STRING, "key2");
    Node valueNode1 = new Node(Token.NUMBER, 1);
    Node valueNode2 = new Node(Token.NUMBER, 2);
    objectLiteralNode.addChildren(keyNode1, valueNode1, keyNode2, valueNode2);
    Node objectLiteralAssignmentNode = new Node(Token.ASSIGN, keyNode1, objectLiteralNode);

    Node variableDeclarationNode = new Node(Token.VAR, Node.newIdent("obj"));
    variableDeclarationNode.addChild(objectLiteralAssignmentNode);
    Node statementNode = new Node(Token.STATEMENT, variableDeclarationNode);
    rootNode.addChild(statementNode);

    // Test case for an object literal as a property.
    Node propertyAssignmentNode = new Node(Token.ASSIGN, keyNode1, valueNode1);
    Node objectNode = new Node(Token.OBJECTLIT);
    Node propertyNode = new Node(Token.STRING, "prop");
    objectNode.addChild(propertyNode);
    objectNode.addChild(propertyAssignmentNode);
    Node objectAssignmentNode = new Node(Token.ASSIGN, Node.newIdent("obj"), objectNode);
    statementNode = new Node(Token.STATEMENT, objectAssignmentNode);
    rootNode.addChild(statementNode);

    Scope scope = new Scope();
    scope.setRoot(rootNode);

    inlineObjectLiterals.process(null, rootNode);

    // Check that the object literal assignment was inlined.
    List<Node> statements = Lists.newArrayList(rootNode.getFirstChild());
    Iterator<Node> statementIterator = statements.iterator();
    Node firstStatement = statementIterator.next();
    Node firstStatementType = firstStatement.getType();
    Node firstStatementVarNode = null;
    Node firstStatementAssignNode = null;
    Node firstStatementKeyNode1 = null;
    Node firstStatementValueNode1 = null;
    Node firstStatementValueNode2 = null;
    while (statementIterator.hasNext()) {
      Node statement = statementIterator.next();
      Node statementType = statement.getType();
      if (statementType == Token.VAR && firstStatementType == Token.STATEMENT) {
        firstStatementVarNode = statement;
      } else if (statementType == Token.ASSIGN &&
          firstStatementType == Token.STATEMENT &&
          firstStatementVarNode != null) {
        firstStatementAssignNode = statement;
      } else if (statementType == Token.ASSIGN &&
          firstStatementAssignNode != null &&
          statement.getFirstChild() != null &&
          statement.getFirstChild().getType() == Token.GETPROP &&
          firstStatementAssignNode.getFirstChild() != null &&
          firstStatementAssignNode.getFirstChild().getString().equals("key1")) {
        firstStatementKeyNode1 = statement.getFirstChild().getLastChild();
      } else if (statementType == Token.ASSIGN &&
          firstStatementAssignNode != null &&
          statement.getFirstChild() != null &&
          statement.getFirstChild().getType() == Token.GETPROP &&
          firstStatementAssignNode.getFirstChild().getString().equals("key1") &&
          statement.getLastChild() != null &&
          statement.getLastChild().getType() == Token.NUMBER &&
          statement.getLastChild().getInt() == 1) {
        firstStatementValueNode1 = statement.getLastChild();
      } else if (statementType == Token.ASSIGN &&
          firstStatementAssignNode != null &&
          statement.getFirstChild() != null &&
          statement.getFirstChild().getType() == Token.GETPROP &&
          firstStatementAssignNode.getFirstChild().getString().equals("key2") &&
          statement.getLastChild() != null &&
          statement.getLastChild().getType() == Token.NUMBER &&
          statement.getLastChild().getInt() == 2) {
        firstStatementValueNode2 = statement.getLastChild();
      }
    }
    assertVarDeclared("JSCompiler_object_inline_key1", firstStatementValueNode1);
    assertVarDeclared("JSCompiler_object_inline_key2", firstStatementValueNode2);
    assertAssignmentsEqual("key1: 1, key2: 2", firstStatementKeyNode1, firstStatementValueNode1);
    assertAssignmentsEqual("prop: JSCompiler_object_inline_key1", firstStatementKeyNode1, firstStatementValueNode1);
    assertAssignmentsEqual("prop: JSCompiler_object_inline_key2", firstStatementKeyNode1, firstStatementValueNode2);

    // Check that the object literal as a property was inlined.
    statementIterator = statements.iterator();
    statement = null;
    while (statementIterator.hasNext()) {
      statement = statementIterator.next();
      if (statement.getType() == Token.STATEMENT &&
          statement.getFirstChild() != null &&
          statement.getFirstChild().getType() == Token.ASSIGN &&
          statement.getFirstChild().getFirstChild() != null &&
          statement.getFirstChild().getFirstChild().getType() == Token.GETPROP &&
          statement.getFirstChild().getFirstChild().getLastChild().getString().equals("obj")) {
        break;
      }
    }
    assertVarDeclared("JSCompiler_object_inline_key1", statement.getFirstChild().getLastChild().getNextSibling());
    assertVarDeclared("JSCompiler_object_inline_key2", statement.getFirstChild().getLastChild().getNextSibling().getNextSibling());
    assertAssignmentsEqual("prop: JSCompiler_object_inline_key1", statement.getFirstChild().getFirstChild().getLastChild(),
        statement.getFirstChild().getLastChild().getNextSibling());
    assertAssignmentsEqual("prop: JSCompiler_object_inline_key2", statement.getFirstChild().getFirstChild().getLastChild().getNextSibling(),
        statement.getFirstChild().getLastChild().getNextSibling().getNextSibling());
  }

  private void assertVarDeclared(String variableName, Node expectedValue) {
    Node variableDeclaration = null;
    for (Node childNode = rootNode.getFirstChild(); childNode != null;
         childNode = childNode.getNextSibling()) {
      if (childNode.getType() == Token.VAR &&
          childNode.getFirstChild() != null &&
          childNode.getFirstChild().getString().equals(variableName)) {
        variableDeclaration = childNode;
        break;
      }
    }
    assertNotNull(variableDeclaration);
    Node variableValue = variableDeclaration.getLastChild();
    assertEquals("Expected value to match", expectedValue, variableValue);
  }

  private void assertAssignmentsEqual(String expectedAssignments, Node expectedKeyNode, Node expectedValueNode) {
    String assignments = "";
    if (expectedKeyNode != null) {
      assignments += expectedKeyNode.getString() + ": ";
    }
    if (expectedValueNode != null) {
      assignments += expectedValueNode.getString();
    }
    assertEquals(expectedAssignments, assignments);
  }

  private void assertAssignmentsEqual(String expectedAssignments, Node expectedKeyNode, Node expectedValueNode1, Node expectedValueNode2) {
    String assignments = "";
    if (expectedKeyNode != null) {
      assignments += expectedKeyNode.getString() + ": ";
    }
    if (expectedValueNode1 != null) {
      assignments += expectedValueNode1.getString();
    } else {
      assignments += "undefined";
    }
    if (expectedValueNode2 != null) {
      assignments += ", ";
      assignments += expectedValueNode2.getString();
    }
    assertEquals(expectedAssignments, assignments);
  }

  private void assertEquals(String message, Object expected, Object actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError(message + ": Expected " + expected + " but got " + actual);
    }
  }

  private void assertNotNull(Object object) {
    if (object == null) {
      fail("Expected object not to be null");
    }
  }
}