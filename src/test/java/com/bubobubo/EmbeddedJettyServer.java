package com.bubobubo;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public abstract class EmbeddedJettyServer {

    private final static Logger LOG = Logger.getLogger(EmbeddedJettyServer.class);

    private static boolean running = false;
    private static Server server = new Server();
    private static int jettyPort;
    private static String jettyUrl;
    private static Map<String, String> contextParams = new HashMap<String, String>();
    private static List<EventListener> eventListeners = new ArrayList<EventListener>();
    private static Map<String, Filter> requestFilterMapping = new HashMap<String, Filter>();
    private static Map<String, Servlet> servletMapping = new HashMap<String, Servlet>();
    private static File webXmlFile;

    public static File getWebXmlFile() {
        return webXmlFile;
    }

    public static void startUp(final String contextConfigLocation) throws Exception {
        setContextConfigLocation(contextConfigLocation);
        addEventListener(new ContextLoaderListener());
        start();
    }

    public static void start() throws Exception {
        if (!running) {
            if (webXmlFile == null && (contextParams.isEmpty() || !contextParams.containsKey("contextConfigLocation"))) {
                throw new FileNotFoundException(
                        "You must provide a spring context config location using addContextParam(String, String)");
            }
            startTheJettyServer();
        }
    }

    public static void stop() throws Exception {
        if (running) {
            LOG.debug(">>> STOPPING EMBEDDED JETTY SERVER >>>");
            server.stop();
            running = false;
        }
    }

    private static synchronized void startTheJettyServer() {

        SocketConnector connector = new SocketConnector();
        try {

            jettyPort = findFreePort();

            connector.setPort(jettyPort);

            server.setConnectors(new Connector[]{connector});

            if (webXmlFile == null) {
                LOG.debug(">>> LOADING WEB APP CONTEXT FROM SET PARAMETERS >>>");
                server.setHandler(buildWebAppContextFromParams());
            } else {
                if (webXmlFile.exists()) {
                    LOG.debug(">>> " + webXmlFile.getAbsolutePath() + " file detected, loading context >>>");
                    server.setHandler(getWebAppContextFromFile());
                } else {
                    throw new FileNotFoundException(">>> web.xml NOT FOUND >>>");
                }
            }

            LOG.debug(">>> STARTING EMBEDDED JETTY SERVER ON PORT " + jettyPort + " >>>");

            server.start();
            jettyUrl = "http://localhost:" + jettyPort + "/";
            running = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Handler buildWebAppContextFromParams() {
        WebAppContext webAppContext = new WebAppContext();
        // add event listeners to server
        if (!eventListeners.isEmpty()) {
            EventListener[] eventListenersArray = eventListeners.toArray(new EventListener[eventListeners.size()]);
            webAppContext.setEventListeners(eventListenersArray);
        }
        // add context params to server
        for (Map.Entry<String, String> entry : contextParams.entrySet()) {
            webAppContext.setInitParameter(entry.getKey(), entry.getValue());
        }

        webAppContext.setContextPath("/");

        // add other servlets
        for (Map.Entry<String, Servlet> entry : servletMapping.entrySet()) {
            webAppContext.addServlet(new ServletHolder(entry.getValue()), entry.getKey());
        }

        // add filters to server
        for (Map.Entry<String, Filter> entry : requestFilterMapping.entrySet()) {
            webAppContext.addFilter(new FilterHolder(entry.getValue()), entry.getKey(), EnumSet.of(DispatcherType.REQUEST));
        }

        webAppContext.setResourceBase("");
        return webAppContext;
    }

    private static Handler getWebAppContextFromFile() {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setDescriptor(webXmlFile.getAbsolutePath());
        webAppContext.setResourceBase(webXmlFile.getAbsolutePath());
        webAppContext.setContextPath("/");
        return webAppContext;
    }

    private static int findFreePort() throws IOException {
        ServerSocket server = new ServerSocket(0);
        int port = server.getLocalPort();
        server.close();
        return port;
    }

    public static HttpResponse httpGet(String relativeUri) throws IOException {
        return httpGet(relativeUri, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpGet(String relativeUri, Map<String, String> headers) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(jettyUrl + relativeUri);
        addHeadersToMethod(headers, httpGet);
        return httpClient.execute(httpGet);
    }

    public static HttpResponse httpPost(String relativeUri, Map<String, Object> parameters) throws IOException {
        return httpPost(relativeUri, parameters, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpPost(String relativeUri, Map<String, Object> parameters, Map<String, String> headers) throws IOException {
        return httpPost(relativeUri, parameters, headers, null);

    }

    public static HttpResponse httpPost(String relativeUri, Map<String, Object> parameters, Map<String, String> headers, InputStream inputStream) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(jettyUrl + relativeUri);
        HttpParams httpParams = new BasicHttpParams();
        if (parameters != null) {
            for (String parameterName : parameters.keySet()) {
                httpParams.setParameter(parameterName, parameters.get(parameterName));
            }
        }
        httpPost.setParams(httpParams);
        addHeadersToMethod(headers, httpPost);

        addBodyToClient(inputStream, httpPost);

        return httpClient.execute(httpPost);

    }

    private static void addBodyToClient(InputStream inputStream, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        if (inputStream == null) {
            return;
        }
        try {
            httpEntityEnclosingRequestBase.setEntity(new StringEntity(isToString(inputStream)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpResponse httpDelete(String relativeUri) throws IOException {
        return httpDelete(relativeUri, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpDelete(String relativeUri, Map<String, String> headers) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(jettyUrl + relativeUri);
        addHeadersToMethod(headers, httpDelete);
        return httpClient.execute(httpDelete);

    }

    public static HttpResponse httpPut(String relativeUri, Map<String, Object> parameters) throws IOException {
        return httpPut(relativeUri, parameters, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpPut(String relativeUri, Map<String, Object> parameters, Map<String, String> headers) throws IOException {
        return httpPut(relativeUri, parameters, headers, null);

    }

    public static HttpResponse httpPut(String relativeUri, Map<String, Object> parameters, Map<String, String> headers, InputStream is) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(jettyUrl + relativeUri);
        HttpParams httpParams = new BasicHttpParams();
        if (parameters != null) {
            for (String parameterName : parameters.keySet()) {
                httpParams.setParameter(parameterName, parameters.get(parameterName));
            }
        }
        httpPut.setParams(httpParams);
        addHeadersToMethod(headers, httpPut);

        addBodyToClient(is, httpPut);

        return httpClient.execute(httpPut);

    }

    public static HttpResponse httpHead(String relativeUri) throws IOException {
        return httpHead(relativeUri, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpHead(String relativeUri, Map<String, String> headers) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpHead httpHead = new HttpHead(jettyUrl + relativeUri);
        addHeadersToMethod(headers, httpHead);
        return httpClient.execute(httpHead);

    }

    public static HttpResponse httpOptions(String relativeUri) throws IOException {
        return httpOptions(relativeUri, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpOptions(String relativeUri, Map<String, String> headers) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpOptions httpOptions = new HttpOptions(jettyUrl + relativeUri);
        addHeadersToMethod(headers, httpOptions);
        return httpClient.execute(httpOptions);
    }

    public static HttpResponse httpTrace(String relativeUri) throws IOException {
        return httpTrace(relativeUri, Collections.<String, String>emptyMap());
    }

    public static HttpResponse httpTrace(String relativeUri, Map<String, String> headers) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpTrace httpTrace = new HttpTrace(jettyUrl + relativeUri);
        addHeadersToMethod(headers, httpTrace);
        return httpClient.execute(httpTrace);

    }

    private static void addHeadersToMethod(Map<String, String> headers, HttpRequestBase method) {
        if (headers != null) {
            for (String headerName : headers.keySet()) {
                method.addHeader(headerName, headers.get(headerName));
            }
        }
    }

    private static String isToString(InputStream in) throws IOException {
        return IOUtils.toString(in, "UTF-8");
    }

    public static void setContextConfigLocation(String configLocation) {
        addContextParam("contextConfigLocation", configLocation);
    }

    public static void addContextParam(String name, String value) {
        contextParams.put(name, value);
    }

    public static void addEventListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }

    public static void addRequestFilterMapping(String urlMappingSpec, Filter filter) {
        requestFilterMapping.put(urlMappingSpec, filter);
    }

    public static void addServlet(String urlMappingSpec, Servlet servlet) {
        servletMapping.put(urlMappingSpec, servlet);
    }

    public static void useWebXmlFileFromClasspath(String path) throws IOException {
        webXmlFile = new ClassPathResource(path).getFile();
    }

    public static void useWebXmlFileFromPath(String path) throws IOException {
        webXmlFile = new File(path);
    }

    public static void destroyContexts() {
        eventListeners.clear();
        contextParams.clear();
        servletMapping.clear();
        requestFilterMapping.clear();
    }

    public static void shutdown() {
        try {
            stop();
            destroyContexts();
        } catch (RuntimeException ignored) {
            ignored.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}