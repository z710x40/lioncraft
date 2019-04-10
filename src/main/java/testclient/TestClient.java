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

import lioncraftserver.comobjects.ChunkListRecord;
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
		
		try {
			inet = InetAddress.getByName("192.168.178.21");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connect to server");
		InetSocketAddress socket = new InetSocketAddress(inet, port);

		try {

			System.out.println("Write data");
			SocketChannel channel = SocketChannel.open(socket);

			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(da);

			channel.write(ByteBuffer.wrap(bos.toByteArray()));

			ByteBuffer inBuffer=ByteBuffer.allocate(8192);
			int rc=channel.read(inBuffer);
			System.out.println("buffer "+rc);
			System.out.println("buffer "+inBuffer);
			
			
			byte[] tempbuffer=inBuffer.array();
			ByteArrayInputStream bis = new ByteArrayInputStream(tempbuffer);
			ObjectInputStream ois = new ObjectInputStream(bis);
			ChunkListRecord cr=(ChunkListRecord)ois.readObject();
			System.out.println("result "+cr.list);
			cr.list.forEach(chunk -> System.out.println(chunk));
			channel.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
