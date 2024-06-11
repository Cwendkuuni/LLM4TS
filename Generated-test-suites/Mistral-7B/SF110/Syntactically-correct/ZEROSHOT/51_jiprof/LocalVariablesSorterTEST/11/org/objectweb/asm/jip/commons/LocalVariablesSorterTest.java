package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.MethodAdapter;
import org.objectweb.asm.commons.MethodVisitor;

import java.util.Arrays;

public class LocalVariablesSorterTest {

    @org.junit.Test
    public void testVisitVarInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        // Test int
        sorter.visitVarInsn(21, 0);
        sorter.visitVarInsn(21, 1);
        int[] expected = new int[]{0, 1};
        int[] actual = sorter.mapping;
        assertArrayEquals(expected, actual);

        // Test long
        sorter.visitVarInsn(22, 2);
        expected = new int[]{0, 1, 2};
        actual = sorter.mapping;
        assertArrayEquals(expected, actual);

        // Test double
        sorter.visitVarInsn(24, 3);
        expected = new int[]{0, 1, 2, 3};
        actual = sorter.mapping;
        assertArrayEquals(expected, actual);

        // Test float
        sorter.visitVarInsn(23, 4);
        expected = new int[]{0, 1, 2, 3, 4};
        actual = sorter.mapping;
        assertArrayEquals(expected, actual);

        // Test object
        sorter.visitVarInsn(18, 5);
        expected = new int[]{0, 1, 2, 3, 4, 5};
        actual = sorter.mapping;
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testVisitIincInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        // Test int
        sorter.visitIincInsn(0, 5);
        int[] expected = new int[]{5, 1, 0};
        int[] actual = sorter.mapping;
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testVisitMaxs() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        sorter.visitMaxs(2, 10);
        assertEquals(12, sorter.nextLocal);
    }

    @org.junit.Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        // Test int
        sorter.visitLocalVariable("a", "I", null, null, null, 0);
        int[] expected = new int[]{0, "I", null, 0, 0};
        Object[] actual = sorter.newLocals;
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 5));

        // Test long
        sorter.visitLocalVariable("b", "J", null, null, null, 1);
        expected = new int[]{0, "I", null, 1, 2};
        actual = sorter.newLocals;
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 5));

        // Test double
        sorter.visitLocalVariable("c", "D", null, null, null, 2);
        expected = new int[]{0, "I", null, 1, 2, 3};
        actual = sorter.newLocals;
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 6));

        // Test float
        sorter.visitLocalVariable("d", "F", null, null, null, 3);
        expected = new int[]{0, "I", null, 1, 2, 3, 4};
        actual = sorter.newLocals;
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 7));

        // Test object
        sorter.visitLocalVariable("e", "Ljava/lang/String;", null, null, null, 4);
        expected = new int[]{0, "I", null, 1, 2, 3, 4, 5};
        actual = sorter.newLocals;
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 8));
    }

    @org.junit.Test
    public void testVisitFrame() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);

        // Test initial frame
        int maxStack = 1;
        int maxLocals = 5;
        sorter.visitFrame(0, maxLocals, new Object[0], maxStack, new Object[0]);
        assertEquals(maxLocals, sorter.nextLocal);

        // Test changed frame
        Object[] local = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.LONG};
        int nLocal = local.length;
        int[] expectedMapping = new int[]{0, "I", null, 1, "I", 2, 3, 2, 4};
        Object[] expectedLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.LONG, Opcodes.TOP};
        int nStack = 0;
        Object[] stack = new Object[0];
        sorter.visitFrame(-1, nLocal, local, nStack, stack);
        assertArrayEquals(expectedMapping, sorter.mapping);
        assertArrayEquals(expectedLocals, sorter.newLocals);
    }

    @org.junit.Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Test int
        Type typeInt = Type.INT_TYPE;
        int localInt = sorter.newLocal(typeInt);
        assertEquals(1, localInt);

        // Test long
        Type typeLong = Type.LONG_TYPE;
        int localLong = sorter.newLocal(typeLong);
        assertEquals(2, localLong);

        // Test float
        Type typeFloat = Type.FLOAT_TYPE;
        int localFloat = sorter.newLocal(typeFloat);
        assertEquals(3, localFloat);

        // Test double
        Type typeDouble = Type.DOUBLE_TYPE;
        int localDouble = sorter.newLocal(typeDouble);
        assertEquals(4, localDouble);

        // Test object
        Type typeString = Type.getType("java/lang/String");
        int localString = sorter.newLocal(typeString);
        assertEquals(5, localString);
    }
}