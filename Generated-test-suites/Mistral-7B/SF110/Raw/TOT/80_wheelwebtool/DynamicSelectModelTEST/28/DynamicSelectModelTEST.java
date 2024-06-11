package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

import wheel.*;
import wheel.components.*;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest
{
    private DynamicSelectModel dynamicSelectModel;
    private ElExpression collectionMock;
    private ElExpression labelMock;
    private ElExpression valueMock;
    private Component componentMock;
    private StandaloneComponent topLevelComponentMock;

    @Before
    public void setUp()
    {
        this.collectionMock = mock(ElExpression.class);
        this.labelMock = mock(ElExpression.class);
        this.valueMock = mock(ElExpression.class);
        this.componentMock = mock(Component.class);
        this.topLevelComponentMock = mock(StandaloneComponent.class);

        this.dynamicSelectModel = new DynamicSelectModel();
        this.dynamicSelectModel.collection(this.collectionMock);
        this.dynamicSelectModel.label(this.labelMock);
        this.dynamicSelectModel.value(this.valueMock);
        this.dynamicSelectModel.setComponent(this.componentMock);
        this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponentMock);
    }

    @Test
    public void testGetOptionCount_WhenCollectionIsNotEmpty_ThenReturnsSizeOfCollection()
    {
        // Given: Collection is not empty
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"}));

        // When: Get option count
        int optionCount = this.dynamicSelectModel.getOptionCount();

        // Then: Option count is equal to the size of the collection
        assertEquals("Option count is not equal to the size of the collection.", 3, optionCount);
    }

    @Test
    public void testGetLabel_WhenIndexIsValid_ThenReturnsValidLabel()
    {
        // Given: Collection is not empty and label expression returns a valid label
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"}));
        when(this.labelMock.eval(anyObject(), anyObject())).thenReturn("Label 1");

        // When: Get label for index 0
        String label = this.dynamicSelectModel.getLabel(0);

        // Then: Label is equal to "Label 1"
        assertEquals("Label is not equal to 'Label 1'.", "Label 1", label);
    }

    @Test(expected = WheelException.class)
    public void testGetLabel_WhenIndexIsInvalid_ThenThrowsWheelException()
    {
        // Given: Collection is not empty
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"}));

        // When: Get label for invalid index
        this.dynamicSelectModel.getLabel(3);

        // Then: Throws WheelException
    }

    @Test
    public void testGetValue_WhenIndexIsValid_ThenReturnsValidValue()
    {
        // Given: Collection is not empty and value expression returns a valid value
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1: Value 1", "Option 2: Value 2", "Option 3: Value 3"}));
        when(this.valueMock.eval(anyObject(), anyObject())).thenReturn("Value 2");

        // When: Get value for index 1
        String value = this.dynamicSelectModel.getValue(1);

        // Then: Value is equal to "Value 2"
        assertEquals("Value is not equal to 'Value 2'.", "Value 2", value);
    }

    @Test(expected = WheelException.class)
    public void testGetValue_WhenIndexIsInvalid_ThenThrowsWheelException()
    {
        // Given: Collection is not empty
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"}));

        // When: Get value for invalid index
        this.dynamicSelectModel.getValue(3);

        // Then: Throws WheelException
    }

    @Test
    public void testTranslateValue_WhenValidValueIsPassed_ThenReturnsCorrespondingObject()
    {
        // Given: Collection is not empty, value expression returns a valid value, and translator expression returns a valid object
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1: Value 1", "Option 2: Value 2", "Option 3: Value 3"}));
        when(this.valueMock.eval(anyObject(), anyObject())).thenReturn("Value 2");
        when(this.translatorMock.eval(anyObject(), anyObject())).thenReturn(new Object(){});

        // When: Translate value "Value 2"
        Object translatedValue = this.dynamicSelectModel.translateValue("Value 2");

        // Then: Translated value is equal to the object returned by the translator expression
        assertEquals("Translated value is not equal to the object returned by the translator expression.", new Object(){}, translatedValue);
    }

    @Test(expected = WheelException.class)
    public void testTranslateValue_WhenInvalidValueIsPassed_ThenThrowsWheelException()
    {
        // Given: Collection is not empty
        when(this.collectionMock.eval(this.topLevelComponentMock, this.componentMock)).thenReturn(Arrays.asList(new Object[]{"Option 1: Value 1", "Option 2: Value 2", "Option 3: Value 3"}));

        // When: Translate value "Invalid Value"
        this.dynamicSelectModel.translateValue("Invalid Value");

        // Then: Throws WheelException
    }
}