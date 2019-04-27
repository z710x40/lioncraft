package lioncraftserver.comobjects;

import java.io.Serializable;


/**
 * This class is the answer to a request. It provides the record type to receive and the record size;
 * @author Erwin
 *
 */
public class PreRecord implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6457608118894230233L;
	int recordsize;
	Object record;
	
	public PreRecord() {
		// TODO Auto-generated constructor stub
	}
	
	

	public PreRecord(Object record) {
		super();
		this.recordsize = record.getClass().getModifiers();
		this.record=record;
	}

	

	public Object getRecord() {
		return record;
	}



	public int getRecordsize() {
		return recordsize;
	}

	public void setRecordsize(int recordsize) {
		this.recordsize = recordsize;
	}

}
