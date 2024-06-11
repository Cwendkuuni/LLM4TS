import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;

public class PropertyAndSetterInjectionTest {

    private static PropertyAndSetterInjection propertyAndSetterInjection;
    private static MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private static Object mockCandidate;
    private static Object injectMocksFieldOwner;
    private static Field injectMocksField;
    private static Set<Object> mockCandidates = new HashSet<>();

    @BeforeAll
    public static void setup() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
        mockCandidates.add(new Object() {});
        injectMocksFieldOwner = new Object();
        injectMocksField = Reflections.getField(injectMocksFieldOwner.getClass(), "injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(injectMocksFieldOwner, new Object());
    }

    @Test
    public void testProcessInjection_SetterInjection() {
        mockCandidate = new Object() {
            @Override
            public Object getMock() {
                return new MockitoMock();
            }
        };

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        Object mock = mockCandidate.getMock();
        assertEquals(mock, getFieldValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_FieldInjection() {
        mockCandidate = new Object() {
            @Override
            public Object getMock() {
                throw new UnsupportedOperationException("Should use field injection");
            }
        };

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        Object mock = mockCandidate;
        assertEquals(mock, getFieldValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void testProcessInjection_NoMockCandidate() {
        mockCandidates.clear();

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        assertEquals(null, getFieldValue(injectMocksField, injectMocksFieldOwner));
    }
}