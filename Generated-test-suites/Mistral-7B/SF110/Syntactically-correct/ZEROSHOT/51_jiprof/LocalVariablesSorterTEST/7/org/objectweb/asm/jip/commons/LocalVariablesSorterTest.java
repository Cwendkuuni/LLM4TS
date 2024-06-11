package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testLocalVariablesSorter() {
        String javaCode = "public class TestClass {\n" +
                "    int a;\n" +
                "    int b;\n" +
                "    int c;\n" +
                "    float d;\n" +
                "    long e;\n" +
                "    double f;\n" +
                "    String g;\n" +
                "    void test() {\n" +
                "        a = 1;\n" +
                "        b = 2;\n" +
                "        c = 3;\n" +
                "        d = 3.14f;\n" +
                "        e = 4L;\n" +
                "        f = 3.1415926536;\n" +
                "        g = \"Hello World\";\n" +
                "    }\n" +
                "}";

        ClassReader classReader = new ClassReader(javaCode.getBytes());

        MethodVisitor methodVisitor = new MethodVisitor(0, null, null, null) {
            @Override
            public void visitCode() {}

            @Override
            public void visitVarInsn(int opcode, int var) {}

            @Override
            public void visitIincInsn(int var, int increment) {}

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {}

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {}

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {}
        };

        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(AccessFlag.PUBLIC, "void test()V", methodVisitor);

        ClassReader updatedClassReader = new ClassReader(classReader.getBytes());
        MethodVisitor updatedMethodVisitor = updatedClassReader.visitMethod(AccessFlag.PUBLIC, "test", "()V", null, null);
        LocalVariablesSorter updatedLocalVariablesSorter = new LocalVariablesSorter(AccessFlag.PUBLIC, "void test()V", updatedMethodVisitor);

        int[] expectedMapping = {-1, 0, 1, 2, 3, 4, 5, 6};
        Object[] expectedNewLocals = {Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE, String.class, Opcodes.TOP};

        for (int i = 0; i < expectedMapping.length; i++) {
            int actualMapping = localVariablesSorter.mapping[i * 2 + LocalVariablesSorter.OBJECT_TYPE.getSize()];
            assertEquals(expectedMapping[i], actualMapping);
        }

        Object[] actualNewLocals = localVariablesSorter.newLocals;
        assertArrayEquals(expectedNewLocals, actualNewLocals);

        int[] updatedExpectedMapping = {-1, 0, 1, 1, 2, 3, 4, 5};
        Object[] updatedExpectedNewLocals = {Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE, String.class, Opcodes.TOP};

        for (int i = 0; i < updatedExpectedMapping.length; i++) {
            int actualMapping = updatedLocalVariablesSorter.mapping[i * 2 + LocalVariablesSorter.OBJECT_TYPE.getSize()];
            assertEquals(updatedExpectedMapping[i], actualMapping);
        }

        Object[] updatedActualNewLocals = updatedLocalVariablesSorter.newLocals;
        assertArrayEquals(updatedExpectedNewLocals, updatedActualNewLocals);
    }
}