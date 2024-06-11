import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("function BaseClass() {}", null);
    FunctionType baseClassCtor = (FunctionType) compiler.getTypes().lookupType(
        "test.BaseClass");
    compiler.processString("function SubClass() {}", null);
    FunctionType subClassCtor = (FunctionType) compiler.getTypes().lookupType(
        "test.SubClass");
    SubclassType relationship = SubclassType.INHERITS;
    convention.applySubclassRelationship(baseClassCtor, subClassCtor, relationship);
    assertEquals("SubClass.prototype.constructor", subClassCtor,
        baseClassCtor.getPrototype().getConstructor());
    assertEquals("SubClass.prototype.superClass_", baseClassCtor,
        subClassCtor.getPrototype().getSuperclass());
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("function BaseClass() {}", null);
    compiler.processString("function SubClass() {}", null);
    Node subclassDefiningCall = Node.newCall(
        Node.newName("goog.inherits"), null,
        Node.newName("SubClass"), Node.newName("BaseClass"));
    SubclassRelationship relationship = convention.getclasssDefinedByCall(
        subclassDefiningCall);
    assertTrue(relationship != null);
    assertEquals("SubClass", relationship.getSubclass());
    assertEquals("BaseClass", relationship.getSuperclass());
    assertEquals(SubclassType.INHERITS, relationship.getType());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("goog.provide('MyNamespace.MyClass')", null);
    String className = convention.extractClassNameIfProvide(
        Node.newName("goog.provide"), null);
    assertTrue(className != null && className.equals("MyNamespace.MyClass"));
  }

  @Test
  public void testApplySingletonGetter() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("function MySingleton() {}", null);
    FunctionType mySingletonCtor = (FunctionType) compiler.getTypes().lookupType(
        "test.MySingleton");
    Node getterCall = Node.newCall(
        Node.newName("goog.addSingletonGetter"), null,
        Node.newName("MySingleton", "getInstance"), null);
    Node getter = getterCall.getNext();
    ObjectType getterType = (ObjectType) getter.getType();
    Node instanceProperty = getterType.getProperties().get("instance_");
    assertEquals("function", instanceProperty.getType().getName());
    assertEquals("test.MySingleton", instanceProperty.getType().getStaticType());
    assertEquals("test.MySingleton", getter.getPrototype().getStaticType());
    assertEquals("test.MySingleton", mySingletonCtor.getPrototype().getStaticType());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("function MyClass() {}", null);
    Node isDefCall = Node.newCall(
        Node.newName("goog.isDef"), null, Node.newName("MyClass.prototype.x"));
    assertTrue(convention.isPropertyTestFunction(isDefCall));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.setOptions(options);
    compiler.setOptions(options);
    compiler.addProcessingInstruction("source", "test");
    compiler.setProcessor(new com.google.javascript.jscomp.JssSourceFile(
        "test.js", "test.js", null));
    compiler.processString("function MyClass() { return {}; }", null);
    Node objectLiteral = Node.newObjectLiteral();
    objectLiteral.setProperty("x", Node.newNumber(1));
    Node castCall = Node.newCall(
        Node.newName("goog.reflect.object.getPrototypeOf"), null,
        Node.newName("MyClass"), Node.newName("MyClass.prototype"));
    Node cast = castCall.getNext();
    ObjectLiteralCast expectedCast = new ObjectLiteralCast(
        "goog.reflect.ObjectType", objectLiteral);
    ObjectLiteralCast actualCast = convention.getObjectLiteralCast(
        new NodeTraversal(compiler.getRootNode(), new NodeTraversal.Scope()),
        castCall);
    assertEquals(expectedCast, actualCast);
  }
}