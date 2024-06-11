package org.apache.commons.cli.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    private MethodVisitor mv;
    private LocalVariablesSorter sorter;

    @Before
    public void setup() {
        mv = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", mv);
    }

    @Test
    public void testVisitVarInsn() {
        // Test int
        Type intType = Type.INT_TYPE;
        sorter.visitVarInsn(Opcodes.ILOAD, 0);
        assertEquals(0, sorter.remap(0, intType));
        verify(mv).visitVarInsn(Opcodes.ILOAD, 0);

        // Test long
        Type longType = Type.LONG_TYPE;
        sorter.visitVarInsn(Opcodes.LLOAD, 0);
        assertEquals(0, sorter.remap(0, longType));
        verify(mv).visitVarInsn(Opcodes.LLOAD, 0);

        // Test float
        Type floatType = Type.FLOAT_TYPE;
        sorter.visitVarInsn(Opcodes.FLOAD, 0);
        assertEquals(0, sorter.remap(0, floatType));
        verify(mv).visitVarInsn(Opcodes.FLOAD, 0);

        // Test double
        Type doubleType = Type.DOUBLE_TYPE;
        sorter.visitVarInsn(Opcodes.DLOAD, 0);
        assertEquals(0, sorter.remap(0, doubleType));
        verify(mv).visitVarInsn(Opcodes.DLOAD, 0);

        // Test object
        Type objectType = Type.getType("java.lang.String");
        sorter.visitVarInsn(Opcodes.ALOAD, 0);
        assertEquals(0, sorter.remap(0, objectType));
        verify(mv).visitVarInsn(Opcodes.ALOAD, 0);

        // Test negative index
        sorter.visitVarInsn(Opcodes.ILOAD, -1);
        assertNull(sorter.remap(-1, intType));
        verify(mv).visitVarInsn(Opcodes.ILOAD, -1);
    }

    @Test
    public void testVisitIincInsn() {
        Type intType = Type.INT_TYPE;
        sorter.visitIincInsn(0, 1);
        assertEquals(0, sorter.remap(0, intType));
        assertEquals(1, sorter.remap(0, intType));
        verify(mv).visitIincInsn(0, 1);
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 2;
        int maxLocals = 3;
        sorter.visitMaxs(maxStack, maxLocals);
        assertEquals(maxStack, sorter.nextLocal);
        verify(mv).visitMaxs(maxStack, maxLocals);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "name";
        String desc = "Ljava/lang/String;";
        String signature = null;
        Label start = mock(Label.class);
        Label end = mock(Label.class);
        int index = 1;
        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        assertEquals(1, sorter.remap(index, Type.getType(desc)));
        verify(mv).visitLocalVariable(name, desc, signature, start, end, 1);
    }

    @Test
    public void testVisitFrame() {
        int nLocal = 3;
        Object[] local = new Object[] { Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG };
        int nStack = 2;
        Object[] stack = new Object[] { Opcodes.INTEGER, Opcodes.FLOAT };
        sorter.visitFrame(Opcodes.FRAME_TYPE, nLocal, local, nStack, stack);
        int number = 0;
        Object[] newLocals = new Object[4];
        for (Object t : local) {
            final int size = (t == Opcodes.LONG || t == Opcodes.DOUBLE) ? 2 : 1;
            int localIndex = sorter.newLocal(Type.getType((t == Opcodes.LONG || t == Opcodes.DOUBLE) ? "J" : "F"));
            newLocals[number++] = localIndex;
        }
        sorter.visitFrame(Opcodes.FRAME_TYPE, number, newLocals, nStack, stack);
        assertArrayEquals(newLocals, sorter.newLocals);
        verify(mv).visitFrame(Opcodes.FRAME_TYPE, number, newLocals, nStack, stack);
    }
}