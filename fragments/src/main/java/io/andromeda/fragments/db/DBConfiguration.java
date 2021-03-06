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

import io.andromeda.fragments.Fragments;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Alexander Brandt
 */
public class DBConfiguration {
    //private final String driver = "org.h2.jdbcx.JdbcDataSource";
    private final String driver = "org.h2.Driver";
    private String connection = "jdbc:h2:";
    private final String username = "fragments";
    private String password;
    private final String name;
    private boolean resetDB = false;

    public DBConfiguration(Fragments fragments, String password) {
        String nameTemplate = "fragments_clicks_";
        name = nameTemplate + fragments.getName();
        Path path = Paths.get(fragments.getDataDirectory()).normalize().toAbsolutePath();
        this.password = password;
        String dbFileLocation = path.getParent().toString() + "/";
        /* Append the name of the Fragments instance to the connection path to be able to distinguish between different
         * databases. */
        dbFileLocation += fragments.getName();
        connection += dbFileLocation;
    }

    public String getDriver() {
        return driver;
    }

    public String getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDBName() {
        return name;
    }

    public boolean getResetDB() {
        return resetDB;
    }

    public void setResetDB(boolean resetDB) {
        this.resetDB = resetDB;
    }
}
