import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.test.util.ReflectionTestUtils;

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

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils.getField(objectMapper, "customOutputSerializer");
        assertNotNull(customOutputSerializer);

        I18nOutputSerializer i18nOutputSerializer = (I18nOutputSerializer) customOutputSerializer.getSerializer(Internationalizable.class);
        assertNotNull(i18nOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
        assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
    }
}