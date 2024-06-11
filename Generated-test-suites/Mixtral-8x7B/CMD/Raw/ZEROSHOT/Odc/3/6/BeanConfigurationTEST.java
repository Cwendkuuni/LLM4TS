import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
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
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.session.CookieSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils.getField(objectMapper, "customOutputSerializer");
        assertNotNull(customOutputSerializer);

        I18nOutputSerializer i18nOutputSerializer = (I18nOutputSerializer) customOutputSerializer.getSerializers().get(0).getValue();
        assertNotNull(i18nOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
        assertEquals(true, ((DefaultHttpFirewall) httpFirewall).getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);

        assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, cloudMetadataClient.supportsTenantInstance());
        assertEquals(false, cloudMetadataClient.needsOBTenantName());
        assertEquals(false, cloudMetadataClient.needsSysTenantUser());
        assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> instances = new ArrayList<>();
        List<OBTenant> tenants = new ArrayList<>();
        List<OBDatabaseUser> users = new ArrayList<>();
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);

        Mockito.when(sensitivePropertyHandler.decrypt("test")).thenReturn("test");

        cloudMetadataClient.listInstances();
        cloudMetadataClient.listTenants("testInstanceId");
        cloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId");
        cloudMetadataClient.getTenant("testInstanceId", "testTenantId");
        cloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId");
        cloudMetadataClient.getSysTenantUser("testInstanceId");

        Mockito.verify(sensitivePropertyHandler, Mockito.times(6)).decrypt("test");

        Mockito.verifyNoMoreInteractions(sensitivePropertyHandler);
    }
}