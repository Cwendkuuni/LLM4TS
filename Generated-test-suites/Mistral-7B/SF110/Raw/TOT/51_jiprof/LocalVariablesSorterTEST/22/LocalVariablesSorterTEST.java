package org.objectweb.asm.jip.commons.test;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;
    private ClassWriter classWriter;

    @Before
    public void setUp() {
        localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test/TestMethod:(I)V", new MethodVisitor(0, classWriter) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                localVariablesSorter.visitVarInsn(opcode, var);
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                localVariablesSorter.visitIincInsn(var, increment);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                localVariablesSorter.visitMaxs(maxStack, maxLocals);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
            }

            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
            }

            @Override
            public void visitCode() {
            }
        });
        classWriter = new ClassWriter(0);
    }

    @Test
    public void testVisitVarInsnInt() {
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 2);

        final int[] expected = {0, 1, 2};
        final int[] actual = localVariablesSorter.mapping;
        assertEquals("Unexpected variable mapping order", expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithExpandFramesUnset() {
        localVariablesSorter.visitFrame(-1, 1, new Object[]{Opcodes.TOP}, 2, new Object[]{Opcodes.TOP});
    }

    @Test
    public void testVisitFrameWithExpandFramesSet() {
        localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test/TestMethod:(I)V", new MethodVisitor(1, classWriter) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
            }
        });

        final int[] inputLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
        final Object[] inputFrameLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};

        localVariablesSorter.visitFrame(-1, inputLocals.length, inputFrameLocals, 1, new Object[]{Opcodes.TOP});

        final Object[] expectedLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
        final Object[] actualLocals = localVariablesSorter.newLocals;
        assertArrayEquals("Unexpected local variables order", expectedLocals, actualLocals);

        localVariablesSorter.visitFrame(-1, inputLocals.length, null, 1, null);
        assertNull("Unexpected local variables after frame reset", localVariablesSorter.newLocals);
    }

    @Test
    public void testVisitFrameWithDifferentStackSizes() {
        localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test/TestMethod:(I)V", new MethodVisitor(0, classWriter) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                localVariablesSorter.visitFrame(type, nLocal, local, 2, stack);
            }
        });

        final int[] inputLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
        final Object[] inputFrameLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};

        localVariablesSorter.visitFrame(-1, inputLocals.length, inputFrameLocals, 1, new Object[]{Opcodes.TOP});

        final Object[] expectedLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
        final Object[] actualLocals = localVariablesSorter.newLocals;
        assertArrayEquals("Unexpected local variables order", expectedLocals, actualLocals);

        localVariablesSorter.visitFrame(-1, inputLocals.length, null, 2, new Object[]{Opcodes.TOP, Opcodes.TOP});

        final Object[] expectedFrameLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.TOP};
        final Object[] actualFrameLocals = localVariablesSorter.newLocals;
        assertArrayEquals("Unexpected frame local variables order", expectedFrameLocals, actualFrameLocals);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(0, 1);
        localVariablesSorter.visitIincInsn(1, 2);
        localVariablesSorter.visitIincInsn(2, 3);

        final int[] expected = {0, 1, 2};
        final int[] actual = localVariablesSorter.mapping;
        assertEquals("Unexpected variable mapping order", expected, actual);
    }

    @Test
    public void testVisitMaxs() {
        final int maxStack = 5;
        final int maxLocals = 10;
        localVariablesSorter.visitMaxs(maxStack, maxLocals);

        assertEquals("Unexpected maximum stack size", maxStack, localVariablesSorter.nextLocal - localVariablesSorter.firstLocal);
        assertEquals("Unexpected maximum local variables size", maxLocals, localVariablesSorter.mapping.length / 2);
    }
}