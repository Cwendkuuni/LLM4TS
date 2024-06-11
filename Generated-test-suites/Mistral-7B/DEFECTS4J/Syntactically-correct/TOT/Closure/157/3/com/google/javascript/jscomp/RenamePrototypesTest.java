package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResponse;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RenamePrototypesTest {

  @Mock
  private Compiler compiler;

  @Mock
  private CompilerOptions options;

  @Mock
  private SourceFile input;

  @Mock
  private CompilerResponse response;

  @Mock
  private Node rootNode;

  @Mock
  private Node externsNode;

  @Mock
  private VariableMap prevUsedRenameMap;

  @Mock
  private AbstractCompiler abstractCompiler;

  @Test
  public void testRenameSimplePrototypeProperty() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var myProto = {\n" +
                                    "  oldProp: 42,\n" +
                                    "  newProp: function() {}\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);
    when(compiler.getRootNode()).thenReturn(rootNode);
    when(compiler.getExternsNode()).thenReturn(externsNode);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node newProtoNode = findNodeContaining("newProto", rootNode);
    assertThat(newProtoNode).isNotNull();
    Node newPropNode = findNodeContaining("newProp", newProtoNode);
    assertThat(newPropNode).isNotNull();
    assertThat(newPropNode.getType()).isEqualTo(Token.FUNCTIONDECL);
    Node oldPropNode = findNodeContaining("oldProp", newProtoNode);
    assertThat(oldPropNode).isNotNull();
    assertThat(oldPropNode.getType()).isEqualTo(Token.VARDECL);

    // Check that the old property was renamed
    Node oldPropValueNode = oldPropNode.getFirstChild();
    assertThat(oldPropValueNode.getType()).isEqualTo(Token.NUMBER);
    assertThat(oldPropValueNode.getString()).isEqualTo("42");

    // Check that the new property was not renamed
    Node newPropValueNode = newPropNode.getFirstChild();
    assertThat(newPropValueNode.getType()).isEqualTo(Token.FUNCTION);
  }

  @Test
  public void testRenameAggressivePrototypeProperty() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var myProto = {\n" +
                                    "  oldUpperProp: 42,\n" +
                                    "  oldMixedProp: 42,\n" +
                                    "  newProp: function() {}\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);
    when(compiler.getRootNode()).thenReturn(rootNode);
    when(compiler.getExternsNode()).thenReturn(externsNode);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node newProtoNode = findNodeContaining("newProto", rootNode);
    Node oldUpperPropNode = findNodeContaining("oldUpperProp", newProtoNode);
    Node oldMixedPropNode = findNodeContaining("oldMixedProp", newProtoNode);
    Node newPropNode = findNodeContaining("newProp", newProtoNode);

    // Check that oldUpperProp was renamed
    assertThat(oldUpperPropNode).isNotNull();
    assertThat(oldUpperPropNode.getType()).isEqualTo(Token.VARDECL);
    Node newUpperPropNode = findNodeContaining("UPPER_ Prop", newProtoNode);
    assertThat(newUpperPropNode).isNotNull();
    Node newUpperPropValueNode = newUpperPropNode.getFirstChild();
    assertThat(newUpperPropValueNode.getType()).isEqualTo(Token.NUMBER);
    assertThat(newUpperPropValueNode.getString()).isEqualTo("42");

    // Check that oldMixedProp was renamed
    assertThat(oldMixedPropNode).isNotNull();
    assertThat(oldMixedPropNode.getType()).isEqualTo(Token.VARDECL);
    Node newMixedPropNode = findNodeContaining("MIXED_ Prop", newProtoNode);
    assertThat(newMixedPropNode).isNotNull();
    Node newMixedPropValueNode = newMixedPropNode.getFirstChild();
    assertThat(newMixedPropValueNode.getType()).isEqualTo(Token.NUMBER);
    assertThat(newMixedPropValueNode.getString()).isEqualTo("42");

    // Check that newProp was not renamed
    assertThat(newPropNode).isNotNull();
    assertThat(newPropNode.getType()).isEqualTo(Token.FUNCTIONDECL);
  }

  @Test
  public void testRenamePrivatePrototypeProperty() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var myProto = {\n" +
                                    "  _oldProp: 42,\n" +
                                    "  newProp: function() {}\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);
    when(compiler.getRootNode()).thenReturn(rootNode);
    when(compiler.getExternsNode()).thenReturn(externsNode);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node newProtoNode = findNodeContaining("newProto", rootNode);
    Node _oldPropNode = findNodeContaining("_oldProp", newProtoNode);
    Node newPropNode = findNodeContaining("newProp", newProtoNode);

    // Check that _oldProp was not renamed
    assertThat(_oldPropNode).isNotNull();
    assertThat(_oldPropNode.getType()).isEqualTo(Token.VARDECL);
    Node _oldPropValueNode = _oldPropNode.getFirstChild();
    assertThat(_oldPropValueNode.getType()).isEqualTo(Token.NUMBER);
    assertThat(_oldPropValueNode.getString()).isEqualTo("42");

    // Check that newProp was not renamed
    assertThat(newPropNode).isNotNull();
    assertThat(newPropNode.getType()).isEqualTo(Token.FUNCTIONDECL);
  }

  @Test
  public void testRenamePrototypeWithSameNameInPrototypeAndObjectLit() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var myProto = {\n" +
                                    "  oldProp: 42,\n" +
                                    "};\n\n" +
                                    "var myObj = {\n" +
                                    "  oldProp: 42,\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);
    when(compiler.getRootNode()).thenReturn(rootNode);
    when(compiler.getExternsNode()).thenReturn(externsNode);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node myProtoNode = findNodeContaining("myProto", rootNode);
    Node myObjNode = findNodeContaining("myObj", rootNode);
    Node oldPropNodeMyProto = findNodeContaining("oldProp", myProtoNode);
    Node oldPropNodeMyObj = findNodeContaining("oldProp", myObjNode);

    // Check that oldProp in myProto was renamed
    assertThat(oldPropNodeMyProto).isNotNull();
    assertThat(oldPropNodeMyProto.getType()).isEqualTo(Token.VARDECL);
    Node newPropNodeMyProto = findNodeContaining("newProp", myProtoNode);
    assertThat(newPropNodeMyProto).isNotNull();
    Node newPropValueNodeMyProto = newPropNodeMyProto.getFirstChild();
    assertThat(newPropValueNodeMyProto.getType()).isEqualTo(Token.NUMBER);

    // Check that oldProp in myObj was not renamed
    assertThat(oldPropNodeMyObj).isNotNull();
    assertThat(oldPropNodeMyObj.getType()).isEqualTo(Token.VARDECL);
    Node oldPropValueNodeMyObj = oldPropNodeMyObj.getFirstChild();
    assertThat(oldPropValueNodeMyObj.getType()).isEqualTo(Token.NUMBER);
  }

  @Test
  public void testDoNotRenameBuiltInProperty() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var myProto = {\n" +
                                    "  indexOf: 42,\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);
    when(compiler.getRootNode()).thenReturn(rootNode);
    when(compiler.getExternsNode()).thenReturn(externsNode);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node newProtoNode = findNodeContaining("newProto", rootNode);
    Node indexOfNode = findNodeContaining("indexOf", newProtoNode);

    // Check that indexOf was not renamed
    assertThat(indexOfNode).isNotNull();
    assertThat(indexOfNode.getType()).isEqualTo(Token.VARDECL);
    Node indexOfValueNode = indexOfNode.getFirstChild();
    assertThat(indexOfValueNode.getType()).isEqualTo(Token.NUMBER);
  }

  @Test
  public void testDoNotRenamePrivateExternedProperty() throws IOException {
    // Set up the input code
    when(input.getName()).thenReturn("InputFile.js");
    when(input.getText()).thenReturn("var _myProto = {\n" +
                                    "  _privateProp: 42,\n" +
                                    "};");

    // Set up the compiler
    when(compiler.getLifeCycleStage()).thenReturn(Compiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getInputFile(input.getName())).thenReturn(input);
    when(compiler.getAbstractCompiler()).thenReturn(abstractCompiler);

    // Set up the compiler pass
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    // Set up the processor
    Node externsNode = mock(Node.class);
    Node rootNode = mock(Node.class);

    when(compiler.getExternsNode()).thenReturn(externsNode);
    when(compiler.getRootNode()).thenReturn(rootNode);

    // Process the test input
    when(compiler.compile(input, rootNode)).thenReturn(response);

    CompilerPass pass = renamePrototypes;
    compiler.process(pass, externsNode, rootNode);

    // Check the results
    Node _myProtoNode = findNodeContaining("_myProto", rootNode);
    Node _privatePropNode = findNodeContaining("_privateProp", _myProtoNode);

    // Check that _privateProp was not renamed
    assertThat(_privatePropNode).isNotNull();
    assertThat(_privatePropNode.getType()).isEqualTo(Token.VARDECL);
    Node _privatePropValueNode = _privatePropNode.getFirstChild();
    assertThat(_privatePropValueNode.getType()).isEqualTo(Token.NUMBER);
  }

  private Node findNodeContaining(String name, Node node) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.STRING && child.getString().equals(name)) {
        return child;
      }
      Node result = findNodeContaining(name, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  static class CodingConvention {
    public boolean isExported(String name) {
      return false;
    }

    public boolean isPrivate(String name) {
      return name.startsWith("_");
    }
  }
}