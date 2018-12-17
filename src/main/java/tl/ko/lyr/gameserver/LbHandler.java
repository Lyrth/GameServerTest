package tl.ko.lyr.gameserver;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;
import tl.ko.lyr.gameserver.handlers.Handler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class LbHandler extends Handler {

    public void onStart(){

    }

    public void onOpen(WebSocket s, ClientHandshake h){
        //s.send("Hi from " + this.getClass().getName());
    }

    public void onClose(WebSocket s, int code, String reason, boolean remote){

    }

    public void onMessage(WebSocket s, String m){
        //s.send("From " + this.getClass().getName() + ", you said " + m);
        s.send("Leaderboards:");
    }

    public void onMessage(WebSocket s, ByteBuffer m){
        s.send("Bytes!!!");
    }

    public void onError(WebSocket s, Exception e){

    }
}
