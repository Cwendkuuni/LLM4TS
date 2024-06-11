import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.inlineObjectLiterals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.Option;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import java.util.List;

public class InlineObjectLiteralsTest extends CompilerTestBase {

  public void testInlineObjectLiterals() {
    String input =
        "function Foo() {" +
            "this.foo = {" +
                "a: 1," +
                "bar: function() {" +
                "  console.log('bar');" +
                "}" +
            "};" +
            "this.baz = {" +
                "qux: true" +
            "};" +
            "this.quux = function() { return this.foo.bar(); }" +
            "}" +
            "var obj = new Foo();";

    CompilerOptions options = new CompilerOptions();
    options.setOption(Option.COMPATIBLE_WITH_ES5, true);
    options.setOption(Option.ALLOW_THIS_ASSIGNMENT, true);

    AbstractCompiler compiler = getCompiler();
    CompilerPassManager manager = new CompilerPassManager(compiler, options);

    Node externs = Node.newText("function foo() {}");
    Node root = parseJs(input);
    manager.runPasses(externs, root);

    List<CompilerPass> passes = ImmutableList.of(inlineObjectLiterals(null, null));
    manager.runPasses(externs, root, passes);

    assertCompilesTo("function Foo() {" +
        "this.foo = {" +
            "a: 1," +
            "bar: console" +
            ": {" +
                "call: function(context, thisArg, args) {" +
                    "context.console.log('bar');" +
                "}" +
            "};" +
            "_foo_a: 1," +
            "_foo_bar: null" +
        "};" +
        "this.baz = {" +
            "qux: true" +
        "};" +
        "this.quux = function() {" +
            "return this.foo.bar.call(this);" +
        "};" +
        "var obj = new Foo();", root);

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertFalse(referenceCollection.isWellDefined());

    for (Reference reference : referenceCollection.references) {
      assertEquals("obj.foo", reference.toString());
      assertEquals("obj._foo_a", reference.getInitializingDeclaration().getChildAtIndex(1).toString());
      assertEquals("obj._foo_bar", reference.getInitializingDeclaration().getChildAtIndex(2).toString());
      assertEquals("obj.baz", reference.getChildAtIndex(1).toString());
      assertEquals("obj._baz_qux", reference.getChildAtIndex(2).toString());
    }
  }

  public static class InliningBehavior implements Behavior {

    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
      for (Var v : t.getScope().getVars()) {
        if (isVarInlineForbidden(v)) continue;

        ReferenceCollection referenceInfo = referenceMap.getReferences(v);
        if (isInlinableObject(referenceInfo.references)) {
          // Blacklist the object itself, as well as any other values
          // it refers to, since they will have been moved around.
          staleVars.add(v);

          Reference declaration = referenceInfo.references.get(0);
          Reference init = referenceInfo.getInitializingReference();

          splitObject(v, declaration, init, referenceInfo);
        }
      }
    }

    // Test helper methods

    private boolean isVarInlineForbidden(Var var) {
      // Variables defined in externs or exported should not be inlined.
      return var.isGlobal() || var.isExtern() || compiler.getCodingConvention().isExported(var.name);
    }

    private boolean isInlinableObject(List<Reference> refs) {
      // Only inline objects that have no direct references.
      return refs.isEmpty();
    }

    private void splitObject(Var v, Reference declaration,
                             Reference init,
                             ReferenceCollection referenceInfo) {
      Map<String, String> varmap = computeVarList(v, referenceInfo);

      Map<String, Node> initvals = Maps.newHashMap();
      fillInitialValues(init, initvals);

      for (Map.Entry<String, String> entry : varmap.entrySet()) {
        Node val = initvals.get(entry.getKey());
        Node varnode = NodeUtil.newVarNode(entry.getValue(), val);
        v.getScope().getRootNode()
            .getLastChild()
            .getFirstChild()
            .addChildBefore(varnode, v.getScope().getRootNode().getLastChild().getFirstChild());
      }

      if (init != null) {
        v.getScope().getRootNode().getLastChild().removeChild(v.getScope().getRootNode().getLastChild().getChildAtIndex(v.getIndex()));
      }

      for (Reference ref : referenceInfo.references) {
        if (ref.isLvalue()) {
          replaceAssignmentExpression(v, ref, varmap);
        } else if (ref.getParent().getType() == Token.VAR) {
          ref.getGrandparent().removeChild(ref.getParent());
        } else {
          Node getprop = ref.getParent();
          String var = getprop.getChildAtIndex(1).getString();
          Node replacement = NodeUtil.newVarNode(varmap.get(var), null);
          getprop.replaceChild(getprop.getChildAtIndex(1), replacement);
        }
      }

      compiler.reportCodeChange();
    }

    private Map<String, String> computeVarList(Var v, ReferenceCollection referenceInfo) {
      Map<String, String> varmap = Maps.newLinkedHashMap();

      for (Reference ref : referenceInfo.references) {
        if (ref.isLvalue() || ref.isInitializingDeclaration()) {
          Node val = ref.getAssignedValue();
          if (val != null) {
            Preconditions.checkState(val.getType() == Token.OBJECTLIT);
            for (Node child = val.getFirstChild(); child != null;
                 child = child.getNext()) {
              String varname = child.getString();
              if (varmap.containsKey(varname)) {
                continue;
              }

              String var = VAR_PREFIX + varname + "_" + safeNameIdSupplier.get();
              varmap.put(varname, var);
            }
          }
        } else if (ref.getParent().getType() == Token.VAR) {
          // This is the var. There is no value.
        } else {
          Node getprop = ref.getParent();
          Preconditions.checkState(getprop.getType() == Token.GETPROP);

          // The key being looked up in the original map.
          String varname = getprop.getLastChild().getString();
          if (varmap.containsKey(varname)) {
            continue;
          }

          String var = VAR_PREFIX + varname + "_" + safeNameIdSupplier.get();
          varmap.put(varname, var);
        }
      }

      return varmap;
    }

    private void fillInitialValues(Reference init, Map<String, Node> initvals) {
      if (init == null) return;

      Node object = init.getAssignedValue();
      Preconditions.checkState(object.getType() == Token.OBJECTLIT);

      for (Node key = object.getFirstChild(); key != null; key = key.getNext()) {
        initvals.put(key.getString(), key.removeFirstChild());
      }
    }

    private void replaceAssignmentExpression(Var v, Reference ref,
                                             Map<String, String> varmap) {
      // Compute all of the assignments necessary
      List<Node> nodes = Lists.newArrayList();
      Node val = ref.getAssignedValue();
      Preconditions.checkState(val.getType() == Token.OBJECTLIT);
      Set<String> all = Sets.newLinkedHashSet(varmap.keySet());
      for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        Node value = key.removeFirstChild();
        if (value != null) {
          nodes.add(
            new Node(Token.ASSIGN,
                     NodeUtil.newVarNode(varmap.get(var), value),
                     NodeUtil.newUndefinedNode(null)));
          all.remove(var);
        } else {
          nodes.add(NodeUtil.newVarNode(varmap.get(var), null));
        }
      }

      // TODO(user): Better source information.
      for (String var : all) {
        nodes.add(
          new Node(Token.ASSIGN,
                   NodeUtil.newVarNode(varmap.get(var), null),
                   NodeUtil.newUndefinedNode(null)));
      }

      // All assignments evaluate to true, so make sure that the
      // expr statement evaluates to true in case it matters.
      nodes.add(new Node(Token.TRUE));

      // Join these using COMMA.  A COMMA node must have 2 children, so we
      // create a tree. In the tree the first child be the COMMA to match
      // the parser, otherwise tree equality tests fail.
      nodes = Lists.reverse(nodes);
      Node replacement = new Node(Token.COMMA);
      Node cur = replacement;
      int i;
      for (i = 0; i < nodes.size() - 2; i++) {
        cur.addChildToFront(nodes.get(i));
        Node t = new Node(Token.COMMA);
        cur.addChildToFront(t);
        cur = t;
      }
      cur.addChildToFront(nodes.get(i));
      cur.addChildToFront(nodes.get(i + 1));

      ref.getParent().replaceChild(ref, replacement);
    }
  }
}