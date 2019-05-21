package testclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.sande.lioncraft.managers.NetworkConnector;

import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.PreRecord;
import lioncraftserver.comobjects.RequestRecord;

public class TestClient {

	InetAddress inet;

	int port = 2016;

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		TestClient client = new TestClient();

	}

	public TestClient() {

		List<String> testlist=new ArrayList<>();
		testlist.add("0X0");
		testlist.add("0X1");
		testlist.add("0X2");
		testlist.add("0X3");
		
		RequestRecord da = RequestRecord.builder().withRequesttype(1).withUsernumber(1033).withChunkids(testlist).build();
		
		NetworkConnector conn=NetworkConnector.getConnector();
		if(!conn.connect("192.168.178.21", 2016))
		{
		 System.out.println("Cannot connect to the lioncraft server");;
		};
		
		conn.writeRecord(da);
		
		Object readed=conn.readRecord();
		if(readed instanceof lioncraftserver.comobjects.ChunkListRecord)
		{
			System.out.println("Chunklistrecord");
		}
		else
		{
			System.out.println("no Chunklistrecord "+readed.getClass().getName());
			System.out.println("expected name "+lioncraftserver.comobjects.ChunkListRecord.class.getName());
		}
		
		
	}

}
