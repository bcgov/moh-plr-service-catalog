package hlth.gov.bc.ca.serviceCatalog;

import hlth.gov.bc.ca.serviceCatalog.provider.ServiceCatalogProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author camille.estival
 */
@WebServlet("/*")
public class ServiceCatalogRestfulServer extends RestfulServer {
    
    private final ApplicationContext applicationContext;
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogRestfulServer.class);
    
    ServiceCatalogRestfulServer(ApplicationContext context) {
        this.applicationContext = context;
    }

    @Override
    protected void initialize() throws ServletException{
        super.initialize();
        setFhirContext(FhirContext.forR4());
        
        //Register Resource Providers 
        registerProvider(applicationContext.getBean(ServiceCatalogProvider.class));
        
        CorsConfiguration  config = new  CorsConfiguration();
        config.addAllowedHeader("x-fhir-starter");
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Content-Type");
        config.addAllowedOrigin("*");
        config.addExposedHeader("Location");
        config.addExposedHeader("Content-Location");
        config.setAllowedMethods(Arrays.asList("GET"));
        // Create the interceptor and register it
        CorsInterceptor  interceptor = new  CorsInterceptor(config);
        registerInterceptor(interceptor);
    }
   
}
