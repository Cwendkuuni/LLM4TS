package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter sorter;
    private MethodVisitor mv;

    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0, "", mv);
    }

    @Test
    public void testVisitVarInsnWithPrimitiveTypes() {
        for (int i = 0; i < 5; ++i) {
            Type type = Type.getPrimitiveType(i);
            sorter.visitVarInsn(21, 0);
            sorter.visitVarInsn(opcodeForType(type), 0);
            assertArrayEquals(new int[]{0}, sorter.mapping);
        }
    }

    @Test
    public void testVisitVarInsnWithObjectType() {
        Type type = Type.getType("java.lang.String");
        sorter.visitVarInsn(23, 0);
        sorter.visitVarInsn(opcodeForType(type), 0);
        assertArrayEquals(new int[]{1, 1}, sorter.mapping);
    }

    @Test
    public void testVisitVarInsnWithLongType() {
        Type type = Type.LONG_TYPE;
        sorter.visitVarInsn(22, 0);
        sorter.visitVarInsn(opcodeForType(type), 0);
        assertArrayEquals(new int[]{1, 2}, sorter.mapping);
    }

    @Test
    public void testVisitVarInsnWithDoubleType() {
        Type type = Type.DOUBLE_TYPE;
        sorter.visitVarInsn(24, 0);
        sorter.visitVarInsn(opcodeForType(type), 0);
        assertArrayEquals(new int[]{1, 3}, sorter.mapping);
    }

    @Test
    public void testVisitIincInsn() {
        int var = 1;
        int increment = 1;
        sorter.visitIincInsn(var, increment);
        assertEquals(var + increment, sorter.mapping[var]);
    }

    @Test
    public void testVisitMaxs() {
        int maxLocals = 10;
        int maxStack = 20;
        sorter.visitMaxs(maxStack, maxLocals);
        assertEquals(maxLocals, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "name";
        String desc = "Ljava/lang/String;";
        String signature = "Signature";
        Label start = mock(Label.class);
        Label end = mock(Label.class);
        int index = 0;

        Type type = Type.getType(desc);
        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        assertEquals(index + 1, sorter.mapping[index]);
    }

    @Test
    public void testVisitFrame() {
        int nLocal = 10;
        Object[] local = new Object[nLocal];
        Object[] stack = new Object[5];

        for (int i = 0; i < nLocal; ++i) {
            Type type;
            if (i % 2 == 0) {
                type = Type.LONG_TYPE;
            } else {
                type = Type.INT_TYPE;
            }
            LocalVariableAttribute attribute = new LocalVariableAttribute(i, type.getDescriptor(), null, null, null, null);
            local[i] = attribute;
        }

        when(mv.visitFrame(Type.INTEGER_TYPE, nLocal, local, 5, stack)).thenReturn(null);

        sorter.visitFrame(Type.INTEGER_TYPE, nLocal, local, 5, stack);

        assertArrayEquals(local, sorter.newLocals);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        assertEquals(local, sorter.mapping[0]);
        assertEquals(1, sorter.mapping.length);
    }

    @Test
    public void testNewLocalWithLongType() {
        Type type = Type.LONG_TYPE;
        int local = sorter.newLocal(type);
        assertEquals(local, sorter.mapping[0]);
        assertEquals(1, sorter.mapping.length);
        assertEquals(local + 1, sorter.nextLocal);
    }

    @Test
    public void testNewLocalWithDoubleType() {
        Type type = Type.DOUBLE_TYPE;
        int local = sorter.newLocal(type);
        assertEquals(local, sorter.mapping[0]);
        assertEquals(1, sorter.mapping.length);
        assertEquals(local + 2, sorter.nextLocal);
    }

    @Test
    public void testNewLocalWithObjectType() {
        Type type = Type.getType("java.lang.String");
        int local = sorter.newLocal(type);
        assertEquals(local, sorter.mapping[0]);
        assertEquals(1, sorter.mapping.length);
        assertEquals(local + 1, sorter.nextLocal);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.LONG_TYPE;

        sorter.setLocalType(local, type);
        assertEquals(type, sorter.newLocals[local]);
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Type.LONG_TYPE;

        sorter.setFrameLocal(local, type);
        assertEquals(type, sorter.newLocals[local]);
    }

    @Test
    public void testRemap() {
        int var = 1;
        int size = 10;
        Type type = Type.INT_TYPE;

        sorter.nextLocal = size;
        sorter.mapping = new int[size];

        int local = sorter.newLocal(type);
        int mappedVar = sorter.remap(var, type);

        assertEquals(var, mappedVar);
        assertEquals(local, sorter.mapping[var]);
    }

    @Test
    public void testRemapWithNonExistentVariable() {
        int var = 10;
        Type type = Type.INT_TYPE;

        sorter.nextLocal = 5;
        sorter.mapping = new int[5];

        int local = sorter.newLocal(type);
        int mappedVar = sorter.remap(var, type);

        assertNull(mappedVar);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = Type.LONG_TYPE;
        int local = sorter.newLocalMapping(type);
        assertEquals(local, sorter.mapping[0]);
        assertEquals(1, sorter.mapping.length);
        assertEquals(local + 1, sorter.nextLocal);
    }

    private int opcodeForType(Type type) {
        switch (type.getSort()) {
            case 1:
                return 21;
            case 2:
                return 54;
            case 3:
                return 21;
            case 4:
                return 55;
            case 5:
                return 56;
            case 6:
                return 57;
            case 7:
                return 57;
            case 8:
                return 58;
            case 9:
                return -1;
            default:
                return 23;
        }
    }
}