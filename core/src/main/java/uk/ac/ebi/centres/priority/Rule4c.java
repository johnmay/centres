/*
 * Copyright (c) 2014. John May
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

package uk.ac.ebi.centres.priority;

import uk.ac.ebi.centres.Descriptor;
import uk.ac.ebi.centres.priority.access.DescriptorAccessor;

/**
 * A rule with prioritises ligands in r preceeds s, (rule 4c, or 5b).
 *
 * @author John May
 */
public class Rule4c<A> extends DescriptorRule<A> {
    public Rule4c(DescriptorAccessor<A> accessor) {
        super(accessor, Type.GEOMETRICAL, Descriptor.s, Descriptor.r);
    }
}