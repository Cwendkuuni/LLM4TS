package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // Do nothing
            }
        });
        assertTrue(sorter.mapping.length > 0);
        assertTrue(sorter.mapping[0] == 0);
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        assertEquals(local, sorter.remap(0, type));
    }

    @Test
    public void testVisitVarInsnWithDifferentTypes() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // Do nothing
            }
        });
        Type intType = Type.INT_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getObjectType("java/lang/Object");

        int localInt = sorter.newLocal(intType);
        int localFloat = sorter.newLocal(floatType);
        int localLong = sorter.newLocal(longType);
        int localDouble = sorter.newLocal(doubleType);
        int localObject = sorter.newLocal(objectType);

        assertArrayEquals(new int[]{localInt, localFloat, localLong, localDouble, localObject}, new int[]{sorter.remap(0, intType), sorter.remap(0, floatType), sorter.remap(0, longType), sorter.remap(0, doubleType), sorter.remap(0, objectType)});
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitIincInsn(int var, int increment) {
                // Do nothing
            }
        });
        Type intType = Type.INT_TYPE;
        int local = sorter.newLocal(intType);
        assertEquals(local, sorter.remap(0, intType));
        assertEquals(local + 1, sorter.remap(1, intType));
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                // Do nothing
            }
        });
        int maxLocals = 10;
        assertEquals(maxLocals, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // Do nothing
            }
        });

        String name = "a";
        String desc = "I";
        Type type = Type.INT_TYPE;
        sorter.visitLocalVariable(name, desc, null, null, null, 0);
        assertEquals(0, sorter.remap(0, type));
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // Do nothing
            }
        });

        Object[] locals = new Object[]{null, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.TOP};
        int nLocal = locals.length;
        Object[] stack = new Object[0];
        sorter.visitFrame(0, nLocal, locals, 0, stack);
        int[] expected = new int[nLocal];
        for (int i = 0; i < nLocal; i++) {
            Type type;
            if (i < locals.length && locals[i] != null) {
                type = Type.getType((String)locals[i]);
            } else {
                type = Opcodes.TOP;
            }
            expected[i] = sorter.remap(i, type);
        }
        assertArrayEquals(expected, sorter.newLocals);
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // Do nothing
            }
        });

        Object[] locals = new Object[]{null, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.TOP};
        int nLocal = locals.length;
        sorter.visitFrame(0, nLocal, locals, 0, new Object[0]);

        for (int i = 0; i < nLocal; i++) {
            if (locals[i] != null) {
                Type type = Type.getType((String)locals[i]);
                sorter.setFrameLocal(i, type);
            }
        }
        assertFalse(sorter.changed);
    }

    @Test
    public void testSetFrameLocalWithChanged() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // Do nothing
            }
        });

        Object[] locals = new Object[]{null, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.TOP};
        int nLocal = locals.length;
        sorter.visitFrame(0, nLocal, locals, 0, new Object[0]);

        for (int i = 0; i < nLocal; i++) {
            if (locals[i] != null) {
                Type type = Type.getType((String)locals[i]);
                sorter.setFrameLocal(i, type);
                sorter.changed = true;
            }
        }
        assertTrue(sorter.changed);
    }
}