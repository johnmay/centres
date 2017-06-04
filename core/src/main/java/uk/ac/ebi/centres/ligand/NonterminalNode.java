/*
 * Copyright (c) 2012. John May
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA
 */

package uk.ac.ebi.centres.ligand;

import uk.ac.ebi.centres.ConnectionProvider;
import uk.ac.ebi.centres.MutableDescriptor;

import java.util.Collections;
import java.util.Set;

/**
 * @author John May
 */
public class NonterminalNode<A> extends AbstractNode<A> {

    private final A atom;
    private       A parent;


    public NonterminalNode(MutableDescriptor descriptor,
                           A atom,
                           A parent,
                           int distance) {
        this(descriptor, Collections.EMPTY_SET, atom, parent, distance);
    }


    public NonterminalNode(ConnectionProvider<A> provider,
                           MutableDescriptor descriptor,
                           A atom,
                           A parent,
                           int distance) {
        this(provider, descriptor, Collections.EMPTY_SET, atom, parent, distance);
    }


    public NonterminalNode(ConnectionProvider<A> provider,
                           MutableDescriptor descriptor,
                           Set<A> visited,
                           A atom,
                           A parent,
                           int distance) {
        super(provider,
              visited, descriptor, distance);

        if (atom == null)
            throw new IllegalArgumentException("Provided non-terminal " +
                                                       "atom should not be null");
        if (parent == null)
            throw new IllegalArgumentException("Provided non-terminal parent" +
                                                       " atom should not be null");

        getVisited().add(atom);
        getVisited().add(parent);

        this.atom = atom;
        this.parent = parent;
    }


    public NonterminalNode(MutableDescriptor descriptor,
                           Set<A> visited,
                           A atom,
                           A parent,
                           int distance) {

        super(visited, descriptor, distance);

        if (atom == null)
            throw new IllegalArgumentException("Provided non-terminal " +
                                                       "atom should not be null");
        if (parent == null)
            throw new IllegalArgumentException("Provided non-terminal parent" +
                                                       " atom should not be null");

        this.atom = atom;
        this.parent = parent;

        getVisited().add(atom);
        getVisited().add(parent);


    }


    @Override
    public A getParent() {
        return this.parent;
    }


    @Override
    public void setParent(A atom) {
        this.parent = atom;
    }


    @Override
    public A getAtom() {
        return atom;
    }


    @Override
    public boolean isTerminal() {
        return Boolean.FALSE;
    }


    @Override
    public boolean isBranching() {
        return Boolean.FALSE;
    }


    @Override
    public Boolean isParent(A atom) {
        return parent.equals(atom);
    }

}