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

import org.openscience.cdk.config.Elements;
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IIsotope;
import uk.ac.ebi.centres.DefaultPerceptor;
import uk.ac.ebi.centres.SignCalculator;
import uk.ac.ebi.centres.priority.AtomicNumberRule;
import uk.ac.ebi.centres.priority.CombinedRule;
import uk.ac.ebi.centres.priority.DuplicateAtomRule;
import uk.ac.ebi.centres.priority.MassNumberRule;
import uk.ac.ebi.centres.priority.access.AtomicNumberAccessor;
import uk.ac.ebi.centres.priority.access.MassNumberAccessor;
import uk.ac.ebi.centres.priority.access.PsuedoAtomicNumberModifier;
import uk.ac.ebi.centres.priority.access.descriptor.AuxiliaryDescriptor;
import uk.ac.ebi.centres.priority.access.descriptor.PrimaryDescriptor;
import uk.ac.ebi.centres.priority.descriptor.PairRule;
import uk.ac.ebi.centres.priority.descriptor.PseudoRSRule;
import uk.ac.ebi.centres.priority.descriptor.RSRule;
import uk.ac.ebi.centres.priority.descriptor.ZERule;

import java.io.IOException;

/**
 * @author John May
 */
public class CDKPerceptor extends DefaultPerceptor<IAtom> {

    private static final MassNumberAccessor<IAtom> cdkMassNumberAccessor = new MassNumberAccessor<IAtom>() {
        @Override
        public int getMassNumber(IAtom atom) {
            Integer massnum = atom.getMassNumber();
            if (massnum == null) {
                int elem = atomicNumber(atom);
                if (elem == 0)
                    return 0;
                try {
                    IIsotope isotope = Isotopes.getInstance().getMajorIsotope(Elements.ofNumber(elem).symbol());
                    if (isotope == null)
                        return 0;
                    return isotope.getMassNumber();
                } catch (IOException e) {
                    return 0;
                }
            }
            return massnum;
        }
    };

    private static int atomicNumber(IAtom atom) {
        final Integer elem = atom.getAtomicNumber();
        if (elem == null) return 0;
        return elem;
    }

    public CDKPerceptor(SignCalculator<IAtom> calculator) {
        super(new CombinedRule<IAtom>(
                      new AtomicNumberRule<IAtom>(
                              new PsuedoAtomicNumberModifier<IAtom>(
                                      new AtomicNumberAccessor<IAtom>() {
                                          @Override
                                          public int getAtomicNumber(IAtom atom) {
                                              return atomicNumber(atom);
                                          }
                                      })),
                      // new DuplicateAtomRule<IAtom>(),
                      new MassNumberRule<IAtom>(cdkMassNumberAccessor),
                      new ZERule<IAtom>(),
                      new PairRule<IAtom>(new PrimaryDescriptor<IAtom>()),
                      new PseudoRSRule<IAtom>(new PrimaryDescriptor<IAtom>()),
                      new RSRule<IAtom>(new PrimaryDescriptor<IAtom>())
              ),
              new CombinedRule<IAtom>(
                      new AtomicNumberRule<IAtom>(
                              new PsuedoAtomicNumberModifier<IAtom>(
                                      new AtomicNumberAccessor<IAtom>() {
                                          @Override
                                          public int getAtomicNumber(IAtom atom) {
                                              return atomicNumber(atom);
                                          }
                                      })),
                      new MassNumberRule<IAtom>(cdkMassNumberAccessor),
                      new ZERule<IAtom>(),
                      new PairRule<IAtom>(new AuxiliaryDescriptor<IAtom>()),
                      new PseudoRSRule<IAtom>(new AuxiliaryDescriptor<IAtom>()),
                      new RSRule<IAtom>(new AuxiliaryDescriptor<IAtom>())
              ),
              calculator);
    }

    /**
     * Creates a perceptor for 2D centres
     */
    public CDKPerceptor() {
        this(new CDK2DSignCalculator());
    }


    public void perceive(IAtomContainer container) {
        perceive(new CDKCentreProvider(container), new CDKManager(container));
    }

}
