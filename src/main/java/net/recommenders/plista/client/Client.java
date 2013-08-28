/*
Copyright (c) 2013, TU Berlin
Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
*/

package net.recommenders.plista.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.recommenders.plista.recommender.Recommender;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The main class
 * Functions:
 *      - initializing and starting the http server
 *      - configuring the http server
 * Note: Configuration details may be provided as a properties file (args[0])
 *
 * @author andreas, alan, alejandro
 *
 */
public class Client {

    /**
     * the default logger
     */
    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    /**
     * the constructor.
     */
    public Client() {
        super();
    }

    /**
     * This method starts the server
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
            throws Exception {

        final Properties properties = new Properties();
        String fileName = "";
        String recommenderClass = null;
        String handlerClass = null;

        if(args.length < 3)
            fileName = System.getProperty("propertyFile");
        else{
            fileName = args[0];
            recommenderClass = args[1];
            handlerClass = args[2];
        }
        // load the team properties
        try {
            properties.load(new FileInputStream(fileName));
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        Recommender recommender = null;
        recommenderClass = (recommenderClass != null ? recommenderClass : properties.getProperty("plista.recommender"));
        try {
            final Class<?> transformClass = Class.forName(recommenderClass);
            recommender = (Recommender) transformClass.newInstance();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("No recommender specified or recommender not available.");
        }
        // configure log4j
        if (args.length >= 4 && args[3] != null) {
            PropertyConfigurator.configure(args[0]);
        }
        else {
            PropertyConfigurator.configure("log4j.properties");
        }
        // set up and start server

        AbstractHandler handler = null;
        handlerClass = (handlerClass != null ? handlerClass : properties.getProperty("plista.handler"));
        try {
            final Class<?> transformClass = Class.forName(handlerClass);
            handler = (AbstractHandler) transformClass.getConstructor(Properties.class, Recommender.class).newInstance(properties, recommender);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("No handler specified or handler not available.");
        }
        final Server server = new Server(Integer.parseInt(properties.getProperty("plista.port", "8080")));

        server.setHandler(handler);
        logger.debug("Serverport " + server.getConnectors()[0].getPort());

        server.start();
        server.join();
    }

}
