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
//  TcpServer.java
//
// Description
//  TCP server connection.
//
// RCS ID
// $Id$
//
// CHANGE LOG
// $Log$
// Revision 1.5  2007/12/28 12:34:40  cwrapp
// Version 5.0.1 check-in.
//
// Revision 1.4  2005/05/28 13:51:24  cwrapp
// Update Java examples 1 - 7.
//
// Revision 1.0  2003/12/14 20:21:12  charlesr
// Initial revision
//

package smc_ex6;

/**
 * TCP server connection for accepting TCP client connections.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class TcpServer
    extends TcpConnection
{
// Member methods.

    /**
     * Creates a TCP server connection with the given TCP
     * connection listener.
     * @param listener TCP connection listener.
     */
    public TcpServer(TcpConnectionListener listener)
    {
        super(listener);
    }

    /**
     * Returns the TCP service port.
     * @return TCP service port.
     */
    public int getPort()
    {
        int retval;

        if (_async_socket == null ||
            _async_socket.getDatagramSocket() == null)
        {
            retval = -1;
        }
        else
        {
            retval = _async_socket.getDatagramSocket().getLocalPort();
        }

        return(retval);
    }

    /**
     * Opens TCP service for the given port.
     * @param port TCP service port.
     */
    public void open(int port)
    {
        passiveOpen(port);
        return;
    }

// Member data.
}
