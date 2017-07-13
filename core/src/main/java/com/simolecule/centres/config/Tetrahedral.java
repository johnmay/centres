package com.simolecule.centres.config;

import com.simolecule.centres.BaseMol;
import com.simolecule.centres.Descriptor;
import com.simolecule.centres.Digraph;
import com.simolecule.centres.Edge;
import com.simolecule.centres.Node;
import com.simolecule.centres.rules.Priority;
import com.simolecule.centres.rules.SequenceRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tetrahedral<A, B> extends Configuration<A, B> {

  public static final int LEFT  = 0x1;
  public static final int RIGHT = 0x2;

  public Tetrahedral(A focus, A[] carriers, int cfg)
  {
    super(focus, carriers, cfg);
  }

  @Override
  public void setPrimaryLabel(BaseMol<A, B> mol, Descriptor desc)
  {
    mol.setAtomProp(getFocus(), BaseMol.CIP_LABEL_KEY, desc);
  }

  @Override
  public Descriptor label(SequenceRule<A, B> comp)
  {
    Digraph<A, B> digraph = getDigraph();
    Node<A, B>    root    = digraph.getRoot();
    if (root == null)
      root = digraph.init(getFocus());
    else
      digraph.changeRoot(digraph.getRoot());

    return label(root, comp);
  }

  private Descriptor label(Node<A, B> node, SequenceRule<A, B> comp)
  {
    final List<Edge<A, B>> edges = new ArrayList<>(node.getEdges());

    // something not right!?! bad creation
    if (edges.size() < 3)
      return Descriptor.None;

    Priority priority = comp.sort(node, edges);

    if (!priority.isUnique())
      return Descriptor.Unknown;

    Object[] ordered = new Object[4];
    int      idx     = 0;
    for (Edge<A, B> edge : edges) {
      if (edge.getEnd().isSet(Node.BOND_DUPLICATE) ||
          edge.getEnd().isSet(Node.IMPL_HYDROGEN))
        continue;
      ordered[idx++] = edge.getEnd().getAtom();
    }
    if (idx < 4)
      ordered[idx] = getFocus();

    int parity = parity4(ordered, getCarriers());

    if (parity == 0)
      throw new RuntimeException("Could not calculate parity! Carrier mismatch");

    int config = this.getConfig();
    if (parity == 1)
      config ^= 0x3;

    if (config == 0x1) {
      if (priority.isPseduoAsymettric())
        return Descriptor.s;
      else
        return Descriptor.S;
    } else if (config == 0x2) {
      if (priority.isPseduoAsymettric())
        return Descriptor.r;
      else
        return Descriptor.R;
    }

    return Descriptor.Unknown;
  }

  @Override
  public void labelAux(Map<Node<A, B>, Descriptor> map,
                       Digraph<A, B> digraph,
                       SequenceRule<A, B> comp)
  {
    A                focus = getFocus();
    List<Node<A, B>> nodes = digraph.getNodes(focus);
    for (Node<A, B> node : nodes) {
      if (map.containsKey(node))
        continue;
      digraph.changeRoot(node);
      Descriptor label = label(node, comp);
      if (label != Descriptor.Unknown)
        map.put(node, label);
    }
  }
}
