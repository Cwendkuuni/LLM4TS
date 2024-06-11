import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.CompressionMode;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversingTreeProcessor;
import com.google.javascript.jscomp.NodeTraversingTreeProcessor.TreeProcessor;
import com.google.javascript.jscomp.RewrittenSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import static com.google.common.base.FunctionEvictingPrecomputingMap.map;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

 @Test
 public void testIsDef() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isDef(x)) {\n" +
 "    return x;\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isDef".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsDefUndefined() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isDef(x)) {\n" +
 "    return x;\n" +
 "  } else {\n" +
 "    return 'undefined';\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isDef".equals(node.getLastChild().getString())) {
 assertNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsDefNotNull() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isDefAndNotNull(x)) {\n" +
 "    return x;\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversalTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isDefAndNotNull".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsString() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isString(x)) {\n" +
 "    return x.length;\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isString".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("string"));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsBoolean() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isBoolean(x)) {\n" +
 "    return !x;\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isBoolean".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("boolean"));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsNumber() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isNumber(x)) {\n" +
 "    return Math.floor(x / 2);\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isNumber".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("number"));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsFunction() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isFunction(x)) {\n" +
 "    return x.call(null, 'test');\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isFunction".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("function"));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsArray() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isArray(x)) {\n" +
 "    return x[0];\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isArray".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("Array"));
 }
 }
 };

 processor.visit(tree);
 }

 @Test
 public void testIsObject() throws IOException {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setOutputFile("output.js");
 options.setCompilerFlags("-DGOOG_ISDEF=true");
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setSourceMap(true);
 options.setCompressionLevel(CompressionMode.SIMPLE);

 SourceFile input = createSourceFile("input.js",
 "function foo(x) {\n" +
 "  if (goog.isObject(x)) {\n" +
 "    return x.toString();\n" +
 "  }\n" +
 "}");

 RewrittenSourceFile rewritten = compiler.compile(input, options);
 Node tree = rewritten.getTree();

 TreeProcessor processor = new NodeTraversingTreeProcessor() {
 @Override
 public void process(NodeTraversingTreeProcessor.TraversalInfo info, Node node) {
 if (node.getType() == Node.CALL && node.getChildCount() == 2 &&
 node.getFirstChild().getType() == Node.GETPROP &&
 "goog".equals(node.getFirstChild().getString()) &&
 "isObject".equals(node.getLastChild().getString())) {
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier()));
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName());
 assertNotNull(info.getFlowScope().getVariable(node.getLastChild().getIdentifier())
 .getType().getDisplayName().startsWith("Object"));
 }
 }
 };

 processor.visit(tree);
 }

 private SourceFile createSourceFile(String name, String code) {
 return new SourceFile(new File(name), null, code, null);
 }
}