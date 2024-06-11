package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    @Test
    public void testNewLocal() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // Do nothing
            }
        });
        final Type intType = Type.INT_TYPE;
        final Type floatType = Type.FLOAT_TYPE;
        final Type longType = Type.LONG_TYPE;
        final Type doubleType = Type.DOUBLE_TYPE;
        final Type stringType = Type.getType("java.lang.String");

        assertEquals(1, sorter.newLocal(intType).intValue());
        assertEquals(2, sorter.newLocal(floatType).intValue());
        assertEquals(3, sorter.newLocal(longType).intValue());
        assertEquals(4, sorter.newLocal(doubleType).intValue());
        assertEquals(5, sorter.newLocal(stringType).intValue());

        final MethodVisitor mv = mock(MethodVisitor.class);
        final LocalVariablesSorter sorter2 = new LocalVariablesSorter(0, "", mv);
        final int local = sorter2.newLocal(intType);
        when(mv.visitLocalVariable(anyString(), anyString(), anyString(), any(Label.class), any(Label.class), eq(local)))
                .thenReturn(null);
        assertEquals(local, sorter2.remap(local, intType));
    }

    @Test
    public void testVisitVarInsn() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // Do nothing
            }
        });

        final Type intType = Type.INT_TYPE;
        final Type longType = Type.LONG_TYPE;

        assertEquals(1, sorter.remap(0, intType));
        assertEquals(1, sorter.remap(1, intType));
        assertEquals(2, sorter.remap(2, intType));
        assertEquals(2, sorter.remap(3, intType));

        assertEquals(1, sorter.remap(0, longType));
        assertEquals(2, sorter.remap(1, longType));
        assertEquals(3, sorter.remap(2, longType));
        assertEquals(3, sorter.remap(3, longType));

        final int local = sorter.newLocal(intType);
        assertEquals(local, sorter.remap(local, intType));
    }

    @Test
    public void testVisitIincInsn() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitIincInsn(int var, int increment) {
                // Do nothing
            }
        });

        final int local = sorter.newLocal(Type.INT_TYPE);
        assertEquals(local, sorter.remap(local, Type.INT_TYPE));
        assertEquals(local + 1, sorter.remap(local + 1, Type.INT_TYPE));
    }

    @Test
    public void testVisitMaxs() {
        final int maxLocals = 10;
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                // Do nothing
            }
        });

        assertEquals(maxLocals, sorter.nextLocal);
        assertEquals(maxLocals, sorter.remap(maxLocals - 1, Type.INT_TYPE));
        assertEquals(-1, sorter.remap(maxLocals, Type.INT_TYPE));
    }

    @Test
    public void testVisitLocalVariable() {
        final Type intType = Type.INT_TYPE;
        final Type longType = Type.LONG_TYPE;

        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // Do nothing
            }
        });

        final int local1 = sorter.newLocal(intType);
        final int local2 = sorter.newLocal(intType);

        assertEquals(local1, sorter.remap(local1, intType));
        assertEquals(local2, sorter.remap(local2, intType));
        assertEquals(local1 + 1, sorter.remap(local2, intType));

        final int local3 = sorter.newLocal(longType);
        assertEquals(local3, sorter.remap(local3, longType));
        assertEquals(local3 + 1, sorter.remap(local3 + 1, longType));
    }

    @Test
    public void testVisitFrame() {
        final Type intType = Type.INT_TYPE;
        final Type longType = Type.LONG_TYPE;
        final Type stringType = Type.getType("java.lang.String");

        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // Do nothing
            }
        });

        final int local1 = sorter.newLocal(intType);
        final int local2 = sorter.newLocal(intType);
        final int local3 = sorter.newLocal(longType);
        final int local4 = sorter.newLocal(stringType);

        assertEquals(local1, sorter.remap(local1, intType));
        assertEquals(local2, sorter.remap(local2, intType));
        assertEquals(local1 + 1, sorter.remap(local2, intType));
        assertEquals(local3, sorter.remap(local3, longType));
        assertEquals(local3 + 1, sorter.remap(local3 + 1, longType));
        assertEquals(local4, sorter.remap(local4, stringType));

        final int local5 = sorter.newLocal(intType);
        final int local6 = sorter.newLocal(intType);
        final int local7 = sorter.newLocal(longType);

        final Object[] newLocals = {local5, local6, Opcodes.TOP, local7, Opcodes.TOP};
        final int number = newLocals.length;

        assertEquals(number, sorter.remap(sorter.nextLocal - 1, intType));
        assertEquals(local5, sorter.remap(local5, intType));
        assertEquals(local6, sorter.remap(local6, intType));
        assertEquals(local7, sorter.remap(local7, longType));
        assertEquals(local5 + 1, sorter.remap(local6, intType));
        assertEquals(local7 + 1, sorter.remap(local7 + 1, longType));
    }
}