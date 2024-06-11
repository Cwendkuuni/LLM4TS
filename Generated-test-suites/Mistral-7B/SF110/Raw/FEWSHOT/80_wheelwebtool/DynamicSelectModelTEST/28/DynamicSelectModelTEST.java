package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel dynamicSelectModel;
    private Component component;
    private StandaloneComponent topLevelComponent;
    private ElExpression collection;
    private ElExpression label;
    private ElExpression value;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setUp() {
        this.component = Mockito.mock(Component.class);
        this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
        this.collection = Mockito.mock(ElExpression.class);
        this.label = Mockito.mock(ElExpression.class);
        this.value = Mockito.mock(ElExpression.class);
        this.objects = Arrays.asList(new Object[]{"Option 1", "Option 2", "Option 3"});
        this.emptyObject = Mockito.mock(Object.class);

        when(this.collection.eval(this.topLevelComponent, this.component)).thenAnswer(new Answer<Collection>() {
            @Override
            public Collection answer(InvocationOnMock invocation) throws Throwable {
                return (Collection)invocation.getArgument(0);
            }
        });

        when(this.label.eval(anyObject(), anyObject())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0).toString();
            }
        });

        when(this.value.eval(anyObject(), anyObject())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });

        this.dynamicSelectModel = new DynamicSelectModel();
        this.dynamicSelectModel.collection(this.collection);
        this.dynamicSelectModel.label(this.label);
        this.dynamicSelectModel.value(this.value);
        this.dynamicSelectModel.setComponent(this.component);
        this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponent);
        this.dynamicSelectModel.setObjects(this.objects);
        this.dynamicSelectModel.empty(this.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        assertEquals(this.objects.size(), this.dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        ArgumentCaptor<Object> indexCaptor = ArgumentCaptor.forClass(Object.class);
        String label;

        try {
            label = this.dynamicSelectModel.getLabel(0);
        } catch (IllegalArgumentException e) {
            label = e.getMessage();
        }

        assertEquals("Option 1", label);

        verify(this.label).eval(eq(this.objects.toArray()[0]), eq(this.component));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLabelWithInvalidIndex() {
        this.dynamicSelectModel.getLabel(3);
    }

    @Test
    public void testGetValue() {
        ArgumentCaptor<Object> indexCaptor = ArgumentCaptor.forClass(Object.class);
        Object value;

        try {
            value = this.dynamicSelectModel.getValue(0);
        } catch (IllegalArgumentException e) {
            value = e.getMessage();
        }

        assertEquals("Option 1", value);

        verify(this.value).eval(eq(this.objects.toArray()[0]), eq(this.component));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithInvalidIndex() {
        this.dynamicSelectModel.getValue(3);
    }

    @Test
    public void testTranslateValue() {
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        Object translatedValue = this.dynamicSelectModel.translateValue("Option 1");
        assertEquals("Option 1", translatedValue);

        verify(this.translator, never()).eval(anyObject(), anyObject());

        this.dynamicSelectModel.translator("translator");

        translatedValue = this.dynamicSelectModel.translateValue("Option 1");
        verify(this.translator).eval(eq(this.objects.toArray()[0]), eq(this.component));

        translatedValue = this.dynamicSelectModel.translateValue("Option 2");
        verify(this.translator).eval(eq(this.objects.toArray()[1]), eq(this.component));
    }

    @Test
    public void testTranslateValueWithNullValue() {
        assertNull(this.dynamicSelectModel.translateValue(null));

        verify(this.translator, never()).eval(anyObject(), anyObject());
    }

    @Test
    public void testGetObjects() {
        assertEquals(this.objects, this.dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetComponent() {
        assertEquals(this.component, this.dynamicSelectModel.getComponent());
    }

    @Test
    public void testSetComponent() {
        Component newComponent = Mockito.mock(Component.class);

        this.dynamicSelectModel.setComponent(newComponent);

        verify(this.component).setParent(eq(newComponent));
    }

    @Test
    public void testGetTopLevelComponent() {
        assertEquals(this.topLevelComponent, this.dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent newTopLevelComponent = Mockito.mock(StandaloneComponent.class);

        this.dynamicSelectModel.setTopLevelComponent(newTopLevelComponent);

        verify(this.topLevelComponent).setParent(eq(newTopLevelComponent));
    }
}