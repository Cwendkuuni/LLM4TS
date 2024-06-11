package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

    @Test
    public void testDynamicSelectModel() {
        // Given
        Component component = Mockito.mock(Component.class);
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        ElExpression collection = new ElExpression(" Arrays.asList(1, 2, 3, 4, 5) ");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        // When
        dynamicSelectModel = dynamicSelectModel.collection(collection.getExpression())
                .label(label.getExpression())
                .value(value.getExpression());

        // Then
        assertEquals(5, dynamicSelectModel.getOptionCount());

        for (int i = 0; i < dynamicSelectModel.getOptionCount(); i++) {
            String labelValue = dynamicSelectModel.getLabel(i);
            String valueValue = dynamicSelectModel.getValue(i);

            assertEquals("" + (i + 1), labelValue);
            assertEquals("" + i, valueValue);
        }
    }

    @Test(expected = WheelException.class)
    public void testInvalidDynamicSelectModel() {
        // Given
        Component component = Mockito.mock(Component.class);
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        ElExpression collection = new ElExpression("null");
        ElExpression label = null;
        ElExpression value = new ElExpression("ordinal()");
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        // When
        dynamicSelectModel = dynamicSelectModel.collection(collection.getExpression())
                .label(label)
                .value(value.getExpression());

        // Then - expected exception
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testEmptyDynamicSelectModel() {
        // Given
        Component component = Mockito.mock(Component.class);
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        ElExpression collection = new ElExpression("Collections.emptyList()");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        // When
        dynamicSelectModel = dynamicSelectModel.collection(collection.getExpression())
                .label(label.getExpression())
                .value(value.getExpression());

        // Then - expected exception
        dynamicSelectModel.translateValue("0");
    }

    @Test
    public void testTranslateValue() {
        // Given
        Component component = Mockito.mock(Component.class);
        StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
        ElExpression collection = new ElExpression(" Arrays.asList(new MyObject(\"A\"), new MyObject(\"B\"), new MyObject(\"C\")) ");
        ElExpression label = new ElExpression("toString()");
        ElExpression value = new ElExpression("ordinal()");
        ElExpression translator = new ElExpression("\"Translated_\" + toString()");
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        // When
        dynamicSelectModel = dynamicSelectModel.collection(collection.getExpression())
                .label(label.getExpression())
                .value(value.getExpression())
                .translator(translator.getExpression());

        Object translatedObject = dynamicSelectModel.translateValue("1");

        // Then
        assertEquals("Translated_B", translatedObject.toString());
    }

    private static class MyObject {
        private final String name;

        public MyObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}