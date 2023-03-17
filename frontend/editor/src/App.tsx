import "./style.css";

import React, { useState } from "react";
import Layout from "./components/Layout";
import { FocusedObjectProvider } from "./contexts/FocusedObjectContext";
import {
  MenuButton,
  Text,
  DrawerFooter,
  useDisclosure,
} from "@chakra-ui/react";
import { CodeEditionProvider } from "./contexts/CodeEditionContext";
import { Canvas } from "./components/Canvas";
import { OptionBar } from "./option/OptionBar";
import { CodeEditor } from "./components/CodeEditor";

function App() {
  const [newElement, setNewElement] = useState("");

  return (
    <FocusedObjectProvider>
      <CodeEditionProvider>
        <Layout>
          <Canvas newElement={newElement} setNewElement={setNewElement} />
          <OptionBar />
          <CodeEditor />
        </Layout>
      </CodeEditionProvider>
    </FocusedObjectProvider>
  );
}

export default App;
