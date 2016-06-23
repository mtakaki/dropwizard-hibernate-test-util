### Status
![Build Status](https://codeship.com/projects/676a7c70-1b3f-0134-7817-66ed86225da0/status?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mtakaki/dropwizard-hibernate-test-util/badge.svg?branch=master)](https://coveralls.io/github/mtakaki/dropwizard-hibernate-test-util?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/039da749e34a4c46b297ddf1389d593a)](https://www.codacy.com/app/mitsuotakaki/dropwizard-circuitbreaker)
[![Download](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-hibernate-test-util/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mtakaki/dropwizard-hibernate-test-util)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.github.mtakaki/dropwizard-hibernate-test-util/badge.svg)](http://www.javadoc.io/doc/com.github.mtakaki/dropwizard-hibernate-test-util)
![License](https://img.shields.io/github/license/mtakaki/dropwizard-hibernate-test-util.svg)

# dropwizard-hibernate-test-util

Hibernate utility class for writing integration tests for `AbstractDAO` classes in dropwizard applications. It uses an in-memory database ([HSQLDB](http://hsqldb.org/)) and uses hibernate functionality to auto-create the tables on startup.

This library follows what is described in this blog post: http://www.petrikainulainen.net/programming/testing/writing-tests-for-data-access-code-unit-tests-are-waste/ Bugs often slips through the unit tests with mocks. Using an in-memory database allows you to actually run the SQL scripts and remove the effort of writing mocks.

 
## Maven

The library is available at the maven central, so just add the dependency to `pom.xml` with scope set to `test`:

```xml
<dependencies>
    <dependency>
        <groupId>com.github.mtakaki</groupId>
        <artifactId>dropwizard-hibernate-test-util</artifactId>
        <version>0.0.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Example

The class `HibernateDAOTestUtil` provides a jUnit Rule, so it can be simply used like this:

```java
public class TestEntityDAOTest {
    @Rule
    public HibernateDAOTestUtil testUtil = new HibernateDAOTestUtil(TestEntity.class);
    
    private TestEntityDAO dao;

    public void setup() {
        this.dao = new TestEntityDAO(this.testUtil.getSessionFactory());
    }

    @Test
    public void testSaveAndQuery() {
        // We have a session opened and ready to be used.
        final Session session = this.testUtil.getSession();
        final TestEntity entity = TestEntity.builder().body("testing writing").build();
        session.save(entity);

        final Optional<TestEntity> foundEntityOptional = this.dao.findById(entity.getId());
        assertThat(foundEntityOptional).isPresent().contains(entity);
    }
}
```

The schema is completely dropped after each test, guaranteeing test isolation. So no need to clean up after yourself, just let the database be destroyed after the test.