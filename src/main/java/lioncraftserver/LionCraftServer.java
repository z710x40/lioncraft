package lioncraftserver;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Logger;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;
import lioncraftserver.tools.Processors;
import lioncraftserver.tools.WorldGenerator;

public class LionCraftServer {

	
	InetAddress adres;
    int port;
    
    Processors processor=new Processors();					// processor processes incoming requests
   // ChunkStorage chunkStorage;
   
    private Logger log = Logger.getLogger(this.getClass());
    private DatagramSocket serverUDPSocket;
    

    boolean runflag=true;
	
	public LionCraftServer() throws IOException {
		port=2016;
		adres=InetAddress.getLocalHost();
		
		serverUDPSocket=new DatagramSocket(port);
	}
	
	public static void main(String argv[]) throws IOException, ClassNotFoundException
	{
		LionCraftServer lioncraftserver=new LionCraftServer();
		lioncraftserver.initDatabase();			// Start the databases
		lioncraftserver.runUDPServer();			// Start de UDP server
	}


	
	

	private void initDatabase() {
		//chunkStorage=ChunkStorage.getChunkStorage();
		WorldGenerator wg=new WorldGenerator();
		//wg.randomWorld();
		wg.randomWorld();
		wg.WorldFromCSV("D:\\erwin\\JMonkey\\unix_systems.csv");
	}

	
	
	
	
	
	private void runUDPServer() {

		
		log.info("Start the UDP server");
		byte[] receiveData = new byte[16*1024];
		boolean endofrecord=false;
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		while (runflag) {
			try {
				
				endofrecord=false;
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				while(!endofrecord)
				{
					serverUDPSocket.receive(receivePacket);
					receiveData= receivePacket.getData();
					if(receiveData[0]==0)endofrecord=true;
					baos.write(receiveData, 4, receiveData.length-4);
				}
				
				
				ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
				baos.close();
				ObjectInputStream ois = new ObjectInputStream(bis);
				RequestRecord readedObject = (RequestRecord) ois.readObject();
				log.debug("Reqeust type is " + readedObject.getRequesttype());
				ois.close();
				bis.close();
				
				log.debug("Send response, Chunk is " + readedObject.getChunkids());
				writeUDP(receivePacket.getAddress(),receivePacket.getPort(),processor.getChunksFromList(readedObject.getChunkids()));
				
				
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	

							//switch(ra.getRequesttype())
							/*{
								case 1: log.debug("Process Request type 1");
									    write(key,processor.getChunksFromList(ra.getChunkids()));
										break;
								case 2: processor.addNewBlock(ra.getBlockid(), ra.getBlockType());
								        break;
								case 3: processor.delBlock(ra.getBlockid(),ra.getChunkid());
								        break;
								case 4: log.debug("Get chunk "+ra.getChunkid());
									    write(key,processor.getSingleChunk(ra.getChunkid()));
										break;
							}*/


	
	/**
	 * Write an object to the client
	 * @param key
	 * @param chunksFromList
	 */
	private void writeUDP(InetAddress requesterAddr,int requesterPort, ChunkListRecord chunk) {
		log.debug("Write object to client system ");
		int datasize=512;
		int metadatasize=4;

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(baos);
			oos.writeObject(chunk);
			byte[] sendData = new byte[metadatasize+datasize];
			DatagramPacket sendpackage=new DatagramPacket(sendData,sendData.length,requesterAddr,requesterPort);
			ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
			int bytesreaded=0;
			boolean loopflag=true;
			while(loopflag)
			{
				bytesreaded=bais.read(sendData,metadatasize,datasize);
				if(bytesreaded<datasize)
					{sendData[0]=0;						// Dit is een eind record
					loopflag=false;
					}
				else 
					{sendData[0]=1;											// record heeft een vervolg
					}
				
				sendpackage.setData(sendData);
				serverUDPSocket.send(sendpackage);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("Data written, total is "+chunk.list.size());

	}
		

}
