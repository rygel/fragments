/*
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.andromeda.fragments.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.andromeda.fragments.Fragment;
import io.andromeda.fragments.Fragments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Alexander Brandt
 */
public class DBSupport {
    /**
     * The logger instance for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DBSupport.class);

    private int defaultTopFragments = 5;
    private DBConfiguration configuration;
    private String tableName;

    private Sql2o sql2o;

    public DBSupport(DBConfiguration configuration, Fragments fragments) {
        this.configuration = configuration;
        initialize();
    }

    public final boolean initialize() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.toString());
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(configuration.getDriver());
        //hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setUsername(configuration.getUsername());
        hikariConfig.setPassword(configuration.getPassword());
        hikariConfig.addDataSourceProperty("URL", configuration.getConnection());
        //hikariConfig.addDataSourceProperty("database", configuration.getDBName());
        hikariConfig.setAutoCommit(false);

        DataSource dataSource = new HikariDataSource(hikariConfig);
        sql2o = new Sql2o(dataSource);

        return createTable();
    }


    public boolean createTable() {
        boolean result = true;
        String createTable = String.format("CREATE TABLE %s(id int primary key, name varchar(1000), clicks bigint)", configuration.getDBName());
        //String sql = "SELECT id FROM author WHERE p_username = ':p_username'";

        try (Connection con = sql2o.open()) {
            con.createQuery(createTable).executeUpdate().commit();
        } catch (Exception exception) {
            LOGGER.error("Exception: {}", exception);
            result = false;
        }
        return result;
    }

    public void addClick(Fragment fragment) {
        String sql = String.format("SELECT clicks FROM %s where name = ':name'", configuration.getDBName());
        String sqlInsert = String.format("insert into %s(name, clicks) values (:name, :clicks)", configuration.getDBName());

        try (Connection con = sql2o.open()) {
            Long clicks = con.createQuery(sql)
                    .addParameter("name", fragment.filename)
                    .executeScalar(Long.class);


            con.createQuery(sql)
                    .addParameter("p_name", fragment.filename)
                    .addParameter("p_clicks", ++clicks)
                    .executeUpdate()
                    .getKey();
            con.commit();
        } catch (Exception exception) {
            LOGGER.error("Exception: " + exception);
        }
    }

    public List<Fragment> getTopFragments() {
        return getTopFragments(defaultTopFragments);
    }

    public List<Fragment> getTopFragments(int numberOfTopFragments) {
        String sql = "SELECT id FROM author WHERE p_username = ':p_username'";

        /*List<Author> author = new ArrayList<Author>();
        try (Connection con = sql2o.open()) {
            author = con.createQuery(sql)
                    .addParameter("p_username", username)
                    .executeAndFetch(Author.class);
        } catch (Exception exception) {
            LOGGER.error("Exception: " + exception);
        }

        if (author.isEmpty()) {
            LOGGER.error("Author is empty!");
            return false;
        } else if (author.size() > 1) {
            LOGGER.error("Author size is greater than 1 (!" + author.size() + ") for username=\"" + username + "\"!");
            return false;
        }
        Author a = author.get(0);
        return true;*/

        return null;
    }

    /*private static void insertWithPreparedStatement() throws SQLException {
        Connection connection = getDBConnection();
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement selectPreparedStatement = null;

        String CreateQuery = "CREATE TABLE PERSON(id int primary key, name varchar(255))";
        String InsertQuery = "INSERT INTO PERSON" + "(id, name) values" + "(?,?)";
        String SelectQuery = "select * from PERSON";
        try {
            connection.setAutoCommit(false);

            createPreparedStatement = connection.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setString(2, "Jose");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            selectPreparedStatement = connection.prepareStatement(SelectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            System.out.println("H2 Database inserted through PreparedStatement");
            while (rs.next()) {
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
            }
            selectPreparedStatement.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }*/

    /*private static void insertWithStatement() throws SQLException {
        Connection connection = getDBConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute("CREATE TABLE PERSON(id int primary key, name varchar(255))");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(1, 'Anju')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(2, 'Sonia')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

            ResultSet rs = stmt.executeQuery("select * from PERSON");
            System.out.println("H2 Database inserted through Statement");
            while (rs.next()) {
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }*/



}
