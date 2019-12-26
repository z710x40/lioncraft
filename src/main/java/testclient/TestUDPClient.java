package testclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;

public class TestUDPClient {

	

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
	{
		TestUDPClient tudp=new TestUDPClient();
		
	}
	
	
	public TestUDPClient() throws IOException, InterruptedException, ClassNotFoundException {

		DatagramSocket clientsocket=new DatagramSocket();
		InetAddress addr=InetAddress.getByName("192.168.178.23");
		
		
		
		List<String> chunkids=new ArrayList();
		chunkids.add("0X0");
		chunkids.add("1X0");
		chunkids.add("2X0");
		chunkids.add("3X0");
		chunkids.add("4X0");
		
		
		RequestRecord rr=new RequestRecord();
		rr.setRequesttype(1);
		rr.setChunkids(chunkids);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(bos);
		oos.writeObject(rr);
		
		byte[] sendbuffer=bos.toByteArray();
		
		int bytesreaded=0;
		DatagramPacket SDGP=new DatagramPacket(sendbuffer,sendbuffer.length,addr,2016);
		clientsocket.send(SDGP);
		
		System.out.println("All send");
		
		
		byte[] recvbuffer=new byte[516];
		DatagramPacket RDGP=new DatagramPacket(recvbuffer,recvbuffer.length);
		bos = new ByteArrayOutputStream();
		int bytesread=0;
		while(bytesread!=-1)
		{
			System.out.println("Wait for data");
			clientsocket.receive(RDGP);
			bytesread=RDGP.getLength();
			System.out.println("received bytes is "+bytesread);
			
			byte[] recvbuf=RDGP.getData();
			System.out.println("record type is "+recvbuf[0]);
			bos.write(recvbuf,4,bytesread-4);
			if(recvbuf[0]==0)
			{
				System.out.println("Endrecord received");
				
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());	// Maak een inputstream op basis van de outputstream
				ObjectInputStream ois = new ObjectInputStream(bis);
				ChunkListRecord chunklist=(ChunkListRecord)ois.readObject();
				System.out.println("Recieved "+chunklist.list.toString());
				return;
			}
			
		}
		
		
	}

}
