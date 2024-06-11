import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() throws Exception {
        ClassReader cr = new ClassReader("TestClass.class");
        MethodVisitor mv = new JavaClassMethodVisitor(cr.getClassReader(), "TestClass", "testMethod", null, null);
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", mv);

        // Test different opcodes
        sorter.visitVarInsn(Opcodes.ILOAD, 0);
        sorter.visitVarInsn(Opcodes.LLOAD, 1);
        sorter.visitVarInsn(Opcodes.FLOAD, 2);
        sorter.visitVarInsn(Opcodes.ALOAD, 3);

        int expected[] = {0, 1, 2, 3};
        int actual[] = sorter.mapping;
        assertEquals(expected, actual);

        // Test non-supported opcode
        sorter.visitVarInsn(Opcodes.BIPUSH, 0);
        expected = new int[]{0, 1, 2, 3, 5};
        actual = sorter.mapping;
        assertEquals(expected, actual);
    }
}