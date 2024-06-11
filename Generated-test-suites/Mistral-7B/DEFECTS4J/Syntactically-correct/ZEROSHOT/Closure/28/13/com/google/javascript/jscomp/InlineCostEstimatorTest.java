import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompiledSizeEstimator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class InlineCostEstimatorTest {

 private InlineCostEstimator estimator;
 private CompiledSizeEstimator mockSizeEstimator;

 @Before
 public void setup() {
 estimator = new InlineCostEstimator();
 mockSizeEstimator = mock(CompiledSizeEstimator.class);
 }

 @Test
 public void testGetCostOfEmptyRootNode() {
 // Given
 Node rootNode = Node.EMPTY;

 // When
 int cost = InlineCostEstimator.getCost(rootNode);

 // Then
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostOfComplexNode() {
 // Given
 Node rootNode = createNode("function test() {\n" +
 "var x = 1,\n" +
 "y = 2,\n" +
 "z;\n" +
 "}");

 // When
 int cost = InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);

 // Then
 ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
 verify(mockSizeEstimator).add(nodeArgumentCaptor.capture());

 Node capturedNode = nodeArgumentCaptor.getValue();
 assertEquals("test", capturedNode.getSource().getName());
 assertEquals(0, capturedNode.getSource().getBeginLine());
 assertEquals(3, capturedNode.getSource().getEndLine());
 assertEquals(12, cost);
 }

 @Test
 public void testGetCostOfComplexNodeWithThreshold() {
 // Given
 Node rootNode = createNode("function test() {\n" +
 "var x = 1,\n" +
 "y = 2,\n" +
 "z;\n" +
 "}");

 // When
 int cost = InlineCostEstimator.getCost(rootNode, 10);

 // Then
 ArgumentCaptor<Node> nodeArgumentCaptor = ArgumentCaptor.forClass(Node.class);
 verify(mockSizeEstimator, times(1)).add(nodeArgumentCaptor.capture());

 Node capturedNode = nodeArgumentCaptor.getValue();
 assertEquals("test", capturedNode.getSource().getName());
 assertEquals(0, capturedNode.getSource().getBeginLine());
 assertEquals(3, capturedNode.getSource().getEndLine());
 assertEquals(12, cost);

 // Then the rest of the nodes should not be processed
 verify(mockSizeEstimator, never()).add(any(Node.class));
 }

 private Node createNode(String source) {
 Node node = new Node(Node.ROOT, null, source);
 return node;
 }
}