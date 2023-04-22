import React from "react";
import { Flex } from "@chakra-ui/react";
import { DefaultOptionBarContent } from "./DefaultOptionBarContent";
import { useAppSelector } from "../hooks";

import { StateOptionBarContent } from "../option/StateOptionBarContent";
import { FocusedObject } from "../types";
import { TransitionOptionBarContent } from "./TransitionOptionBarContent";

export function OptionBar() {
  const focusedObject = useAppSelector((data) => data.settings.focusedObject);
  return (
    <Flex
      minWidth={300}
      bg="gray.800"
      borderRadius={12}
      borderColor="gray.50"
      m="1"
    >
      <OptionBarContent focusedObject={focusedObject} />
    </Flex>
  );
}

export enum OptionBarVariant {
  State,
  DefaultState,
  Transition,
}

export function OptionBarContent({
  focusedObject,
}: {
  focusedObject?: FocusedObject;
}): JSX.Element {
  switch (focusedObject?.optionbar) {
    case OptionBarVariant.State:
      return <StateOptionBarContent stateId={focusedObject.id} />;
    case OptionBarVariant.Transition:
      return <TransitionOptionBarContent transitionId={focusedObject.id} />;
    default:
      return <DefaultOptionBarContent />;
  }
}
