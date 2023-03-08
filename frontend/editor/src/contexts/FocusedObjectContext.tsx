import React, { createContext, useContext, useState } from "react";
import { FocusedObject } from "../types";

const FocusedObjectContext = createContext<FocusedObject | null>(null);
const SetFocusedObjectContext = createContext<
  React.Dispatch<React.SetStateAction<FocusedObject | null>>
>(
  null as unknown as React.Dispatch<React.SetStateAction<FocusedObject | null>>
);

export function useFocusedObject() {
  return useContext(FocusedObjectContext);
}

export function useSetFocusedObject() {
  return useContext(SetFocusedObjectContext);
}

export function FocusedObjectProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [focusedObject, setFocusedObject] = useState<FocusedObject | null>(
    null
  );

  return (
    <FocusedObjectContext.Provider value={focusedObject}>
      <SetFocusedObjectContext.Provider value={setFocusedObject}>
        {children}
      </SetFocusedObjectContext.Provider>
    </FocusedObjectContext.Provider>
  );
}
