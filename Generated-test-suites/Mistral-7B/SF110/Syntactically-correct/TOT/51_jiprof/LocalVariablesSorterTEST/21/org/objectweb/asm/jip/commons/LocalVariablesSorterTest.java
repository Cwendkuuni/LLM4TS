package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testLocalVariablesSorter() throws Exception {
        String className = "TestClass";
        String methodName = "testMethod";
        String methodDesc = "(I)V";

        String inputCode = "L" + className + ";\n" +
                "public static void " + methodName + "(int i) {\n" +
                "    int a = 1;\n" +
                "    int b = 2;\n" +
                "    int c = i;\n" +
                "    int d = a + b;\n" +
                "}\n";

        // Create input class bytecode array
        byte[] inputBytecode = ClassWriter.compile(0, null, null, className, methodName, methodDesc, inputCode);

        // Create class reader and LocalVariablesSorter
        ClassReader classReader = new ClassReader(inputBytecode);
        MethodAdapter inputMethod = classReader.parseMethod(MethodAdapter.AS_METHOD, 0, className, methodName, methodDesc);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, methodDesc, null);

        // Set input method to LocalVariablesSorter
        localVariablesSorter.visit(Opcodes.ASM5, inputMethod.asMethodVisitor(), 0, inputMethod.access, inputMethod.name, inputMethod.desc, null);

        // Test visitVarInsn method with int variables
        localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 0);
        localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 1);
        assertEquals(2, localVariablesSorter.nextLocal);

        // Test visitVarInsn method with long variables
        localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 0);
        localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 1);
        assertEquals(4, localVariablesSorter.nextLocal);

        // Test visitVarInsn method with float variables
        localVariablesSorter.visitVarInsn(Opcodes.FSTORE, 0);
        localVariablesSorter.visitVarInsn(Opcodes.FSTORE, 1);
        assertEquals(6, localVariablesSorter.nextLocal);

        // Test visitVarInsn method with double variables
        localVariablesSorter.visitVarInsn(Opcodes.DSTORE, 0);
        localVariablesSorter.visitVarInsn(Opcodes.DSTORE, 1);
        assertEquals(8, localVariablesSorter.nextLocal);

        // Test visitVarInsn method with Object variables
        localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 0);
        localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 1);
        assertEquals(12, localVariablesSorter.nextLocal);

        // Test visitIincInsn method
        localVariablesSorter.visitIincInsn(0, 1);
        localVariablesSorter.visitIincInsn(1, 2);

        // Test visitMaxs method
        localVariablesSorter.visitMaxs(3, 4);
        assertEquals(4, localVariablesSorter.nextLocal);

        // Test visitLocalVariable method
        localVariablesSorter.visitLocalVariable("a", "I", null, null, null, 0);
        localVariablesSorter.visitLocalVariable("b", "I", null, null, null, 1);

        // Test visitFrame method with no changes
        byte[] outputBytecode = localVariablesSorter.mv.toByteArray();
        ClassReader outputClassReader = new ClassReader(outputBytecode);
        MethodAdapter outputMethod = outputClassReader.parseMethod(MethodAdapter.AS_METHOD, 0, className, methodName, methodDesc);
        assertArrayEquals(inputBytecode, outputBytecode);

        // Test visitFrame method with changes
        inputMethod.visitLocalVariable("c", "I", null, null, null, 2);
        localVariablesSorter.visitFrame(Opcodes.FRAME_METHOD, 2, new Object[]{Opcodes.TOP, Opcodes.TOP}, 3, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP});
        outputBytecode = localVariablesSorter.mv.toByteArray();
        outputClassReader = new ClassReader(outputBytecode);
        outputMethod = outputClassReader.parseMethod(MethodAdapter.AS_METHOD, 0, className, methodName, methodDesc);
        assertArrayNotEquals(inputBytecode, outputBytecode);

        // Test newLocal method
        Type intType = Type.INT_TYPE;
        int newLocalInt = localVariablesSorter.newLocal(intType);
        assertEquals(3, newLocalInt);

        Type floatType = Type.FLOAT_TYPE;
        int newLocalFloat = localVariablesSorter.newLocal(floatType);
        assertEquals(5, newLocalFloat);

        Type longType = Type.LONG_TYPE;
        int newLocalLong = localVariablesSorter.newLocal(longType);
        assertEquals(7, newLocalLong);

        Type doubleType = Type.DOUBLE_TYPE;
        int newLocalDouble = localVariablesSorter.newLocal(doubleType);
        assertEquals(9, newLocalDouble);

        Type objectType = Type.getType("java/lang/String");
        int newLocalObject = localVariablesSorter.newLocal(objectType);
        assertEquals(13, newLocalObject);

        // Test setLocalType and setFrameLocal methods
        localVariablesSorter.setLocalType(1, intType);
        localVariablesSorter.setFrameLocal(1, intType);

        // Clean up
        localVariablesSorter = null;
    }
}