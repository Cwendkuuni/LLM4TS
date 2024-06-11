package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.objectweb.asm.commons.MethodAdapter;
import org.objectweb.asm.commons.Type;
import org.objectweb.asm.commons.util.Printer;
import org.objectweb.asm.commons.util.TypeUtils;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final Type type = Type.INT_TYPE;

        sorter.visitVarInsn(0x1, var);
        assertEquals(var, sorter.remap(var, type));
        sorter.mv.visitVarInsn(0, 1);
        verifyVisitedVarInsn(1, 0, type, sorter.mv);
    }

    @Test
    public void testVisitVarInsnLong() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final Type type = Type.LONG_TYPE;

        sorter.visitVarInsn(0x2, var);
        assertEquals(var, sorter.remap(var, type));
        sorter.mv.visitVarInsn(1, 1);
        verifyVisitedVarInsn(1, 1, type, sorter.mv);
    }

    @Test
    public void testVisitVarInsnFloat() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final Type type = Type.FLOAT_TYPE;

        sorter.visitVarInsn(0x3, var);
        assertEquals(var, sorter.remap(var, type));
        sorter.mv.visitVarInsn(2, 1);
        verifyVisitedVarInsn(1, 2, type, sorter.mv);
    }

    @Test
    public void testVisitVarInsnDouble() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final Type type = Type.DOUBLE_TYPE;

        sorter.visitVarInsn(0x4, var);
        assertEquals(var, sorter.remap(var, type));
        sorter.mv.visitVarInsn(3, 1);
        verifyVisitedVarInsn(1, 3, type, sorter.mv);
    }

    @Test
    public void testVisitVarInsnObject() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final Type type = Type.getType("java.lang.String");

        sorter.visitVarInsn(0x19, var);
        assertEquals(var, sorter.remap(var, type));
        sorter.mv.visitVarInsn(4, 1);
        verifyVisitedVarInsn(1, 4, type, sorter.mv);
    }

    @Test
    public void testVisitIincInsn() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final int var = 1;
        final int increment = 1;

        sorter.visitIincInsn(var, increment);
        sorter.mv.visitIincInsn(sorter.remap(var, Type.INT_TYPE), increment);
    }

    @Test
    public void testVisitMaxs() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);

        sorter.visitMaxs(10, 15);
        sorter.mv.visitMaxs(10, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final String name = "name";
        final String desc = Type.getType("java.lang.String").getDescriptor();
        final String signature = "();V";
        final Label start = new Label();
        final Label end = new Label();
        final int index = 1;

        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        sorter.mv.visitLocalVariable(name, desc, signature, start, end, sorter.remap(index, Type.getType(desc)));
    }

    @Test
    public void testVisitFrame() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final Object[] local = new Object[1];
        final int nLocal = 1;
        final Object[] stack = new Object[1];
        final int nStack = 1;

        sorter.visitFrame(0, nLocal, local, nStack, stack);

        final Object[] newLocals = new Object[2];
        newLocals[0] = TypeUtils.TOP;
        newLocals[1] = TypeUtils.TOP;

        when(sorter.newLocals).thenReturn(newLocals);

        sorter.visitFrame(0, 2, local, nStack, stack);
        verifyVisitedFrame(nLocal, local, stack, sorter.mv);
    }

    @Test
    public void testVisitFrameWithChanged() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final Object[] local = new Object[1];
        final int nLocal = 1;
        final Object[] stack = new Object[1];
        final int nStack = 1;

        sorter.changed = true;

        when(sorter.newLocals).thenReturn(new Object[0]);

        sorter.visitFrame(0, nLocal, local, nStack, stack);
        verifyVisitedFrame(-1, local, stack, sorter.mv);
    }

    @Test
    public void testNewLocal() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final Type type = Type.getType("java.lang.String");

        final int local = sorter.newLocal(type);
        assertEquals(sorter.nextLocal, local);
        assertEquals(type, sorter.mapping[local]);
        assertEquals(type, sorter.newLocals[local]);
        assertEquals(type.getSize(), Type.getSize(Type.INT_TYPE) + Type.getSize(type));
        assertEquals(local, sorter.remap(local, type));
    }

    @Test
    public void testNewLocalObjectArray() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final Type type = Type.getType("java.lang.String[]");

        final int local = sorter.newLocal(type);
        assertEquals(sorter.nextLocal, local);
        assertEquals(type, sorter.mapping[local]);
        assertEquals(Type.getType("java.lang.Object"), sorter.newLocals[local]);
        assertEquals(type.getSize(), Type.getSize(Type.INT_TYPE) + Type.getSize(Type.getType("java.lang.Object")));
        assertEquals(local, sorter.remap(local, type));
    }

    @Test
    public void testNewLocalObject() {
        final LocalVariablesSorter sorter = createSorter();
        final MethodVisitor mv = mock(MethodVisitor.class);
        final Type type = Type.getType("java.lang.String");

        final int local = sorter.newLocal(type);
        assertEquals(sorter.nextLocal, local);
        assertEquals(type, sorter.mapping[local]);
        assertEquals(type, sorter.newLocals[local]);
        assertEquals(Type.getSize(type), Type.getSize(Type.INT_TYPE) + Type.getSize(type));
        assertEquals(local, sorter.remap(local, type));
    }

    private LocalVariablesSorter createSorter() {
        return new LocalVariablesSorter(0, "()V", mv -> new Printer(mv)) {
            @Override
            protected void setLocalType(final int local, final Type type) {
            }

            @Override
            protected void setFrameLocal(final int local, final Object type) {
            }

            @Override
            public MethodVisitor mv;
            @Override
            public Object[] newLocals;
        };
    }

    private void verifyVisitedVarInsn(final int index, final int opcode, final Type type, final MethodVisitor mv) {
        mv.visitVarInsn(opcode, index);
        assertEquals(opcode, mv.visitVarInsn(opcode, sorter.remap(index, type)));
    }

    private void verifyVisitedFrame(final int nLocal, final Object[] local, final Object[] stack, final MethodVisitor mv) {
        mv.visitFrame(0, nLocal, local, stack.length, stack);
        assertEquals(nLocal, mv.visitFrame(0, sorter.nextLocal, sorter.newLocals, stack.length, stack));
    }
}