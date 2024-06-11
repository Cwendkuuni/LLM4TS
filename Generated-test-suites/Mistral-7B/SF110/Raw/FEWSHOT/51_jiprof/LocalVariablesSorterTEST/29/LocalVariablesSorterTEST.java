package org.apache.commons.cli;

import org.junit.Assert;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Opcodes;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        sorter.visitVarInsn(21, 1);
        sorter.visitVarInsn(21, 2);
        sorter.visitVarInsn(22, 3);
        sorter.visitVarInsn(24, 4);
        sorter.visitVarInsn(23, 5);

        Assert.assertEquals(3, mv.getNextLocal());
        Assert.assertEquals(5, mv.getMaxLocals());

        Type[] types = {Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.FLOAT_TYPE};
        for (int i = 0; i < types.length; i++) {
            sorter.visitVarInsn(opcodeForType(types[i]), i + 1);
            Assert.assertEquals(i + 4, mv.getNextLocal());
            Assert.assertEquals(i + 5, mv.getMaxLocals());
        }
    }

    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        sorter.visitIincInsn(1, 1);
        sorter.visitIincInsn(2, 2);

        Assert.assertEquals(3, mv.getNextLocal());
        Assert.assertEquals(5, mv.getMaxLocals());
    }

    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        sorter.visitMaxs(5, 5);

        Assert.assertEquals(5, mv.getMaxStack());
        Assert.assertEquals(5, mv.getMaxLocals());
    }

    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        sorter.visitLocalVariable("i1", "I", null, null, null, 1);
        sorter.visitLocalVariable("d1", "D", null, null, null, 2);
        sorter.visitLocalVariable("f1", "F", null, null, null, 3);
        sorter.visitLocalVariable("l1", "J", null, null, null, 4);
        sorter.visitLocalVariable("s1", "Ljava/lang/String;", null, null, null, 5);

        Assert.assertEquals(6, mv.getNextLocal());
        Assert.assertEquals(6, mv.getMaxLocals());
    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        Object[] local = {Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER};
        sorter.visitFrame(0, 5, null, 0, null);

        int index = 0;
        for (final Object t : local) {
            int localIndex = sorter.remap(index++, Type.getType(t.getClass().getName()));
            Assert.assertEquals(index, localIndex);
        }

        int[] expectedLocals = {1, 2, 3, 4, 5};
        Assert.assertArrayEquals(expectedLocals, mv.getLocals());
    }

    @Test
    public void testNewLocal() {
        MethodVisitor mv = new MockMethodVisitor();
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        int local1 = sorter.newLocal(Type.INT_TYPE);
        int local2 = sorter.newLocal(Type.FLOAT_TYPE);
        int local3 = sorter.newLocal(Type.LONG_TYPE);
        int local4 = sorter.newLocal(Type.DOUBLE_TYPE);
        int local5 = sorter.newLocal(Type.OBJECT_TYPE);

        int nextLocal = mv.getNextLocal();
        int maxLocals = mv.getMaxLocals();

        Assert.assertEquals(6, nextLocal);
        Assert.assertEquals(6, maxLocals);
    }

    private int opcodeForType(Type type) {
        if (type.getSort() == 1 || type.getSort() == 2 || type.getSort() == 3 || type.getSort() == 4 || type.getSort() == 5) {
            return 21;
        } else if (type.getSort() == 6) {
            return 23;
        } else if (type.getSort() == 7) {
            return 22;
        } else if (type.getSort() == 8) {
            return 24;
        }
        return -1;
    }

    static class MockMethodVisitor implements MethodVisitor {
        private int nextLocal;
        private int maxLocals;
        private Object[] locals;
        private int maxStack;

        public MockMethodVisitor() {
            this.nextLocal = -1;
            this.maxLocals = 0;
            this.locals = new Object[10];
            this.maxStack = 0;
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            this.nextLocal += ((opcode == 22 || opcode == 55) ? 2 : 1);
        }

        @Override
        public void visitIincInsn(int var, int increment) {
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            this.locals[index] = index;
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            this.nextLocal = nLocal;
            this.maxLocals = maxLocals;
        }

        @Override
        public void visitCode() {
        }

        @Override
        public void visitEnd() {
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type, String label) {
        }

        @Override
        public void visitLabel(Label label) {
        }

        @Override
        public void visitLookupSwitchInsn(int key, int dflt) {
        }

        @Override
        public void visitTableSwitchInsn(int min, int max, int dflt) {
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt) {
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
        }

        @Override
        public void visitJumpInsn(int opcode, int label) {
        }

        @Override
        public void visitLabel(int label) {
        }

        @Override
        public void visitIreturn() {
        }

        @Override
        public void visitLreturn() {
        }

        @Override
        public void visitFreturn() {
        }

        @Override
        public void visitAreturn(int slot) {
        }

        @Override
        public void visitIntInsn(int opcode, int value) {
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bonds) {
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
        }

        @Override
        public void visitJumpInsn(int opcode, int label) {
        }

        @Override
        public void visitLabel(Label label) {
        }

        @Override
        public void visitLdcInsn(Object value) {
        }

        @Override
        public void visitIincInsn(int var, int increment) {
        }

        @Override
        public void visitTableSwitchInsn(int min, int max, int dflt) {
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt) {
        }

        @Override
        public void visitMultiANewArrayInsn(String desc, int numDimensions) {
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return null;
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, boolean visible) {
            return null;
        }

        @Override
        public void visitEnd() {
        }

        @Override
        public void visitExceptionTable(int numExceptions, ExceptionTable.Entry[] exTable) {
        }

        @Override
        public void visitAttribute(Attribute attribute) {
        }

        public Object[] getLocals() {
            return locals;
        }
    }
}