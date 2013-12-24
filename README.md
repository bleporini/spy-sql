# Spy SQL
## What is it?
Spy SQL is just a datasource wrapper that allows to hook listeners to monitor DB activity. Its original purpose is to programmatically check the number of JDBC roundtrips and the number or the nature of SQL commands executed by JPA application.
## How to use it?
Suppose you're using `commons-dbcp`, just create a new `SpyDatasource` with the target datasource :

```java
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:sample");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");
        final SpyDatasource spyDs = new SpyDatasource(basicDataSource);
```

Obviously, it can be used with `Spring` or any other DI framework.

Then you can register any implementation of the simple interface `io.blep.spysql.SqlListener`, `io.blep.spysql.SqlCounter` which is provided or your own:

```java
spyDatasource.addListener(listener);
```

If your datasource is shared accross multiple threads, beware of concurrency issues as listeners are shared as well. If you're just using it in single treaded test cases, just don't care about it.
