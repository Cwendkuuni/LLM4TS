package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.type.Type;

import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288));
        assertEquals(1, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288)) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, 0);
            }
        };
        localVariablesSorter.visitVarInsn(22, 1);
        assertEquals(1, localVariablesSorter.remap(1, Type.LONG_TYPE));
    }

    @Test
    public void testVisitVarInsnWithObjectType() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288)) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, 0);
            }
        };
        localVariablesSorter.visitVarInsn(21, 1);
        assertEquals(0, localVariablesSorter.remap(1, Type.INT_TYPE));
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithInvalidType() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288)) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(1, nLocal, local, nStack, stack);
            }
        };
        localVariablesSorter.visitFrame(0, 1, new Object[]{Type.getType("java.lang.String")}, 1, null);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288)) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(1, nLocal, local, nStack, stack);
            }
        };
        Object[] localVariables = new Object[]{Type.getType("int"), Opcodes.INTEGER, Opcodes.TOP, Type.getType("java.lang.String")};
        localVariablesSorter.visitFrame(1, localVariables.length, localVariables, 1, null);
        assertEquals(localVariables.length, localVariablesSorter.newLocals.length);
        for (int i = 0; i < localVariables.length; i++) {
            assertEquals(localVariables[i], localVariablesSorter.newLocals[i]);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithUnchangedFlag() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288)) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(1, nLocal, local, nStack, stack);
            }
        };
        localVariablesSorter.visitFrame(1, 1, null, 1, null);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288));
        assertEquals(1, localVariablesSorter.newLocal(Type.INT_TYPE));
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288));
        localVariablesSorter.setFrameLocal(1, Type.INT_TYPE);
        assertEquals(Type.INT_TYPE, localVariablesSorter.newLocals[0]);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(524288));
        localVariablesSorter.visitVarInsn(21, 1);
        localVariablesSorter.visitVarInsn(21, 2);
        localVariablesSorter.visitVarInsn(21, 3);
        localVariablesSorter.visitVarInsn(21, 4);
        localVariablesSorter.visitVarInsn(21, 5);

        assertEquals(0, localVariablesSorter.remap(1, Type.INT_TYPE));
        assertEquals(1, localVariablesSorter.remap(2, Type.INT_TYPE));
        assertEquals(2, localVariablesSorter.remap(3, Type.INT_TYPE));
        assertEquals(3, localVariablesSorter.remap(4, Type.INT_TYPE));
        assertEquals(4, localVariablesSorter.remap(5, Type.INT_TYPE));

        localVariablesSorter.visitLocalVariable("test", "I", null, null, null, 5);
        assertEquals(5, localVariablesSorter.remap(5, Type.INT_TYPE));
        assertEquals(5, localVariablesSorter.remap(5, Type.TYPE_INT));

        localVariablesSorter.visitFrame(1, 1, null, 1, null);
        assertEquals(5, localVariablesSorter.remap(5, Type.INT_TYPE));
    }
}