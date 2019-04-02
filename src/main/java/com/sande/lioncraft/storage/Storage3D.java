package com.sande.lioncraft.storage;

public class Storage3D<T> {

	
	private T[][][] storage;
	private int size;
	
	@SuppressWarnings("unchecked")
	public Storage3D(int size) {
		storage=(T[][][])new Object[size][size][size];
		
	}
	
	
	public void add(T newObj,int x,int y,int z)
	{
		if(x<size && y<size && z<size)
		{
			storage[x][y][z]=newObj;
		}
	}
	
	public T get(T newObj,int x,int y,int z)
	{
		if(x<size && y<size && z<size)
		{
			return storage[x][y][z];
		}
		
		return null;
	}
	

}
