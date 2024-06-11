package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.MethodAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
        for (Type type : types) {
            MethodVisitor mv = new MethodVisitor(0, null) {
                @Override
                public void visitVarInsn(int opcode, int var) {
                    LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", this);
                    int remappedVar = sorter.remap(var, type);
                    assertEquals(var, remappedVar);
                    sorter.visitVarInsn(opcode, remappedVar);
                }
            };
            LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
            int local = sorter.newLocal(type);
            sorter.visitVarInsn(opcode, local);
            assertEquals(local, sorter.remap(local, type));
        }
    }

    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = new MethodVisitor(0, null) {
            @Override
            public void visitIincInsn(int var, int increment) {
                LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", this);
                int remappedVar = sorter.remap(var, Type.INT_TYPE);
                assertEquals(var, remappedVar);
                sorter.visitIincInsn(remappedVar, increment);
            }
        };
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
        int local = sorter.newLocal(Type.INT_TYPE);
        sorter.visitIincInsn(local, 1);
        assertEquals(local, sorter.remap(local, Type.INT_TYPE));
    }

    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = new MethodVisitor(0, null) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", this);
                int remappedIndex = sorter.remap(index, Type.getType(desc));
                assertEquals(index, remappedIndex);
                sorter.visitLocalVariable(name, desc, signature, start, end, remappedIndex);
            }
        };
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
        String name = "test";
        String desc = "I";
        Label start = new Label();
        Label end = new Label();
        int index = 0;
        sorter.visitLocalVariable(name, desc, null, start, end, index);
        assertEquals(index, sorter.remap(index, Type.getType(desc)));
    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = new MethodVisitor(0, null) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", this);
                Object[] newLocals = sorter.newLocals;
                int numberOfLocals = newLocals.length;
                sorter.visitFrame(type, nLocal, local, nStack, stack);
                assertEquals(numberOfLocals, sorter.newLocals.length);
                for (int i = 0; i < numberOfLocals; i++) {
                    Object localObject = newLocals[i];
                    assertNull(localObject);
                }
            }
        };
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
        Object[] localObjects = sorter.newLocals;
        int numberOfLocals = localObjects.length;
        sorter.visitFrame(0, numberOfLocals, new Object[numberOfLocals], 0, new Object[numberOfLocals]);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        MethodVisitor mv = new MethodVisitor(0, null) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", this);
                sorter.visitFrame(type, nLocal, local, nStack, stack);
            }
        };
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
        try {
            sorter.visitFrame(-1, 0, null, 0, null);
        } catch (IllegalStateException e) {
            assertEquals("ClassReader.accept() should be called with EXPAND_FRAMES flag", e.getMessage());
            throw e;
        }
    }
}