import React from "react";
import {
  Drawer,
  DrawerBody,
  DrawerHeader,
  DrawerOverlay,
  DrawerContent,
  DrawerCloseButton,
  Textarea,
} from "@chakra-ui/react";
import { useAppSelector } from "../hooks";
import {
  useCodeEdition,
  useSetCodeEdition,
} from "../contexts/CodeEditionContext";

export function CodeEditor() {
  const codeEdition = useCodeEdition();
  const setCodeEdition = useSetCodeEdition();
  const defaultValueGetter = () => "";
  const value = useAppSelector(codeEdition?.getValue ?? defaultValueGetter);

  return (
    <>
      <Drawer
        isOpen={codeEdition !== null}
        placement="bottom"
        onClose={() => setCodeEdition(null)}
        size={"xl"}
      >
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>{codeEdition?.label}</DrawerHeader>
          <DrawerBody>
            <Textarea
              value={value}
              onChange={(e) => codeEdition?.onChange?.(e.target.value)}
            />
          </DrawerBody>
        </DrawerContent>
      </Drawer>
    </>
  );
}
