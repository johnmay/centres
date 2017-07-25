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

package com.simolecule.centres.rules;

import com.simolecule.centres.BaseMol;
import com.simolecule.centres.Descriptor;
import com.simolecule.centres.Edge;
import com.simolecule.centres.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * A descriptor pair rule. This rule defines that like descriptor pairs have
 * priority over unlike descriptor pairs.
 *
 * @author John May
 */
public class Rule4bNew<A, B>
        extends SequenceRule<A, B> {

  private final Descriptor ref;

  public Rule4bNew(BaseMol<A, B> mol)
  {
    super(mol);
    ref = null;
  }

  public Rule4bNew(BaseMol<A, B> mol, Descriptor ref)
  {
    super(mol);
    this.ref = ref;
  }

  /**
   * Generates a set of descriptor lists that maintain the like/unlike pairing
   * whilst descriptors are added. The set is navigable and maintains priority
   * ordering when multiple lists are present. This method is a wrapper for
   * adding the seeding ligand to the queue.
   *
   * @return navigable set of descriptor lists
   */
  protected NavigableSet<PairList> generate(Edge<A, B> e)
  {
    // would be good to give an expected size
    Queue<Edge<A, B>> queue = new ArrayDeque<>();
    queue.add(e);
    return new TreeSet<PairList>(generate(queue));
  }

  private boolean hasDescriptors(Node<A, B> node)
  {
    Deque<Node<A, B>> deque = new ArrayDeque<>();
    deque.add(node);
    while (!deque.isEmpty()) {
      Node<A, B> n = deque.poll();
      if (getAtomLabel(n) != null)
        return true;
      for (Edge<A, B> e : n.getEdges()) {
        if (e.getEnd().equals(n))
          continue;
        if (getBondLabel(e) != null)
          return true;
        deque.add(e.getEnd());
      }
    }
    return false;
  }

  public void printLevels(Node<A, B> node)
  {
    List<List<Node<A, B>>> prev = initialLevel(node);
    while (!prev.isEmpty()) {
      System.out.println(prev);
      prev = getNextLevel(prev);
    }
  }

  private boolean getReference(List<Node<A, B>> nodes, List<Descriptor> result)
  {
    int right = 0;
    int left  = 0;
    for (Node<A, B> node : nodes) {
      Descriptor desc = getAtomLabel(node);
      if (desc != null) {
        switch (desc) {
          case R:
          case M:
          case seqCis:
            right++;
            break;
          case S:
          case P:
          case seqTrans:
            left++;
            break;
        }
      }
    }
    if (right + left == 0) {
      return false;
    } else if (right > left) {
      result.add(Descriptor.R);
      return true;
    } else if (right < left) {
      result.add(Descriptor.S);
      return true;
    } else {
      result.add(Descriptor.R);
      result.add(Descriptor.S);
      return true;
    }

  }

  public List<Descriptor> getReferenceDescriptors(Node<A, B> node)
  {
    List<Descriptor>       result = new ArrayList<>(2);
    List<List<Node<A, B>>> prev   = initialLevel(node);
    while (!prev.isEmpty()) {
      for (List<Node<A, B>> nodes : prev) {
        if (getReference(nodes, result))
          return result;
      }
      prev = getNextLevel(prev);
    }
    return null;
  }

  private List<List<Node<A, B>>> initialLevel(Node<A, B> node)
  {
    List<List<Node<A, B>>> levels = new ArrayList<>();
    levels.add(Collections.singletonList(node));
    return levels;
  }

  private List<List<Node<A, B>>> getNextLevel(List<List<Node<A, B>>> prevLevel)
  {
    List<List<Node<A, B>>> nextLevel = new ArrayList<>(4 * prevLevel.size());
    for (List<Node<A, B>> prev : prevLevel) {

      List<List<List<Edge<A, B>>>> tmp = new ArrayList<>();
      for (Node<A, B> node : prev) {
        List<Edge<A, B>> edges = node.getNonTerminalOutEdges();
        sort(node, edges);
        tmp.add(getSorter().getGroups(edges));
      }

      // check sizes
      int size = -1;
      for (int i = 0; i < tmp.size(); ++i) {
        int localSize = tmp.get(0).size();
        if (size < 0)
          size = localSize;
        else if (size != localSize)
          throw new IllegalArgumentException("Something unexpected!");
      }

      for (int i = 0; i < size; i++) {
        List<Node<A, B>> eq = new ArrayList<>();
        for (List<List<Edge<A, B>>> aTmp : tmp) {
          eq.addAll(toNodeList(aTmp.get(i)));
        }
        if (!eq.isEmpty())
          nextLevel.add(eq);
      }
    }
    return nextLevel;
  }

  private List<Node<A, B>> toNodeList(List<Edge<A, B>> eqEdges)
  {
    List<Node<A, B>> eqNodes = new ArrayList<>(eqEdges.size());
    for (Edge<A, B> edge : eqEdges)
      eqNodes.add(edge.getEnd());
    return eqNodes;
  }

  private void visit(List<PairList> plists, Node<A, B> beg)
  {
    Deque<Node<A, B>> queue = new ArrayDeque<>();
    queue.add(beg);
    while (!queue.isEmpty()) {
      Node<A, B> node = queue.poll();

      // append any descriptors to the list
      for (PairList plist : plists)
        plist.add(getAtomLabel(node));

      for (Edge<A, B> e : node.getEdges()) {
        if (e.isBeg(node)) {
          queue.add(e.getEnd());
        }
      }
    }
  }

  /**
   * Generates a set of descriptor lists that maintain the like/unlike pairing
   * whilst descriptors are added. The set is navigable and maintains priority
   * ordering when multiple lists are present.
   *
   * @param queue a queue of ligands for which to get descriptors and expand
   * @return navigable set of descriptor lists
   */
  protected Set<PairList> generate(Queue<Edge<A, B>> queue)
  {

    return new HashSet<>();
  }

  /**
   * Reduce the number of combinations by not including terminal ligands in
   * the permuting. They can't be stereocentres and so won't contribute the
   * the like / unlike list.
   *
   * @param edges a list of edges
   * @return a list of non-terminal ligands
   */
  private List<Edge<A, B>> getLigandsToSort(Node<A, B> node, List<Edge<A, B>> edges)
  {
    List<Edge<A, B>> filtered = new ArrayList<Edge<A, B>>();
    for (Edge<A, B> edge : edges) {
      if (edge.isEnd(node) || edge.getEnd().isTerminal())
        continue;
      if (!hasDescriptors(node))
        continue;
      filtered.add(edge);
    }
    return filtered;
  }

  private List<PairList> newPairLists(List<Descriptor> descriptors)
  {
    if (descriptors == null)
      return Collections.emptyList();
    List<PairList> pairs = new ArrayList<PairList>();
    for (Descriptor descriptor : descriptors) {
      pairs.add(new PairList(descriptor));
    }
    return pairs;
  }

  private void fillPairs(Node<A, B> beg, PairList plist)
  {
    Sort<A, B>        sorter = getRefSorter(plist.getRefDescriptor());
    Deque<Node<A, B>> queue  = new ArrayDeque<>();
    queue.add(beg);
    while (!queue.isEmpty()) {
      Node<A, B> node = queue.poll();
      plist.add(getAtomLabel(node));
      List<Edge<A, B>> edges = node.getEdges();
      sorter.prioritise(node, edges);
      for (Edge<A, B> edge : edges) {
        if (edge.isBeg(node) && !edge.getEnd().isTerminal()) {
          queue.add(edge.getEnd());
        }
      }
    }
  }

  private int comparePairs(Node<A, B> a, Node<A, B> b, Descriptor refA, Descriptor refB)
  {
    Sort<A, B>        aSorter = getRefSorter(refA);
    Sort<A, B>        bSorter = getRefSorter(refB);
    Deque<Node<A, B>> aQueue  = new ArrayDeque<>();
    Deque<Node<A, B>> bQueue  = new ArrayDeque<>();
    aQueue.add(a);
    bQueue.add(b);
    while (!aQueue.isEmpty() && !bQueue.isEmpty()) {
      Node<A, B> aNode = aQueue.poll();
      Node<A, B> bNode = bQueue.poll();

      Descriptor desA = getAtomLabel(aNode);
      Descriptor desB = getAtomLabel(bNode);

      desA = PairList.ref(desA);
      desB = PairList.ref(desB);

      if (desA == refA && desB != refB)
        return +1;
      else if (desA != refA && desB == refB)
        return -1;

      List<Edge<A, B>> edges = aNode.getEdges();
      aSorter.prioritise(aNode, edges);
      for (Edge<A, B> edge : edges) {
        if (edge.isBeg(aNode) && !edge.getEnd().isTerminal()) {
          aQueue.add(edge.getEnd());
        }
      }

      edges = bNode.getEdges();
      bSorter.prioritise(bNode, edges);
      for (Edge<A, B> edge : edges) {
        if (edge.isBeg(bNode) && !edge.getEnd().isTerminal()) {
          bQueue.add(edge.getEnd());
        }
      }
    }
    return 0;
  }

  private Sort<A, B> getRefSorter(Descriptor refA)
  {
    List<SequenceRule<A, B>> rules = new ArrayList<>(getSorter().getRules());
    assert rules.remove(this);
    rules.add(new Rule4bNew<A, B>(getMol(), refA));
    return new Sort<A, B>(rules);
  }

  @Override
  public int compare(Edge<A, B> a, Edge<A, B> b)
  {
    if (!a.getBeg().getDigraph().getCurrRoot().equals(a.getBeg()) ||
        !b.getBeg().getDigraph().getCurrRoot().equals(b.getBeg())) {
      if (ref == null)
        return 0;
      Descriptor aDesc = getAtomLabel(a.getEnd());
      Descriptor bDesc = getAtomLabel(b.getEnd());
      if (aDesc != null && bDesc != null && aDesc != Descriptor.None && bDesc != Descriptor.None) {
        boolean alike = PairList.ref(ref) == PairList.ref(aDesc);
        boolean blike = PairList.ref(ref) == PairList.ref(bDesc);
        if (alike && !blike)
          return +1;
        if (blike && !alike)
          return -1;
      }
      return 0;
    } else {
      List<PairList> list1 = newPairLists(getReferenceDescriptors(a.getEnd()));
      List<PairList> list2 = newPairLists(getReferenceDescriptors(b.getEnd()));

      if (list1.isEmpty() != list2.isEmpty())
        throw new InternalError("Ligands should be topologically equivalent!");

      if (list1.size() == 1) {
//        fillPairs(a.getEnd(), list1.get(0));
//        fillPairs(b.getEnd(), list2.get(0));
//        if (a.getBeg().toString().equals("C:23")) {
//          System.err.println(getMol().dumpDigraph(a.getBeg().getDigraph()));
//          System.err.println(a + "=" + list1 + " " + b + "=" + list2 + " : " + list1.get(0).compareTo(list2.get(0)));
//        }
//        return list1.get(0).compareTo(list2.get(0));
        return comparePairs(a.getEnd(), b.getEnd(),
                            list1.get(0).getRefDescriptor(),
                            list2.get(0).getRefDescriptor());
      } else if (list1.size() > 1) {
        for (PairList plist : list1)
          fillPairs(a.getEnd(), plist);
        for (PairList plist : list2)
          fillPairs(b.getEnd(), plist);
        Collections.sort(list1, Collections.reverseOrder());
        Collections.sort(list2, Collections.reverseOrder());
        for (int i = 0; i < list1.size(); i++) {
          int cmp = list1.get(0).compareTo(list2.get(0));
          if (cmp != 0)
            return cmp;
        }
      }
      return 0;
    }
  }
}
