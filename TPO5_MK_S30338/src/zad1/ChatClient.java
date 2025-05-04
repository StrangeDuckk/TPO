/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ChatClient {
    private String host;
    private int port;
    private String id;
    private StringBuilder chatView = new StringBuilder();

    private SocketChannel socketChannel;
    private Selector selector;
    private Thread thread;
    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public String getChatView() {
        return "=== " + id + " chat view\n" + chatView.toString();
    }
    public String getId() {
        return this.id;
    }

    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host,port));

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            while(!socketChannel.finishConnect()){
                Thread.sleep(1);
            }

            thread = new Thread(()->{
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while(socketChannel.isOpen()){
                    try {
                        selector.select();
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while(iterator.hasNext()){
                            SelectionKey key = iterator.next();
                            iterator.remove();

                            if(key.isReadable()){
                                SocketChannel sc = (SocketChannel) key.channel();
                                byteBuffer.clear();
                                int message = sc.read(byteBuffer);
                                if (message == -1){
                                    sc.close();
                                    continue;
                                }
                                byteBuffer.flip();
                                String response = new String(byteBuffer.array(), 0, byteBuffer.limit()).trim();
                                synchronized (chatView){
                                    appendToChatView(response);
                                }
                            }
                        }
                    } catch (IOException e) {
                        appendToChatView("*** " + e.toString());
                    }

                }
            });
            thread.setDaemon(true); //dla nie blokowania dzialania programu po zamknieciu innych watkow, te wykonaja swoja prace
            thread.start();
        } catch (IOException | InterruptedException e) {
            appendToChatView("*** " + e.toString());
        }
    }

    public String send(String message){
        try{
            ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
            socketChannel.write(buffer);
            return message;
        } catch (IOException e) {
            appendToChatView("*** " + e);
            return null;
        }
    }

    public void appendToChatView(String e) {
        chatView.append("*** ").append(e).append("\n");
    }
}
