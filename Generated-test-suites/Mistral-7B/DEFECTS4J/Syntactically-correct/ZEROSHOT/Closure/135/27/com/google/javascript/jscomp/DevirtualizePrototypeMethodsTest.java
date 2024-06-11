package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Externs;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private SimpleDefinitionFinder defFinder;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    defFinder = new SimpleDefinitionFinder(compiler);
    typeRegistry = mock(JSTypeRegistry.class);
    when(compiler.getTypeRegistry()).thenReturn(typeRegistry);
  }

  @Test
  public void testIsCall() {
    Node callNode = Node.newCall("A.prototype.accumulate", Lists.newArrayList(
        Node.newThis(),
        Node.newNumber(2)
    ));
    UseSite callSite = new UseSite(callNode, null, null, null, null);

    assertTrue(DevirtualizePrototypeMethods.isCall(callSite));

    Node nonCallNode = Node.newCall("A.prototype.accumulate", Lists.newArrayList());
    assertFalse(DevirtualizePrototypeMethods.isCall(new UseSite(nonCallNode, null, null, null, null)));
  }

  @Test
  public void testIsPrototypeMethodDefinition() {
    Node prototypeMethodNode = Node.newCall("A.prototype.accumulate = function(value) {...}", Lists.newArrayList());

    assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(prototypeMethodNode));

    Node nonPrototypeMethodNode = Node.newCall("A.prototype.accumulate = function() {...}", Lists.newArrayList());
    assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(nonPrototypeMethodNode));
  }

  @Test
  public void testGetRewrittenMethodName() {
    String originalMethodName = "accumulate";
    String expectedNewMethodName = "JSCompiler_StaticMethods_accumulate";

    String newMethodName = new DevirtualizePrototypeMethods().getRewrittenMethodName(originalMethodName);

    assertEquals(expectedNewMethodName, newMethodName);
  }

  @Test
  public void testProcess() {
    Node root = Node.newProgram(
        Node.newExterns(new Externs(), Lists.newArrayList()),
        Node.newCall("A.prototype.accumulate = function(value) {...}", Lists.newArrayList()),
        Node.newCall("var total = a.accumulate(2)", Lists.newArrayList())
    );

    Node externs = root.getFirstChild();
    DefinitionSite definitionSite = new DefinitionSite(
        Node.newCall("A.prototype.accumulate = function(value) {...}", Lists.newArrayList()),
        null,
        null,
        null,
        null
    );

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(externs, root);

    ArgumentCaptor<Definition> definitionArgumentCaptor = ArgumentCaptor.forClass(Definition.class);
    devirtualizePrototypeMethods.rewriteDefinitionIfEligible(definitionArgumentCaptor.capture(), defFinder);

    Definition definition = definitionArgumentCaptor.getValue();
    assertEquals(definitionSite.node, definition.getRValue().getFirstChild());
    assertEquals("accumulate", definitionSite.node.getLastChild().getString());
    assertEquals("JSCompiler_StaticMethods_accumulate", definition.getRValue().getString());
  }

  @Test
  public void testIsEligibleDefinition() {
    Node root = Node.newProgram(
        Node.newExterns(new Externs(), Lists.newArrayList()),
        Node.newCall("A.prototype.accumulate = function(value) {...}", Lists.newArrayList()),
        Node.newCall("var total = a.accumulate(2)", Lists.newArrayList()),
        Node.newCall("B.prototype.accumulate = function(value) {...}", Lists.newArrayList())
    );

    DefinitionSite definitionSiteA = new DefinitionSite(
        Node.newCall("A.prototype.accumulate = function(value) {...}", Lists.newArrayList()),
        null,
        null,
        null,
        null
    );
    DefinitionSite definitionSiteB = new DefinitionSite(
        Node.newCall("B.prototype.accumulate = function(value) {...}", Lists.newArrayList()),
        null,
        null,
        null,
        null
    );

    SimpleDefinitionFinder simpleDefinitionFinder = new SimpleDefinitionFinder(compiler);
    simpleDefinitionFinder.process(externs, root);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

    assertTrue(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSiteA));
    assertFalse(devirtualizePrototypeMethods.isEligibleDefinition(simpleDefinitionFinder, definitionSiteB));
  }
}