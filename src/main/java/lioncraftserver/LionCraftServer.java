package lioncraftserver;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Set;


import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.PreRecord;
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
		wg.testWorld();
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
			
			System.out.println("Ready to accept incoming connections");

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	
	private void runServer() {
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
						continue;
					}

					if (key.isAcceptable()) {
						accept(key);

					} else if (key.isReadable()) {
						RequestRecord ra=read(key);
						if(ra!=null)
						{
							switch(ra.getRequesttype())
							{
								case 1: write(key,processor.getChunksFromList(ra.getChunkids()));
										break;
								case 2: processor.addNewBlock(ra.getBlockid(), ra.getBlockType());
								        break;
							}
						}
						

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		SocketChannel channel=(SocketChannel) key.channel();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			PreRecord prerecord=new PreRecord(record);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(prerecord);
			
			
			
			int rc=channel.write(ByteBuffer.wrap(bos.toByteArray()));
			//System.out.println("Write "+rc+" bytes");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
	private RequestRecord read(SelectionKey key) throws IOException {
		RequestRecord ra=null;
		ByteBuffer readBuffer = ByteBuffer.allocate(8192);
		SocketChannel readSocket = (SocketChannel) key.channel();
		int readbytes = 0;
		try {
			readbytes = readSocket.read(readBuffer);
		} catch (IOException e) {

			System.out.println("Cannot read from socket");
			key.cancel();
			readSocket.close();
			e.printStackTrace();
			return ra;
		}

		if (readbytes == -1) {
			System.out.println("End of record, close socket");
			key.channel().close();
			key.cancel();
			return ra;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(readBuffer.array());
		ObjectInputStream ois = new ObjectInputStream(bis);
		try {
			ra = (RequestRecord) ois.readObject();
			//System.out.println("ra " + ra.getRequesttype() + " ra " + ra.getUsernumber());
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ra;
	}



	private void accept(SelectionKey key) {
		ServerSocketChannel newServerSocketChannel = (ServerSocketChannel) key.channel();

		try {
			SocketChannel socketChannel = newServerSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			System.out.println("Connection accepted from "+socketChannel.getRemoteAddress());
		} catch (IOException e) {
			System.out.println("Failed to select the incoming selection");
			e.printStackTrace();
		}

	}
    
	
	
	
}
