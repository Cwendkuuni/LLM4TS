package org.objectweb.asm.jip.commons;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                System.out.printf("Visited VarInsn opcode: %d, var: %d%n", opcode, var);
            }
        });

        Type[] types = new Type[]{Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.GETOBJECT(String.class.getName())};
        for (int i = 0; i < types.length; i++) {
            int var = i + 1;
            sorter.visitVarInsn(21, var); // int
            sorter.visitVarInsn(22, var); // long
            if (types[i] != Type.LONG_TYPE) {
                sorter.visitVarInsn(23, var); // float
            }
            sorter.visitVarInsn(24, var); // double
            if (types[i] != Type.DOUBLE_TYPE) {
                sorter.visitVarInsn(25, var); // object
            }
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitIincInsn(int var, int increment) {
                System.out.printf("Visited IincInsn var: %d, increment: %d%n", var, increment);
            }
        });

        for (int i = 0; i < 10; i++) {
            sorter.visitIincInsn(i + 1, i + 1);
        }
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                System.out.printf("Visited maxStack: %d, maxLocals: %d%n", maxStack, maxLocals);
            }
        });

        int maxStack = 10;
        int maxLocals = 20;
        sorter.visitMaxs(maxStack, maxLocals);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                System.out.printf("Visited LocalVariable name: %s, desc: %s, index: %d%n", name, desc, index);
            }
        });

        LocalVariablesSorter.MethodVisitor mv = new MethodVisitor(0x0) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, desc, signature, start, end, index);
                Assert.assertEquals("name", "expectedName", name);
                Assert.assertEquals("desc", "expectedDesc", desc);
                Assert.assertEquals("index", expectedIndex, index);
            }
        };
        sorter = new LocalVariablesSorter(0, "()V", mv);

        String[] names = new String[]{"name1", "name2", "name3", "name4"};
        String[] descs = new String[]{"I", "J", "F", "D", "Ljava/lang/String;"};
        int[] expectedIndices = new int[]{1, 2, 3, 4};
        int[] expectedLocalVariableSlots = new int[]{1, 2, 3, 4};

        for (int i = 0; i < names.length; i++) {
            sorter.visitLocalVariable(names[i], descs[i], null, Labels.newLabel(), Labels.newLabel(), expectedLocalVariableSlots[i]);
        }

        for (int i = 0; i < names.length; i++) {
            Assert.assertEquals("index", expectedIndices[i], sorter.mv.localVariables[i].index);
        }
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                System.out.printf("Visited Frame type: %d, nLocal: %d, local: %s, nStack: %d, stack: %s%n", type, nLocal, Arrays.toString(local), nStack, Arrays.toString(stack));
            }
        });

        Object[] local = {INTEGER, FLOAT, LONG, DOUBLE, GETOBJECT(String.class.getName()), GETOBJECT(String.class.getName())};
        int nLocal = local.length;
        Object[] stack = {};
        int nStack = 0;

        sorter.visitFrame(-1, nLocal, local, nStack, stack);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0x0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                switch (opcode) {
                    case 21:
                    case 54: {
                        Type intType = Type.INT_TYPE;
                        int local = sorter.newLocal(intType);
                        System.out.printf("Visited newLocal int, local: %d%n", local);
                        break;
                    }
                    case 23:
                    case 56: {
                        Type floatType = Type.FLOAT_TYPE;
                        int local = sorter.newLocal(floatType);
                        System.out.printf("Visited newLocal float, local: %d%n", local);
                        break;
                    }
                    case 22:
                    case 55: {
                        Type longType = Type.LONG_TYPE;
                        int local = sorter.newLocal(longType);
                        System.out.printf("Visited newLocal long, local: %d%n", local);
                        break;
                    }
                    case 24:
                    case 57: {
                        Type doubleType = Type.DOUBLE_TYPE;
                        int local = sorter.newLocal(doubleType);
                        System.out.printf("Visited newLocal double, local: %d%n", local);
                        break;
                    }
                    case 9: {
                        Type objectType = Type.GETOBJECT(String.class.getName());
                        int local = sorter.newLocal(objectType);
                        System.out.printf("Visited newLocal object, local: %d%n", local);
                        break;
                    }
                }
            }
        });

        Type[] types = new Type[]{Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.GETOBJECT(String.class.getName())};
        for (Type type : types) {
            int local = sorter.newLocal(type);
            System.out.printf("Visited newLocal with type: %s, local: %d%n", type, local);
        }
    }
}