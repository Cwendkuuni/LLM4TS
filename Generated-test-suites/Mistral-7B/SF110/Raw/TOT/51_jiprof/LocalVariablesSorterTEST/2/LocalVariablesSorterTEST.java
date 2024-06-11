package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testSortLocalVariables() throws Exception {
        String javaCode = "public class TestClass {\n" +
                "    int a;\n" +
                "    int b;\n" +
                "    float c;\n" +
                "    long d;\n" +
                "    void method1() {\n" +
                "        int e = 5;\n" +
                "        float f = 3.14f;\n" +
                "        long g = 10L;\n" +
                "    }\n" +
                "    void method2() {\n" +
                "        int h = 15;\n" +
                "        float i = 6.28f;\n" +
                "        long j = 20L;\n" +
                "    }\n" +
                "}";

        ClassReader classReader = new ClassReader(javaCode.getBytes());
        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor = new MethodAdapter(classWriter, access(Opcodes.ACC_PUBLIC), "TestClass", null, null, null);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(access(Opcodes.ACC_PUBLIC), "TestClass<init>", methodVisitor);

        byte[] originalCode = classReader.toByteArray();
        methodVisitor.visitCode();
        localVariablesSorter.visitCode();
        methodVisitor.visitMaxs(1, 0);
        methodVisitor.visitEnd();
        methodVisitor = localVariablesSorter;

        byte[] sortedCode = classWriter.toByteArray();

        ClassReader sortedClassReader = new ClassReader(sortedCode);
        MethodVisitor sortedMethodVisitor = sortedClassReader.visitMethod(0, "method1", "()V", null, null);
        int[] expectedMapping = {1, 2, 3, 4, 5, 6};
        Object[] expectedNewLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.INTEGER, Opcodes.FLOAT};
        verifyLocalVariablesMappingAndNewLocals(sortedMethodVisitor, expectedMapping, expectedNewLocals);

        sortedMethodVisitor = sortedClassReader.visitMethod(0, "method2", "()V", null, null);
        int[] expectedMapping2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Object[] expectedNewLocals2 = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.FLOAT, Opcodes.LONG};
        verifyLocalVariablesMappingAndNewLocals(sortedMethodVisitor, expectedMapping2, expectedNewLocals2);

        byte[] sortedJavaCode = classWriter.toByteArray();
        assertEquals(new String(originalCode), new String(sortedJavaCode));
    }

    private static int access(int accessFlags) {
        return accessFlags | Opcodes.ACC_SUPER | Opcodes.ACC_SYNTHETIC;
    }

    private static void verifyLocalVariablesMappingAndNewLocals(MethodVisitor methodVisitor, int[] expectedMapping, Object[] expectedNewLocals) {
        int nLocal = methodVisitor.visitMaxs(0, 0).localVarSlot;
        int[] actualMapping = new int[nLocal];
        Object[] actualNewLocals = new Object[nLocal];

        for (int i = 0; i < nLocal; i++) {
            actualMapping[i] = methodVisitor.visitLocalVariable("", "", "", null, null, i).index;
            actualNewLocals[i] = methodVisitor.visitLocalVariable("", "", "", null, null, i).name;
        }

        assertArrayEquals(expectedMapping, actualMapping);
        assertArrayEquals(expectedNewLocals, actualNewLocals);
    }
}