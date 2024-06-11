import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {
  private SourceFile sourceFile;
  private ClosureCodingConvention codingConvention;

  @Before
  public void setup() {
    codingConvention = new ClosureCodingConvention();
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType(new ObjectType("Parent"),
        ImmutableList.<String>of("superClass_", "constructor"));
    FunctionType childCtor = new FunctionType(new ObjectType("Child"),
        ImmutableList.<String>of("superClass_", "constructor"));
    SubclassType type = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    ObjectType parentPrototype = parentCtor.getPrototype();
    ObjectType childPrototype = childCtor.getPrototype();
    assertEquals(parentCtor, childPrototype.getConstructor());
    assertEquals(parentPrototype, childPrototype.getSuperClass());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node("goog.provide('X')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node("goog.require('X')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String functionName = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = new Node("a.b.c.prototype");
    assertTrue(codingConvention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void testGetClassNameIfGoogProvide() {
    Node node = new Node("goog.provide('X')");
    String className = codingConvention.extractClassNameIfGoog(node, null,
        "goog.provide");
    assertEquals("X", className);
  }

  @Test
  public void testGetClassNameIfGoogRequire() {
    Node node = new Node("goog.require('X')");
    String className = codingConvention.extractClassNameIfGoog(node, null,
        "goog.require");
    assertEquals("X", className);
  }
}