package testclient;

import lioncraftserver.tools.Tools;

public class Mocktest {

	public Mocktest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println(" 0:0 "+Tools.getChunkId(0,0));
		System.out.println(" 1:1 "+Tools.getChunkId(1,1));
		System.out.println(" 10:10 "+Tools.getChunkId(10,10));
		System.out.println(" -1:1 "+Tools.getChunkId(-1,1));
		System.out.println(" -1,-1 "+Tools.getChunkId(-1,-1));
		System.out.println(" -10:-10 "+Tools.getChunkId(-10,-10));
		System.out.println(" -20:-20 "+Tools.getChunkId(-20,-20));
		
		
	}

}
