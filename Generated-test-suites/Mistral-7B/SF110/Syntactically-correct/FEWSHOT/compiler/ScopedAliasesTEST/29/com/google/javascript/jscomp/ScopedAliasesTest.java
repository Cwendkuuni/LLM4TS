package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.CompilationUnit;
import org.apache.commons.cli.diagnostics.DiagnosticType;
import org.apache.commons.cli.impl.AbstractCompiler;
import org.apache.commons.cli.impl.StandardJarProcessor;
import org.apache.commons.cli.impl.check.NoChecks;
import org.apache.commons.cli.impl.jvm.JavaCompiler;
import org.apache.commons.cli.impl.jvm.JavaCompilerProvider;
import org.apache.commons.cli.impl.jvm.JavaFileObject;
import org.apache.commons.cli.impl.jvm.StandardFileObject;
import org.apache.commons.cli.process.JavaClassBytecodeProcessor;
import org.apache.commons.cli.process.JavaCompilerOptions;
import org.apache.commons.cli.process.StandardJavaFileManager;
import org.apache.commons.cli.process.StandardJavaFileObject;
import org.apache.commons.cli.util.AliasTransformation;
import org.apache.commons.cli.util.AliasTransformationHandler;
import org.apache.commons.cli.util.PreprocessorSymbolTable;
import org.apache.commons.cli.util.ScopedAliases;
import org.apache.commons.cli.util.SourcePosition;
import org.apache.commons.cli.util.Traversal;
import org.apache.commons.cli.util.alias.AliasUsage;
import org.apache.commons.cli.util.alias.AliasedNode;
import org.apache.commons.cli.util.alias.AliasedTypeNode;
import org.apache.commons.cli.util.alias.Traversal.ScopeCall;
import org.junit.Before;
import org.junit.Test;

import java.io.FileObject;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

    private AbstractCompiler compiler;
    private AliasTransformationHandler transformationHandler;
    private PreprocessorSymbolTable preprocessorSymbolTable;

    @Before
    public void setup() {
        JavaCompilerProvider javaCompilerProvider = new JavaCompilerProvider();
        JavaCompiler javaCompiler = javaCompilerProvider.getJavaCompiler();
        JavaCompilerOptions javaCompilerOptions = new JavaCompilerOptions();
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(javaCompilerOptions, null, null);
        StandardJarProcessor jarProcessor = new StandardJarProcessor();
        NoChecks checks = new NoChecks();
        StandardFileObject source = new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE);
        StandardJavaFileObject output = new StandardJavaFileObject(fileManager, "test.class", JavaFileObject.Kind.OUTPUT);

        CompilationUnit compilationUnit = new CompilationUnit(new SourcePosition(null, 0, null, null), source, null);
        fileManager.writeTo(output, new byte[0]);
        compilationUnit.addCompilationUnit(source, null, null);

        compiler = new AbstractCompiler(
            javaCompiler,
            fileManager,
            checks,
            new JavaFileObject[]{source},
            null,
            jarProcessor,
            null,
            null);

        transformationHandler = new AliasTransformationHandler();
        preprocessorSymbolTable = new PreprocessorSymbolTable();
    }

    @Test
    public void testScopedAliases() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        String sourceCode = "package test;\n" +
                "import static test.Utils.stripLeadingAndTrailingQuotes;\n" +
                "import static test.Utils.stripLeadingHyphens;\n" +
                "goog.scope(function() {\n" +
                "  var Util = {\n" +
                "    stripLeadingAndTrailingQuotes: function(str) {\n" +
                "      return Util.stripLeadingAndTrailingQuotes(str);\n" +
                "    },\n" +
                "    stripLeadingHyphens: function(str) {\n" +
                "      return Util.stripLeadingHyphens(str);\n" +
                "    }\n" +
                "  };\n" +
                "  test.testStripLeadingAndTrailingQuotes();\n" +
                "  test.testStripLeadingHyphens();\n" +
                "});\n" +
                "test.Utils = {\n" +
                "  stripLeadingAndTrailingQuotes: function(str) {\n" +
                "    return arguments[0];\n" +
                "  },\n" +
                "  stripLeadingHyphens: function(str) {\n" +
                "    return arguments[0].substring(1);\n" +
                "  }\n" +
                "};\n" +
                "function testStripLeadingAndTrailingQuotes() {\n" +
                "  Util.test('" + "'foo' " + "'bar'" + "');\n" +
                "}\n" +
                "function testStripLeadingHyphens() {\n" +
                "  Util.test('--foo--');\n" +
                "}\n" +
                "function test(str) {\n" +
                "  assertEquals('foo bar', Util.stripLeadingAndTrailingQuotes(str));\n" +
                "  assertEquals('f', Util.stripLeadingHyphens(str));\n" +
                "}\n";

        CompilationUnit testCompilationUnit = new CompilationUnit(new SourcePosition(null, 0, null, null), new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE), null);
        testCompilationUnit.addCompilationUnit(new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE), null, null);
        testCompilationUnit.setJavaClassBytecodeProcessor(new JavaClassBytecodeProcessor() {
            @Override
            public boolean processCode(int api, FileObject code, FileObject source, JavaCompiler javaCompiler) throws IOException {
                List<AliasUsage> aliasUsages = ((Traversal) javaCompiler.getOptions().getOption("ScopedAliasesTraversal")).getAliasUsages();
                for (AliasUsage aliasUsage : aliasUsages) {
                    if (aliasUsage instanceof AliasedNode) {
                        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
                        assertEquals(aliasedNode.getAliasDefinition().getString(), "test.Utils.stripLeadingAndTrailingQuotes", aliasedNode.getAliasReference().getString());
                    } else if (aliasUsage instanceof AliasedTypeNode) {
                        AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
                        assertEquals("test.Utils", aliasedTypeNode.getAliasName());
                        assertEquals("test.Utils", aliasedTypeNode.getAliasDefinition().getQualifiedName().substring(0, aliasedTypeNode.getAliasDefinition().getQualifiedName().lastIndexOf(".")));
                    }
                }
                return true;
            }
        });

        List<ScopeCall> scopeCalls = Lists.newArrayList();
        assertEquals(1, scopedAliases.process(null, new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE)).getAliasDefinitionsInOrder().size());
        assertEquals(1, scopedAliases.process(null, new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE)).getScopeCalls().size());
        assertEquals(1, scopeCalls.size());
        assertEquals(scopeCalls.get(0).getNode().getFirstChild().getLastChild().getLastChild().getLastChild().getFirstChild().getValue(), "function() {");

        compiler.compile(new SourcePosition(null, 0, null, null), sourceCode.getBytes());
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
        StandardJavaFileObject outputFileObject = (StandardJavaFileObject) javaCompiler.getStandardFileManager().getJavaFileForOutput(null, "test.class", null);
        fileManager.writeTo(outputFileObject, compiler.getOutput());
        byte[] compiledCode = fileManager.readBytes(outputFileObject, null);

        testCompilationUnit.setCompiledCode(compiledCode);
        testCompilationUnit.compile();
        assertTrue(testCompilationUnit.getDiagnostics().isEmpty());

        Method run = testCompilationUnit.getClass().getMethod("main", String[].class);
        run.invoke(null, new Object[]{null});
    }

    @Test
    public void testScopedAliasesErrors() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        String sourceCode = "package test;\n" +
                "goog.scope(function() {\n" +
                "  test.test();\n" +
                "});\n" +
                "function test() {\n" +
                "}\n";

        CompilationUnit testCompilationUnit = new CompilationUnit(new SourcePosition(null, 0, null, null), new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE), null);
        testCompilationUnit.addCompilationUnit(new StandardFileObject("test.java", JavaFileObject.Kind.SOURCE), null, null);
        testCompilationUnit.setJavaClassBytecodeProcessor(new JavaClassBytecodeProcessor() {
            @Override
            public boolean processCode(int api, FileObject code, FileObject source, JavaCompiler javaCompiler) throws IOException {
                List<AliasUsage> aliasUsages = ((Traversal) javaCompiler.getOptions().getOption("ScopedAliasesTraversal")).getAliasUsages();
                for (AliasUsage aliasUsage : aliasUsages) {
                    assertNull(aliasUsage);
                }
                List<DiagnosticType> diagnostics = testCompilationUnit.getDiagnostics();
                assertEquals(4, diagnostics.size());
                assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", diagnostics.get(0).getType());
                assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostics.get(1).getType());
                assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", diagnostics.get(2).getType());
                assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", diagnostics.get(3).getType());
                return true;
            }
        });

        compiler.compile(new SourcePosition(null, 0, null, null), sourceCode.getBytes());

        testCompilationUnit.setCompiledCode(compiler.getOutput());
        testCompilationUnit.compile();
        assertTrue(testCompilationUnit.getDiagnostics().isEmpty());
    }
}