package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter sorter;
    private MethodVisitor mvMock;

    @Before
    public void setUp() {
        mvMock = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0, "()V", mvMock);
    }

    @Test
    public void testVisitVarInsn() {
        verify(mvMock, never()).visitVarInsn(anyInt(), anyInt());

        sorter.visitVarInsn(21, 0);
        verify(mvMock).visitVarInsn(21, 0);

        sorter.visitVarInsn(23, 1);
        verify(mvMock).visitVarInsn(23, 1);

        Type longType = Type.LONG_TYPE;
        sorter.visitVarInsn(22, 2);
        verify(mvMock).visitVarInsn(22, sorter.remap(2, longType));

        Type doubleType = Type.DOUBLE_TYPE;
        sorter.visitVarInsn(24, 3);
        verify(mvMock).visitVarInsn(24, sorter.remap(3, doubleType));

        Type floatType = Type.FLOAT_TYPE;
        sorter.visitVarInsn(23, 4);
        verify(mvMock).visitVarInsn(23, sorter.remap(4, floatType));

        Type intType = Type.INT_TYPE;
        sorter.visitVarInsn(21, 5);
        verify(mvMock).visitVarInsn(21, sorter.remap(5, intType));

        Type objectType = LocalVariablesSorter.OBJECT_TYPE;
        sorter.visitVarInsn(21, 6);
        verify(mvMock).visitVarInsn(21, sorter.remap(6, objectType));
    }

    @Test
    public void testVisitIincInsn() {
        verify(mvMock, never()).visitIincInsn(anyInt(), anyInt());

        int var = 0;
        int increment = 1;
        sorter.visitIincInsn(var, increment);
        verify(mvMock).visitIincInsn(sorter.remap(var, Type.INT_TYPE), increment);

        var += 2;
        int longIncrement = 2;
        sorter.visitIincInsn(var, longIncrement);
        verify(mvMock, times(2)).visitIincInsn(eq(sorter.remap(var, Type.LONG_TYPE)), anyInt());
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 1;
        int maxLocals = 1;
        sorter.visitMaxs(maxStack, maxLocals);
        verify(mvMock).visitMaxs(maxStack, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        verify(mvMock, never()).visitLocalVariable(anyString(), anyString(), anyString(), any(Label.class), any(Label.class), anyInt());

        String name = "name";
        String desc = "I";
        String signature = "";
        Label start = mock(Label.class);
        Label end = mock(Label.class);
        int index = 1;
        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        verify(mvMock).visitLocalVariable(name, desc, signature, start, end, sorter.remap(index, Type.getType(desc)));
    }

    @Test
    public void testVisitFrame() {
        int type = -1;
        int nLocal = 1;
        Object[] local = new Object[1];
        int nStack = 1;
        Object[] stack = new Object[1];
        sorter.visitFrame(type, nLocal, local, nStack, stack);
        verify(mvMock).visitFrame(type, nLocal, local, nStack, stack);

        sorter.changed = true;
        Object[] newLocals = new Object[2];
        System.arraycopy(sorter.newLocals, 0, newLocals, 0, newLocals.length);
        int index = 0;
        for (Object t : local) {
            final int size = (t == Opcodes.LONG || t == Opcodes.DOUBLE) ? 2 : 1;
            Type typ;
            if (t == Opcodes.INTEGER) {
                typ = Type.INT_TYPE;
            } else if (t == Opcodes.FLOAT) {
                typ = Type.FLOAT_TYPE;
            } else if (t == Opcodes.LONG) {
                typ = Type.LONG_TYPE;
            } else if (t == Opcodes.DOUBLE) {
                typ = Type.DOUBLE_TYPE;
            } else {
                typ = Type.getObjectType((String)t);
            }
            sorter.setFrameLocal(sorter.remap(index, typ), t);
            index += size;
        }
        int number = 0;
        int i = 0;
        while (index < newLocals.length) {
            Object t2 = newLocals[index++];
            if (t2 != null && t2 != Opcodes.TOP) {
                sorter.newLocals[i] = t2;
                number = i + 1;
                if (t2 == Opcodes.LONG || t2 == Opcodes.DOUBLE) {
                    ++index;
                }
            }
            i++;
        }
        sorter.mv.visitFrame(type, number, sorter.newLocals, nStack, stack);
        verify(mvMock).visitFrame(type, number, sorter.newLocals, nStack, stack);
        sorter.newLocals = local;
    }

    @Test
    public void testNewLocal() {
        Type intType = Type.INT_TYPE;
        int local = sorter.newLocal(intType);
        assertEquals(local, sorter.nextLocal - intType.getSize());
        assertSame(intType, Type.getType(sorter.mapping[local]));

        Type longType = Type.LONG_TYPE;
        local = sorter.newLocal(longType);
        assertEquals(local, sorter.nextLocal - longType.getSize());
        assertSame(longType, Type.getType(sorter.mapping[local]));

        Type floatType = Type.FLOAT_TYPE;
        local = sorter.newLocal(floatType);
        assertEquals(local, sorter.nextLocal - floatType.getSize());
        assertSame(floatType, Type.getType(sorter.mapping[local]));

        Type doubleType = Type.DOUBLE_TYPE;
        local = sorter.newLocal(doubleType);
        assertEquals(local, sorter.nextLocal - doubleType.getSize());
        assertSame(doubleType, Type.getType(sorter.mapping[local]));

        Type objectType = Type.getObjectType("java/lang/String");
        local = sorter.newLocal(objectType);
        assertEquals(local, sorter.nextLocal - objectType.getSize());
        assertSame(objectType, Type.getType(sorter.mapping[local]));
    }

    @Test
    public void testSetLocalType() {
        // No implementation, so no test.
    }

    @Test
    public void testSetFrameLocal() {
        int local = 0;
        String objectTypeDescriptor = "Ljava/lang/String;";
        sorter.setFrameLocal(local, "java/lang/String");
        assertEquals(local, sorter.mapping[local + 1]);
        assertSame(Type.getObjectType(objectTypeDescriptor), Type.getType(sorter.mapping[local]));
    }

    @Test
    public void testRemap() {
        int var = 0;
        Type intType = Type.INT_TYPE;

        // Test with var < firstLocal
        assertEquals(var, sorter.remap(var, intType));

        // Test with firstLocal <= var < nextLocal
        int nextLocal = 5;
        sorter = new LocalVariablesSorter(0, "()V", mvMock);
        sorter.firstLocal = 1;
        sorter.nextLocal = nextLocal;
        assertEquals(var, sorter.remap(var, intType));

        // Test with nextLocal <= var
        nextLocal += 2;
        sorter.nextLocal = nextLocal;
        int newIndex = sorter.remap(var, intType);
        assertEquals(var, newIndex);
        assertEquals(intType, Type.getType(sorter.mapping[newIndex]));
        assertTrue(sorter.changed);
    }

    @Test
    public void testNewLocalMapping() {
        int local = 0;
        Type intType = Type.INT_TYPE;
        assertEquals(local, sorter.newLocalMapping(intType));
    }
}