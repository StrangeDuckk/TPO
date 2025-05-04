/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private String host;
    private int port;
    private final StringBuilder serverLog = new StringBuilder();
    private Thread serverThread;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private boolean isRunning;
    private List<SocketChannel> loggedClients = Collections.synchronizedList(new ArrayList<>());
    private Map<SocketChannel, String> clients = new ConcurrentHashMap<>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        this.serverThread = new Thread(()->{
            try {
                selector = Selector.open();
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(host,port));
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                isRunning = true;

                System.out.println("Server started");

                while (isRunning){
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();

                    while (it.hasNext()){
                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isAcceptable()){
                            SocketChannel client = serverSocketChannel.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            int read = client.read(bb);
                            if (read == -1){
                                loggedClients.remove(client);
                                clients.remove(client);
                                client.close();
                            }
                            else{
                                bb.flip();
                                String message = new String(bb.array(),0,bb.limit()).trim();
                                
                                if (message.startsWith("LOGIN")){
                                    String id = message.substring(6);
                                    clients.put(client,id);
                                    loggedClients.add(client);
                                    broadcast(id + " logged in");
                                    logAppend(id + " logged in");
                                } else if (message.startsWith("LOGOUT")) {
                                    String id = clients.get(client);
                                    broadcast(id + " logged out");
                                    logAppend(id + " logged out");
                                    loggedClients.remove(client);
                                    clients.remove(client);
                                    client.close();
                                }
                                else{
                                    broadcast(clients.get(client) + ": " + message);
                                    logAppend(clients.get(client) + ": " + message);
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        serverThread.start();
    }

    private void broadcast(String s) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap((s+"\n").getBytes());
        synchronized (loggedClients){
            for (SocketChannel sc: loggedClients){
                sc.write(bb.duplicate());
            }
        }
    }
    private void logAppend(String s) {
        synchronized (serverLog){
            String time = simpleDateFormat.format(new Date());
            serverLog.append(time).append(" ").append(s).append("\n");
        }
    }

    public void stopServer() {
        isRunning = false;
        serverThread.interrupt();
        try {
            selector.close();
            serverSocketChannel.close();
            for (SocketChannel sc : loggedClients)
                sc.close();
        } catch (IOException e) {
            System.out.println("*** "+e.toString());
        }
        System.out.println("Server stopped");
    }

    public String getServerLog() {
        synchronized (serverLog) {
            return serverLog.toString();
        }
    }
}
