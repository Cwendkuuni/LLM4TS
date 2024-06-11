package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        String testCode = "Lorg/junit/Assert;.static {};\n" +
                "public class Test {\n" +
                "    int a;\n" +
                "    int b;\n" +
                "    long c;\n" +
                "    float d;\n" +
                "    void test() {\n" +
                "        a = 1;\n" +
                "        b = 2;\n" +
                "        c = 3L;\n" +
                "        d = 4.5F;\n" +
                "    }\n" +
                "}";

        ClassReader reader = new ClassReader(testCode.getBytes());
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        MethodVisitor mv = new MethodVisitor(writer, 0);
        MethodAdapter adapter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "Test", mv);

        adapter.visitCode();
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitFieldInsn(Opcodes.GETFIELD, "Test", "a", "I");
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitFieldInsn(Opcodes.GETFIELD, "Test", "b", "I");
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitVarInsn(Opcodes.LDC2_W, 3);
        adapter.visitVarInsn(Opcodes.ALOAD_0, 0);
        adapter.visitVarInsn(Opcodes.LDC2_F, 4.5F);
        adapter.visitVarInsn(Opcodes.ASTORE, 1);
        adapter.visitVarInsn(Opcodes.ALOAD, 1);
        adapter.visitVarInsn(Opcodes.ASTORE, 2);
        adapter.visitVarInsn(Opcodes.ALOAD, 1);
        adapter.visitVarInsn(Opcodes.ALOAD, 2);
        adapter.visitMethodInsn(Opcodes.INVOKESTATIC, "org/junit/Assert/assertEquals", "(IL;IL;)V");
        adapter.visitInsn(Opcodes.RETURN);
        adapter.visitMaxs(2, 3);
        adapter.visitEnd();

        byte[] testClass = writer.toByteArray();
        reader.parse(testClass);
        MethodVisitor newMv = reader.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "test", "()V", null, null);
        newMv = new LocalVariablesSorter(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "Test", newMv);

        int[] mapping = new int[40];
        int[] newLocals = new int[20];
        int firstLocal = 0;
        int nextLocal = 1;

        // Test for int variables
        newMv.visitVarInsn(Opcodes.ALOAD_0, 0);
        newMv.visitVarInsn(Opcodes.ALOAD_0, 0);
        newMv.visitFieldInsn(Opcodes.GETFIELD, "Test", "a", "I");
        int intVarRemapped = newMv.remap(1, Type.INT_TYPE);
        assertEquals(1, intVarRemapped);
        newMv.visitVarInsn(Opcodes.ALOAD_0, 0);
        newMv.visitVarInsn(Opcodes.ALOAD_0, 0);
        newMv.visitFieldInsn(Opcodes.GETFIELD, "Test", "b", "I");
        intVarRemapped = newMv.remap(2, Type.INT_TYPE);
        assertEquals(2, intVarRemapped);

        // Test for long variables
        newMv.visitLdcInsn(3L);
        long longVarRemapped = newMv.remap(0, Type.LONG_TYPE);
        assertEquals(3, longVarRemapped);

        // Test for float variables
        newMv.visitLdc2_F(4.5F);
        float floatVarRemapped = newMv.remap(0, Type.FLOAT_TYPE);
        assertEquals(5, floatVarRemapped);

        // Test for object variables
        newMv.visitLdc2_W("java/lang/String");
        String objectVarRemapped = (String) newMv.remap(0, Type.getType("java/lang/String"));
        assertEquals("java/lang/String", objectVarRemapped);

        // Test for incorrect opcode
        newMv.visitVarInsn(100, 0);
        assertThrows(IllegalArgumentException.class, () -> newMv.remap(0, Type.INT_TYPE));

        // Test for visitFrame method
        newMv.visitFrame(1, 0, null, 1, null);
        newMv.visitFrame(1, 1, new Object[]{Opcodes.TOP}, 1, null);
        newMv.visitFrame(1, 2, new Object[]{Opcodes.LONG, Opcodes.TOP}, 2, null);
        newMv.visitFrame(1, 3, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, 1, null);
        int frameSizeBefore = newMv.visitFrame(1, 4, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, 1, null).getLocalVariableSize();
        newMv.visitFrame(1, 4, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcades.TOP}, 0, null);
        int frameSizeAfter = newMv.visitFrame(1, 4, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, 1, null).getLocalVariableSize();
        assertEquals(0, frameSizeAfter - frameSizeBefore);

        // Test for visitLocalVariable method
        newMv.visitLocalVariable("a", "I", null, null, null, 1);
        newMv.visitLocalVariable("b", "I", null, null, null, 2);
        newMv.visitLocalVariable("c", "J", null, null, null, 3);
        newMv.visitLocalVariable("d", "F", null, null, null, 4);
        int[] expectedLocalVariableMapping = {1, 2, 5, 6};
        Object[] expectedNewLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.FLOAT};
        Object[] locals = newMv.visitLocalVariable("a", "I", null, null, null, 1).getLocalVariableDescriptor();
        assertEquals(Type.INT_TYPE, locals[0].descriptorType());
        assertEquals(1, newMv.getLocalVariableMapping(locals[0].name).intValue());
        locals = newMv.visitLocalVariable("b", "I", null, null, null, 2).getLocalVariableDescriptor();
        assertEquals(Type.INT_TYPE, locals[0].descriptorType());
        assertEquals(2, newMv.getLocalVariableMapping(locals[0].name).intValue());
        locals = newMv.visitLocalVariable("c", "J", null, null, null, 3).getLocalVariableDescriptor();
        assertEquals(Type.LONG_TYPE, locals[0].descriptorType());
        assertEquals(3, newMv.getLocalVariableMapping(locals[0].name).intValue());
        locals = newMv.visitLocalVariable("d", "F", null, null, null, 4).getLocalVariableDescriptor();
        assertEquals(Type.FLOAT_TYPE, locals[0].descriptorType());
        assertEquals(5, newMv.getLocalVariableMapping(locals[0].name).intValue());
        assertArrayEquals(expectedLocalVariableMapping, newMv.getLocalVariableMappingArray());
        assertArrayEquals(expectedNewLocals, newMv.newLocals);

        newMv.visitEnd();
    }
}