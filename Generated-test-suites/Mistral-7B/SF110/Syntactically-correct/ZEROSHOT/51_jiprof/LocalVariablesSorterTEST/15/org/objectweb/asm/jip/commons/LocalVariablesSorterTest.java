package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        assertEquals(1, sorter.nextLocal);
    }

    @Test
    public void testVisitVarInsn() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        sorter.visitVarInsn(21, 0);
        int expected = 0;
        assertEquals(expected, sorter.remap(0, Type.INT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        sorter.visitIincInsn(1, 1);
        int expected = 1;
        assertEquals(expected, sorter.remap(1, Type.INT_TYPE));
    }

    @Test
    public void testVisitMaxs() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        sorter.visitMaxs(1, 2);
        int expectedMaxStack = 1;
        int expectedMaxLocals = 2;
        int[] actualMaxLocals = sorter.mapping;
        int actualMaxLocalsLength = actualMaxLocals.length;
        assertEquals(expectedMaxStack, sorter.mv.maxStack);
        assertEquals(expectedMaxLocals, actualMaxLocalsLength);
    }

    @Test
    public void testVisitLocalVariable() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        sorter.visitLocalVariable("test", "Ljava/lang/String;", null, null, null, 0);
        int expected = 0;
        assertEquals(expected, sorter.remap(0, Type.getType("Ljava/lang/String;")));
    }

    @Test
    public void testVisitFrame() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        Object[] oldLocals = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.getObjectType("java/lang/String")};
        sorter.visitFrame(-1, 6, oldLocals, 1, null);
        Object[] expectedLocals = {Type.INTEGER, Type.LONG, Type.FLOAT, Type.LONG, Type.DOUBLE, Type.getObjectType("java/lang/String")};
        Object[] actualLocals = sorter.newLocals;
        assertArrayEquals(expectedLocals, actualLocals);
    }

    @Test
    public void testNewLocal() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        Type type = Type.getObjectType("java/lang/Integer");
        int expectedLocal = sorter.newLocal(type);
        int actualLocal = sorter.mapping[expectedLocal];
        assertEquals(expectedLocal, actualLocal);
    }

    @Test
    public void testSetLocalTypeAndSetFrameLocal() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        Type type = Type.getObjectType("java/lang/Integer");
        int local = sorter.newLocal(type);
        sorter.setLocalType(local, type);
        Object expectedType = Type.getObjectType("java/lang/Integer");
        Object actualType = sorter.newLocals[local];
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testRemap() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        int var = 1;
        Type type = Type.getObjectType("java/lang/Integer");
        int expected = sorter.nextLocal - type.getSize();
        int actual = sorter.remap(var, type);
        assertEquals(expected, actual);
    }

    @Test
    public void testNewLocalMapping() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        Type type = Type.getObjectType("java/lang/Integer");
        int expectedLocal = sorter.nextLocal;
        int actualLocal = sorter.newLocalMapping(type);
        assertEquals(expectedLocal, actualLocal);
    }

    @Test
    public void testSetFrameLocalWithNull() {
        String desc = "(Ljava/lang/String;)V";
        ClassReader cr = new ClassReader(LocalVariablesSorterTest.class.getResourceAsStream("/LocalVariablesSorterTest.class"));
        LocalVariablesSorter sorter = new LocalVariablesSorter(AccessFlag.PUBLIC, desc, new MethodVisitor(0, cr.getClassWriter()));
        int local = 0;
        sorter.setFrameLocal(local, null);
        assertNull(sorter.newLocals[local]);
    }
}