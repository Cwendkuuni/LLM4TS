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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        assertEquals(customOutputSerializer, objectMapper.getSerializationConfig().getSerializationView(CustomOutputSerializer.class));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);

        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decryptedString");
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer()));
        assertNotSame(objectMapper, beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decryptedString");
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> instances = new ArrayList<>();
        instances.add(new OBInstance());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());

        List<OBTenant> tenants = new ArrayList<>();
        tenants.add(new OBTenant());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(anyString()));

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));

        List<OBDatabaseUser> databaseUsers = new ArrayList<>();
        databaseUsers.add(new OBDatabaseUser());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString()));

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        assertEquals(endpoint, nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString()));

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(anyString()));
    }

    private CustomOutputSerializer customOutputSerializer() {
        return new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
    }
}