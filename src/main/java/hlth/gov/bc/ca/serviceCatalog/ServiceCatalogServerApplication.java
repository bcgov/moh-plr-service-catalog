package hlth.gov.bc.ca.serviceCatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceCatalogServerApplication {
    
    @Autowired
    private ApplicationContext context;
    
    public static void main(String[] args) {
        SpringApplication.run(ServiceCatalogServerApplication.class, args);
    }
    
    @Bean
    public ServletRegistrationBean ServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServiceCatalogRestfulServer(context),"/ServiceCatalogue/*");
        registration.setName("PLRServiceCatalog");
        return registration;
    }
}