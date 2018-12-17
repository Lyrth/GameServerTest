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
import java.util.HashMap;
import java.util.List;

import static tl.ko.lyr.gameserver.Util.reducePath;

public class Server extends WebSocketServer {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int    DEFAULT_PORT = 8080;

    private HashMap<String, Handler> handlers = new HashMap<>();
    private int handlerCount = 0; //only for non-labeled handlers

    public Server(){
        this(DEFAULT_HOST,DEFAULT_PORT);
    }

    public Server(String hostname, int port){
        this(new InetSocketAddress(hostname,port));
    }

    public Server(InetSocketAddress address) {
        super(address);
        setConnectionLostTimeout(20);
        setTcpNoDelay(true);
        setReuseAddr(true);
    }

    // Handler handlers

    public Server addHandler(Handler handler){
        handlers.put("#"+handlerCount,handler);
        handlerCount++;
        return this;
    }

    public Server addHandler(Handler handler, String label){
        handlers.put("$"+label,handler);
        return this;
    }

    //Overrides an endpoint if it exists.
    public Server addHandler(String endpoint, Handler handler){
        if (endpoint.charAt(0) == '^') {  // regex
            handlers.put(endpoint,handler.setMatcher(endpoint));
        } else {  // filter exact
            handlers.put(reducePath(endpoint), handler.setMatcher("^"+reducePath(endpoint)+"/?([?].*)?$"));
        }  // TODO: no need to reduce endpoint string
        return this;
    }

    public Server removeHandler(String name){
        if (handlers.containsKey(name)){
            handlers.remove(name);
        } else {
            handlers.remove("$"+name);
            handlers.remove(reducePath(name));
        }
        return this;
    }

    // Callbacks

    public void onStart(){
        handlers.forEach((name,handler) -> handler.onStart());
    }

    public void onOpen(WebSocket s, ClientHandshake h){
        handlers.forEach((name,handler) -> {
            String path = reducePath(s.getResourceDescriptor());
            if(handler.test(path)) handler.onOpen(s,h);
        });
    }

    public void onClose(WebSocket s, int code, String reason, boolean remote){
        handlers.forEach((name,handler) -> {
            String path = reducePath(s.getResourceDescriptor());
            if(handler.test(path)) handler.onClose(s,code,reason,remote);
        });
    }

    public void onMessage(WebSocket s, String m){
        handlers.forEach((name,handler) -> {
            String path = reducePath(s.getResourceDescriptor());
            if(handler.test(path)) handler.onMessage(s,m);
        });
    }

    public void onMessage(WebSocket s, ByteBuffer m){
        handlers.forEach((name,handler) -> {
            String path = reducePath(s.getResourceDescriptor());
            if(handler.test(path)) handler.onMessage(s,m);
        });
    }

    public void onError(WebSocket s, Exception e){
        handlers.forEach((name,handler) -> {
            String path = reducePath(s.getResourceDescriptor());
            if(handler.test(path)) handler.onError(s,e);
        });
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(
            WebSocket conn,
            Draft draft,
            ClientHandshake request)
            throws InvalidDataException {
        ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
//        String str = "USER CONNECT:";
//        str += "FROM: " + conn.getRemoteSocketAddress() + "\n" +
//            "CONN REQ DESC: " + conn.getResourceDescriptor() + "\n" +
//            "CREQ REQ DESC: " + request.getResourceDescriptor() + "\n";
//        StringBuilder builderStr = new StringBuilder(str);
//        request.iterateHttpFields().forEachRemaining(f -> builderStr.append(f).append(": ").append(request.getFieldValue(f)).append("\n"));
//
//        System.out.println(builderStr.toString());
        return builder;
    }
}
