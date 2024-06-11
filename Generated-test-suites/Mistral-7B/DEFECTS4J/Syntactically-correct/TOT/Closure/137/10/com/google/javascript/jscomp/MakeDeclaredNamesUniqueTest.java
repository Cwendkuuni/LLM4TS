package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MakeDeclaredNamesUniqueTest {

 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
 private Node testNode;

 @Before
 public void setUp() {
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 testNode = NodeFactory.createScopedNode("testNode", Token.NEW_EXPR, null);
 }

 @Test
 public void testNameRenaming() {
 // Test case with no name conflicts.
 Node name1Node = NodeFactory.createNameNode("name1");
 Node var1Node = NodeFactory.createVarNode(NodeFactory.createNameNode("name1"), null, null, null);
 Node declNode = NodeFactory.createStatement(NodeFactory.createAssign(NodeFactory.createNameNode("x"), NodeFactory.createName(NodeFactory.createNameNode("name1")), null), null, null);
 testNode.addChild(declNode);
 testNode.addChild(name1Node);
 testNode.addChild(NodeFactory.createNameNode("name1"));
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(testNode));
 assertEquals("name1", testNode.getChild(2).getValue());

 // Test case with name conflicts.
 Node name2Node = NodeFactory.createNameNode("name1");
 Node var2Node = NodeFactory.createVarNode(NodeFactory.createNameNode("name1"), null, null, null);
 Node decl2Node = NodeFactory.createStatement(NodeFactory.createAssign(NodeFactory.createNameNode("x"), NodeFactory.createName(NodeFactory.createNameNode("name1")), null), null, null);
 testNode.addChild(decl2Node);
 testNode.addChild(name1Node);
 testNode.addChild(name2Node);
 testNode.addChild(NodeFactory.createNameNode("name1"));
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(testNode));
 assertEquals("name1_1", testNode.getChild(4).getValue());
 assertEquals("name1_2", testNode.getChild(6).getValue());
 }

 @Test
 public void testFunctionRenaming() {
 // Test case with no name conflicts in a function.
 Node functionNode = NodeFactory.createStatement(
 NodeFactory.createFunctionDeclaration(
 NodeFactory.createNameNode("func"),
 ImmutableSet.of(),
 NodeFactory.createBlock(
 ImmutableSet.of(
 NodeFactory.createStatement(NodeFactory.createAssign(NodeFactory.createNameNode("x"), NodeFactory.createName(NodeFactory.createNameNode("name1")), null), null, null)
 ),
 null,
 null
 ),
 null
 ),
 null,
 null,
 null
 );
 Node name1Node = NodeFactory.createNameNode("name1");
 testNode.addChild(functionNode);
 testNode.addChild(name1Node);
 testNode.addChild(NodeFactory.createNameNode("name1"));
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(testNode));
 assertEquals("name1", testNode.getChild(1).getFirstChild().getChild(0).getValue());

 // Test case with name conflicts in a function.
 Node func2Node = NodeFactory.createStatement(
 NodeFactory.createFunctionDeclaration(
 NodeFactory.createNameNode("func"),
 ImmutableSet.of(),
 NodeFactory.createBlock(
 ImmutableSet.of(
 NodeFactory.createStatement(NodeFactory.createAssign(NodeFactory.createNameNode("x"), NodeFactory.createName(NodeFactory.createNameNode("name1")), null), null, null),
 NodeFactory.createStatement(NodeFactory.createAssign(NodeFactory.createNameNode("x"), NodeFactory.createName(NodeFactory.createNameNode("name1")), null), null, null))
 ),
 null,
 null,
 null
 ),
 null,
 null,
 null
 );
 Node name2Node = NodeFactory.createNameNode("name1");
 testNode.addChild(func2Node);
 testNode.addChild(name1Node);
 testNode.addChild(name2Node);
 testNode.addChild(NodeFactory.createNameNode("name1"));
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(testNode));
 assertEquals("name1_0", testNode.getChild(2).getFirstChild().getChild(0).getValue());
 assertEquals("name1_1", testNode.getChild(2).getFirstChild().getChild(2).getValue());
 }

 @Test
 public void testGlobalRenaming() {
 // Test case with no name conflicts in the global scope.
 Node name3Node = NodeFactory.createNameNode("name1");
 testNode.addChild(name3Node);
 testNode.addChild(NodeFactory.createNameNode("name1"));
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(testNode), null, null);
 assertEquals("name1", testNode.getChild(1).getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForNonNodeArgument() {
 makeDeclaredNamesUnique.traverse("testNode", null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForNullChild() {
 Node childNode = null;
 Node parentNode = NodeFactory.createScopedNode("parentNode", Token.NEW_EXPR, null);
 parentNode.addChild(childNode);
 makeDeclaredNamesUnique.traverse(NodeTraversal.forTree(parentNode));
 }
}