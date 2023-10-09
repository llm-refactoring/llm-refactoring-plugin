package classes;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class HibernateQueryResultDataSource implements JRDataSource {

	private String[] fields;
	private Iterator iterator;
	private Object currentValue;

	public HibernateQueryResultDataSource(List list, String[] fields) {
		this.fields = fields;
		this.iterator = list.iterator();
	}

	public Object getFieldValue( JRField field ) throws JRException {
		Object value = null;
		int index = getFieldIndex( field.getName() );
		if ( index > -1 ) {
			Object[] values = (Object[]) currentValue;
			value = values[index];
		}
		return value;
	}

	public boolean next() throws JRException {
		currentValue = iterator.hasNext() ? iterator.next() : null;
		return (currentValue != null);
	}

	private int getFieldIndex( String field ) {
		int index = -1;
		for ( int i = 0; i < fields.length; i++ ) {
			if ( fields[i].equals( field ) ) {
				index = i;
				break;
			}
		}
		return index;
	}

}
