package com.lazy.devhub.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, Object> parameters = new HashMap<>();
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public HibernateUtil() {
    } 

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public void setParameters(Map<String, Object> params) {
        parameters.putAll(params);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public int update(String sql) {
        Transaction transaction = null;
        int result = 0;

        try (Session session = entityManager.unwrap(Session.class)) {
            transaction = session.beginTransaction();
            NativeQuery query = session.createNativeQuery(sql);
            setParameters(query);
            result = query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Failed to execute update", e);
        }

        return result;
    }

    public List<Map<String, Object>> searchAndRetrieve(String sql) {
        List<Map<String, Object>> result = null;

        try (Session session = entityManager.unwrap(Session.class)) {
            NativeQuery query = session.createNativeQuery(sql);
            setParameters(query);
            query.setResultTransformer(org.hibernate.transform.Transformers.ALIAS_TO_ENTITY_MAP);
            result = query.list();
        } catch (Exception e) {
            LOGGER.error("Failed to execute query", e);
        }

        return result;
    }

    private void setParameters(NativeQuery query) {
        parameters.forEach(query::setParameter);
    }
}