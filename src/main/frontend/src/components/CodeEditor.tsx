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
import { useAppDispatch, useAppSelector } from "../hooks";
import { setCodeEdition } from "../reducers/SettingsReducer";

export function CodeEditor() {
  const dispatch = useAppDispatch();
  const codeEdition = useAppSelector((data) => data.settings.codeEdition);
  const defaultValueGetter = () => "";
  const value = useAppSelector(codeEdition?.getValue ?? defaultValueGetter);

  return (
    <>
      <Drawer
        isOpen={codeEdition as any}
        placement="bottom"
        onClose={() => dispatch(setCodeEdition(null))}
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
