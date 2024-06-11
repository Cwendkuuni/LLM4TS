package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest extends CompilerTestCase {

  public InlineObjectLiteralsTest() {
    super(new CompilerOptions() {{
      setAssumesequals(true);
      setOutputFile("inlineObjectLiteralsTestOutput.js");
    }});
  }

  @Override
  protected CompilerPass getProcessor(AbstractCompiler compiler) {
    return new InlineObjectLiterals(compiler, () -> "inlineObjectLiteralsTestVarId_") {
      @Override
      public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        super.afterExitScope(t, referenceMap);
        for (Iterator<Var> it = t.getScope().getVars(); it.hasNext();) {
          Var v = it.next();
          if (this.staleVars.contains(v)) {
            System.out.println("Found stale variable: " + v.name);
          }
        }
      }
    };
  }

  public void testInlineObjectLiterals() {
    String source =
        "function Foo() {" +
        "  this.a = {b: 1, c: 2};" +
        "  this.b = {d: 3, e: 4};" +
        "  this.print = function() {" +
        "    this.a.print();" +
        "  };" +
        "}" +
        "Foo.prototype.print = function() {" +
        "  this.b.print();" +
        "}" +
        "Foo.prototype.c = function() {" +
        "  var obj = {a: 1, b: 2};" +
        "  this.x = obj.a;" +
        "}" +
        "var foo = new Foo();";

    Node root = parseJs(source);
    process(root);
    assertEquals(
        "function Foo() {" +
        "  var a_b_1_inlineObjectLiteralsTestVarId_0 = 1;" +
        "  var a_c_1_inlineObjectLiteralsTestVarId_1 = 2;" +
        "  var b_d_1_inlineObjectLiteralsTestVarId_2 = 3;" +
        "  var b_e_1_inlineObjectLiteralsTestVarId_3 = 4;" +
        "  this.a = {" +
        "    b: a_b_1_inlineObjectLiteralsTestVarId_0," +
        "    c: a_c_1_inlineObjectLiteralsTestVarId_1" +
        "  };" +
        "  this.b = {" +
        "    d: b_d_1_inlineObjectLiteralsTestVarId_2," +
        "    e: b_e_1_inlineObjectLiteralsTestVarId_3" +
        "  }" +
        "  this.print = function() {" +
        "    this.a.print();" +
        "  };" +
        "}" +
        "Foo.prototype.print = function() {" +
        "  this.b.print();" +
        "}" +
        "Foo.prototype.c = function() {" +
        "  var a_1_inlineObjectLiteralsTestVarId_4 = 1;" +
        "  var b_1_inlineObjectLiteralsTestVarId_5 = 2;" +
        "  this.x = a_1_inlineObjectLiteralsTestVarId_4;" +
        "}" +
        "var foo_1_inlineObjectLiteralsTestVarId_6 = new Foo();",
        getSource(root));
  }

  public void testInlineObjectLiteralsWithFunctions() {
    String source =
        "function Foo() {" +
        "  this.a = {" +
        "    b: function() {" +
        "      return 1;" +
        "    }," +
        "    c: function() {" +
        "      return 2;" +
        "    }" +
        "  };" +
        "}" +
        "Foo.prototype.print = function() {" +
        "  this.a.b();" +
        "}" +
        "Foo.prototype.c = function() {" +
        "  var obj = {b: function() {" +
        "      return 3;" +
        "    }, c: function() {" +
        "      return 4;" +
        "    }};" +
        "  this.x = obj.b();" +
        "}" +
        "var foo = new Foo();";

    Node root = parseJs(source);
    process(root);
    assertEquals(
        "function Foo() {" +
        "  var a_b_function_1_inlineObjectLiteralsTestVarId_0 = function() {" +
        "      return 1;" +
        "    };" +
        "  var a_c_function_1_inlineObjectLiteralsTestVarId_1 = function() {" +
        "      return 2;" +
        "    };" +
        "  var a_1_inlineObjectLiteralsTestVarId_2 = {" +
        "    b: a_b_function_1_inlineObjectLiteralsTestVarId_0," +
        "    c: a_c_function_1_inlineObjectLiteralsTestVarId_1" +
        "  };" +
        "  this.a = a_1_inlineObjectLiteralsTestVarId_2;" +
        "}" +
        "Foo.prototype.print = function() {" +
        "  this.a.b();" +
        "}" +
        "Foo.prototype.c = function() {" +
        "  var b_function_2_inlineObjectLiteralsTestVarId_3 = function() {" +
        "      return 3;" +
        "    };" +
        "  var c_function_2_inlineObjectLiteralsTestVarId_4 = function() {" +
        "      return 4;" +
        "    };" +
        "  var obj_1_inlineObjectLiteralsTestVarId_5 = {" +
        "    b: b_function_2_inlineObjectLiteralsTestVarId_3," +
        "    c: c_function_2_inlineObjectLiteralsTestVarId_4" +
        "  };" +
        "  this.x = obj_1_inlineObjectLiteralsTestVarId_5.b();" +
        "}" +
        "var foo_1_inlineObjectLiteralsTestVarId_6 = new Foo();",
        getSource(root));
  }

  public void testInlineObjectLiteralsWithExportedVariables() {
    String source =
        "var a = {" +
        "  b: 1," +
        "  c: 2" +
        "};" +
        "export let x = a;";

    Node root = parseJs(source);
    process(root);
    assertEquals(
        "var a_1_inlineObjectLiteralsTestVarId_0 = {" +
        "  b: 1," +
        "  c: 2" +
        "};" +
        "export let x_1_inlineObjectLiteralsTestVarId_1 = a_1_inlineObjectLiteralsTestVarId_0.b;",
        getSource(root));
  }

  public void testInlineObjectLiteralsWithSelfReferentialAssignments() {
    String source =
        "function Foo() {" +
        "  this.a = {" +
        "    b: this.a" +
        "  }";
    Node root = parseJs(source);
    assertThrows(IllegalArgumentException.class, () -> process(root));
  }

  public void testInlineObjectLiteralsWithGetterSetters() {
    String source =
        "function Foo() {" +
        "  this.a = {" +
        "    get bar() {" +
        "      return 3;" +
        "    }," +
        "    set bar(val) {" +
        "      this.b = val;" +
        "    }" +
        "  }" +
        "}" +
        "var foo = new Foo();" +
        "foo.a.bar = 1;";

    Node root = parseJs(source);
    process(root);
    assertEquals(
        "function Foo() {" +
        "  this.a = {" +
        "    get bar_1_inlineObjectLiteralsTestVarId_0() {" +
        "      return 3;" +
        "    }," +
        "    set bar_1_inlineObjectLiteralsTestVarId_1(val) {" +
        "      this.b_1_inlineObjectLiteralsTestVarId_2 = val;" +
        "    }" +
        "  }" +
        "  this.a = {" +
        "    b: this.b_1_inlineObjectLiteralsTestVarId_2" +
        "  }" +
        "}" +
        "var foo_1_inlineObjectLiteralsTestVarId_3 = new Foo();" +
        "foo_1_inlineObjectLiteralsTestVarId_3.a.bar = 1;",
        getSource(root));
  }

  public void testInlineObjectLiteralsWithLocalVariables() {
    String source =
        "function Foo() {" +
        "  var a = {" +
        "    b: 1," +
        "    c: 2" +
        "  }" +
        "  function bar() {" +
        "    var obj = a;" +
        "  }" +
        "}" +
        "var foo = new Foo();";

    Node root = parseJs(source);
    process(root);
    assertEquals(
        "function Foo() {" +
        "  var a_1_inlineObjectLiteralsTestVarId_0 = {" +
        "    b: 1," +
        "    c: 2" +
        "  };" +
        "  function bar() {" +
        "    var obj_1_inlineObjectLiteralsTestVarId_1 = a_1_inlineObjectLiteralsTestVarId_0;" +
        "  }" +
        "  this.a = a_1_inlineObjectLiteralsTestVarId_0;" +
        "}" +
        "var foo_1_inlineObjectLiteralsTestVarId_2 = new Foo();",
        getSource(root));
  }
}