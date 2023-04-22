import "./style.css";

import React from "react";
import Layout from "./components/Layout";
import { Canvas } from "./components/Canvas";
import { OptionBar } from "./option/OptionBar";
import { CodeEditor } from "./components/CodeEditor";

function App() {
  return (
    <Layout>
      <Canvas />
      <OptionBar />
      <CodeEditor />
    </Layout>
  );
}

export default App;
