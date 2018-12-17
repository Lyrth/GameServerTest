package tl.ko.lyr.gameserver.handlers;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

import java.nio.ByteBuffer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class Handler {

    private Predicate<String> pred;

    public void onStart(){}
    public void onOpen(WebSocket s, ClientHandshake h){}
    public void onClose(WebSocket s, int code, String reason, boolean remote){}
    public void onMessage(WebSocket s, String message){}
    public void onMessage(WebSocket s, ByteBuffer message){}
    public void onError(WebSocket s, Exception e){}

    //public abstract ServerHandshakeBuilder onHandshake(WebSocket s, Draft draft, ClientHandshake request);

    public boolean accept(WebSocket s, Draft draft, ClientHandshake request){
        return true;
    }

    public Handler(){
        pred = Pattern.compile(".").asPredicate();
    }

    public Handler setMatcher(String regex){
        this.pred = Pattern.compile(regex).asPredicate();
        return this;
    }

    public boolean test(String input){
        return pred.test(input);
    }

}
