package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {
    private static final int MAX_LOCALS = 10;
    private static final int MAX_STACK = 5;

    @Test
    public void testNewLocal() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {});
        for (Type type = Type.INT_TYPE; type != null; type = Type.getObjectType(type.getDescriptor())) {
            final int local = sorter.newLocal(type);
            assertEquals(local, sorter.nextLocal - type.getSize());
        }
    }

    @Test
    public void testVisitVarInsn() {
        final Type[] types = new Type[] { Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE };
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                assertEquals(var, opcode == 21 || opcode == 54 ? 0 : sorter.remap(var, Type.getType(types[opcode - 22])));
            }
        });
        for (int opcode = 0; opcode < 25; ++opcode) {
            if (opcode == 22 || opcode == 54) {
                continue;
            }
            final int local = sorter.remap(0, Type.getType(types[opcode - 22]));
            assertEquals(local, 0);
        }
    }

    @Test
    public void testVisitLocalVariable() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                final int newIndex = sorter.remap(index, Type.getType(desc));
                assertEquals(newIndex, index);
            }
        });
        for (int i = 0; i < MAX_LOCALS; ++i) {
            final int index = i;
            final String name = "local" + i;
            final String desc = "Ljava/lang/String;";
            final LocalVariableAttribute attribute = new LocalVariableAttribute(name, desc, signature, start, end, index);
            sorter.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    @Test
    public void testVisitFrame() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                assertEquals(nLocal, sorter.nextLocal);
                assertArrayEquals(sorter.newLocals, local);
                assertEquals(nStack, MAX_STACK);
                assertArrayEquals(new Object[MAX_STACK], stack);
                assertTrue(sorter.changed);
            }
        });
        final Object[] locals = new Object[MAX_LOCALS];
        for (int i = 0; i < MAX_LOCALS; ++i) {
            locals[i] = Opcodes.INTEGER;
            sorter.setFrameLocal(i, locals[i]);
        }
        sorter.visitFrame(-1, MAX_LOCALS, locals, MAX_STACK, new Object[MAX_STACK]);
        assertNull(sorter.newLocals);
        assertTrue(sorter.changed = false);
    }

    @Test
    public void testSetLocalType() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {});
        final int local = sorter.newLocal(Type.INT_TYPE);
        sorter.setLocalType(local, Type.LONG_TYPE);
        assertSame(Type.LONG_TYPE, Type.getType(sorter.localVariables[local].desc));
    }

    @Test
    public void testSetFrameLocal() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {});
        final int local = sorter.newLocal(Type.INT_TYPE);
        final Object object = new Object();
        sorter.setFrameLocal(local, object);
        assertNotNull(sorter.newLocals[local]);
        assertSame(object, sorter.newLocals[local]);
    }

    @Test
    public void testRemap() {
        final LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM5) {});
        final int local = sorter.newLocal(Type.INT_TYPE);
        assertEquals(local, sorter.remap(local, Type.INT_TYPE));
        assertEquals(-1, sorter.remap(local + 1, Type.INT_TYPE));
        assertEquals(-1, sorter.remap(local, Type.LONG_TYPE));
    }
}