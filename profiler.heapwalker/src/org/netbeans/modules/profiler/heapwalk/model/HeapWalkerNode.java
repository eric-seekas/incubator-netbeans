/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.profiler.heapwalk.model;

import org.netbeans.lib.profiler.results.CCTNode;
import javax.swing.Icon;


/**
 * This interface must be implemented by each node used in Fields Browser
 * Note: currently implements CCTNode just for compatibility with TreeTableModel
 *
 * @author Jiri Sedlacek
 */
public abstract class HeapWalkerNode extends CCTNode {
    //~ Static fields/initializers -----------------------------------------------------------------------------------------------

    public static final int MODE_FIELDS = 1;
    public static final int MODE_REFERENCES = 2;

    //~ Methods ------------------------------------------------------------------------------------------------------------------

    public abstract HeapWalkerNode getChild(int index);

    public abstract HeapWalkerNode[] getChildren();

    public abstract Icon getIcon();

//    public abstract int getIndexOfChild(Object child);

//    public abstract boolean isLeaf();

//    public abstract int getNChildren();

    public abstract String getName();

    public abstract HeapWalkerNode getParent();

    public abstract boolean isRoot();

    public abstract String getSimpleType();

    public abstract String getType();

    public abstract String getValue();
    
    public abstract String getDetails();

    public abstract String getSize();

    public abstract String getRetainedSize();
    
    // used for equals() and hashCode() implementation
    public abstract Object getNodeID();

    // used for testing children for null without lazy-populating invocation
    // note that if false, it means that chilren are not yet computed OR this node is leaf!
    public abstract boolean currentlyHasChildren();

    /**
     * Used to get information if node is used within Fields Browser or References Browser
     * There are two different algorithms for generating childs in both Browsers.
     */
    public abstract int getMode();
}
