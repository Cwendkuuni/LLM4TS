package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", writer);

        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mv.visitVarInsn(Opcodes.ASTORE, 2);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 3);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod", int.class), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", testMv);
        BytecodeInstructionVisitor visitor = new BytecodeInstructionVisitor();

        int instructionIndex = 0;

        while (instructionIndex < bytecode.length) {
            int opcode = (bytecode[instructionIndex] & 0xFF);
            int var = Opcodes.getOperand(opcode);
            Type type = Opcodes.getOperandsType(opcode);

            if (opcode == Opcodes.VISIT_VAR_INSN) {
                int expectedLocal = sorter.remap(var, type);
                int actualLocal = visitor.getLocalVariable(instructionIndex);

                assertEquals("Unexpected local variable mapping", expectedLocal, actualLocal);
            }

            instructionIndex++;
            visitor.visitNextInstruction();
        }
    }

    @Test
    public void testVisitIincInsn() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod()V", writer);

        mv.visitCode();
        mv.visitIntInsn(Opcodes.ICONST_0, 0);
        mv.visitIincInsn(1, 1);
        mv.visitIntInsn(Opcodes.ICONST_1, 0);
        mv.visitIntInsn(Opcodes.ICONST_1, 1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod"), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod()V", testMv);
        BytecodeInstructionVisitor visitor = new BytecodeInstructionVisitor();

        int instructionIndex = 0;

        while (instructionIndex < bytecode.length) {
            int opcode = (bytecode[instructionIndex] & 0xFF);

            if (opcode == Opcodes.VISIT_IINC_INSN) {
                int expectedLocal = sorter.remap(Opcode.getIndex(opcode), Type.INT_TYPE);
                int actualLocal = visitor.getLocalVariable(instructionIndex);

                assertEquals("Unexpected local variable mapping", expectedLocal, actualLocal);
            }

            instructionIndex++;
            visitor.visitNextInstruction();
        }
    }

    @Test
    public void testVisitMaxs() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", writer);

        mv.visitCode();
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod", int.class), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", testMv);

        int maxLocals = sorter.nextLocal;

        assertEquals("Unexpected maximum number of local variables", 2, maxLocals);
    }

    @Test
    public void testVisitLocalVariable() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", writer);

        mv.visitCode();
        mv.visitLocalVariable("i", "I", null, new Label(0), new Label(2), 1);
        mv.visitIntInsn(Opcodes.ICONST_0, 0);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod", int.class), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", testMv);

        String name = sorter.mapping[2 * 1 + 1].getName();

        assertEquals("Unexpected local variable name", "i", name);
    }

    @Test
    public void testVisitFrame() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", writer);

        mv.visitCode();
        mv.visitLocalVariable("i", "I", null, new Label(0), new Label(2), 1);
        mv.visitIntInsn(Opcodes.ICONST_0, 0);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod", int.class), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(I)V", testMv);

        Object[] localVariables = sorter.newLocals;

        assertNotNull("Unexpected null local variables array", localVariables);
        assertEquals("Unexpected number of local variables", 2, localVariables.length);
    }

    @Test
    public void testNewLocal() throws Exception {
        ClassReader reader = new ClassReader("TestClass");
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(Ljava/lang/String;)V", writer);

        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitIntInsn(Opcodes.ICONST_0, 0);
        mv.visitVarInsn(Opcodes.ISTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "TestClass", "name", "Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I");
        mv.visitIntInsn(Opcodes.IADD, 1);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitIntInsn(Opcodes.BIPUSH, 16);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(I)V");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "TestClass", "name", "Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitFieldInsn(Opcodes.PUTFIELD, "TestClass", "result", "Ljava/lang/String;");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(5, 2);
        mv.visitEnd();

        reader.accept(mv);

        byte[] bytecode = writer.toByteArray();

        ClassReader testReader = new ClassReader(bytecode);
        MethodAdapter testMv = testReader.getMethodAdapter(TestClass.class.getDeclaredMethod("testMethod", String.class), 0);

        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "testMethod(Ljava/lang/String;)V", testMv);

        int newLocal = sorter.newLocal(Type.getType("java/lang/StringBuilder"));
        int expectedLocal = 3;

        assertEquals("Unexpected new local variable index", expectedLocal, newLocal);
    }
}