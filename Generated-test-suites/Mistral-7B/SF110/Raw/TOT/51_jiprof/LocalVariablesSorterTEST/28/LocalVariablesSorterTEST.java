package org.objectweb.asm.jip.commons;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for LocalVariablesSorter class.
 */
public class LocalVariablesSorterTest {

    /**
     * Test method adapt() with normal input.
     */
    @Test
    public void testNormalInput() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitIntInsn(Opcodes.BIPUSH, 5);
        mv.visitIntInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitIntInsn(Opcodes.BIPUSH, 10);
        mv.visitIntInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.LSTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitIntInsn(Opcodes.BIPUSH, 20);
        mv.visitIntInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.DSTORE, 3);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 4);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }

    /**
     * Test method adapt() with long locals.
     */
    @Test
    public void testLongLocals() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.LDC, 1);
        mv.visitVarInsn(Opcodes.LDC, 2);
        mv.visitVarInsn(Opcodes.LADD);
        mv.visitVarInsn(Opcodes.LSTORE, 1);
        mv.visitVarInsn(Opcodes.LDC, 3);
        mv.visitVarInsn(Opcodes.LSTORE, 2);
        mv.visitVarInsn(Opcodes.LDC, 4);
        mv.visitVarInsn(Opcodes.LSTORE, 3);
        mv.visitVarInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 4);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }

    /**
     * Test method adapt() with float locals.
     */
    @Test
    public void testFloatLocals() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.FLOAD, 0);
        mv.visitVarInsn(Opcodes.FLOAD, 1);
        mv.visitInsn(Opcodes.FADD);
        mv.visitVarInsn(Opcodes.FSTORE, 1);
        mv.visitVarInsn(Opcodes.FLOAD, 2);
        mv.visitInsn(Opcodes.FADD);
        mv.visitVarInsn(Opcodes.FSTORE, 2);
        mv.visitVarInsn(Opcodes.FLOAD, 3);
        mv.visitInsn(Opcodes.FADD);
        mv.visitVarInsn(Opcodes.FSTORE, 3);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 4);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }

    /**
     * Test method adapt() with integer locals.
     */
    @Test
    public void testIntegerLocals() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 3);
        mv.visitVarInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 3);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 4);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }

    /**
     * Test method adapt() with Object locals.
     */
    @Test
    public void testObjectLocals() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/String");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Test String");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "()V");
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Integer");
        mv.visitIntInsn(Opcodes.BIPUSH, 10);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Integer", "<init>", "(I)V");
        mv.visitVarInsn(Opcodes.ASTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Double");
        mv.visitLdc2Insn(1.23d);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Double", "<init>", "(D)V");
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 4);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }

    /**
     * Test method adapt() with complex input.
     */
    @Test
    public void testComplexInput() {
        String classData = "..."; // class data to be passed
        ClassReader reader = new ClassReader(classData.getBytes());
        ClassWriter writer = new ClassWriter(0);
        MethodVisitor mv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod", null, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/String");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("Test String");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "()V");
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitIntInsn(Opcodes.BIPUSH, 5);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Double");
        mv.visitLdc2Insn(1.23d);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(Ljava/lang/String;)Ljava/lang/Double;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Double", "<init>", "(Ljava/lang/Double;)V");
        mv.visitVarInsn(Opcodes.ASTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/Float");
        mv.visitLdcInsn(3.14f);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Float", "<init>", "(F)V");
        mv.visitVarInsn(Opcodes.ASTORE, 3);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I");
        mv.visitIntInsn(Opcodes.IADD);
        mv.visitIntInsn(Opcodes.BIPUSH, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "toString", "(D)Ljava/lang/String;");
        mv.visitVarInsn(Opcodes.ASTORE, 4);
        mv.visitIntInsn(Opcodes.BIPUSH, 10);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "toString", "(F)Ljava/lang/String;");
        mv.visitVarInsn(Opcodes.ASTORE, 5);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 4);
        mv.visitVarInsn(Opcodes.ALOAD, 5);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "concat", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(6, 6);
        mv.visitEnd();

        reader.accept(mv);
        byte[] bytecode = writer.toByteArray();

        // Test the sorted bytecode against original bytecode
        assertArrayEquals(reader.getBytes(), bytecode);
    }
}