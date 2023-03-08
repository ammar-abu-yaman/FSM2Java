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
// The Original Code is  State Machine Compiler(SMC).
//
// The Initial Developer of the Original Code is Charles W. Rapp.
// Portions created by Charles W. Rapp are
// Copyright (C) 2021. Charles W. Rapp.
// All Rights Reserved.
//
// Contributor(s):
//
// statemap.java --
//
//  This package defines the FSMContext class which must be
//  inherited by any Java class wanting to use an smc-generated
//  state machine.
//

package statemap;

/**
 * Enumerates the supported debug output types. SMC supports
 * two debug output types:
 * {@link java.io.PrintStream print stream} and
 * {@link java.util.logging.Logger Java logger}.
 */

public enum DebugOutputType
{
    /**
     * No debug output is performed.
     */
    NO_DEBUG_OUTPUT,

    /**
     * Debug output is posted to a print stream.
     */
    PRINT_STREAM,

     /**
     * Debug output is posted to
     * {@link java.util.logging.Logger Java logger}.
     */
    JAVA_LOGGING
} // end of class DebugOutputType
// end of DebugOutputType
