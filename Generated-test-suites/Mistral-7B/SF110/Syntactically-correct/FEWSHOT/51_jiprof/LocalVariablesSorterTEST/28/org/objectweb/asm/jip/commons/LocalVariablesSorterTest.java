package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
            }
        });

        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("java/lang/String")};

        for (Type type : types) {
            int var = sorter.nextLocal();
            int remappedVar = sorter.remap(var, type);
            Assert.assertEquals(var, remappedVar);
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitIincInsn(int var, int increment) {
            }
        });

        for (int i = 0; i < 10; i++) {
            int var = sorter.nextLocal();
            int remappedVar = sorter.remap(var, Type.INT_TYPE);
            Assert.assertEquals(var, remappedVar);
        }
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
            }
        });

        int maxLocals = 10;
        int remappedMaxLocals = sorter.remap(maxLocals, Type.INT_TYPE);
        Assert.assertEquals(maxLocals, remappedMaxLocals);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            }
        });

        String name = "testName";
        String desc = "I";
        Label start = new Label();
        Label end = new Label();
        int index = 10;

        int remappedIndex = sorter.remap(index, Type.getType(desc));
        Assert.assertEquals(index, remappedIndex);

        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        Assert.assertTrue(sorter.changed);
        sorter.changed = false;

        Object[] locals = sorter.newLocals;
        Assert.assertEquals(11, locals.length);
        Assert.assertNull(locals[10]);
        Object expectedLocal = Opcodes.INTEGER;
        Assert.assertEquals(expectedLocal, locals[remappedIndex]);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            }
        });

        int nLocal = 10;
        Object[] local = new Object[nLocal];
        Object[] stack = new Object[5];

        for (int i = 0; i < nLocal; i++) {
            Object obj = Opcodes.INTEGER;
            int remappedIndex = sorter.remap(i, Type.INT_TYPE);
            sorter.setFrameLocal(remappedIndex, obj);
            local[i] = obj;
        }

        sorter.visitFrame(-1, nLocal, local, 5, stack);

        Object[] newLocals = sorter.newLocals;
        Assert.assertEquals(10, newLocals.length);
        for (int i = 0; i < nLocal; i++) {
            Object expectedLocal = Opcodes.INTEGER;
            Object actualLocal = newLocals[i];
            Assert.assertEquals(expectedLocal, actualLocal);
        }
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            }
        });

        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        int remappedLocal = sorter.remap(local, type);
        Assert.assertEquals(local, remappedLocal);
    }
}