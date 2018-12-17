package tl.ko.lyr.gameserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import tl.ko.lyr.gameserver.handlers.Handler;

import java.nio.ByteBuffer;

public class PlayerHandler extends Handler {

    public void onStart(){

    }

    public void onOpen(WebSocket s, ClientHandshake h){
        s.send("Hi from " + this.getClass().getName());
    }

    public void onClose(WebSocket s, int code, String reason, boolean remote){

    }

    public void onMessage(WebSocket s, String m){
        //s.send("From " + this.getClass().getName() + ", you said " + m);
        String name = Util.splitQuery(s.getResourceDescriptor()).get("name");
        if (name==null) name = "";
        s.send("ID: " + name);
    }

    public void onMessage(WebSocket s, ByteBuffer m){
        s.send("Bytes!!!");
    }

    public void onError(WebSocket s, Exception e){

    }
}
