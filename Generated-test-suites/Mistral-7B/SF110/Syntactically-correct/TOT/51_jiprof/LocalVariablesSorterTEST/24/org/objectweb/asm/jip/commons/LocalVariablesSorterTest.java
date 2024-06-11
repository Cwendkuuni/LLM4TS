package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    @Test
    public void testLocalVariablesSorter() throws Exception {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    int b;\n" +
                "    float c;\n" +
                "    long d;\n" +
                "    static int e;\n" +
                "    void method(int i, float j, double k, int l, String m) {\n" +
                "        int a = 1;\n" +
                "        int b = 2;\n" +
                "        float c = 3.0f;\n" +
                "        long d = 4L;\n" +
                "        int e = 5;\n" +
                "        int f = 6;\n" +
                "        int g = 7;\n" +
                "        int h = 8;\n" +
                "        int i = 9;\n" +
                "        int j = 10;\n" +
                "        String k = \"test\";\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), reader.getClassName().replace("/", "."), new MethodVisitor(0, new ClassWriter(0)));
        reader.accept(localVariablesSorter);

        byte[] sortedClassBytes = localVariablesSorter.getBytecode();

        byte[] expectedClassBytes = compileTestClass("public class Test {\n" +
                "    long d;\n" +
                "    long e;\n" +
                "    float c;\n" +
                "    long f;\n" +
                "    int a;\n" +
                "    int b;\n" +
                "    int g;\n" +
                "    int h;\n" +
                "    int i;\n" +
                "    int j;\n" +
                "    int k;\n" +
                "    int l;\n" +
                "    String m;\n" +
                "    static int e;\n" +
                "    void method(int j, float c, double d, int b, int a, int i, int g, int h, int f, int k, int l, String m) {\n" +
                "        long d = 1;\n" +
                "        long e = 2;\n" +
                "        float c = 3.0f;\n" +
                "        int a = 4;\n" +
                "        int b = 5;\n" +
                "        int g = 6;\n" +
                "        int h = 7;\n" +
                "        int i = 8;\n" +
                "        int j = 9;\n" +
                "        int k = 10;\n" +
                "        int l = 11;\n" +
                "    }\n" +
                "}");

        assertEquals(expectedClassBytes.length, sortedClassBytes.length);
        assertEquals(new String(expectedClassBytes), new String(sortedClassBytes));

        // Test with invalid opcode
        testInvalidOpcode();

        // Test with invalid type
        testInvalidType();

        // Test with null method visitor
        testNullMethodVisitor();

        // Test with null type argument
        testNullTypeArgument();

        // Test with negative index
        testNegativeIndex();
    }

    private void testInvalidOpcode() {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    \n" +
                "    void method() {\n" +
                "        int b = 1;\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), reader.getClassName().replace("/", "."), null);
        try {
            reader.accept(localVariablesSorter);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private void testInvalidType() {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    void method() {\n" +
                "        int b = 1;\n" +
                "        String c = \"test\";\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), reader.getClassName().replace("/", "."), new MethodVisitor(0, new ClassWriter(0)));
        try {
            localVariablesSorter.visitVarInsn(-1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void testNullMethodVisitor() {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    void method() {\n" +
                "        int b = 1;\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), reader.getClassName().replace("/", "."), null);
        try {
            localVariablesSorter.accept(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private void testNullTypeArgument() {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    void method() {\n" +
                "        int b = 1;\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), null, new MethodVisitor(0, new ClassWriter(0)));
        try {
            localVariablesSorter.accept(reader);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private void testNegativeIndex() {
        String testClass = "public class Test {\n" +
                "    int a;\n" +
                "    void method() {\n" +
                "        int b = 1;\n" +
                "        int c = 2;\n" +
                "    }\n" +
                "}";

        byte[] originalClassBytes = compileTestClass(testClass);

        ClassReader reader = new ClassReader(originalClassBytes);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(reader.getAccess(), reader.getClassName().replace("/", "."), new MethodVisitor(0, new ClassWriter(0)));
        reader.accept(localVariablesSorter);

        try {
            localVariablesSorter.remap(-1, Type.INT_TYPE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            localVariablesSorter.remap(Integer.MIN_VALUE, Type.INT_TYPE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private byte[] compileTestClass(String testClass) throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        ClassReader classReader = new ClassReader(testClass.getBytes());
        MethodVisitor methodVisitor = new MethodVisitor(0, classWriter);
        classReader.accept(methodVisitor);
        return classWriter.toByteArray();
    }
}