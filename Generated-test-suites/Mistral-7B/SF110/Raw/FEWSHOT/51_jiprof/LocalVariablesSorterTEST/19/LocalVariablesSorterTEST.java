package org.apache.commons.cli.test;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.junit.Assert;
import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.assertEquals(0, opcode);
                Assert.assertEquals(0, var);
            }
        });

        sorter.visitVarInsn(Opcodes.ALOAD, 0);
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitIntInsn(int opcode, int value) {
                Assert.assertEquals(Opcodes.IINC, opcode);
            }
        });

        sorter.visitIincInsn(0, 1);
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                Assert.assertEquals(0, maxStack);
                Assert.assertEquals(5, maxLocals);
            }
        });

        sorter.visitMaxs(10, 10);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Assert.assertEquals("name", name);
                Assert.assertEquals("Ljava/lang/String;", desc);
                Assert.assertNull(signature);
                Assert.assertEquals(0, index);
            }
        });

        sorter.visitLocalVariable("name", "Ljava/lang/String;", null, null, null, 0);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Assert.assertEquals(-1, type);
                Assert.assertEquals(5, nLocal);
                Assert.assertArrayEquals(new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, local);
                Assert.assertEquals(0, nStack);
                Assert.assertArrayEquals(new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, stack);
            }
        });

        Object[] locals = new Object[]{Opcodes.LONG, Opcodes.FLOAT, Opcodes.LONG, Opcodes.INTEGER, Opcodes.LONG};
        Object[] stack = new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP};
        sorter.visitFrame(Opcodes.FRAME_same, 5, locals, 5, stack);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public int visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                return index;
            }
        });

        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        Assert.assertEquals(1, local);
    }

    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                sorter.setLocalType(index, Type.DOUBLE_TYPE);
            }
        });

        Type type = Type.DOUBLE_TYPE;
        int local = sorter.newLocal(type);
        sorter.setLocalType(local, type);
        Assert.assertEquals(Type.DOUBLE_TYPE, sorter.getLocalType(local));
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Object localObj = Opcodes.LONG;
                sorter.setFrameLocal(0, localObj);
                Assert.assertEquals(localObj, sorter.getFrameLocal(0));
            }
        });

        Object localObj = Opcodes.LONG;
        sorter.setFrameLocal(0, localObj);
        Assert.assertEquals(localObj, sorter.getFrameLocal(0));
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                int local = sorter.remap(index, Type.getIntType());
                Assert.assertEquals(index, local);
            }
        });

        int index = 1;
        int local = sorter.remap(index, Type.getIntType());
        Assert.assertEquals(index, local);
    }

    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public int visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                return index;
            }
        });

        Type type = Type.getIntType();
        int local = sorter.newLocalMapping(type);
        Assert.assertEquals(1, local);
    }
}