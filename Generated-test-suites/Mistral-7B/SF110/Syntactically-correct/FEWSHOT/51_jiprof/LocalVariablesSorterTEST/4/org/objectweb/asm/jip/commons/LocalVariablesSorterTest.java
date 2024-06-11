package org.objectweb.asm.jip.commons;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;

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

        sorter.visitVarInsn(21, 0); // int
        sorter.visitVarInsn(22, 1); // long
        sorter.visitVarInsn(23, 2); // float
        sorter.visitVarInsn(24, 3); // double
        sorter.visitVarInsn(21, 4); // int
        sorter.visitVarInsn(56, 5); // float
        sorter.visitVarInsn(55, 6); // long
        sorter.visitVarInsn(57, 7); // double
        sorter.visitVarInsn(54, 8); // int
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitIincInsn(int var, int increment) {
                Assert.assertEquals(0, var);
                Assert.assertEquals(1, increment);
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
                Assert.assertEquals(10, maxLocals);
            }
        });

        sorter.visitMaxs(0, 10);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Type type = Type.getType(desc);
                Assert.assertEquals("i", name);
                Assert.assertEquals(Type.INT_TYPE, type);
                Assert.assertEquals(0, index);
            }
        });

        sorter.visitLocalVariable("i", "I", null, null, null, 0);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Assert.assertEquals(-1, type);
                Assert.assertEquals(2, nLocal);
                Assert.assertArrayEquals(new Object[]{Type.LONG_TYPE, Type.INT_TYPE}, local);
                Assert.assertEquals(0, nStack);
                Assert.assertNull(stack);
            }
        });

        Type[] argTypes = new Type[0];
        sorter.visitLocalVariable("", "", "", null, null, 0);
        sorter.visitLocalVariable("l", "J", null, null, null, 1);
        sorter.visitLocalVariable("i", "I", null, null, null, 2);
        sorter.visitFrame(0, 2, new Object[]{Type.LONG_TYPE, Type.INT_TYPE}, 0, null);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                int local = sorter.newLocal(Type.INT_TYPE);
                Assert.assertEquals(local, index);
                sorter.visitLocalVariable(name, desc, signature, start, end, local);
            }
        });

        Type[] argTypes = new Type[0];
        sorter.visitLocalVariable("i1", "I", null, null, null, 0);
        sorter.visitLocalVariable("i2", "I", null, null, null, 1);
        Type intType = Type.INT_TYPE;
        int local = sorter.newLocal(intType);
        sorter.visitLocalVariable("i3", intType.getDescriptor(), null, null, null, local);
        int nextLocal = sorter.nextLocal;
        Assert.assertEquals(3, nextLocal);
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Object[] newLocals = sorter.newLocals;
                for (int i = 0; i < nLocal; i++) {
                    Object localObject = newLocals[i];
                    if (localObject != Opcodes.TOP) {
                        Type type = localObject.getClass() == Opcodes.class ? Type.getObjectType((String)localObject) : (Type)localObject;
                        int index = sorter.remap(i, type);
                        Assert.assertEquals(i, index);
                    }
                }
            }
        });

        Type[] argTypes = new Type[0];
        sorter.visitLocalVariable("i1", "I", null, null, null, 0);
        Object t1 = new Integer(1);
        sorter.setFrameLocal(0, t1);
        Type intType = Type.INT_TYPE;
        sorter.newLocal(intType);
        Object t2 = new Integer(2);
        sorter.setFrameLocal(1, t2);
        Object t3 = new Integer(3);
        sorter.setFrameLocal(2, t3);
        sorter.visitFrame(0, 3, new Object[]{intType, intType, intType}, 0, null);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Type type = Type.getType(desc);
                int local = sorter.remap(index, type);
                Assert.assertEquals(index, local);
                sorter.setFrameLocal(local, Type.getObjectType(name));
            }
        });

        Type[] argTypes = new Type[0];
        int localVariable0 = sorter.nextLocal;
        sorter.visitLocalVariable("i1", "I", null, null, null, 0);
        int localVariable1 = sorter.nextLocal;
        sorter.visitLocalVariable("l1", "J", null, null, null, 1);
        int localVariable2 = sorter.nextLocal;
        sorter.visitLocalVariable("d1", "D", null, null, null, 2);
        Type intType = Type.INT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getObjectType("java/lang/Object");

        Assert.assertEquals(0, sorter.remap(localVariable0, intType));
        Assert.assertEquals(1, sorter.remap(localVariable1, longType));
        Assert.assertEquals(2, sorter.remap(localVariable2, doubleType));
        Assert.assertEquals(3, sorter.remap(localVariable0, objectType));
    }
}