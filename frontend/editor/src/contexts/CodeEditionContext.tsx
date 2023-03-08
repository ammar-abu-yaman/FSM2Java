import React, { createContext, useContext, useState } from "react";
import { CodeEdition } from "../types";

const CodeEditionContext = createContext<CodeEdition | null>(null);
const SetCodeEditionContext = createContext<
  React.Dispatch<React.SetStateAction<CodeEdition | null>>
>(null as unknown as React.Dispatch<React.SetStateAction<CodeEdition | null>>);

export function useCodeEdition() {
  return useContext(CodeEditionContext);
}

export function useSetCodeEdition() {
  return useContext(SetCodeEditionContext);
}

export function CodeEditionProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [codeEdition, setCodeEdition] = useState<CodeEdition | null>(null);

  return (
    <CodeEditionContext.Provider value={codeEdition}>
      <SetCodeEditionContext.Provider value={setCodeEdition}>
        {children}
      </SetCodeEditionContext.Provider>
    </CodeEditionContext.Provider>
  );
}
