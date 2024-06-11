
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.*;
import static com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken<String> targetType = new TypeToken<String>() {};
        TypeAdapter<String> expectedAdapter = new StringAdapter(); // Assuming StringAdapter is a valid TypeAdapter
        TypeAdapter<String> returnedAdapter = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor()).create(gson, targetType);
        Assert.assertSame(expectedAdapter, returnedAdapter);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
        TypeAdapter<String> expectedAdapter = new StringAdapter();
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        Assert.assertSame(expectedAdapter, returnedAdapter);
    }

    @Test
    public void testGetTypeAdapterWithTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter(StringAdapterFactory.class);
        TypeAdapterFactory typeAdapterFactory = new StringAdapterFactory();
        TypeAdapter<String> expectedAdapter = typeAdapterFactory.create(gson, fieldType);
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        Assert.assertSame(expectedAdapter, returnedAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterWithInvalidTypeAdapter() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter(String.class);
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapterWithInvalidTypeAdapterFactory() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter(StringAdapterFactoryNotFound.class);
        TypeAdapterFactory typeAdapterFactory = new StringAdapterFactory();
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeAdapterWithNullGson() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, null, fieldType, annotation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeAdapterWithNullAnnotation() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<String> fieldType = new TypeToken<String>() {};
        JsonAdapter annotation = null;
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeAdapterWithNullTypeToken() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        JsonAdapter annotation = new JsonAdapter(StringAdapter.class);
        TypeToken<String> fieldType = null;
        TypeAdapter<String> returnedAdapter = getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }
}
