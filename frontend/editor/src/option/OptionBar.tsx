import React from "react";
import { useFocusedObject } from "../contexts/FocusedObjectContext";
import { Flex } from "@chakra-ui/react";
import { DefaultOptionBarContent } from "./DefaultOptionBarContent";

export function OptionBar() {
  const focusedObject = useFocusedObject();
  const optionBarContent =
    focusedObject?.optionBarContent ?? DefaultOptionBarContent;
  return <Flex minWidth={300}>{optionBarContent()}</Flex>;
}
