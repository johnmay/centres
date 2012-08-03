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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.centres;

import java.util.Comparator;

/** @author John May */
public interface LigandComparator<A>
        extends Comparator<Ligand<A>> {

    /**
     * Analogous to {@link #compare(Object, Object)} the prioritise method will
     * add the {@link Descriptor.Type} to the order and can indicate what
     * comparison method was used.
     *
     * @param o1 first ligand
     * @param o2 second ligand
     *
     * @return the order of the two objects
     *
     * @see #compare(Object, Object)
     */
    public Comparison compareLigands(Ligand<A> o1, Ligand<A> o2);

}
