import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(123) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                verify(mockMethodVisitor).visitVarInsn(opcode, eq(sorter.remap(var, Type.getType("I"))));
            }
        });

        Type intType = Type.getIntType();
        sorter.visitVarInsn(Opcodes.ILOAD, 1);
        sorter.mv.verifyNoMoreInterfaces();
    }
}