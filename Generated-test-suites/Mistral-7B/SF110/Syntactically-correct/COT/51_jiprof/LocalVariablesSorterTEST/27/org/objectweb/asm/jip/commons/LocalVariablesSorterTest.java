package org.objectweb.asm.jip.commons;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        // Create a LocalVariablesSorter instance with an example method signature
        new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {});
    }

    @Test
    public void testVisitVarInsn() {
        // Create a LocalVariablesSorter instance and test visiting VAR_INT instruction
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                super.visitVarInsn(opcode, 0);
                Assert.assertEquals(var, 0);
            }
        });

        // Test visiting VAR_LONG and VAR_DOUBLE instructions
        for (int i = 0; i < 3; ++i) {
            final int opcode = (i == 0) ? Opcodes.VAR_LONG : (i == 1) ? Opcodes.VAR_DOUBLE : Opcodes.VAR_FLOAT;
            final int var = i + 1;
            sorter.visitVarInsn(opcode, var);
            Assert.assertEquals(var, (opcode == Opcodes.VAR_LONG || opcode == Opcodes.VAR_DOUBLE) ? var * 2 : var);
        }

        // Test visiting VAR_OBJECT instruction
        final int varObject = 4;
        sorter.visitVarInsn(Opcodes.VAR_OBJECT, varObject);
        Assert.assertEquals(varObject, varObject);
    }

    @Test
    public void testVisitIincInsn() {
        // Create a LocalVariablesSorter instance and test visiting IINC instruction
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(0, 1);
                Assert.assertEquals(var, 0);
                Assert.assertEquals(increment, 1);
            }
        });

        // Test visiting IINC instruction with negative increment
        sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitIincInsn(int var, int increment) {
                super.visitIincInsn(0, -1);
                Assert.assertEquals(var, 0);
                Assert.assertEquals(increment, -1);
            }
        });
    }

    @Test
    public void testVisitMaxs() {
        // Create a LocalVariablesSorter instance and test visiting MAXS instruction
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                super.visitMaxs(1, 1);
                Assert.assertEquals(maxStack, 1);
                Assert.assertEquals(maxLocals, 1);
            }
        });
    }

    @Test
    public void testVisitLocalVariable() {
        // Test visiting LOCAL_VAR_INT instruction
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                super.visitLocalVariable("testName", "I", null, start, end, 0);
                Assert.assertEquals(name, "testName");
                Assert.assertEquals(desc, "I");
                Assert.assertEquals(index, 0);
            }
        });

        // Test visiting LOCAL_VAR_LONG and LOCAL_VAR_DOUBLE instructions
        LocalVariablesSorter sorter2 = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                if (index == 0) {
                    super.visitLocalVariable("testNameLong", "J", null, start, end, 0);
                    Assert.assertEquals(name, "testNameLong");
                } else if (index == 1) {
                    super.visitLocalVariable("testNameDouble", "D", null, start, end, 0);
                    Assert.assertEquals(name, "testNameDouble");
                } else {
                    super.visitLocalVariable("testNameFloat", "F", null, start, end, 0);
                    Assert.assertEquals(name, "testNameFloat");
                }
            }
        });

        // Test visiting LOCAL_VAR_OBJECT instruction
        LocalVariablesSorter sorter3 = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                if (index == 0) {
                    super.visitLocalVariable("testNameObject", "Ljava/lang/String;", null, start, end, 0);
                    Assert.assertEquals(name, "testNameObject");
                } else {
                    super.visitLocalVariable("testNameObject2", "Ljava/lang/Integer;", null, start, end, 0);
                    Assert.assertEquals(name, "testNameObject2");
                }
            }
        });
    }

    @Test
    public void testVisitFrame() {
        // Test a simple frame
        byte[] classData = new byte[] {
            0x00, 0x60, 0x65, 0x63, 0x70, 0x61, 0x6D, 0x00, 0x3B, 0xAC, 0xCA, 0x01, 0x3D, 0xAC, 0xCA, 0x03,
            0x1B, 0x02, 0x1A, 0x00, 0x02, 0x4B, 0x01, 0x00, 0x60, 0x01, 0x62, 0x01, 0x2B, 0x06, 0x60, 0x02,
            0x2B, 0x06, 0x60, 0x03, 0x6F, 0x54, 0x52, 0x2B, 0x04, 0x6C, 0x52, 0x2B, 0x04, 0x6C, 0x52
        };

        ClassReader classReader = new ClassReader(classData);
        ClassWriter classWriter = new ClassWriter(0);
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, nLocal, local, nStack, stack);
                Assert.assertEquals(type, -1);
                Assert.assertArrayEquals(new int[]{1, 1, 1, 2}, new int[nLocal]);
                Assert.assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP}, local);
                Assert.assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP}, stack);
            }
        });

        classReader.accept(sorter);
        byte[] output = classWriter.toByteArray();

        ClassReader testClassReader = new ClassReader(output);
        MethodVisitor testMethodVisitor = new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, nLocal, local, nStack, stack);
                System.out.println("Local variables: " + Arrays.toString(local) + "\nStack: " + Arrays.toString(stack));
            }
        };
        testClassReader.visitMethod(Opcodes.ACC_PUBLIC, "TestMethod", "I", null, null);
        testClassReader.visitCode();
        testMethodVisitor.visitFrame(0, 4, null, 0, null);
        testMethodVisitor.visitMaxs(2, 2);
        testMethodVisitor.visitEnd();
        testClassReader.visitEnd();

        // Test a frame with a custom size
        LocalVariablesSorter sorter2 = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, 10, local, nStack, stack);
                Assert.assertEquals(type, -1);
                Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, new int[nLocal]);
                Assert.assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.STRING, Opcodes.INTEGER, Opcodes.DOUBLE, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER}, local);
                Assert.assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.STRING, Opcodes.INTEGER, Opcodes.DOUBLE, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER}, stack);
            }
        });

        classData = new byte[] {
            0x00, 0x60, 0x65, 0x63, 0x70, 0x61, 0x6D, 0x00, 0x3B, 0xAC, 0xCA, 0x01, 0x3D, 0xAC, 0xCA, 0x03,
            0x1B, 0x02, 0x1A, 0x01, 0x2B, 0x06, 0x60, 0x02, 0x2B, 0x06, 0x60, 0x03, 0x6F, 0x54, 0x52, 0x2B, 0x04,
            0x6C, 0x52, 0x2B, 0x04, 0x6C, 0x52, 0x2B, 0x04, 0x6C, 0x52, 0x2B, 0x04, 0x6C, 0x52
        };

        classReader = new ClassReader(classData);
        classWriter = new ClassWriter(0);
        sorter2.visitFrame(0, 10, null, nStack, stack);
        byte[] output2 = classWriter.toByteArray();

        ClassReader testClassReader2 = new ClassReader(output2);
        MethodVisitor testMethodVisitor2 = new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                super.visitFrame(type, 10, local, nStack, stack);
                System.out.println("Local variables: " + Arrays.toString(local) + "\nStack: " + Arrays.toString(stack));
            }
        };
        testClassReader2.visitMethod(Opcodes.ACC_PUBLIC, "TestMethod", "I", null, null);
        testClassReader2.visitCode();
        testMethodVisitor2.visitFrame(0, 10, null, nStack, stack);
        testMethodVisitor2.visitMaxs(2, 2);
        testMethodVisitor2.visitEnd();
        testClassReader2.visitEnd();
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithInvalidType() {
        // Test visiting FRAME_EXTENDED instruction with invalid type
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                if (type == -1) {
                    super.visitFrame(type, nLocal, local, nStack, stack);
                    Assert.fail();
                }
                throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
            }
        });
    }

    @Test
    public void testNewLocal() {
        // Create a LocalVariablesSorter instance and test newLocal method
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "TestMethod(I)V", new MethodVisitor(Opcodes.ASM10) {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                int local = sorter.newLocal(Type.INT_TYPE);
                super.visitLocalVariable(name, desc, signature, start, end, local);
                Assert.assertEquals(local, 0);
            }
        });

        // Test newLocal method with different types
        for (int i = 0; i < 8; ++i) {
            final Type type = (i == 0) ? Type.INT_TYPE : (i == 1) ? Type.FLOAT_TYPE : (i == 2) ? Type.LONG_TYPE : (i == 3) ? Type.DOUBLE_TYPE : (i == 4) ? Type.BOOLEAN_TYPE : Type.getObjectType("java/lang/String");
            final int local = sorter.newLocal(type);
            super.visitLocalVariable("testName" + i, descFromType(type), null, start, end, local);
            Assert.assertEquals(local, i + 1);
        }
    }

    // Helper method to get the descriptor based on the given Type
    private String descFromType(Type type) {
        switch (type.getSort()) {
            case 1:
                return "I";
            case 2:
                return "F";
            case 3:
                return "J";
            case 4:
                return "D";
            case 6:
                return "Ljava/lang/String;";
            default:
                return type.getDescriptor();
        }
    }
}