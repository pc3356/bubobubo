package com.bubobubo.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 21/04/2012
 * Time: 21:10
 */
public class UrlUtils {

    private final static String REFERRER = "referer";

    private UrlUtils() {}

    public static String buildUrl(String targetUri, HttpHeaders headers) throws Exception {

        if(UrlUtils.hasProtocol(targetUri)) {
            return targetUri;
        }


        String referrer = getReferringUri(headers);

        String basePath;
        if(referrer == null) {
            basePath = "";
        } else {
            try {
                URL url = new URL(referrer);
                basePath = url.getPath();
            } catch (MalformedURLException mue) {
                /* don't do much.... */
                throw new Exception(mue);
            }
        }

        URI uri = UriBuilder.fromPath(basePath).path(targetUri).build();
//        StringBuilder builder = new StringBuilder();
//

//
//        }
//
//        if(!builder.toString().endsWith("/")) {
//            builder.append('/');
//        }
//        return builder.append(targetUri).toString();
        String out = uri.toString();
        if(!out.startsWith("/")) {
            out = "/" + out;
        }
        return out;
    }

    public static boolean hasProtocol(String uri) {
        String lc = uri.toLowerCase();
        return (lc.startsWith("http:") || lc.startsWith("https:")
                || lc.startsWith("file:") || lc.startsWith("mailto:") || lc.startsWith("ftp:"));
    }

    public static String getReferringUri(HttpHeaders headers) {
        List<String> referrers = headers.getRequestHeader(REFERRER);
        if(referrers != null && referrers.size() > 0) {
            return referrers.get(0);
        }
        return null;
    }
}
