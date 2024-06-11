package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.MethodAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.anner.AnnotationDefault;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.Method;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsnAndVisitIincInsn() throws IOException {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5, null) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, var);
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(var, increment);
            }
        });

        Type intType = Type.INT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getType("java.lang.Object");

        sorter.visitVarInsn(Opcodes.ALOAD, 0); // expected: 0
        sorter.visitVarInsn(Opcodes.ALOAD, 1); // expected: 1
        sorter.visitVarInsn(Opcodes.ILOAD, 2); // expected: 2
        sorter.visitVarInsn(Opcodes.IINC, 2, 1); // expected: 2
        sorter.visitVarInsn(Opcodes.LLOAD, 3); // expected: 3
        sorter.visitVarInsn(Opcodes.LLOAD, 4); // expected: 4
        sorter.visitVarInsn(Opcodes.FLOAD, 5); // expected: 5
        sorter.visitVarInsn(Opcodes.FLOAD, 6); // expected: 6
        sorter.visitVarInsn(Opcodes.DLOAD, 7); // expected: 7
        sorter.visitVarInsn(Opcodes.DLOAD, 8); // expected: 8
        sorter.visitVarInsn(Opcodes.ALOAD, 9); // expected: 9

        sorter.visitIincInsn(2, 1); // expected: 2
        sorter.visitIincInsn(2, 2); // expected: 2

        sorter.visitVarInsn(Opcodes.ALOAD, 0); // expected: 0
        sorter.visitVarInsn(Opcodes.ALOAD, 1); // expected: 1
        sorter.visitVarInsn(intType.getOpcode(Opcodes.ISTORE), 2); // expected: 2
        sorter.visitVarInsn(intType.getOpcode(Opcodes.ISTORE), 2); // expected: 2
        sorter.visitVarInsn(longType.getOpcode(Opcodes.LSTORE), 3); // expected: 3
        sorter.visitVarInsn(longType.getOpcode(Opcodes.LSTORE), 3); // expected: 3
        sorter.visitVarInsn(floatType.getOpcode(Opcodes.FSTORE), 5); // expected: 5
        sorter.visitVarInsn(floatType.getOpcode(Opcodes.FSTORE), 6); // expected: 6
        sorter.visitVarInsn(doubleType.getOpcode(Opcodes.DSTORE), 7); // expected: 7
        sorter.visitVarInsn(doubleType.getOpcode(Opcodes.DSTORE), 8); // expected: 8
        sorter.visitVarInsn(objectType.getOpcode(Opcodes.ASTORE), 9); // expected: 9

        sorter.visitIincInsn(2, 1); // expected: 2
        sorter.visitIincInsn(2, 2); // expected: 2

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ClassReader classReader = new ClassReader(inputStream);
        ClassVisitor cv = mock(ClassVisitor.class);
        MethodVisitor mv = new MethodVisitor(Opcodes.ASM5, cv) {
            @Override
            public void visitCode() {
                super.visitCode();
            }

            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, LocalVariablesSorterTest.remap(var, Type.INT_TYPE, sorter));
            }

            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(LocalVariablesSorterTest.remap(var, Type.INT_TYPE, sorter), increment);
            }

            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                int newIndex = LocalVariablesSorterTest.remap(index, Type.getType(desc), sorter);
                super.visitLocalVariable(name, desc, signature, start, end, newIndex);
            }
        };
        Method method = new Method(new AnnotationDefault(), "test", "()V", null);
        ClassVisitor cvWrapper = new ClassVisitor(Opcodes.ASM5, cv) {
            @Override
            public void visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(access, desc, mv);
                when(cv.visitMethod(anyInt(), anyString(), anyString(), anyString(), anyArray(), anyInt()))
                        .thenReturn(localVariablesSorter);
                super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
        ClassVisitor cvWrapper2 = new ClassVisitor(Opcodes.ASM5, cvWrapper) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                assertArrayEquals(new int[]{0, 1, 2, 2, 3, 3, 5, 5, 7, 7, 9}, sorter.mapping);
                assertArrayEquals(new Object[]{null, null, Integer.TYPE, Integer.TYPE, Long.TYPE, Long.TYPE,
                        Float.TYPE, Float.TYPE, Double.TYPE, Double.TYPE, Object.class}, sorter.newLocals);
            }
        };
        classReader.accept(cvWrapper2, 0);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5, null) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                assertEquals(index, LocalVariablesSorterTest.remap(index, Type.INT_TYPE, sorter));
            }
        });

        Type intType = Type.INT_TYPE;

        sorter.visitLocalVariable("i", intType.getDescriptor(), null, new Label(0), new Label(1), 0);
        sorter.visitLocalVariable("j", intType.getDescriptor(), null, new Label(2), new Label(3), 1);
        sorter.visitLocalVariable("k", intType.getDescriptor(), null, new Label(4), new Label(5), 2);
        sorter.visitLocalVariable("l", longType.getDescriptor(), null, new Label(6), new Label(7), 3);
        sorter.visitLocalVariable("m", longType.getDescriptor(), null, new Label(8), new Label(9), 4);
        sorter.visitLocalVariable("n", floatType.getDescriptor(), null, new Label(10), new Label(11), 5);
        sorter.visitLocalVariable("o", floatType.getDescriptor(), null, new Label(12), new Label(13), 6);
        sorter.visitLocalVariable("p", doubleType.getDescriptor(), null, new Label(14), new Label(15), 7);
        sorter.visitLocalVariable("q", doubleType.getDescriptor(), null, new Label(16), new Label(17), 8);
        sorter.visitLocalVariable("r", "java/lang/String".getDescriptor(), null, new Label(18), new Label(19), 9);

        int[] expected = {0, 1, 2, 3, 3, 3, 5, 5, 5, 5, 9};
        assertEquals(Arrays.stream(expected).toArray(), sorter.mapping);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5, null) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, LocalVariablesSorterTest.remap(nLocal, Type.INT_TYPE, sorter), local, nStack, stack);
            }
        });

        int nLocal = 10;
        Object[] local = new Object[nLocal];
        Type[] types = new Type[nLocal];
        for (int i = 0; i < nLocal; ++i) {
            types[i] = i % 2 == 0 ? intType : longType;
            local[i] = i % 2 == 0 ? Opcodes.INTEGER : Opcodes.LONG;
        }

        sorter.visitFrame(Opcodes.FRAME_LOCALS, nLocal, local, 0, null);

        int[] expected = {0, 1, 1, 2, 2, 2, 3, 3, 3, 3};
        assertEquals(Arrays.stream(expected).toArray(), sorter.mapping);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5, null) {
            @Override
            public int newLocal(Type type) {
                return LocalVariablesSorterTest.remap(LocalVariablesSorterTest.newLocalMapping(type), type, sorter);
            }
        });

        Type intType = Type.INT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getType("java.lang.Object");

        assertEquals(1, sorter.newLocal(intType));
        assertEquals(2, sorter.newLocal(intType));
        assertEquals(3, sorter.newLocal(longType));
        assertEquals(4, sorter.newLocal(longType));
        assertEquals(5, sorter.newLocal(floatType));
        assertEquals(6, sorter.newLocal(floatType));
        assertEquals(7, sorter.newLocal(doubleType));
        assertEquals(8, sorter.newLocal(doubleType));
        assertEquals(9, sorter.newLocal(objectType));

        assertEquals(1, sorter.mapping[3]);
        assertEquals(2, sorter.mapping[4]);
        assertEquals(3, sorter.mapping[5]);
        assertEquals(4, sorter.mapping[6]);
        assertEquals(5, sorter.mapping[7]);
        assertEquals(6, sorter.mapping[8]);
        assertEquals(7, sorter.mapping[9]);
        assertEquals(8, sorter.mapping[10]);
        assertEquals(9, sorter.mapping[11]);
    }

    @Test
    public void testSetLocalTypeAndSetFrameLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5, null) {
            @Override
            public void setLocalType(int local, Type type) {
                assertEquals(type, LocalVariablesSorterTest.getTypeAt(sorter, local));
            }

            @Override
            public void setFrameLocal(int local, Object type) {
                assertEquals(type, LocalVariablesSorterTest.getFrameLocalAt(sorter, local));
            }
        });

        Type intType = Type.INT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getType("java.lang.Object");

        sorter.setLocalType(0, intType);
        sorter.setFrameLocal(0, Opcodes.INTEGER);
        sorter.setLocalType(1, longType);
        sorter.setFrameLocal(1, Opcodes.LONG);
        sorter.setLocalType(2, floatType);
        sorter.setFrameLocal(2, Opcodes.FLOAT);
        sorter.setLocalType(3, doubleType);
        sorter.setFrameLocal(3, Opcodes.DOUBLE);
        sorter.setLocalType(4, objectType);
        sorter.setFrameLocal(4, "java/lang/String".getInternalName());

        assertEquals(intType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(sorter)).get(0));
        assertEquals(intType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(1)));
        assertEquals(longType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(2)));
        assertEquals(longType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(3)));
        assertEquals(floatType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(4)));
        assertEquals(floatType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(5)));
        assertEquals(doubleType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(6)));
        assertEquals(doubleType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(7)));
        assertEquals(objectType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(8)));
        assertEquals(objectType, Type.getType(sorter.getClass().getDeclaredField("mapping").get(9)));

        assertEquals(Opcodes.INTEGER, Opcodes.getIntegerType(sorter.mapping[0]));
        assertEquals(Opcodes.LONG, Opcodes.getIntegerType(sorter.mapping[1]));
        assertEquals(Opcodes.LONG, Opcodes.getIntegerType(sorter.mapping[2]));
        assertEquals(Opcodes.FLOAT, Opcodes.getFloatType(sorter.mapping[3]));
        assertEquals(Opcodes.FLOAT, Opcodes.getFloatType(sorter.mapping[4]));
        assertEquals(Opcodes.DOUBLE, Opcodes.getDoubleType(sorter.mapping[5]));
        assertEquals(Opcodes.DOUBLE, Opcodes.getDoubleType(sorter.mapping[6]));
        assertEquals(objectType, sorter.mapping[7]);
        assertEquals(objectType, sorter.mapping[8]);
    }

    private static int remap(int var, Type type, LocalVariablesSorter sorter) {
        return sorter.remap(var, type);
    }

    private static int newLocalMapping(Type type) {
        return LocalVariablesSorter.newLocalMapping(type);
    }

    private static Object getTypeAt(LocalVariablesSorter sorter, int local) {
        return sorter.mapping[local + 1];
    }

    private static Object getFrameLocalAt(LocalVariablesSorter sorter, int local) {
        return sorter.newLocals[local];
    }
}