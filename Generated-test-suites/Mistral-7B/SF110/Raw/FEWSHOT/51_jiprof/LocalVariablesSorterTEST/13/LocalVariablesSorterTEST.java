package org.apache.commons.cli;

import org.objectweb.asm.jip.*;
import org.junit.Assert;
import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
                System.out.println("opcode=" + opcode + ", var=" + var);
            }
        });
        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("Ljava/lang/String;")};
        for (int i = 0; i < types.length; i++) {
            int var = sorter.nextLocal();
            int expectedVar = i + 1;
            Assert.assertEquals("Unexpected variable index", expectedVar, var);
            sorter.visitVarInsn(opcodeFor(types[i].getSort()), var);
            Assert.assertEquals("Unexpected mapped variable index", expectedVar, sorter.remap(var, types[i]));
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(var, increment);
                System.out.println("var=" + var + ", increment=" + increment);
            }
        });
        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};
        for (int i = 0; i < types.length; i++) {
            int var = sorter.nextLocal(types[i]);
            int expectedVar = i + 1;
            Assert.assertEquals("Unexpected variable index", expectedVar, var);
            sorter.visitIincInsn(var, 1);
            Assert.assertEquals("Unexpected mapped variable index", expectedVar, sorter.remap(var, types[i]));
        }
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable(name, desc, signature, start, end, index);
                System.out.println("name=" + name + ", desc=" + desc + ", index=" + index);
            }
        });
        String[] names = {"intX", "longY", "floatZ", "doubleW", "StringS"};
        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("Ljava/lang/String;")};
        for (int i = 0; i < names.length; i++) {
            int var = sorter.nextLocal(types[i]);
            String expectedName = names[i];
            Assert.assertEquals("Unexpected variable name", expectedName, sorter.mapping[2 * var + types[i].getSize() - 1]);
            sorter.visitLocalVariable(expectedName, types[i].getDescriptor(), null, Labels.newLabel(), Labels.newLabel(), var);
        }
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, nLocal, local, nStack, stack);
                System.out.println("local=" + Arrays.toString(local) + ", stack=" + Arrays.toString(stack));
            }
        });
        Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};
        Object[] locals = {Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, null};
        Object[] stack = {Opcodes.TOP, Opcodes.TOP, Opcodes.TOP};
        for (int i = 0; i < types.length; i++) {
            int var = sorter.nextLocal(types[i]);
            Object local = locals[i];
            Object stackElement = stack[i];
            int expectedLocalIndex = i;
            if (local != null) {
                expectedLocalIndex = sorter.remap(var, types[i]);
            }
            sorter.setFrameLocal(expectedLocalIndex, local);
            sorter.visitVarInsn(opcodeFor(types[i].getSort()), var);
            sorter.visitFrame(Opcodes.FRAME_same, 5, null, 2, null);
            Assert.assertArrayEquals("Unexpected local variables", new Object[]{local}, sorter.newLocals);
            Assert.assertArrayEquals("Unexpected stack elements", new Object[]{stackElement}, sorter.newLocals2);
        }
    }

    private int opcodeFor(int sort) {
        switch (sort) {
            case 1: return 21;
            case 2: return 22;
            case 3: return 23;
            case 4: return 54;
            case 5: return 55;
            case 6: return 23;
            case 7: return 24;
            case 8: return 24;
            default: return 56;
        }
    }

    static class Labels {
        public static Label newLabel() {
            return new Label();
        }
    }
}