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
import java.util.Set;

public class Client {
    private String host;
    private int port;
    private String id;
    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);
    private Selector selector;


    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect(){
        try {
            selector = Selector.open();
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host,port));

            channel.register(selector, SelectionKey.OP_CONNECT);

            while (true){
                selector.select(50);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while(iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isConnectable()){
                        finishConnection(key);
                        return;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finishConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnectionPending())
            socketChannel.finishConnect();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_WRITE);
    }

    //    public void connect() {//todo wszyscy powinni sie laczyc w tym samym momencie
//        try {//do ewentualnej poprawy na 3 termin
//            channel = SocketChannel.open();
//            channel.connect(new InetSocketAddress(host, port));
//            channel.configureBlocking(false); //dla dzialania rownoleglego dla klientow
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String send(String req) {
//        try {
//            channel.write(ByteBuffer.wrap((req + "\n").getBytes()));
//            buffer.clear();
//
//            while (channel.read(buffer) == 0){
//                Thread.sleep(10); //chwila oczekiwania na odczyt jako buffer
//            }
//
////            int read = channel.read(buffer);
//            return new String(buffer.array(), 0, buffer.position()).trim()+"\n";
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public String getId(){
        return id;
    }

    public String send(String request) {
        buffer.clear();
        try {
            channel.write(ByteBuffer.wrap((request+"\n").getBytes()));
            channel.register(selector,SelectionKey.OP_READ);

            while (true){
                selector.select(50);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer bufferLocal = ByteBuffer.allocate(1024);
                        int bytesRead = socketChannel.read(bufferLocal);

                        if (bytesRead > 0){
                            bufferLocal.flip();
                            byte[] data = new byte[bufferLocal.remaining()];
                            bufferLocal.get(data);
                            return new String(data).trim()+"\n";
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
