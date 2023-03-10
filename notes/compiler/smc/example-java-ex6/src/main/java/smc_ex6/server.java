//
// The contents of this file are subject to the Mozilla Public
// License Version 1.1 (the "License"); you may not use this file
// except in compliance with the License. You may obtain a copy
// of the License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an
// "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
// implied. See the License for the specific language governing
// rights and limitations under the License.
//
// The Original Code is State Machine Compiler (SMC).
//
// The Initial Developer of the Original Code is Charles W. Rapp.
// Portions created by Charles W. Rapp are
// Copyright (C) 2000 - 2007. Charles W. Rapp.
// All Rights Reserved.
//
// Contributor(s):
//
// Name
//  TcpConnection.java
//
// Description
//  Encapsulates "TCP" server connection, accepting new client
//  connections.
//
// RCS ID
// $Id$
//
// CHANGE LOG
// (See the bottom of this file.)
//

package smc_ex6;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides the main application method when running a TCP
 * server.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */

public final class server
    implements TcpConnectionListener
{
//---------------------------------------------------------------
// Member methods.
//

    /**
     * Runs the TCP server application.
     * @param args contains the single TCP port argument.
     */
    public static void main(String[] args)
    {
        int port;

        if (args.length != 1)
        {
            System.err.println("usage: server port");
            System.exit(1);
        }

        try
        {
            server server;

            server = new server();
            port = Integer.parseInt(args[0]);

            System.out.println(
                "(Starting execution. Hit Enter to stop.)");

            server.run(port);

            System.out.println("(Stopping execution.)");

            System.exit(0);
        }
        catch (NumberFormatException ex)
        {
            System.err.println("Invalid port number - \"" +
                               args[0] +
                               "\".");
            System.exit(2);
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            System.exit(5);
        }
    }

    /**
     * Creates a new, unopened TCP server.
     */
    public server()
    {
        _isRunning = false;
        _opened = false;
        _myThread = null;
        _reason = null;
        _clientList = new LinkedList<>();

        return;
    }

    /**
     * Accepted TCP client is now closed.
     * @param tcp_client newly closed TCP client connection.
     */
    public synchronized void clientClosed(client tcp_client)
    {
        // Remove client from list.
        _clientList.remove(tcp_client);

        return;
    }

    /**
     * Starts the TCP server application for the given port.
     * @param port TCP server port.
     */
    public void run(int port)
    {
        TcpServer server_socket = new TcpServer(this);
        StopThread thread = new StopThread(this);
        Iterator<client> it;


        // Remember this thread for latter.
        _myThread = Thread.currentThread();

        // Create a thread to watch for a keystroke.
        thread.start();

        // Open the server connection.
        System.out.print("Opening server on " +
                         Integer.toString(port) +
                         " ... ");
        _opened = false;
        server_socket.start();
        server_socket.open(port);

        // Wait for open to complete.
        try
        {
            _isRunning = true;
            while (_isRunning == true)
            {
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException interrupt) {}

        if (_opened == false)
        {
            System.out.println("Open failed - " + _reason + ".");
        }
        else
        {
            System.out.println("Open successful.");
            System.out.println("Listening for new connections.");

            _isRunning = true;
            while (_isRunning == true)
            {
                try
                {
                    Thread.sleep(MAX_SLEEP);
                }
                catch (InterruptedException interrupt)
                {
                    System.out.println(
                        "(Server: Interrupt caught.)");
                }
            }

            // Now that we are no longer running, close the
            // connection.
            System.out.print("Closing connection ... ");
            server_socket.close();

            // Stop all remaining accepted clients.
            for (client client: _clientList)
            {
                client.halt();
            }

            // Wait for all accepted clients to stop running
            // before returning.
            while (_clientList.isEmpty() == false)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException interrupt)
                {}

                // Remove dead clients.
                for (it = _clientList.iterator();
                     it.hasNext() == true;
                    )
                {
                    if ((it.next()).isAlive() == false)
                    {
                        it.remove();
                    }
                }
            }
        }

        return;
    }

    // Stop the app.

    /**
     * Stops the running TCP server application.
     */
    public synchronized void halt()
    {
        _isRunning = false;

        // Wake me up in case I am sleeping.
        _myThread.interrupt();

        return;
    }

    /**
     * TCP server is now open.
     * @param server callback is from this TCP server.
     */
    @Override
    public void opened(TcpConnection server)
    {
        _opened = true;
        _myThread.interrupt();
        return;
    }

    /**
     * Called when TCP server fails to open.
     * @param reason text explaining why the server failed to
     * open.
     * @param server callback is from this TCP server.
     */
    @Override
    public void openFailed(String reason, TcpConnection server)
    {
        _opened = false;
        _reason = reason;
        _myThread.interrupt();
        return;
    }

    /**
     * TCP client is half closed.
     * @param client half-closed TCP client.
     */
    @Override
    public void halfClosed(TcpConnection client) {}

    /**
     * TCP server is closed.
     * @param reason text explaining why the TCP server is
     * closed.
     * @param server closed TCP server.
     */
    @Override
    public void closed(String reason, TcpConnection server)
    {
        System.out.println("Closed.");
        return;
    }

    /**
     * Handles an accept TCP client connection.
     * @param client accepted TCP client connection.
     * @param server TCP server accepting the client.
     */
    @Override
    public void accepted(TcpClient client, TcpServer server)
    {
        client new_client;

        System.out.println("Accepted new connection from " +
                           client.getAddress() +
                           ":" +
                           Integer.toString(client.getPort()) +
                           ".");
        new_client = new client(client, this);
        _clientList.add(new_client);

        // Start the client running in a separate thread.
        new_client.start();

        return;
    }

    /**
     * Data successfully transmitted by TCP client connection.
     * @param client data transmitted on this TCP client
     * connection.
     */
    @Override
    public void transmitted(TcpConnection client)
    {}

    /**
     * Called when TCP client connection transmit fails.
     * @param reason text explaining transmit failure.
     * @param client TCP client failing to transmit data.
     */
    @Override
    public void transmitFailed(String reason,
                               TcpConnection client)
    {}

    /**
     * Called when TCP client connection receives data.
     * @param data most recently received data.
     * @param client TCP client connection receiving data.
     */
    @Override
    public void receive(byte[] data, TcpConnection client)
    {}

//---------------------------------------------------------------
// Member data
//

    private boolean _isRunning;
    private boolean _opened;
    private Thread _myThread;
    private String _reason;

    // Keep list of accepted connections.
    private final List<client> _clientList;

    /**
     * Maximum sleep time is {@value} milliseconds.
     */
    public static final long MAX_SLEEP = 0x7fffffff;

//---------------------------------------------------------------
// Inner classes
//

    private final class StopThread
        extends Thread
    {
        private StopThread(server server)
        {
            _server = server;
        }

        @Override
        public void run()
        {
            // As soon as any key is hit, stop.
            try
            {
                System.in.read();
            }
            catch (IOException io_exception)
            {}

            _server.halt();

            return;
        }

        private final server _server;
    }
}

//
// CHANGE LOG
// $Log$
// Revision 1.7  2009/03/01 18:20:39  cwrapp
// Preliminary v. 6.0.0 commit.
//
// Revision 1.6  2007/02/21 13:42:57  cwrapp
// Moved Java code to release 1.5.0
//
// Revision 1.5  2005/11/07 19:34:54  cwrapp
// Changes in release 4.3.0:
// New features:
//
// + Added -reflect option for Java, C#, VB.Net and Tcl code
//   generation. When used, allows applications to query a state
//   about its supported transitions. Returns a list of transition
//   names. This feature is useful to GUI developers who want to
//   enable/disable features based on the current state. See
//   Programmer's Manual section 11: On Reflection for more
//   information.
//
// + Updated LICENSE.txt with a missing final paragraph which allows
//   MPL 1.1 covered code to work with the GNU GPL.
//
// + Added a Maven plug-in and an ant task to a new tools directory.
//   Added Eiten Suez's SMC tutorial (in PDF) to a new docs
//   directory.
//
// Fixed the following bugs:
//
// + (GraphViz) DOT file generation did not properly escape
//   double quotes appearing in transition guards. This has been
//   corrected.
//
// + A note: the SMC FAQ incorrectly stated that C/C++ generated
//   code is thread safe. This is wrong. C/C++ generated is
//   certainly *not* thread safe. Multi-threaded C/C++ applications
//   are required to synchronize access to the FSM to allow for
//   correct performance.
//
// + (Java) The generated getState() method is now public.
//
// Revision 1.4  2005/05/28 13:51:24  cwrapp
// Update Java examples 1 - 7.
//
// Revision 1.0  2003/12/14 20:22:01  charlesr
// Initial revision
//
