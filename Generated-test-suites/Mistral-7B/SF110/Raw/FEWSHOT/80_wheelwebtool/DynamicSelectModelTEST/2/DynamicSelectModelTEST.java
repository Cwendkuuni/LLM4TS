package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.*;

public class DynamicSelectModelTest {

    private DynamicSelectModel model;
    private ElExpression collectionMock;
    private ElExpression labelMock;
    private ElExpression valueMock;
    private ElExpression translatorMock;
    private Collection objects;
    private Object emptyObject;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        collectionMock = mock(ElExpression.class);
        labelMock = mock(ElExpression.class);
        valueMock = mock(ElExpression.class);
        translatorMock = mock(ElExpression.class);

        model = new DynamicSelectModel();
        model.setCollection(collectionMock);
        model.setLabel(labelMock);
        model.setValue(valueMock);
    }

    @Test
    public void testConstructorWithValidation() {
        model.validate();
        verify(collectionMock, times(1)).isValid();
        verify(labelMock, times(1)).isValid();
        verify(valueMock, times(1)).isValid();
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutCollection() {
        model = new DynamicSelectModel();
        model.setValue(valueMock);
        model.validate();
        verify(collectionMock, never()).isValid();
        throw new AssertionError("Validation did not throw WheelException");
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutLabel() {
        model = new DynamicSelectModel();
        model.setCollection(collectionMock);
        model.validate();
        verify(labelMock, never()).isValid();
        throw new AssertionError("Validation did not throw WheelException");
    }

    @Test(expected = WheelException.class)
    public void testConstructorWithoutValue() {
        model = new DynamicSelectModel();
        model.setCollection(collectionMock);
        model.setLabel(labelMock);
        model.validate();
        verify(valueMock, never()).isValid();
        throw new AssertionError("Validation did not throw WheelException");
    }

    @Test
    public void testGetOptionCount() {
        when(collectionMock.eval(any(), any())).thenReturn(new Object[]{new Object[]{"Option1", 1}, new Object[]{"Option2", 2}, new Object[]{"Option3", 3}});
        assertEquals(3, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        when(labelMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(valueMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(collectionMock.eval(any(), any())).thenReturn(new Object[]{new Object[]{"Option1", 1}, new Object[]{"Option2", 2}, new Object[]{"Option3", 3}});

        assertEquals("Option1", model.getLabel(0));
        assertEquals("Option2", model.getLabel(1));
        assertEquals("Option3", model.getLabel(2));
    }

    @Test
    public void testGetValue() {
        when(labelMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(valueMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(collectionMock.eval(any(), any())).thenReturn(new Object[]{new Object[]{"Option1", 1}, new Object[]{"Option2", 2}, new Object[]{"Option3", 3}});

        assertEquals("1", model.getValue(0));
        assertEquals("2", model.getValue(1));
        assertEquals("3", model.getValue(2));
    }

    @Test
    public void testTranslateValue() {
        when(labelMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(valueMock.eval(any(), any())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgArray();
                return (String) args[0];
            }
        });

        when(collectionMock.eval(any(), any())).thenReturn(new Object[]{new Object[]{"Option1", 1}, new Object[]{"Option2", 2}, new Object[]{"Option3", 3}});

        when(translatorMock.eval(any(), any())).thenReturn("TranslatedValue");

        model.setTranslator(translatorMock);

        assertEquals("TranslatedValue", model.translateValue("1"));
        assertEquals("TranslatedValue", model.translateValue("2"));
        assertEquals("TranslatedValue", model.translateValue("3"));
    }

    @Test
    public void testEmptyList() {
        when(collectionMock.eval(any(), any())).thenReturn(Collections.emptyList().toArray());
        assertNull(model.getLabel(0));
        assertNull(model.getValue(0));
    }

    @Test
    public void testEmptyObject() {
        model.empty(new Object());
        assertEquals(1, model.getOptionCount());
        assertNull(model.getLabel(0));
        assertNull(model.getValue(0));
    }
}