/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> implements Runnable {

    private final Client client;
    private final List<String> reqs;
    private final boolean showSendRes;

    private static class InternalCallable implements Callable<String> {
        private final Client client;
        private final List<String> reqs;
        private final boolean showSendRes;

        public InternalCallable(Client client, List<String> reqs, boolean showSendRes) {
            this.client = client;
            this.reqs = reqs;
            this.showSendRes = showSendRes;
        }

        @Override
        public String call() {
            client.connect();
            client.send("login " + client.getId());
            for (String r : reqs) {
                String res = client.send(r);
                if (showSendRes) System.out.println(res);
            }
            return client.send("bye and log transfer");
        }
    }

    private ClientTask(Client c, List<String> reqs, boolean showSendRes) {
        super(new InternalCallable(c, reqs, showSendRes));
        this.client = c;
        this.reqs = reqs;
        this.showSendRes = showSendRes;
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes) {
        return new ClientTask(c, reqs, showSendRes);
    }
}