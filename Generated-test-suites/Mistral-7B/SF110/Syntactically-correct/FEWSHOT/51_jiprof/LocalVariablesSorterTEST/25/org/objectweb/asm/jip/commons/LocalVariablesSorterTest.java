package org.apache.commons.cli.test;

import org.junit.Assert;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.MethodVisitor;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        int[] testCases = {1, 2, 3, 4, 5};
        Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
        for (int i = 0; i < testCases.length; i++) {
            int local = sorter.newLocal(types[i]);
            sorter.visitVarInsn(21, testCases[i]);
            int remappedLocal = sorter.remap(testCases[i], types[i]);
            Assert.assertEquals(local, remappedLocal);
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitIincInsn(int var, int increment) {
                Assert.fail();
            }
        });
        int[] testCases = {1, 2, 3, 4, 5};
        for (int i = 0; i < testCases.length; i++) {
            int local = sorter.newLocal(Type.INT_TYPE);
            sorter.visitIincInsn(i + 1, 1);
            int remappedLocal = sorter.remap(i + 1, Type.INT_TYPE);
            Assert.assertEquals(local, remappedLocal);
        }
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                Assert.fail();
            }
        });
        int maxLocals = 10;
        int maxStack = 5;
        sorter.visitMaxs(maxStack, maxLocals);
        int newMaxLocals = sorter.nextLocal;
        sorter.visitMaxs(maxStack, maxLocals);
        int remappedMaxLocals = sorter.remap(maxLocals, Type.INT_TYPE);
        Assert.assertEquals(newMaxLocals, remappedMaxLocals + 1);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Assert.fail();
            }
        });
        String[] names = {"name1", "name2", "name3", "name4", "name5"};
        Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
        Label startLabel = new Label();
        Label endLabel = new Label();
        for (int i = 0; i < names.length; i++) {
            int local = sorter.newLocal(types[i]);
            sorter.visitLocalVariable(names[i], types[i].getDescriptor(), null, startLabel, endLabel, local);
            int remappedLocal = sorter.remap(local, types[i]);
            Assert.assertEquals(local, remappedLocal);
        }
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Assert.fail();
            }
        });
        Object[] oldLocals = new Object[10];
        Object[] newLocals = new Object[10];
        for (int i = 0; i < oldLocals.length; i++) {
            oldLocals[i] = Opcodes.TOP;
        }
        int nLocal = 10;
        int nStack = 5;
        Object[] stack = new Object[5];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = Opcodes.TOP;
        }
        LocalVariableAttribute localVariableAttribute = new LocalVariableAttribute(
                new String[]{"name1", "name2", "name3", "name4", "name5"}, Type.getObjectTypes(new Type[]{Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE}), 0, 0, nLocal);
        sorter.visitFrame(0, nLocal, oldLocals, nStack, stack);
        sorter.visitFrame(0, nLocal, newLocals, nStack, stack);
        for (int i = 0; i < oldLocals.length; i++) {
            Object oldLocal = oldLocals[i];
            Object newLocal = newLocals[i];
            if (newLocal == null) {
                continue;
            }
            if (newLocal != Opcodes.TOP) {
                Type type = Type.getType(newLocal.toString());
                int remappedLocal = sorter.remap(i, type);
                Assert.assertEquals(i, remappedLocal);
                Assert.assertEquals(newLocal, sorter.newLocals[remappedLocal]);
            }
        }
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
        for (Type type : types) {
            int local = sorter.newLocal(type);
            Assert.assertNotEquals(-1, local);
            Assert.assertEquals(type, sorter.getLocalType(local));
        }
    }

    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        sorter.setLocalType(local, Type.LONG_TYPE);
        Assert.assertNotEquals(Type.INT_TYPE, sorter.getLocalType(local));
        Assert.assertEquals(Type.LONG_TYPE, sorter.getLocalType(local));
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        Object frameLocal = Opcodes.INTEGER;
        sorter.setFrameLocal(local, frameLocal);
        Assert.assertEquals(frameLocal, sorter.newLocals[local]);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        int remappedLocal = sorter.remap(local, type);
        Assert.assertEquals(local, remappedLocal);
    }

    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.fail();
            }
        });
        Type type = Type.INT_TYPE;
        int newLocal = sorter.newLocalMapping(type);
        Assert.assertNotEquals(-1, newLocal);
        Assert.assertEquals(type, sorter.getLocalType(newLocal));
    }
}