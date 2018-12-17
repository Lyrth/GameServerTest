package tl.ko.lyr.gameserver;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*
        WebSocketServer server = new Server(new InetSocketAddress("127.0.0.1",8888));
        server.setTcpNoDelay(true);
        server.setReuseAddr(true);
        server.run();
        */

        Server server = new Server();
        server
            .addHandler(new Handler0(),"test1")
            .addHandler("/lb", new LbHandler())
            .addHandler("/players", new PlayerHandler())
            //.removeHandler("test1") // yep, working
            .run();

    }

    private static void thinger(){
        int a = 1;
        a++;
    }

    public static void thing(){
        List<Mono<Integer>> monoList = new ArrayList<>();
        for (int k=0;k<5;k++) {
            final int j = k;
            final long a = (long) Math.floor(Math.random()*5);
            final long b = (long) Math.floor(Math.random()*5);
            monoList.add(Mono.just(j)
                .delayElement(Duration.ofSeconds(a))
                .map(i -> {
                    System.out.println("Iter "+j+" First: "+i+" at "+a);
                    return i + 2;
                })
                .delayElement(Duration.ofSeconds(b))
                .map(i -> {
                    System.out.println("Iter "+j+" Second: "+i+" at "+(a+b));
                    return i;
                }));
        }
        Mono.when(monoList).block();
    }

}