package orj.worf.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.PersistenceExceptionTranslator;

public class SqlSessionTemplate extends org.mybatis.spring.SqlSessionTemplate {
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionTemplate.class);

    public SqlSessionTemplate(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public SqlSessionTemplate(final SqlSessionFactory sqlSessionFactory, final ExecutorType executorType) {
        super(sqlSessionFactory, executorType);
    }

    public SqlSessionTemplate(final SqlSessionFactory sqlSessionFactory, final ExecutorType executorType,
            final PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
    }

    @Override
    public void close() {
        if (logger.isDebugEnabled()) {
            logger.debug("Manual close is not allowed over a Spring managed SqlSession");
        }
    }

}
