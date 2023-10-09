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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.opensymphony.xwork2.ActionContext;

public class ReportAction extends ExampleSupport {
	private static Logger logger = Logger.getLogger( ReportAction.class ); //f:log
	private InputStream inputStream;

	public InputStream getInputStream() {
		return this.inputStream;
	}

	public void setInputStream( InputStream fileInputStream ) {
		this.inputStream = fileInputStream;
	}

	@Override
	public String execute() throws Exception {
		return this.input();
	}

	public String productReport() throws Exception {
		logger.info( "Starting productReport()" ); //f:log

		ActionContext ac = ActionContext.getContext();
		ServletContext sc = (ServletContext) ac.get( StrutsStatics.SERVLET_CONTEXT );

		JasperReport jasperReport = JasperCompileManager.compileReport( sc.getResourceAsStream( "/WEB-INF/classes/ProductReport.xml" ) ); //f:jr

		Map<String, Object> parameters = new HashMap<String, Object>(); //f:jr
		parameters.put( "ReportTitle", "List of Products" ); //f:jr
		parameters.put( "DataFile", new Date().toString() ); //f:jr

		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Product.class ); //f:hibernate

		criteria.setProjection( Projections.projectionList().add( Projections.property( "id" ) ).add( Projections.property( "name" ) ).add( Projections.property( "price" ) ) ); //f:hibernate

		
		@SuppressWarnings("unchecked")
		List<Object[]> l = (List<Object[]>) criteria.list(); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		HibernateQueryResultDataSource ds = new HibernateQueryResultDataSource(l, new String[] { "Id", "Name", "Price" }); //f:jr

		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, ds ); //f:jr

		byte b[] = JasperExportManager.exportReportToPdf( jasperPrint ); //f:jr

		this.inputStream = new ByteArrayInputStream( b );

		logger.info( "Finishing productReport()" ); //f:log
		return "download";
	}
	
	
	public String customerReport() throws Exception {
		logger.info( "Starting customerReport()" ); //f:log

		ActionContext ac = ActionContext.getContext();
		ServletContext sc = (ServletContext) ac.get( StrutsStatics.SERVLET_CONTEXT );

		JasperReport jasperReport = JasperCompileManager.compileReport( sc.getResourceAsStream( "/WEB-INF/classes/CustomerReport.xml" ) ); //f:jr

		Map<String, Object> parameters = new HashMap<String, Object>(); //f:jr
		parameters.put( "ReportTitle", "List of Customers" ); //f:jr
		parameters.put( "DataFile", new Date().toString() ); //f:jr

		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Customer.class ); //f:hibernate

		criteria.setProjection( Projections.projectionList().add( Projections.property( "id" ) ).add( Projections.property( "name" ) ).add( Projections.property( "phone" ) ) ); //f:hibernate

		
		@SuppressWarnings("unchecked")
		List<Object[]> l = (List<Object[]>) criteria.list(); //f:hibernate

		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		HibernateQueryResultDataSource ds = new HibernateQueryResultDataSource(l, new String[] { "Id", "Name", "Phone" }); //f:jr

		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, ds ); //f:jr

		byte b[] = JasperExportManager.exportReportToPdf( jasperPrint ); //f:jr

		this.inputStream = new ByteArrayInputStream( b );

		logger.info( "Finishing customerReport()" ); //f:log
		return "download";
	}
	
}