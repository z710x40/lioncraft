package com.sande.lioncraft.managers;

import java.util.ArrayList;

import java.util.List;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.RequestRecord;


public class NetworkManager {

	
	private static NetworkManager INSTANCE;
	NetworkConnector networkConnector=NetworkConnector.getConnector();
	OrderManager oderManager=OrderManager.getInstance();
	
	List<NetworkManagerRunner> runnerList=new ArrayList<>();
	int runners=4;
	int count;
	NetworkManagerRunner tempRunner;
	RequestRecord tempRecord;
	Chunk tempChunk;
	
	private NetworkManager() {
		for(int tel=0;tel<runners;tel++)
		{
			runnerList.add(new NetworkManagerRunner(tel));
		}
		
	}


	public static NetworkManager getInstance()
	{
		if(INSTANCE==null)
		{
			INSTANCE=new NetworkManager();
		}
		return INSTANCE;
	}


	public void checkForNewOrders()
	{
		
		for(count=0;count<runners;count++)
		{
			tempRunner=runnerList.get(count);
			if(!tempRunner.isRunning())
			{
				tempRecord=oderManager.getRequestRecord();
				if(tempRecord==null)return;
				tempRunner.setRequestRecord(oderManager.getRequestRecord());
				tempRunner.start();
				continue;
			}
			if(tempRunner.isFinished())
			{
				tempChunk=tempRunner.getChunk();
				oderManager.addNewChunk(tempChunk.getChunkid(), tempChunk);
			}
		}
		
		
		
	}
	
	
	
	
}
