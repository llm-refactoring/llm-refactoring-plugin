package classes;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SupplyMailJob implements Job {
	private static Logger logger = Logger.getLogger( SupplyMailJob.class ); //f:log
	
	private String to = "terra@dcc.ufmg.br";
	private String from = "terra@dcc.ufmg.br";
	private String host = "smtp.dcc.ufmg.br"; 

	
	public void execute( JobExecutionContext context )
			throws JobExecutionException {
		logger.info( "Starting execute() in job" ); //f:log

		org.hibernate.Session sess = HibernateUtil.getSessionFactory().openSession(); //f:hibernate
		Transaction t = sess.beginTransaction(); //f:hibernate

		Criteria criteria = sess.createCriteria( Product.class ); //f:hibernate
		criteria.add( Restrictions.lt( "supply", 5 ) ); //f:hibernate

		@SuppressWarnings("unchecked")
		List<Product> l = (List<Product>) criteria.list(); //f:hibernate
		t.commit(); //f:hibernate
		sess.close(); //f:hibernate

		if ( !l.isEmpty() ) {
			StringBuilder str = new StringBuilder();
			str.append( "The following products are getting over:\n" );

			Properties props = new Properties(); //f:mail
			props.setProperty( "mail.smtp.submitter", "terra" ); //f:mail
			props.setProperty( "mail.smtp.auth", "false" ); //f:mail
			props.setProperty( "mail.smtp.host", this.host ); //f:mail
			props.setProperty( "mail.smtp.port", "25" ); //f:mail

			javax.mail.Session session = javax.mail.Session.getInstance( props ); //f:mail

			try { //f:mail
				Message msg = new MimeMessage( session ); //f:mail

				msg.setFrom( new InternetAddress( this.from ) ); //f:mail
				InternetAddress[] address = { new InternetAddress( this.to ) }; //f:mail
				msg.setRecipients( Message.RecipientType.TO, address ); //f:mail
				msg.setSubject( "Low Supply of Products" ); //f:mail
				msg.setSentDate( new Date() ); //f:mail

				msg.setText( str.toString() ); //f:mail

				Transport.send( msg ); //f:mail
			} catch (MessagingException mex) {
				mex.printStackTrace();
			}

			for ( Product p : l ) {
				str.append( p.getName() + "\t\t" + p.getSupply() + "\n" );
			}
		}

		logger.info( "Finishing execute() in job" ); //f:log
	}

}

