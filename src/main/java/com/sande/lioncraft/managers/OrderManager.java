package com.sande.lioncraft.managers;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.NoSuchElementException;
import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.RequestRecord;


/**
 * Class to queue all the orders for the lioncraft server
 * @author Erwin
 *
 */
public class OrderManager {

	
	private static OrderManager INSTANCE;
		
	
	
	private OrderManager() {
		
	}

	
	public static OrderManager getInstance()
	{
		if(INSTANCE==null)
		{
			INSTANCE=new OrderManager();
		}
		return INSTANCE;
	}
	
	
	// Reqeust records
	
	private ArrayDeque<RequestRecord> requestList=new ArrayDeque<>();
	
	public void putRequestRecord(RequestRecord r)
	{
		requestList.add(r);
	}
	
	public RequestRecord getRequestRecord()
	{
		try{
			return requestList.removeLast();
		}catch(NoSuchElementException e)
		{
			return null;
		}
	}
	
	
	
	// Chunk received records
	private HashMap<String,Chunk> chunkResultList=new HashMap<>();
	
	public void addNewChunk(String id,Chunk chunk)
	{
		chunkResultList.put(id, chunk);
	}
	
	public Chunk getChunk(String id)
	{
		return  chunkResultList.remove(id);
		
	}
	
	
	
	
	
}















