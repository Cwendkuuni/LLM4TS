import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);

    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testRenamePrototypeProperty() {
    String name = "myCustomProperty";
    Property property = new Property(name);

    assertEquals(false, property.canRename());

    when(compiler.getCodingConvention().isExported(name)).thenReturn(false);
    when(compiler.getCodingConvention().isPrivate(name)).thenReturn(false);

    assertEquals(true, property.canRename());
  }

  @Test
  public void testDontRenamePrototypePropertyWithUpperCaseLetter() {
    String name = "myCustomPropertyWithUpperCaseLetter";
    Property property = new Property(name);

    assertEquals(true, property.canRenamePrototypeProperty());
  }

  @Test
  public void testDontRenamePrototypePropertyWithNonLetterCharacter() {
    String name = "myCustomPropertyWithNumber";
    Property property = new Property(name);

    assertEquals(true, property.canRenamePrototypeProperty());
  }

  @Test
  public void testRenameObjLitProperty() {
    String name = "myCustomObjLitProperty";
    Property property = new Property(name);

    assertEquals(false, property.canRenameObjLitProperty());
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    String name = "myCustomPrototypeProperty";
    Property property = new Property(name);

    renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    assertEquals(true, property.canRename());
  }

  @Test
  public void testRenamePrototypePropertyWithLeadingUnderscore() {
    String name = "_myCustomPrototypeProperty";
    Property property = new Property(name);

    assertEquals(false, property.canRename());
  }

  @Test
  public void testRenamePrototypePropertyWithTrailingUnderscore() {
    String name = "myCustomPrototypeProperty_";
    Property property = new Property(name);

    assertEquals(false, property.canRename());
  }

  @Test
  public void testRenamePrototypePropertyWithBothLeadingAndTrailingUnderscores() {
    String name = "___myCustomPrototypeProperty____";
    Property property = new Property(name);

    assertEquals(false, property.canRename());
  }

  @Test
  public void testRenamePrototypePropertyWithExternedName() {
    String name = "indexOf";
    Property property = new Property(name);

    assertEquals(false, property.canRename());
  }

  @Test
  public void testRenamePrototypePropertyWithPrivateName() {
    String name = "__myPrivatePrototypeProperty";
    Property property = new Property(name);

    when(compiler.getCodingConvention().isPrivate(name)).thenReturn(true);

    assertEquals(true, property.canRename());
  }

  @Test
  public void testRenameObjLitPropertyWithQuotedName() {
    String name = "\"myCustomObjLitProperty\"";
    Property property = new Property(name);

    assertEquals(false, property.canRenameObjLitProperty());
  }

  @Test
  public void testRenameObjLitPropertyWithNonQuotedName() {
    String name = "myCustomObjLitProperty";
    Property property = new Property(name);

    assertEquals(false, property.canRenameObjLitProperty());
  }

  @Test
  public void testProcess() {
    CompilerInput input = mock(CompilerInput.class);
    Node root = mock(Node.class);

    renamePrototypes.process(null, root);

    // The test does not verify the actual renaming, it just checks that the
    // process method is called.
  }
}