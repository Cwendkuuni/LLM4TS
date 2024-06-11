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
    public void setUp() {
        mv = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0, "", mv);
    }

    @Test
    public void testVisitVarInsn() {
        final int opcode = 21;
        final int var = 0;
        Type type = Type.INT_TYPE;
        sorter.visitVarInsn(opcode, var);
        verify(mv).visitVarInsn(opcode, sorter.remap(var, type));
    }

    @Test
    public void testVisitIincInsn() {
        final int var = 0;
        final int increment = 5;
        sorter.visitIincInsn(var, increment);
        verify(mv).visitIincInsn(sorter.remap(var, Type.INT_TYPE), increment);
    }

    @Test
    public void testVisitMaxs() {
        final int maxStack = 10;
        final int maxLocals = 20;
        sorter.visitMaxs(maxStack, maxLocals);
        verify(mv).visitMaxs(maxStack, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        final String name = "name";
        final String desc = "Ljava/lang/String;";
        final String signature = "();V";
        final Label start = new Label();
        final Label end = new Label();
        final int index = 1;
        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        final Type type = Type.getType(desc);
        verify(mv).visitLocalVariable(name, desc, signature, start, end, sorter.remap(index, type));
    }

    @Test
    public void testVisitFrame() {
        final int type = 0;
        final int nLocal = 3;
        final Object[] local = new Object[3];
        final int nStack = 5;
        final Object[] stack = new Object[5];
        sorter.visitFrame(type, nLocal, local, nStack, stack);
        if (type != -1) {
            throw new IllegalStateException("Unexpected type value");
        }
        if (!sorter.changed) {
            return;
        }

        final Object[] oldLocals = new Object[sorter.newLocals.length];
        System.arraycopy(sorter.newLocals, 0, oldLocals, 0, oldLocals.length);

        int index = 0;
        for (final Object t : local) {
            Type typ;
            if (t == Opcodes.LONG || t == Opcodes.DOUBLE) {
                typ = Type.LONG_TYPE;
            } else if (t == Opcodes.FLOAT) {
                typ = Type.FLOAT_TYPE;
            } else {
                typ = Type.getObjectType((String)t);
            }
            sorter.setFrameLocal(sorter.remap(index, typ), t);
            index += (t == Opcodes.LONG || t == Opcodes.DOUBLE) ? 2 : 1;
        }

        int number = 0;
        int i = 0;
        while (index < sorter.newLocals.length) {
            final Object t2 = sorter.newLocals[index++];
            if (t2 != null && t2 != Opcodes.TOP) {
                sorter.newLocals[i] = t2;
                number = i + 1;
                if (t2 == Opcodes.LONG || t2 == Opcodes.DOUBLE) {
                    ++index;
                }
            } else {
                sorter.newLocals[i] = Opcodes.TOP;
            }
            ++i;
        }

        sorter.mv.visitFrame(type, number, sorter.newLocals, nStack, stack);
        sorter.newLocals = oldLocals;
    }

    @Test
    public void testNewLocal() {
        final Type type = Type.INT_TYPE;
        final int local = sorter.newLocal(type);
        assertEquals(local, sorter.nextLocal - type.getSize());
    }

    @Test
    public void testSetLocalType() {
        // No-op method
    }

    @Test
    public void testSetFrameLocal() {
        final int local = 1;
        final Type type = Type.LONG_TYPE;
        sorter.setFrameLocal(local, type);
        final Object[] newLocals = sorter.newLocals;
        assertEquals(newLocals[local], Opcodes.LONG);
    }

    @Test
    public void testRemap() {
        final int var = 0;
        final Type type = Type.INT_TYPE;
        final int local = sorter.newLocal(type);
        assertEquals(sorter.remap(var, type), local);
    }

    @Test
    public void testNewLocalMapping() {
        final Type type = Type.INT_TYPE;
        final int local = sorter.newLocalMapping(type);
        assertEquals(local, sorter.nextLocal);
    }
}