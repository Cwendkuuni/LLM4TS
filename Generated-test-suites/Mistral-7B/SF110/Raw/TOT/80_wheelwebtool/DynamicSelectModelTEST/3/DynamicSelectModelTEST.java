package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.*;
import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component componentMock;
 private ElExpression labelMock;
 private ElExpression valueMock;
 private ElExpression collectionMock;
 private ElExpression translatorMock;
 private Collection objectsMock;

 @Before
 public void setUp() {
 this.componentMock = mock(Component.class);
 this.labelMock = mock(ElExpression.class);
 this.valueMock = mock(ElExpression.class);
 this.collectionMock = mock(ElExpression.class);
 this.translatorMock = mock(ElExpression.class);
 this.objectsMock = Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"}).subList(0, 3);

 this.model = new DynamicSelectModel()
 .collection("['Option 1', 'Option 2', 'Option 3']")
 .label(new ElExpression("toString()"))
 .value(new ElExpression("ordinal()"));

 when(this.collectionMock.eval(this.componentMock, this.componentMock)).thenReturn(this.objectsMock);
 when(this.labelMock.eval(anyObject(), any())).thenAnswer(i -> i.toString());
 when(this.valueMock.eval(anyObject(), any())).thenAnswer(i -> i.getClass().getName() + ":" + i.hashCode());
 }

 @Test
 public void testGetOptionCount() {
 assertEquals(3, this.model.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 assertEquals("Option 1", this.model.getLabel(0));
 }

 @Test
 public void testGetValue() {
 assertEquals("Class0:1572522144", this.model.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollectionEvaluation() {
 this.collectionMock.errorMessage("Could not evaluate expression");
 this.model = new DynamicSelectModel()
 .collection(this.collectionMock);
 this.model.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testNullCollection() {
 this.collectionMock.when(this.collectionMock.eval(this.componentMock, this.componentMock)).thenReturn(null);
 this.model = new DynamicSelectModel()
 .collection(this.collectionMock);
 this.model.getObjects();
 }

 @Test
 public void testTranslateValue() {
 assertEquals("Option 1", this.model.translateValue("Option 1"));
 }

 @Test
 public void testTranslateValueNull() {
 assertNull(this.model.translateValue("InvalidValue"));
 }

 @After
 public void tearDown() {
 reset(this.componentMock, this.labelMock, this.valueMock, this.collectionMock, this.translatorMock);
 }
}