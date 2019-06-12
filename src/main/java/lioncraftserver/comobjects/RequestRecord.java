package lioncraftserver.comobjects;

import java.util.List;
import java.io.Serializable;
import java.util.Collections;

/*
 * This class is for communication from the client to the server
 */
public class RequestRecord implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1785679293617988947L;
	// 0 identify record
	// 1 get chunks from chunkIDlist
	// 2 update new block on server
	// 3 update remove block on
	// 4 get Single chunk
	
	private int requesttype;
	public int getBlockType() {
		return blockType;
	}




	public void setBlockType(int blockType) {
		this.blockType = blockType;
	}




	private int usernumber;
	private List<String> chunkids;
	
	private String blockid;
	private String chunkid;
	private int blockType;
	


	private RequestRecord(Builder builder) {
		this.requesttype = builder.requesttype;
		this.usernumber = builder.usernumber;
		this.chunkids = builder.chunkids;
		this.blockid = builder.blockid;
	}
	
	
	
	
	public RequestRecord() {
		// TODO Auto-generated constructor stub
	}


	public int getRequesttype() {
		return requesttype;
	}


	public int getUsernumber() {
		return usernumber;
	}


	public List<String> getChunkids() {
		return chunkids;
	}


	public String getBlockid() {
		return blockid;
	}


	public static Builder builder() {
		return new Builder();
	}

	


	public void setRequesttype(int requesttype) {
		this.requesttype = requesttype;
	}




	public void setUsernumber(int usernumber) {
		this.usernumber = usernumber;
	}




	public void setChunkids(List<String> chunkids) {
		this.chunkids = chunkids;
	}




	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}




	public static final class Builder {
		private int requesttype;
		private int usernumber;
		private List<String> chunkids = Collections.emptyList();
		private String blockid;

		private Builder() {
		}

		public Builder withRequesttype(int requesttype) {
			this.requesttype = requesttype;
			return this;
		}

		public Builder withUsernumber(int usernumber) {
			this.usernumber = usernumber;
			return this;
		}

		public Builder withChunkids(List<String> chunkids) {
			this.chunkids = chunkids;
			return this;
		}

		public Builder withBlockid(String blockid) {
			this.blockid = blockid;
			return this;
		}

		public RequestRecord build() {
			return new RequestRecord(this);
		}
	}




	public String getChunkid() {
		return chunkid;
	}




	public void setChunkid(String chunkid) {
		this.chunkid = chunkid;
	}
	
	

}
