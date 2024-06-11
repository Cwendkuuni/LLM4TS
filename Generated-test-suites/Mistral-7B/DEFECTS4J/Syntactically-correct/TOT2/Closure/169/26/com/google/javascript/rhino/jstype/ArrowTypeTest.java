import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    // Given
    Node parameters = Node.createList(Node.createName("int"), Node.createName("String"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, getNativeType(int.class), false);

    // When
    ArrowType arrowTypeExpected = new ArrowType(new JSTypeRegistry(), parameters, getNativeType(int.class), false);

    // Then
    assertEquals(arrowTypeExpected, arrowType);
  }

  @Test
  public void testArrowTypeConstructionWithNullParameters() {
    // Given
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, getNativeType(int.class), false);

    // Then
    assertNull(arrowType.parameters);
  }

  @Test
  public void testArrowTypeConstructionWithReturnTypeInferred() {
    // Given
    Node parameters = Node.createList(Node.createName("int"), Node.createName("String"));
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, null, true);

    // Then
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeConstructionWithInvalidParameters() {
    // Given
    Node parameters = Node.createList(Node.createName("int"), Node.createName("int"), Node.createName("String"));

    // When
    new ArrowType(new JSTypeRegistry(), parameters, getNativeType(int.class), false);

    // Then: expectation is that an IllegalArgumentException is thrown
  }

  // Add more test cases for other methods
}