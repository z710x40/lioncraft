package com.sande.lioncraft.managers;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;

public class OrderManager {

	private Logger log = Logger.getLogger(this.getClass());
	private static OrderManager INSTANCE;
	
	public static OrderManager getInstance()
	{
		if(INSTANCE==null)
		{
			INSTANCE=new OrderManager();
		}
		return INSTANCE;
	}
	
	private OrderManager() {
		// TODO Auto-generated constructor stub
	}
	
	private ArrayDeque<RequestRecord> requestQueue=new ArrayDeque<>();
	
	public void putRequest(RequestRecord rr)
	{
		log.debug("Place request, number of chunks "+rr.getChunkids().size());
		requestQueue.add(rr);
	}
	

	public RequestRecord getReqeust()
	{
		try
		{
			return requestQueue.removeLast();
		}
		catch(NoSuchElementException e)
		{
			return null;
		}
	}
	
	
	private ArrayDeque<ChunkListRecord> chunklListQueue=new ArrayDeque<>();
	
	public void putChunkList(ChunkListRecord clr)
	{
		chunklListQueue.add(clr);
	}
	

	public ChunkListRecord getChunkList()
	{
		try
		{
			return chunklListQueue.removeLast();
		}
		catch(NoSuchElementException e)
		{
			return null;
		}
	}
	
	
	
	public void stats()
	{
		log.info("requestQueue size    :"+requestQueue.size());
		log.info("chunklListQueue size :"+chunklListQueue.size());
	}
	
}
