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

package uk.ac.ebi.centres.priority;

import uk.ac.ebi.centres.Descriptor;
import uk.ac.ebi.centres.priority.access.descriptor.ArcPrimaryDescriptor;

/**
 * A rule with prioritises ligands in Z (cis) configuration over those in E
 * (trans) configuration.
 *
 * @author John May
 */
public class Rule3<A> extends DescriptorRule<A> {

    public Rule3() {
        super(new ArcPrimaryDescriptor<A>(), Type.GEOMETRICAL, Descriptor.E, Descriptor.Z);
    }

}