import org.junit.Test;
import org.junit.Assert.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @Before
 public void setup() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitMethodEnd() {}
 });
 }

 @Test
 public void testConstructor() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitMethodEnd() {}
 });
 assertNotNull(sorter);
 }

 @Test
 public void testVisitVarInsn() {
 sorter.visitVarInsn(Opcodes.ILOAD, 0);
 sorter.visitVarInsn(Opcodes.ALOAD, 0);
 sorter.visitVarInsn(Opcodes.LLOAD, 0);
 sorter.visitVarInsn(Opcodes.FLOAD, 0);
 sorter.visitVarInsn(Opcodes.DLOAD, 0);
 sorter.visitVarInsn(Opcodes.ASTORE, 0);
 sorter.visitVarInsn(Opcodes.LDC, new Type("java/lang/String"));
 Type type = Type.getObjectType("java/lang/String");
 sorter.visitVarInsn(Opcodes.ALOAD, 0);
 sorter.visitVarInsn(Opcodes.ALOAD, 1);
 sorter.visitVarInsn(Opcodes.ALOAD, 2);
 sorter.visitVarInsn(Opcodes.ALOAD, 3);
 int var = 10;
 sorter.visitVarInsn(Opcodes.ILOAD, var);
 Type intType = Type.INT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type objType = Type.getObjectType("java/lang/Object");
 Type[] args = new Type[] { intType, longType, floatType, doubleType, objType };
 Type methodDesc = Type.getMethodType("testVisitVarInsn:([Lorg/objectweb/asm/Type;)V", args);
 MethodVisitor mv = new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitMethodEnd() {}
 };
 LocalVariablesSorter lvs = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, methodDesc, mv);
 int expectedVarIndex = 0;
 for (int i = 0; i < args.length; i++) {
 int size = args[i].getSize();
 expectedVarIndex += size;
 }
 for (int i = 0; i < args.length; i++) {
 int opcode = 0;
 switch (args[i].getSort()) {
 case 1:
 case 2:
 case 3:
 case 4:
 case 5:
 opcode = Opcodes.ILOAD;
 break;
 case 6:
 opcode = Opcodes.FLOAD;
 break;
 case 7:
 opcode = Opcodes.LLOAD;
 break;
 case 8:
 opcode = Opcodes.DLOAD;
 break;
 case 9:
 opcode = Opcodes.ALOAD;
 break;
 default:
 opcode = Opcodes.ALOAD;
 }
 lvs.visitVarInsn(opcode, expectedVarIndex);
 int actualVarIndex = lvs.remap(i, args[i]);
 assertEquals("Unexpected variable index for argument " + i, expectedVarIndex, actualVarIndex);
 }
 }

 @Test
 public void testVisitIincInsn() {
 sorter.visitIincInsn(2, 1);
 sorter.visitIincInsn(5, 2);
 }

 @Test
 public void testVisitMaxs() {
 sorter.visitMaxs(10, 20);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "();";
 Label start = new Label();
 Label end = new Label();
 int index = 1;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithNoChangedFlag() {
 sorter.visitFrame(-1, 1, new Object[] { Type.INT_TYPE }, 0, new Object[] {});
 }

 @Test
 public void testVisitFrameWithChangedFlag() {
 Object[] oldLocals = new Object[] { Opcodes.INTEGER, Opcodes.INTEGER };
 Object[] newLocals = new Object[3];
 sorter.visitFrame(-1, 2, oldLocals, 10, new Object[] { Opcodes.TOP, Opcodes.TOP, Opcodes.TOP });
 sorter.visitFrame(0, 3, newLocals, 10, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP });
 assertEquals(2, newLocals.length);
 assertTrue(Arrays.equals(new Object[] { Opcodes.INTEGER, Opcodes.INTEGER }, oldLocals));
 }

 @Test
 public void testNewLocal() {
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type objType = Type.getObjectType("java/lang/String");
 for (Type type : new Type[] { intType, floatType, longType, doubleType, objType }) {
 int local = sorter.newLocal(type);
 assertEquals("Unexpected local variable index for type " + type, nextLocal, local);
 nextLocal += type.getSize();
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 sorter.newLocal(Type.getType("java/lang/Object:(Ljava/lang/String;)V"));
 }
}