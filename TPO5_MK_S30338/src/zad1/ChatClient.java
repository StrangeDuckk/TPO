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
    private Thread clientThread;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        System.out.println("chatClient -> utworzono klienta "+host+" " + port + " " + id+"ok");
    }

    public String getChatView() {
        return "=== " + id + " chat view\n" + chatView.toString();
    }
    public String getId() {
        System.out.println("chatClient -> getId -> " + id);
        return this.id;
    }

    public void connect() {
        System.out.println("chatClient -> connect -> przed trycatchem ok");
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host,port));
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            System.out.println("chatClient -> connect -> po trycatchu przed thread.sleep (51) ok" );
            while(!socketChannel.finishConnect()){
                Thread.sleep(1);
            }

            System.out.println("chatClient -> connect -> po trycatchu po thread.sleep (56) ok" );
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
                                    System.out.println("chatClient -> connect -> zamkniecie socketchsannel (73) ok" );
                                    sc.close(); //wylogowanie pojedynczego klienta
                                    System.out.println("STAN TABELI KLIENTOW: "+ChatServer.getLoggedClients());

                                }
                                byteBuffer.flip();
                                String response = new String(byteBuffer.array(), 0, byteBuffer.limit()).trim();
                                synchronized (chatView){
                                    appendToChatView(response);
                                }

                                if (response.contains("LOGOUT")) {
                                    try {
                                        socketChannel.close(); // to zakończy działanie tego wątku
                                        System.out.println("udalo sie zamknac kanal, 86");
                                    } catch (IOException e) {
                                        appendToChatView("*** " + e);
                                    }
                                    System.out.println("WYJSCIE Z PETLI NASLUCHUJACEJ 90");
                                    break; // wyjście z pętli nasłuchującej
                                }
                            }
                        }

                        System.out.println("is empty: " + ChatServer.getLoggedClients().isEmpty());
                        if (ChatServer.getLoggedClients().isEmpty()){
                            socketChannel.close();
                            this.stopClient();
                            break;
                        }

                    } catch (IOException e) {
                        appendToChatView("*** " + e.toString());
                    }

                }
            });
            thread.setDaemon(true); //dla nie blokowania dzialania programu po zamknieciu innych watkow, te wykonaja swoja prace

            System.out.println("chatClient -> connect -> thread start (92) ok" );
            thread.start();
        } catch (IOException | InterruptedException e) {
            appendToChatView("*** " + e.toString());
        }
    }
    public void setClientThread(Thread t) {
        this.clientThread = t;
    }
    public void stopClient() {
        if (clientThread != null) {
            clientThread.interrupt(); // zatrzymuje sleep i przerywa run()
        }

        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            appendToChatView("*** " + e);
        }
        System.out.println("udalo sie zatrzymac klienta !!!!!!!!!!!!");
    }

    public String send(String message){
        System.out.println("chatClient -> send -> (100) ok" );
        try{
            ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
            socketChannel.write(buffer);
            return message;
        } catch (IOException e) {
            appendToChatView("*** " + e);
            return null;
        }
    }

    public void appendToChatView(String e) {// todo wiadomosc jest dodawana potrojnie
        System.out.println("chatClient -> appendToChatView -> (112) "+ e  + "ok");
        chatView.append("*** ").append(e).append("\n");
    }

    public Thread getClientThread() {
        return thread;
    }
}
