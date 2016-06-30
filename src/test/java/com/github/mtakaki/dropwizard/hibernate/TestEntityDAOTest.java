package com.github.mtakaki.dropwizard.hibernate;

import static org.assertj.guava.api.Assertions.assertThat;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Optional;

public class TestEntityDAOTest {
    @Rule
    public HibernateDAOTestUtil testUtil = new HibernateDAOTestUtil(TestEntity.class);
    private TestEntityDAO dao;

    @Before
    public void setup() {
        this.dao = new TestEntityDAO(this.testUtil.getSessionFactory());
    }

    /**
     * Testing that we can save using the created session and the DAO can
     * retrieve the saved entity.
     */
    @Test
    public void testSaveAndQuery() {
        final Session session = this.testUtil.getSession();
        final TestEntity entity = TestEntity.builder().body("testing writing").build();
        session.save(entity);

        final Optional<TestEntity> foundEntityOptional = this.dao.findById(entity.getId());
        assertThat(foundEntityOptional).isPresent().contains(entity);
    }

    /**
     * Testing that we can't find an entity with an invalid id. It also makes
     * sure we have multiple tests.
     */
    @Test
    public void testQueryNotFound() {
        final Optional<TestEntity> foundEntityOptional = this.dao.findById(1);
        assertThat(foundEntityOptional).isAbsent();
    }
}