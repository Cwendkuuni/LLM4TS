package org.apache.commons.cli.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import java.util.Arrays;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                assertEquals(LocalVariablesSorter.OBJECT_TYPE.getInternalName(), this.mv.getLocalVariable(var, "type").desc);
            }
        });

        // Test int
        sorter.visitVarInsn(Opcodes.ISTORE, 0);
        // Test float
        sorter.visitVarInsn(Opcodes.FSTORE, 0);
        // Test long
        sorter.visitVarInsn(Opcodes.LSTORE, 0);
        // Test double
        sorter.visitVarInsn(Opcodes.DSTORE, 0);
        // Test object
        sorter.visitVarInsn(Opcodes.ASTORE, 0);
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitIincInsn(int var, int increment) {
                assertEquals(var, this.remap(var, Type.INT_TYPE).intValue());
            }
        });

        sorter.visitIincInsn(0, 1);
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                assertEquals(maxLocals, sorter.nextLocal);
            }
        });

        sorter.visitMaxs(1, 1);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                assertEquals(index, this.remap(index, Type.getType(desc)).intValue());
            }
        });

        sorter.visitLocalVariable("test", "I", null, null, null, 0);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Object[] expectedLocals = new Object[] { Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, "java/lang/String" };
                assertArrayEquals(expectedLocals, local);
            }
        });

        sorter.visitFrame(Opcodes.FRAME_CODE, 5, null, 1, null);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                int local = sorter.newLocal(Type.INT_TYPE);
                assertEquals(local, this.remap(var, Type.INT_TYPE).intValue());
            }
        });

        sorter.visitVarInsn(Opcodes.ISTORE, 0);
    }

    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void setLocalType(int local, Type type) {
                assertEquals(Type.INT_TYPE, type);
            }
        });

        Type type = Type.INT_TYPE;
        sorter.setLocalType(0, type);
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void setFrameLocal(int local, Object type) {
                assertSame(Type.INT_TYPE, type);
            }
        });

        Object type = Type.INT_TYPE;
        sorter.setFrameLocal(0, type);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public int visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                int local = sorter.remap(index, Type.getType(desc));
                assertEquals(local, index);
                return local;
            }
        });

        int local = sorter.nextLocal;
        assertEquals(local, sorter.remap(local, Type.INT_TYPE).intValue());
    }

    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public int newLocalMapping(Type type) {
                assertEquals(Type.INT_TYPE, type);
                return 0;
            }
        });

        Type type = Type.INT_TYPE;
        assertEquals(0, sorter.newLocalMapping(type));
    }
}