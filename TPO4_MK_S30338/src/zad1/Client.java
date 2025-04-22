/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private String host;
    private int port;
    private String id;
    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect() {//todo wszyscy powinni sie laczyc w tym samym momencie
        try {//do ewentualnej poprawy na 3 termin
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
            channel.configureBlocking(false); //dla dzialania rownoleglego dla klientow
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String req) {
        try {
            channel.write(ByteBuffer.wrap((req + "\n").getBytes()));
            buffer.clear();

            while (channel.read(buffer) == 0){
                Thread.sleep(10); //chwila oczekiwania na odczyt jako buffer
            }

//            int read = channel.read(buffer);
            return new String(buffer.array(), 0, buffer.position()).trim()+"\n";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getId(){
        return id;
    }
}
