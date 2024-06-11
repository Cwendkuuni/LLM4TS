import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypesTest.createRenamePrototypes;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    char[] reservedCharacters = new char[0];
    VariableMap prevUsedRenameMap = null;

    RenamePrototypes renamer = createRenamePrototypes(compiler, false,
        reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("input.js", "input.js");
    Node root = Node.compile(input.getInputText());

    renamer.process(null, root);

    Map<String, String> expected = new HashMap<String, String>();
    expected.put("getBar", "get_getBar");
    expected.put("prototype", "prototype_");

    assertEquals(expected, renamer.getPropertyMap().getMap());
  }

  @Test
  public void testAggressiveRenaming() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    char[] reservedCharacters = new char[0];
    VariableMap prevUsedRenameMap = null;

    RenamePrototypes renamer = createRenamePrototypes(compiler, true,
        reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("input.js", "input.js");
    Node root = Node.compile(input.getInputText());

    renamer.process(null, root);

    Map<String, String> expected = new HashMap<String, String>();
    expected.put("getBar", "get_getBar__");
    expected.put("prototype", "prototype_");

    assertEquals(expected, renamer.getPropertyMap().getMap());
  }

  @Test
  public void testReservedNames() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    char[] reservedCharacters = new char[] {'_'};
    VariableMap prevUsedRenameMap = null;

    RenamePrototypes renamer = createRenamePrototypes(compiler, false,
        reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("input.js", "input.js");
    Node root = Node.compile(input.getInputText());

    renamer.process(null, root);

    Map<String, String> expected = new HashMap<String, String>();
    expected.put("indexOf", "indexOf_");
    expected.put("getBar", "get_getBar");
    expected.put("prototype", "prototype_");

    assertEquals(expected, renamer.getPropertyMap().getMap());
  }

  @Test
  public void testNoRename() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    char[] reservedCharacters = null;
    VariableMap prevUsedRenameMap = null;

    RenamePrototypes renamer = createRenamePrototypes(compiler, false,
        reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("input.js", "input.js");
    Node root = Node.compile(input.getInputText());

    renamer.process(null, root);

    Map<String, String> expected = new HashMap<String, String>();
    expected.put("getBar", "getBar");
    expected.put("prototype", "prototype");

    assertEquals(expected, renamer.getPropertyMap().getMap());
  }

  @Test
  public void testNoRenameAggressive() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // do nothing
      }
    };
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);

    char[] reservedCharacters = null;
    VariableMap prevUsedRenameMap = null;

    RenamePrototypes renamer = createRenamePrototypes(compiler, true,
        reservedCharacters, prevUsedRenameMap);

    CompilerInput input = new CompilerInput("input.js", "input.js");
    Node root = Node.compile(input.getInputText());

    renamer.process(null, root);

    Map<String, String> expected = new HashMap<String, String>();
    expected.put("getBar", "getBar");
    expected.put("prototype", "prototype");

    assertEquals(expected, renamer.getPropertyMap().getMap());
  }

  private static RenamePrototypes createRenamePrototypes(
      AbstractCompiler compiler, boolean aggressiveRenaming,
      char[] reservedCharacters, VariableMap prevUsedRenameMap) {
    return new RenamePrototypes(compiler, aggressiveRenaming,
        reservedCharacters, prevUsedRenameMap);
  }
}