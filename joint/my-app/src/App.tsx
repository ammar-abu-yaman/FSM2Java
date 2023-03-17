import React, { useEffect, useRef } from "react";
import "./style.css";
import * as joint from "jointjs";

function App() {
  const ref = useRef();

  useEffect(() => {
    if (!ref.current) return;
    const holder = ref.current;
    const namespace = joint.shapes;

    const graph = new joint.dia.Graph({}, { cellNamespace: namespace });

    const paper = new joint.dia.Paper({
      el: holder,
      model: graph,
      width: 1000,
      height: 1000,
      gridSize: 1,
      cellViewNamespace: namespace,
      drawGrid: true,
      background: {
        color: `rgba(0, 0, 255, 0.3)`,
      },
    } as any);

    const rect = new joint.shapes.standard.Rectangle();
    console.log(rect);
    rect.attr({
      onclick(evt: any) {
        console.log(evt);
      },
    });
    rect.position(100, 30);
    rect.resize(100, 40);
    rect.attr({
      body: {
        class: "x",
      },
      label: {
        text: "Hello",
        color: "blue",
        class: "y",
      },
    });
    rect.addTo(graph);

    const rect2 = rect.clone();
    rect2.translate(300, 0);
    rect2.attr("label/text", "World!");
    rect2.addTo(graph);
    (paper.findViewByModel(rect2) as any).on(
      "element:pointerclick",
      (evt: any) => console.log(evt)
    );

    const link = new joint.shapes.standard.Link();
    link.source(rect);
    link.target(rect2);
    link.router(joint.routers.orthogonal);
    link.connector(joint.connectors.rounded);
    link.addTo(graph);

    const linkView = link.findView(paper);
    const toolsView = new joint.dia.ToolsView({
      tools: [
        new joint.linkTools.SourceArrowhead(),
        new joint.linkTools.TargetArrowhead(),
      ],
    });

    linkView.addTools(toolsView);
  }, [ref.current]);

  return (
    <div ref={ref as any} className="App">
      <div className="holder" />
    </div>
  );
}

export default App;
