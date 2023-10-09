/*
 * $Id: Login.java 471756 2006-11-06 15:01:43Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package classes;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionContext;

public class LoginAction extends ExampleSupport {
	private static Logger logger = Logger.getLogger(LoginAction.class);
	private User user;

	public User getUser() {
		return this.user;
	}

	public void setUser( User user ) {
		this.user = user;
	}

	public String execute() throws Exception {
		logger.info( "Starting execute()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( User.class ); //f:hibernate

		criteria.add( Restrictions.idEq( this.user.getUsername() ) ); //f:hibernate
		criteria.add( Restrictions.eq( "password", this.user.getPassword() ) ); //f:hibernate

		User user = (User) criteria.uniqueResult(); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		if ( user != null ) {
			ActionContext.getContext().getSession().put( SystemConstants.AUTHENTICATED_USER, user );
			logger.info( "Finishing execute() -- Success" ); //f:log
			return SUCCESS;
		}

		this.addActionError( this.getText( "login.failure" ) );
		logger.info( "Finishing execute() -- Failure" ); //f:log		
		return INPUT;
	}

	public String logoff() throws Exception {
		logger.info( "Starting logoff()" ); //f:log
		ActionContext.getContext().getSession().clear();
		logger.info( "Finishing logoff()" ); //f:log
		return INPUT;
	}

}