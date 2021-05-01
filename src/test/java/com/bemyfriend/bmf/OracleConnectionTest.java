package com.bemyfriend.bmf;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*-context.xml" })
public class OracleConnectionTest {


	
	 @Test
	 public void testConnection() throws Exception{
		 
		 try (Connection con = DriverManager
				 .getConnection("jdbc:oracle:thin:@pclassdb_high?TNS_ADMIN=/Users/miyoung/06_oracle/Wallet_pclassDB", "admin", "1*aL201210380")) {
			 
	            System.out.println("\n >>>>>>>>>> Connection 출력 : " + con + "\n");
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	 }
	 
}
