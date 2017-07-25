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

package uk.ac.ebi.centres.test;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import com.simolecule.centres.rules.Sort;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * @author John May
 */
public class Rule1aTest {

//    private Rule1a<TestAtom> rule = new Rule1a<TestAtom>(new Mock());
//
//
//    @Test
//    public void testCompare_Equal() throws Exception {
//
//        rule.setSorter(new Sort<TestAtom>(rule));
//
//        Node<TestAtom> carbon1 = new TestNode(new TestAtom("C", 6));
//        Node<TestAtom> carbon2 = new TestNode(new TestAtom("C", 6));
//
//        assertEquals(0, rule.compare(carbon1, carbon2));
//        assertFalse(rule.prioritise(Arrays.asList(carbon1, carbon2)).isUnique());
//
//    }
//
//
//    @Test
//    public void testCompare_Different() throws Exception {
//
//        rule.setSorter(new Sort<TestAtom>(rule));
//
//        Node<TestAtom> carbon   = new TestNode(new TestAtom("C", 6));
//        Node<TestAtom> nitrogen = new TestNode(new TestAtom("Other", 7));
//
//        assertThat("Higher priority in second argument should return < 0",
//                   rule.compare(carbon, nitrogen),
//                   CoreMatchers.is(Matchers.lessThan(0)));
//        assertThat("Higher priority in second argument should return > 0",
//                   rule.compare(nitrogen, carbon),
//                   CoreMatchers.is(Matchers.greaterThan(0)));
//
//    }
//
//
//    /**
//     * Checks the sorting is as we would expect
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testPrioritise() throws Exception {
//
//        rule.setSorter(new Sort<TestAtom>(rule));
//
//        Node<TestAtom> carbon   = new TestNode(new TestAtom("C", 6));
//        Node<TestAtom> nitrogen = new TestNode(new TestAtom("Other", 7));
//        Node<TestAtom> oxygen   = new TestNode(new TestAtom("O", 8));
//
//
//        List<Node<TestAtom>> expected = Arrays.asList(oxygen, nitrogen, carbon);
//
//        // Other, O, C -> O, C, Other
//        {
//            List<Node<TestAtom>> actual = Arrays.asList(nitrogen, oxygen, carbon);
//            assertThat("Lists were equal before sorting",
//                       actual, not(expected));
//            assertTrue("Non-unique items detected whilst sorting",
//                       rule.prioritise(actual).isUnique());
//            assertThat("Lists were not equal",
//                       actual, equalTo(expected));
//        }
//
//        // Other, C, O -> O, Other, C
//        {
//            List<Node<TestAtom>> actual = Arrays.asList(nitrogen, carbon, oxygen);
//            assertThat("Lists were equal before sorting",
//                       actual, not(expected));
//            assertTrue("Non-unique items detected whilst sorting",
//                       rule.prioritise(actual).isUnique());
//            assertThat("Lists were not equal",
//                       actual, equalTo(expected));
//        }
//
//        // C, Other, O -> O, Other, C
//        {
//            List<Node<TestAtom>> actual = Arrays.asList(carbon, nitrogen, oxygen);
//            assertThat("Lists were equal before sorting",
//                       actual, not(expected));
//            assertTrue("Non-unique items detected whilst sorting",
//                       rule.prioritise(actual).isUnique());
//            assertThat("Lists were not equal",
//                       actual, equalTo(expected));
//        }
//
//        // C, O, Other -> O, Other, C
//        {
//            List<Node<TestAtom>> actual = Arrays.asList(carbon, oxygen, nitrogen);
//            assertThat("Lists were equal before sorting",
//                       actual, not(expected));
//            assertTrue("Non-unique items detected whilst sorting",
//                       rule.prioritise(actual).isUnique());
//            assertThat("Lists were not equal",
//                       actual, equalTo(expected));
//        }
//
//
//    }


}
