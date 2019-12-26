package testclient;

import java.net.InetAddress;
import com.sande.lioncraft.managers.ChunkFieldManager;
import com.sande.lioncraft.managers.networking.NetworkConnector;

import lioncraftserver.comobjects.RequestRecord;

public class TestClient {

	InetAddress inet;

	int port = 2016;
	
	RequestRecord rr=new RequestRecord();

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		TestClient client = new TestClient();

	}

	public TestClient() {

		ChunkFieldManager chm=new ChunkFieldManager(10);
		chm.setNewCenterAt(0, 0);
		
		NetworkConnector conn=NetworkConnector.getConnector();
		conn.connect("192.168.178.20", 2016);
		
		RequestRecord rr=new RequestRecord();
		
		
		
	}

}
