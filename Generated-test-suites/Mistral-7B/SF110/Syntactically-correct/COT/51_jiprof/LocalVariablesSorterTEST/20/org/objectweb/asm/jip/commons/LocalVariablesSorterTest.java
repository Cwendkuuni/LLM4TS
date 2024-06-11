import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});
        assertEquals(40, sorter.mapping.length);
        assertEquals(20, sorter.newLocals.length);
        assertEquals(1, sorter.nextLocal);
        assertEquals(0, sorter.firstLocal);
        assertFalse(sorter.changed);
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        // Test int variable
        sorter.visitVarInsn(Opcodes.ISTORE, 1);
        assertEquals(1, sorter.remap(1, Type.INT_TYPE));

        // Test float variable
        sorter.visitVarInsn(Opcodes.FSTORE, 2);
        assertEquals(2, sorter.remap(2, Type.FLOAT_TYPE));

        // Test long variable
        sorter.visitVarInsn(Opcodes.LSTORE, 3);
        assertEquals(3, sorter.remap(3, Type.LONG_TYPE));

        // Test double variable
        sorter.visitVarInsn(Opcodes.DSTORE, 4);
        assertEquals(4, sorter.remap(4, Type.DOUBLE_TYPE));

        // Test object variable
        sorter.visitVarInsn(Opcodes.ASTORE, 5);
        assertEquals(5, sorter.remap(5, Type.OBJECT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        // Test int local variable
        sorter.visitIincInsn(1, 1);
        assertEquals(1, sorter.remap(1, Type.INT_TYPE));

        // Test int local variable increment
        sorter.visitIincInsn(1, 2);
        assertEquals(1, sorter.remap(1, Type.INT_TYPE));
        assertEquals(2, sorter.mapping[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithUnchangedState() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        Object[] oldLocals = new Object[sorter.newLocals.length];
        System.arraycopy(sorter.newLocals, 0, oldLocals, 0, oldLocals.length);

        sorter.visitFrame(-1, 10, null, 10, null);
    }

    @Test
    public void testVisitFrameWithChangedState() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        Object[] oldLocals = new Object[sorter.newLocals.length];
        System.arraycopy(sorter.newLocals, 0, oldLocals, 0, oldLocals.length);

        sorter.visitFrame(-1, 10, null, 10, null);
        assertTrue(sorter.changed);

        sorter.mv.visitFrame(-1, 10, oldLocals, 10, null);
        assertFalse(sorter.changed);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        Type intType = Type.INT_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = Type.getObjectType("java/lang/String");

        int local1 = sorter.newLocal(intType);
        int local2 = sorter.newLocal(intType);
        int local3 = sorter.newLocal(intType);
        int local4 = sorter.newLocal(floatType);
        int local5 = sorter.newLocal(longType);
        int local6 = sorter.newLocal(doubleType);
        int local7 = sorter.newLocal(objectType);

        int[] expected = {1, 2, 3, 4, 5, 6, 7};
        assertArrayEquals(expected, sorter.mapping);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewLocalWithInvalidType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0, null) {});

        Type invalidType = Type.getDescriptor("java/lang/Integer"); // this type doesn't exist
        int local = sorter.newLocal(invalidType);
    }
}