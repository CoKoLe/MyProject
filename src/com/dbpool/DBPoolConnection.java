package com.dbpool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.util.PropUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by pengsheng on 2014/06/13.
 */
public class DBPoolConnection {

    private static Logger logger = LogManager.getLogger(DBPoolConnection.class);


    private static DBPoolConnection databasePool = null;
    private static DruidDataSource dataSource = null;

    static {

        Properties db_property = PropUtil.getProperty("db_server.properties");
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(db_property);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static DBPoolConnection getInstance() {

        if (databasePool == null) {
            databasePool = new DBPoolConnection();
        }

        return databasePool;
    }

    public DruidPooledConnection getConnection() throws SQLException {

        if (dataSource != null) {
            return dataSource.getConnection();
        }

        return null;
    }
}
