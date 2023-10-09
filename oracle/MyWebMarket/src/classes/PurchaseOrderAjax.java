package classes;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PurchaseOrderAjax {
	private static Logger logger = Logger.getLogger(PurchaseOrderAjax.class); //f:log
	
	public Double getProductPrice(Integer idProduct){
		logger.info( "Starting getProductPrice()" ); //f:log
		Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Product.class ); //f:hibernate
		
		
		criteria.setProjection( Projections.property( "price" ) ); //f:hibernate
		
		criteria.add( Restrictions.eq( "id", idProduct ) ); //f:hibernate

		Double price = (Double) criteria.uniqueResult(); //f:hibernate
		
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate
		logger.info( "Finishing getProductPrice()" ); //f:log
		return price;
	}
	
	
}
