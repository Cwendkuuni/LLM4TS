package org.apache.commons.cli.jip.commons;

import static org.junit.Assert.*;
import org.objectweb.asm.jip.Signature;
import org.objectweb.asm.jip.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import java.util.Arrays;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());
        int[] mapping = new int[4];
        int[] expected = {1, 2, 3, 4};

        sorter.visitVarInsn(Opcodes.ISTORE, 0);
        sorter.visitVarInsn(Opcodes.ISTORE, 1);
        sorter.visitVarInsn(Opcodes.ISTORE, 2);
        sorter.visitVarInsn(Opcodes.ISTORE, 3);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], mapping[i]);
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        sorter.visitIincInsn(0, 1);
        sorter.visitIincInsn(1, 2);

        int[] mapping = sorter.mapping;
        int[] expected = {1, 2, 3, 4};

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], mapping[i * 2]);
        }
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        sorter.visitLocalVariable("i1", Type.INT_TYPE.getDescriptor(), null, new Label(), new Label(), 0);
        sorter.visitLocalVariable("f1", Type.FLOAT_TYPE.getDescriptor(), null, new Label(), new Label(), 1);
        sorter.visitLocalVariable("l1", Type.LONG_TYPE.getDescriptor(), null, new Label(), new Label(), 2);
        sorter.visitLocalVariable("d1", Type.DOUBLE_TYPE.getDescriptor(), null, new Label(), new Label(), 3);
        sorter.visitLocalVariable("s1", "Ljava/lang/String;", null, new Label(), new Label(), 4);

        int[] mapping = sorter.mapping;
        String[] names = {"i1", "f1", "l1", "d1", "s1"};
        int[] expected = {1, 2, 5, 6, 7};

        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], LocalVariablesSorter.getLocalVariableName(mapping[i * 2 + 1]));
            assertEquals(expected[i], mapping[i * 2]);
        }
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        sorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);

        sorter.visitVarInsn(Opcodes.ISTORE, 0);
        sorter.visitVarInsn(Opcodes.ISTORE, 1);

        sorter.visitFrame(Opcodes.FRAME_LOCALS, 2, new Object[]{Type.INT_TYPE, Type.INT_TYPE}, 0, null);

        Object[] locals = sorter.newLocals;
        int[] mapping = sorter.mapping;

        assertEquals(2, locals.length);
        assertEquals(Type.INT_TYPE, locals[0].getClass());
        assertEquals(Type.INT_TYPE, locals[1].getClass());

        assertEquals(2, mapping.length);
        assertEquals(0, mapping[0]);
        assertEquals(1, mapping[1]);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        int local1 = sorter.newLocal(Type.INT_TYPE);
        int local2 = sorter.newLocal(Type.FLOAT_TYPE);
        int local3 = sorter.newLocal(Type.LONG_TYPE);
        int local4 = sorter.newLocal(Type.DOUBLE_TYPE);
        int local5 = sorter.newLocal(Type.getType("java/lang/String"));

        assertEquals(5, sorter.nextLocal);
        assertEquals(5, sorter.newLocals.length);

        Object[] locals = sorter.newLocals;
        Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.getType("java/lang/String")};

        for (int i = 0; i < locals.length; i++) {
            assertEquals(types[i], locals[i].getClass());
        }
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        Object[] locals = new Object[10];
        int local1 = sorter.newLocal(Type.INT_TYPE);
        sorter.setFrameLocal(local1, Opcodes.INTEGER);

        assertEquals(10, sorter.newLocals.length);
        assertEquals(Opcodes.INTEGER, sorter.newLocals[0]);
        assertEquals(local1, sorter.newLocals[1]);

        Object[] expected = {Opcodes.INTEGER, local1};
        assertArrayEquals(expected, sorter.newLocals);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        int local1 = sorter.newLocal(Type.INT_TYPE);
        int local2 = sorter.newLocal(Type.INT_TYPE);

        int mapped1 = sorter.remap(0, Type.INT_TYPE);
        int mapped2 = sorter.remap(1, Type.INT_TYPE);

        assertEquals(local1, mapped1);
        assertEquals(local2, mapped2);
    }

    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitorAdapter());

        int local = sorter.newLocalMapping(Type.INT_TYPE);
        int local2 = sorter.newLocalMapping(Type.INT_TYPE);

        assertEquals(local, local2 - 1);
    }
}

class MethodVisitorAdapter extends MethodVisitor {
    @Override
    public void visitVarInsn(int opcode, int var) {
    }

    @Override
    public void visitIincInsn(int var, int increment) {
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
    }
}