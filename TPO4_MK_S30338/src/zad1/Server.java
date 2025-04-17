/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import com.sun.org.apache.xerces.internal.util.DatatypeMessageFormatter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server {
    private String host;
    private int port;
    private String log;
    private Thread serverThread;
    private Selector selector;//do obslugi wielu kanalow w jednym watku
    private boolean running = false;
    private Map<SocketChannel,String> clientLogs = new HashMap<>();

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
        this.log = "=== Server log ===\n";
    }

    public void startServer() {
        this.running = true;
        serverThread = new Thread(() ->{
            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind( new InetSocketAddress(host,port));
                serverSocketChannel.configureBlocking(false); // dla nieblokowania klientow
                selector = Selector.open();
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //nasluchiwanie polaczen z uzyciem selectora, nieblokujaco

                while (running){
                    selector.select(); // oczekiwanie na wiadomosc do odczytu
                    Set<SelectionKey> keys = selector.selectedKeys(); // jesli ktos sie polaczyl, mozemy zaakceprowac, jesli wyslal, odczytac
                    Iterator<SelectionKey> iterator = keys.iterator(); //iterujemy po kluczach, kanalach z wydazeniami

                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();// usuwamy zeby zaraz nie byl znow przetwarzany

                        if (key.isAcceptable()){
                            handleAccpet(serverSocketChannel); //akceptowanie polaczenia, rejestracja klienta - kanalu do odczytu
                        }
                        else if (key.isReadable()){
                            handleRead(key);//odczytanie danych od klienta
                        }
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder request = new StringBuilder();

        int read = client.read(buffer);
        if (read == -1){
            client.close();
            return;
        }
        buffer.flip();
        while(buffer.hasRemaining())
            request.append((char) buffer.get());

        String requestStr = request.toString().trim();
        String response = processRequest(client, requestStr);

        if (response != null)
        {
            ByteBuffer outBuf = ByteBuffer.wrap(response.getBytes());
            client.write(outBuf);
        }
        client.close();
    }

    private String processRequest(SocketChannel client, String requestStr) {
        String clientLog = clientLogs.get(client);
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return "processRequest";
    }

    private void handleAccpet(ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        clientLogs.put(client, String.valueOf(new StringBuilder()));
    }

    public void stopServer() {
        running = false;
        if (selector != null){
            try{
                selector.wakeup(); //dla zebrania ostatniej wiadomosci ktora mogla wpasc
                selector.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (serverThread != null){
            try {
                serverThread.join(); //czeka az watek sie skonczy przed zamknieciem go
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void updateLog(String update){
        this.log += update;
    }
    public String getServerLog() {
        return this.log;
    }

}
