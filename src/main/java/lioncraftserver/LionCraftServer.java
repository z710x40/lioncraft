package lioncraftserver;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import org.apache.log4j.Logger;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;
import lioncraftserver.tools.Processors;
import lioncraftserver.tools.WorldGenerator;

public class LionCraftServer {

	
	Selector selector ; // selector is open here
	ServerSocketChannel lionSrvSocketChannel;
	InetSocketAddress lionSrvSocketAddr;
	ServerSocket lionServerSocket;
	ByteBuffer myBuffer = ByteBuffer.allocate(8192);
	InetAddress adres;
    int port;
    ServerSocketChannel serverSocketChannel;
    Processors processor=new Processors();					// processor processes incoming requests
   // ChunkStorage chunkStorage;
   
    private Logger log = Logger.getLogger(this.getClass());
    
    

    boolean runflag=true;
	
	public LionCraftServer() throws IOException {
		port=2016;
		adres=InetAddress.getLocalHost();
	}
	
	public static void main(String argv[]) throws IOException, ClassNotFoundException
	{
		
		
		LionCraftServer lioncraftserver=new LionCraftServer();
		lioncraftserver.initDatabase();							// Start the databases
		lioncraftserver.initServer();							// Start the server
		lioncraftserver.runServer();							// Start listening for connections and handle them
		
	}


	
	private void initDatabase() {
		//chunkStorage=ChunkStorage.getChunkStorage();
		WorldGenerator wg=new WorldGenerator();
		//wg.randomWorld();
		wg.randomWorld();
		wg.WorldFromCSV("D:\\erwin\\JMonkey\\unix_systems.csv");
	}

	
	
	private void initServer() 
	{
		try {
			selector = SelectorProvider.provider().openSelector();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			InetSocketAddress inet = new InetSocketAddress(adres, port);
			serverSocketChannel.socket().bind(inet);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			log.info("Server ready listen on "+adres.getHostAddress()+" at port "+port);
			log.info("Ready to accept incoming connections");

		} catch (IOException e) {
			log.info("IOException "+e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	private void runServer() {
		log.info("Start the lioncrate server process");
		while (runflag) {
			int rc = 0;
			try {
				rc = selector.select();
				// System.out.println("there are "+rc+" selectors");

				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						log.debug("Invalid key "+key);
						continue;
					}

					if (key.isAcceptable()) {
						log.debug("key is accepted");
						accept(key);

					} else if (key.isReadable()) {
						RequestRecord ra=read(key);
						if(ra!=null)
						{
							log.debug("Received request type "+ra.getRequesttype());
							switch(ra.getRequesttype())
							{
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
							}
						}
						

					}
				}

			} catch (IOException e) {
				log.info("IOException "+e.getMessage());
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Write an object to the client
	 * @param key
	 * @param chunksFromList
	 */
	private void write(SelectionKey key,Object record) {
		log.debug("Write object to client system ");
		SocketChannel channel=(SocketChannel) key.channel();
		if(!channel.isOpen())
		{
			log.debug("Error, channel is closed");
			return;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ChunkListRecord chunk=(ChunkListRecord) record;
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(chunk);
			int rc=channel.write(ByteBuffer.wrap(bos.toByteArray()));
			log.debug("Write "+rc+" bytes");
		} catch (IOException e) {
			log.debug("IOException "+e.getMessage());
			e.printStackTrace();
		}
		
	}

	
	
	private RequestRecord read(SelectionKey key) throws IOException {
		log.debug("Read data from the network");
		RequestRecord ra=null;
		ByteBuffer readBuffer = ByteBuffer.allocate(8192);
		SocketChannel readSocket = (SocketChannel) key.channel();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int readbytes = 0;
		int rc=0;
		
		try {
			while((rc=readSocket.read(readBuffer))>0)
			{
				log.debug("Received bytes "+rc);
				baos.write(readBuffer.array());
				readbytes+=rc;
				readBuffer.clear();
			}
		} catch (IOException e) {

			log.info("Cannot read from socket");
			//key.cancel();
			readSocket.close();
			e.printStackTrace();
			baos.close();
			return null;
		}

		if (readbytes == -1) {
			log.info("End of record, close socket");
			//key.channel().close();
			//key.cancel();
			baos.close();
			return null;
		}
		log.debug("Object received size is "+readbytes);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object readedObject = null;
		try {
			readedObject = ois.readObject();
		} catch (ClassNotFoundException e) {
			log.debug("ClassNotFoundException received is "+e.getMessage());
			e.printStackTrace();
			key.channel().close();
			//key.cancel();
			baos.close();
		}
		
		if(readedObject instanceof RequestRecord)
		{
			log.debug("Object received is instance of RequestRecord");
			ra = (RequestRecord) readedObject;
			//key.channel().close();
			//key.cancel();
			baos.close();
			return ra;
		}
		else
		{log.debug("Object received is invalid"+readedObject);
		}
		//wwwwwwwkey.channel().close();
		//key.cancel();
		baos.close();
		return null;
	}



	private void accept(SelectionKey key) {
		ServerSocketChannel newServerSocketChannel = (ServerSocketChannel) key.channel();

		try {
			SocketChannel socketChannel = newServerSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			log.info("Connection accepted from "+socketChannel.getRemoteAddress());
		} catch (IOException e) {
			log.info("Failed to select the incoming selection");
			e.printStackTrace();
		}

	}
	
	
   
	
	
}
