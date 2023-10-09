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

import java.util.Date;
import java.util.Iterator;
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

import com.opensymphony.xwork2.ActionContext;

public class PurchaseOrderAction extends ExampleSupport implements
		RequestAware, ParameterAware {
	private static Logger logger = Logger.getLogger(PurchaseOrderAction.class); //f:log
	private Map<String, Object> request;
	private Map<String, String[]> parameters;
	private PurchaseOrder purchaseOrder;
	private String task;

	@Override
	public void setRequest( Map<String, Object> map ) {
		request = map;
	}

	@Override
	public void setParameters( Map<String, String[]> parameters ) {
		this.parameters = parameters;

	}

	public PurchaseOrder getPurchaseOrder() {
		return this.purchaseOrder;
	}

	public void setPurchaseOrder( PurchaseOrder purchaseOrder ) {
		this.purchaseOrder = purchaseOrder;
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
		logger.info( "Starting input()" ); //f:log
		this.task = SystemConstants.CR_MODE;

		this.purchaseOrder = null;

		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Customer.class ); //f:hibernate
		@SuppressWarnings("unchecked")
		List<Customer> lc = (List<Customer>) criteria.list(); //f:hibernate
		ActionContext.getContext().getSession().put( "listCustomer", lc );

		criteria = sess.createCriteria( Product.class ); //f:hibernate
		@SuppressWarnings("unchecked")
		List<Product> lp = (List<Product>) criteria.list(); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		ActionContext.getContext().getSession().put( "listProduct", lp );

		logger.info( "Finishing input()" ); //f:log
		return INPUT;
	}

	public String find() throws Exception {
		logger.info( "Starting find()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( PurchaseOrder.class ); //f:hibernate

		criteria.add( Example.create( this.purchaseOrder ).excludeZeroes().ignoreCase().enableLike( MatchMode.ANYWHERE ) ); //f:hibernate
		if ( this.purchaseOrder.getId() != null ) { //f:hibernate
			criteria.add( Restrictions.idEq( this.purchaseOrder.getId() ) ); //f:hibernate
		} //f:hibernate

		if ( this.purchaseOrder.getCustomer().getId() != null ) { //f:hibernate
			criteria.add( Restrictions.eq( "customer", this.purchaseOrder.getCustomer() ) ); //f:hibernate
		} //f:hibernate

		criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY ); //f:hibernate
		@SuppressWarnings("unchecked")
		List<PurchaseOrder> l = (List<PurchaseOrder>) criteria.list(); //f:hibernate
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate
		request.put( "list", l );

		this.task = SystemConstants.CR_MODE;
		logger.info( "Finishing input()" ); //f:log
		return INPUT;
	}

	public String save() throws Exception {
		logger.info( "Starting save()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		sess.save( this.purchaseOrder ); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.purchaseOrder.setOrderDate( new Date() );

		for (PurchaseOrderItem item : this.purchaseOrder.getPurchaseOrderItems()) {  
			item.setPurchaseOrder( this.purchaseOrder );
		}
		
		this.task = SystemConstants.UD_MODE;
		this.addActionMessage( this.getText( "saveSuccessful", new String[] { "order" } ) );

		logger.info( "Finishing save()" ); //f:log
		return INPUT;
	}

	public String update() throws Exception {
		logger.info( "Starting update()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate
		sess.update( this.purchaseOrder ); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		for (PurchaseOrderItem item : this.purchaseOrder.getPurchaseOrderItems()){
			item.setPurchaseOrder( this.purchaseOrder );
		}
		
		this.task = SystemConstants.UD_MODE;
		this.addActionMessage( this.getText( "updateSuccessful", new String[] { "order" } ) );

		logger.info( "Finishing update()" ); //f:log
		return INPUT;
	}

	public String delete() throws Exception {
		logger.info( "Starting delete()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		sess.delete( this.purchaseOrder ); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.task = SystemConstants.CR_MODE;
		this.addActionMessage( this.getText( "deleteSuccessful", new String[] { "order" } ) );
		logger.info( "Finishing delete()" ); //f:log
		return this.input();
	}

	public String edit() throws Exception {
		logger.info( "Starting edit()" ); //f:log
		Integer id = Integer.valueOf( this.parameters.get( "id" )[0] );

		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		PurchaseOrder po = (PurchaseOrder) sess.get( PurchaseOrder.class, id ); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		this.setPurchaseOrder( po );

		this.task = SystemConstants.UD_MODE;
		logger.info( "Finishing edit()" ); //f:log
		return INPUT;
	}

	@Override
	public void validate() {
		logger.info( "Starting validate()" ); //f:log
		super.validate();

		int i = 0;
		for ( Iterator<PurchaseOrderItem> it = this.purchaseOrder.getPurchaseOrderItems().iterator(); it.hasNext(); i++ ) {
			PurchaseOrderItem item = it.next();

			if ( item.getProduct().getId() != null || item.getPrice() != null 
					|| item.getQuantity() != null || i == 0 ) {
				if ( item.getProduct().getId() == null ) {
					this.addFieldError( "item" + i + "_product", getText( "requiredspecific", new String[] { "Product" } ) );
				}
				if ( item.getPrice() == null ) {
					this.addFieldError( "item" + i + "_price", getText( "requiredspecific", new String[] { "Price" } ) );
				}
				if ( item.getQuantity() == null ) {
					this.addFieldError( "item" + i + "_quantity", getText( "requiredspecific", new String[] { "Quantity" } ) );
				}
			} else {
				it.remove();
			}
		}
		logger.info( "Finishing validate()" ); //f:log
	}

}