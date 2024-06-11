import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null; // default value

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }

    @Test
    public void testGetClassIntrospector() {
        assertEquals(baseSettings.getClassIntrospector(), baseSettings._classIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(baseSettings.getAnnotationIntrospector(), baseSettings._annotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        assertEquals(baseSettings.getVisibilityChecker(), baseSettings._visibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        assertEquals(baseSettings.getPropertyNamingStrategy(), baseSettings._propertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        assertEquals(baseSettings.getTypeFactory(), baseSettings._typeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        assertEquals(baseSettings.getTypeResolverBuilder(), baseSettings._typeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        assertEquals(baseSettings.getDateFormat(), baseSettings._dateFormat);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        BaseSettings newBaseSettings = new BaseSettings(baseSettings._classIntrospector, baseSettings._annotationIntrospector, baseSettings._visibilityChecker,
                baseSettings._propertyNamingStrategy, baseSettings._typeFactory, baseSettings._typeResolverBuilder, baseSettings._dateFormat,
                handlerInstantiator, baseSettings._locale, baseSettings._timeZone, baseSettings._defaultBase64);

        assertEquals(handlerInstantiator, newBaseSettings.getHandlerInstantiator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        TimeZone nullTimeZone = null;
        BaseSettings newBaseSettings = baseSettings.with(nullTimeZone);
    }

    @Test
    public void testGetLocale() {
        Locale expectedLocale = new Locale("en", "US");
        BaseSettings newBaseSettings = new BaseSettings(baseSettings._classIntrospector, baseSettings._annotationIntrospector, baseSettings._visibilityChecker,
                baseSettings._propertyNamingStrategy, baseSettings._typeFactory, baseSettings._typeResolverBuilder, baseSettings._dateFormat,
                baseSettings._handlerInstantiator, expectedLocale, baseSettings._timeZone, baseSettings._defaultBase64);

        assertEquals(expectedLocale, newBaseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone expectedTimeZone = new TimeZone("America/New_York");
        BaseSettings newBaseSettings = new BaseSettings(baseSettings._classIntrospector, baseSettings._annotationIntrospector, baseSettings._visibilityChecker,
                baseSettings._propertyNamingStrategy, baseSettings._typeFactory, baseSettings._typeResolverBuilder, baseSettings._dateFormat,
                baseSettings._handlerInstantiator, baseSettings._locale, expectedTimeZone, baseSettings._defaultBase64);

        assertEquals(expectedTimeZone, newBaseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant expectedBase64Variant = new Base64Variant();
        BaseSettings newBaseSettings = new BaseSettings(baseSettings._classIntrospector, baseSettings._annotationIntrospector, baseSettings._visibilityChecker,
                baseSettings._propertyNamingStrategy, baseSettings._typeFactory, baseSettings._typeResolverBuilder, baseSettings._dateFormat,
                baseSettings._handlerInstantiator, baseSettings._locale, baseSettings._timeZone, expectedBase64Variant);

        assertEquals(expectedBase64Variant, newBaseSettings.getBase64Variant());
    }

    // Add more test cases for other methods here

}