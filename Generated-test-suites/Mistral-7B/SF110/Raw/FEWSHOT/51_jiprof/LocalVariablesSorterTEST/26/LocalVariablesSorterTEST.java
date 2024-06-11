package org.apache.commons.cli;

import org.objectweb.asm.jip.commons.Type;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        Type[] types = new Type[]{Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("java/lang/String")};
        for (Type type : types) {
            sorter.visitVarInsn(opcodeFor(type), 0);
            assertTrue(sorter.changed);
            assertEquals(sorter.firstLocal + type.getSize(), sorter.remap(0, type));
            sorter.changed = false;
        }
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitIincInsn(int var, int increment) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        sorter.visitIincInsn(0, 1);
        assertTrue(sorter.changed);
        assertEquals(sorter.firstLocal + Type.INT_TYPE.getSize(), sorter.remap(0, Type.INT_TYPE));
        sorter.changed = false;

        sorter.visitIincInsn(1, 1);
        assertEquals(0, sorter.remap(1, Type.INT_TYPE));
        assertEquals(sorter.firstLocal + Type.INT_TYPE.getSize() * 2, sorter.remap(1, Type.INT_TYPE));
        assertTrue(sorter.changed);
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        sorter.visitMaxs(1, 1);
        assertEquals(sorter.nextLocal, sorter.remap(0, Type.INT_TYPE));
        assertTrue(sorter.changed);
        assertEquals(sorter.nextLocal + 1, sorter.remap(1, Type.INT_TYPE));
        assertTrue(sorter.changed);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        String[] names = new String[]{"a", "b", "c"};
        Type[] types = new Type[]{Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE};
        Label start = new Label();
        Label end = new Label();
        for (int i = 0; i < names.length; i++) {
            sorter.visitLocalVariable(names[i], types[i].getDescriptor(), null, start, end, i);
            assertEquals(i, sorter.remap(i, types[i]));
            assertTrue(sorter.changed);
        }
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitFrame(int type, int number, Object[] local, int nStack, Object[] stack) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        Object[] locals = new Object[]{null, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, "java/lang/String"};
        Object[] stack = new Object[]{null, null, null, null};
        sorter.visitFrame(0, locals.length, locals, stack.length, stack);
        assertEquals(sorter.newLocals.length, locals.length);
        for (int i = 0; i < locals.length; i++) {
            Object local = locals[i];
            Object frameLocal = sorter.newLocals[i];
            if (local != null) {
                assertEquals(local, frameLocal);
            }
        }
        assertTrue(sorter.changed);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        Type[] types = new Type[]{Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("java/lang/String")};
        for (Type type : types) {
            int local = sorter.newLocal(type);
            assertEquals(local, sorter.remap(sorter.nextLocal - type.getSize(), type));
            assertTrue(sorter.changed);
        }
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        Object[] locals = new Object[]{null, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, "java/lang/String"};
        for (int i = 0; i < locals.length; i++) {
            sorter.setFrameLocal(i, locals[i]);
            assertEquals(locals[i], sorter.newLocals[i]);
        }
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        Type[] types = new Type[]{Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.getType("java/lang/String")};
        for (Type type : types) {
            for (int var = 0; var < 10; var++) {
                int remapped = sorter.remap(var, type);
                if (var < sorter.firstLocal) {
                    assertEquals(var, remapped);
                }
                else {
                    assertEquals(sorter.firstLocal + type.getSize() * (var - sorter.firstLocal / type.getSize()), remapped);
                }
                assertTrue(sorter.changed);
            }
        }
    }

    @Test
    public void testChanged() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }
        });

        assertTrue(sorter.changed);
    }

    private static int opcodeFor(Type type) {
        if (type == Type.INT_TYPE) {
            return 21;
        }
        else if (type == Type.LONG_TYPE) {
            return 22;
        }
        else if (type == Type.FLOAT_TYPE) {
            return 23;
        }
        else if (type == Type.DOUBLE_TYPE) {
            return 24;
        }
        else if (type instanceof ClassType) {
            return 56;
        }
        else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(252) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // This test case does not perform any actual test, but just to check that the method is called
            }

            @Override
            protected void setLocalType(int local, Type type) {
                assertEquals(type, Type.INT_TYPE);
            }
        });

        Type type = Type.INT_TYPE;
        sorter.setLocalType(0, type);
        assertTrue(sorter.changed);
    }
}