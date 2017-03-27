package orj.worf.mybatis.mapper;

import orj.worf.core.util.BeanUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

public class MapperScannerConfigurer extends org.mybatis.spring.mapper.MapperScannerConfigurer {
    private ApplicationContext applicationContext;
    private String basePackage;

    @Override
    public void setBasePackage(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        super.setApplicationContext(applicationContext);
        setAnnotationClass(Repository.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.basePackage == null) {
            String allBasePackages = BeanUtils.getAllBasePackagesString(this.applicationContext);
            super.setBasePackage(allBasePackages.toString());
        } else {
            super.setBasePackage(this.basePackage);
        }
        super.afterPropertiesSet();
    }
}