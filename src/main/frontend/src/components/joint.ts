import * as joint from "jointjs";
import { CustomLinkView } from "../views/TransitionView";

const namespace = joint.shapes;

export const graph = new joint.dia.Graph({}, { cellNamespace: namespace });

export let paper: joint.dia.Paper = null as any as joint.dia.Paper;

export function initPaper(el: any) {
  paper = new joint.dia.Paper({
    el,
    model: graph,
    width: "100%",
    height: "1000",
    cellViewNamespace: namespace,
    defaultLink: () =>
      new joint.shapes.standard.Link({
        linkView: CustomLinkView,
        router: joint.routers.orthogonal,
        connector: joint.connectors.rounded,
      }),
    linkView: () => CustomLinkView,

    // gridSize: 10,
    // drawGrid: { name: "mesh" },
    background: { color: "#383c4a" },
  } as any);
}
