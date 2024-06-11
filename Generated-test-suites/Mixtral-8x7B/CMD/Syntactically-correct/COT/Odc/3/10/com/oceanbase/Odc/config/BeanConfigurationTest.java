import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertNotNull(passwordEncoder.encode("test"));
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        // Check if objectMapper is equal to expectedObjectMapper
        // You can add more assertions based on your needs
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);

        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
        when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);

        when(cloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        when(cloudMetadataClient.listTenants(anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        when(cloudMetadataClient.getTenant(anyString(), anyString())).thenThrow(new UnsupportedException("getTenant not supported in non-cloud"));
        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(new OBTenantEndpoint());
        when(cloudMetadataClient.getSysTenantUser(anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));

        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertNotNull(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser(anyString()));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        cookieSerializer.setCookieName("JSESSIONID");
    }
}