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

package uk.ac.ebi.centres.cdk;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import uk.ac.ebi.centres.Centre;
import uk.ac.ebi.centres.CentreProvider;
import uk.ac.ebi.centres.ConnectionTable;
import uk.ac.ebi.centres.DescriptorManager;
import uk.ac.ebi.centres.graph.ConnectionTableDigraph;
import uk.ac.ebi.centres.ligand.PlanarCentre;
import uk.ac.ebi.centres.ligand.TetrahedralCentre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John May
 */
public class CDKCentreProvider implements CentreProvider<IAtom> {

    private final IAtomContainer         container;
    private final ConnectionTable<IAtom> table;


    public CDKCentreProvider(IAtomContainer container) {
        this.container = container;
        this.table = new CDKConnectionTable(container);
    }


    @Override
    public Collection<Centre<IAtom>> getCentres(DescriptorManager<IAtom> manager) {

        List<Centre<IAtom>> centres = new ArrayList<Centre<IAtom>>(container.getAtomCount());

        // tetrahedral centres
        for (IAtom atom : container.atoms()) {
            // might need refinement
            if (IAtomType.Hybridization.SP3.equals(atom.getHybridization())
                    && container.getConnectedAtomsCount(atom) > 2) {
                TetrahedralCentre<IAtom> centre = new TetrahedralCentre<IAtom>(manager.getDescriptor(atom), atom);
                centre.setProvider(new ConnectionTableDigraph<IAtom>(centre, manager, table));
                centres.add(centre);
            }
        }

        // planar centres
        for (IBond bond : container.bonds()) {
            // TODO: check we're not in a ring
            if (IBond.Order.DOUBLE.equals(bond.getOrder())) {
                PlanarCentre<IAtom> centre = new PlanarCentre<IAtom>(bond.getAtom(0), bond.getAtom(1),
                                                                     manager.getDescriptor(bond.getAtom(0), bond.getAtom(1)));
                centre.setProvider(new ConnectionTableDigraph<IAtom>(centre, manager, table));
                centres.add(centre);

            }
        }

        return centres;

    }

}