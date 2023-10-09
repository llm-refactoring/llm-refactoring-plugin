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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class CustomerAction extends ExampleSupport implements RequestAware,
		ParameterAware {
	private static Logger logger = Logger.getLogger(CustomerAction.class);
	private Map<String, Object> request;
	private Map<String, String[]> parameters;
	private Customer customer;
	private String task;

	@Override
	public void setRequest( Map<String, Object> map ) {
		request = map;
	}

	@Override
	public void setParameters( Map<String, String[]> parameters ) {
		this.parameters = parameters;

	}

	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask( String task ) {
		this.task = task;
	}

	@Override
	public String execute() throws Exception {
		return this.input();
	}
	
	@Override
	public String input() throws Exception {
		this.task = SystemConstants.CR_MODE;
		
		this.customer = null;
		
		return super.input();
	}

	public String find() throws Exception {
		logger.info( "Starting find()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Customer.class ); //f:hibernate

		criteria.add( Example.create( this.customer ).excludeZeroes().ignoreCase().enableLike( MatchMode.ANYWHERE ) ); //f:hibernate
		if ( this.customer.getId() != null ) { //f:hibernate
			criteria.add( Restrictions.idEq( this.customer.getId() ) ); //f:hibernate
		} //f:hibernate

		@SuppressWarnings("unchecked")
		List<Customer> l = (List<Customer>) criteria.list(); //f:hibernate
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate
		request.put( "list", l );

		this.task = SystemConstants.CR_MODE;
		logger.info( "Finishing find()" ); //f:log
		return INPUT;
	}

	public String save() throws Exception {
		logger.info( "Starting save()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate
		
		sess.save( this.customer ); //f:hibernate
		
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.task = SystemConstants.UD_MODE;
		this.addActionMessage( this.getText( "saveSuccessful", new String[]{"customer"})  );
		
		logger.info( "Finishing save()" ); //f:log
		return INPUT;
	}

	public String update() throws Exception {
		logger.info( "Starting update()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate
		
		sess.update( this.customer ); //f:hibernate
		
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.task = SystemConstants.UD_MODE;
		this.addActionMessage( this.getText( "updateSuccessful", new String[]{"customer"})  );
		
		logger.info( "Finishing update()" ); //f:log
		return INPUT;
	}

	public String delete() throws Exception {
		logger.info( "Starting delete()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate
		
		sess.delete( this.customer ); //f:hibernate
		
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.task = SystemConstants.CR_MODE;
		this.addActionMessage( this.getText( "deleteSuccessful", new String[]{"customer"})  );
		
		logger.info( "Finishing delete()" ); //f:log
		return this.input();
	}

	public String edit() throws Exception {
		logger.info( "Starting edit()" ); //f:log
		Integer id = Integer.valueOf( this.parameters.get( "id" )[0] );

		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Customer c = (Customer) sess.get( Customer.class, id ); //f:hibernate
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.setCustomer( c );

		logger.info( "Finishing edit()" ); //f:log
		this.task = SystemConstants.UD_MODE;
		return INPUT;
	}

}