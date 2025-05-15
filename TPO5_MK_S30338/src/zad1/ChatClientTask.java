/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<String> implements Runnable {
    private ChatClient client;
    private List<String> messages;
    private int wait;

    private static class InternalCallable implements Callable<String>{
        private ChatClient client;
        private List<String> messages;
        private int wait;
        public InternalCallable(ChatClient c, List<String> messages, int wait){
            this.client = c;
            this.messages = messages;
            this.wait = wait;
            System.out.println("ChatClientTask -> InternalCallable -> (30) " + wait +" "+ client +" "+ messages +"ok");
        }

        @Override
        public String call() throws Exception {
            client.connect();
            client.send("LOGIN " + client.getId());
            System.out.println("ChatClientTask -> InternalCallable -> call -> (37) " + "LOGIN " );
            for (String m: messages){
                System.out.println("ChatClientTask -> InternalCallable -> call -> (39) " + m );
                String mess = client.send(m);
            }
            System.out.println("ChatClientTask -> InternalCallable -> call -> (42) " + "LOGOUT " );
            return client.send("LOGOUT " + client.getId());
        }
    }

    public ChatClientTask(ChatClient c, List<String> msgs, int wait) {
        super(new InternalCallable(c,msgs,wait));
        this.client = c;
        this.messages = msgs;
        this.wait = wait;
        System.out.println("ChatClientTask -> ChatClientTask -> (48) " + wait +" "+ client +" "+ messages +"ok");
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c,msgs,wait);
    }

    @Override
    public void run() {
        client.setClientThread(Thread.currentThread());
        System.out.println("ChatClientTask -> run -> (61) ok" );
        try {
            client.connect();
            System.out.println("ChatClientTask -> run -> (64) ok" );
            client.send("LOGIN " + client.getId());
            System.out.println("ChatClientTask -> run -> (66) ok" );
            if (wait >0) {
                System.out.println("ChatClientTask -> run -> (68) ok" );
                Thread.sleep(wait);
            }
            for (String m: messages){
                client.send(m);
                System.out.println("ChatClientTask -> run -> (73) ok" );
                if(wait>0) Thread.sleep(wait);
            }
            client.send("LOGOUT " + client.getId());
            System.out.println("ChatClientTask -> run LOGOUT -> (78) ok" );
            if (wait>0) Thread.sleep(wait);
            // zatrzymanie watku chatClientTask

            client.getClientThread().interrupt();

        } catch (InterruptedException e) {
            client.appendToChatView(e.toString());
        }
    }

    public ChatClient getClient() {
        return client;
    }
}
