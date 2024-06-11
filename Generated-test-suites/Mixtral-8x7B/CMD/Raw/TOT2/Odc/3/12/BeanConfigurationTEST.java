import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);

        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotNull(encodedPassword);

        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        assertEquals(customOutputSerializer, objectMapper.getSerializationConfig().getSerializerProvider().getNullValueSerializer());

        Internationalizable internationalizable = mock(Internationalizable.class);
        when(internationalizable.getName(anyString())).thenReturn("testName");
        assertEquals("testName", objectMapper.convertValue(internationalizable, String.class));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);

        ((DefaultHttpFirewall) httpFirewall).setAllowUrlEncodedSlash(true);
        assertTrue(((DefaultHttpFirewall) httpFirewall).getAllowedUrlEncodedSlash());

        ((DefaultHttpFirewall) httpFirewall).setAllowUrlEncodedSlash(false);
        assertFalse(((DefaultHttpFirewall) httpFirewall).getAllowedUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);

        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> obInstances = new ArrayList<>();
        obInstances.add(new OBInstance());
        when(cloudMetadataClient.listInstances()).thenReturn(obInstances);
        assertEquals(obInstances, cloudMetadataClient.listInstances());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);

        String cookieName = cookieSerializer.getCookieName();
        assertEquals("JSESSIONID", cookieName);

        cookieSerializer.setCookieName("testCookieName");
        assertNotSame("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());

        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(anyString()));
    }
}