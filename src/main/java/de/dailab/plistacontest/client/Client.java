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

package de.dailab.plistacontest.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dailab.plistacontest.client.ContestHandler;


/**
 * The main class
 * Functions:
 *      - initializing and starting the http server
 *      - configuring the http server
 * Note: Configuration details may be provided as a properties file (args[0])
 * 
 * @author andreas
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

    	// store some configurations
        final Properties properties = new Properties();

        // load the team properties
        try {
        	if (args.length > 0) {
        		properties.load(new FileInputStream(args[0]));
        	}
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

        // you might want to use a recommender
        Object recommender = null;

        try {
        	// initialize the recommender dynamically
        	/*
            final Class<?> transformClass = Class.forName(args[1]);
            recommender = (Object) transformClass.newInstance();
            */
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("No recommender specified or recommender not avialable.");
        }

        // configure log4j
        if (args.length >= 3 && args[2] != null) {
            PropertyConfigurator.configure(args[0]);
        }
        else {
            PropertyConfigurator.configure("log4j.properties");
        }

        // set up and start server
        final Server server = new Server(Integer.parseInt(properties.getProperty("plista.port", "8080")));
        server.setHandler(new ContestHandler(properties, recommender));
        logger.debug("Serverport " + server.getConnectors()[0].getPort());
        
        // start
        server.start();
        server.join();
    }

}
