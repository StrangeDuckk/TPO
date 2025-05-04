/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
        }

        @Override
        public String call() throws Exception {
            client.connect();
            client.send("LOGIN " + client.getId());
            for (String m: messages){
                String mess = client.send(m);
            }
            return client.send("LOGOUT " + client.getId());
        }
    }

    public ChatClientTask(ChatClient c, List<String> msgs, int wait) {
        super(new InternalCallable(c,msgs,wait));
        this.client = c;
        this.messages = msgs;
        this.wait = wait;
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c,msgs,wait);
    }

    @Override
    public void run() {
        try {
            client.connect();
            client.send("LOGIN " + client.getId());
            if (wait >0) {
                    Thread.sleep(wait);
            }
            for (String m: messages){
                client.send(m);
                if(wait>0) Thread.sleep(wait);
            }
            client.send("LOGOUT " + client.getId());
            if (wait>0) Thread.sleep(wait);
        } catch (InterruptedException e) {
            client.appendToChatView(e.toString());
        }
    }

    public ChatClient getClient() {
        return client;
    }
}
