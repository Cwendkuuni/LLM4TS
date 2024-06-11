package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                System.out.printf("opcode: %d, var: %d%n", opcode, var);
            }
        });
        int[] testCases = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (int i = 0; i < testCases.length; ++i) {
            Type type = null;
            switch (i) {
                case 0:
                    type = Type.INT_TYPE;
                    break;
                case 1:
                    type = Type.FLOAT_TYPE;
                    break;
                case 2:
                    type = Type.LONG_TYPE;
                    break;
                case 3:
                    type = Type.DOUBLE_TYPE;
                    break;
                case 4:
                    type = Type.OBJECT_TYPE;
                    break;
                default:
                    type = Type.getIntType(i + 1);
            }
            sorter.visitVarInsn(i + 22, i);
            assertEquals(i + 22, sorter.mv.visitVarInsn(opcode, i + sorter.remap(i, type)).getOpcode());
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitIincInsn(int var, int increment) {
                System.out.printf("var: %d, increment: %d%n", var, increment);
            }
        });
        int[] testCases = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (int i = 0; i < testCases.length; ++i) {
            sorter.visitIincInsn(i, 1);
            assertEquals(i, sorter.mv.visitIincInsn(i, 1).getIndex());
        }
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                System.out.printf("maxStack: %d, maxLocals: %d%n", maxStack, maxLocals);
            }
        });
        Type[] argTypes = new Type[] { Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE, Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE, Type.INT_TYPE };
        int maxLocals = 12;
        for (int i = 0; i < argTypes.length; ++i) {
            sorter.visitVarInsn(21, i);
            maxLocals += argTypes[i].getSize();
        }
        sorter.visitMaxs(10, maxLocals);
        assertEquals(10, sorter.mv.visitMaxs(10, 12).getMaxLocals());
        assertEquals(12, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                System.out.printf("name: %s, desc: %s, signature: %s, index: %d%n", name, desc, signature, index);
            }
        });
        String[] testNames = { "i", "f", "l", "d", "o", "i1", "f1", "l1", "d1", "o1", "i2" };
        Type[] testTypes = { Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE, Type.getIntType(1), Type.getFloatType(1), Type.getLongType(1), Type.getDoubleType(1), Type.getObjectType("java/lang/Object"), Type.getIntType(2) };
        Label startLabel = new Label();
        Label endLabel = new Label();
        for (int i = 0; i < testNames.length; ++i) {
            sorter.visitLocalVariable(testNames[i], testTypes[i].getDescriptor(), null, startLabel, endLabel, i);
        }
        Object[] localVariables = sorter.mv.visitFrame(0, 12, null, 1, null).getLocalVariables();
        Object[] expectedValues = new Object[12];
        for (int i = 0; i < testNames.length; ++i) {
            expectedValues[i] = testNames[i];
        }
        assertArrayEquals(expectedValues, localVariables);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                System.out.printf("type: %d, nLocal: %d, local: %s, nStack: %d, stack: %s%n", type, nLocal, Arrays.toString(local), nStack, Arrays.toString(stack));
                assertEquals(-1, type);
                assertNull(local);
                assertNull(stack);
            }
        });
        int[] testCases = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (int i = 0; i < testCases.length; ++i) {
            sorter.visitVarInsn(21, i);
            Type type = null;
            switch (i) {
                case 0:
                    type = Type.INT_TYPE;
                    break;
                case 1:
                    type = Type.FLOAT_TYPE;
                    break;
                case 2:
                    type = Type.LONG_TYPE;
                    break;
                case 3:
                    type = Type.DOUBLE_TYPE;
                    break;
                case 4:
                    type = Type.OBJECT_TYPE;
                    break;
                default:
                    type = Type.getIntType(i + 1);
            }
            Object[] local = new Object[] { type, i };
            Object[] stack = new Object[] { Type.getIntType(i + 1) };
            sorter.visitFrame(0, 2, local, 1, stack);
        }
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                System.out.printf("name: %s, desc: %s, signature: %s, index: %d%n", name, desc, signature, index);
            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Object[] localCopy = new Object[local.length];
                System.arraycopy(local, 0, localCopy, 0, local.length);
                assertArrayEquals(localCopy, sorter.newLocals);
            }
        });
        for (int i = 0; i < 11; ++i) {
            Type type = null;
            switch (i) {
                case 0:
                    type = Type.INT_TYPE;
                    break;
                case 1:
                    type = Type.FLOAT_TYPE;
                    break;
                case 2:
                    type = Type.LONG_TYPE;
                    break;
                case 3:
                    type = Type.DOUBLE_TYPE;
                    break;
                case 4:
                    type = Type.OBJECT_TYPE;
                    break;
                default:
                    type = Type.getIntType(i + 1);
            }
            int local = sorter.newLocal(type);
            Type localType = Type.getType(type.getDescriptor());
            sorter.visitLocalVariable("_" + i, localType.getDescriptor(), null, new Label(), new Label(), local);
        }
        sorter.visitFrame(0, 11, null, 1, null);
    }
}