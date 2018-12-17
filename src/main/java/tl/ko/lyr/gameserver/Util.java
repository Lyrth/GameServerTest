package tl.ko.lyr.gameserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static String reducePath(String path){
        //if (path.matches("^.{2,5}://[^/]+/"))
        path = path.trim()  // trim whitespace
            .replaceFirst("#.*$","")        // remove frag identifier
            .replaceFirst("\\?.*$","")      // remove query
            .replaceFirst("/+$","")         // remove trailing slash
            .replaceFirst("^.{2,6}://[^/]+/","/");  // remove protocol
        if (! path.startsWith("/") ) path = "/" + path;     // require leading slash
        return path;
    }

    public static HashMap<String,String> splitQuery(String path){
        if (path.contains("?")){
            try {
                return splitQuery(makeUrl(path));
            } catch (MalformedURLException e){
                return new HashMap<>();
            }
        } else { // assume already query
            return queryToMap(path);
        }
    }

    public static HashMap<String, String> splitQuery(URL url) {
        if (url.getQuery() == null || url.getQuery().isEmpty()) {
            return new HashMap<>();
        }
        return queryToMap(url.getQuery());
    }

    public static URL makeUrl(String path) throws MalformedURLException {
        if (path.matches("^(https?|wss?)://.*"))
            return new URL(path);
        else
            return new URL("http://127.0.0.1" +
                (path.startsWith("/") ? "" : "/") + path); // make URL-like
    }

    public static HashMap<String, String> queryToMap(String query) {
        return new HashMap<>(
            Arrays.stream(query.split("&"))
                .map(Util::splitQueryParameter)
                .collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue) )
        );
    }

    private static Map.Entry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

}
