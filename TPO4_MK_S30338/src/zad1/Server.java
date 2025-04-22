/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private String host;
    private int port;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean running = false;
    private final Map<SocketChannel, String> clientIds = new HashMap<>();
    private final Map<String, List<String>> clientLogs = new ConcurrentHashMap<>();
    private final StringBuilder serverLog = new StringBuilder();

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.bind(new InetSocketAddress(host, port));
                serverChannel.configureBlocking(false);
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                running = true;

                while (running) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        if (key.isAcceptable()) {
                            handleAccept();
                        } else if (key.isReadable()) {
                            handleRead(key);
                        }
                    }
                }
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopServer() {
        running = false;
        try {
            if (selector != null) selector.wakeup();
            if (serverChannel != null) serverChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerLog() {
        return serverLog.toString();
    }

    private void handleAccept() throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
    }

    private void handleRead(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        try {
            int read = client.read(buffer);
            if (read == -1) {
                client.close();
                return;
            }

            buffer.flip();
            String msg = new String(buffer.array(), 0, buffer.limit()).trim();
            buffer.clear();

            String response = process(client, msg);
            client.write(ByteBuffer.wrap((response + "\n").getBytes()));
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String process(SocketChannel client, String msg) {
        String time = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        String clientId = clientIds.getOrDefault(client, "unknown");

        if (msg.startsWith("login")) {
            String id = msg.split(" ")[1];
            clientIds.put(client, id);
            clientLogs.putIfAbsent(id, new ArrayList<>());
            clientLogs.get(id).add("logged in");
            serverLog.append(id).append(" logged in at ").append(time).append("\n");
            return "logged in";
        }

        if (msg.equals("bye")) {
            clientLogs.get(clientId).add("logged out");
            serverLog.append(clientId).append(" logged out at ").append(time).append("\n");
            return "logged out";
        }

        if (msg.equals("bye and log transfer")) {
            clientLogs.get(clientId).add("logged out");
            serverLog.append(clientId).append(" logged out at ").append(time).append("\n");
            StringBuilder log = new StringBuilder();
            log.append("=== ").append(clientId).append(" log start ===\n");
            for (String line : clientLogs.get(clientId)) {
                log.append(line).append("\n");
            }
            log.append("=== ").append(clientId).append(" log end ===\n");
            return log.toString();
        }

        String result = Time.passed(msg.split(" ")[0],msg.split(" ")[1]);
        clientLogs.get(clientId).add("Request: " + msg + "\nResult:\n" + result);
        serverLog.append(clientId).append(" request at ").append(time).append(": \"").append(msg).append("\"\n");
        return result;
    }
}