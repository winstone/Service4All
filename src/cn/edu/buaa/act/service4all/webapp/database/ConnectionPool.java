/**
* Service4All: A Service-oriented Cloud Platform for All about Software Development
* Copyright (C) Institute of Advanced Computing Technology, Beihang University
* Contact: service4all@act.buaa.edu.cn
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3.0 of the License, or any later version. 
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details. 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*/
package cn.edu.buaa.act.service4all.webapp.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import cn.edu.buaa.act.service4all.webapp.utility.Constants;

public class ConnectionPool {

	private List<Connection> availableConPool;
	private static String url;
	private static String username;
	private static String password;
	private static String driverClassName;
	/**
	 * the size of the pool
	 */
	private int minConNum = 1;
	private int maxConNum = 5;
	private int actualConNum = 0;
	private static ConnectionPool instance = null;

	synchronized public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	private ConnectionPool() {
		init();
	}

	private void init() {
		username = Constants.DB_USER_NAME;
		password = Constants.DB_PASSWORD;
		url = Constants.DATABASE_URL;
		driverClassName = "com.mysql.jdbc.Driver";
		addConnection();
	}

	synchronized public void releaseConnection(Connection conn) {
		availableConPool.add(conn);
		notifyAll();
	}

	synchronized public void closePool() {
		int size = availableConPool.size();
		for (int i = 0; i < size; i++) {
			try {
				availableConPool.get(i).close();
				actualConNum--;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		availableConPool.clear();
	}

	private void addConnection() {
		availableConPool = new LinkedList<Connection>();
		Connection conn = null;
		for (int i = 0; i < minConNum; i++) {
			try {
				Class.forName(driverClassName);
				conn = java.sql.DriverManager.getConnection(url, username,
						password);
				availableConPool.add(conn);
				actualConNum++;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	synchronized public Connection getConnection() {
		while (true) {
			if (availableConPool.size() > 0) {
				return availableConPool.remove(0);
			} else if (actualConNum < maxConNum) {
				Connection conn = null;
				try {
					Class.forName(driverClassName);
					conn = java.sql.DriverManager.getConnection(url, username,
							password);
					availableConPool.add(conn);
					actualConNum++;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return availableConPool.remove(0);
			} else {
				try {
					wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
