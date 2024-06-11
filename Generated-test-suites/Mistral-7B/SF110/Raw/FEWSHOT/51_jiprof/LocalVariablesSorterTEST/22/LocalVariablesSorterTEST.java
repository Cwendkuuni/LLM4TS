package org.apache.commons.cli.jip.commons;

import org.junit.After;
import org.junit.Before;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

    private static final String TEST_CLASS = "TestLocalVariablesSorter";
    private static final String TEST_METHOD = "testMethod";
    private byte[] originalBytecode;
    private byte[] sortedBytecode;
    private Class<?> originalClass;
    private Class<?> sortedClass;

    @Before
    public void setUp() throws Exception {
        // Generate original bytecode
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv = cw.visitMethod(
                0x0020, TEST_METHOD, "(I)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitIntInsn(BIPUSH, 5);
        mv.visitVarInsn(25, 1);
        mv.visitIntInsn(BIPUSH, 10);
        mv.visitIntInsn(ISUB);
        mv.visitVarInsn(25, 0);
        mv.visitIntInsn(IADD, 3);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
        cw.visitEnd();
        originalBytecode = cw.toByteArray();
        originalClass = new ClassReader(new ByteArrayInputStream(originalBytecode)).parse0();

        // Generate bytecode to test LocalVariablesSorter
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        mv = cw.visitMethod(
                0x0020, TEST_METHOD, "(I)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", mv);
        mv.visitVarInsn(25, 1);
        mv.visitIntInsn(BIPUSH, 5);
        sorter.visitVarInsn(sorter.visitVarInsn(25, 1), 3);
        mv.visitIntInsn(BIPUSH, 10);
        sorter.visitVarInsn(sorter.visitVarInsn(25, 0), 3);
        mv.visitIntInsn(ISUB);
        mv.visitVarInsn(sorter.visitVarInsn(25, 0), 0);
        mv.visitIntInsn(IADD, 3);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
        cw.visitEnd();
        sortedBytecode = cw.toByteArray();
        sortedClass = new ClassReader(new ByteArrayInputStream(sortedBytecode)).parse0();
    }

    @After
    public void tearDown() throws Exception {
        // Do nothing
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        int var1 = 4;
        int var2 = 5;
        int opcode = Opcodes.IADD;
        Type type = Type.INT_TYPE;

        sorter.visitVarInsn(opcode, var1);
        int expected = (var1 - this.firstLocal) + 1;
        assertEquals(expected, sorter.remap(var1, type));

        sorter.visitVarInsn(opcode, var2);
        expected += type.getSize();
        assertEquals(expected, sorter.remap(var2, type));

        sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        var1 = 1;
        var2 = 6;
        opcode = Opcodes.LDC;
        type = Type.LONG_TYPE;

        sorter.visitVarInsn(opcode, var1);
        assertEquals(var1, sorter.remap(var1, type));

        sorter.visitVarInsn(opcode, var2);
        assertEquals(var2, sorter.remap(var2, type));
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        int var = 4;
        int increment = 3;

        sorter.visitIincInsn(var, increment);
        int expected = (var - this.firstLocal) + 1;
        assertEquals(expected, sorter.remap(var, Type.INT_TYPE));

        sorter.visitIincInsn(var, increment);
        assertEquals(expected + increment, sorter.remap(var, Type.INT_TYPE));
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        int maxLocals = 5;

        sorter.visitMaxs(3, maxLocals);
        int expected = maxLocals;
        assertEquals(expected, sorter.nextLocal);

        sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        maxLocals = 10;

        sorter.visitMaxs(5, maxLocals);
        expected += 5;
        assertEquals(expected, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        String name = "name";
        String desc = "Ljava/lang/String;";
        String signature = "()V";
        Label start = new Label();
        Label end = new Label();
        int index = 2;

        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        assertEquals(index, sorter.remap(index, Type.getType(desc)));

        sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        name = "name";
        desc = "Ljava/lang/String;";
        signature = "([BI)V";
        start = new Label();
        end = new Label();
        index = 3;

        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        assertEquals(index, sorter.remap(index, Type.getType(desc)));

        sorter.visitLocalVariable(name, desc, signature, start, end, index + 1);
        assertEquals(index + 1, sorter.remap(index + 1, Type.getType(desc)));
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        Object[] local = new Object[2];
        local[0] = Opcodes.INTEGER;
        local[1] = Opcodes.LONG;

        sorter.visitFrame(0, 1, new Object[]{Type.INT_TYPE}, 1, new Object[]{Opcodes.INTEGER});
        assertEquals(1, sorter.nextLocal);
        assertNull(sorter.newLocals);

        sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        sorter.changed = true;
        local = new Object[3];
        local[0] = Opcodes.INTEGER;
        local[1] = Opcodes.LONG;
        local[2] = Opcodes.TOP;

        sorter.visitFrame(0, 1, new Object[]{Type.INT_TYPE}, 1, new Object[]{Opcodes.INTEGER});
        sorter.visitFrame(Opcodes.ABORT, 3, local, 1, local);
        int expected = 1;
        assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.LONG, Opcodes.TOP}, sorter.newLocals);
        assertEquals(expected, sorter.nextLocal);

        sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        local = new Object[2];
        local[0] = Opcodes.INTEGER;
        local[1] = Opcodes.LONG;

        sorter.visitFrame(0, 1, new Object[]{Type.INT_TYPE}, 1, new Object[]{Opcodes.INTEGER});
        sorter.visitFrame(Opcodes.ABORT, 3, local, 1, local);
        expected = 1;
        assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.LONG}, sorter.newLocals);
        assertEquals(expected, sorter.nextLocal);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, TEST_METHOD + "([BI)V", new MethodVisitor(0, null));
        Type type = Type.INT_TYPE;

        int local = sorter.newLocal(type);
        assertEquals(this.firstLocal, local);
        assertEquals(type, sorter.mapping[local]);

        local = sorter.newLocal(Type.LONG_TYPE);
        assertEquals(this.firstLocal + 1, local);
        assertEquals(Type.LONG_TYPE, sorter.mapping[local]);

        local = sorter.newLocal(Type.FLOAT_TYPE);
        assertEquals(this.firstLocal + 2, local);
        assertEquals(Type.FLOAT_TYPE, sorter.mapping[local]);

        local = sorter.newLocal(Type.DOUBLE_TYPE);
        assertEquals(this.firstLocal + 3, local);
        assertEquals(Type.DOUBLE_TYPE, sorter.mapping[local]);

        local = sorter.newLocal(Type.getType("java/lang/String"));
        assertEquals(this.firstLocal + 4, local);
        assertEquals(Type.getType("java/lang/String"), sorter.mapping[local]);
    }
}